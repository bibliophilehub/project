
package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.dto.AssetRepaymentOrderWithholdDto;
import com.inext.entity.AssetRepaymentOrderWithhold;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 功能描述: 代扣记录表
 * @编码实现人员 wxy
 * @实现日期 2018年6月22日
 *      --------------------------------------------------
 */
@Repository
public interface IAssetRepaymentOrderWithholdDao extends BaseDao<AssetRepaymentOrderWithhold>
{
    /**
     * 
     * TODO 代扣结果需查询列表
     * @author wxy
     * @date 2018年6月26日
     * @return
     */
    List<AssetRepaymentOrderWithhold> getQueryDKList(AssetRepaymentOrderWithholdDto dto);

    /**
     * 
     * TODO 根据还款订单id，查询用户已经代扣总数
     * @author wxy
     * @date 2018年6月29日
     * @param dto
     * @return
     */
    int countTotalRepaymentDKNum(AssetRepaymentOrderWithholdDto dto);

    /**
     * 
     * TODO 根据还款订单id，当天时间段，查询用户已经代扣总数
     * @author wxy
     * @date 2018年6月29日
     * @param dto
     * @return
     */
    int countDayRepaymentDKNum(AssetRepaymentOrderWithholdDto dto);

    /**
     * 
     * TODO 统计当前需要查询代扣状态总数
     * @author wxy
     * @date 2018年7月2日
     * @param dto
     * @return
     */
    int calculationDKQueryNum(AssetRepaymentOrderWithhold dto);

    /**
     * 添加扣款信息
     * @param dto
     * @return
     */
    int insertOrderWithhold(AssetRepaymentOrderWithhold dto);

    int updateWithhold(@Param("batchId") String batchId,@Param("chargeStatus") Integer chargeStatus,
                       @Param("updateTime") Date updateTime,@Param("errMessage") String errMessage,
                       @Param("id") Integer id);

    AssetRepaymentOrderWithhold selectWithholdByOther(@Param("requestId") String requestId,
                                                      @Param("id") Integer id);


    List<AssetRepaymentOrderWithhold> getWithholdByStatus(@Param("chargeStatus")Integer chargeStatus);
}