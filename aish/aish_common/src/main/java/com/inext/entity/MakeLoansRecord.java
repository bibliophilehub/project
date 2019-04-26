package com.inext.entity;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 每日放款资金记录
 *
 * @author lisige
 */
@Data
public class MakeLoansRecord {


    @Id
    private Integer id;

    /**
     * 日期
     */
    private String date;

    /**
     * 支付通道id
     */
    private Integer paymentChannelId;

    /**
     * 放款金额
     */
    private BigDecimal makeLoansMoney;
}
