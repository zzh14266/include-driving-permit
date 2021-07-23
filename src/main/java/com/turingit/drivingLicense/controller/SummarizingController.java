package com.turingit.drivingLicense.controller;

import com.turingit.drivingLicense.baiduClass.SelectReturn;
import com.turingit.drivingLicense.pojo.Export;
import com.turingit.drivingLicense.pojo.Summarizing;
import com.turingit.drivingLicense.service.SummarizingService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;

@RestController
public class SummarizingController {

    @Autowired
    private SummarizingService summarizingService;

    //获取输出判断结果
    @RequestMapping("selectExport")
    public List<Export> selectExport (Integer pagination,Integer typeId){
        return summarizingService.selectExport(pagination,typeId);
    }

    //统计总数判断结果
    @RequestMapping("countExport")
    public Integer countExport (Integer typeId){
        return summarizingService.countExport(typeId);
    }

    @RequestMapping("/outPutExcel")
    public void outPutExcel(HttpServletResponse response,Integer typeId) throws Exception {
        // 每次写100行数据，就刷新数据出缓存
        SXSSFWorkbook wb = new SXSSFWorkbook(); // keep 100 rows in memory,
        Sheet sh = wb.createSheet();
        // 这个是业务数据
        List<Export> tmps = summarizingService.getSumm(typeId);
        String[] titles = null;
        if (typeId==5){
            titles = new String[]{"查验编号", "登记车牌（牵引车车牌）", "挂车车牌", "挂车车牌（车尾照）", "入口站名称", "出口路段", "出口站名称", "查验开始时间", "运输货物品种", "查验结果", "车型", "车种", "应收金额（元）", "申报电话", "备注", "牵引车号牌号码（识别）", "牵引车车辆类型", "牵引车所有人", "牵引车住址", "牵引车使用性质", "牵引车品牌型号", "牵引车车辆识别代号", "牵引车发动机号码", "牵引车注册日期", "牵引车发证日期", "牵引车整备质量", "牵引车轮廓尺寸", "挂车车辆类型", "挂车所有人", "挂车住址", "挂车使用性质", "挂车品牌型号", "挂车车辆识别代号", "挂车注册日期", "挂车发证日期", "挂车核定载质量", "挂车总质量", "挂车整备质量", "挂车轮廓尺寸", "图片地址"};
        }else {
            titles = new String[]{"查验编号", "登记车牌（牵引车车牌）", "挂车车牌", "入口站名称", "出口路段", "出口站名称", "查验开始时间", "运输货物品种", "查验结果", "车型", "车种", "应收金额（元）", "申报电话", "备注", "牵引车号牌号码（识别）", "牵引车车辆类型", "牵引车所有人", "牵引车住址", "牵引车使用性质", "牵引车品牌型号", "牵引车车辆识别代号", "牵引车发动机号码", "牵引车注册日期", "牵引车发证日期", "牵引车整备质量", "牵引车轮廓尺寸", "挂车车辆类型", "挂车所有人", "挂车住址", "挂车使用性质", "挂车品牌型号", "挂车车辆识别代号", "挂车注册日期", "挂车发证日期", "挂车核定载质量", "挂车总质量", "挂车整备质量", "挂车轮廓尺寸", "图片地址"};
        }
        Row row = sh.createRow(0);
        // 第一行设置标题
        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            Cell cell1 = row.createCell(i);
            cell1.setCellValue(title);
        }

        // 导出数据
        for (int rowNum = 0; rowNum < tmps.size(); rowNum++) {
            int i = 0;
            Row rowData = sh.createRow(rowNum + 1);
            // TbClass 这个是我的业务类，这个是根据业务来进行填写数据
            Export tmp = tmps.get(rowNum);
            // 第1列
            Cell cellDataA = rowData.createCell(i++);
            cellDataA.setCellValue(tmp.getCheckid());
            // 第2列
            Cell cellDataB = rowData.createCell(i++);
            cellDataB.setCellValue(tmp.getVehicleid());
            // 第3列
            Cell cellDataC = rowData.createCell(i++);
            cellDataC.setCellValue(tmp.getTLicencePlate());

            if (typeId==5){
                // 第4列 and 第5列
                Cell cellDataD2 = rowData.createCell(i++);
                cellDataD2.setCellValue(tmp.getTrailerVehicleid());

                Cell cellDataD = rowData.createCell(i++);
                cellDataD.setCellValue(tmp.getEnstationname());
            }else {
                // 第4列
                Cell cellDataD = rowData.createCell(i++);
                cellDataD.setCellValue(tmp.getEnstationname());
            }
            // 第5列
            Cell cellDataE = rowData.createCell(i++);
            cellDataE.setCellValue(tmp.getRoadname());
            // 第6列
            Cell cellDataF = rowData.createCell(i++);
            cellDataF.setCellValue(tmp.getExstationname());
            // 第7列
            Cell cellDataG = rowData.createCell(i++);
            cellDataG.setCellValue(tmp.getChecktime());
            // 第8列
            Cell cellDataH = rowData.createCell(i++);
            cellDataH.setCellValue(tmp.getFreighttypes());
            // 第9列
            Cell cellDataI = rowData.createCell(i++);
            cellDataI.setCellValue(tmp.getCheckresult());
            // 第10列
            Cell cellDataJ = rowData.createCell(i++);
            cellDataJ.setCellValue(tmp.getVehicletype());
            // 第11列
            Cell cellDataK = rowData.createCell(i++);
            cellDataK.setCellValue(tmp.getVehicleclass());
            // 第12列
            Cell cellDataL = rowData.createCell(i++);
            cellDataL.setCellValue(tmp.getCalculationfeeStr());
            // 第13列
            Cell cellDataM = rowData.createCell(i++);
            cellDataM.setCellValue(tmp.getSourcedrivertelephone());
            // 第14列
            Cell cellDataN = rowData.createCell(i++);
            cellDataN.setCellValue(tmp.getVL_limit_text());
            // 第15列
            Cell cellDataO = rowData.createCell(i++);
            cellDataO.setCellValue(tmp.getLicencePlate());
            // 第16列
            Cell cellDataP = rowData.createCell(i++);
            cellDataP.setCellValue(tmp.getVehicleTypeNew());
            // 第17列
            Cell cellDataQ = rowData.createCell(i++);
            cellDataQ.setCellValue(tmp.getOwner());
            // 第18列
            Cell cellDataR = rowData.createCell(i++);
            cellDataR.setCellValue(tmp.getAddress());
            // 第19列
            Cell cellDataS = rowData.createCell(i++);
            cellDataS.setCellValue(tmp.getFunction());
            // 第20列
            Cell cellDataT = rowData.createCell(i++);
            cellDataT.setCellValue(tmp.getBrandModel());
            // 第21列
            Cell cellDataU = rowData.createCell(i++);
            cellDataU.setCellValue(tmp.getPlateNumber());
            // 第22列
            Cell cellDataV = rowData.createCell(i++);
            cellDataV.setCellValue(tmp.getEngineNumber());
            // 第23列
            Cell cellDataW = rowData.createCell(i++);
            cellDataW.setCellValue(tmp.getRecordDate());
            // 第24列
            Cell cellDataX = rowData.createCell(i++);
            cellDataX.setCellValue(tmp.getOpeningDate());
            // 第25列
            Cell cellDataY = rowData.createCell(i++);
            cellDataY.setCellValue(tmp.getCurbWeight());
            // 第26列
            Cell cellDataZ = rowData.createCell(i++);
            cellDataZ.setCellValue(tmp.getVehicleDimension());
            // 第27列
            Cell cellDataAA = rowData.createCell(i++);
            cellDataAA.setCellValue(tmp.getTVehicleType());
            // 第28列
            Cell cellDataAB = rowData.createCell(i++);
            cellDataAB.setCellValue(tmp.getTOwner());
            // 第29列
            Cell cellDataAC = rowData.createCell(i++);
            cellDataAC.setCellValue(tmp.getTAddress());
            // 第30列
            Cell cellDataAD = rowData.createCell(i++);
            cellDataAD.setCellValue(tmp.getTFunction());
            // 第31列
            Cell cellDataAE = rowData.createCell(i++);
            cellDataAE.setCellValue(tmp.getTBrandModel());
            // 第32列
            Cell cellDataAF = rowData.createCell(i++);
            cellDataAF.setCellValue(tmp.getTPlateNumber());
            // 第33列
            Cell cellDataAG = rowData.createCell(i++);
            cellDataAG.setCellValue(tmp.getTRecordDate());
            // 第34列
            Cell cellDataAH = rowData.createCell(i++);
            cellDataAH.setCellValue(tmp.getTOpeningDate());
            // 第35列
            Cell cellDataAI = rowData.createCell(i++);
            cellDataAI.setCellValue(tmp.getLoadCapacity());
            // 第36列
            Cell cellDataAJ = rowData.createCell(i++);
            cellDataAJ.setCellValue(tmp.getTTotalMass());
            // 第37列
            Cell cellDataAK = rowData.createCell(i++);
            cellDataAK.setCellValue(tmp.getTCurbWeight());
            // 第38列
            Cell cellDataAL = rowData.createCell(i++);
            cellDataAL.setCellValue(tmp.getTrailerDimension());
            // 第39列
            Cell cellDataAM = rowData.createCell(i++);
            cellDataAM.setCellValue(tmp.getImgPath());
        }

        String fileName =null;
        if (typeId==1){
            fileName="“仅可用于运送不可拆解物体”大件运输车辆假冒绿色通道嫌疑的真假证记录.xlsx";
        }else if (typeId==2){
            fileName="“仅可用于运送不可拆解物体”大件运输车的绿色通道记录.xlsx";
        }else if (typeId==3){
            fileName="同个挂车车牌总质量不一致.xlsx";
        }else if (typeId==4){
            fileName="宽度大于等于3000mm但无“仅可用于运送不可拆解物体”字样.xlsx";
        }else if (typeId==5){
            fileName="车尾车牌与行驶证不符.xlsx";
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        wb.write(response.getOutputStream());
        wb.close();
    }
}
