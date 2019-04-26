package com.inext.view.result;


import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lisige
 */
@ApiModel(value = "卡类别")
public class CardBinResult extends BaseResult {

    @ApiModelProperty(value = "face++ 识别出来的身份证号码", example = "430521199509284319")
    private String bankId;

    @ApiModelProperty(value = "银行卡类别描述", example = "中国建设银行")
    private String bankDescription;

    public CardBinResult() {
    }

    public CardBinResult(String bankId, String bankDescription) {
        this.bankId = bankId;
        this.bankDescription = bankDescription;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankDescription() {
        return bankDescription;
    }

    public void setBankDescription(String bankDescription) {
        this.bankDescription = bankDescription;
    }
}
