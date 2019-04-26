package com.inext.entity;

import com.google.common.collect.Maps;
import com.inext.enums.PaymentChannelEnum;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author lisige
 */
@Data
public class PaymentChannel {

    /**
     * 支付通道的描述
     */
    public final static Map<Integer, String> DESCRIPTION = Maps.newHashMap();

    static {
        DESCRIPTION.put(PaymentChannelEnum.HELIPAY.getCode(), PaymentChannelEnum.HELIPAY.getDescription());
        DESCRIPTION.put(PaymentChannelEnum.HELIPAYD1.getCode(), PaymentChannelEnum.HELIPAYD1.getDescription());
        DESCRIPTION.put(PaymentChannelEnum.BAOFOO.getCode(), PaymentChannelEnum.BAOFOO.getDescription());
    }

    @Id
    private Integer id;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 放款通道的bean名称
     */
    private String serviceName;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Integer enable;

    /**
     * 金额限制
     */
    private BigDecimal moneyLimit;
}
