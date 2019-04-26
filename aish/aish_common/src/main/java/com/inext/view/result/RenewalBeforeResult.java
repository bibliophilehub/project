package com.inext.view.result;

import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: jzhang
 * @Date: 2018-04-13 0013 下午 05:55
 */
public class RenewalBeforeResult extends BaseResult {


    @ApiModelProperty(value = "履约期限", example = "14")
    public  int renewalDay;
    @ApiModelProperty(value = "履约期限", example = "14")
    public int renewalMoney;

    public RenewalBeforeResult(int renewalDay, int renewalMoney) {
        this.renewalDay = renewalDay;
        this.renewalMoney = renewalMoney;
    }

    public int getRenewalDay() {
        return renewalDay;
    }

    public void setRenewalDay(int renewalDay) {
        this.renewalDay = renewalDay;
    }

    public int getRenewalMoney() {
        return renewalMoney;
    }

    public void setRenewalMoney(int renewalMoney) {
        this.renewalMoney = renewalMoney;
    }
}
