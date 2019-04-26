package com.inext.utils.helibao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.inext.entity.heli.BindCardResponseVo;
import com.inext.utils.helibao.request.AgreementBindCardValidateCodeVo;
import com.inext.utils.helibao.request.BindCardVo;
import com.inext.utils.helibao.request.CancelCardVo;
import com.inext.utils.helibao.request.QueryOrderVo;
import com.inext.utils.helibao.response.AgreementSendValidateCodeResponseVo;
import com.inext.utils.helibao.response.CancelCardResponseVo;
import com.inext.utils.helibao.response.QueryOrderResponseVo;

/**
 * Created by dhl on 2017/6/19.
 */
@Component
public class HelibaoAuthUtil {

	/**
	 * 发送银行卡短信验证码
     * 由第三方发送
	 * @param ds_mchntcd_p
	 * @param user_id_p
	 * @param trans_id_p
	 * @param trade_date_p
	 * @param acc_no_p
	 * @param id_card_p
	 * @param id_holder_p
	 * @param mobile_p
	 * @param bind_url
	 * @return
	 */
    public Map<String, Object> sendSmsAuth(
    		String ds_mchntcd_p,  //商户编号
    		String user_id_p,  //商户用户id
            String trans_id_p,//商户订单号
            String trade_date_p,//订单交易时间日期 yyyyMMddHHmmss
            String acc_no_p,//银行卡号码
            String id_card_p,//身份证
            String id_holder_p,// 真实姓名
            String mobile_p, //预留手机号
            String bind_url
    ){
    	Map<String, Object> retMap = new HashMap<String, Object>();
    	try{
	    	
	    	AgreementBindCardValidateCodeVo vo = new AgreementBindCardValidateCodeVo();
	    	vo.setP1_bizType("AgreementPayBindCardValidateCode");
	    	vo.setP2_customerNumber(ds_mchntcd_p);
	    	vo.setP3_userId(user_id_p);
	    	vo.setP4_orderId(trans_id_p);
	    	vo.setP5_timestamp(trade_date_p);
	    	vo.setP6_cardNo(acc_no_p);
	    	vo.setP7_phone(mobile_p);
	    	vo.setP8_idCardNo(id_card_p);
	    	vo.setP9_idCardType("IDCARD");
	    	vo.setP10_payerName(id_holder_p);
	    	vo.setSignatureType("MD5WITHRSA");
	    	
	    	Map reqestMap = MessageHandle.getReqestMap(vo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, bind_url);
	    	
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				retMap.put("code", "01");
				retMap.put("message", "请求失败");
				return retMap;
			}
	
			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				retMap.put("code", "01");
				retMap.put("message", resultMsg);
				return retMap;
			}
	
			AgreementSendValidateCodeResponseVo responseVo = JSONObject.parseObject(resultMsg, AgreementSendValidateCodeResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				retMap.put("code", "01");
				retMap.put("message", "验签失败");
				return retMap;
			}
	
			if (!"0000".equals(responseVo.getRt2_retCode())) {
				retMap.put("code", "01");
			} else {
				retMap.put("code", "00");
			}
			retMap.put("message", responseVo.getRt3_retMsg());
			return retMap;

		} catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", "请求异常：" + e.getMessage());
		}
    	
    	return retMap;
    }

    /**
     * 鉴权绑卡
     * @param trans_id_p
     * @param trade_date_p
     * @param acc_no_p
     * @param id_card_p
     * @param id_holder_p
     * @param mobile_p
     * @param card_type_p
     * @param type_p
     * @param trade_no_x_p
     * @param sms_code_p
     * @return
     * @throws IOException
     */
    public Map<String, Object> Auth(
    		String ds_mchntcd_p,  //商户编号
    		String user_id_p,  //商户用户id
            String trans_id_p,//商户订单号
            String trade_date_p,//订单交易时间日期 yyyyMMddHHmmss
            String acc_no_p,//银行卡号码
            String id_card_p,//身份证
            String id_holder_p,// 真实姓名
            String mobile_p, //预留手机号
            String bind_url, //url
            String sms_code_p//短信验证码
    ) {
    	
    	Map<String, Object> retMap = new HashMap<String, Object>();
    	
    	try {
    	
			BindCardVo vo = new BindCardVo();
	    	vo.setP1_bizType("QuickPayBindCard");
	    	vo.setP2_customerNumber(ds_mchntcd_p);
	    	vo.setP3_userId(user_id_p);
	    	vo.setP4_orderId(trans_id_p);
	    	vo.setP5_timestamp(trade_date_p);
	    	vo.setP6_payerName(id_holder_p);
	    	vo.setP7_idCardType("IDCARD");
	    	vo.setP8_idCardNo(id_card_p);
	    	vo.setP9_cardNo(acc_no_p);
	    	vo.setP13_phone(mobile_p);
	    	vo.setP14_validateCode(sms_code_p);
	    	vo.setP15_isEncrypt("true");
	    	vo.setSignatureType("MD5WITHRSA");
//	    	vo.setUserAccount(mobile_p);
			Map reqestMap = MessageHandle.getReqestMap(vo);
	
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, bind_url);
		
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				retMap.put("code",  "01");
				retMap.put("message", "请求失败");
				return retMap;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				retMap.put("code", "01");
				retMap.put("message", resultMsg);
				return retMap;
			}

			BindCardResponseVo responseVo = JSONObject.parseObject(resultMsg, BindCardResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				retMap.put("code", "01");
				retMap.put("message", "验签失败");
				return retMap;
			}
			
			if (!"0000".equals(responseVo.getRt2_retCode())) {
				retMap.put("code", "01");
			} else {
				if("SUCCESS".equals(responseVo.getRt7_bindStatus())){
					retMap.put("code", "00");
					retMap.put("bindStatus", "SUCCESS");
					retMap.put("bindId", responseVo.getRt10_bindId());
					retMap.put("serialNumber", responseVo.getRt11_serialNumber());
				}else{
					retMap.put("code", "01");
					retMap.put("bindStatus", responseVo.getRt7_bindStatus());
					retMap.put("bindId", responseVo.getRt10_bindId());
					retMap.put("serialNumber", responseVo.getRt11_serialNumber());
				}
			}
			retMap.put("message", responseVo.getRt3_retMsg());
			return retMap;
			
		} catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", "请求异常：" + e.getMessage());
		}
		return retMap;
 
    }
    
	/**
	 * 查询用户卡订单
	 * @param ds_mchntcd_p
	 * @param user_id_p
	 * @param trade_date_p
	 * @param bind_url
	 * @return
	 */
	public Map<String, Object> QueryAuth(
			String ds_mchntcd_p,  //商户编号
			String user_id_p,  //商户用户id
	        String trade_date_p,//订单交易时间日期 yyyyMMddHHmmss
	        String bind_url //url
	        ) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		try {
			
			QueryOrderVo vo = new QueryOrderVo();
	    	vo.setP1_bizType("BankCardbindList");
	    	vo.setP2_customerNumber(ds_mchntcd_p);
	    	vo.setP3_userId(user_id_p);
	    	vo.setP5_timestamp(trade_date_p);
	    	vo.setSignatureType("MD5WITHRSA");			
			
			Map reqestMap = MessageHandle.getReqestMap(vo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, bind_url);
	
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				retMap.put("code", "01");
				retMap.put("message", "请求失败");
				return retMap;
			}
	
			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				retMap.put("code", "01");
				retMap.put("message", resultMsg);
				return retMap;
			}
	
			QueryOrderResponseVo responseVo = JSONObject.parseObject(resultMsg, QueryOrderResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				retMap.put("code", "01");
				retMap.put("message", "验签失败");
				return retMap;
			}
	
			retMap.put("code", "00");
			retMap.put("message", "查询成功，bankid=");
			return retMap;
			
		} catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", "请求异常：" + e.getMessage());
		}
		return retMap;
	
	}
    
	/**
	 * 查询用户卡订单
	 * @param ds_mchntcd_p
	 * @param user_id_p
	 * @param trade_date_p
	 * @param bind_url
	 * @return
	 */
	public Map<String, Object> CancelAuth(
			String ds_mchntcd_p,  //商户编号
			String user_id_p,  //商户用户id
	        String trade_date_p,//订单交易时间日期 yyyyMMddHHmmss
	        String trans_id_p, //订单号
	        String bind_id_p,  //绑定id
	        String bind_url //url
	        ) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		try {
			
			CancelCardVo vo = new CancelCardVo();
	    	vo.setP1_bizType("BankCardUnbind");
	    	vo.setP2_customerNumber(ds_mchntcd_p);
	    	vo.setP3_userId(user_id_p);
	    	vo.setP4_bindId(bind_id_p);
	    	vo.setP5_orderId(trans_id_p);
	    	vo.setP6_timestamp(trade_date_p);
	    	vo.setSignatureType("MD5WITHRSA");
			
			Map reqestMap = MessageHandle.getReqestMap(vo);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, bind_url);
	
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				retMap.put("code", "01");
				retMap.put("message", "请求失败");
				return retMap;
			}
	
			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				retMap.put("code", "01");
				retMap.put("message", resultMsg);
				return retMap;
			}
	
			CancelCardResponseVo responseVo = JSONObject.parseObject(resultMsg, CancelCardResponseVo.class);
			if (!MessageHandle.checkSign(responseVo)) {
				retMap.put("code", "01");
				retMap.put("message", "验签失败");
				return retMap;
			}
	
			retMap.put("code", "00");
			retMap.put("message", "查询成功，bankid=");
			return retMap;
			
		} catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", "请求异常：" + e.getMessage());
		}
		return retMap;
	
	}
    
	public static boolean isJSON(String test) {
		try {
			JSONObject.parseObject(test);
		} catch (JSONException ex) {
			return false;
		}
		return true;
	}
}
