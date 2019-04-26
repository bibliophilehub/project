
package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.dto.AssetRepaymentOrderDaikouDto;
import com.inext.entity.AssetRepaymentOrderDaikou;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 功能描述: 代扣记录表
 * @编码实现人员 wxy
 * @实现日期 2018年6月22日
 *      --------------------------------------------------
 */
@Repository
public interface AssetRepaymentOrderDaikouDao extends BaseDao<AssetRepaymentOrderDaikou>
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