//package com.turingit.drivingLicense.service.impl;
////保存照片基本信息，调用百度识图。
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.sun.org.apache.xml.internal.security.utils.Base64;
//import com.turingit.drivingLicense.ImageProcessing.JsonToList;
//import com.turingit.drivingLicense.baiduClass.DisposeReturn;
//import com.turingit.drivingLicense.mapper.SavePhotoMapper;
//import com.turingit.drivingLicense.mapper.SummarizingMapper;
//import com.turingit.drivingLicense.pojo.ImageData;
//import com.turingit.drivingLicense.pojo.OCR;
//import com.turingit.drivingLicense.pojo.Summarizing;
//import com.turingit.drivingLicense.service.SavePhotoService_old;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Service
//public class SavePhotoServiceImpl_old implements SavePhotoService_old {
//
//    @Autowired
//    private SavePhotoMapper savePhotoMapper;
//
//    @Autowired
//    private SummarizingMapper summarizingMapper;
//
//    private static final String strUrl = "http://192.168.10.7:8080/OcrWeb/servlet/PageOcrServlet";
//
//    @Override
//    public String updataData(Long id) {
//        ImageData imageData = null;
//        Summarizing summarizing = new Summarizing();
//
//        try{
//            imageData = savePhotoMapper.selectById(id);
//        }catch (Exception e){
//            System.out.println(e);
//            return null;
//        }
//
////        if (imageData.getAbnormalImage()!=null){
////            System.out.println("图片已被处理");
////            return "图片已被处理";
////        }
//
//        summarizing.setCheckid(imageData.getCheckid());
//
//        imageData.setAbnormalImage("图片处理完成");
//
//        String a = imageData.getImgPath();
//        a = a.replaceAll(" ", "_");
//        a = a.replaceAll(":", "_");
//        imageData.setImgPath(a);
//        //文件路径
//        String fileUrl = "\\\\192.168.10.108\\CheckData\\images\\"+imageData.getImgPath();
////        String fileUrl = "D:\\photo\\"+imageData.getImgPath();
//        fileUrl = fileUrl.replaceAll("/", "\\\\");
//
//        //文件类型
//        String fileName = imageData.getTypename();
//        if (fileName.equals("行驶证")){
//            summarizing.setImgPath(imageData.getImgPath());
//            String strpid = "5"; //pid
//
//            File file = new File(fileUrl);
//            FileInputStream is;
//            String readByGet = null;
//            try {
//                is = new FileInputStream(file);
//                byte[] data = new byte[is.available()];
//                is.read(data);
//                String base64file = Base64.encode(data);
//                String params = "filedata="+ URLEncoder.encode(base64file, "utf-8");
//
//                //请求参数
//                params+="&pid="+URLEncoder.encode(strpid, "utf-8");
//
//                long startTime=System.currentTimeMillis();
//
//                readByGet = readByPOST(strUrl, params);
//
//                long endTime=System.currentTimeMillis();
//                System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
//
////                System.out.println(readByGet);
//            }catch (Exception e){
//                String s = e.toString();
//                if (s.contains("Connection refused: connect")){
//                    return "OCR";
//                }else if (s.contains("系统找不到指定的路径")){
//                    imageData.setAbnormalImage("系统找不到指定的路径");
//                    savePhotoMapper.updateById(imageData);
//                    return "系统找不到指定的路径";
//                }else if (s.contains("找不到网络名")){
//                    return "NET";
//                }
//            }
//
//            try {
//                //处理返回值
//                JSONObject jo = new JSONObject(new String(readByGet));
//                Object result = jo.get("PageInfo");
//
//                String str = result.toString();
//                str=str.substring(1);
//                str.substring(0,str.length()-1);
//
//                jo = new JSONObject(new String(str.toString()));
//                result = jo.get("Result");
//
//                str = result.toString();
//                str=str.substring(1);
//                str.substring(0,str.length()-1);
//
//                jo = new JSONObject(new String(str.toString()));
//                result = jo.get("ResultList");
//
//                ArrayList<OCR> ocrs = JsonToList.OCRArray(result.toString());
//
//                summarizing = DisposeReturn.carCertificates(ocrs,summarizing);
//
//                if (summarizing.getLicencePlate()!=null&&summarizing.getLicencePlate().length()!=7){
//                    imageData.setAbnormalImage("识别不全");
//                }
//                if (summarizing.getTLicencePlate()!=null&&summarizing.getTLicencePlate().length()!=7){
//                    imageData.setAbnormalImage("识别不全");
//                }
//
//
//                //提交至数据库
//                //保存imageData表数据
//
//                savePhotoMapper.updateById(imageData);
//                QueryWrapper<Summarizing> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("checkid",imageData.getCheckid());
//                List<Summarizing> summarizings = summarizingMapper.selectList(queryWrapper);
//                if (summarizings.size()==0){
//                    summarizingMapper.insert(summarizing);
//                }else {
//                    UpdateWrapper<Summarizing> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("checkid",imageData.getCheckid());
//                    summarizingMapper.update(summarizing,updateWrapper);
//                }
//            }catch (Exception e){
//                imageData.setAbnormalImage("图片有误");
//                savePhotoMapper.updateById(imageData);
////                System.out.println(e);
//            }
//        }else if (fileName.equals("车头照")||fileName.equals("车尾照")){
//            String strpid = "6"; //pid
//            File file = new File(fileUrl);
//            FileInputStream is;
//            String readByGet = null;
//            try {
//                is = new FileInputStream(file);
//                byte[] data = new byte[is.available()];
//                is.read(data);
//                String base64file = Base64.encode(data);
//                String params = "filedata="+ URLEncoder.encode(base64file, "utf-8");
//
//                //请求参数
//                params+="&pid="+URLEncoder.encode(strpid, "utf-8");
//                readByGet = readByPOST(strUrl, params);
//
//            }catch (Exception e){
//                String s = e.toString();
//                if (s.contains("Connection refused: connect")){
//                    return "OCR";
//                }else if (s.contains("系统找不到指定的路径")){
//                    imageData.setAbnormalImage("系统找不到指定的路径");
//                    savePhotoMapper.updateById(imageData);
//                    return "系统找不到指定的路径";
//                }else if (s.contains("找不到网络名")){
//                    return "NET";
//                }
//            }
//
//            try {
//                //处理数据
//                JSONObject jo = new JSONObject(new String(readByGet));
//                Object result = jo.get("PageInfo");
//
//                String str = result.toString();
//                str=str.substring(1);
//                str.substring(0,str.length()-1);
//
//                jo = new JSONObject(new String(str.toString()));
//                result = jo.get("Result");
//
//                str = result.toString();
//                str=str.substring(1);
//                str.substring(0,str.length()-1);
//
//                jo = new JSONObject(new String(str.toString()));
//                result = jo.get("Plate");
//
//                str = result.toString();
//                str=str.substring(1);
//                str.substring(0,str.length()-1);
//
//                jo = new JSONObject(new String(str.toString()));
//                Object vehicleid = jo.get("车牌号");
//
//                //存放入实体类
//                if (fileName.equals("车头照")){
//                    summarizing.setTractionVehicleid(vehicleid.toString());
//                }else{
//                    summarizing.setTrailerVehicleid(vehicleid.toString());
//                }
//
//                //判断车牌是否过长
//                if (summarizing.getTrailerVehicleid()!=null&&summarizing.getTrailerVehicleid().length()!=7){
//                    imageData.setAbnormalImage("识别不全");
//                }
//                if (summarizing.getTractionVehicleid()!=null&&summarizing.getTractionVehicleid().length()!=7){
//                    imageData.setAbnormalImage("识别不全");
//                }
//
//                savePhotoMapper.updateById(imageData);
//
//                QueryWrapper<Summarizing> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("checkid",imageData.getCheckid());
//                List<Summarizing> summarizings = summarizingMapper.selectList(queryWrapper);
//                if (summarizings.size()==0){
//                    summarizingMapper.insert(summarizing);
//                }else {
//                    UpdateWrapper<Summarizing> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("checkid",imageData.getCheckid());
//                    summarizingMapper.update(summarizing,updateWrapper);
//                }
//
//            }catch (Exception e){
//                imageData.setAbnormalImage("图片有误");
//                savePhotoMapper.updateById(imageData);
//            }
//        }else {
//            try {
////                imageData.setImgUrl(fileUrl);
//                savePhotoMapper.updateById(imageData);
//            }catch (Exception e){
//                imageData.setAbnormalImage("图片有误");
//                savePhotoMapper.updateById(imageData);
//            }
//        }
//
//        return "保存成功";
//    }
//
//    //根据时间段获取图片ID
//    @Override
//    public List<Long> getIdList(String st, String et){
//
////        System.out.println("起始时间为："+st);
////        System.out.println("结束时间为："+et);
//
//        st=st+" 00:00:00";
//        et=et+" 00:00:00";
//
//        QueryWrapper<ImageData> imageDataQueryWrapper = new QueryWrapper<>();
////        //ge：>= 两个参数： 数据库中的列名，比对的值,
////        checkdataQueryWrapper.ge("checktime","2020-05-06 00:00:00");
////        //le: <= 两个参数： 数据库中的列名，比对的值
////        checkdataQueryWrapper.le("checktime","2020-05-07 00:00:00");
//        imageDataQueryWrapper.select("id");
//        imageDataQueryWrapper.between("checktime",st,et);
//        imageDataQueryWrapper.isNull("abnormal_image")
////        imageDataQueryWrapper.eq("abnormal_image","识别不全")
//                .and(wrapper -> wrapper.eq("typename","行驶证")
//                        .or().eq("typename","车头照")
//                        .or().eq("typename","车尾照"));
//        imageDataQueryWrapper.orderByAsc("checktime");
//        List<ImageData> imageDataList=savePhotoMapper.selectList(imageDataQueryWrapper);
//        List<Long> idList = new ArrayList();
//        for (ImageData imageData : imageDataList){
//            idList.add(imageData.getId());
//        }
//        return idList;
//    }
//
//    //根据ID查询记录信息
//    @Override
//    public Summarizing getImageData(int id) {
//        ImageData imageData = savePhotoMapper.selectById(id);
//        QueryWrapper<Summarizing> summarizingQueryWrapper = new QueryWrapper<>();
//        summarizingQueryWrapper.eq("checkid",imageData.getCheckid());
//        List<Summarizing> summarizings = summarizingMapper.selectList(summarizingQueryWrapper);
//        Summarizing summarizing = summarizings.get(0);
//        summarizing.setId((long)id);
//        summarizing.setAddress(imageData.getImgPath());
//        return summarizing;
//    }
//
//    @Override
//    public void updateImage(Summarizing summarizing) {
//        ImageData imageData= new ImageData();
//        imageData.setId(summarizing.getId());
//        imageData.setAbnormalImage("图片处理完成");
//        savePhotoMapper.updateById(imageData);
//        summarizing.setId(null);
//        UpdateWrapper<Summarizing> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("checkid",summarizing.getCheckid());
//        summarizingMapper.update(summarizing,updateWrapper);
//
//    }
//
//    @Override
//    public void updateImageNo(Long id) {
//        ImageData imageData = new ImageData();
//        imageData.setId(id);
//        imageData.setAbnormalImage("图片无法识别");
//        savePhotoMapper.updateById(imageData);
//    }
//
//    @Override
//    public Integer getSun() {
//        QueryWrapper<ImageData> imageDataQueryWrapper=new QueryWrapper<>();
//        imageDataQueryWrapper.eq("abnormal_image", "图片有误")
//                .or().eq("abnormal_image", "识别不全");
//        Integer integer = savePhotoMapper.selectCount(imageDataQueryWrapper);
//        if (integer%10==0){
//            integer=integer/10;
//        }else {
//            integer=integer/10+1;
//        }
//        return integer;
//    }
//
//    //获取错误信息
//    @Override
//    public List<ImageData> getError(Long id) {
//        List<ImageData> imageDataList = savePhotoMapper.selectError(id);
////        Page<ImageData> page = new Page<>(1, 10);
////        QueryWrapper<ImageData> imageDataQueryWrapper=new QueryWrapper<>();
////        imageDataQueryWrapper.select("id","typename","abnormal_image","typeid","img_path");
////        imageDataQueryWrapper.ne("abnormal_image", "图片处理完成");
////        imageDataQueryWrapper.gt("id",id);
////        imageDataQueryWrapper.lt("id",id+200);
////        imageDataQueryWrapper.orderByAsc("id");
////        Page<ImageData> imageDataPage = savePhotoMapper.selectPage(page, imageDataQueryWrapper);
////        List<ImageData> imageDataList = imageDataPage.getRecords();
//////        List<ImageData> imageDataList=savePhotoMapper.selectList(imageDataQueryWrapper);
//        for (ImageData imageData : imageDataList){
//            if (imageData.getAbnormalImage().equals("图片有误")){
//                imageData.setAbnormalImage("图片识别失败");
//            }
//        }
//        return imageDataList;
//    }
//
//    @Override
//    public List<ImageData> getErrorUp(Long id) {
//        List<ImageData> imageDataList = savePhotoMapper.selectErrorUp(id);
////        Page<ImageData> page = new Page<>(1, 10);
////        QueryWrapper<ImageData> imageDataQueryWrapper=new QueryWrapper<>();
////        imageDataQueryWrapper.select("id","typename","abnormal_image","typeid","img_path");
////        imageDataQueryWrapper.ne("abnormal_image", "图片处理完成");
////        imageDataQueryWrapper.lt("id",id);
////        imageDataQueryWrapper.orderByDesc("id");
////        Page<ImageData> imageDataPage = savePhotoMapper.selectPage(page, imageDataQueryWrapper);
////        List<ImageData> imageDataList = imageDataPage.getRecords();
//        for (ImageData imageData : imageDataList){
//            if (imageData.getAbnormalImage().equals("图片有误")){
//                imageData.setAbnormalImage("图片识别失败");
//            }
//        }
//        Collections.reverse(imageDataList);
//        return imageDataList;
//    }
//
//    //智讯识别方法(不能动)
//    private static String readByPOST(String inUrl, String params)throws IOException {
//        StringBuffer sbf = new StringBuffer();
//        String strRead = null;
//        URL url = new URL(inUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        connection.setRequestMethod("POST");
//        connection.setDoInput(true);
//        connection.setDoOutput(true);
//        connection.connect();
//        PrintWriter out = new PrintWriter(connection.getOutputStream());
//        out.print(params);
//        out.flush();
//        InputStream is = connection.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
//        while ((strRead = reader.readLine()) != null) {
//            sbf.append(strRead);
//            sbf.append("\r\n");
//        }
//        reader.close();
//        connection.disconnect();
//        return sbf.toString();
//    }
//}