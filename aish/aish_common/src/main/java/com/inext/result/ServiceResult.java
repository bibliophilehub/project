package com.inext.result;

import com.inext.enumerate.Status;

public class ServiceResult {


    public static String SUCCESS = "200";
    /**
     * 返回代码
     */
    private String code = Status.SUCCESS.getName();// 默认成功

    /*
     * 提示信息
     */
    private String msg = Status.SUCCESS.getValue(); // 默认提示语

    private Object ext = null;


    private Object data; // 返回数据


    public ServiceResult() {
    }

    public ServiceResult(String msg) {
        this.msg = msg;
    }

    public ServiceResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ServiceResult(String code, String msg, Object data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccessed() {
        return SUCCESS.equals(getCode());
    }

    public boolean isFail() {
        return !SUCCESS.equals(getCode());
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

}
