package com.turingit.drivingLicense.baiduClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectReturn {
    public static String returnS(String s){
        if (s.equals("OCR")) {
            System.out.println("图像识别接口出错，请检查OCR服务。");
            return "图像识别接口出错，请检查OCR服务。";
        }else if (s.equals("NET")){
            System.out.println("文件共享异常，请检查是否开启文件共享。");
            return "文件共享异常，请检查是否开启文件共享。";
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String formatDate = sdf.format(date);
            System.out.println("识别完成,当前时间为:" + formatDate);
            return "完事了";
        }
    }

    public static List<Long> returnList(List<Long> idList,int id){
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            if (i%5==(id-46)){
                longList.add(idList.get(i));
            }
        }
        return longList;
    }
}
