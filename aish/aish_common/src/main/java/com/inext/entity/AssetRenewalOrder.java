package com.inext.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 续期订单
 */
@Data
public class AssetRenewalOrder implements Serializable {

    private static final long serialVersionUID = 8098342754628169918L;
    @Id
    private Integer id;
    private Integer userId; //用户id
    private String userPhone;//用户手机号码
    private String userName;//用户姓名
    private Integer orderId;//借款订单id
    private BigDecimal moneyAmount; //借款金额，单位为分
    private BigDecimal penaltyAmount;//违约金，单位为分
    private Integer moneyDay;//借款期限/天
    private Date creditRepaymentTime;//放款时间
    private Date renewalTime;//续期时间
    private Integer renewalDay;//续期期限/天
    private Date repaymentTime;//续期后应还款时间
    @Transient
    private String channelName;
    private Integer renewalType;
    private String renealComment;
    @Transient
    private String orderNo;//扣款单号
    @Transient
    private String repaymentChannel;//扣款渠道
    @Transient
    private Date paymentTime;//扣款时间
    @Transient
    private String renewalOrderNo;//续期支付单号
    @Transient
    private Integer paymentChannel;
    @Transient
    private String renewalBank;

}
