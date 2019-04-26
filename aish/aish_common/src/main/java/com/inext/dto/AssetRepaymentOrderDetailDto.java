
package com.inext.dto;

import com.inext.entity.AssetRepaymentOrderDetail;

import java.math.BigDecimal;

public class AssetRepaymentOrderDetailDto extends AssetRepaymentOrderDetail
{
    private BigDecimal currentAmount; //当前用户还款金额
    private String outOrderNo; //商户订单号
    private String operator; //后台操作人
    private Integer repaymentType; //还款方式

    public BigDecimal getCurrentAmount()
    {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount)
    {
        this.currentAmount = currentAmount;
    }

    public String getOutOrderNo()
    {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo)
    {
        this.outOrderNo = outOrderNo;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public Integer getRepaymentType()
    {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType)
    {
        this.repaymentType = repaymentType;
    }


}