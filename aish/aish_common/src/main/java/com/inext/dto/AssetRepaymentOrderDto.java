
package com.inext.dto;

import com.inext.entity.AssetRepaymentOrder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款订单
 */
public class AssetRepaymentOrderDto extends AssetRepaymentOrder
{

    private static final long serialVersionUID = -8429380877986118536L;

    private String channelName;

    private String comment;

    private BigDecimal shouldRepayAmount; //剩余应还金额

    private Integer[] statuses;

    private Date withholdingTime;//代扣时间

    private Integer pageNum;//当前页

    private Integer pageSize;//当前页大小

    public Integer[] getStatuses()
    {
        return statuses;
    }

    public void setStatuses(Integer[] statuses)
    {
        this.statuses = statuses;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public BigDecimal getShouldRepayAmount()
    {
        return shouldRepayAmount;
    }

    public void setShouldRepayAmount(BigDecimal shouldRepayAmount)
    {
        this.shouldRepayAmount = shouldRepayAmount;
    }

    public Date getWithholdingTime()
    {
        return withholdingTime;
    }

    public void setWithholdingTime(Date withholdingTime)
    {
        this.withholdingTime = withholdingTime;
    }

    public Integer getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
}
