package com.inext.view.result;


import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lisige
 */
@ApiModel("订单")
public class AssetBorrowOrderResult extends BaseResult {

    @ApiModelProperty(value = "订单ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "设备名称", example = "iphone 7")
    private String deviceModel;

    @ApiModelProperty(value = "时间", example = "2018-01-01 10:00:00")
    private String dateTime;

    @ApiModelProperty(value = "状态 {1=平台审核中, 5=审核拒绝, 8=已取消, 11=已完成, 2=待打款, 3=待寄出, 7=已违约, 10=已寄出}", example = "1")
    private Integer status;

    @ApiModelProperty(value = "状态展示字符", example = "审核中")
    private String statusStr;

    @ApiModelProperty(value = "金额 单位(元)", example = "850.00")
    private String perPayMoney;

    @ApiModelProperty(value = "平台费率 单位(元)", example = "750.00")
    private String penaltyAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
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
}
