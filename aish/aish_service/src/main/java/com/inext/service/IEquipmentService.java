package com.inext.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.inext.entity.ChannelInfo;
import com.inext.entity.EquipmentInfo;

/**
 * Created by user on 2018/1/6.
 */
public interface IEquipmentService {
	



    public void updateById(EquipmentInfo equipmentInfo);

    public void insert(EquipmentInfo equipmentInfo);
    
    public void delById(String id);

    PageInfo<EquipmentInfo> getPageList(Map<String, Object> params);
    
    List<EquipmentInfo> getList();

    EquipmentInfo getEquipmentById(String id);
    
    
    


}
