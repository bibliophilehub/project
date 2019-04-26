package com.inext.constants;

import com.inext.utils.StringUtils;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanStatus {
    public final static List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    public static Integer borrowingStatus_01 = new Integer("1"); // 未认证
    public static Integer borrowingStatus_02 = new Integer("2"); // 认证中
    public static Integer borrowingStatus_03 = new Integer("3"); // 认证成功
    public static Integer borrowingStatus_04 = new Integer("4"); // 认证失败
    public static Integer borrowingStatus_05 = new Integer("5"); // 未申请
    public static Integer borrowingStatus_06 = new Integer("6"); // 审核中
    public static Integer borrowingStatus_07 = new Integer("7"); // 审核通过
    public static Integer borrowingStatus_08 = new Integer("8"); // 审核拒绝
    public static Integer borrowingStatus_09 = new Integer("9"); // 放款中
    public static Integer borrowingStatus_10 = new Integer("10"); // 已放款
    public static Integer borrowingStatus_11 = new Integer("11"); // 放款失败
    public static Integer borrowingStatus_12 = new Integer("12"); // 未到期
    public static Integer borrowingStatus_13 = new Integer("13"); // 还款中
    public static Integer borrowingStatus_14 = new Integer("14"); // 已还款
    public static Integer borrowingStatus_15 = new Integer("15"); // 逾期

    static {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "1");
        map.put("name", "未认证");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "2");
        map.put("name", "认证中");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "3");
        map.put("name", "认证成功");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "4");
        map.put("name", "认证失败");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "5");
        map.put("name", "未申请");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "6");
        map.put("name", "审核中");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "7");
        map.put("name", "审核通过");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "8");
        map.put("name", "审核拒绝");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "9");
        map.put("name", "放款中");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "10");
        map.put("name", "已放款");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "11");
        map.put("name", "放款失败");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "12");
        map.put("name", "未到期");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "13");
        map.put("name", "还款中");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "14");
        map.put("name", "已还款");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("code", "15");
        map.put("name", "逾期");
        list.add(map);
    }

    public static String getLoanValue(String key) {
        String borrowingStatus = "";
        for (Map<String, Object> map : list) {
            if (StringUtils.getString(map.get("code")).equals(key)) {
                borrowingStatus = StringUtils.getString(map.get("name"));
            }
        }
        return borrowingStatus;
    }

    public static String getLoanStatus() {
        JSONArray jaArray = JSONArray.fromObject(list);
        return jaArray.toString();
    }

    public static void main(String[] args) {
        //System.out.println(LoanStatus.getLoanStatus());
    }
}
