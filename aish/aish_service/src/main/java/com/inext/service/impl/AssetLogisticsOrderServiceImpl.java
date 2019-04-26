package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IAssetBorrowOrderDao;
import com.inext.dao.IAssetLogisticsOrderDao;
import com.inext.dao.IAssetOrderStatusHisDao;
import com.inext.dao.IAssetRepaymentOrderDao;
import com.inext.entity.*;
import com.inext.service.IAssetLogisticsOrderService;
import com.inext.service.IBackConfigParamsService;
import com.inext.utils.RequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单物流信息
 * 开发人员：wenkk 创建时间：2018-03-21 18:47
 */

@Service
@Transactional
public class AssetLogisticsOrderServiceImpl implements IAssetLogisticsOrderService {

    @Resource
    IAssetLogisticsOrderDao iAssetLogisticsOrderDao;
    @Resource
    IAssetBorrowOrderDao iAssetBorrowOrderDao;
    @Resource
    IAssetRepaymentOrderDao iAssetRepaymentOrderDao;
    @Resource
    IAssetOrderStatusHisDao iAssetOrderStatusHisDao;


    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @Override
    public PageInfo<AssetLogisticsOrder> getPageList(Map<String, Object> params) {
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
        PageInfo<AssetLogisticsOrder> pageInfo = new PageInfo<AssetLogisticsOrder>(this.iAssetLogisticsOrderDao.getPageList(params));
        return pageInfo;
    }

    @Override
    public List<Map<String, String>> getList(Map<String, Object> params) {
        return this.iAssetLogisticsOrderDao.getList(params);
    }

    @Override
    public int audit(Integer id, Integer auditStatus) {
        BackUser operbackUser = (BackUser) RequestUtils.getRequest().getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        AssetLogisticsOrder assetLogisticsOrder = iAssetLogisticsOrderDao.selectByPrimaryKey(id);
        assetLogisticsOrder.setAuditStatus(auditStatus);
        assetLogisticsOrder.setUpdateAccount(operbackUser.getAccount());
        assetLogisticsOrder.setUpdateTime(new Date());
        iAssetLogisticsOrderDao.updateByPrimaryKey(assetLogisticsOrder);
        if (auditStatus.intValue() == 1) {
            //1借款单关闭订单
            AssetBorrowOrder assetBorrowOrder = new AssetBorrowOrder();
            assetBorrowOrder.setId(assetLogisticsOrder.getOrderId());
            assetBorrowOrder.setOrderEnd(1);
            assetBorrowOrder.setStatus(AssetOrderStatusHis.STATUS_JCHG);
            assetBorrowOrder.setUpdateTime(assetLogisticsOrder.getUpdateTime());
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrder);
            //2还款单完成订单
            AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderDao.getByOrderId(assetLogisticsOrder.getOrderId());
            assetRepaymentOrder.setOrderStatus(8);
            assetRepaymentOrder.setRepaymentRealTime(assetLogisticsOrder.getUpdateTime());
            // 检测合格
            assetRepaymentOrder.setRepaymentType(AssetRepaymentOrder.REPAYMENT_TYPE_MAIL);
            iAssetRepaymentOrderDao.updateByPrimaryKey(assetRepaymentOrder);
            //记录状态变更
            AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
            assetOrderStatusHis.setOrderId(assetLogisticsOrder.getOrderId());
            assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_JCHG);
            assetOrderStatusHis.setCreateTime(assetLogisticsOrder.getUpdateTime());
            assetOrderStatusHis.setRemark("平台已收货，检测合格");
            iAssetOrderStatusHisDao.insert(assetOrderStatusHis);


        } else {
            AssetBorrowOrder assetBorrowOrder = new AssetBorrowOrder();
            assetBorrowOrder.setId(assetLogisticsOrder.getOrderId());
            assetBorrowOrder.setStatus(AssetOrderStatusHis.STATUS_YYQ);
            assetBorrowOrder.setUpdateTime(assetLogisticsOrder.getUpdateTime());
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrder);

            //记录状态变更
            AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
            assetOrderStatusHis.setOrderId(assetLogisticsOrder.getOrderId());
            assetOrderStatusHis.setOrderStatus(12);
            assetOrderStatusHis.setCreateTime(assetLogisticsOrder.getUpdateTime());
            //Map<String, String> loan = backConfigParamsService.getBackConfig(BackConfigParams.LOAN, null);
            //String penaltyAmount = loan.get("LOAN_WY");//违约金
            AssetBorrowOrder assetBorrowOrderOld = iAssetBorrowOrderDao.getOrderById(assetLogisticsOrder.getOrderId());
            String penaltyAmount = assetBorrowOrderOld.getPenaltyAmount().toString();//违约金
            assetOrderStatusHis.setRemark("平台已收货，检测不合格,已违约，违约金" + penaltyAmount + "元");
            iAssetOrderStatusHisDao.insert(assetOrderStatusHis);
        }
        return 1;
    }

    @Override
    public int saveAssetLogisticsOrder(AssetLogisticsOrder assetLogisticsOrder) {

        return iAssetLogisticsOrderDao.insertSelective(assetLogisticsOrder);
    }


}
