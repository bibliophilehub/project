package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.HcDfInfoDao;
import com.inext.dao.IAbnormalOrderDao;
import com.inext.dao.IAssetRepaymentDetailDao;
import com.inext.dao.IAssetRepaymentOrderDao;
import com.inext.entity.AbnormalOrder;
import com.inext.entity.AssetRepaymentDetail;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.HcDfInfo;
import com.inext.enumerate.Status;
import com.inext.result.AjaxResult;
import com.inext.service.ICapitalFlowOrderService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：wenkk 创建时间：2018-03-21 18:47
 */

@Service
@Transactional
public class CapitalFlowOrderServiceImpl extends BaseSerivceImpl<AssetRepaymentOrder> implements ICapitalFlowOrderService {

    @Resource
    IAssetRepaymentOrderDao iAssetRepaymentOrderDao;
    @Resource
    IAbnormalOrderDao ibnormalOrderDao;
    @Resource
    private IAssetRepaymentDetailDao iAssetRepaymentDetailDao;
    @Resource
    private HcDfInfoDao hcDfInfoDao;
    org.slf4j.Logger logger = LoggerFactory.getLogger(CapitalFlowOrderServiceImpl.class);

    @Override
    public PageInfo<AssetRepaymentOrder> getFlowPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage;
            int pageSize = Constants.INITIAL_PAGE_SIZE;

            if (params.get(Constants.CURRENT_PAGE) == null || "".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = Constants.INITIAL_CURRENT_PAGE;
            } else {
                currentPage = Integer.parseInt(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.containsKey(Constants.PAGE_SIZE)) {
                pageSize = Integer.parseInt(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        List<AssetRepaymentOrder> list = this.iAssetRepaymentOrderDao.getFlowPageList(params);
        for (AssetRepaymentOrder item : list) {
            AbnormalOrder abnormalOrder = getAbnormalOrder(item.getNoOrder());
            if (abnormalOrder != null) {
                item.setType(1);
            } else {
                item.setType(0);
            }
        }
        PageInfo<AssetRepaymentOrder> pageInfo = new PageInfo<AssetRepaymentOrder>(list);
        return pageInfo;
    }

    public AbnormalOrder getAbnormalOrder(String noOrder) {
        AbnormalOrder abnormalOrder = ibnormalOrderDao.getAbnormalOrder(noOrder);
        return abnormalOrder;
    }


    @Override
    public List<Map<String, String>> getCapitalFlowList(Map<String, Object> params) {
        List<Map<String, String>> list = iAssetRepaymentOrderDao.getCapitalFlowList(params);
        return list;
    }

    @Override
    public PageInfo<AbnormalOrder> getAbnormalOrder(Map<String, Object> params) {
        if (params != null) {
            int currentPage;
            int pageSize = Constants.INITIAL_PAGE_SIZE;

            if (params.get(Constants.CURRENT_PAGE) == null || "".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = Constants.INITIAL_CURRENT_PAGE;
            } else {
                currentPage = Integer.parseInt(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.containsKey(Constants.PAGE_SIZE)) {
                pageSize = Integer.parseInt(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        List<AbnormalOrder> list = ibnormalOrderDao.getAbnormalOrderList(params);

        PageInfo<AbnormalOrder> pageInfo = new PageInfo<AbnormalOrder>(list);
        return pageInfo;
    }


    @Override
    public List<Map<String, String>> getList(Map<String, Object> params) {
        List<Map<String, String>> list = ibnormalOrderDao.getList(params);
        return list;
    }

    @Override
    public void saveAssetRepaymentDetail(AssetRepaymentDetail assetRepaymentDetail) {

//        //更新还款信息
//        iAssetRepaymentOrderDao.updateRepaymentById(aroModel);

        //保存线下还款信息
        iAssetRepaymentDetailDao.insert(assetRepaymentDetail);
    }


    @Override
    public AjaxResult saveAbnormal(AbnormalOrder abnormalOrder) {
//        int orderId = abnormalOrder.getOrderId();
//        int amount = Integer.parseInt(abnormalOrder.getAmount());
//        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderDao.getByOrderId(orderId);
//        BigDecimal repaymentedAmount = assetRepaymentOrder.getRepaymentedAmount();
//        if (repaymentedAmount.compareTo(new BigDecimal(amount)) == -1) {
//            return new AjaxResult("false", "退款金额不能大于已还款金额");
//        }
//        if (abnormalOrder.getRepaymentChannel() == 1) {
//            iAssetRepaymentOrderDao.updateCeditAndRepaymented(orderId, amount);
//        }
        Long i = ibnormalOrderDao.getOrderByRefundOrderNo(abnormalOrder.getRefundOrderNo());
        if (i > 0) {
            return new AjaxResult("false", "该电子回执单号已存在，请核对后输入");
        }
        ibnormalOrderDao.insert(abnormalOrder);
        return new AjaxResult(Status.SUCCESS.getName(), Status.SUCCESS.getValue());
    }

    @Override
    public long getARDetailByRepaymentOrderNo(String repaymentOrderNo) {
        long count = iAssetRepaymentDetailDao.getARDetailByRepaymentOrderNo(repaymentOrderNo);
        return count;
    }

    /**
     * 查询代付提现记录
     * @param params
     * @return
     */
    @Override
    public PageInfo<HcDfInfo> getHcDfInfoPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage;
            int pageSize = Constants.INITIAL_PAGE_SIZE;

            if (params.get(Constants.CURRENT_PAGE) == null || "".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = Constants.INITIAL_CURRENT_PAGE;
            } else {
                currentPage = Integer.parseInt(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.containsKey(Constants.PAGE_SIZE)) {
                pageSize = Integer.parseInt(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        List<HcDfInfo> list = ibnormalOrderDao.getHcDfInfoList(params);
        PageInfo<HcDfInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<HcDfInfo> getHcDfInfoList(Map<String, Object> params) {
        return hcDfInfoDao.getHcDfInfoList();
    }
}
