package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.BackUserLoginRecord;

import java.util.Map;

public interface IBackUserLoginRecordService {
    public String getMaxId();

    void doSave(BackUserLoginRecord backUserLoginRecord);

    PageInfo<BackUserLoginRecord> getPageList(Map<String, Object> params);
}
