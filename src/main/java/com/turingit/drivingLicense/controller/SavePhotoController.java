package com.turingit.drivingLicense.controller;

import com.turingit.drivingLicense.pojo.ImageData;
import com.turingit.drivingLicense.pojo.Summarizing;
import com.turingit.drivingLicense.service.SavePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class SavePhotoController {
    @Autowired
    private SavePhotoService savePhotoService;

    @RequestMapping("mq")
    public String mq(){
        return savePhotoService.mq();
    }

    //处理某一日的图片
    @RequestMapping("SavePhotoUrl")
    public String savePhotoUrl(String st ,String et,int id) throws ParseException {
        return getString(st, et);
    }

    //emm怎么说好呢。。代码重复封装。
//    private String getString(String st, String et) throws ParseException {
//        String st2 = date(st,1);
//        String res = savePhotoService.getIdList(st, st2);
//        if (!res.equals("保存成功")) return res;
//
//        for (int i = 1; i < 100; i++) {
//            st = date(st,10);
//
//            //比较时间
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            Date sd1=df.parse(st);
//            Date sd2=df.parse(et);
//
//            if (sd1.before(sd2)){
//                st2 = date(st,1);
//                res = savePhotoService.getIdList(st, st2);
//                if (!res.equals("保存成功")) return res;
//            }else break;
//        }
//        return res;
//    }

    //emm怎么说好呢。。代码重复封装。
    private String getString(String st, String et) throws ParseException {
        String a=st;
        String st2 = time(st,1);
        String res = savePhotoService.getIdList(st, st2);
        if (!res.equals("保存成功")) return res;

        while (true){
            st = time(st,1);

            //比较时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date sd1=df.parse(st);
            Date sd2=df.parse(et);

            if (sd1.before(sd2)){
                st2 = time(st,1);
                res = savePhotoService.getIdList(st, st2);
                if (!res.equals("保存成功")) return res;
            }else {
                break;
            }
        }
        System.out.println("时间段："+a+"~"+et+"处理完成。");
        return res;
    }

    //获取错误信息
    @RequestMapping("getError")
    public List<ImageData> getError (Long lastId){
        List<ImageData> imageDataList = savePhotoService.getError(lastId);
        return imageDataList;
    }

    //获取错误信息(上一页)
    @RequestMapping("getErrorUp")
    public List<ImageData> getErrorUp (Long firstId){
        List<ImageData> imageDataList = savePhotoService.getErrorUp(firstId);
        return imageDataList;
    }

    //根据id查询
    @RequestMapping("getImageData")
    public Summarizing getImageData (int id){
        Summarizing summarizing = savePhotoService.getImageData(id);
        return summarizing;
    }

    //修改单个image表记录参数
    @RequestMapping("updateImage")
    public String updateImage (Summarizing summarizing){
        savePhotoService.updateImage(summarizing);
        return "修改成功";
    }

    //修改单个image表记录参数(图片无法识别)
    @RequestMapping("updateImageNo")
    public String updateImageNo (Long id){
        savePhotoService.updateImageNo(id);
        return "提交成功";
    }

    //获取总页数
    @RequestMapping("getSun")
    public int getSun (){
        return savePhotoService.getSun();
    }

    //修改日期的方法
    @RequestMapping("getImage")
    public String getImage(String startTime ,String endTime,String vehicleId){
        Process proc;
        String exe = "python";
        String command = "D:\\MyCode\\GLListData\\start.py";
        String[] cmdArr = new String[] {exe, command, startTime, endTime,vehicleId};
        try {
            proc = Runtime.getRuntime().exec(cmdArr);// 执行py文件
            //用输入输出流来截取结果
//            DataInputStream in = new DataInputStream(proc.getInputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            boolean end = false;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                if (line.contains("GEnd"))end=true;
            }
            in.close();
            proc.waitFor();
            if (end){
                return "下载完成";
            }else{
                return "下载失败";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "下载失败";
        }
    }

    //修改时间的方法
    private String time(String st ,int hour){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(st);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null) System.out.println("front:" + format.format(date)); //显示输入的日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        return format.format(date);
    }
}
