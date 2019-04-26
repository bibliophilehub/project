package com.inext.utils.xiaobailaihua.dto;

public class ResultDto {
    private Integer code;
    private String msg;
    private Object data;
    private String reason;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static ResultDto success(Object data){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMsg("success");
        resultDto.setData(data);
        return resultDto;
    }
    public static ResultDto success(){
        return success(null);
    }
}
