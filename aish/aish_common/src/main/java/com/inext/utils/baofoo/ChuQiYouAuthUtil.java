package com.inext.utils.baofoo;

import com.inext.utils.JsonUtils;
import com.inext.utils.baofoo.util.HttpUtil;
import com.inext.utils.chuqiyou.CreateMD5;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 出其右风控
 * @author cannavaro
 *
 */
@Component
public class ChuQiYouAuthUtil {

    protected static String cqy_merchantId;
    protected static String cqy_appId;
    protected static String cqy_appKey;
    protected static String cqy_auth_url;
    protected static String cqy_auth_query_url;
    protected static String cqy_operator_original_query_url;


    private static Logger log = LoggerFactory.getLogger(ChuQiYouAuthUtil.class);

    static {

        //
    	cqy_merchantId = PropertiesUtil.get("cqy_merchantId");//商户号
    	cqy_appId = PropertiesUtil.get("cqy_appId");
    	cqy_appKey = PropertiesUtil.get("cqy_appKey");

    	cqy_auth_url = PropertiesUtil.get("cqy_authurl");
    	//运营商认证结果查询地址
        cqy_auth_query_url =PropertiesUtil.get("cqy_auth_query_url");
        //运营商原始数据
        cqy_operator_original_query_url=PropertiesUtil.get("cqy_operator_original_query_url");

    }
    
    public Map<String, Object> Auth(
            String userId,//用户id
            String userName,//姓名
            String mobile,//手机号码
            String cardNo,//身份证
            String themeColor //主题颜色
    ) throws IOException {
    	
        String merchantId = cqy_merchantId;
        String appId = cqy_appId;
        String appKey = cqy_appKey;
        String auth_url = cqy_auth_url;
        
        //待加密字符串
        String vai_str = merchantId+appId+appKey+userId;
        
        //加密字符串
        String md5_str = CreateMD5.getMd5(vai_str);
        
        Map<String, String> HeadPostParam = new HashMap<String, String>();
        HeadPostParam.put("merchantId", merchantId);
        HeadPostParam.put("appId", appId);
        HeadPostParam.put("appKey", appKey);
        HeadPostParam.put("userId", userId);
        HeadPostParam.put("userName", userName);
        HeadPostParam.put("mobile", mobile);
        HeadPostParam.put("cardNo", cardNo);
        HeadPostParam.put("backUrl", PropertiesUtil.get("local_url")+"/borrowUser/return_applyCollect");
        HeadPostParam.put("themeColor", themeColor);
        HeadPostParam.put("sign", md5_str);
        String PostString = HttpUtil.RequestForm(auth_url, HeadPostParam);
        log.info(" 出其右 返回参数 =" + PostString);
        Map<String, Object> ArrayDataString = JSONObject.fromObject(PostString);//将JSON转化为Map对象。
        return ArrayDataString;
    }

    /**
     * 运营商认证结果查询
     * @param userId
     * @param mobile
     * @return
     * @throws IOException
     */
    public Map<String, Object> AuthQuery(
            String userId,//用户id
            String mobile//手机号码
    ) throws IOException {
        //组装参数
        Map<String, String> headPostParam = new HashMap<>();
        headPostParam.put("merchantId", cqy_merchantId);
        headPostParam.put("appId", cqy_appId);
        headPostParam.put("appKey", cqy_appKey);
        headPostParam.put("userId", userId);
        headPostParam.put("mobile", mobile);
        headPostParam.put("sign", CreateMD5.getMd5(cqy_merchantId + cqy_appId + cqy_appKey + userId));
        log.info("无其右activeQuery请求参数=" + JsonUtils.beanToJson(headPostParam));
        String PostString = HttpUtil.RequestForm(cqy_auth_query_url, headPostParam);
        log.info("无其右activeQuery返回参数=" + PostString);
        //将JSON转化为Map对象。
        return (Map)JSONObject.fromObject(PostString);
    }

    /**
     * 获取运营商原始数据
     * @param mobile 手机号码
     * @return
     * @throws IOException
     */
    public Map<String, Object> queryOperatorOriginalData(String mobile) throws IOException {
        //组装参数
        Map<String, String> headPostParam = new HashMap<>();
        headPostParam.put("merchantId", cqy_merchantId);
        headPostParam.put("appId", cqy_appId);
        headPostParam.put("appKey", cqy_appKey);
        headPostParam.put("mobile", mobile);
        headPostParam.put("sign", CreateMD5.getMd5(cqy_merchantId + cqy_appId + cqy_appKey + mobile));
        log.info("无其右openGetBills请求参数=" + JsonUtils.beanToJson(headPostParam));
        String PostString = com.inext.utils.HttpUtil.doPostByJson(
                cqy_operator_original_query_url,
                JsonUtils.beanToJson(headPostParam)
        );
        log.info("无其右openGetBills返回参数=" + PostString);
        //将JSON转化为Map对象。
        return (Map)JSONObject.fromObject(PostString);
    }
    
    public static void main(String[] args) {
    	String md5_str = CreateMD5.getMd5("10001001RdaGu2ngPnAse6qN13503");
    	System.out.print(md5_str);
    	
	}

}
