//package com.inext.controller;
//
//import java.io.PrintWriter;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.inext.constants.Constants;
//import com.inext.entity.AssetBorrowOrder;
//import com.inext.entity.BackConfigParams;
//import com.inext.entity.OutOrders;
//import com.inext.service.IAssetBorrowOrderService;
//import com.inext.service.IOutOrdersService;
//import com.inext.utils.helipay.RSA;
//
//import net.sf.json.JSONObject;
//
///**
// * Created by wenkk on 2018/3/21 09：31. 还款订单
// */
//@Controller
//@RequestMapping("/pay")
//public class PayController extends BaseController {
//    Logger logger = LoggerFactory.getLogger(PayController.class);
//
//    public static final String split = "&";
//    
//    @Autowired
//    private IOutOrdersService iOutOrdersService;
//    
//    @Autowired
//    private IAssetBorrowOrderService assetBorrowOrderService;
//    
//    /**
//     * 合利宝代付结果回调
//     * @throws Exception 
//     */
//    @RequestMapping(value = "/helibao/daifu_notify")
//    public void daifu_notify(HttpServletRequest request, HttpServletResponse response){
//    	
//    	Map<String, Object> sPara = new HashMap<String, Object>();
//
//    	try {
//    		
//        	sPara.put("rt1_bizType", request.getParameter("rt1_bizType"));
//        	sPara.put("rt2_retCode", request.getParameter("rt2_retCode"));
//        	sPara.put("rt3_retMsg", request.getParameter("rt3_retMsg"));
//        	sPara.put("rt4_customerNumber", request.getParameter("rt4_customerNumber"));
//        	sPara.put("rt5_orderId", request.getParameter("rt5_orderId"));
//        	sPara.put("rt6_serialNumber", request.getParameter("rt6_serialNumber"));
//        	sPara.put("rt7_orderStatus", request.getParameter("rt7_orderStatus"));
//        	sPara.put("rt8_notifyType", request.getParameter("rt8_notifyType"));
//        	sPara.put("rt9_reason", request.getParameter("rt9_reason"));
//        	sPara.put("rt10_createDate", request.getParameter("rt10_createDate"));
//        	sPara.put("rt11_completeDate", request.getParameter("rt11_completeDate"));
//        	sPara.put("sign", request.getParameter("sign"));
//        	
//        	PrintWriter out = response.getWriter();
//      		
//            StringBuffer sb = new StringBuffer();
//            sb.append(split).append(sPara.get("rt1_bizType"));
//            sb.append(split).append(sPara.get("rt2_retCode"));
//            sb.append(split).append(sPara.get("rt3_retMsg"));
//            sb.append(split).append(sPara.get("rt4_customerNumber"));
//            sb.append(split).append(sPara.get("rt5_orderId"));
//            sb.append(split).append(sPara.get("rt6_serialNumber"));
//            sb.append(split).append(sPara.get("rt7_orderStatus"));
//            sb.append(split).append(sPara.get("rt8_notifyType"));
//            sb.append(split).append(sPara.get("rt9_reason"));
//            sb.append(split).append(sPara.get("rt10_createDate"));
//            sb.append(split).append(sPara.get("rt11_completeDate"));
//        	
//        	String local_sign = RSA.sign(sb.toString(), RSA.getPrivateKey(Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("DF_SIGNKEY_TRANSFER")));
//        	
//        	if(request.getParameter("sign").equals(local_sign)){
//        		
//        		logger.info("代付回调 : {}", sPara.toString());
//        		
//        		//查询对应的代付订单
//        		OutOrders orders = iOutOrdersService.findByOrderNo(sPara.get("rt4_customerNumber").toString());
//        		
//        		if(orders!=null && orders.getStatus().equals(OutOrders.STATUS_WAIT) && orders.getNotifyParams()==null){
//        			
//        			orders.setUpdateTime(new Date());
//        			orders.setNotifyParams(JSONObject.fromObject(sPara).toString());
//        			orders.setNotifyTime(new Date());
//        			if(sPara.get("rt7_orderStatus").equals("SUCCESS")){
//        				orders.setStatus(OutOrders.STATUS_SUC);
//        			}else{
//        				orders.setStatus(OutOrders.STATUS_OTHER);
//        			}
//        			
//        			iOutOrdersService.update(orders);
//        			
//        			//更新订单信息
//        			if(sPara.get("rt7_orderStatus").equals("SUCCESS")){
//        			  AssetBorrowOrder assetBorrowOrder = assetBorrowOrderService.getOrderById(orders.getAssetOrderId());
//                      assetBorrowOrder.setStatus(AssetBorrowOrder.STATUS_FKCG);
//                      assetBorrowOrder.setPayStatus(AssetBorrowOrder.SUB_PAY_SUCC);
//                      assetBorrowOrder.setUpdateTime(new Date());
//                      
//                      assetBorrowOrderService.updateByPrimaryKeySelective(assetBorrowOrder);
//        			}
//        		}
//        	}
//        	
//        	out.print("success");    		
//			
//		} catch (Exception e) {
//			
//			OutOrders orders = iOutOrdersService.findByOrderNo(sPara.get("rt4_customerNumber").toString());
//			
//			if(orders!=null && orders.getStatus().equals(OutOrders.STATUS_WAIT) && orders.getNotifyParams()==null){
//				
//    			orders.setUpdateTime(new Date());
//    			orders.setNotifyParams(JSONObject.fromObject(sPara).toString());
//    			orders.setNotifyTime(new Date());
//    			orders.setStatus(OutOrders.STATUS_OTHER);
//    			iOutOrdersService.update(orders);
//			}
//		}
//    	
//
//    }
//
//}
