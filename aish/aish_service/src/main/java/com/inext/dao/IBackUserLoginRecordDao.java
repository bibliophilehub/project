package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.BackUserLoginRecord;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("backUserLoginRecordDao")
public interface IBackUserLoginRecordDao extends BaseDao<BackUserLoginRecord> {
    @Select("select MAX(id) from back_user_login_record")
    public String getMaxId();

    public List<BackUserLoginRecord> getPageList(Map<String, Object> params);

}
