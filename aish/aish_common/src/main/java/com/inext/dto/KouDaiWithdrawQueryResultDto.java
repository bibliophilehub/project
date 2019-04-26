package com.inext.dto;

import lombok.Data;

import java.util.Map;

/**
 * 口袋理财放款查询返回值包装类
 *
 * @author lisige
 */
@Data
public class KouDaiWithdrawQueryResultDto {

    /**
     * 打款状态，0：打款成功；1002：处理中；1003：打款失败；1006：订单不存在；500：系统异常
     * 200011：手续费大于提现金额；200012：单日次数超限，
     * 500：系统异常
     */
    private Integer code;

    /**
     * 申请结果描述
     */
    private String msg;

    private Map<String, Object> data;

    /**
     * 本次操作是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return this.code.intValue() == 0;
    }
}
