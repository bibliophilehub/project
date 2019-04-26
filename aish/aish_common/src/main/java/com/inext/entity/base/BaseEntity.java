package com.inext.entity.base;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.Transient;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发人员：jzhang
 * 创建时间：2017-05-05 下午 14:57
 */
public abstract class BaseEntity<T> implements Serializable {
    /**
     * 存储数据
     */

    @Transient
    private Map _map = new HashMap();

    /**
     * 抽象方法 获取表名
     *
     * @return
     */
    @Transient
    public abstract TableName getTableName();

    /**
     * @return the map
     */
    public Map<String, Object> getMap() {
        return _map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map<String, Object> map) {
        this._map = map;
    }

    public <T> T clone(BaseEntity<T> cls) {
        T obj = null;
        try { // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            // 将流序列化成对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public Object clone() {
        Object obj = null;
        try { // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            // 将流序列化成对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
