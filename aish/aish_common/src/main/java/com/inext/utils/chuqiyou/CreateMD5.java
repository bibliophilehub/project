package com.inext.utils.chuqiyou;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CreateMD5 {
    //静态方法，便于作为工具类
    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
     }
    public static void main(String[] args){

//         StringBuilder sb = new StringBuilder();
//        // String ss = "http://192.168.124.41:8081/certification/operator?merchantId=51001&appId=10000&appKey*Ydsf%&(*$sdweA&userId=126710&userName=王福豪&mobile=17601050206&cardNo=230129199808141618&backUrl=http://api.jdq.zmcs168.com/moxie/callBackview&themeColor=F5C533";
//         String parMd5 = sb.append(OperatorConstant.TESTREQUESTURL).append("merchantId=").append("1000").append("&appId=").append("1001").append("&appKey=").append("RdaGu2ngPnAse6qN")
//                 .append("&userId=").append("7_126646").toString();
//         System.out.println(parMd5);
//        // System.out.println(ss);
//         System.out.println(CreateMD5.getMd5(parMd5));
//         //System.out.println(CreateMD5.getMd5(ss));
     }
}
