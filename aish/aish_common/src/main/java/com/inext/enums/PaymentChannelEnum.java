
package com.inext.enums;

/**
 * 放款通道枚举类 serviceName和支付通道getClass().getSimpleName()名称挂钩
 *
 * @author lisige
 */
public enum PaymentChannelEnum {
    /**
     * 合利宝
     */
    HELIPAY(1, "heLiServiceImpl", "合利宝"),

    /**
     * 合利宝D1
     */
    HELIPAYD1(2, "heLiD1ServiceImpl", "合利宝D1"),

    /**
     * 口袋
     */
    KOUDAI(3, "kouDaiServiceImpl", "口袋"),
    /**
     * 汇潮
     */
    HUICHAOPAY(4, "hcServiceImpl", "汇潮"),
    /**
     * 宝付
     */
    BAOFOO(5, "baoFooServiceImpl", "宝付");

    /**
     * 请注意code值  该值直接和数据库表 payment_channel表id挂钩
     */
    private Integer code;

    private String serviceName;

    private String description;

    PaymentChannelEnum(final Integer code, final String serviceName, final String description) {
        this.code = code;
        this.serviceName = serviceName;
        this.description = description;
    }

    /**
     * 根据对应的code获取serviceName
     *
     * @param code
     * @return
     */
    public static String getServiceName(final Integer code) {
        for (final PaymentChannelEnum e : PaymentChannelEnum.values()) {
            if (e.getCode().intValue() == code.intValue()) {
                return e.getServiceName();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }


    public String getServiceName() {
        return serviceName;
    }


    public String getDescription() {
        return description;
    }
}
