package com.fisher.base.gateway.util;

import com.google.common.util.concurrent.RateLimiter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fisher
 */
public class BaseRateLimiter {

    //每秒只发出5个令牌
    private static RateLimiter rateLimiter ;
    public  BaseRateLimiter (double qps){
        rateLimiter = RateLimiter.create(qps);
    }
    public  boolean tryAcquire(){
        return rateLimiter.tryAcquire();
    }

    public static void main(String[] args) {
        BaseRateLimiter br = new BaseRateLimiter(1);
        for(int i=0;i<10;i++) {
            System.out.println(br.access());
        }
    }

    public  String access(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //尝试获取令牌
        if(tryAcquire()){
            //模拟业务执行500毫秒
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return "aceess success [" + sdf.format(new Date()) + "]";
        }else{
            return "aceess limit [" + sdf.format(new Date()) + "]";
        }
    }

}
