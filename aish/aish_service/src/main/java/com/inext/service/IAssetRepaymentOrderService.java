package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.dto.AssetRepaymentOrderByWithholdDto;
import com.inext.dto.AssetRepaymentOrderDto;
import com.inext.entity.AssetRepaymentDetail;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.AssetRepaymentOrderWithhold;
import com.inext.entity.HcRepaymentInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：wenkk
 * 创建时间：2017-07-26 09:45
 */
public interface IAssetRepaymentOrderService extends BaseSerivce<AssetRepaymentOrder> {

    PageInfo<AssetRepaymentOrder> getPageList(Map<String, Object> params);

    List<Map<String, String>> getList(Map<String, Object> params);

    /**
     * 通过用户Id查询未还款订单
     */
    AssetRepaymentOrder getByUserId(Integer userId);

    AssetRepaymentOrder getRepaymentByOrderId(Integer orderId);

    int updateRepaymentByOrderId(AssetRepaymentOrder assetRepaymentOrder);

    /**
     * 管理员还款
     */
    void trueRepaymentMoney(AssetRepaymentDetail assetRepaymentDetail);

    int update(AssetRepaymentOrder assetRepaymentOrder);

    int doSave(AssetRepaymentDetail assetRepaymentDetail);

    int insertRepaymentOrder(AssetRepaymentOrder assetRepaymentOrder);

    void trueRepaymentMoney(AssetRepaymentOrder aroModel, AssetRepaymentDetail paramsDto);

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
     * @param dto
     * @return
     * @author wxy
     * @date 2018年7月10日
     */
    List<AssetRepaymentOrder> getDKList(AssetRepaymentOrderDto dto);

    /**
     * TODO 查询当天代扣总数
     *
     * @param dto
     * @return
     * @author wxy
     * @date 2018年7月10日
     */
    int calculationDKOrderNum(AssetRepaymentOrderDto dto);

    //往催收推送逾期订单
    void pushCuiShouTask();

    //查询逾期的订单已还款推送到催收系统
    void findRaymentedHuanKuan();

    //推送明天逾期数据
    void findCurrentOverdue();

    //每天定时推送未逾期已还款
    void pushCuishouNotOverdue();

    //调用删除接口
    String delUnderLineRe(Integer orderId, String userPhone);

    /**
     * 扣款信息查询
     *
     * @param params
     * @return
     */
    List<AssetRepaymentOrderByWithholdDto> getListByWithhold(Map<String, Object> params);

    /**
     * 扣款批次添加
     *
     * @param dto
     * @return
     */
    int insertOrderWithhold(AssetRepaymentOrderWithhold dto);

    /**
     * 更新批次号
     *
     * @return
     */
    int updateRepaymentByIdWithhold(Integer withholdId, Integer dkPayStatus, Integer repaymentPayStatus, String errMessage, List<Integer> list);

    int updateWithhold(String batchId, Integer chargeStatus, Date updateTime, String errMessage, Integer id);

    AssetRepaymentOrderWithhold getWithholdByOther(String requestId, Integer id);

    List<AssetRepaymentOrderWithhold> getWithholdByStatus(Integer chargeStatus);

    List<AssetRepaymentOrder> getListByRapaymentOrder(Map<String, Object> params);


    /**
     * 已还款页面查询
     *
     * @param params
     * @return
     */
    PageInfo<AssetRepaymentOrder> getListForRepaymented(Map<String, Object> params);

    /**
     * 查询汇潮支付订单
     *
     * @param orderId
     * @return
     */
    HcRepaymentInfo getOneByHcRepaymentOrder(int orderId);

    int updateCeditAndRepaymented(int orderId, Integer amount);


    long getOrderByRepaymentOrderNo(String repaymentOrderNo);
}
