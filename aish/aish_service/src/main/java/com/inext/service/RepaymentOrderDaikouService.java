
package com.inext.service;

import com.inext.dto.AssetRepaymentOrderDaikouDto;
import com.inext.entity.AssetRepaymentOrderDaikou;

import java.util.List;

public interface RepaymentOrderDaikouService extends BaseSerivce<AssetRepaymentOrderDaikou>
{
    /**
     *
     * TODO 代扣结果需查询列表
     * @author wxy
     * @date 2018年6月26日
     * @return
     */
    List<AssetRepaymentOrderDaikou> getQueryDKList(AssetRepaymentOrderDaikouDto dto);

    /**
     *
     * TODO 根据还款订单id，查询用户已经代扣总数
     * @author wxy
     * @date 2018年6月29日
     * @param dto
     * @return
     */
    int countTotalRepaymentDKNum(AssetRepaymentOrderDaikouDto dto);

    /**
     *
     * TODO 根据还款订单id，当天时间段，查询用户已经代扣总数
     * @author wxy
     * @date 2018年6月29日
     * @param dto
     * @return
     */
    int countDayRepaymentDKNum(AssetRepaymentOrderDaikouDto dto);

    /**
     *
     * TODO 统计当前需要查询代扣状态总数
     * @author wxy
     * @date 2018年7月2日
     * @param dto
     * @return
     */
    int calculationDKQueryNum(AssetRepaymentOrderDaikou dto);
}
