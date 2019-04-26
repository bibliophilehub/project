package com.inext.entity;

import javax.persistence.Id;
import java.util.Date;

/**
 * 开发人员：jzhang 创建时间：2017-05-08 下午 14:46
 */
public class ChannelCountReport {

    @Id
    private Integer id;
    private Date createTime;
    private String channelName;
    private String channelId;
    private Long channelPathId;
    private String channelUrlName;
    private String registerNumber;
    private String syjRegisterNumber;
    private String newApplyPersonNumber;
    private String newApplyProductNumber;
    private String applyPersonNumber;
    private String applyProductNumber;
    private String productNumber;
    private String lastUpdateTime;

    private Integer channelType;
    private Double coefficient;

    private Integer registerTodayAddNumber;
    private Double registerTodayAddRate;
    private Double newApplyPersonConverRate;
    private Integer newApplyProductAddNumber;
    private Double newApplyProductAddRate;
    private Double newApplyPersonAvgProductNumber;
    private Integer userCount;
    private Integer loginNumber;
    private Double applyPersonConverRate;
    private Integer applyProductAddNumber;
    private Double applyProductAddRate;
    private Double applyPersonAvgProductNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelUrlName() {
        return channelUrlName;
    }

    public void setChannelUrlName(String channelUrlName) {
        this.channelUrlName = channelUrlName;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getSyjRegisterNumber() {
        return syjRegisterNumber;
    }

    public void setSyjRegisterNumber(String syjRegisterNumber) {
        this.syjRegisterNumber = syjRegisterNumber;
    }

    public String getNewApplyPersonNumber() {
        return newApplyPersonNumber;
    }

    public void setNewApplyPersonNumber(String newApplyPersonNumber) {
        this.newApplyPersonNumber = newApplyPersonNumber;
    }

    public String getNewApplyProductNumber() {
        return newApplyProductNumber;
    }

    public void setNewApplyProductNumber(String newApplyProductNumber) {
        this.newApplyProductNumber = newApplyProductNumber;
    }

    public String getApplyPersonNumber() {
        return applyPersonNumber;
    }

    public void setApplyPersonNumber(String applyPersonNumber) {
        this.applyPersonNumber = applyPersonNumber;
    }

    public String getApplyProductNumber() {
        return applyProductNumber;
    }

    public void setApplyProductNumber(String applyProductNumber) {
        this.applyProductNumber = applyProductNumber;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Long getChannelPathId() {
        return channelPathId;
    }

    public void setChannelPathId(Long channelPathId) {
        this.channelPathId = channelPathId;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Integer getRegisterTodayAddNumber() {
        return registerTodayAddNumber;
    }

    public void setRegisterTodayAddNumber(Integer registerTodayAddNumber) {
        this.registerTodayAddNumber = registerTodayAddNumber;
    }

    public Double getRegisterTodayAddRate() {
        return registerTodayAddRate;
    }

    public void setRegisterTodayAddRate(Double registerTodayAddRate) {
        this.registerTodayAddRate = registerTodayAddRate;
    }

    public Double getNewApplyPersonConverRate() {
        return newApplyPersonConverRate;
    }

    public void setNewApplyPersonConverRate(Double newApplyPersonConverRate) {
        this.newApplyPersonConverRate = newApplyPersonConverRate;
    }

    public Integer getNewApplyProductAddNumber() {
        return newApplyProductAddNumber;
    }

    public void setNewApplyProductAddNumber(Integer newApplyProductAddNumber) {
        this.newApplyProductAddNumber = newApplyProductAddNumber;
    }

    public Double getNewApplyProductAddRate() {
        return newApplyProductAddRate;
    }

    public void setNewApplyProductAddRate(Double newApplyProductAddRate) {
        this.newApplyProductAddRate = newApplyProductAddRate;
    }

    public Double getNewApplyPersonAvgProductNumber() {
        return newApplyPersonAvgProductNumber;
    }

    public void setNewApplyPersonAvgProductNumber(Double newApplyPersonAvgProductNumber) {
        this.newApplyPersonAvgProductNumber = newApplyPersonAvgProductNumber;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getLoginNumber() {
        return loginNumber;
    }

    public void setLoginNumber(Integer loginNumber) {
        this.loginNumber = loginNumber;
    }

    public Double getApplyPersonConverRate() {
        return applyPersonConverRate;
    }

    public void setApplyPersonConverRate(Double applyPersonConverRate) {
        this.applyPersonConverRate = applyPersonConverRate;
    }

    public Integer getApplyProductAddNumber() {
        return applyProductAddNumber;
    }

    public void setApplyProductAddNumber(Integer applyProductAddNumber) {
        this.applyProductAddNumber = applyProductAddNumber;
    }

    public Double getApplyProductAddRate() {
        return applyProductAddRate;
    }

    public void setApplyProductAddRate(Double applyProductAddRate) {
        this.applyProductAddRate = applyProductAddRate;
    }

    public Double getApplyPersonAvgProductNumber() {
        return applyPersonAvgProductNumber;
    }

    public void setApplyPersonAvgProductNumber(Double applyPersonAvgProductNumber) {
        this.applyPersonAvgProductNumber = applyPersonAvgProductNumber;
    }


}
