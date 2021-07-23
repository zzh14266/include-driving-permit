package com.turingit.drivingLicense.service;

import com.turingit.drivingLicense.pojo.Export;
import com.turingit.drivingLicense.pojo.Summarizing;

import java.util.List;

public interface SummarizingService {

//    List getFIdList();

    List<Export> selectExport(Integer pagination,Integer typeId);

//    void save(Long fid);

    Integer countExport(Integer typeId);

    List<Long> a();

    void b(Long id);

    List<Long> c(int id);

    List<Export> getSumm(Integer typeId);
}
