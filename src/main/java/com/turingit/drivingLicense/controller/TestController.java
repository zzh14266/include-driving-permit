package com.turingit.drivingLicense.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.turingit.drivingLicense.mapper.ExportMapper;
import com.turingit.drivingLicense.mapper.SavePhotoMapper;
import com.turingit.drivingLicense.mapper.SummarizingMapper;
import com.turingit.drivingLicense.pojo.Export;
import com.turingit.drivingLicense.pojo.ImageData;
import com.turingit.drivingLicense.service.SavePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @创建人: zzh
 * @创建日期: 2022/2/16
 */
@RestController
public class TestController {

    @Autowired
    private SummarizingMapper summarizingMapper;

    @Autowired
    private SavePhotoService savePhotoService;

    @Autowired
    private SavePhotoMapper savePhotoMapper;

    @Autowired
    private ExportMapper exportMapper;

    //获取输出判断结果
    @RequestMapping("down1")
    public void selectExport () throws ParseException {
        QueryWrapper<ImageData> imageDataQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Export> exportQueryWrapper = new QueryWrapper<>();
        exportQueryWrapper.eq("is_genuine_and_sham", 1);
        List<Export> lsExport = exportMapper.selectList(exportQueryWrapper);
        int iAll = lsExport.size();
        int iSize = 0;
        for(Export objGreenFlee : lsExport){
            List<String> lsImagePath = new ArrayList<>();
            if (objGreenFlee.getIsGenuineAndSham() == 1) {
                //获取当前图片路径
                imageDataQueryWrapper = new QueryWrapper<>();
                imageDataQueryWrapper.eq("typeid",13).eq("checkid", objGreenFlee.getCheckid())
                        .or().eq("typeid",11).eq("checkid", objGreenFlee.getCheckid())
                        .or().eq("typeid",12).eq("checkid", objGreenFlee.getCheckid());
                List<ImageData> lsImageData = savePhotoMapper.selectList(imageDataQueryWrapper);
                for (ImageData objImageData : lsImageData) {
                    lsImagePath.add(objImageData.getImgPath());
                }

                //获取以往图片路径
                exportQueryWrapper = new QueryWrapper<>();

                exportQueryWrapper.eq("t_licence_plate", objGreenFlee.getTLicencePlate());
                exportQueryWrapper.ne("checkid", objGreenFlee.getCheckid());
                exportQueryWrapper.eq("vl_limit_text", "挂车行驶证印有“仅可用于运送不可拆解物体”");
                Export objExport = null;
                try {
                    List<Export> lsExport2 = exportMapper.selectList(exportQueryWrapper);
                    if (lsExport2 != null)
                        objExport = lsExport2.get(0);
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println(++iSize + "/" + iAll);
                    continue;
                }
                if (objExport != null) {
                    imageDataQueryWrapper = new QueryWrapper<>();
                    imageDataQueryWrapper.eq("checkid", objExport.getCheckid()).eq("typeid",13);

                    try {
                        List<ImageData> lsImageData2 = savePhotoMapper.selectList(imageDataQueryWrapper);
                        lsImagePath.add(lsImageData2.get(0).getImgPath());
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println(++iSize + "/" + iAll);
                        continue;
                    }
                }
//            } else
//                if (objGreenFlee.getIsOnlyUsed() == 1){
//                    //获取当前图片路径
//                    imageDataQueryWrapper = new QueryWrapper<>();
//                    imageDataQueryWrapper.eq("typeid",13).eq("checkid", objGreenFlee.getCheckid())
//                            .or().eq("typeid",11).eq("checkid", objGreenFlee.getCheckid())
//                            .or().eq("typeid",12).eq("checkid", objGreenFlee.getCheckid());
//                    List<ImageData> lsImageData = savePhotoMapper.selectList(imageDataQueryWrapper);
//                for (ImageData objImageData : lsImageData) {
//                    lsImagePath.add(objImageData.getImgPath());
//                }
            }

            for (int i = 0; i < lsImagePath.size(); i++) {
                String sImagePath = lsImagePath.get(i);
                //判断文件夹是否存在
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd hh_mm_ss");

                SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                Date date=formatter.parse(objGreenFlee.getChecktime());

                String sChecktime = sdf.format(date);
                String sFolderPath = objGreenFlee.getVehicleid() + "\\" + "逃费假证" + "\\" + sChecktime + objGreenFlee.getExstationname();
                sFolderPath = sFolderPath.replaceAll(":", "_");
                sFolderPath = "D:\\一车一档\\" + sFolderPath.replace(".", "");
                File fFolder = new File(sFolderPath);
                if (!fFolder.exists()) {
                    fFolder.mkdirs();
                }

                String sFrom = "\\\\192.168.10.108\\CheckData\\images\\" + sImagePath;
                String sTo = "";

                if (i == lsImagePath.size() - 1){
                    sTo = sFolderPath + "\\" + "真证 " + sImagePath.substring(sImagePath.lastIndexOf("/")+1);
                }else {
                    sTo = sFolderPath + "\\" + "假证 " + sImagePath.substring(sImagePath.lastIndexOf("/")+1);
                }
                if (!new File(sTo).exists()) {
                    try {
                        copyFile1(sFrom, sTo);
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println(e);
                    }
                }

            }
            System.out.println(++iSize + "/" + iAll);
        }
    }

    //获取输出判断结果
    @RequestMapping("down2")
    public void selectExport2 (int a) throws ParseException {
        QueryWrapper<ImageData> imageDataQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Export> exportQueryWrapper = new QueryWrapper<>();
        exportQueryWrapper.eq("is_only_used", 1);
        List<Export> lsExport = exportMapper.selectList(exportQueryWrapper);
        int iAll = lsExport.size();
        int iSize = 0;

        for (int j = 0; j < lsExport.size(); j++) {
            if (j <= a * 3000){
                System.out.println(++iSize + "/" + iAll);
                continue;
            }
            if (j > a * 3000 + 3000){
                break;
            }
            Export objGreenFlee = lsExport.get(j);

            List<String> lsImagePath = new ArrayList<>();
//            if (objGreenFlee.getIsGenuineAndSham() == 1) {
//                //获取当前图片路径
//                imageDataQueryWrapper = new QueryWrapper<>();
//                imageDataQueryWrapper.eq("typeid",13).eq("checkid", objGreenFlee.getCheckid())
//                        .or().eq("typeid",11).eq("checkid", objGreenFlee.getCheckid())
//                        .or().eq("typeid",12).eq("checkid", objGreenFlee.getCheckid());
//                List<ImageData> lsImageData = savePhotoMapper.selectList(imageDataQueryWrapper);
//                for (ImageData objImageData : lsImageData) {
//                    lsImagePath.add(objImageData.getImgPath());
//                }
//
//                //获取以往图片路径
//                QueryWrapper<Export> exportQueryWrapper = new QueryWrapper<>();
//
//                exportQueryWrapper.eq("t_licence_plate", objGreenFlee.getTLicencePlate());
//                exportQueryWrapper.ne("checkid", objGreenFlee.getCheckid());
//                exportQueryWrapper.eq("vl_limit_text", "挂车行驶证印有“仅可用于运送不可拆解物体”");
//                Export objExport = new Export();
//                try {
//                    objExport = exportMapper.selectOne(exportQueryWrapper);
//                }catch (Exception e){
//                    System.out.println(++iSize + "/" + iAll);
//                    continue;
//                }
//                if (objExport != null) {
//                    imageDataQueryWrapper = new QueryWrapper<>();
//                    imageDataQueryWrapper.eq("checkid", objExport.getCheckid()).eq("typeid",13);
//
//                    try {
//                        ImageData objImageData = savePhotoMapper.selectOne(imageDataQueryWrapper);
//                        lsImagePath.add(objImageData.getImgPath());
//                    }catch (Exception e){
//                        System.out.println(++iSize + "/" + iAll);
//                        continue;
//                    }
//                }
//            } else
            if (objGreenFlee.getIsOnlyUsed() == 1){
                //获取当前图片路径
                imageDataQueryWrapper = new QueryWrapper<>();
                imageDataQueryWrapper.eq("typeid",13).eq("checkid", objGreenFlee.getCheckid())
                        .or().eq("typeid",11).eq("checkid", objGreenFlee.getCheckid())
                        .or().eq("typeid",12).eq("checkid", objGreenFlee.getCheckid());
                List<ImageData> lsImageData = savePhotoMapper.selectList(imageDataQueryWrapper);
                for (ImageData objImageData : lsImageData) {
                    lsImagePath.add(objImageData.getImgPath());
                }
            }

            for (int i = 0; i < lsImagePath.size(); i++) {
                String sImagePath = lsImagePath.get(i);
                //判断文件夹是否存在
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd hh_mm_ss");
                SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                Date date=formatter.parse(objGreenFlee.getChecktime());
                String sChecktime = sdf.format(date);
                String sFolderPath = objGreenFlee.getVehicleid() + "\\" + "大件运输逃费" + "\\" + sChecktime + objGreenFlee.getExstationname();
                sFolderPath = sFolderPath.replaceAll(":", "_");
                sFolderPath = "D:\\一车一档\\" + sFolderPath.replace(".", "");
                File fFolder = new File(sFolderPath);
                if (!fFolder.exists()) {
                    fFolder.mkdirs();
                }

                String sFrom = "\\\\192.168.10.108\\CheckData\\images\\" + sImagePath;
                String sTo = "";

                sTo = sFolderPath + "\\" + sImagePath.substring(sImagePath.lastIndexOf("/")+1);
//                if (i == lsImagePath.size() - 1){
//                    sTo = sFolderPath + "\\" + "假证 " + sImagePath.substring(sImagePath.lastIndexOf("/")+1);
//                }else {
//                    sTo = sFolderPath + "\\" + "真证 " + sImagePath.substring(sImagePath.lastIndexOf("/")+1);
//                }
                if (!new File(sTo).exists()) {
                    try {
                        copyFile1(sFrom, sTo);
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println(e);
                    }
                }

            }
            System.out.println(++iSize + "/" + iAll);
        }
    }

    public static void copyFile1(String srcPath, String destPath) throws IOException {
        FileInputStream fis = new FileInputStream(srcPath);
        FileOutputStream fos = new FileOutputStream(destPath);

        int len = 0;
        while ((len = fis.read()) != -1) {
            fos.write(len);
        }

        fos.close();
        fis.close();
    }

    @RequestMapping("down3")
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

        int a = 0;
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
            System.out.println(++a + "/" + lsAllFileUrl.size());
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
}
