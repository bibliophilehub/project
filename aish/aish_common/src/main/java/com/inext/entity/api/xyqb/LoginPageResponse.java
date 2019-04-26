package com.inext.entity.api.xyqb;

/**
 * 响应参数实体示例
 */
public class LoginPageResponse {
    private String sign;
    private String bizResult;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBizResult() {
        return bizResult;
    }

    public void setBizResult(String bizResult) {
        this.bizResult = bizResult;
    }

    public static class BizResult {
        private Integer code;
        private String msg;
        private String loginUrl;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getLoginUrl() {
            return loginUrl;
        }

        public void setLoginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
        }
    }
}
