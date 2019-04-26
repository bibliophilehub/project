package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.*;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
@Repository
public interface IUserAuthRecordDao extends BaseDao<UserAuthRecord> {


    void saveRecord(UserAuthRecord record);

    void updateRecord(UserAuthRecord record);

    List<UserAuthRecord> getRecordByUserId(Map<String, Object> params);

}
