package com.inext.result;


import com.inext.enumerate.ApiStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;

/**
 * @author lisige
 */
@ApiModel(value = "接口响应携带类")
public class ApiGxbResult implements Serializable {

    /**
     * 返回代码
     * 默认成功
     */
    @ApiModelProperty(value = "接口调用状态码", access = "0", example = "0")
    private String code = ApiStatus.SUCCESS.getCode();
    /**
     * 提示信息
     * 默认提示语
     */
    @ApiModelProperty(value = "接口调用情况描述", access = "操作成功", example = "操作成功")
    private String message = ApiStatus.SUCCESS.getValue();
    private String result;

    public ApiGxbResult() {
    }


    public ApiGxbResult(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ApiGxbResult(String code, String msg, String t) {
        this.code = code;
        this.message = msg;
        this.result = t;
    }


    public ApiGxbResult(ApiGxbServiceResult apiGxbServiceResult) {
        this.code = apiGxbServiceResult.getCode();
        this.message = apiGxbServiceResult.getMessage();
        this.result =  apiGxbServiceResult.getResult();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static void main(String[] args){
        String appId="gxbbfe847e08ffacbb0";
        String appSecurity="21938cd1eac24cd9a8d4bd3795765b6b";
        String authItem ="zhimaScore";
        Long time = System.currentTimeMillis();
        String timestamp = time.toString();
        System.out.println(timestamp);
        String sequenceNo="5c2500de12c8479cb05686c84c5fb2cf";
        String sign = DigestUtils.md5Hex(String.format("%s%s%s%s%s", appId, appSecurity, authItem, timestamp, sequenceNo));
        System.out.println(sign);
    }
}
