package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.dto.AssetRepaymentOrderByWithholdDto;
import com.inext.dto.AssetRepaymentOrderDto;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.HcRepaymentInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 开发人员：wenkk
 * 创建时间：2018-03-21 09:26
 */
public interface IAssetRepaymentOrderDao extends BaseDao<AssetRepaymentOrder> {

    public List<AssetRepaymentOrder> getPageList(Map<String, Object> params);

    public List<Map<String, String>> getList(Map<String, Object> params);

    public AssetRepaymentOrder getByOrderId(Integer orderId);

    public AssetRepaymentOrder getByUserId(Integer userId);

    public int updateRepaymentByOrderId(AssetRepaymentOrder assetRepaymentOrder);

    public AssetRepaymentOrder getRepaymentByOrderId(Integer orderId);

    public List<AssetRepaymentOrder> findTaskRepayment(Map<String, Object> params);

    public List<Map<String, Object>> getAssetBorrowOrder(Map<String, Object> param);

    @Select("SELECT rcu.id AS serialId, rcu.risk_id riskId, abo.user_name name, abo.user_phone phoneNum, abo.user_id_number idCard,abo.order_end, DATE_FORMAT(abo.loan_time, '%Y-%m-%d %H:%i:%s') AS loanTime, TIMESTAMPDIFF( DAY, abo.loan_time, abo.loan_end_time ) AS rqc, IFNULL(aro.repayment_amount,abo.money_amount) AS amount,IFNULL(aro.repaymented_amount,0) AS amounted, DATE_FORMAT( abo.loan_end_time, '%Y-%m-%d %H:%i:%s' ) AS repaymentTime, abo. status,DATE_FORMAT(aro.repayment_real_time, '%Y-%m-%d %H:%i:%s') realRepaymentTime, IFNULL(aro.late_day, abo.late_day) AS lateDay FROM asset_borrow_order abo LEFT JOIN risk_credit_user rcu ON rcu.asset_id = abo.id LEFT JOIN asset_repayment_order aro ON aro.order_id = abo.id WHERE DATE_FORMAT(aro.update_time, '%Y-%m-%d') = #{0} and   abo.`status` NOT IN (1, 2, 4, 5)")
    List<Map<String, Object>> getfkDhCallbackData(String dateStr);

    public void updateRepaymentById(AssetRepaymentOrder assetRepaymentOrder);

    public AssetRepaymentOrder getRepaymentById(Integer id);

    /**
     * TODO 获取易宝需代扣的数据
     *
     * @param assetRepaymentOrderDto
     * @return
     * @author wxy
     * @date 2018年6月26日
     */
    List<AssetRepaymentOrder> getYOPDKList(AssetRepaymentOrderDto assetRepaymentOrderDto);

    /**
     * TODO 获取需代扣的数据
     *
     * @param assetRepaymentOrderDto
     * @return
     * @author wxy
     * @date 2018年7月10日
     */
    List<AssetRepaymentOrder> getDKList(AssetRepaymentOrderDto assetRepaymentOrderDto);

    /**
     * TODO 查询当天代扣总数
     *
     * @param assetRepaymentOrderDto
     * @return
     * @author wxy
     * @date 2018年7月10日
     */
    int calculationDKOrderNum(AssetRepaymentOrderDto assetRepaymentOrderDto);

    /**
     * 获取提额有效的还款记录数
     *
     * @param param
     * @return
     */
    int getEffectCount(Map<String, Object> param);

    //催收
    List<AssetRepaymentOrder> yuyiYihuankuanOrder(Map<String, Object> map);

    List<AssetRepaymentOrder> findRaymentedHuanKuan(Map<String, Object> map);

    List<AssetRepaymentOrder> currentTimeOrder(Map<String, Object> params);

    List<AssetRepaymentOrder> pushCuishouNotOverdue(Map<String, Object> params);

    List<AssetRepaymentOrder> selectCountByHand(Map<String, Object> map);


    /**
     * 查询到期订单信息
     *
     * @param map
     * @return
     */
    List<AssetRepaymentOrderByWithholdDto> getListByWithhold(Map<String, Object> map);

    /**
     * 更新批次号
     *
     * @return
     */
    int updateRepaymentByIdWithhold(@Param("withholdId") Integer withholdId,
                                    @Param("dkPayStatus") Integer dkPayStatus,
                                    @Param("repaymentPayStatus") Integer repaymentPayStatus,
                                    @Param("errMessage") String errMessage,
                                    @Param("list") List<Integer> list);

    public List<AssetRepaymentOrder> getListByRepaymentOrder(Map<String, Object> params);

    /**
     * 已还款查询
     *
     * @param params
     * @return
     */
    List<AssetRepaymentOrder> getListForRepaymented(Map<String, Object> params);

    /**
     * 查询汇潮支付订单
     *
     * @return
     */
    @Select("SELECT * FROM hc_repayment_info WHERE asset_id = #{orderId} ORDER BY id DESC LIMIT 1")
    HcRepaymentInfo getOneByHcRepaymentOrder(@Param("orderId") int orderId);


    public List<AssetRepaymentOrder> getAbnormalPageList(Map<String, Object> params);

    public List<Map<String, String>> getAbnormalList(Map<String, Object> params);

    public List<AssetRepaymentOrder> getFlowPageList(Map<String, Object> params);

    public List<Map<String, String>> getCapitalFlowList(Map<String, Object> params);

    public int updateCeditAndRepaymented(@Param("orderId") int orderId, @Param("amount") Integer amount);

    int getRepaymentExpiredCount(int userId);

    /**
     * 查询符合-入-白名单-的用户-总数
     * @return
     */
    @Select("SELECT IFNULL(COUNT(DISTINCT user_phone,user_name), 0) FROM asset_repayment_order WHERE order_status=8 AND (repayment_real_time <= repayment_time) AND user_id NOT IN(SELECT DISTINCT user_id FROM asset_repayment_order WHERE (order_status = 7 AND late_day > 2) OR (order_status = 8 AND DATEDIFF(repayment_real_time,repayment_time) > 20)) AND user_id NOT IN(SELECT DISTINCT user_id FROM asset_repayment_order WHERE (order_status = 8 AND repayment_real_time > repayment_time AND DATEDIFF(repayment_real_time,repayment_time) <= 20))")
    int getInWhiteListUserCount();

    /**
     *  查询符合-入-白名单-的用户-数据
     * @return
     */
    @Select("SELECT DISTINCT user_phone AS userPhone, user_name AS userName FROM asset_repayment_order WHERE order_status=8 AND (repayment_real_time <= repayment_time) AND user_id NOT IN(SELECT DISTINCT user_id FROM asset_repayment_order WHERE (order_status = 7 AND late_day > 2) OR (order_status = 8 AND DATEDIFF(repayment_real_time,repayment_time) > 20)) AND user_id NOT IN(SELECT DISTINCT user_id FROM asset_repayment_order WHERE (order_status = 8 AND repayment_real_time > repayment_time AND DATEDIFF(repayment_real_time,repayment_time) <= 20)) LIMIT #{0}, #{1}")
    List<Map<String, String>> getInWhiteListUser(int start, int pageSize);

    /**
     *  查询符合-入-黑名单-的用户-总数
     * @return
     */
    @Select("SELECT IFNULL(COUNT(DISTINCT user_phone,user_name), 0) FROM asset_repayment_order WHERE (order_status = 7 AND late_day > 2) OR (order_status = 8 AND DATEDIFF(repayment_real_time,repayment_time) > 20)")
    int getInBlackListUserCount();

    /**
     *  查询符合-入-黑名单-的用户-数据
     * @return
     */
    @Select("SELECT DISTINCT user_phone AS userPhone, user_name AS userName FROM asset_repayment_order WHERE (order_status = 7 AND late_day > 2) OR (order_status = 8 AND DATEDIFF(repayment_real_time,repayment_time) > 20) LIMIT #{0}, #{1}")
    List<Map<String, String>> getInBlackListUser(int start, int pageSize);

    /**
     *  查询符合-出-黑名单-的用户-总数
     * @return
     */
    @Select("SELECT IFNULL(COUNT(DISTINCT user_phone,user_name), 0) FROM asset_repayment_order WHERE (order_status = 8 AND repayment_real_time > repayment_time AND DATEDIFF(repayment_real_time,repayment_time) <= 20) AND user_id NOT IN(SELECT DISTINCT user_id FROM asset_repayment_order WHERE (order_status = 7 AND late_day > 2) OR (order_status = 8 AND DATEDIFF(repayment_real_time,repayment_time) > 20))")
    int getOutBlackListUserCount();

    /**
     *  查询符合-出-黑名单-的用户-数据
     * @return
     */
    @Select("SELECT DISTINCT user_phone AS userPhone, user_name AS userName FROM asset_repayment_order WHERE (order_status = 8 AND repayment_real_time > repayment_time AND DATEDIFF(repayment_real_time,repayment_time) <= 20) AND user_id NOT IN(SELECT DISTINCT user_id FROM asset_repayment_order WHERE (order_status = 7 AND late_day > 2) OR (order_status = 8 AND DATEDIFF(repayment_real_time,repayment_time) > 20)) LIMIT #{0}, #{1}")
    List<Map<String, String>> getOutBlackListUser(int start, int pageSize);

}
