package com.inext.view.result;


import com.inext.result.BaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lisige
 */
@ApiModel(value = "文件上传返回值")
public class IdCardResult extends BaseResult {

    @ApiModelProperty(value = "上传后 完整的可公网访问的文件路径", readOnly = true, example = "http://p6wppefop.bkt.clouddn.com///files/xjp_02/02_15/02_15_954/newfiles/20180410/20180410104049_pk2ji4wutj_appTh.png")
    private String fullFileUrl;

    @ApiModelProperty(value = "face++ 识别出来的身份证号码", readOnly = true, example = "430521199509284319")
    private String idCardNumber;

    @ApiModelProperty(value = "face++ 识别出来的姓名", readOnly = true, example = "张三")
    private String realName;

    public IdCardResult() {
    }

    public IdCardResult(String fullFileUrl) {
        this.fullFileUrl = fullFileUrl;
    }

    public IdCardResult(String fullFileUrl, String idCardNumber, String realName) {
        this.fullFileUrl = fullFileUrl;
        this.idCardNumber = idCardNumber;
        this.realName = realName;
    }

    public String getFullFileUrl() {
        return fullFileUrl;
    }

    public void setFullFileUrl(String fullFileUrl) {
        this.fullFileUrl = fullFileUrl;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
