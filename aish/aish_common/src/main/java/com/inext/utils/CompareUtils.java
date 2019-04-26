package com.inext.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class CompareUtils {

    public static void main(String a[]) {
        DecimalFormat df = new DecimalFormat("###,###,###,###.##");
        System.out.println(df.format(new BigDecimal("12345678911.3")));
    }

    /**
     * 大于0
     *
     * @param dec1
     * @return dec1>0
     */
    public static boolean greaterThanZero(BigDecimal dec1) {
        if (dec1 == null) {
            return false;
        }
        return greaterThan(dec1, new BigDecimal(0));
    }

    /**
     * 大于等于0
     */
    public static boolean greaterThanAndEqualsZero(BigDecimal dec1) {
        if (dec1 == null) {
            return false;
        }
        return greaterEquals(dec1, new BigDecimal(0));
    }

    /**
     * 大于等于
     *
     * @return dec1 >= dec1
     */
    public static boolean greaterEquals(BigDecimal dec1, BigDecimal dec2) {
        if (dec1.compareTo(dec2) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 不等于
     *
     * @return dec1 != dec1
     */
    public static boolean notEquals(BigDecimal dec1, BigDecimal dec2) {
        if (dec1.compareTo(dec2) != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 不等于0
     *
     * @return dec1 != 0
     */
    public static boolean notEqualsZero(BigDecimal dec1) {
        if (dec1 == null) {
            return false;
        }
        return notEquals(dec1, new BigDecimal(0));
    }

    /**
     * 等于
     *
     * @param dec1
     * @param dec2
     * @return dec1 == dec1
     */
    public static boolean equals(BigDecimal dec1, BigDecimal dec2) {
        if (dec1.compareTo(dec2) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 大于
     *
     * @return dec1 > dec2
     */
    public static boolean greaterThan(BigDecimal dec1, BigDecimal dec2) {
        if (dec1.compareTo(dec2) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 大于等于
     *
     * @param date1
     * @param date2
     * @return date1 >= date2
     */
    public static boolean greaterEquals(Date date1, Date date2) {
        if (date1.compareTo(date2) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 大于
     *
     * @param date1
     * @param date2
     * @return date1 > date2
     */
    public static boolean greaterThan(Date date1, Date date2) {
        if (date1.compareTo(date2) == 1) {
            return true;
        } else {
            return false;
        }
    }
}
