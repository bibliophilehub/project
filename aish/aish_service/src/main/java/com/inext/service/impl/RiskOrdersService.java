package com.inext.service.impl;

import com.inext.dao.IRiskOrdersDao;
import com.inext.entity.RiskOrders;
import com.inext.service.IRiskOrdersService;
import com.inext.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiskOrdersService implements IRiskOrdersService {
    @Autowired
    private IRiskOrdersDao ordersDao;

    @Override
    public int insert(RiskOrders orders) {
        if (StringUtils.isBlank(orders.getAddIp())) {
            orders.setAddIp(RequestUtils.getIpAddr());
        }
        return ordersDao.insertSelective(orders);
    }

    @Override
    public int update(RiskOrders orders) {
        return ordersDao.updateByPrimaryKeySelective(orders);
    }

    @Override
    public RiskOrders findById(Integer id) {
        return ordersDao.selectByPrimaryKey(id);
    }
}
