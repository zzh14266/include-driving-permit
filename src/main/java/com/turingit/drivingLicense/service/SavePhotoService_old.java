package com.turingit.drivingLicense.service;

import com.turingit.drivingLicense.pojo.Checkdata;
import com.turingit.drivingLicense.pojo.ImageData;
import com.turingit.drivingLicense.pojo.Summarizing;

import java.util.List;

public interface SavePhotoService_old {

    String updataData(Long id);

    List<ImageData> getError(Long id);

    List getIdList(String st, String et);

    Summarizing getImageData(int id);

    void updateImage(Summarizing summarizing);

    void updateImageNo(Long id);

    Integer getSun();

    List<ImageData> getErrorUp(Long firstId);
}
