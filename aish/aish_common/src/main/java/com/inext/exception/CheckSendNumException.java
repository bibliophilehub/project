package com.inext.exception;

public class CheckSendNumException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;
    private String code;

    public CheckSendNumException(String message) {
        this.setMessage(message);
    }

    public CheckSendNumException(String code, String message) {
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
