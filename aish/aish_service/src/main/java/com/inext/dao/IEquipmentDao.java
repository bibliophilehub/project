package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.EquipmentInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEquipmentDao extends BaseDao<EquipmentInfo> {

    // 查询总用户数 -->
    public List<EquipmentInfo> getEquipmentList();


    @Select("select * from app_equipment_info where id=#{0}")
    EquipmentInfo getDataById(String equipmentId);


	public void delById(String id);
}
