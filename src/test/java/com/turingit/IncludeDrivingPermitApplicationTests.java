package com.turingit;

//import com.turingit.drivingLicense.ImageProcessing.Reset;
//import com.turingit.drivingLicense.baiduClass.License;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turingit.drivingLicense.mapper.ExportMapper;
import com.turingit.drivingLicense.mapper.SavePhotoMapper;
import com.turingit.drivingLicense.mapper.SummarizingMapper;
import com.turingit.drivingLicense.pojo.Export;
import com.turingit.drivingLicense.service.SavePhotoService;
import com.turingit.drivingLicense.service.SummarizingService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

@SpringBootTest
class IncludeDrivingPermitApplicationTests {

    @Autowired
    private SummarizingMapper summarizingMapper;

    @Autowired
    private SavePhotoService savePhotoService;

    @Autowired
    private SavePhotoMapper savePhotoMapper;

    @Autowired
    private ExportMapper exportMapper;

    @Autowired
    private SummarizingService summarizingService;

    @Test
    void insertExport() {
        List<Export> s1 = summarizingMapper.getS1();
    }

    @Test
    void test() throws IOException {
        String path = "D:\\一车一档";

        File file=new File(path);
        File[] tempList = file.listFiles();

        List<String[]> lsFile = new ArrayList<>();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isDirectory()) {
                //读取某个文件夹下的所有文件夹
                File[] temp=tempList[i].listFiles();
                for (int j = 0; j < temp.length; j++) {
                    String sFilePath = temp[j].toString();

                    sFilePath = sFilePath.substring(8);
                    String[] parts = sFilePath.split("\\\\");
                    if (parts[1].equals("大件运输逃费")){
                        parts[1] = String.valueOf(1);
                    }else if (parts[1].equals("逃费假证")){
                        parts[1] = String.valueOf(2);
                    }
                    lsFile.add(parts);
                }
            }
        }


    }

    private String d(String s){
        if (!s.contains("m")){
            s=s+"mm";
        }

        byte[] b1 = s.getBytes();
        int b= b1.length;

        StringBuilder sb = new StringBuilder(s);
        int x = s.indexOf("x");
        int x1 = s.lastIndexOf("x");
        int m = s.indexOf("m");

        String substring = s.substring(0, 1);//获取第一个参数
        String substring3 = s.substring(x+1, x+2);//获取第一个x后的数字
        String substring4 = s.substring(x+2, x+3);//获取第一个x后的数字

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

        s=s.replace("x","×");

        return s;
    }

    @Test
    public void test2(){
        File dirFile = new File("D:\\一车一档");
        ArrayList<String> lsAllFileUrl = new ArrayList<String>();

        if (dirFile.exists()) {
            //直接取出利用listFiles()把当前路径下的所有文件夹、文件存放到一个文件数组
            File files[] = dirFile.listFiles();
            for (File objFile : files) {
                //如果传递过来的参数dirFile是以文件分隔符，也就是/或者\结尾，则如此构造
                if (dirFile.getPath().endsWith(File.separator)) {
                    lsAllFileUrl.add(dirFile.getPath() + objFile.getName());
                } else {
                    //否则，如果没有文件分隔符，则补上一个文件分隔符，再加上文件名，才是路径
                    lsAllFileUrl.add(dirFile.getPath() + File.separator + objFile.getName());
                }
            }
        }

        for (String sFileUrl : lsAllFileUrl) {
            ArrayList<String> lsFileUrl = new ArrayList<String>();

            File objFile = new File(sFileUrl);
            File files[] = objFile.listFiles();
            for (File file : files) {
                //如果传递过来的参数dirFile是以文件分隔符，也就是/或者\结尾，则如此构造
                if (objFile.getPath().endsWith(File.separator)) {
                    lsFileUrl.add(objFile.getPath() + file.getName());
                } else {
                    //否则，如果没有文件分隔符，则补上一个文件分隔符，再加上文件名，才是路径
                    lsFileUrl.add(objFile.getPath() + File.separator + file.getName());
                }
            }

            boolean isFake = false;
            for (String sFileName : lsFileUrl) {
                if (sFileName.contains("逃费假证")){
                    isFake = true;
                }
            }

            if (!isFake){
                boolean b = deleteDir(objFile);
            }
        }
    }

    public static boolean deleteDir(File objFile){
        if (objFile.isDirectory()) {
            String[] children = objFile.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(objFile, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return objFile.delete();
    }

    public static void main(String[] args) {
        deleteDir(new File("D:\\1234"));
    }
}
