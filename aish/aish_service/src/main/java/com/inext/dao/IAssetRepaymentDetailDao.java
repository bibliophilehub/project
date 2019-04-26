package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.AssetRepaymentDetail;


/**
 * 第三方还款计划表
 * 开发人员：wenkk
 * 创建时间：2018-03-21 09:26
 */
public interface IAssetRepaymentDetailDao extends BaseDao<AssetRepaymentDetail> {

    long getARDetailByRepaymentOrderNo(String repaymentOrderNo);
}
