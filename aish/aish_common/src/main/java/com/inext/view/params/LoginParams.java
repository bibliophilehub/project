package com.inext.view.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lisige
 */
@ApiModel("登录参数")
public class LoginParams {

    @ApiModelProperty(value = "手机号码", required = true, example = "13636458637")
    private String mobile;

    @ApiModelProperty(value = "密码 DES公钥加密", required = true, example = "im8/1pvUtgs=")
    private String encryptionPassword;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getEncryptionPassword() {
        return encryptionPassword;
    }

    public void setEncryptionPassword(String encryptionPassword) {
        this.encryptionPassword = encryptionPassword;
    }
}
