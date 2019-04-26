package com.inext.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

public interface IRepaymentStatisticsService {


    List<Map<String, Object>> exportExcelFile(Map<String, Object> params);

    PageInfo<Map<String,Object>> getPageList(Map<String, Object> params);

    PageInfo<Map<String,Object>> getRepayStatistics(Map<String, Object> params);
    
    List<Map<String,Object>> exportExcelFileStatistics(Map<String, Object> params);
}
