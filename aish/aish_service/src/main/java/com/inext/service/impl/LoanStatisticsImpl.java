package com.inext.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.ILoanStatisticsDao;
import com.inext.service.ILoanStatisticsService;
import com.inext.utils.DateUtil;
import com.inext.utils.DateUtils;

@Service("loanStatisticsService")
public class LoanStatisticsImpl implements ILoanStatisticsService {

    @Resource
    ILoanStatisticsDao loanStatisticsDao;


    @Override
    public PageInfo<Map<String, Object>> getPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage = Constants.INITIAL_CURRENT_PAGE;
            int pageSize = Constants.INITIAL_PAGE_SIZE;
            if (params.get(Constants.CURRENT_PAGE) != null && !"".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = com.inext.utils.StringUtils.getInteger(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.get(Constants.PAGE_SIZE) != null && !"".equals(params.get(Constants.PAGE_SIZE))) {
                pageSize = com.inext.utils.StringUtils.getInteger(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        List<Map<String, Object>> list = loanStatisticsDao.getLoanStatistics(params);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
        return pageInfo;
    }


    @Override
    public List<Map<String, Object>> exportExcelFile(Map<String, Object> params) {
        List<Map<String, Object>> list = loanStatisticsDao.getLoanStatistics(params);
        return list;
    }


	
}
