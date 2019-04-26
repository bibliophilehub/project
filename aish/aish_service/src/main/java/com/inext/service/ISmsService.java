package com.inext.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface ISmsService {

    void getSmsCode(HttpServletRequest request, JSONObject json);

}
