package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.PaymentChannel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lisige
 */
@Repository
public interface IPaymentChannelDao extends BaseDao<PaymentChannel> {

    /**
     * 筛选出可用的放款通道并排序
     *
     * @param paymentChannel
     * @return
     */
    @Select("select * from payment_channel where enable = #{paymentChannel.enable} order by sort")
    List<PaymentChannel> selectListOrderBySort(@Param("paymentChannel") PaymentChannel paymentChannel);
}
