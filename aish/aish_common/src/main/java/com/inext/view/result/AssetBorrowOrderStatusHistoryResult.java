package com.inext.view.result;


import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lisige
 */
@ApiModel(value = "订单状态历史")
public class AssetBorrowOrderStatusHistoryResult extends BaseResult {

    @ApiModelProperty(value = "发生时间", readOnly = true, example = "2018-01-01 21:28:29")
    private String dateTime;

    @ApiModelProperty(value = "状态展示文字", readOnly = true, example = "订单已提交")
    private String statusStr;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
}
