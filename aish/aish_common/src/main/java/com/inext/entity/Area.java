package com.inext.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Area implements Serializable {

    private static final long serialVersionUID = 732733664122020540L;
    public static List<Map<String, Object>> provinceCityList;//与数据库无关的参数 生成前端需要的 地址json字符串
    private Integer id;
    private String areaName;
    private String areaCode;
    private Integer areaSequence;
    private Integer parentId;
    private String areaDomain;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getAreaSequence() {
        return areaSequence;
    }

    public void setAreaSequence(Integer areaSequence) {
        this.areaSequence = areaSequence;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getAreaDomain() {
        return areaDomain;
    }

    public void setAreaDomain(String areaDomain) {
        this.areaDomain = areaDomain;
    }
}
