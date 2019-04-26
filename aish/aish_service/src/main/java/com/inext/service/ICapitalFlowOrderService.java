package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.AbnormalOrder;
import com.inext.entity.AssetRepaymentDetail;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.HcDfInfo;
import com.inext.result.AjaxResult;

import java.util.List;
import java.util.Map;

/**
 * 开发人员：wenkk
 * 创建时间：2017-07-26 09:45
 */
public interface ICapitalFlowOrderService extends BaseSerivce<AssetRepaymentOrder> {


    /**
     * 还款资金列表查询
     *
     * @param params
     * @return
     */
    PageInfo<AssetRepaymentOrder> getFlowPageList(Map<String, Object> params);

    public List<Map<String, String>> getCapitalFlowList(Map<String, Object> params);

    PageInfo<AbnormalOrder> getAbnormalOrder(Map<String, Object> params);

    public List<Map<String, String>> getList(Map<String, Object> params);

    public void saveAssetRepaymentDetail(AssetRepaymentDetail assetRepaymentDetail);

    /**
     * 保存退款信息
     */
    AjaxResult saveAbnormal(AbnormalOrder abnormalOrder);


    long getARDetailByRepaymentOrderNo(String repaymentOrderNo);

    /**
     * 查询代付提现记录-分页
     * @param params
     * @return
     */
    PageInfo<HcDfInfo> getHcDfInfoPageList(Map<String, Object> params);

    /**
     * 查询代付提现记录
     * @param params
     * @return
     */
    List<HcDfInfo> getHcDfInfoList(Map<String, Object> params);


}
