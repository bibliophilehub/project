package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.HcDfInfo;
import com.inext.entity.HcRepaymentInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 */
public interface HcDfInfoDao extends BaseDao<HcDfInfo> {

    @Select("SELECT * FROM hc_df_info WHERE `status` = 0")
    List<HcDfInfo> getHcDfInfoList();
}
