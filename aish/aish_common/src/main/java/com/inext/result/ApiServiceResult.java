package com.inext.result;


import com.inext.enumerate.ApiStatus;

/**
 * @param <T>
 * @author lisige
 */
public class ApiServiceResult<T> {

    /**
     * 操作成功
     */
    public final static ApiServiceResult SUCCESS = new ApiServiceResult(ApiStatus.SUCCESS.getCode(), ApiStatus.SUCCESS.getValue());

    /**
     * 无数据
     */
    public final static ApiServiceResult NO_DATA = new ApiServiceResult(ApiStatus.NO_DATA.getCode(), ApiStatus.NO_DATA.getValue());


    /**
     * 返回代码
     */
    private String code = ApiStatus.SUCCESS.getCode();

    /**
     * 提示信息
     */
    private String msg = ApiStatus.SUCCESS.getValue();

    private T ext;

    public ApiServiceResult(T ext) {
        this.ext = ext;
    }

    public ApiServiceResult(String msg) {
        this.msg = msg;
    }

    public ApiServiceResult() {
    }

    public ApiServiceResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiServiceResult(String code, String msg, T ext) {
        this.code = code;
        this.msg = msg;
        this.ext = ext;
    }

    public boolean isSuccessed() {
        return getCode().equals(ApiStatus.SUCCESS.getCode());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getExt() {
        return ext;
    }

    public ApiServiceResult<T> setExt(T ext) {
        this.ext = ext;
        return this;
    }
}
