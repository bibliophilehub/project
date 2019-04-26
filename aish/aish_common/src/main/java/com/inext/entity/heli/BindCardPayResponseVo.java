package com.inext.entity.heli;

import lombok.Data;

/**
 * Created by heli50 on 2017/4/15.
 */
@Data
public class BindCardPayResponseVo {
    private String rt1_bizType;
    private String rt2_retCode;
    private String rt3_retMsg;
    private String rt4_customerNumber;
    private String rt5_orderId;
    private String sign;
}
