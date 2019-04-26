package com.inext.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.inext.entity.BorrowUser;
import com.inext.entity.PlatfromAdvise;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IAdviseService {


    List<PlatfromAdvise> getList(Map<String, Object> param);


    PageInfo<PlatfromAdvise> getPageList(Map<String, Object> params);


    void savePlatfromAdvise(JSONObject json, HttpServletRequest request, BorrowUser bu);
}
