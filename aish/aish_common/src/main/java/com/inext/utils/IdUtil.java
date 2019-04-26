package com.inext.utils;

import java.util.Random;

public class IdUtil {
    /**
     * 根据Id获取自增长id
     *
     * @param initId 初始id  000001  U00001
     * @param prefix 前缀数     0       1
     * @return 自增id        000002  U00002
     */
    public static String getId(String initId, int prefix) {
        return getId(initId, initId.length(), prefix);
    }

    /**
     * 根据Id获取自增长id
     *
     * @param initId 初始id  000001  U00001
     * @param digit  位数          6       6
     * @param prefix 前缀数     0       1
     * @return 自增id        000002  U00002
     */
    public static String getId(String initId, int digit, int prefix) {
        return initId.substring(0, prefix) + String.format("%0" + (digit - prefix) + "d", Integer.parseInt(initId.substring(prefix)) + 1);
    }

    /**
     * 根据Id获取日期前缀自增长id
     *
     * @param initId 初始id   SUL170817000042
     * @param digit  位数               15
     * @param prefix 前缀               3
     * @return 自增id        SUL170817000043
     */
    public static String getIdDate(String initId, int digit, int prefix) {
        return initId.substring(0, prefix) + DateUtils.getDate(DateUtils.FORMAT) + String.format("%0" + (digit - prefix - 6) + "d", Integer.parseInt(initId.substring(prefix + 6)) + 1);
    }

    public static String getRandomString(int length){
        //1.  定义一个字符串（A-Z，a-z，0-9）即62个数字字母；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //2.  由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //3.  长度为几就循环几次
        for(int i=0; i<length; ++i){
            //从62个的数字或字母中选择
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }
}
