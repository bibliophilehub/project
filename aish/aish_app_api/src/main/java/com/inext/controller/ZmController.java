package com.inext.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inext.constants.SmsContentConstant;
import com.inext.entity.BorrowUser;
import com.inext.entity.ZmScoreLog;
import com.inext.entity.zm.SesameData;
import com.inext.result.ApiGxbResult;
import com.inext.service.IBorrowUserService;
import com.inext.service.IZmScoreLogService;
import com.inext.service.IZmService;
import com.inext.utils.sms.SmsSendUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * APP用户控制器
 *
 * @author ttj
 */

@RestController
@RequestMapping(value = "/zm")
public class ZmController extends BaseController {
	Logger logger = LoggerFactory.getLogger(ZmController.class);
	@Resource
	IZmService zmService;
	@Resource
	IBorrowUserService borrowUserService;
	@Resource
	private IZmScoreLogService zmScoreLogService;

	/*@ApiOperation(value = "芝麻授权认证.isAuth:是否授权(1:已授权  0：未授权);authUrl:授权链接(未授权时返回	)"
			+ "alredayAuth=1说明该用户已在第三方平台上通过其它App授权(其它情况不返回该字段)")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token", value = "用户授权码", required = true, dataType = "String", paramType = "header")
	})
	@PostMapping(value = "/creditreport/zm-mobile-api")
	public ApiResult<ZhimaResult> creditReportZm(HttpServletRequest request, String isNew) throws Exception {
		BorrowUser bu = this.getLoginUser(request.getHeader("token"));
		return new ApiResult(zmService.creditReportZm(isNew, bu.getId()));
	}
*/


	@ApiOperation(value = "芝麻授权认证.isAuth:是否授权(1:已授权  0：未授权);authUrl:授权链接(未授权时返回	)"
			+ "alredayAuth=1说明该用户已在第三方平台上通过其它App授权(其它情况不返回该字段)")
	@ApiImplicitParam(name = "token", value = "用户授权码", required = true, dataType = "String", paramType = "header")
	@PostMapping(value = "/creditreport/zm-mobile-api")
	public ApiGxbResult creditsReportZm(HttpServletRequest request, String isNew) throws Exception {
		BorrowUser bu = this.getLoginUser(request.getHeader("token"));
		return new ApiGxbResult(zmService.creditReportGxbZm(isNew, bu.getId()));
	}

	/**
	 * 芝麻回调
	 */
	@PostMapping(value = "/callback")
	@ResponseBody
	public JSONObject gxbCallBack(@RequestBody JSONObject jsonObject) {
		logger.info("芝麻多渠道回调-callback:" + JSON.toJSONString(jsonObject));

		JSONObject result = new JSONObject();
		result.put("retCode", 2);

		if (jsonObject == null) {
			result.put("retMsg", "没有返回用户信息");
			return result;
		}
		if (jsonObject.get("idcard")==null) {
			result.put("retMsg", "身份证信息不能为空");
			return result;
		}
		String idCard = jsonObject.get("idcard").toString();
		BorrowUser user = borrowUserService.getBorrowUserByCardNumber(idCard);
		if (user == null) {
			result.put("retMsg", "芝麻回调根据身份证信息[" + idCard + "]查询不到用户.......");
			return result;
		}


		if(jsonObject.get("authJson")==null || "".equals(jsonObject.get("authJson").toString())){
			result.put("retMsg", "返回sesameData:["+JSON.toJSONString(jsonObject.get("authJson"))+"]芝麻信息有问题");
			return result;
		}
		SesameData sesameData = JSONObject.parseObject(jsonObject.get("authJson").toString(),SesameData.class);
		if(sesameData.getSesameScore()==null){
			result.put("retMsg", "返回sesameScore:["+JSON.toJSONString(sesameData)+"]芝麻分有问题");
			return result;
		}

		Integer userId = Integer.valueOf(user.getId());
		BorrowUser updateUser = new BorrowUser();
		updateUser.setId(userId);
		updateUser.setIsZhima(1);
		updateUser.setZhimaScore(sesameData.getSesameScore().toString());
		updateUser.setZmAuthTime(new Date());
		borrowUserService.updateUser(updateUser);
		ZmScoreLog zmScoreLog = new ZmScoreLog();
		zmScoreLog.setCreateTime(new Date());
		zmScoreLog.setUserId(user.getId());
		zmScoreLog.setZmScore(sesameData.getSesameScore().toString());
		zmScoreLog.setZmContent(JSON.toJSONString(jsonObject));
		zmScoreLogService.saveZmScoreLog(zmScoreLog);
		result.put("retMsg", "成功");
		result.put("retCode", 1);
		return result;
	}

	@GetMapping(value = "/returnurl")
	public ModelAndView gxbStatusBack(@RequestParam String success, @RequestParam(required = false) String remark) {
		logger.info("芝麻-returnurl success:{},remark:{}" ,success,remark);
		ModelAndView model = new ModelAndView();
		JSONObject result = new JSONObject();
		result.put("retCode", 2);

		if ("1".equals(success)) {
			model.setViewName("zm/zmSuccess");
			return model;
		}
		if ("0".equals(success) && "推送失败".equals(remark)) {
			model.setViewName("zm/zmSuccess");
		}
		logger.info("芝麻-returnurl success:调用失败" );
		model.setViewName("zm/zmSuccess");
		return model;
	}

	@GetMapping(value = "/zmSuccess")
	public ModelAndView zmSuccess(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("zm/zmSuccess");
		return mav;
	}


	/**
	 * 短信测试
	 * @param request
	 * @param response
	 * @param gapDay
	 */
	@PostMapping(value = "/test/sms")
	public  void sentSMS(HttpServletRequest request, HttpServletResponse response,Integer gapDay,String phone) {
		String result;
		String content = "";
		String card = "6214852117390891";
		String userName="李四";
		String order="17854656144511451";
		String repaymentAmount="100";
		String lateDay="2";
		card = card.substring(card.length()-4,card.length());
		if (gapDay == 1) {
			content = SmsContentConstant.getTomorrowOverdue(
					userName,repaymentAmount,card);
		} else if (gapDay == 0) {
			content = SmsContentConstant.getTodayOverdue(
					userName,repaymentAmount,card);
		} else if (gapDay == -1) {
			content = SmsContentConstant.getOverdue(
					userName,repaymentAmount,lateDay);
		}
		if(gapDay==1 || gapDay==0)
		{
			result = SmsSendUtil.sendSmsDiyCLandCollection("13918611242", content);//催收类
		}else if(gapDay==-1){
			result = SmsSendUtil.sendSmsDiyCLandYXByRepaymentRemind("13918611242", content);//通知类
		}
	}
}
