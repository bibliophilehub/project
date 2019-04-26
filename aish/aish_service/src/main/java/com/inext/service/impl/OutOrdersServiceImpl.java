package com.inext.service.impl;

import com.inext.dao.IOutOrdersDao;
import com.inext.entity.OutOrders;
import com.inext.service.IOutOrdersService;
import com.inext.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class OutOrdersServiceImpl implements IOutOrdersService {
	@Autowired
	private IOutOrdersDao ordersDao;

	@Override
	public int insert(OutOrders orders) {
		if (StringUtils.isBlank(orders.getAddIp())) {
			orders.setAddIp(RequestUtils.getIpAddr());
		}
		return ordersDao.insert(orders);
	}

	@Override
	public int update(OutOrders orders) {
		return ordersDao.update(orders);
	}

	@Override
	public int updateByOrderNo(OutOrders orders){
		return ordersDao.updateByOrderNo(orders);
	}

	@Override
	public OutOrders findById(Integer id) {
		return ordersDao.findById(id);
	}

	@Override
	public OutOrders findByOrderNo(String orderNo){
		return ordersDao.findByOrderNo(orderNo);
	}

	@Override
	public int insertByTablelastName(OutOrders orders) {
		// TODO Auto-generated method stub
		return ordersDao.insertByTablelastName(orders);
	}

	@Override
	public int updateByTablelastName(OutOrders orders) {
		// TODO Auto-generated method stub
		return ordersDao.updateByTablelastName(orders);
	}

	@Override
	public int updateByOrderNoByTablelastName(OutOrders orders) {
		// TODO Auto-generated method stub
		return ordersDao.updateByOrderNoByTablelastName(orders);
	}

	@Override
	public OutOrders findByIdByTablelastName(Integer id, String TablelastName) {
		// TODO Auto-generated method stub
		return ordersDao.findByIdByTablelastName(id, TablelastName);
	}

	@Override
	public OutOrders findByOrderNoByTablelastName(String orderNo, String TablelastName) {
		// TODO Auto-generated method stub
		return ordersDao.findByOrderNoByTablelastName(orderNo, TablelastName);
	}

	@Override
	public int insertSelectiveStatus(OutOrders orders) {
		// TODO Auto-generated method stub
		return ordersDao.insertSelectiveStatus(orders);
	}
	
	@Override
	public List<OutOrders> findByOrderNoByTablelast(HashMap<String, Object> params) {
		return ordersDao.findByOrderNoByTablelast(params);
	}
}
