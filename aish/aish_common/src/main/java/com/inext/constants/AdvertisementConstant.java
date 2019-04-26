package com.inext.constants;

import java.util.Map;
import java.util.TreeMap;

/**
 * 错误信息常类
 *
 * @author user
 */
public class AdvertisementConstant {

    public final static Integer CODE_001 = 1;
    public final static String MSG_001 = "APP-闪屏广告";
    public final static String INDEX_001 = "4";

    public final static Integer CODE_002 = 2;
    public final static String MSG_002 = "APP-弹窗广告";
    public final static String INDEX_002 = "5";


    public final static Integer CODE_003 = 3;
    public final static String MSG_003 = "APP-活动专区广告";
    public final static String INDEX_003 = "2";


    public final static Integer CODE_004 = 4;
    public final static String MSG_004 = "APP-帖子广告";
    public final static String INDEX_004 = "3";


    public final static Integer CODE_005 = 5;
    public final static String MSG_005 = "APP-首页Banner";
    public final static String INDEX_005 = "1";


    public final static Integer CODE_006 = 6;
    public final static String MSG_006 = "APP-实时资讯";
    public final static String INDEX_006 = "6";


    public final static Integer CODE_007 = 7;
    public final static String MSG_007 = "WX-小额借贷-Banner";
    public final static String INDEX_007 = "7";


    public final static Integer CODE_008 = 8;
    public final static String MSG_008 = "WX-新口子借款-Banner";
    public final static String INDEX_008 = "8";


    public static Map<Integer, String> typeMap = new TreeMap<Integer, String>();// 类型    

    static {
        typeMap.put(CODE_001, MSG_001);
        typeMap.put(CODE_002, MSG_002);
        typeMap.put(CODE_003, MSG_003);
        typeMap.put(CODE_004, MSG_004);
        typeMap.put(CODE_005, MSG_005);
        typeMap.put(CODE_006, MSG_006);
        typeMap.put(CODE_007, MSG_007);
        typeMap.put(CODE_008, MSG_008);
    }

    public static Map<Integer, String> getTypeMap() {
        return typeMap;
    }

    public static void setTypeMap(Map<Integer, String> typeMap) {
        AdvertisementConstant.typeMap = typeMap;
    }

    public Integer getCode(String indexVal) {

        if (INDEX_001.equals(indexVal)) {
            return CODE_001;
        }
        if (INDEX_002.equals(indexVal)) {
            return CODE_002;
        }
        if (INDEX_003.equals(indexVal)) {
            return CODE_003;
        }
        if (INDEX_004.equals(indexVal)) {
            return CODE_004;
        }
        if (INDEX_005.equals(indexVal)) {
            return CODE_005;
        }
        if (INDEX_006.equals(indexVal)) {
            return CODE_006;
        }
        if (INDEX_007.equals(indexVal)) {
            return CODE_007;
        }
        if (INDEX_008.equals(indexVal)) {
            return CODE_008;
        }

        return 0;
    }

}
