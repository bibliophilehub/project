package com.inext.view.params;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author lisige
 */
public class ApplyCollectParams {

    @ApiModelProperty(value = "令牌 运营商认证接口返回的令牌 非自有系统登录下发的令牌", required = true, example = "79433992af1c4d9187417263d5642362")
    private String jxlToken;

    @ApiModelProperty(value = "站点 运营商认证接口返回的站点", required = true, example = "chinaunicom")
    private String website;

    @ApiModelProperty(value = "手机号码服务密码", required = true, example = "123456")
    private String operatorPassword;

    @ApiModelProperty(value = "北京移动需要的参数 其它的可以无视该参数", example = "123456")
    private String queryPwd;

    @ApiModelProperty(value = "运营商下发的短信验证码", required = true, example = "123456")
    private String smsCaptcha;

    public String getJxlToken() {
        return jxlToken;
    }

    public void setJxlToken(String jxlToken) {
        this.jxlToken = jxlToken;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOperatorPassword() {
        return operatorPassword;
    }

    public void setOperatorPassword(String operatorPassword) {
        this.operatorPassword = operatorPassword;
    }

    public String getQueryPwd() {
        return queryPwd;
    }

    public void setQueryPwd(String queryPwd) {
        this.queryPwd = queryPwd;
    }

    public String getSmsCaptcha() {
        return smsCaptcha;
    }

    public void setSmsCaptcha(String smsCaptcha) {
        this.smsCaptcha = smsCaptcha;
    }
}
