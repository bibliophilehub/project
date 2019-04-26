package com.inext.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author lisige
 * @Description logger.info输出请求响应信息
 */
@org.springframework.web.bind.annotation.RestControllerAdvice(basePackages = "com.inext.controller")
public class RestControllerAdvice implements ResponseBodyAdvice {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        logger.info("request url : " + request.getURI() + " response body : " + JSONObject.toJSONString(body));
        return body;
    }
}
