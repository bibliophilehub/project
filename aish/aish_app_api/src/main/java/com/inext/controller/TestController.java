//package com.inext.controller;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.inext.entity.AssetOrderStatusHis;
//import com.inext.entity.AssetRepaymentOrder;
//import com.inext.entity.RepaymentStatistics;
//import com.inext.service.IRepaymentStatisticsService;
//import com.inext.utils.DateUtil;
//import com.inext.utils.DateUtils;
//
///**
// * APP用户控制器
// *
// * @author ttj
// */
//
//@RestController
//@RequestMapping(value = "/zltest")
//public class TestController extends BaseController {
//    Logger logger = LoggerFactory.getLogger(TestController.class);
//    //	@Value("${operUrl}")
////    private String operUrl;
//    @Resource
//    IRepaymentStatisticsService statisticsService;
//
//    @GetMapping(value = "/group")
//    @ResponseBody
//    public void group(HttpServletRequest request, HttpServletResponse response) {
//    	System.out.println("开始时间：" + new Date());
//    	List<AssetRepaymentOrder> details =  statisticsService.getRepayStatistics(null);
//    	System.out.println("查询后时间：" + new Date());
//    	System.out.println("查询后details的个数" + details.size());
//    	Map<String, List<AssetRepaymentOrder>> detailmap = details.stream()
//    			.collect(Collectors.groupingBy(d -> fetchGroupKey(d) ));
//    	System.out.println("排列后时间：" + new Date());
//    	System.out.println(new Date());
//    	
//    	String data_query = "2018-11-09";
//    	
//    	List<AssetRepaymentOrder> data_list = detailmap.get(data_query);
//    	
//    	int loan_all_count = data_list.size(); //放款总单数
//    	int shoujie = 0;  //首借
//    	int fujie = 0; //复借
//    	int history_xuqi = 0; //历史续期到期数
//    	int xuqi = 0;  //续期单数
//    	int anshi_huankuan = 0;  //按时还款单数
//    	int yuqi_danshu = 0;   //逾期总单数
//    	int yuqi_yihuan = 0;	//逾期已还单数
//    	int daihuan_danshu = 0;   //当前待还单数
//    	
//    	int shouri_yuqishu = 0;   //首日逾期数
//    	String shouri_yuqilv;   //首日逾期率
//    	
//    	int day7_yuqishu = 0;   //7日逾期数
//    	String day7_yuqilv;		//7日逾期率
//    	String now_huikuanlv_xuqi;  //当前回款率(含续期)
//    	
//    	int shoujie_xudanshu = 0;   //首借续单数
//    	int shoujie_huankuanshu = 0;  //首借还款数
//    	String shoujie_huikuanlv_xuqi;   //首借回款率(含续期
//    	
//    	int fujie_xudanshu = 0;   //复借续单数
//    	int fujie_huankuanshu = 0;  //复借还款数
//    	String fujie_huikuanlv_xuqi;    //复借回款率(含续期
//    	
//    	String history_huikuanlv_xuqi;   //历史续期回款率(含续期
//    	
//    	for (int i = 0; i < data_list.size(); i++) {
//    		
//    		AssetRepaymentOrder rep = data_list.get(i);
//    		
//    		//首借 复借
//    		if(rep.getIsOld()==0){
//    			shoujie = shoujie + 1;
//    			
//    			//首借续单数
//        		if(DateUtils.compareDay(DateUtils.addDay(rep.getCreditRepaymentTime(), 6), 
//        				rep.getRepaymentTime()) < 0){
//        			
//        			shoujie_xudanshu = shoujie_xudanshu + 1;
//        		}    
//        		
//        		//首借还款数
//        		if(rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
//        			shoujie_huankuanshu = shoujie_huankuanshu + 1;
//        		}
//        		
//    		}else{
//    			
//    			fujie = fujie + 1;
//    			
//    			//复借续单数
//        		if(DateUtils.compareDay(DateUtils.addDay(rep.getCreditRepaymentTime(), 6), 
//        				rep.getRepaymentTime()) < 0){
//        			
//        			fujie_xudanshu = fujie_xudanshu + 1;
//        		}
//        		
//        		//复借还款数
//        		if(rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
//        			fujie_huankuanshu = fujie_huankuanshu + 1;
//        		}
//        		
//        		
//        		
//    		}
//    		
//    		//历史续期到期单数
//    		if(DateUtils.compareDay(DateUtils.addDay(rep.getCreditRepaymentTime(), 6), 
//    				rep.getRepaymentTime()) < 0 && rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
//    			
//    			history_xuqi = history_xuqi + 1;
//    		}
//    		
//    		
//    		//续期单数
//    		if(DateUtils.compareDay(DateUtils.addDay(rep.getCreditRepaymentTime(), 6), 
//    				rep.getRepaymentTime()) < 0){
//    			
//    			xuqi = xuqi + 1;
//    		}    		
//
//    		//按时还款单数
//    		if(DateUtils.compareDay(rep.getRepaymentTime(), rep.getRepaymentRealTime()) >= 0
//    				&& rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
//    			
//    			anshi_huankuan = anshi_huankuan + 1;
//    		}
//    		
//    		//逾期总单数
//    		if(DateUtils.compareDay(rep.getRepaymentTime(), rep.getRepaymentRealTime()) < 0 
//    				|| rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YYQ)){
//    			
//    			yuqi_danshu = yuqi_danshu + 1;
//    		}
//    		
//    		//逾期已还单数
//    		if(DateUtils.compareDay(rep.getRepaymentTime(), rep.getRepaymentRealTime()) < 0 
//    				&& rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
//    			
//    			yuqi_yihuan = yuqi_yihuan + 1;
//    		}
//
//    		//当前待还单数
//    		if(rep.getRepaymentRealTime()==null){
//    			daihuan_danshu =  daihuan_danshu + 1;
//    		}
//    		
//    		//首逾数
//    		if(rep.getLateDay()>0){
//    			shouri_yuqishu = shouri_yuqishu + 1;
//    		}
//    		
//		}
//    	
//    	//首日逾期率
//    	shouri_yuqilv = String.format("%.2f", (double)shouri_yuqishu/loan_all_count*100);
//    	
//    	//7日逾期数
//    	day7_yuqishu = yuqi_danshu - yuqi_yihuan;
//    	day7_yuqilv = String.format("%.2f", (double)day7_yuqishu/loan_all_count*100);
//    	
//    	//当前回款率(含续期)
//    	now_huikuanlv_xuqi = String.format("%.2f", (double)(xuqi + anshi_huankuan)/loan_all_count*100);
//    	
//    	//首借回款率(含续期
//    	shoujie_huikuanlv_xuqi = String.format("%.2f", (double)(shoujie_xudanshu+shoujie_huankuanshu)/shoujie*100);
//
//    	//复借回款率(含续期
//    	fujie_huikuanlv_xuqi = String.format("%.2f", (double)(fujie_xudanshu+fujie_huankuanshu)/fujie*100);
//	
//    }
//    
//    
////    /**
////     * 计算
////     * @param repayment
////     * @return
////     */
////    private RepaymentStatistics computerRepayment(AssetRepaymentOrder repayment){
////
////    	
////       	//判断是否老用户
////    	if(repayment.getIsOld()==1){
////    		
////    		if(AssetOrderStatusHis.STATUS_YHK){
////    			
////    		}
////    		
////    		
////    	}
////    	
////    	
////    	
////		return null;
////    	
////    }
////    
////    
////    /**
////     * 计算公式
////     * @param repayment
////     * @param type
////     * @return
////     */
////    private Map<String, Object> countRepaymentDetail(AssetRepaymentOrder repay, int type){
////    	int all_daoqi = 0;  //全部用户  到期单数
////    	int all_zhengchang_huankuan = 0;   //全部用户 正常还款数
////    	int all_xuqi = 0;   //全部用户 续期单数
////    	int all_yijing_huankuan = 0;  //全部用户 已还款数
////    	int all_yuqi = 0;   //全部用户 逾期数
////    	int all_shouyulv = 0;  //全部用户 首逾率
////    	int all_huankuanlv = 0;  //全部用户 还款率
////    	
////    	int old_daoqi = 0;   //老用户 到期单数
////    	int old_shouyu = 0;   //老用户 首逾率
////    	int old_yuqi = 0;   //老用户 逾期单数
////    	int old_huankuan = 0;  //老用户 还款数
////    	
////    	int new_daoqi = 0;   //新用户 到期单数
////    	int new_shouyu = 0;   //新用户 首逾率
////    	int new_yuqi = 0;   //新用户 逾期单数
////    	int new_huankuan = 0;  //新用户 还款数    	
////    	
////    	switch (type) {
////    	
////		case 1:  //全部用户
////			if(repay.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)){
////				
////			}
////			
////			
////			
////			
////			break;
////		case 2:  //老用户
////			
////			break;
////		case 3:   //新用户
////			
////			break;			
////
////		default:
////			break;
////		}
////    	
////    	
////    	
////		return null;
////    	
////    	
////    }
//    
//    
//    
//    /**
//     * 按还款日合计
//     * @param detail
//     * @return
//     */
//	private String fetchGroupKey(AssetRepaymentOrder detail){
//	        return detail.getStatisticsLoanTime();
//	}
//	
//	
//	
//}
