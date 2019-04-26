package com.inext.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ILoanStatisticsDao  {


    List<Map<String, Object>> getLoanStatistics(Map<String, Object> params);




}
