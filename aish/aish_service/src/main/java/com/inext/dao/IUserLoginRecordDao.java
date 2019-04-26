package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.UserLoginRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by 李思鸽 on 2017/5/17 0017.
 */
@Repository("userLoginRecordDao")
public interface IUserLoginRecordDao extends BaseDao<UserLoginRecord> {


}
