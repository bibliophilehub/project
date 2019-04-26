package com.inext.service.impl;

import com.inext.dao.IAssetOrderStatusHisDao;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.service.IAssetOrderStatusHisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("orderHis")
public class AssetOrderStatusHisServiceImpl implements IAssetOrderStatusHisService {

    @Resource
    IAssetOrderStatusHisDao iAssetOrderStatusHisDao;

    @Override
    public AssetOrderStatusHis getLastOrderHis(Integer orderId) {

        return iAssetOrderStatusHisDao.getLastOrderHis(orderId);
    }

    @Override
    public List<AssetOrderStatusHis> getOrderHisListByOrderId(Integer orderId) {

        return iAssetOrderStatusHisDao.getOrderHisListByOrderId(orderId);
    }

    @Override
    public int saveHis(AssetOrderStatusHis his) {

        return iAssetOrderStatusHisDao.insertSelective(his);
    }

	@Override
	public void removeHis(Map<String, Object> params) {
		 
		iAssetOrderStatusHisDao.removeHis(params);
		
	}

}
