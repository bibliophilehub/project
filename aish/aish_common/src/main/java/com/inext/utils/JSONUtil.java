package com.inext.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;

/**
 * JSON工具类
 */
public class JSONUtil {

    /**
     * javabean转map
     *
     * @param javaBean
     * @return
     */
    public static Map<String, String> beanToMap(Object javaBean) {
        Map<String, String> result = new HashMap<String, String>();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());
                }
            } catch (Exception e) {
            }
        }

        return result;
    }

    /**
     * json对象转换成Map
     *
     * @param jsonObject
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> jsonToMap(JSONObject jsonObject) {
        Map<String, String> result = new HashMap<String, String>();
        Iterator<String> iterator = jsonObject.keys();
        String key = null;
        String value = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);
        }
        return result;
    }

    /**
     * JSON 对象字符格式转换成java对象
     *
     * @param jsonString
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(String jsonString, Class<T> beanClass) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        T bean = (T) JSONObject.toBean(jsonObject, beanClass);
        return bean;
    }

    /**
     * java对象转换成json字符串
     *
     * @param bean
     * @return
     */
    public static String beanToJson(Object bean) {
        JSONObject json = JSONObject.fromObject(bean);
        return json.toString();
    }

    /**
     * java对象转换成json
     *
     * @param bean
     * @return
     */
    public static JSONObject beanToJsonObj(Object bean) {
        JSONObject json = JSONObject.fromObject(bean);
        return json;
    }

    /**
     * java对象List集合转换成json字符串
     *
     * @param beans
     * @return
     */

    @SuppressWarnings("rawtypes")
    public static String beanListToJson(List beans) {
        StringBuffer rest = new StringBuffer();
        rest.append("[");
        int size = beans.size();
        for (int i = 0; i < size; i++) {
            rest.append(beanToJson(beans.get(i)) + ((i < size - 1) ? "," : ""));
        }
        rest.append("]");
        return rest.toString();
    }

    /**
     * String 转List<T>
     *
     * @param <T>
     * @param jsonArray
     * @param beanClsss
     * @return
     */
    public static <T> List<T> jsonArrayToBean(String jsonArray,
                                              Class<T> beanClsss) {
        JSONArray jsonArr = JSONArray.fromObject(jsonArray);
        List<T> beanList = new ArrayList<T>();
        for (int i = 0; i < jsonArr.size(); i++) {
            T bean = jsonToBean(String.valueOf(jsonArr.get(i)), beanClsss);
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 输出json
     *
     * @param response
     * @param object
     */
    public static void toObjectJson(HttpServletResponse response, String json) {
        try {
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (Exception e) {

        }
    }

    /**
     * json字符串转换成map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJSON2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }
}
