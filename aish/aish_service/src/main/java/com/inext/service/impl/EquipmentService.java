package com.inext.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IChannelDao;
import com.inext.dao.IEquipmentDao;
import com.inext.entity.ChannelInfo;
import com.inext.entity.EquipmentInfo;
import com.inext.service.IChannelService;
import com.inext.service.IEquipmentService;

@Service
public class EquipmentService implements IEquipmentService {

    @Autowired
    private IEquipmentDao equipmentDao;


    @Override
    public void updateById(EquipmentInfo equipmentInfo) {
    	equipmentDao.updateByPrimaryKey(equipmentInfo);
    }


    @Override
    public PageInfo<EquipmentInfo> getPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage = Constants.INITIAL_CURRENT_PAGE;
            int pageSize = Constants.INITIAL_PAGE_SIZE;
            if (params.get(Constants.CURRENT_PAGE) != null && !"".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = com.inext.utils.StringUtils.getInteger(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.get(Constants.PAGE_SIZE) != null && !"".equals(params.get(Constants.PAGE_SIZE))) {
                pageSize = com.inext.utils.StringUtils.getInteger(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        List<EquipmentInfo> list = this.equipmentDao.getEquipmentList();
        PageInfo<EquipmentInfo> pageInfo = new PageInfo<EquipmentInfo>(list);
        return pageInfo;

    }


    @Override
    public EquipmentInfo getEquipmentById(String id) {

        return equipmentDao.getDataById(id);
    }


    @Override
    public void insert(EquipmentInfo equipmentInfo) {
//        channelDao.insert(channel);
    	equipmentDao.insert(equipmentInfo);
    }


	@Override
	public List<EquipmentInfo> getList() {
		return this.equipmentDao.getEquipmentList();
	}


	@Override
	public void delById(String id) {
		equipmentDao.delById(id);
	}






}
