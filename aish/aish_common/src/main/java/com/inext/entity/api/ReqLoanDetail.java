package com.inext.entity.api;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class ReqLoanDetail implements Serializable {
    /**
     * @Description: TODO
     */
    private static final long serialVersionUID = 1L;

    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}
