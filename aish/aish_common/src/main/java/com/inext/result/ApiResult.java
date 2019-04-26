package com.inext.result;


import com.inext.enumerate.ApiStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author lisige
 */
@ApiModel(value = "接口响应携带类")
public class ApiResult<T extends BaseResult> implements Serializable {

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
    private T result;

    public ApiResult() {
    }

    public ApiResult(T t) {
        this.result = t;
    }

    public ApiResult(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ApiResult(String code, String msg, T t) {
        this.code = code;
        this.message = msg;
        this.result = t;
    }


    public ApiResult(ApiServiceResult apiServiceResult) {
        this.code = apiServiceResult.getCode();
        this.message = apiServiceResult.getMsg();
        this.result = (T) apiServiceResult.getExt();
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

    public T getResult() {
        return result;
    }

    public ApiResult<T> setResult(T result) {
        this.result = result;
        return this;
    }
}
