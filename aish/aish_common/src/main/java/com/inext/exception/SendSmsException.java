package com.inext.exception;

public class SendSmsException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;
    private String code;

    public SendSmsException(String message) {
        this.setMessage(message);
    }

    public SendSmsException(String code, String message) {
        this.setMessage(message);
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
