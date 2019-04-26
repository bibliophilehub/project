package com.inext.exception;

/**
 * 基础业务异常处理
 */
public class BusinessException extends RuntimeException{
    private String code;
    private String msg;
    public BusinessException(String errorCode, String message) {
        this(errorCode, message, new Throwable());
    }

    public BusinessException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.msg = message;
        this.code = code;
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
}
