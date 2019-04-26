
package com.inext.dto;

import com.inext.entity.AssetRepaymentOrderDaikou;

import java.util.Date;

public class AssetRepaymentOrderDaikouDto extends AssetRepaymentOrderDaikou
{
    private Date beginCreateTime;

    private Date endCreateTime;

    private Integer pageNum;//当前页

    private Integer pageSize;//当前页大小

    public Date getBeginCreateTime()
    {
        return beginCreateTime;
    }

    public void setBeginCreateTime(Date beginCreateTime)
    {
        this.beginCreateTime = beginCreateTime;
    }

    public Date getEndCreateTime()
    {
        return endCreateTime;
    }

    public void setEndCreateTime(Date endCreateTime)
    {
        this.endCreateTime = endCreateTime;
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
}
