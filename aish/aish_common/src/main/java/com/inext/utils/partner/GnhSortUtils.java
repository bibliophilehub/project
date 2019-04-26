package com.inext.utils.partner;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by yinzhimin@u51.com
 */
public class GnhSortUtils {

    /**
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     * @param paraMap 要排序的Map对象
     */
    public static String formatUrlMap(Map<String, String> paraMap) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    Object val = item.getValue();
                    buf.append(key + "=" + val);
                    buf.append("&");
                }
            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

    /**
     * 字符串掩码
     * @param str 字符串
     * @param index 掩码后几位
     */
    public static String maskStr(String str, int index) {
        String start = str.substring(0, str.length() - index);
        return start + StringUtils.repeat("*", index);
    }

    /**
     * MD5加密
     * @param data 明文
     * @return 密文小写
     */
    public static String md5LowerCase(String data) {
        return DigestUtils.md5Hex(data).toLowerCase();
    }
}
