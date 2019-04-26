package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IBackUserDao;
import com.inext.dao.IBackUserLoginRecordDao;
import com.inext.entity.BackUser;
import com.inext.entity.BackUserLoginRecord;
import com.inext.service.IBackUserLoginRecordService;
import com.inext.utils.DateUtils;
import com.inext.utils.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenkk on 2017/8/16
 */
@Service("backUserLoginRecordService")
public class BackUserLoginRecordServiceImpl implements IBackUserLoginRecordService {
    @Autowired
    @Qualifier("backUserLoginRecordDao")
    private IBackUserLoginRecordDao backUserLoginRecordDao;
    @Resource
    private IBackUserDao backUserDao;

    @Override
    public String getMaxId() {
        return backUserLoginRecordDao.getMaxId();
    }

    @Async
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void doSave(BackUserLoginRecord backUserLoginRecord) {
        String id = backUserLoginRecordDao.getMaxId();
        if (StringUtils.isBlank(id)) {
            id = "SUL" + DateUtils.getDate(DateUtils.FORMAT) + "000001";
        } else {
            id = IdUtil.getIdDate(id, 15, 3);
        }
        backUserLoginRecord.setId(id);
        backUserLoginRecord.setCreateTime(new Date());
        backUserLoginRecordDao.insertSelective(backUserLoginRecord);
        BackUser backUser = new BackUser();
        backUser.setId(backUserLoginRecord.getUserId());
        backUser.setUserLoginTime(new Date());
        backUserDao.updateByPrimaryKeySelective(backUser);
    }

    @Override
    public PageInfo<BackUserLoginRecord> getPageList(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        int currentPage;
        int pageSize = Constants.INITIAL_PAGE_SIZE;

        if (params.get(Constants.CURRENT_PAGE) == null || "".equals(params.get(Constants.CURRENT_PAGE))) {
            currentPage = Constants.INITIAL_CURRENT_PAGE;
        } else {
            currentPage = Integer.parseInt(params.get(Constants.CURRENT_PAGE) + "");
        }
        if (params.containsKey(Constants.PAGE_SIZE)) {
            pageSize = Integer.parseInt(params.get(Constants.PAGE_SIZE) + "");
        }
        PageHelper.startPage(currentPage, pageSize);
        PageInfo<BackUserLoginRecord> pageInfo = new PageInfo<BackUserLoginRecord>(this.backUserLoginRecordDao.getPageList(params));
        return pageInfo;
    }

}
