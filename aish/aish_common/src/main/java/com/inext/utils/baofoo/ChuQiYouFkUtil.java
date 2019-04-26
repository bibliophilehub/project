package com.inext.utils.baofoo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inext.entity.chuqiyou.ChuqiyouFk_p;
import com.inext.utils.HttpUtil;
import com.inext.utils.chuqiyou.CreateMD5;
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
public class ChuQiYouFkUtil {

    protected static String cqy_merchantId;
    protected static String cqy_appId;
    protected static String cqy_appKey;
    protected static String cqy_fk_url;


    private static Logger log = LoggerFactory.getLogger(ChuQiYouFkUtil.class);

    static {

        //
    	cqy_merchantId = PropertiesUtil.get("cqy_merchantId");//商户号
    	cqy_appId = PropertiesUtil.get("cqy_appId");
    	cqy_appKey = PropertiesUtil.get("cqy_appKey");

    	cqy_fk_url = PropertiesUtil.get("cqy_fkurl");


    }
    
    /**
     * 
     * @param fk_p
     * @return
     * @throws IOException
     */
    public Map<String, Object> doFk(ChuqiyouFk_p fk_p) throws IOException {
    	
        String merchantId = cqy_merchantId;
        String appId = cqy_appId;
        String appKey = cqy_appKey;
        String fk_url = cqy_fk_url;
        
        if(fk_p!=null){
            //待加密字符串
            String vai_str = merchantId+appId+appKey+fk_p.getOrderId();
            
            //加密字符串
            String md5_str = CreateMD5.getMd5(vai_str);
            
            Map<String, Object> HeadPostParam = new HashMap<String, Object>();
            HeadPostParam.put("merchantId", merchantId);
            HeadPostParam.put("appId", appId);
            HeadPostParam.put("appKey", appKey);
            HeadPostParam.put("orderId", fk_p.getOrderId());
            HeadPostParam.put("userName", fk_p.getUserName());
            HeadPostParam.put("cardNum", fk_p.getCardNum());
            HeadPostParam.put("mobile", fk_p.getMobile());
            HeadPostParam.put("riskData", fk_p.getRiskData());
            HeadPostParam.put("sign", md5_str);
            
            String PostString = HttpUtil.doPostByJson(fk_url, JSONObject.toJSONString(HeadPostParam));
            log.info(" 无其右风控返回参数 =" + PostString+", 返回订单号="+fk_p.getOrderId());
            Map<String, Object> ArrayDataString = JSONObject.parseObject(PostString, new TypeReference<Map<String, Object>>(){});//将JSON转化为Map对象。
            return ArrayDataString;
        }else{
        	return null;
        }
        

    }
    
    
    public static void main(String[] args) {
    	String md5_str = CreateMD5.getMd5("10001001RdaGu2ngPnAse6qN1111");
    	System.out.print(md5_str);
    	
	}

}
