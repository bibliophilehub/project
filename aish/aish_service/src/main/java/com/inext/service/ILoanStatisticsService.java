package com.inext.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

public interface ILoanStatisticsService {


    List<Map<String, Object>> exportExcelFile(Map<String, Object> params);

    PageInfo<Map<String, Object>> getPageList(Map<String, Object> params);


}
