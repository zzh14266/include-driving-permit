package com.turingit.drivingLicense.baiduClass;
//处理行驶证数据

import com.turingit.drivingLicense.ImageProcessing.JsonToList;
import com.turingit.drivingLicense.pojo.*;
import org.apache.ibatis.javassist.bytecode.analysis.ControlFlow;
import org.json.JSONArray;

import java.util.ArrayList;

public class DisposeReturn {

    //前车行驶证
    public static Summarizing carCertificates(ArrayList<OCR> ocrs, Summarizing checkdata){

        for (int i = 0; i < ocrs.size(); i++) {
            JSONArray fieldList = ocrs.get(i).getFieldList();
            ArrayList<OCRFieldList> ocrFieldLists = JsonToList.OCRFieldList(fieldList);

            if (ocrs.get(i).getType().equals("行驶证副页")){
                if (ocrFieldLists.get(0).getValue().contains("挂")){
                    for (OCRFieldList ocrFieldList : ocrFieldLists){
                        switch (ocrFieldList.getChn_key()){
                            case "总质量":
                                checkdata.setTTotalMass(ocrFieldList.getValue());
                                break;
                            case "整备质量":
                                checkdata.setTCurbWeight(ocrFieldList.getValue());
                                break;
                            case "核定载质量":
                                checkdata.setLoadCapacity(ocrFieldList.getValue());
                                break;
                            case "外廓尺寸":
                                if (ocrFieldList.getValue()==null||ocrFieldList.getValue().equals("")){
                                }else {
                                    String d = d(ocrFieldList.getValue());
                                    checkdata.setTrailerDimension(d);
                                }
                                break;
                            case "备注":
                                String universal = ocrFieldList.getValue();
                                if (universal.contains("仅可")||universal.contains("用于")||universal.contains("不可拆解")||universal.contains("物体")){
                                    checkdata.setVlLimitText(1);
                                }else{
                                    checkdata.setVlLimitText(2);
                                }
                                break;
                        }
                    }
                }else{
                    for (OCRFieldList ocrFieldList : ocrFieldLists){
                        switch (ocrFieldList.getChn_key()){
                            case "整备质量":
                                checkdata.setCurbWeight(ocrFieldList.getValue());
                                break;
                            case "外廓尺寸":
                                if (ocrFieldList.getValue()==null||ocrFieldList.getValue().equals("")){
                                }else {
                                    String d = d(ocrFieldList.getValue());
                                    checkdata.setVehicleDimension(d);
                                }
                                break;
                            case "总质量":
                                checkdata.setTTotalMass(ocrFieldList.getValue());
                                break;
                            case "核定载质量":
                                checkdata.setLoadCapacity(ocrFieldList.getValue());
                                break;
                        }
                    }
                }
            }else{
                if (ocrFieldLists.get(0).getValue().contains("挂")){
                    for (OCRFieldList ocrFieldList : ocrFieldLists){
                        switch (ocrFieldList.getChn_key()){
                            case "车辆识别代码":
                                checkdata.setTPlateNumber(ocrFieldList.getValue());
                                break;
                            case "地址":
                                checkdata.setTAddress(ocrFieldList.getValue());
                                break;
                            case "发证日期":
                                checkdata.setTOpeningDate(ocrFieldList.getValue());
                                break;
                            case "品牌型号":
                                checkdata.setTBrandModel(ocrFieldList.getValue());
                                break;
                            case "车辆类型":
                                checkdata.setTVehicleType(ocrFieldList.getValue());
                                break;
                            case "所有人":
                                checkdata.setTOwner(ocrFieldList.getValue());
                                break;
                            case "使用性质":
                                checkdata.setTFunction(ocrFieldList.getValue());
                                break;
                            case "发动机号":
                                checkdata.setTEngineNumber(ocrFieldList.getValue());
                                break;
                            case "号牌号码":
                                checkdata.setTLicencePlate(ocrFieldList.getValue());
                                break;
                            case "注册日期":
                                checkdata.setTRecordDate(ocrFieldList.getValue());
                                break;
                        }
                    }
                }else{
                    for (OCRFieldList ocrFieldList : ocrFieldLists){
                        switch (ocrFieldList.getChn_key()){
                            case "车辆识别代码":
                                checkdata.setPlateNumber(ocrFieldList.getValue());
                                break;
                            case "地址":
                                checkdata.setAddress(ocrFieldList.getValue());
                                break;
                            case "发证日期":
                                checkdata.setOpeningDate(ocrFieldList.getValue());
                                break;
                            case "品牌型号":
                                checkdata.setBrandModel(ocrFieldList.getValue());
                                break;
                            case "车辆类型":
                                checkdata.setVehicleType(ocrFieldList.getValue());
                                break;
                            case "所有人":
                                checkdata.setOwner(ocrFieldList.getValue());
                                break;
                            case "使用性质":
                                checkdata.setFunction(ocrFieldList.getValue());
                                break;
                            case "发动机号":
                                checkdata.setEngineNumber(ocrFieldList.getValue());
                                break;
                            case "号牌号码":
                                checkdata.setLicencePlate(ocrFieldList.getValue());
                                break;
                            case "注册日期":
                                checkdata.setRecordDate(ocrFieldList.getValue());
                                break;
                        }
                    }
                }
            }
        }
        return checkdata;
    }

    //尺寸错位修改
    private static String d(String s){
        if (!s.contains("m")){
            s=s+"mm";
        }

        byte[] b1 = s.getBytes();
        int b= b1.length;

        StringBuilder sb = new StringBuilder(s);
        int x = 0;
        int x1 = 0;
        try {
            x = s.indexOf("x");
            x1 = s.lastIndexOf("x");
        }catch (Exception e){
            return s;
        }
        if (x==-1||x1==-1||x==x1){
            s=s.replace("x","×");
            return s;
        }

        int m = s.indexOf("m");

        String substring = s.substring(0, 1);//获取第一个参数
        String substring3 = s.substring(x+1, x+2);//获取第一个x后的数字
        String substring4 = s.substring(x+2, x+3);//获取第一个x后的数字

        try {
            if (substring.equals("1")&&x1-x==6&&x==4){
                String substring1 = s.substring(5, 6);
                sb.insert(4, substring1);
                sb.deleteCharAt(6);
                s = sb.toString();
            }else if (substring.equals("1")&&b-x1-1>=7&&x1==9&&Integer.parseInt(substring3)>4){
                String substring1 = s.substring(10, 11);
                sb.insert(9, substring1);
                sb.deleteCharAt(11);
                s = sb.toString();

                sb = new StringBuilder(s);
                x = s.indexOf("x");
                x1 = s.lastIndexOf("x");

                if (x1-x==6&&x==4) {
                    String substring2 = s.substring(5, 6);
                    sb.insert(4, substring2);
                    sb.deleteCharAt(6);
                    s = sb.toString();
                }
            }else if (substring.equals("1")&&x1-x==4&&m-x1>=6){//13750×300×01710mm
                String substring2 = s.substring(x1+1, x1+2);
                sb.insert(x1-1, substring2);
                sb.deleteCharAt(x1+2);
                s = sb.toString();
            }else if (substring.equals("1")&&x==5&&x1-x==4){
                sb.insert(9,0);
                s = sb.toString();
            }else if (substring.equals("1")&&x==4) {//1599×5309×1700mm
                String substring2 = s.substring(5, 6);
                if (Integer.parseInt(substring2) >= 5) {
                    sb.insert(4, substring2);
                    sb.deleteCharAt(6);

                    substring2 = s.substring(10, 11);
                    sb.insert(9, substring2);
                    s = sb.toString();
                }else if (Integer.parseInt(substring2) == 0){
                    sb.deleteCharAt(x+1);
                    sb.insert(4,0);
                    s = sb.toString();
                    x = s.indexOf("x");
                    x1 = s.lastIndexOf("x");
                }else {
                    sb.insert(4,0);
                    s = sb.toString();

                    sb = new StringBuilder(s);
                    x1 = s.lastIndexOf("x");
                    b1 = s.getBytes();
                    b= b1.length;
                }

                if (b-x1-1==7&&x1==9){
                    String substring1 = s.substring(10, 11);
                    sb.insert(9, substring1);
                    sb.deleteCharAt(11);
                    s = sb.toString();
                }else if(x1-x==4&&m-x1>=6){
                    substring2 = s.substring(x1+1, x1+2);
                    sb.insert(x1-1, substring2);
                    sb.deleteCharAt(x1+2);
                    s = sb.toString();
                }
            }else if (!substring.equals("1")&&b-x1-1>=7&&x1==8){
                String substring1 = s.substring(9, 10);
                sb.insert(8, substring1);
                sb.deleteCharAt(10);
                s = sb.toString();

                sb = new StringBuilder(s);
                x = s.indexOf("x");
                x1 = s.lastIndexOf("x");

                if (x1-x==6&&x==3) {
                    String substring2 = s.substring(4, 5);
                    sb.insert(3, substring2);
                    sb.deleteCharAt(5);
                    s = sb.toString();
                }
            }else if (!substring.equals("1")&&x==4&&x1-x==4){
                sb.insert(8,0);
                s = sb.toString();
            }else if (!substring.equals("1")&&x==4&&Integer.parseInt(substring3)>4&&x1-x==5&&Integer.parseInt(substring4)<=4&&Integer.parseInt(substring4)!=0){
                sb.deleteCharAt(5);
                sb.insert(8, 0);
                s = sb.toString();
            }else if (substring.equals("1")&&Integer.parseInt(substring3)>4&&Integer.parseInt(substring4)!=0&&x1-x==5&&Integer.parseInt(substring4)<=4){
                sb.deleteCharAt(6);
                sb.insert(9, 0);
                s = sb.toString();
            }else if (substring.equals("1")&&x1-x==5&&x==4){
                sb.insert(x, 0);
                s = sb.toString();
            }else if (substring.equals("1")&&x1-x==4&&x==5&&Integer.parseInt(substring3)<=4){
                sb.insert(x1-1, 0);
                s = sb.toString();
            }else if (!substring.equals("1")&&x1-x==5&&x==3){
                sb.insert(x-1, 0);
                s = sb.toString();
            }else if (!substring.equals("1")&&x1-x==6&&x==3&&Integer.parseInt(substring3)<=4){//700×25000×3398mm
                sb.deleteCharAt(x1-1);
                sb.insert(x-1, 0);
                s = sb.toString();
            }else if (!substring.equals("1")&&x1-x==5&&x==3&&Integer.parseInt(substring3)<=4){//750×3000×1500mm
                sb.insert(x-1, 0);
                s = sb.toString();
            }else if (!substring.equals("1")&&x1-x==5&&x==4&&Integer.parseInt(substring3)==0){//7224×0249×01530mm
                String substring2 = s.substring(x1+1, x1+2);
                if (substring2.equals("0")){
                    sb.deleteCharAt(x1+1);
                    sb.insert(x1, substring2);
                    sb.deleteCharAt(x+1);
                }else {
                    sb.insert(x1, 0);
                    sb.deleteCharAt(x+1);
                }
                s = sb.toString();
            }else if (substring.equals("1")&&x1-x==5&&x==5&&Integer.parseInt(substring3)==0){//7224×0249×01530mm
                String substring2 = s.substring(x1+1, x1+2);
                if (substring2.equals("0")){
                    sb.deleteCharAt(x1+1);
                    sb.insert(x1, substring2);
                    sb.deleteCharAt(x+1);
                }else {
                    sb.insert(x1, 0);
                    sb.deleteCharAt(x+1);
                }
                s = sb.toString();
            }
        }catch (Exception e){
            s = sb.toString();
        }


        s=s.replace("x","×");

        return s;
    }
}
