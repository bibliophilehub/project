
package com.inext.service.dk;

public interface RepaymentDKService
{
    /**
     * 
     * TODO 代扣方法
     * @author wxy
     * @date 2018年7月12日
     */
    void doRepaymenytDK();
    
    /**
     * 代扣业务
     * @param channel 支付通道名
     */
    void doSubstitute(String channel);
}
