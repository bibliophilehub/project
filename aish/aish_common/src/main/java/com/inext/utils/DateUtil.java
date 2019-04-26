package com.inext.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具
 *
 * @author gaoyuhai 2016-6-14 上午09:23:37
 */
public class DateUtil {
    public static String DEFAULT_FORMAT = "yyyy-MM-dd";
    public static String DEFAULT_FORMAT_INFO = "yyyy年MM月dd日";
    /**
     * yyyyMMdd
     */
    public static String FORMAT1 = "yyyyMMdd";
    public static String timePattern2 = "yyyyMMddHHmmss";
    public static String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    private static String timePattern = "HH:mm";

    /**
     * 获取当前时间 format 格式
     *
     * @return
     */
    public static String getDateFormat(String format) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        String time = df.format(date);
        return time;
    }

    /**
     * 获取某时间-当前时间 format 格式
     *
     * @return
     * @throws ParseException
     */
    public static int getDateFormat(String endDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long days = 0;
        try {
            Date eDate = sdf.parse(endDate);
            Date date = sdf.parse(getDateFormat(format));

            long diff = eDate.getTime() - date.getTime();
            days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {

        }
        return (int) days;
    }

    /**
     * 获取某时间-某时间 format 格式
     *
     * @return
     * @throws ParseException
     */
    public static String getDateFormat(String endDate0, String endDate1,
                                       String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long days = 0;
        try {
            Date eDate0 = sdf.parse(endDate0);
            Date eDate1 = sdf.parse(endDate1);
            long diff = eDate1.getTime() - eDate0.getTime();
            days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {

        }
        return String.valueOf(days);
    }

    /**
     * 获取某时间-某时间 format 格式
     *
     * @return
     * @throws ParseException
     */
    public static int getDateFormats(String endDate0, String endDate1, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long days = 0;
        try {
            Date eDate0 = sdf.parse(endDate0);
            Date eDate1 = sdf.parse(endDate1);
            long diff = eDate1.getTime() - eDate0.getTime();
            days = diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {

        }
        return (int) days;
    }

    /**
     * 获取下n月
     *
     * @return
     */
    public static Date getNextMon(int month) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +month);
        Date time = calendar.getTime();
        return time;
    }

    /**
     * 获取前days日
     *
     * @param days
     * @return
     */
    public static String getDateForDayBefor(int days, String format) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        SimpleDateFormat df = new SimpleDateFormat(format);
        String time = df.format(calendar.getTime());
        return time;
    }

    public static void main(String[] args) {
        String str = getDateForDayBefor(1, "yyyy-MM-dd");
        System.out.println(getDateFormat("yyyyMMddHHmmss"));//
    }

    /**
     * 时间相加
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static Date addHour(Date date, int hour) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    public static Date addMinute(Date date, int minute) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static Date addSecond(Date date, int second) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }


    /**
     * 月相加
     *
     * @param date
     * @param month
     * @return
     */
    public static Date addMonth(Date date, int month) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 按照日期格式，将字符串解析为日期对象
     *
     * @param strDate (格式 yyyy-MM-dd)
     * @return
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate)
            throws ParseException {
        Date aDate = null;

        try {
//			if (log.isDebugEnabled()) {
//				log.debug("converting date with pattern: " + DEFAULT_FORMAT);
//			}

            aDate = convertStringToDate(dateTimePattern, strDate);
        } catch (ParseException pe) {
//			log.error("Could not convert '" + strDate
//					+ "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());

        }

        return aDate;
    }

    /**
     * 按照日期格式，将字符串解析为日期对象
     *
     * @param aMask   输入字符串的格式
     * @param strDate 一个按aMask格式排列的日期的字符串描述
     * @return Date 对象
     * @throws ParseException
     * @see SimpleDateFormat
     */
    public static final Date convertStringToDate(String aMask, String strDate)
            throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

//		if (log.isDebugEnabled()) {
//			log.debug("converting '" + strDate + "' to date with mask '"
//					+ aMask + "'");
//		}

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            // log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    public static String formatDate(String formatPattern, Date date) {
        SimpleDateFormat f = new SimpleDateFormat(formatPattern);
        String sDate = date == null ? "" : f.format(date);
        return sDate;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 根据字符串和指定格式生成日期
     *
     * @return
     * @throws ParseException
     */
    public static Date getDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
