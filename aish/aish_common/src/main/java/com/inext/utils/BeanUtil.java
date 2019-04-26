package com.inext.utils;

import tk.mybatis.mapper.util.StringUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangj on 2017/4/17 0017.
 * 没有匹配的类型 请在doBeanType 处理了数据
 */
public class BeanUtil {
    /**
     * Map集合对象转化成 JavaBean集合对象
     *
     * @param javaBean JavaBean实例对象
     * @param mapList  Map数据集对象
     * @return
     * @author jqlin
     */
    public static <T> List<T> map2BeanForList(Class<T> javaBean, List<Map> mapList) {
        if (mapList == null || mapList.isEmpty()) {
            return null;
        }
        List<T> objectList = new ArrayList<T>();

        T object = null;
        for (Map map : mapList) {
            if (map != null) {
                object = map2Bean(javaBean, map);
                objectList.add(object);
            }
        }

        return objectList;

    }

    /**
     * Map对象转化成 JavaBean对象
     *
     * @param javaBean JavaBean实例对象
     * @param map      Map对象
     * @return
     * @author jqlin
     */
    public static <T> T map2Bean(Class<T> javaBean, Map map) {
        try {
            // 获取javaBean属性
            BeanInfo beanInfo = Introspector.getBeanInfo(javaBean);
            // 创建 JavaBean 对象
            Object obj = javaBean.newInstance();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null && propertyDescriptors.length > 0) {
                String propertyName = null; // javaBean属性名
                String propertyValue = null; // javaBean属性值
                for (PropertyDescriptor pd : propertyDescriptors) {
                    propertyValue = null;
                    propertyName = pd.getName();
                    try {
                        if (map.containsKey(propertyName)) {
                            propertyValue = map.get(propertyName).toString();
                        } else if (map.containsKey(StringUtil.camelhumpToUnderline(propertyName))) {//驼峰式判断
                            propertyValue = map.get(StringUtil.camelhumpToUnderline(propertyName)).toString();
                        }
                        if (StringUtils.isNoneBlank(propertyValue)) {
                            Method writeMethod = pd.getWriteMethod();
                            Object val = doBeanType(propertyValue, writeMethod);
                            if (val != null)
                                writeMethod.invoke(obj, val);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return (T) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JavaBean对象转化成Map对象
     *
     * @param javaBean
     * @return
     * @author jqlin
     */
    public static Map Bean2Map(Object javaBean) {
        Map map = new HashMap();

        try {
            // 获取javaBean属性
            BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null && propertyDescriptors.length > 0) {
                String propertyName = null; // javaBean属性名
                Object propertyValue = null; // javaBean属性值
                for (PropertyDescriptor pd : propertyDescriptors) {
                    propertyName = pd.getName();
                    if (!propertyName.equals("class")) {
                        Method readMethod = pd.getReadMethod();
                        propertyValue = readMethod.invoke(javaBean, new Object[0]);
                        map.put(propertyName, propertyValue);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 处理实体映射类型
     *
     * @param propertyValue
     * @param writeMethod
     * @return
     */
    private static Object doBeanType(String propertyValue, Method writeMethod) {
        Class<?>[] parameterTypes = writeMethod.getParameterTypes();
        Object obj = null;
        String beanType = parameterTypes[0].getName();
        //System.out.println(beanType);

        try {
            switch (beanType) {
                case "boolean"://数据库存储 为 0  1的 情况
                    if (propertyValue == "0") {
                        obj = false;
                    } else if (propertyValue == "1") {
                        obj = true;
                    } else {
                        obj = Boolean.valueOf(propertyValue);
                    }
                    break;
                case "int":
                    obj = Integer.parseInt(propertyValue);
                    break;
                case "java.lang.Integer":
                    if (propertyValue != null) {
                        obj = Integer.parseInt(propertyValue);
                    }
                    break;
                case "java.util.Date":
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {

                        obj = sdf.parse(propertyValue);
                    } catch (Exception e) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                        obj = sdf.parse(propertyValue);
                    }
                    break;
                default:
                    obj = propertyValue;
            }


        } catch (Exception e) {

        }
        return obj;
    }

    /**
     * 没有匹配的类型 请在doBeanType 处理了数据
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.print(MD5Utils.stringToMD5("123456"));
    }
}
