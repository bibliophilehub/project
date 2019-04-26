
package com.inext.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.inext.configuration.YeePayConfig;
import com.inext.constants.Constants;
import com.inext.entity.BackConfigParams;
import com.yeepay.g3.sdk.yop.client.YopClient3;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class YeepayServiceUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(YeepayServiceUtil.class);

    public static Map<String, String> yeepayYOP(Map<String, String> map, String Uri) throws IOException
    {
        String appKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("APP_KEY");
        String appSecretKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.YIBAO).get("YOP_PUBLIC_KEY");
        String serverRoot = YeePayConfig.getInstance().getValue("serverRoot");
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        LOGGER.info("向YOP发请求:"+appKey+","+appSecretKey+","+serverRoot );
        YopRequest yoprequest = new YopRequest(appKey, appSecretKey, serverRoot);
        //        YopRequest yoprequest = new YopRequest();
        //YopRequest yoprequest = new YopRequest(appKey);
        Map<String, String> result = new HashMap<String, String>();

        Set<Entry<String, String>> entry = map.entrySet();
        for (Entry<String, String> s : entry)
        {
            yoprequest.addParam(s.getKey(), s.getValue());
        }
        LOGGER.info("易宝代付请求参数：" + JSON.toJSONString(entry));

        //向YOP发请求
        YopResponse yopresponse = YopClient3.postRsa(Uri, yoprequest);
        LOGGER.info("易宝返回结果：" + JSON.toJSONString(yopresponse));
        System.out.println("请求YOP之后结果：" + yopresponse.toString());
        System.out.println("请求YOP之后结果：" + yopresponse.getStringResult());

        //        	对结果进行处理
        if ("FAILURE".equals(yopresponse.getState()))
        {
            if (yopresponse.getError() != null)
            {
                result.put("errorcode", yopresponse.getError().getCode());
            }
            result.put("errormsg", yopresponse.getError().getMessage());
            LOGGER.info("错误明细：" + yopresponse.getError().getSubErrors());
            LOGGER.info("系统处理异常结果：" + result);
            return result;
        }
        //成功则进行相关处理
        if (yopresponse.getStringResult() != null)
        {
            result = parseResponse(yopresponse.getStringResult());
            LOGGER.info("易宝接收成功：" + result);
        }

        return result;
    }

    //将获取到的yopresponse转换成json格式
    public static Map<String, String> parseResponse(String yopresponse)
    {

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap = JSON.parseObject(yopresponse, new TypeReference<TreeMap<String, String>>()
        {
        });
        return jsonMap;
    }

}
