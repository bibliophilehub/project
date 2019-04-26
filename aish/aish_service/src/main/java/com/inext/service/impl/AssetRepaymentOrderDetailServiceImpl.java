
package com.inext.service.impl;

import com.inext.configuration.BaseDao;
import com.inext.dao.IAssetOrderStatusHisDao;
import com.inext.dao.IAssetRepaymentOrderDetailDao;
import com.inext.dto.AssetRepaymentOrderDetailDto;
import com.inext.entity.*;
import com.inext.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class AssetRepaymentOrderDetailServiceImpl extends BaseSerivceImpl<AssetRepaymentOrderDetail> implements IAssetRepaymentOrderDetailService
{
    @Resource
    IAssetRepaymentOrderDetailDao repaymentOrderDetailDao;

    @Autowired
    IAssetRepaymentOrderService iAssetRepaymentOrderService;

    @Autowired
    IAssetBorrowOrderService iAssetBorrowOrderService;

    @Autowired
    IAssetOrderStatusHisDao iAssetOrderStatusHisDao;
//    @Autowired
//    IComboUserSerivce comboUserSerivce;

    @Autowired
    IBorrowUserService borrowUserService;

    @Autowired
    @Override
    public void setBaseDao(BaseDao<AssetRepaymentOrderDetail> repaymentOrderDetailDao)
    {
        super.setBaseDao(repaymentOrderDetailDao);
    }

    @Override
    public AssetRepaymentOrderDetail selectDetailsByRepaymentOrderId(Integer repaymentOrderId)
    {
        return repaymentOrderDetailDao.selectDetailsByRepaymentOrderId(repaymentOrderId);
    }

    @Override
    public void insertAssetRepaymentOrderDetail(BigDecimal currentAmount, BigDecimal ceditAmount, AssetRepaymentOrder rawRepaymentOrder, Integer orderStatus, String repaymentComment, Integer repaymentType,
                                                String outOrderNo, String operator)
    {
        final BigDecimal rawCurrentAmount = currentAmount;

        String comment = repaymentComment + "; 本次还款金额: " + rawCurrentAmount;

        if (ceditAmount.compareTo(new BigDecimal(0)) > 0)
        {
            comment = comment + ",减免金额为: " + ceditAmount;
        }

        //已还款历史记录
        AssetRepaymentOrderDetail rod = repaymentOrderDetailDao.selectDetailsByRepaymentOrderId(rawRepaymentOrder.getId());

        // 初始化当前还的本金
        BigDecimal moneyAmount = new BigDecimal(0);
        // 初始化当前还的违约金
        BigDecimal penaltyAmount = new BigDecimal(0);
        // 初始化当前还的滞纳金
        BigDecimal planLateFee = new BigDecimal(0);

        // 当前剩余应还本金
        BigDecimal moneyAmountBalance = rawRepaymentOrder.getMoneyAmount();
        // 当前剩余应违约金
        BigDecimal penaltyAmountBalance = rawRepaymentOrder.getPenaltyAmount();
        // 当前剩余应滞纳金
        BigDecimal planLateFeeBalance = rawRepaymentOrder.getPlanLateFee();

        if (rod != null)
        {
            moneyAmountBalance = rawRepaymentOrder.getMoneyAmount().subtract(rod.getMoneyAmount());
            penaltyAmountBalance = rawRepaymentOrder.getPenaltyAmount().subtract(rod.getPenaltyAmount());
            planLateFeeBalance = rawRepaymentOrder.getPlanLateFee().subtract(rod.getPlanLateFee());
        }

        if (moneyAmountBalance.compareTo(new BigDecimal(0)) > 0 && currentAmount.compareTo(new BigDecimal(0)) > 0)
        {
            if (currentAmount.compareTo(moneyAmountBalance) <= 0)
            {
                moneyAmount = currentAmount;

                currentAmount = currentAmount.subtract(moneyAmount);
            }
            else
            {
                moneyAmount = moneyAmountBalance;

                currentAmount = currentAmount.subtract(moneyAmountBalance);
            }
        }
        if (penaltyAmountBalance.compareTo(new BigDecimal(0)) > 0 && currentAmount.compareTo(new BigDecimal(0)) > 0)
        {
            if (currentAmount.compareTo(penaltyAmountBalance) <= 0)
            {
                penaltyAmount = currentAmount;
                currentAmount = currentAmount.subtract(penaltyAmount);
            }
            else
            {
                penaltyAmount = penaltyAmountBalance;
                currentAmount = currentAmount.subtract(penaltyAmountBalance);
            }
        }

        if (planLateFeeBalance.compareTo(new BigDecimal(0)) > 0 && currentAmount.compareTo(new BigDecimal(0)) > 0)
        {
            if (currentAmount.compareTo(planLateFeeBalance) <= 0)
            {
                planLateFee = currentAmount;
                currentAmount = currentAmount.subtract(planLateFee);
            }
            else
            {
                planLateFee = planLateFeeBalance;
            }
        }

        AssetRepaymentOrderDetail rodModl = new AssetRepaymentOrderDetail();
        rodModl.setBorrowUserId(rawRepaymentOrder.getUserId());
        rodModl.setBorrowOrderId(rawRepaymentOrder.getOrderId());
        rodModl.setRepaymentOrderId(rawRepaymentOrder.getId());
        rodModl.setSumAmount(rawCurrentAmount);
        rodModl.setCeditAmount(ceditAmount);
        rodModl.setMoneyAmount(moneyAmount);
        rodModl.setPenaltyAmount(penaltyAmount);
        rodModl.setPlanLateFee(planLateFee);
        rodModl.setComment(comment);
        rodModl.setRepaymentWay(repaymentType);
        rodModl.setCurrRepStatus(orderStatus);
        rodModl.setPayTime(new Date());
        rodModl.setCreateBy(operator);
        rodModl.setOutOrderNo(outOrderNo);
        this.insertSelective(rodModl);
    }

    @Override
    public void trueRepaymentMoney(AssetRepaymentOrder aro, AssetRepaymentOrderDetailDto paramsDto)
    {
        AssetRepaymentOrder aroModel = new AssetRepaymentOrder();
        aroModel.setId(aro.getId());
        aroModel.setRepaymentedAmount(paramsDto.getCurrentAmount());
        if(aro.getNoOrder()!=null){//设置商户号信息
            aroModel.setNoOrder(aro.getNoOrder()+","+paramsDto.getOutOrderNo());
        }else{
            aroModel.setNoOrder(paramsDto.getOutOrderNo());
        }

        Integer repaymentType = paramsDto.getRepaymentType();
        // 后台手动还款
        aroModel.setRepaymentType(repaymentType);

        if (paramsDto.getCeditAmount() != null && paramsDto.getCeditAmount().compareTo(new BigDecimal(0)) > 0)
        {
            aroModel.setCeditAmount(paramsDto.getCeditAmount());
        }
        else
        {
            paramsDto.setCeditAmount(new BigDecimal(0));
        }

        Integer status = AssetRepaymentOrder.STATUS_YHK;
        Date now = new Date();

        //已还金额 + 已减免金额 + 当前减免金额  +  当前还款金额 +  大于等于  累计应总还金额 表示用户已经结清
        BigDecimal sumAmount = aro.getRepaymentedAmount().add(aro.getCeditAmount()).add(paramsDto.getCeditAmount()).add(paramsDto.getCurrentAmount());

        //TODO 重写还款逻辑
        if (sumAmount.compareTo(aro.getRepaymentAmount()) >= 0)
        {
            aroModel.setRepaymentRealTime(now);

            //如果逾期天数大于0，则表示逾期还清，反之正常还清
            if (aro.getLateDay() > 0)
            {
//                status = AssetOrderStatusHis.STATUS_YQHQ;
            }
            else
            {
                status = AssetOrderStatusHis.STATUS_YHK;
            }
            //关闭订单
            AssetBorrowOrder assetBorrowOrder = new AssetBorrowOrder();
            assetBorrowOrder.setId(aro.getOrderId());
            assetBorrowOrder.setStatus(status);
            assetBorrowOrder.setOrderEnd(1);
            assetBorrowOrder.setUpdateTime(now);
            iAssetBorrowOrderService.updateByPrimaryKeySelective(assetBorrowOrder);

            //记录状态变更
            AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
            assetOrderStatusHis.setOrderId(aro.getOrderId());
            assetOrderStatusHis.setOrderStatus(status);
            assetOrderStatusHis.setCreateTime(now);
            //assetOrderStatusHis.setRemark("已取消交易，返还预付款+违约金，共" + aro.getRepaymentAmount() + "元");
            assetOrderStatusHis.setRemark(AssetOrderStatusHis.orderStatusMap.get(status));
            iAssetOrderStatusHisDao.insert(assetOrderStatusHis);
            
            /**
             * 用户提额
             */
//            comboUserSerivce.promotionCombo(aro.getUserId());
        }
        else
        {
            //如果逾期天数大于0，则表示逾期还清，反之正常还清
//            if (aro.getLateDay() > 0)
//            {
//                status = AssetOrderStatusHis.STATUS_YQBFHK;
//            }
//            else
//            {
//                // 如果在原有的基础上，进行更改状态，方便统计及app展示
//                if (AssetOrderStatusHis.STATUS_BHG.intValue() == aro.getOrderStatus().intValue() || AssetOrderStatusHis.STATUS_BHGBFHK.intValue() == aro.getOrderStatus().intValue())
//                {
//                    status = AssetOrderStatusHis.STATUS_BHGBFHK; //不合格部分还款
//                }
//                else
//                {
//                    status = AssetOrderStatusHis.STATUS_BFHK;
//                }
//            }
            //修改订单状态
            AssetBorrowOrder assetBorrowOrder = new AssetBorrowOrder();
            assetBorrowOrder.setId(aro.getOrderId());
            assetBorrowOrder.setStatus(status);
            assetBorrowOrder.setUpdateTime(now);
            iAssetBorrowOrderService.updateByPrimaryKeySelective(assetBorrowOrder);

        }
        aroModel.setUpdateTime(now);
        aroModel.setUpdateAccount(paramsDto.getCreateBy());
        aroModel.setOrderStatus(status);

        //更新还款信息
        //iAssetRepaymentOrderService.updateReaymentInfo(aroModel);

        // 更新为老用户
        BorrowUser user = new BorrowUser();
        user.setId(aro.getUserId());
        user.setIsOld(1);
        this.borrowUserService.updateUser(user);
        insertAssetRepaymentOrderDetail(paramsDto.getCurrentAmount(), paramsDto.getCeditAmount(), aro, status, paramsDto.getComment(), repaymentType, paramsDto.getOutOrderNo(), paramsDto.getOperator());

    }

}
