package com.turingit.drivingLicense.ImageProcessing;
//json转List
import com.turingit.drivingLicense.pojo.Card;
import com.turingit.drivingLicense.pojo.OCR;
import com.turingit.drivingLicense.pojo.OCRFieldList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonToList {
    public static ArrayList<Card> fillInQuestionJSONToArray(String json_string) throws JSONException {
        JSONArray json = new JSONArray(json_string);
        ArrayList<Card> List = new ArrayList<>();

        if(json.length() > 0)
        {
            for(int i = 0;i < json.length();i++)
            {
                JSONObject job = json.getJSONObject(i);
                Card card = new Card();
                card.setProbability(job.getDouble("probability"));
                card.setLocation(job.getJSONObject("location"));
                card.setCard_type(job.getString("card_type"));
                List.add(card);
            }
        }
        return List;
    }

    public static ArrayList<OCR> OCRArray(String json_string) throws JSONException {
        JSONArray json = new JSONArray(json_string);
        ArrayList<OCR> List = new ArrayList<>();

        if(json.length() > 0)
        {
            for(int i = 0;i < json.length();i++)
            {
                JSONObject job = json.getJSONObject(i);
                OCR ocr = new OCR();
                ocr.setType(job.getString("type"));
                ocr.setFieldList(job.getJSONArray("FieldList"));
                List.add(ocr);
            }
        }
        return List;
    }

    public static ArrayList<OCRFieldList> OCRFieldList(JSONArray json) throws JSONException {
        ArrayList<OCRFieldList> List = new ArrayList<>();

        if(json.length() > 0)
        {
            for(int i = 0;i < json.length();i++)
            {
                JSONObject job = json.getJSONObject(i);
                OCRFieldList ocr = new OCRFieldList();
                ocr.setValue(job.getString("value"));
                ocr.setChn_key(job.getString("chn_key"));
                List.add(ocr);
            }
        }
        return List;
    }
}
