package com.inext.service.impl;

import com.inext.dao.IUserLoginRecordDao;
import com.inext.entity.UserLoginRecord;
import com.inext.result.ServiceResult;
import com.inext.service.IUserLoginRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by 李思鸽 on 2017/5/17 0017.
 */
@Service("userLoginRecordService")
public class UserLoginRecordServiceImpl implements IUserLoginRecordService {

    private static Logger loger = LoggerFactory.getLogger(UserLoginRecordServiceImpl.class);

    @Resource(name = "userLoginRecordDao")
    IUserLoginRecordDao userLoginRecordDao;

    @Async
    @Override
    public ServiceResult saveUserLoginRecord(Integer userId, String loginIp) {
        try {
            UserLoginRecord userLoginRecord = new UserLoginRecord();
            userLoginRecord.setUserId(userId);
            userLoginRecord.setLoginIp(loginIp);
            userLoginRecord.setCreateTime(new Date());
            this.userLoginRecordDao.insert(userLoginRecord);
        } catch (Exception e) {
            loger.error(e.getMessage(), e);
        }
        return new ServiceResult();
    }
}
