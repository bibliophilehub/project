
package com.inext.service.handler.invocation;

import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.AssetRepaymentOrderDaikou;

import java.io.Serializable;

/**
 * 代扣消息javabean
 * @编码实现人员 lihong
 * @需求提出人员 TODO 填写需求填写人员
 * @实现日期 2018年6月25日
 */
public class DKParam implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String topic;//执行策略标识

    private AssetRepaymentOrder repaymentOrder;//代扣还款订单，处理参数内容
    
    private AssetRepaymentOrderDaikou repayMentDaiKou;//代扣查询数据处理参数

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public AssetRepaymentOrder getRepaymentOrder()
    {
        return repaymentOrder;
    }

    public void setRepaymentOrder(AssetRepaymentOrder repaymentOrder)
    {
        this.repaymentOrder = repaymentOrder;
    }

    public AssetRepaymentOrderDaikou getRepayMentDaiKou()
    {
        return repayMentDaiKou;
    }

    public void setRepayMentDaiKou(AssetRepaymentOrderDaikou repayMentDaiKou)
    {
        this.repayMentDaiKou = repayMentDaiKou;
    }
}
