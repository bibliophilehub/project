package com.inext.controller;

import com.alibaba.fastjson.JSONObject;
import com.inext.enumerate.Status;
import com.inext.scheduled.SchedulingConfig;
import com.inext.service.*;
import com.inext.service.dk.BaoFooDkService;
import com.inext.service.dk.RepaymentDKService;
import com.inext.service.kq.webservice.KqPaySevice;
import com.inext.tasks.CreditTask;
import com.inext.utils.JsonUtils;
import com.inext.utils.sms.SmsSendUtil;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Controller
@RequestMapping(value = "/test")
public class TestController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);
    @Resource
    private RepaymentDKService repaymentDKService;

    @Resource
    private KqPaySevice kqPaySevice;

    @Resource
    CreditService creditService;

    @Resource
    private IAssetBorrowOrderService assetBorrowOrderService;
    @Resource
    private BaoFooDkService baoFooDkService;

    @Autowired
    private SchedulingConfig schedulingConfig;
    @Autowired
    private CreditTask creditTask;

    @ApiOperation(value = "快钱代扣--测试")
    @ApiImplicitParams(value = {
    })
    @GetMapping("/daikou")
    public void overdue(HttpServletRequest request, HttpServletResponse response) {
        
    	kqPaySevice.advDebitBatch();
    	
    }
    
    @ApiOperation(value = "快钱查询--测试")
    @ApiImplicitParams(value = {
    })
    @GetMapping("/daikou_query")
    public void daikou_query(HttpServletRequest request, HttpServletResponse response) {
        
    	 kqPaySevice.pkiBatchQuery();
    	
    }


    @ApiOperation(value = "放款--测试")
    @ApiImplicitParams(value = {})
    @GetMapping("/updateLoanTerm")
    public void updateLoanTerm(HttpServletRequest request, HttpServletResponse response) throws Exception{
        schedulingConfig.updateLoanTerm();
    }

    @ApiOperation(value = "放款查询--测试")
    @ApiImplicitParams(value = {})
    @GetMapping("/queryPaysStateLianLian")
    public void queryPaysStateLianLian(HttpServletRequest request, HttpServletResponse response) throws Exception{
        schedulingConfig.queryPaysStateLianLian();
    }


    @ApiOperation(value = "逾期--测试")
    @ApiImplicitParams(value = {})
    @GetMapping("/overdue_query")
    public void overdue_query(HttpServletRequest request, HttpServletResponse response) throws Exception{
        schedulingConfig.overdue();
    }

    @ApiOperation(value = "风控--测试")
    @ApiImplicitParams(value = {})
    @GetMapping("/credit_query")
    public void credit_query(HttpServletRequest request, HttpServletResponse response) throws Exception{

        creditService.doWuqiyouOrderRisk();

    }

    @ApiOperation(value = "更新运营商认证过期用户状态--测试")
    @ApiImplicitParams(value = {})
    @GetMapping("/mobile_auth_update")
    public void mobile_auth_update(HttpServletRequest request, HttpServletResponse response) throws Exception{

        creditService.doMobileAuthUpdate();

    }


    @ApiOperation(value = "催收短信--测试")
    @ApiImplicitParams(value = {})
    @GetMapping("/sms_send")
    public void sms_send(HttpServletRequest request, HttpServletResponse response) throws Exception{
        SmsSendUtil.sendSmsDiyCLandYXByRepaymentRemind(
                "15026605283",
                "测试短信:尊敬的用户，您的10元借款明日到期，请至APP还款，若到期未还款，平台将自动扣款，请确保尾号1234银行卡资金充足。如已还款，请忽略。"
        );
    }

    @ApiOperation(value = "宝付代扣--测试")
    @ApiImplicitParams(value = {})
    @GetMapping("/baoFoo_dk")
    public void baoFoo_dk(HttpServletRequest request, HttpServletResponse response) throws Exception {
        baoFooDkService.doDebitBatch();
    }

    @ApiOperation(value = "用户黑/白名单--测试")
    @ApiImplicitParams(value = {})
    @GetMapping("/blackWhiteListOperate")
    public void userBlackWhiteListOperate(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //查询用户是否为黑/白名单
//        String s = creditService.getIsBlackWhiteUser("13712345678","测试", "1");
//        System.out.println("--查询--用户是否为黑/白名单---查询结果：" + s);
        //设置用户为黑/白名单
//        ApiServiceResult serviceResult = creditService.saveBlackWhiteUser("13712345678","测试","1","1", "0");
//        System.out.println("--设置--用户是否为黑/白名单---查询结果：" + serviceResult.getCode() + "," + serviceResult.getMsg());

        creditTask.doUserInWhiteList();
        creditTask.doUserInBlackList();
        creditTask.doUserOutBlackList();


    }
}
