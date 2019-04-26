package com.inext.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.fc.csplatform.common.crypto.Base64Util;
import com.alipay.fc.csplatform.common.crypto.CustomerInfoCryptoUtil;
import com.inext.dao.IBorrowUserDao;
import com.inext.entity.BorrowUser;
import com.inext.service.IBackConfigParamsService;
import com.inext.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 在线客服
 */
@RestController
@RequestMapping(value = "/customer")
public class CustomerController extends BaseController {

    Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private IBorrowUserDao borrowUserDao;

    @ApiOperation(value = "拉起弹窗url")
    @RequestMapping (value = "/getChatUrl")
    public Result getChatUrl(HttpServletRequest request) {
        Result<String> result = new Result<>();
        String uri = "";
        Map<String, String> map = backConfigParamsService.getBackConfig("ALI_CUSTOMER", null);
        /*BorrowUser bu = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN));
        if (bu != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",bu.getId());
            jsonObject.put("uname",bu.getUserName());
            jsonObject.put("timestamp",System.currentTimeMillis());
            String cinfo = jsonObject.toJSONString();
//            String cinfo = "{\"userId\":\"123456\",\"userName\":\"alps\",\"extInfo\":{\"userId\":\"123456\",\"userName\":\"alps\"}}";
            try {
                PublicKey publicKey = getPubKey(map.get("PUBLIC_KEY"));
                Map<String, String> res =  CustomerInfoCryptoUtil.encryptByPublicKey(cinfo, publicKey);
                uri = "&key=" + res.get("key") + "&cinfo=" + res.get("text");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/
        result.setCode("0");
        result.setMessage("成功");

        String customerUrl = map.get("CUSTOMER_URL");
        //"https://cschat-ccs.aliyun.com/index.htm?tntInstId=_1Boj1Ds&scene=SCE00003758"
        result.setData(customerUrl+uri);
        return result;
    }

    /**
     * 通过外部接口方式接入访客名片.
     * 工作台显示访客信息, 请使用此方法.
     * note : 本接口必须支持跨域访问.
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/customerinfo", method = RequestMethod.POST)
    public Map<String, Object> customerinfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            //这两个参数是云客服工作台调用该接口 post body带入的参数.
            String params = request.getParameter("params");
            String key = request.getParameter("key");
            logger.info("customerinfo====requestData=========params:"+params);
            logger.info("customerinfo====requestData=========key:"+key);

            //DEMO 的 样例params 和 key
            if (StringUtils.isEmpty(params) && StringUtils.isEmpty(key)) {
                //这里是样例, 请在正式环境删掉这两行内容, 使用从request中取得的内容
                params = "WaSE34kPAd%2bPBmwyrprHXsSFTIJcmO9k24Eg3E4DjKTjWZDn6B0etM83raFaFwORUpyUr%2bsucVxe9Ec9Bdoe71DeptGjVZKxv0wELBG8"
                        + "%2f9TlQumtjHXNo%2f1LP4I7%2fU640dh%2bvnzmkuppS3DOOCgDgXP0nI%2bB%2fEXAjFxSqc592B5SNflrqBB%2fc6%2bVZGJhqtoxOg8Vmqitmym%2f06Eji0YibA%3d%3d";

                key = "bzQyS7PRQftEugf2QaWPI%2fvFCpbsovf%2bmWjS64hWhSv1gLm%2blbjDZw6tO6NgyFLq1xNobXHvtxV5lCD%2bBh21Qcn9qypFsjKGmXj3"
                        + "%2fmr0wDDY0u3ea4Dmu7YvHiKQ51bqQOJ1SgwqeQUsC07TvN7B45VlVWXm6u3JWKqd4%2f054EtH6qhtR50t"
                        + "%2bOa7PMgCzOjr1H5fiDhTrpxIEePQ7rulVdDorJr1PfBc7WiAcSJ4iEfXOAmwOSzNGF1UJxPLd1yaBme43sveY6cBMKTj5HxWwqkm3wvaaYtdX2a5VYIpdLm0pm1ETDPAUpQQHoTTqE3Mbzryc7OpZf9B%2fYvOHgN07A%3d%3d";
            }
            Map<String, String> map = backConfigParamsService.getBackConfig("ALI_CUSTOMER", null);
            PublicKey publicKey = getPubKey(map.get("PUBLIC_KEY"));
//            PublicKey publicKey = getPubKey(PUB_KEY);
            //cinfo是用于客户在自己的后台系统来查询该用户更为详细的其他信息.
            String cinfo = CustomerInfoCryptoUtil.decryptByPublicKey(params, key, publicKey);
            JSONObject jsonObject = JSON.parseObject(cinfo);
            String userId = (String)jsonObject.get("userId");
            BorrowUser existUser = borrowUserDao.selectByPrimaryKey(Integer.valueOf(userId));

            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("success",true);
            resultMap.put("message","查询成功");

            HashMap<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("telePhone",existUser.getUserPhone());
            dataMap.put("userName",existUser.getUserName());
            dataMap.put("userId",existUser.getId());

            resultMap.put("result",dataMap);
            return resultMap;



            //样例输出
            //{"userId":"1043f16e2ee4185dbfe421ff32bc196e","sourceType":"online","agentId":"0002","extInfo":{"uid":"1043f16e2ee4185dbfe421ff32bc196e","userName":"访客"}}


            //查询到的用户信息按照下面格式拼凑成一个json格式内容, 返回. 请严格按照下面结构形态
            /**
             {
             "message": "查询成功", //写死
             "result": {
             "schema": {
             "properties": {
             "email": {
             "name": "邮箱", //自定义参数属性定义
             "type": "text" //参数类型 可以为: text, string, email, time
             },
             "telePhone": {
             "name": "电话", //自定义参数属性定义
             "type": "text" //参数类型
             }
             }
             },
             "telePhone": "11111111111", //自定义参数
             "email":"xxx.email.com", //自定义参数
             "userId": "1646818", //userId参数必填
             "userName": "test" //userName参数必填
             },
             "success": true //写死
             }
             note : 自定义参数要在schema中和result下面一一对应.
             */

//            String mockResult = "{\"message\":\"查询成功\",\"result\":{\"schema\":{\"properties\":{\"email\":{\"name\":\"邮箱\","
//                    + "\"type\":\"text\"},\"telePhone\":{\"name\":\"电话\",\"type\":\"text\"}}},\"telePhone\":\"11111111111\","
//                    + "\"email\":\"xxx.email.com\",\"userId\":\"1646818\",\"userName\":\"test\"},\"success\":true}";
//            return JSONObject.parseObject(mockResult);
        } catch (Exception e) {
            //这里建议使用log记录错误. 减少干扰信息, DEMO里不使用LOG
            e.printStackTrace();
            logger.error("阿里云客服回调访客信息，调用失败",e);
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("success",false);
            resultMap.put("message","查询失败");
            return resultMap;
        }
    }
    //还原出RSA公钥对象
    private PublicKey getPubKey(String pubKey) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Util.decode(pubKey));
        KeyFactory keyFactory;
        keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        return key;
    }
}
