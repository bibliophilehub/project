package com.inext.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChannelStatistics {

    @Id
    private Integer id;
    private Integer channelId;
    private Date statisticsDate;
    private String channelName;
    private Integer pvCount;
    private Integer uvCount;
    private BigDecimal uvRate;
    private Integer registerCount;
    private BigDecimal registerRate;
    private Integer verifiedCount;
    private BigDecimal verifiedRate;
    private Integer borrowCount;
    private BigDecimal borrowRate;
    private Integer auditSuccessCount;
    private BigDecimal auditSuccessRate;
    private Integer loanSuccessCount;
    private BigDecimal loanSuccessRate;
    private Integer renewalCount;
    private BigDecimal renewalRate;
    private Integer repayCount;
    private Integer overdueRepayCount;
    private BigDecimal overdueRepayRate;
    private BigDecimal repayAmount;
    private BigDecimal overdueRepayAmount;
    private BigDecimal overdueRepayAmountRate;

    private Integer realCount;
    private Integer phoneCount;
    private Integer operatorCount;
    private Integer cardCount;
    private Integer zhimaCount;
    private BigDecimal loanAmount;
    private BigDecimal overdueRate;

    private Date updateTime;

}
