package com.inext.service;

import com.inext.result.ApiServiceResult;

import java.io.IOException;
import java.util.Map;

public interface IBaoFooPayService {
    /**
     * 新颜绑卡获取 token
     */
    public Map<String, Object> getBindCardToken(Map<String, String> tokenParaMap);

    /**
     * 新颜绑卡 验证
     *
     * @param validateParaMap
     * @return
     */
    ApiServiceResult bindCardValidate(Map<String, String> validateParaMap) throws IOException;
}
