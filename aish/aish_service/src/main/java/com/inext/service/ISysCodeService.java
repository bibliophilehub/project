package com.inext.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface ISysCodeService {

    void getRbArea(JSONObject json, HttpServletRequest request);

}
