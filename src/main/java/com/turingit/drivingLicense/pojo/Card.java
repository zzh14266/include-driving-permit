package com.turingit.drivingLicense.pojo;

import lombok.Data;
import org.json.JSONObject;

@Data
public class Card {
    private Double probability;
    private JSONObject location;
    private String card_type;
}
