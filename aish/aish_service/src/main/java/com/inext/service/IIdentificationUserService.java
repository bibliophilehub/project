package com.inext.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;

public interface IIdentificationUserService {


	PageInfo<Map<String, Object>> getPageList(Map<String, Object> params);

}
