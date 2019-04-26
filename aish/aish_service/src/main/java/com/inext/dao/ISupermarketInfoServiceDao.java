package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.SupermarketInfo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface ISupermarketInfoServiceDao extends BaseDao<SupermarketInfo> {

	@Select("select * from  supermarket_info where status=1 ORDER BY sort")
    List<Map> getShowList();

    @Select("select * from  supermarket_info ORDER BY sort")
    List<SupermarketInfo> getList();

    @Update("UPDATE supermarket_info set click_num=click_num+1 WHERE id=#{0}")
    int doclick(int id);
}
