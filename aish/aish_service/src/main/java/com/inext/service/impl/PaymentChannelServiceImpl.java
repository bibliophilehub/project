package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IPaymentChannelDao;
import com.inext.entity.PaymentChannel;
import com.inext.result.ApiServiceResult;
import com.inext.service.IPaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lisige
 */
@Service
public class PaymentChannelServiceImpl implements IPaymentChannelService {

    @Autowired
    IPaymentChannelDao paymentChannelDao;

    @Override
    public PageInfo<PaymentChannel> selectPaging(Map<String, Object> params) {

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


        return new PageInfo<>(this.paymentChannelDao.selectAll());
    }


    @Override
    public ApiServiceResult update(PaymentChannel paymentChannel) {


        this.paymentChannelDao.updateByPrimaryKeySelective(paymentChannel);
        return new ApiServiceResult();
    }
}
