package com.turingit.drivingLicense.thread;

import com.turingit.drivingLicense.component.GetBeanUtil;
import com.turingit.drivingLicense.service.SavePhotoService_old;


//@Component
//@EnableScheduling
public class ThreadPool implements Runnable {

    private SavePhotoService_old savePhotoService = GetBeanUtil.getApplicationContext().getBean(SavePhotoService_old.class);

    private Long taskNum;

    public ThreadPool(Long num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("正在处理的Id为: "+taskNum);
        try{
            Thread.sleep(1000);
        }catch (Exception e){

        }
//        savePhotoService.updataData(taskNum);
//        System.out.println("hx");
        try{
            Thread.sleep(1000);
        }catch (Exception e){

        }
        System.out.println("Id为:"+ this.taskNum +" 的数据处理完毕");
    }
}