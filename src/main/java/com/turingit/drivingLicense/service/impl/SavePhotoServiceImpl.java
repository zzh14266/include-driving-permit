package com.turingit.drivingLicense.service.impl;
//保存照片基本信息，调用百度识图。
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.turingit.drivingLicense.ImageProcessing.JsonToList;
import com.turingit.drivingLicense.baiduClass.DisposeReturn;
import com.turingit.drivingLicense.mapper.SavePhotoMapper;
import com.turingit.drivingLicense.mapper.SummarizingMapper;
import com.turingit.drivingLicense.pojo.ImageData;
import com.turingit.drivingLicense.pojo.OCR;
import com.turingit.drivingLicense.pojo.Summarizing;
import com.turingit.drivingLicense.service.SavePhotoService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SavePhotoServiceImpl implements SavePhotoService {


    @Autowired
    private SavePhotoMapper savePhotoMapper;

    @Autowired
    private SummarizingMapper summarizingMapper;

    private static final String strUrl = "http://192.168.10.7:8080/OcrWeb/servlet/PageOcrServlet";

    //根据时间段获取图片ID
    @Override
    public String getIdList(String st, String et){
        String bc="保存成功";

        List<ImageData> imageDataList = savePhotoMapper.selectTime(st, et);

//        QueryWrapper<ImageData> imageDataQueryWrapper = new QueryWrapper<>();
//        imageDataQueryWrapper.select("id","abnormal_image","checkid","img_path","typename","checktime");
//        imageDataQueryWrapper.between("checktime",st,et);
//        imageDataQueryWrapper.isNull("abnormal_image")
//                .and(wrapper -> wrapper.eq("typename","行驶证")
//                        .or().eq("typename","车头照")
//                        .or().eq("typename","车尾照"));
//        imageDataQueryWrapper.orderByAsc("checktime");

//        List<ImageData> imageDataList=savePhotoMapper.selectList(imageDataQueryWrapper);
        List<Summarizing> summarizingList=new ArrayList<>();
        Long jd = 0L;
        for (ImageData imageData:imageDataList){

            jd+=1;
            System.out.println(imageData.getId()+":"+jd+"/"+imageDataList.size()+"处理时间为："+imageData.getChecktime());

            Summarizing summarizing = new Summarizing();

            if (imageData.getAbnormalImage()!=null){
                System.out.println("图片已被处理");
                continue;
            }

            summarizing.setCheckid(imageData.getCheckid());

            imageData.setAbnormalImage("图片处理完成");

            String a = imageData.getImgPath();
            a = a.replaceAll(" ", "_");
            a = a.replaceAll(":", "_");
            imageData.setImgPath(a);
            //文件路径
            String fileUrl = "\\\\192.168.10.108\\CheckData\\images\\"+imageData.getImgPath();
            fileUrl = fileUrl.replaceAll("/", "\\\\");

            //文件类型
            String fileName = imageData.getTypename();
            if (fileName.equals("行驶证")){
                summarizing.setImgPath(imageData.getImgPath());
                String strpid = "5"; //pid

                File file = new File(fileUrl);
                FileInputStream is;
                String readByGet = null;
                try {
                    is = new FileInputStream(file);
                    byte[] data = new byte[is.available()];
                    is.read(data);
                    String base64file = Base64.encode(data);
                    String params = "filedata="+ URLEncoder.encode(base64file, "utf-8");

                    //请求参数
                    params+="&pid="+URLEncoder.encode(strpid, "utf-8");
                    readByGet = readByPOST(strUrl, params);
//                System.out.println(readByGet);
                }catch (Exception e){
                    String s = e.toString();
                    if (s.contains("Connection refused: connect")){
                        System.out.println("图像识别接口出错，请检查OCR服务。");
                        break;
                    }else if (s.contains("系统找不到指定的路径")){
                        imageData.setAbnormalImage("系统找不到指定的路径");
                        continue;
                    }else if (s.contains("找不到网络名")){
                        System.out.println("文件共享异常，请检查是否开启文件共享。");
                        break;
                    }
                }

                try {
                    //处理返回值
                    JSONObject jo = new JSONObject(new String(readByGet));
                    Object result = jo.get("PageInfo");

                    String str = result.toString();
                    str=str.substring(1);
                    str.substring(0,str.length()-1);

                    jo = new JSONObject(new String(str.toString()));
                    result = jo.get("Result");

                    str = result.toString();
                    str=str.substring(1);
                    str.substring(0,str.length()-1);

                    jo = new JSONObject(new String(str.toString()));
                    result = jo.get("ResultList");

                    ArrayList<OCR> ocrs = JsonToList.OCRArray(result.toString());

                    summarizing = DisposeReturn.carCertificates(ocrs,summarizing);

                    if (summarizing.getLicencePlate()!=null&&summarizing.getLicencePlate().length()!=7){
                        imageData.setAbnormalImage("识别不全");
                    }
                    if (summarizing.getTLicencePlate()!=null&&summarizing.getTLicencePlate().length()!=7){
                        imageData.setAbnormalImage("识别不全");
                    }
                }catch (Exception e){
                    imageData.setAbnormalImage("图片有误");
                }
            }else if (fileName.equals("车头照")||fileName.equals("车尾照")){
                String strpid = "6"; //pid
                File file = new File(fileUrl);
                FileInputStream is;
                String readByGet = null;
                try {
                    is = new FileInputStream(file);
                    byte[] data = new byte[is.available()];
                    is.read(data);
                    String base64file = Base64.encode(data);
                    String params = "filedata="+ URLEncoder.encode(base64file, "utf-8");

                    //请求参数
                    params+="&pid="+URLEncoder.encode(strpid, "utf-8");
                    readByGet = readByPOST(strUrl, params);

                }catch (Exception e){
                    String s = e.toString();
                    if (s.contains("Connection refused: connect")){
                        System.out.println("图像识别接口出错，请检查OCR服务。");
                        bc="图像识别接口出错，请检查OCR服务。";
                        break;
                    }else if (s.contains("系统找不到指定的路径")){
                        imageData.setAbnormalImage("系统找不到指定的路径");
                        continue;
                    }else if (s.contains("找不到网络名")){
                        System.out.println("文件共享异常，请检查是否开启文件共享。");
                        bc="文件共享异常，请检查是否开启文件共享。";
                        break;
                    }
                }

                try {

                    //处理数据
                    JSONObject jo = new JSONObject(new String(readByGet));
                    Object result = jo.get("PageInfo");

                    String str = result.toString();
                    str=str.substring(1);
                    str.substring(0,str.length()-1);

                    jo = new JSONObject(new String(str.toString()));
                    result = jo.get("Result");

                    str = result.toString();
                    str=str.substring(1);
                    str.substring(0,str.length()-1);

                    jo = new JSONObject(new String(str.toString()));
                    result = jo.get("Plate");

                    str = result.toString();
                    str=str.substring(1);
                    str.substring(0,str.length()-1);

                    jo = new JSONObject(new String(str.toString()));
                    Object vehicleid = jo.get("车牌号");

                    //存放入实体类
                    if (fileName.equals("车头照")){
                        summarizing.setTractionVehicleid(vehicleid.toString());
                    }else{
                        summarizing.setTrailerVehicleid(vehicleid.toString());
                    }

                    //判断车牌是否过长
                    if (summarizing.getTrailerVehicleid()!=null&&summarizing.getTrailerVehicleid().length()!=7){
                        imageData.setAbnormalImage("识别不全");
                    }
                    if (summarizing.getTractionVehicleid()!=null&&summarizing.getTractionVehicleid().length()!=7){
                        imageData.setAbnormalImage("识别不全");
                    }
                }catch (Exception e){
                    imageData.setAbnormalImage("图片有误");
                }
            }
            if(imageData.getAbnormalImage().equals("图片有误")){
                continue;
            }else summarizingList.add(summarizing);

            //减少消耗
            imageData.setCheckid(null).setImgPath(null).setTypename(null);
        }

        //存数据
        savePhotoMapper.updateImage(imageDataList);
        summarizingMapper.updateSummar(summarizingList);

        //输出当前时间和处理日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String formatDate = sdf.format(date);
        System.out.println("处理时间段为："+st+"~"+et+",识别完成,当前时间为:" + formatDate);
        return bc;
    }

    //根据ID查询记录信息
    @Override
    public Summarizing getImageData(int id) {
        ImageData imageData = savePhotoMapper.selectById(id);
        QueryWrapper<Summarizing> summarizingQueryWrapper = new QueryWrapper<>();
        summarizingQueryWrapper.eq("checkid",imageData.getCheckid());
        List<Summarizing> summarizings = summarizingMapper.selectList(summarizingQueryWrapper);
        Summarizing summarizing = summarizings.get(0);
        summarizing.setId((long)id);
        summarizing.setAddress(imageData.getImgPath());
        return summarizing;
    }

    @Override
    public void updateImage(Summarizing summarizing) {
        ImageData imageData= new ImageData();
        imageData.setId(summarizing.getId());
        imageData.setAbnormalImage("图片处理完成");
        savePhotoMapper.updateById(imageData);
        summarizing.setId(null);
        UpdateWrapper<Summarizing> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("checkid",summarizing.getCheckid());
        summarizingMapper.update(summarizing,updateWrapper);

    }

    @Override
    public void updateImageNo(Long id) {
        ImageData imageData = new ImageData();
        imageData.setId(id);
        imageData.setAbnormalImage("图片无法识别");
        savePhotoMapper.updateById(imageData);
    }

    @Override
    public Integer getSun() {
        QueryWrapper<ImageData> imageDataQueryWrapper=new QueryWrapper<>();
        imageDataQueryWrapper.eq("abnormal_image", "图片有误")
                .or().eq("abnormal_image", "识别不全");
        Integer integer = savePhotoMapper.selectCount(imageDataQueryWrapper);
        if (integer%10==0){
            integer=integer/10;
        }else {
            integer=integer/10+1;
        }
        return integer;
    }

    //获取错误信息
    @Override
    public List<ImageData> getError(Long id) {
        List<ImageData> imageDataList = savePhotoMapper.selectError(id);
//        Page<ImageData> page = new Page<>(1, 10);
//        QueryWrapper<ImageData> imageDataQueryWrapper=new QueryWrapper<>();
//        imageDataQueryWrapper.select("id","typename","abnormal_image","typeid","img_path");
//        imageDataQueryWrapper.ne("abnormal_image", "图片处理完成");
//        imageDataQueryWrapper.gt("id",id);
//        imageDataQueryWrapper.lt("id",id+200);
//        imageDataQueryWrapper.orderByAsc("id");
//        Page<ImageData> imageDataPage = savePhotoMapper.selectPage(page, imageDataQueryWrapper);
//        List<ImageData> imageDataList = imageDataPage.getRecords();
////        List<ImageData> imageDataList=savePhotoMapper.selectList(imageDataQueryWrapper);
        for (ImageData imageData : imageDataList){
            if (imageData.getAbnormalImage().equals("图片有误")){
                imageData.setAbnormalImage("图片识别失败");
            }
        }
        return imageDataList;
    }

    @Override
    public List<ImageData> getErrorUp(Long id) {
        List<ImageData> imageDataList = savePhotoMapper.selectErrorUp(id);
//        Page<ImageData> page = new Page<>(1, 10);
//        QueryWrapper<ImageData> imageDataQueryWrapper=new QueryWrapper<>();
//        imageDataQueryWrapper.select("id","typename","abnormal_image","typeid","img_path");
//        imageDataQueryWrapper.ne("abnormal_image", "图片处理完成");
//        imageDataQueryWrapper.lt("id",id);
//        imageDataQueryWrapper.orderByDesc("id");
//        Page<ImageData> imageDataPage = savePhotoMapper.selectPage(page, imageDataQueryWrapper);
//        List<ImageData> imageDataList = imageDataPage.getRecords();
        for (ImageData imageData : imageDataList){
            if (imageData.getAbnormalImage().equals("图片有误")){
                imageData.setAbnormalImage("图片识别失败");
            }
        }
        Collections.reverse(imageDataList);
        return imageDataList;
    }

    @Override
    public String mq() {
        return savePhotoMapper.mq();
    }

    //智讯识别方法(不能动)
    private static String readByPOST(String inUrl, String params)throws IOException {
        StringBuffer sbf = new StringBuffer();
        String strRead = null;
        URL url = new URL(inUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        PrintWriter out = new PrintWriter(connection.getOutputStream());
        out.print(params);
        out.flush();
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        while ((strRead = reader.readLine()) != null) {
            sbf.append(strRead);
            sbf.append("\r\n");
        }
        reader.close();
        connection.disconnect();
        return sbf.toString();
    }
}