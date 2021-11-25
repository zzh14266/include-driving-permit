package com.turingit.drivingLicense.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName(autoResultMap = true, value = "export2",schema = "public")
@Data
@Accessors(chain=true)
public class Export {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String checkid;          //查验编号
    private String vehicleid;        //登记车牌
    private String tLicencePlate;    //挂车号牌号码（行驶证中的）
    private String enstationname;    //入口站名称
    private String roadname;         //出口路段
    private String exstationname;    //出口站名称
    private String checktime;        //查验开始时间
    private String freighttypes;     //运输货物品种
    private String checkresult;      //查验结果
    private Integer vehicletype;      //车型
    private Integer vehicleclass;     //车种
    private Float calculationfeeStr;//应收金额（元）
    private String sourcedrivertelephone;//申报电话
    private String vlLimitText;      //备注
    private String licencePlate;     //牵引车号牌号码（识别）
    private String vehicleTypeNew;   //牵引车车辆类型
    private String owner;            //前车所有人
    private String address;          //前车住址
    private String function;         //前车使用性质
    private String brandModel;       //前车品牌型号
    private String plateNumber;      //前车车辆识别代号
    private String engineNumber;     //前车发动机号码
    private String recordDate;       //前车注册日期
    private String openingDate;      //前车发证日期
    private String curbWeight;       //牵引车整备质量
    private String vehicleDimension; //前车尺寸
    private String tVehicleType;     //挂车车辆类型
    private String tOwner;           //挂车所有人
    private String tAddress;         //挂车住址
    private String tFunction;        //挂车使用性质
    private String tBrandModel;      //挂车品牌型号
    private String tPlateNumber;     //挂车车辆识别代号
    private String tRecordDate;      //挂车注册日期
    private String tOpeningDate;     //挂车发证日期
    private String loadCapacity;     //挂车核定载质量
    private String tTotalMass;       //挂车总质量
    private String tCurbWeight;      //挂车整备质量
    private String trailerDimension; //挂车尺寸
    private String imgPath;          //图片相对路径
    private String trailerVehicleid; //挂车车牌（车辆后方图片中的）

    private Integer isGenuineAndSham; //“仅可用于运送不可拆解物体”大件运输车辆假冒绿色通道嫌疑的真假证记录
    private Integer isOnlyUsed;     //“仅可用于运送不可拆解物体”大件运输车的绿色通道记录
}
