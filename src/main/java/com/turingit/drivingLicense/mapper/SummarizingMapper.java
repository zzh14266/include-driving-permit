package com.turingit.drivingLicense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turingit.drivingLicense.pojo.Summarizing;
import com.turingit.drivingLicense.pojo.Export;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SummarizingMapper extends BaseMapper<Summarizing> {

    //“仅可用于运送不可拆解物体”大件运输车辆假冒绿色通道嫌疑的真假证记录
    public List<Export> selectExport1(@Param("pagination") Integer pagination);
    //“仅可用于运送不可拆解物体”大件运输车的绿色通道记录
    public List<Export> selectExport2(@Param("pagination") Integer pagination);
//    //绿通有免费记录出口流水有收费记录的记录(1小时内)
//    public List<Export> selectExport3(@Param("pagination") Integer pagination);
//    //宽度大于等于3000mm但无“仅可用于运送不可拆解物体”字样
//    public List<Export> selectExport4(@Param("pagination") Integer pagination);
//    //车尾车牌与行驶证不符
//    public List<Export> selectExport5(@Param("pagination") Integer pagination);

    //方法1统计
    public Integer countExport1();
    //方法2统计
    public Integer countExport2();
//    //方法3统计
//    public Integer countExport3();
//    //方法4统计
//    public Integer countExport4();
//    //方法5统计
//    public Integer countExport5();

    public void updateSummar(List<Summarizing> summarizingList);

    List<Export> getS1();
    List<Export> getS2();
//    List<Export> getS3();
//    List<Export> getS4();
//    List<Export> getS5();

    List<Export> download1();
    List<Export> download2();
}
