package com.turingit.drivingLicense.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turingit.drivingLicense.pojo.Export;
import com.turingit.drivingLicense.pojo.ImageData;
import com.turingit.drivingLicense.pojo.Summarizing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SavePhotoMapper extends BaseMapper<ImageData> {

    @Select("SELECT id,typename,abnormal_image,typeid,img_path FROM public.imageData2 WHERE (abnormal_image <> '图片处理完成' AND id > #{id}) ORDER BY id asc LIMIT 10")
    public List<ImageData> selectError(long id);

    @Select("SELECT id,typename,abnormal_image,typeid,img_path FROM public.imageData2 WHERE (abnormal_image <> '图片处理完成' AND id < #{id}) ORDER BY id Desc LIMIT 10")
    public List<ImageData> selectErrorUp(long id);

    public void updateImage(List<ImageData> imageDataList);

    public List<ImageData> selectTime(@Param("st") String st,@Param("et") String et);

    @Select("select min(checktime) from public.checkdata2 where checkid in ( SELECT checkid FROM public.imagedata2 where abnormal_image is null and (typename='行驶证' or typename='车头照' or typename='车尾照')) ")
    String mq();

    @Select("SELECT * FROM public.export2")
    public List<Export> selectExport();
}
