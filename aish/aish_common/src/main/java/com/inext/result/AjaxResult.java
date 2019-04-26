package com.inext.result;

import com.inext.enumerate.Status;

public class AjaxResult {


    public static AjaxResult ENTERING_EXCEPTION = new AjaxResult(Status.ERROR.getName(), "信息录入异常");


    // 返回码 默认返回成功
    private String code = Status.SUCCESS.getName();
    ;// 默认成功

    // 返回消息 默认提示消息操作成功
    private String message = Status.SUCCESS.getValue();
    ; // 默认提示语

    private Object data; // 返回数据

    // 默认返回成功
    private boolean success = true;

    public AjaxResult() {
    }

    public AjaxResult(String code, String message) {
        this.setCode(code);
        this.message = message;
    }

    public AjaxResult(String code, String message, Object data) {
        this.setCode(code);
        this.message = message;
        this.data = data;
    }

    public AjaxResult(ServiceResult serviceResult) {
        this.setCode(serviceResult.getCode());
        this.message = serviceResult.getMsg();
        this.data = serviceResult.getData();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        if (Status.SUCCESS.getName().equals(code)) {
            this.success = true;
        } else {
            this.success = false;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {

        if (this.code == Status.SUCCESS.getName())
            return true;
        else
            return false;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
