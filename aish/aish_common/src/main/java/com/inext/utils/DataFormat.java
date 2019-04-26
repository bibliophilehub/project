package com.inext.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;


public class DataFormat {
    /**
     * 把金额按照位数nums拆分添加","号
     *
     * @param amount
     * @param nums
     * @return
     */
    public static String getAmount(String amount, int nums) {
        // boolean bool = false;
        if (null == amount) {
            return "0";
        }
        if (amount.length() <= nums) {
            if (!amount.contains(".")) {
                return amount + ".00";
            }
            return amount;
        } else {
            String xamount = ".00";

            if (amount.contains(".")) {
                // bool = true;
                String reAmount = amount;
                amount = amount.substring(0, amount.indexOf("."));
                xamount = reAmount.substring(reAmount.indexOf("."), reAmount
                        .length());
                if (xamount.length() > nums) {
                    xamount = xamount.substring(0, 3);
                }
                if (amount.length() <= nums) {
                    return amount + xamount;
                }
            }
            StringBuffer sb = new StringBuffer();
            int rem = amount.length() % nums;
            int length = amount.length() / nums;
            if (rem > 0) {
                String subAmount = amount.substring(0, rem);
                sb.append(subAmount).append(",");
                amount = amount.substring(rem, amount.length());
            }
            for (int i = 0; i < length; i++) {
                String subStr = amount.substring(0, nums);
                if (i == length - 1) {
                    sb.append(subStr);
                } else {
                    sb.append(subStr).append(",");
                }
                amount = amount.substring(nums, amount.length());
            }
            // if(bool){
            // return sb.toString()+".00";
            // }
            sb.append(xamount);
            return sb.toString();
        }
    }

    public static String getAmount(double amountd, int nums) {
        String amount = String.valueOf(amountd);

        // boolean bool = false;
        if (null == amount) {
            return "0";
        }
        if (amount.length() <= nums) {
            if (!amount.contains(".")) {
                return amount + ".00";
            }
            return amount;
        } else {
            String xamount = ".00";
            if (amount.contains(".")) {
                // bool = true;
                String reAmount = amount;
                amount = amount.substring(0, amount.indexOf("."));
                xamount = reAmount.substring(reAmount.indexOf("."), reAmount
                        .length());
                if (".0".equals(xamount)) {
                    xamount = ".00";
                }
                if (xamount.length() > nums) {
                    xamount = xamount.substring(0, 3);
                }
                if (amount.length() <= nums) {
                    return amount + xamount;
                }
            }
            StringBuffer sb = new StringBuffer();
            int rem = amount.length() % nums;
            int length = amount.length() / nums;
            if (rem > 0) {
                String subAmount = amount.substring(0, rem);
                sb.append(subAmount).append(",");
                amount = amount.substring(rem, amount.length());
            }
            for (int i = 0; i < length; i++) {
                String subStr = amount.substring(0, nums);
                if (i == length - 1) {
                    sb.append(subStr);
                } else {
                    sb.append(subStr).append(",");
                }
                amount = amount.substring(nums, amount.length());
            }
            // if(bool){
            // return sb.toString()+".00";
            // }
            sb.append(xamount);
            return sb.toString();
        }
    }

    /**
     * 保留两位小数
     *
     * @return
     */
    public static String getDataNumber(double number) {
        // fyc20160929修改，0.0时会变成.00
        // DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(number);
    }

    /**
     * 保留两位小数
     *
     * @return
     */
    public static double getDataNumber(String number) {
        return Double.parseDouble(new DecimalFormat("#.00").format(Double
                .parseDouble(number)));
    }

    /**
     * 全市场占比计算
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String getDataForString(String str1, String str2) {
        if (StringUtils.isNotBlank(str2)) {
            double dou1 = 0;
            double dou2 = 0;
            try {
                dou1 = Double.parseDouble(str1);
            } catch (Exception e) {
            }
            try {
                dou2 = Double.parseDouble(str2);
            } catch (Exception e) {
            }
            if (dou2 == 0) {
                return "0.00";
            }
            double dou3 = (dou1 / dou2) * 100;
            String d = new DecimalFormat("#.00").format(dou3);
            return d.startsWith(".") ? 0 + d : d;
        } else {
            return "0.00";
        }
    }

    /**
     * 格式化身份证
     *
     * @return
     */
    public static String formatCartNum(String cartNum) {
        if (StringUtils.isNotBlank(cartNum)) {
            int strNum = 6;
            String string0 = "";
            String string1 = "";
            String string2 = "";
            int le = cartNum.length();
            string0 = cartNum.substring(0, strNum);
            string2 = cartNum.substring(le - strNum, le);
            for (int i = 0; i < le - (strNum * 2); i++) {
                string1 = string1 + "*";
            }
            return string0 + string1 + string2;
        }
        return null;
    }

    /**
     * 格式化数字
     *
     * @param number如果为null或者""则返回默认值
     * @param pattern                 为空则默认###,###,###,###.##
     * @param defaultValue            默认值如果为空则返回去null，其他返回传入值
     * @return
     */
    public static String format(Object number, String pattern, String defaultValue) {
        if (StringUtils.isBlank(pattern)) {
            pattern = "###,###,###,###.##";
        }
        if (StringUtils.isBlank(defaultValue)) {
            defaultValue = null;
        }
        if (number == null || "".equals(number)) {
            return defaultValue;
        } else {
            DecimalFormat df = new DecimalFormat(pattern);
            return df.format(number);
        }

    }

    /**
     * 用户手机号隐藏
     * @param phoneNo
     * @return
     */
    public static String  getUserPhoneNo(String phoneNo)
    {
        if(null!=phoneNo && !"".equals(phoneNo) && phoneNo.length()>6)
            return phoneNo.replaceAll("(\\d{3})\\d{4}","$1****");
        else
            return phoneNo;
    }

    public static void main(String[] args) {
        System.out.println(DataFormat.getAmount("127000000", 3));
    }
}
