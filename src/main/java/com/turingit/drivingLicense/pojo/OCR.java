package com.turingit.drivingLicense.pojo;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

@Data
public class OCR {
    private String type;    //正附页
    private JSONArray fieldList; //具体参数
}
