package com.turingit;

//import com.turingit.drivingLicense.ImageProcessing.Reset;
//import com.turingit.drivingLicense.baiduClass.License;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turingit.drivingLicense.mapper.SavePhotoMapper;
import com.turingit.drivingLicense.mapper.SummarizingMapper;
import com.turingit.drivingLicense.pojo.Export;
import com.turingit.drivingLicense.pojo.ImageData;
import com.turingit.drivingLicense.service.SavePhotoService;
import com.turingit.drivingLicense.service.SummarizingService;
import com.turingit.drivingLicense.service.impl.SummarizingImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
class IncludeDrivingPermitApplicationTests {

    @Autowired
    private SummarizingMapper summarizingMapper;

    @Autowired
    private SavePhotoService savePhotoService;

    @Autowired
    private SavePhotoMapper savePhotoMapper;

    @Autowired
    private SummarizingService summarizingService;

    @Test
    void insertExport() {
        List<Export> s1 = summarizingMapper.getS1();
    }

    @Test
    void test() {
        QueryWrapper<ImageData> imageDataQueryWrapper = new QueryWrapper<>();
        imageDataQueryWrapper.eq("typeid",13).last("limit 500000");
        List<ImageData> lsImageData = savePhotoMapper.selectList(imageDataQueryWrapper);
//        List<Export> exports = summarizingMapper.getS1();
        int n = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        for (ImageData objImageData : lsImageData) {
            n++;
            System.out.println(n);
            if (objImageData.getImgPath() == null || objImageData.getImgPath().equals("")){
                continue;
            }
            String yUrl = objImageData.getImgPath();
            File s = new File("\\\\192.168.10.108\\CheckData\\images\\" + yUrl);

            String sImgPath = objImageData.getImgPath().substring(0, 7);

            File t = new File("D:\\行驶证\\" + sImgPath + " " + sdf.format(objImageData.getChecktime()) + ".jpg");
            FileInputStream fi = null;
            FileOutputStream fo = null;
            FileChannel in = null;
            FileChannel out = null;
            try {
                fi = new FileInputStream(s);
                fo = new FileOutputStream(t);
                in = fi.getChannel();//得到对应的文件通道
                out = fo.getChannel();//得到对应的文件通道
                in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fi.close();
                    in.close();
                    fo.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Test
    public void ps() {
        String d = d("9502x5500x1550mm");
        System.out.println(d);

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
    void save2(){
        System.out.println(new Date());
        Process proc;
        try {
            proc = Runtime.getRuntime().exec("python D:\\start2.py");// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
