package com.inext.service.pay.impl;

import com.inext.constants.Constants;
import com.inext.dao.IAssetBorrowOrderDao;
import com.inext.dao.IMakeLoansRecordDao;
import com.inext.dao.IPaymentChannelDao;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.BackConfigParams;
import com.inext.entity.MakeLoansRecord;
import com.inext.entity.PaymentChannel;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiServiceResult;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.pay.IPayForAnotherService;
import com.inext.utils.CompareUtils;
import com.inext.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author lisige
 * @description 默认的支付方式限制
 */
public abstract class AbstractPayForAnother implements IPayForAnotherService {


    @Autowired
    private IMakeLoansRecordDao makeLoansRecordDao;

    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @Autowired
    private IPaymentChannelDao paymentChannelDao;


    @Autowired
    private IAssetBorrowOrderDao assetBorrowOrderDao;

    /**
     * 核实该支付方式限制是否都满足 如果否  那么返回非成功调用下一个
     * 默认都是符合要求的 如果支付通道有独有需求  请继承该类并在重写该方时先调用父类的验证验证一次  如果不需要请完全重写该方法内容
     *
     * @return
     */
    @Override
    public ApiServiceResult verifyMakeLoansCondition(AssetBorrowOrder assetBorrowOrder, Integer paymentChannelId) {

        Map<String, String> feeAccount = backConfigParamsService.getBackConfig(BackConfigParams.FEE_ACCOUNT, null);
        // 是否开启限额开关 如果未开启限额开关直接验证通过
        if (!Constants.STATUS_VALID.equals(feeAccount.get("quota_switch"))) {
            return new ApiServiceResult();
        }

        String today = DateUtil.getDateFormat(DateUtil.DEFAULT_FORMAT);
        MakeLoansRecord mlrParam = new MakeLoansRecord();
        mlrParam.setDate(today);
        mlrParam.setPaymentChannelId(paymentChannelId);

        // 获取当前支付通道当日放款资金记录
        MakeLoansRecord makeLoansRecord = makeLoansRecordDao.selectOne(mlrParam);
        // 获取
        BigDecimal countMakeLoansMoney = makeLoansRecord == null
                ? assetBorrowOrder.getPerPayMoney()
                : assetBorrowOrder.getPerPayMoney().add(makeLoansRecord.getMakeLoansMoney());

        // 获取该支付通道的限额
        PaymentChannel paymentChannel = this.paymentChannelDao.selectByPrimaryKey(paymentChannelId);
        // 该笔订单金额 + 今日放款金额 >= 系统配置的限额 那么将拒绝本次放款
        if (CompareUtils.greaterEquals(countMakeLoansMoney, paymentChannel.getMoneyLimit())) {
            //
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "当前订单金额+今日放款金额已达系统配置上限");
        }
        // 如果这是今天第一笔订单 那么先新增该支付通道今日放款金额记录 否则直接累加金额并修改
        if (makeLoansRecord == null) {
            makeLoansRecord = new MakeLoansRecord();
            makeLoansRecord.setDate(today);
            makeLoansRecord.setMakeLoansMoney(assetBorrowOrder.getPerPayMoney());
            makeLoansRecord.setPaymentChannelId(paymentChannelId);
            this.makeLoansRecordDao.insert(makeLoansRecord);
        } else {
            // 目标是已经尝试过放款的并且放款失败的金额不累计
            // 每一笔订单在放款前都会生成一个提供给第三方的商户订单号
            // 在第一次尝试条件验证的时候不会保存到数据库 只有当放款通过或者放款失败的时候会记录至数据库
            // 那么当该笔订单在数据库未存在第三方订单号 表示是第一次尝试放款需要累计金额
            AssetBorrowOrder p = this.assetBorrowOrderDao.getOrderByNoOrder(assetBorrowOrder.getNoOrder());
            if (p == null) {
                makeLoansRecord.setMakeLoansMoney(assetBorrowOrder.getPerPayMoney().add(makeLoansRecord.getMakeLoansMoney()));
                this.makeLoansRecordDao.updateByPrimaryKey(makeLoansRecord);
            }
        }

        return new ApiServiceResult();
    }
}
