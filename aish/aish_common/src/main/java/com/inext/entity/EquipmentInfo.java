package com.inext.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * describe:
 *
 * @author mlk
 * @date 2018/03/23
 */
@Table(name="app_equipment_info")
public class EquipmentInfo implements Serializable {

    //违约金
    public static final String WY_PRICE="150";
    //履约期限
    public static final String LY_DAY="14";
    @Id
    private Integer id;
    private String equipmentName;// '设备型号',
    private String piceUrl;// '图片地址',
    private String memory;//'内存',
    private String degree;//'新旧程度',
    private BigDecimal price;//参考价格',
    private Date createTime;//'创建时间',


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getPiceUrl() {
        return piceUrl;
    }

    public void setPiceUrl(String piceUrl) {
        this.piceUrl = piceUrl;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
