package com.inext.view.result;

import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: jzhang
 * @Date: 2018-04-13 0013 下午 05:07
 */
@ApiModel("订单创建前信息")
public class OrderBeforeResult extends BaseResult {
    @ApiModelProperty(value = "设备名称", example = "1")
    private String equipmentName;
    @ApiModelProperty(value = "预付款 单位(元)", example = "850.00")
    private String perPayMoney;

    @ApiModelProperty(value = "违约金", example = "150.00")
    private String penaltyAmount;

    @ApiModelProperty(value = "履约期限", example = "14")
    private String PerformanceDay;

    @ApiModelProperty(value = "银行卡号", example = "14")
    private String userCardCode;
    @ApiModelProperty(value = "银行名称", example = "14")
    private String userCardName;

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getPerPayMoney() {
        return perPayMoney;
    }

    public void setPerPayMoney(String perPayMoney) {
        this.perPayMoney = perPayMoney;
    }

    public String getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(String penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public String getPerformanceDay() {
        return PerformanceDay;
    }

    public void setPerformanceDay(String performanceDay) {
        PerformanceDay = performanceDay;
    }

    public String getUserCardCode() {
        return userCardCode;
    }

    public void setUserCardCode(String userCardCode) {
        this.userCardCode = userCardCode;
    }

    public String getUserCardName() {
        return userCardName;
    }

    public void setUserCardName(String userCardName) {
        this.userCardName = userCardName;
    }
}
