package com.inext.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class OrderNoUtil {
    public static OrderNoUtil orderNoUtil;

    public static OrderNoUtil getInstance() {
        if (orderNoUtil == null) {
            orderNoUtil = new OrderNoUtil();
        }
        return orderNoUtil;
    }

    /**
     * 获得UUID
     *
     * @return
     */
    public String getUUID() {
        return UUID.randomUUID().toString();
    }


    /**
     * 获得UUID 前16位
     *
     * @return
     */
    public String getUUID16() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }

    /**
     * 生成商户付款流水号16位，是当前的年月日时分秒+2位不重复的随机数
     * @return
     */
    public static String payRecordNo(){
        try{

            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString=sdf.format(new Date());
            String randomString= String.valueOf(Math.random()).substring(2).substring(0, 2);

            return dateString+randomString;
        }catch (Exception e) {
            System.out.println("生成充值流水号出错===="+e.toString());
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(payRecordNo());

        System.out.println(DateUtils.getDate(DateUtils.timePattern2));
    }


}
