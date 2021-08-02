package com.turingit.drivingLicense.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.turingit.drivingLicense.mapper.ExportMapper;
import com.turingit.drivingLicense.mapper.SavePhotoMapper;
import com.turingit.drivingLicense.mapper.SummarizingMapper;
import com.turingit.drivingLicense.pojo.Export;
import com.turingit.drivingLicense.pojo.ImageData;
import com.turingit.drivingLicense.pojo.Summarizing;
import com.turingit.drivingLicense.service.SummarizingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummarizingImpl implements SummarizingService {

    @Autowired
    private SavePhotoMapper savePhotoMapper;
    @Autowired
    private SummarizingMapper summarizingMapper;
    @Autowired
    private ExportMapper exportMapper;

    @Override
    public List<Export> selectExport(Integer pagination,Integer typeId) {
        pagination=pagination*10-10;
        List<Export> exports = null;
        if (typeId==1)exports = summarizingMapper.selectExport1(pagination);
        if (typeId==2)exports = summarizingMapper.selectExport2(pagination);
        if (typeId==3)exports = summarizingMapper.selectExport3(pagination);
        if (typeId==4)exports = summarizingMapper.selectExport4(pagination);
        if (typeId==5)exports = summarizingMapper.selectExport5(pagination);
        insertExport();
        return exports;
    }

    @Override
    public Integer countExport(Integer typeId) {
        Integer count = null;
        if (typeId==1)count = summarizingMapper.countExport1();
        if (typeId==2)count = summarizingMapper.countExport2();
        if (typeId==3)count = summarizingMapper.countExport3();
        if (typeId==4)count = summarizingMapper.countExport4();
        if (typeId==5)count = summarizingMapper.countExport5();

        if (count%10==0){
            count=count/10;
        }else {
            count=count/10+1;
        }
        return count;
    }

    @Override
    public List<Long> a() {
        QueryWrapper<ImageData> imageDataQueryWrapper = new QueryWrapper<>();
        imageDataQueryWrapper.select("id","img_path");
        imageDataQueryWrapper.like("img_path",":");
        imageDataQueryWrapper.orderByDesc("id");
        List<ImageData> imageDataList=savePhotoMapper.selectList(imageDataQueryWrapper);
        int id = 0;
        int size=imageDataList.size();
        for (ImageData imageData : imageDataList){
            String a = imageData.getImgPath();
            a = a.replaceAll(" ", "_");
            a = a.replaceAll(":", "_");
            imageData.setImgPath(a);
            savePhotoMapper.updateById(imageData);
            id=id+1;
            System.out.println(imageData.getId()+":"+id+"/"+size);
        }

        List<Long> idList = new ArrayList();
        return idList;
    }

    @Override
    public void b(Long id) {
        QueryWrapper<Summarizing> summarizingQueryWrapper = new QueryWrapper<>();
        summarizingQueryWrapper.select("checkid","trailer_dimension","vehicle_dimension");
        summarizingQueryWrapper.orderByAsc("checkid");
        summarizingQueryWrapper.like("trailer_dimension","x");
        List<Summarizing> summarizingList = summarizingMapper.selectList(summarizingQueryWrapper);
        int l = 0;
        int size = summarizingList.size();

        for (int i = 0; i < size; i++) {
            if (summarizingList.get(i).getVehicleDimension()==null||summarizingList.get(i).getVehicleDimension().equals("")){
            }else {
                String s2 = summarizingList.get(i).getVehicleDimension();
                String d2 = d(s2);
                summarizingList.get(i).setVehicleDimension(d2);
            }
            if (summarizingList.get(i).getTrailerDimension()==null||summarizingList.get(i).getTrailerDimension().equals("")){
            }else {
                String s1 = summarizingList.get(i).getTrailerDimension();
                String d1 = d(s1);
                summarizingList.get(i).setTrailerDimension(d1);
            }

            UpdateWrapper<Summarizing> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("checkid",summarizingList.get(i).getCheckid());
            summarizingMapper.update(summarizingList.get(i), updateWrapper);

            System.out.println(i+1+"/"+size);
            System.out.println(summarizingList.get(i).getCheckid()+":"+summarizingList.get(i).getTrailerDimension()+"/"+summarizingList.get(i).getVehicleDimension());
        }
//        for (Summarizing summarizing : summarizingList){
//            if (summarizing.getVehicleDimension()!=null||!summarizing.getVehicleDimension().equals("")){
//                String s2 = summarizing.getVehicleDimension();
//                String d2 = d(s2);
//                summarizing.setVehicleDimension(d2);
//            }
//            if (summarizing.getTrailerDimension()!=null||!summarizing.getTrailerDimension().equals("")){
//                String s1 = summarizing.getTrailerDimension();
//                String d1 = d(s1);
//                summarizing.setTrailerDimension(d1);
//            }
//
//            summarizingMapper.updateById(summarizing);
//            System.out.println(++l+"/"+size);
//            System.out.println(summarizing.getCheckid()+":"+summarizing.getTrailerDimension()+"/"+summarizing.getVehicleDimension());
//        }
    }

    private String d(String s){
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
            s=s.replace("x","×");
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

//    @Override
//    public List<Long> c(int id) {
//        QueryWrapper<ImageData> imageDataQueryWrapper = new QueryWrapper<>();
//        imageDataQueryWrapper.select("id");
//        imageDataQueryWrapper.eq("abnormal_image","图片有误")
//                .and(wrapper -> wrapper.eq("typename","行驶证")
//                        .or().eq("typename","车头照")
//                        .or().eq("typename","车尾照"));
//        imageDataQueryWrapper.orderByAsc("id");
//        List<ImageData> imageDataList=savePhotoMapper.selectList(imageDataQueryWrapper);
//        List<Long> idList = new ArrayList();
//        for (int i = 0; i < imageDataList.size(); i++) {
//            if (i%5==(id))
//                idList.add(imageDataList.get(i).getId());
//        }
//
//        for (int i = 0; i < imageDataList.size(); i++) {
//            ImageData imageData = savePhotoMapper.selectById(idList.get(i));
//
//            Summarizing summarizing=new Summarizing();
//
//            if (imageData.getTrailerVehicleid()==null||imageData.getTrailerVehicleid().equals(""))imageData.setTrailerVehicleid(null);
//            summarizing.setTrailerVehicleid(imageData.getTrailerVehicleid());
//
//            if (imageData.getVehicleDimension()==null||imageData.getVehicleDimension().equals(""))imageData.setVehicleDimension(null);
//            summarizing.setVehicleDimension(imageData.getVehicleDimension());
//
//            if (imageData.getWeightLimt()==null||imageData.getWeightLimt().equals(""))imageData.setWeightLimt(null);
//            summarizing.setWeightLimt(imageData.getWeightLimt());
//
//            if (imageData.getVlLimitText()==null||imageData.getVlLimitText().equals(""))imageData.setVlLimitText(null);
//            summarizing.setVlLimitText(imageData.getVlLimitText());
//
//            if (imageData.getTractionVehicleid()==null||imageData.getTractionVehicleid().equals(""))imageData.setTractionVehicleid(null);
//            summarizing.setTractionVehicleid(imageData.getTractionVehicleid());
//
//            if (imageData.getPlateNumber()==null||imageData.getPlateNumber().equals(""))imageData.setPlateNumber(null);
//            summarizing.setPlateNumber(imageData.getPlateNumber());
//
//            if (imageData.getAddress()==null||imageData.getAddress().equals(""))imageData.setAddress(null);
//            summarizing.setAddress(imageData.getAddress());
//
//            if (imageData.getOpeningDate()==null||imageData.getOpeningDate().equals(""))imageData.setOpeningDate(null);
//            summarizing.setOpeningDate(imageData.getOpeningDate());
//
//            if (imageData.getBrandModel()==null||imageData.getBrandModel().equals(""))imageData.setBrandModel(null);
//            summarizing.setBrandModel(imageData.getBrandModel());
//
//            if (imageData.getVehicleType()==null||imageData.getVehicleType().equals(""))imageData.setVehicleType(null);
//            summarizing.setVehicleType(imageData.getVehicleType());
//
//            if (imageData.getOwner()==null||imageData.getOwner().equals(""))imageData.setOwner(null);
//            summarizing.setOwner(imageData.getOwner());
//
//            if (imageData.getFunction()==null||imageData.getFunction().equals(""))imageData.setFunction(null);
//            summarizing.setFunction(imageData.getFunction());
//
//            if (imageData.getEngineNumber()==null||imageData.getEngineNumber().equals(""))imageData.setEngineNumber(null);
//            summarizing.setEngineNumber(imageData.getEngineNumber());
//
//            if (imageData.getLicencePlate()==null||imageData.getLicencePlate().equals(""))imageData.setLicencePlate(null);
//            summarizing.setLicencePlate(imageData.getLicencePlate());
//
//            if (imageData.getRecordDate()==null||imageData.getRecordDate().equals(""))imageData.setRecordDate(null);
//            summarizing.setRecordDate(imageData.getRecordDate());
//
//            if (imageData.getCurbWeight()==null||imageData.getCurbWeight().equals(""))imageData.setCurbWeight(null);
//            summarizing.setCurbWeight(imageData.getCurbWeight());
//
//            if (imageData.getTrailerDimension()==null||imageData.getTrailerDimension().equals(""))imageData.setTrailerDimension(null);
//            summarizing.setTrailerDimension(imageData.getTrailerDimension());
//
//            if (imageData.getTPlateNumber()==null||imageData.getTPlateNumber().equals(""))imageData.setTPlateNumber(null);
//            summarizing.setTPlateNumber(imageData.getTPlateNumber());
//
//            if (imageData.getTAddress()==null||imageData.getTAddress().equals(""))imageData.setTAddress(null);
//            summarizing.setTAddress(imageData.getTAddress());
//
//            if (imageData.getTOpeningDate()==null||imageData.getTOpeningDate().equals(""))imageData.setTOpeningDate(null);
//            summarizing.setTOpeningDate(imageData.getTOpeningDate());
//
//            if (imageData.getTBrandModel()==null||imageData.getTBrandModel().equals(""))imageData.setTBrandModel(null);
//            summarizing.setTBrandModel(imageData.getTBrandModel());
//
//            if (imageData.getTVehicleType()==null||imageData.getTVehicleType().equals(""))imageData.setTVehicleType(null);
//            summarizing.setTVehicleType(imageData.getTVehicleType());
//
//            if (imageData.getTOwner()==null||imageData.getTOwner().equals(""))imageData.setTOwner(null);
//            summarizing.setTOwner(imageData.getTOwner());
//
//            if (imageData.getTFunction()==null||imageData.getTFunction().equals(""))imageData.setTFunction(null);
//            summarizing.setTFunction(imageData.getTFunction());
//
//            if (imageData.getTEngineNumber()==null||imageData.getTEngineNumber().equals(""))imageData.setTEngineNumber(null);
//            summarizing.setTEngineNumber(imageData.getTEngineNumber());
//
//            if (imageData.getTLicencePlate()==null||imageData.getTLicencePlate().equals(""))imageData.setTLicencePlate(null);
//            summarizing.setTLicencePlate(imageData.getTLicencePlate());
//
//            if (imageData.getTRecordDate()==null||imageData.getTRecordDate().equals(""))imageData.setTRecordDate(null);
//            summarizing.setTRecordDate(imageData.getTRecordDate());
//
//            if (imageData.getTCurbWeight()==null||imageData.getTCurbWeight().equals(""))imageData.setTCurbWeight(null);
//            summarizing.setTCurbWeight(imageData.getTCurbWeight());
//
//            if (imageData.getTTotalMass()==null||imageData.getTTotalMass().equals(""))imageData.setTTotalMass(null);
//            summarizing.setTTotalMass(imageData.getTTotalMass());
//
//            if (imageData.getLoadCapacity()==null||imageData.getLoadCapacity().equals(""))imageData.setLoadCapacity(null);
//            summarizing.setLoadCapacity(imageData.getLoadCapacity());
//
//            if (imageData.getAutomobileBrand()==null||imageData.getAutomobileBrand().equals(""))imageData.setAutomobileBrand(null);
//            summarizing.setAutomobileBrand(imageData.getAutomobileBrand());
//
//            if (imageData.getTypename().equals("行驶证")){
//                if (imageData.getImgPath()==null||imageData.getImgPath().equals(""))imageData.setImgPath(null);
//                summarizing.setImgPath(imageData.getImgPath());
//            }
//
//            UpdateWrapper<Summarizing> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("checkid",imageData.getCheckid());
//
//            System.out.println(id+":"+imageData.getId()+":"+i+"/"+idList.size());
//            try {
//                summarizingMapper.update(summarizing,updateWrapper);
//            }catch (Exception e){
//                continue;
//            }
//        }
//
//        return null;
//    }

    @Override
    public List<Export> getSumm(Integer typeId) {
        List<Export> exports = null;
        if (typeId==1)exports = summarizingMapper.getS1();
        if (typeId==2)exports = summarizingMapper.getS2();
        if (typeId==3)exports = summarizingMapper.getS3();
        if (typeId==4)exports = summarizingMapper.getS4();
        if (typeId==5)exports = summarizingMapper.getS5();
        return exports;
    }

    public void insertExport(){
        List<Export> exports = summarizingMapper.getS1();

        for (Export objExport : exports){
            QueryWrapper<Export> queryWrapper = new QueryWrapper();
            queryWrapper.eq("checkid",objExport.getCheckid());
            Export export = exportMapper.selectOne(queryWrapper);
            if (export == null && export.getCheckid().equals(objExport.getCheckid()) && export.getIsGenuineAndSham() == 0) {
                export.setIsGenuineAndSham(1);
                exportMapper.updateById(export);
                System.out.println("过滤一条车辆假冒绿色通道嫌疑的真假证记录");
            }else {
                objExport.setIsGenuineAndSham(1);
                exportMapper.insert(objExport);
                System.out.println("过滤一条车辆假冒绿色通道嫌疑的真假证记录");
            }
        }
        exports = summarizingMapper.getS2();
        for (Export objExport : exports){
            QueryWrapper<Export> queryWrapper = new QueryWrapper();
            queryWrapper.eq("checkid",objExport.getCheckid());
            Export export = exportMapper.selectOne(queryWrapper);
            if (export.getCheckid().equals(objExport.getCheckid()) && export.getIsGenuineAndSham() == 0) {
                export.setIsOnlyUsed(1);
                exportMapper.updateById(export);
                System.out.println("过滤一条车辆假冒绿色通道嫌疑的真假证记录");
            }else {
                objExport.setIsOnlyUsed(1);
                exportMapper.insert(objExport);
                System.out.println("过滤一条车辆假冒绿色通道嫌疑的真假证记录");
            }
        }
    }
}
