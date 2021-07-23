package com.turingit.drivingLicense.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@TableName(autoResultMap = true, value = "summarizing2",schema = "public")
@Data
@Accessors(chain=true)
public class Summarizing {
    @TableId(type = IdType.AUTO)
    private Long Id;              //图片ID
    private String checkid;       //查验编号

    private String trailerVehicleid; //挂车车牌（车辆后方图片中的）
    private String vehicleDimension; //前车尺寸
    private String weightLimt;       //前车限重
    private String vlLimitText;      //仅可用于运送不可拆解物体

    //下面为新增字段
    //前车部分
    private String tractionVehicleid;//前车车牌(前车照片中的)
    private String plateNumber;      //前车车辆识别代号
    private String address;          //前车住址
    private String openingDate;      //前车发证日期
    private String brandModel;       //前车品牌型号
    private String vehicleType;      //前车车辆类型
    private String owner;            //前车所有人
    private String function;         //前车使用性质
    private String engineNumber;     //前车发动机号码
    private String licencePlate;     //前车号牌号码
    private String recordDate;       //前车注册日期
    private String curbWeight;       //牵引车整备质量
    //挂车部分
    private String trailerDimension; //挂车尺寸
    private String tPlateNumber;     //挂车车辆识别代号
    private String tAddress;         //挂车住址
    private String tOpeningDate;     //挂车发证日期
    private String tBrandModel;      //挂车品牌型号
    private String tVehicleType;     //挂车车辆类型
    private String tOwner;           //挂车所有人
    private String tFunction;        //挂车使用性质
    private String tEngineNumber;    //挂车发动机号码
    private String tLicencePlate;    //挂车号牌号码（行驶证中的）
    private String tRecordDate;      //挂车注册日期
    private String tCurbWeight;      //挂车整备质量
    private String tTotalMass;       //挂车总质量
    private String loadCapacity;     //挂车核定载质量

    private String automobileBrand;  //汽车品牌
    private String imgPath;          //图片相对路径
}
