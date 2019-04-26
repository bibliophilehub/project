package com.inext.service.impl;

import com.inext.constants.RedisCacheConstants;
import com.inext.dao.IAPPAutologinLogDao;
import com.inext.entity.AppAutoLoginLog;
import com.inext.entity.BorrowUser;
import com.inext.result.ApiServiceResult;
import com.inext.service.IAppAutoLoginLogService;
import com.inext.utils.RedisUtil;
import com.inext.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("appAutoLoginLogService")
public class AppAutoLoginLogServiceImpl implements IAppAutoLoginLogService {

    @Autowired
    private IAPPAutologinLogDao autologinLogDao;

    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    /***
     * 查询
     * @param params
     * @return
     */
    @Override
    public AppAutoLoginLog selectByParams(Map<String, String> params) {
        return this.autologinLogDao.selectByParams(params);
    }

    /****
     * 新增
     * @param log
     */
    @Async
    @Override
    public void saveAPPAutologinLog(AppAutoLoginLog log) {
        this.autologinLogDao.insertSelective(log);
    }

    /***
     * 修改
     */
    @Async
    @Override
    public void updateAPPAutologinLog(AppAutoLoginLog log) {
        this.autologinLogDao.updateByPrimaryKeySelective(log);
    }

    @Override
    public AppAutoLoginLog selectByUserId(Integer userId) {
        return autologinLogDao.selectByUserId(userId);
    }

    @Override
    public ApiServiceResult generateToken(BorrowUser user) {

        String key = RedisCacheConstants.TOKENCODE + user.getUserAccount();

        String token = TokenUtils.getTempToken(key);
        // 从数据库中根据token获取当前用户信息
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        AppAutoLoginLog appAutologinLog = this.selectByParams(params);
        if (appAutologinLog == null) {
            appAutologinLog = new AppAutoLoginLog();
            appAutologinLog.setUserId(user.getId());
            appAutologinLog.setUserAccount(user.getUserAccount());
            appAutologinLog.setUserPassword(user.getUserPassword());
        }

        appAutologinLog.setToken(token);
        appAutologinLog.setEffTime(TokenUtils.getEffTime(new Date()));
        appAutologinLog.setLoginTime(new Date());
        if (appAutologinLog.getId() == null) {
            this.autologinLogDao.insertSelective(appAutologinLog);
        } else {
            this.autologinLogDao.updateByPrimaryKeySelective(appAutologinLog);
        }

        return new ApiServiceResult("登录成功").setExt(appAutologinLog.getToken());
    }
}
