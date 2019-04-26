package com.inext.entity.heli;

import lombok.Data;

/**
 * Created by heli50 on 2017/4/15.
 */
@Data
public class BindCardPayVo {
    private String P1_bizType;//交易类型
    private String P2_customerNumber;//商户编号
    private String P3_userId;//用户id
    private String P4_orderId;//商户订单号
    private String P5_timestamp;//时间戳
    private String P6_payerName;//姓名
    private String P7_idCardType;//证件类型
    private String P8_idCardNo;//证件号码
    private String P9_cardNo;//银行卡号
    private String P10_year;//信用卡有效期年份
    private String P11_month;//信用卡有效期月份
    private String P12_cvv2;//cvv2
    private String P13_phone;//手机号码
    private String P14_currency;//交易币种
    private String P15_orderAmount;//交易金额
    private String P16_goodsName;//商品名称
    private String P17_goodsDesc;//商品描述
    private String P18_terminalType;//终端类型
    private String P19_terminalId;//终端标识
    private String P20_orderIp;//下单ip
    private String P21_period;//订单有效时间
    private String P22_periodUnit;//有效时间单位
    private String P23_serverCallbackUrl;//服务器通知回调地址
    private String P24_isEncrypt;//银行卡信息参数是否加密
    private String sign;//签名
}
