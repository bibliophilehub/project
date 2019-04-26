package com.inext.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inext.constants.Constants;
import com.inext.utils.baofoo.PropertiesUtil;
import com.inext.utils.baofoo.util.HttpUtil;
import com.inext.utils.newbaofoo.rsa.RsaCodingUtil;
import com.inext.utils.newbaofoo.rsa.SecurityUtil;
import com.inext.utils.newbaofoo.rsa.SignatureUtils;
import com.inext.utils.newbaofoo.util.FormatUtil;
import com.inext.view.result.MapResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inext.constants.RedisCacheConstants;
import com.inext.dao.IBankAllInfoDao;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BankAllInfo;
import com.inext.entity.BorrowUser;
import com.inext.entity.UserAuthRecord;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiResult;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBaoFooPayService;
import com.inext.service.IBorrowUserService;
import com.inext.service.UserAuthRecordService;
import com.inext.utils.RedisUtil;
import com.inext.utils.baofoo.XinyanAuthUtil;
import com.inext.utils.helibao.HelibaoAuthUtil;
import com.inext.view.result.CardBinResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/baoFooBindCard")
public class BaoFooBindCardController extends BaseController {
    Logger logger = Logger.getLogger(getClass());
    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    @Resource
    IBorrowUserService borrowUserService;
    @Resource
    IAssetBorrowOrderService assetBorrowOrderService;
    @Autowired
    private IBaoFooPayService baoFooPayService;
    @Autowired
    private UserAuthRecordService userAuthRecordService;
    
    @Autowired
    private IBankAllInfoDao bankAllInfoDao;

    @Autowired
    XinyanAuthUtil xinyanAuthUtil;
    
    @Autowired
    HelibaoAuthUtil helibaoAuthUtil;
    
    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @ApiOperation(value = "绑卡第一步 发送绑卡验证码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "预留手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bank_id", value = "银行", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "card_no", value = "银行卡号", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "sendBindVerifyCode")
    public ApiResult<MapResult> getLianLianToken(HttpServletRequest request) throws Exception {
    	
    	Map<String, String> pams = this.getParameters(request);
    	
		BorrowUser borrowUser = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN));
    			
//		BorrowUser borrowUser = borrowUserService.getBorrowUserById(126531);
		
		BorrowUser user = borrowUserService.getBorrowUserById(borrowUser.getId());

		if ((StringUtils.isBlank(user.getUserName())) || (user.getIsVerified().intValue() != 1)) {
			return new ApiResult(ApiStatus.FAIL.getCode(), "请完成实名认证");
		}

		String phone = pams.get("phone") == null ? "" : pams.get("phone");
		String bankId = pams.get("bank_id") == null ? "" : pams.get("bank_id");
		String cardNo = pams.get("card_no") == null ? "" : pams.get("card_no");

		if (StringUtils.isBlank(phone)) {
			return new ApiResult(ApiStatus.FAIL.getCode(), "预留手机号码不能为空");
		}
		if (StringUtils.isBlank(bankId)) {
			return new ApiResult(ApiStatus.FAIL.getCode(), "银行不能为空");
		}
		if (StringUtils.isBlank(cardNo)) {
			return new ApiResult(ApiStatus.FAIL.getCode(), "银行卡号不能为空");
		}
		
		
		/**------------新颜验证卡------------------------*/
        //com.alibaba.fastjson.JSONObject jsonObject = xinyanAuthUtil.cardbin(cardNo);
		com.alibaba.fastjson.JSONObject jsonObject = xinyanAuthUtil.cardbin_v1(cardNo);

		if (!jsonObject.getBoolean("success")) {
			return new ApiResult<>(ApiStatus.FAIL.getCode(), "暂不识别此卡，请换张银行卡绑定");
		}

		com.alibaba.fastjson.JSONObject data = jsonObject.getJSONObject("data");
		// 不允许使用信用卡绑卡
		if (!"1".equals(data.getString("card_type"))) {
			return new ApiResult<>(ApiStatus.FAIL.getCode(), "不支持信用卡绑定，请更换银行卡");
		}

		/** ----------------合利宝绑卡--------------- */
		/*String trans_id_p = "HXX_" + user.getId() + "_" + System.currentTimeMillis(); // 商户订单号
		String trade_date_p = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 订单日期

		//合利宝商户号
        Map<String, String> helibaoMap = backConfigParamsService.getBackConfig(BackConfigParams.HELIPAY, null);
        String ds_mchntcd = helibaoMap.get("DS_MCHNTCD");
//        String ds_mchntcd = "C1800000002";
        String hl_bind_url = helibaoMap.get("HL_BIND_URL");


		Map<String, Object> sms_reMap = helibaoAuthUtil.sendSmsAuth(
				ds_mchntcd, String.valueOf(user.getId()), trans_id_p, trade_date_p, cardNo, user.getUserCardNo(),
				user.getUserName(), phone, hl_bind_url);*/

        /*----------------------宝付绑卡--------------------------*/
        String bankName = data.getString("bank");//改用v1版本-接口取值-2019-01-10
        BankAllInfo bankAllInfo = bankAllInfoDao.findBankAllInfoByName("%" + bankName + "%");
        if (StringUtils.isBlank(bankAllInfo.getId().toString())) {
            logger.info("本地库未找到该银行卡:" + bankName);
            return new ApiResult<>(ApiStatus.FAIL.getCode(), "暂不识别此卡，请换张银行卡绑定");
        }
        String trans_id_p="HXX_"+ user.getId() + "_" +System.currentTimeMillis();
        //宝付绑卡发送短信验证码
        Map<String, Object> sms_reMap = readBindSendSms(
                trans_id_p,
                cardNo,user.getUserCardNo(),
                user.getUserName(),
                phone
        );
		if(sms_reMap!=null){
			
			UserAuthRecord auth_record = new UserAuthRecord();
			auth_record.setUser_phone(phone);
			auth_record.setUser_name(user.getUserName());
			auth_record.setUser_id_no(user.getUserCardNo());
			auth_record.setUser_card_no(cardNo);
			auth_record.setUser_id(user.getId());
			auth_record.setTerminal_no(trans_id_p);
			auth_record.setC_name(2);  
			auth_record.setCreate_time(new Date());
			auth_record.setReturn_params(String.valueOf(sms_reMap.get("message")));
			auth_record.setAuth_code(String.valueOf(sms_reMap.get("code")));
			auth_record.setIs_fee("N");
			auth_record.setUser_card_id(Integer.valueOf(pams.get("bank_id")));
			
			userAuthRecordService.saveRecord(auth_record);
			
			if(String.valueOf(sms_reMap.get("code")).equals("00")){
//				return new ApiResult(ApiStatus.SUCCESS.getCode(), "验证码发送成功");
                Map<String, Object> uniqueCode = new HashMap<>();
                uniqueCode.put("uniqueCode",sms_reMap.get("uniqueCode"));
                return new ApiResult<>(new MapResult(uniqueCode));
			}else{
				return new ApiResult(ApiStatus.FAIL.getCode(), String.valueOf(sms_reMap.get("message")));
			}
		}
		
		return new ApiResult(ApiStatus.FAIL.getCode(), "验证码发送失败");
    	
    }

    @ApiOperation(value = "重新绑卡时，校验是否有借款订单")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
    })
    @PostMapping(value = "validBorrowOrder")
    public ApiResult validBorrowOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
        Integer userId = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN)).getId();
//        Integer checkResult = assetBorrowOrderService.checkBorrow(userId, null);
//        if (1 == checkResult) {
//            return new ApiResult(ApiStatus.FAIL.getCode(), "您有借款申请正在审核或未还款完成，暂不能修改银行卡");
//        }
//        logger.error("validBorrowOrder userId=" + userId + " checkResult=" + checkResult);

        return new ApiResult();
    }

    @ApiOperation(value = "绑卡第二步 提交绑卡验证")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "预留手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bank_id", value = "银行", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "card_no", value = "银行卡号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verify_code", value = "短信验证码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "uniqueCode", value = "绑卡唯一码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "bindCard")
    public ApiResult tokenValidate(HttpServletRequest request) throws Exception {
    	
        Integer userId = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN)).getId();
        
//        BorrowUser borrowUser = borrowUserService.getBorrowUserById(126531);
//        Integer userId = borrowUser.getId();
        
        BorrowUser user = borrowUserService.getBorrowUserById(userId);

    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("userId", user.getId());
    	params.put("orgCode", "00");
    	
    	List<UserAuthRecord> authlist = userAuthRecordService.getRecordByUserId(params);
    	
    	if(authlist.size()>0){
            /*-----------------------------合利宝-----确认绑卡------------------------------*/
            Map<String, String> helibaoMap = backConfigParamsService.getBackConfig(BackConfigParams.HELIPAY, null);
//            String ds_mchntcd = helibaoMap.get("DS_MCHNTCD");
//            String ds_mchntcd = "C1800000002";
//            String hl_bind_url = helibaoMap.get("HL_BIND_URL");

            Map<String, String> request_params = this.getParameters(request);

            String verify_code = request_params.get("verify_code") == null ? "" : request_params.get("verify_code");
            String uniqueCode = request_params.get("uniqueCode") == null ? "" : request_params.get("uniqueCode");

//    		String trade_date_p = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 订单日期
//
//    		Map<String, Object> auth_reMap = helibaoAuthUtil.Auth(ds_mchntcd,String.valueOf(authlist.get(0).getUser_id()),
//    				authlist.get(0).getTerminal_no(), trade_date_p, authlist.get(0).getUser_card_no(),
//    				authlist.get(0).getUser_id_no(), authlist.get(0).getUser_name(), authlist.get(0).getUser_phone(),
//    				hl_bind_url, verify_code);

            /*----------------------------宝付------确认绑卡-----------------------------*/
            Map<String, Object> auth_reMap = confirmBind(uniqueCode, verify_code, String.valueOf(user.getId()));
            if(auth_reMap!=null){
                if(auth_reMap.get("code").equals("00")){
                    user.setCardType(authlist.get(0).getUser_card_id());
                    user.setCardCode(authlist.get(0).getUser_card_no());
                    user.setCardPhone(authlist.get(0).getUser_phone());
                    user.setIsCard(1);
                    user.setCardTime(new Date());
                    user.setIsYop(0);
                    user.setBindId(auth_reMap.get("bindId").toString());
                    borrowUserService.updateUser(user);

                    UserAuthRecord a_new_record = new UserAuthRecord();
                    a_new_record.setId(authlist.get(0).getId());
                    a_new_record.setTrade_no(String.valueOf(auth_reMap.get("serialNumber")));
                    a_new_record.setReturn_params(
                            authlist.get(0).getReturn_params()
                                    +"||"
                                    +String.valueOf(auth_reMap.get("bindStatus"))
                                    + "&&"
                                    + String.valueOf(auth_reMap.get("bindId")
                            )
                    );
                    userAuthRecordService.updateRecord(a_new_record);

                    return new ApiResult(ApiStatus.SUCCESS.getCode(), "绑卡成功");
    			}else{
    				return new ApiResult(ApiStatus.FAIL.getCode(), String.valueOf(auth_reMap.get("message")));
    			}
    		}else{
    			return new ApiResult(ApiStatus.FAIL.getCode(), "绑卡失败");
    		}
    		
    	}else{
    		return new ApiResult(ApiStatus.FAIL.getCode(), "绑卡失败");
    	}
    }
    
    @ApiOperation(value = "绑卡第一步 新颜卡bin 识别银行卡所属银行")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "bankCard", value = "银行卡号", dataType = "String", paramType = "query"),
    })
    
    @PostMapping("cardBin")
    public ApiResult<CardBinResult> cardBin(HttpServletRequest request)throws IOException {

        logger.info("########################进入新颜cardBin###############");
        String bankCard = request.getParameter("bankCard");
        //com.alibaba.fastjson.JSONObject jsonObject = xinyanAuthUtil.cardbin(bankCard);
        com.alibaba.fastjson.JSONObject jsonObject = xinyanAuthUtil.cardbin_v1(bankCard);//改用v1版本-2019-01-10

        if (!jsonObject.getBoolean("success")) {
            logger.info("新颜银行卡绑定失败" + jsonObject.getString("errorMsg"));
            return new ApiResult<>(ApiStatus.FAIL.getCode(), "暂不识别此卡，请换张银行卡绑定");
        }

        com.alibaba.fastjson.JSONObject data = jsonObject.getJSONObject("data");
        // 不允许使用信用卡绑卡
        if(!"1".equals(data.getString("card_type"))){
            return new ApiResult<>(ApiStatus.FAIL.getCode(), "不支持信用卡绑定，请更换银行卡");
        }

        //String bankName = data.getString("bank_description");
        String bankName = data.getString("bank");//改用v1版本-接口取值-2019-01-10

        bankName = "%" + bankName + "%";
        BankAllInfo bankAllInfo = bankAllInfoDao.findBankAllInfoByName(bankName);
        if (StringUtils.isBlank(bankAllInfo.getId().toString())) {
            logger.info("本地库未找到该银行卡:" + bankName);
            return new ApiResult<>(ApiStatus.FAIL.getCode(), "暂不识别此卡，请换张银行卡绑定");
        }
        // 2018-08-24 16:05:00 功能改动 取消所有绑卡限制
        // if (bankAllInfo.getId().equals(2)||bankAllInfo.getId().equals(9)||bankAllInfo.getId().equals(10)) {
        //     logger.info("暂不支持绑定此银行，请更换其他银行的借记卡:" + bankName);
        //     return new ApiResult<>(ApiStatus.FAIL.getCode(), "暂不支持绑定此银行，请更换其他银行的借记卡");
        // }
        String bankId = bankAllInfo.getId().toString();
        bankName = bankAllInfo.getBankName();

        return new ApiResult<>(new CardBinResult(bankId, bankName));
    }

    @ExceptionHandler
    public ApiResult exceptionHandler(Exception e) {
        logger.error("新颜接口异常[" + this.getClass().getName() + "]异常", e);
        return new ApiResult(ApiStatus.ERROR.getCode(), e.getMessage());
    }
    
    @ApiOperation(value = "查询绑卡id")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
    })
    @PostMapping(value = "queryBankId")
    public ApiResult queryBankId(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, String> helibaoMap = backConfigParamsService.getBackConfig(BackConfigParams.HELIPAY, null);
//      String ds_mchntcd = helibaoMap.get("DS_MCHNTCD");
        String ds_mchntcd = "C1800596977";
        String hl_bind_url = helibaoMap.get("HL_BIND_URL");
        
        BorrowUser borrowUser = borrowUserService.getBorrowUserById(126531);
        Integer userId = borrowUser.getId();
        
        BorrowUser user = borrowUserService.getBorrowUserById(userId);
        
		String trade_date_p = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 订单日期

		Map<String, Object> query_reMap = helibaoAuthUtil.QueryAuth(ds_mchntcd, String.valueOf(userId), trade_date_p, 
				hl_bind_url);
		
		if(query_reMap!=null){
			if(query_reMap.get("code").equals("00")){
				return new ApiResult(ApiStatus.SUCCESS.getCode(), String.valueOf(query_reMap.get("message")));
			}
        
        return new ApiResult();
		}
		return null;
    }
    
    @ApiOperation(value = "解绑银行卡id")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "bindId", value = "绑定id", dataType = "String", paramType = "header"),
    })
    @PostMapping(value = "cancelBank")
    public ApiResult cancelBank(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Integer userId = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN)).getId();
        //BorrowUser borrowUser = borrowUserService.getBorrowUserById(126531);
        //Integer userId = borrowUser.getId();
        BorrowUser user = borrowUserService.getBorrowUserById(userId);

        Map<String, String> pams = this.getParameters(request);
        /*---------------------------合利宝-----解绑-------*/
        /*Map<String, String> helibaoMap = backConfigParamsService.getBackConfig(BackConfigParams.HELIPAY, null);
//      String ds_mchntcd = helibaoMap.get("DS_MCHNTCD");
        String ds_mchntcd = "C1800596977";
        String hl_bind_url = helibaoMap.get("HL_BIND_URL");
		String trans_id_p = "YZB_" + user.getId() + "_" + System.currentTimeMillis(); // 商户订单号
		String trade_date_p = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 订单日期



		Map<String, Object> query_reMap = helibaoAuthUtil.CancelAuth(ds_mchntcd, String.valueOf(userId), trade_date_p,
				trans_id_p, pams.get("bindId"), hl_bind_url);*/

        /*--------------------宝付----------解绑银行卡---------------------------*/
        Map<String, Object> query_reMap = aBolishBind(user.getBindId(), user.getId().toString());
        if(query_reMap!=null){
            if(query_reMap.get("code").equals("00")){
                return new ApiResult(ApiStatus.SUCCESS.getCode(), String.valueOf(query_reMap.get("message")));
            }

            return new ApiResult();
        }
        return null;
    }


    /**
     * 宝付绑卡发送短信验证码
     * @param acc_no//银行卡卡号
     * @param id_card//身份证号码
     * @param id_holder//姓名
     * @param mobile//银行预留手机号
     * @return
     * @throws Exception
     */
    private Map<String, Object> readBindSendSms(
            String transId,
            String acc_no,
            String id_card,
            String id_holder,
            String mobile
    ) throws Exception{
        Map<String, Object> sms_reMap = null;

        String send_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//报文发送日期时间
        String cerpath =PropertiesUtil.get("baofoo.rzzf.pub.key");;//商户私钥
        String pfxpath = PropertiesUtil.get("baofoo.rzzf.pri.key");;//宝付公钥

        //商户自定义（可随机生成  商户自定义(AES key长度为=16位)）
        String AesKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_AES_KEY");
        //使用接收方的公钥加密后的对称密钥，并做Base64转码，明文01|对称密钥，01代表AES[密码商户自定义]
        String dgtl_envlp = "01|"+AesKey;
        logger.info("-----宝付绑卡发送短信验证码------密码dgtl_envlp："+dgtl_envlp);
        dgtl_envlp = RsaCodingUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), cerpath);//公钥加密

//        String Cardinfo = "6212262309006279753|何豪雨|310115199007129776|15823781632||";
        // 账户信息[银行卡号|持卡人姓名|证件号|手机号|银行卡安全码|银行卡有效期]
        String Cardinfo =acc_no + "|" + id_holder + "|" + id_card + "|" +mobile + "||";
        logger.info("-----宝付绑卡发送短信验证码------SHA-1摘要[Cardinfo]结果："+SecurityUtil.sha1X16(Cardinfo, "UTF-8"));

        logger.info("-----宝付绑卡发送短信验证码------卡信息："+Cardinfo+",长度："+Cardinfo.length());
        Cardinfo = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(Cardinfo), AesKey);//先BASE64后进行AES加密
        logger.info("-----宝付绑卡发送短信验证码------AES结果:"+Cardinfo);
        Map<String,String> DateArry = new TreeMap<String,String>();
        DateArry.put("send_time", send_time);
        DateArry.put("msg_id", transId);//报文流水号
        DateArry.put("version", PropertiesUtil.get("baofoo.rzzf.version"));
        DateArry.put("terminal_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_TERM_ID"));
        DateArry.put("txn_type", "01");//交易类型
        DateArry.put("member_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_MER_ID"));
        DateArry.put("dgtl_envlp", dgtl_envlp);
        //DateArry.put("user_id", "T117300110134");//用户在商户平台唯一ID
        DateArry.put("card_type", "101");//卡类型  101	借记卡，102 信用卡
        DateArry.put("id_card_type", "01");//证件类型
        DateArry.put("acc_info",Cardinfo);

        String SignVStr = FormatUtil.coverMap2String(DateArry);
//        logger.info("SHA-1摘要字串："+SignVStr);
        String signature = SecurityUtil.sha1X16(SignVStr, "UTF-8");//签名
//        logger.info("SHA-1摘要结果："+signature);
        String Sign = SignatureUtils.encryptByRSA(
                signature,
                pfxpath,
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_PRI_KEY_PASS")
        );
//        logger.info("RSA签名结果："+Sign);
        DateArry.put("signature", Sign);//签名域

        String PostString  = HttpUtil.RequestForm(
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_URL"),
                DateArry
        );
        logger.info("-----宝付绑卡发送短信验证码------请求返回:"+PostString);

        Map<String, String> ReturnData = FormatUtil.getParm(PostString);

        if(!ReturnData.containsKey("signature")){
            throw new Exception("缺少验签参数！");
        }

        String RSign=ReturnData.get("signature");
//        logger.info("返回的验签值："+RSign);
        ReturnData.remove("signature");//需要删除签名字段
        String RSignVStr = FormatUtil.coverMap2String(ReturnData);
        logger.info("-----宝付绑卡发送短信验证码------返回SHA-1摘要字串："+RSignVStr);
        String RSignature = SecurityUtil.sha1X16(RSignVStr, "UTF-8");//签名
//        logger.info("返回SHA-1摘要结果："+RSignature);

        if(SignatureUtils.verifySignature(cerpath,RSignature,RSign)){
            logger.info("Yes");//验签成功
        }

        if(!ReturnData.containsKey("resp_code")){
            throw new Exception("缺少resp_code参数！");
        }

        if(StringUtils.equals("S",ReturnData.get("resp_code"))){
            if(!ReturnData.containsKey("dgtl_envlp")){
                throw new Exception("缺少dgtl_envlp参数！");
            }
            String RDgtlEnvlp = SecurityUtil.Base64Decode(
                    RsaCodingUtil.decryptByPriPfxFile(
                            ReturnData.get("dgtl_envlp"),
                            pfxpath,
                            Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_PRI_KEY_PASS")
                    )
            );
//            logger.info("返回数字信封："+RDgtlEnvlp);
            String RAesKey=FormatUtil.getAesKey(RDgtlEnvlp);//获取返回的AESkey
//            logger.info("返回的AESkey:"+RAesKey);
            String uniqueCode = SecurityUtil.Base64Decode(SecurityUtil.AesDecrypt(ReturnData.get("unique_code"),RAesKey));
            logger.info("-----宝付绑卡发送短信验证码------唯一码:" + uniqueCode);
            if(StringUtils.equals("0000",ReturnData.get("biz_resp_code"))){
                sms_reMap = new HashMap<>();
                sms_reMap.put("code","00");
                sms_reMap.put("uniqueCode",uniqueCode);
                sms_reMap.put("message", PostString);
            }
        }else if(StringUtils.equals("F",ReturnData.get("resp_code"))){
            sms_reMap = new HashMap<>();
            sms_reMap.put("code","01");
            sms_reMap.put("message", PostString);
        }else {
            sms_reMap = new HashMap<>();
            sms_reMap.put("code", ReturnData.get("resp_code"));
            sms_reMap.put("message", PostString);
        }
        return sms_reMap;
    }

    /**
     * baofoo确认绑卡（协议支付）
     * @throws Exception
     */
    private Map<String, Object> confirmBind(String uniqueCode, String SMSStr, String userId) throws Exception {
        Map<String, Object> sms_reMap = null;
        String send_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//报文发送日期时间
        String cerpath =PropertiesUtil.get("baofoo.rzzf.pub.key");//商户私钥
        String pfxpath = PropertiesUtil.get("baofoo.rzzf.pri.key");//宝付公钥

        //SMSStr="123456";//短信验证码，测试环境随机6位数;生产环境验证码预绑卡成功后发到用户手机。确认绑卡时回传。
        //商户自定义（可随机生成  商户自定义(AES key长度为=16位)）
        String AesKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_AES_KEY");
        //使用接收方的公钥加密后的对称密钥，并做Base64转码，明文01|对称密钥，01代表AES[密码商户自定义]
        String dgtl_envlp = "01|" + AesKey;

        //logger.info("密码dgtl_envlp："+dgtl_envlp);
        dgtl_envlp = RsaCodingUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), cerpath);//公钥加密
        //预签约唯一码(预绑卡返回的值)[格式：预签约唯一码|短信验证码]
        uniqueCode = uniqueCode + "|" + SMSStr;
        //logger.info("预签约唯一码："+UniqueCode);
        uniqueCode = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(uniqueCode), AesKey);//先BASE64后进行AES加密
        //logger.info("AES结果:"+UniqueCode);

        Map<String,String> DateArry = new TreeMap<String,String>();
        DateArry.put("send_time", send_time);
        DateArry.put("msg_id", "HXX_TISN_" + userId +"_"+ System.currentTimeMillis());//报文流水号
        DateArry.put("version", PropertiesUtil.get("baofoo.rzzf.version"));
        DateArry.put("terminal_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_TERM_ID"));
        DateArry.put("txn_type", "02");//交易类型
        DateArry.put("member_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_MER_ID"));
        DateArry.put("dgtl_envlp", dgtl_envlp);
        DateArry.put("unique_code", uniqueCode);//预签约唯一码

        String SignVStr = FormatUtil.coverMap2String(DateArry);
        logger.info("----------宝付确认绑卡----------SHA-1摘要字串："+SignVStr);
        String signature = SecurityUtil.sha1X16(SignVStr, "UTF-8");//签名
        logger.info("----------宝付确认绑卡----------SHA-1摘要结果："+signature);
        String Sign = SignatureUtils.encryptByRSA(
                signature,
                pfxpath,
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_PRI_KEY_PASS")
        );
        logger.info("----------宝付确认绑卡----------RSA签名结果："+Sign);
        DateArry.put("signature", Sign);//签名域

        String PostString  = HttpUtil.RequestForm(
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_URL"),
                DateArry
        );
        logger.info("----------宝付确认绑卡----------请求返回:"+PostString);

        Map<String, String> ReturnData = FormatUtil.getParm(PostString);

        if(!ReturnData.containsKey("signature")){
            throw new Exception("缺少验签参数！");
        }

        String RSign=ReturnData.get("signature");
        logger.info("----------宝付确认绑卡----------返回的验签值："+RSign);
        ReturnData.remove("signature");//需要删除签名字段
        String RSignVStr = FormatUtil.coverMap2String(ReturnData);
        logger.info("----------宝付确认绑卡----------返回SHA-1摘要字串："+RSignVStr);
        String RSignature = SecurityUtil.sha1X16(RSignVStr, "UTF-8");//签名
        logger.info("----------宝付确认绑卡----------返回SHA-1摘要结果："+RSignature);

        if(SignatureUtils.verifySignature(cerpath,RSignature,RSign)){
            logger.info("Yes");//验签成功
        }
        if(!ReturnData.containsKey("resp_code")){
            throw new Exception("缺少resp_code参数！");
        }
        if(StringUtils.equals("S", ReturnData.get("resp_code"))){
            if(!ReturnData.containsKey("dgtl_envlp")){
                throw new Exception("缺少dgtl_envlp参数！");
            }
            String RDgtlEnvlp = SecurityUtil.Base64Decode(
                    RsaCodingUtil.decryptByPriPfxFile(
                            ReturnData.get("dgtl_envlp"),
                            pfxpath,
                            Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_PRI_KEY_PASS")
                    )
            );
            logger.info("----------宝付确认绑卡----------返回数字信封："+RDgtlEnvlp);
            String RAesKey=FormatUtil.getAesKey(RDgtlEnvlp);//获取返回的AESkey
            logger.info("----------宝付确认绑卡----------返回的AESkey:"+RAesKey);
            String protocol_no=SecurityUtil.Base64Decode(SecurityUtil.AesDecrypt(ReturnData.get("protocol_no"),RAesKey));
            logger.info("----------宝付确认绑卡----------签约协议号:" + protocol_no);
            if(StringUtils.equals("0000",ReturnData.get("biz_resp_code"))){
                sms_reMap = new HashMap<>();
                sms_reMap.put("code","00");
                sms_reMap.put("bindStatus", ReturnData.get("biz_resp_code"));
                sms_reMap.put("serialNumber", ReturnData.get("msg_id"));
                sms_reMap.put("bindId", protocol_no);
                sms_reMap.put("message", PostString);
            }
        }else if(StringUtils.equals("I", ReturnData.get("resp_code"))){
            logger.info("----------宝付确认绑卡----------处理中！");
        }else if(StringUtils.equals("F", ReturnData.get("resp_code"))){
            logger.info("----------宝付确认绑卡----------失败！");
        }else{
            throw new Exception("反回异常！");//异常不得做为订单状态。
        }
        return sms_reMap;
    }

    /**
     * 宝付解绑银行卡
     * @param bindId
     * @param userId
     * @return
     * @throws Exception
     */
    private Map<String, Object> aBolishBind(String bindId, String userId) throws Exception {
        Map<String, Object> sms_reMap = null;
        if(StringUtils.isEmpty(bindId)){
            return  sms_reMap;
        }
        String send_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//报文发送日期时间
        String cerpath =PropertiesUtil.get("baofoo.rzzf.pub.key");//商户私钥
        String pfxpath = PropertiesUtil.get("baofoo.rzzf.pri.key");//宝付公钥

        String AesKey = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_AES_KEY");//商户自定义(可随机生成  AES key长度为=16位)
        String dgtl_envlp = "01|"+AesKey;//使用接收方的公钥加密后的对称密钥，并做Base64转码，明文01|对称密钥，01代表AES[密码商户自定义]
        logger.info("----------宝付解绑银行卡----------密码dgtl_envlp："+dgtl_envlp);
        dgtl_envlp = RsaCodingUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(dgtl_envlp), cerpath);//公钥加密
        String ProtocolNo = bindId;//签约协议号（确认绑卡返回）
        logger.info("----------宝付解绑银行卡----------签约协议号："+ProtocolNo);
        ProtocolNo = SecurityUtil.AesEncrypt(SecurityUtil.Base64Encode(ProtocolNo), AesKey);//先BASE64后进行AES加密
        logger.info("----------宝付解绑银行卡----------签约协议号AES结果:"+ProtocolNo);

        Map<String,String> DateArry = new TreeMap<>();
        DateArry.put("send_time", send_time);
        DateArry.put("msg_id", "HXX_TISN_" + userId + "_" + System.currentTimeMillis());//报文流水号
        DateArry.put("version", PropertiesUtil.get("baofoo.rzzf.version"));
        DateArry.put("terminal_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_TERM_ID"));
        DateArry.put("txn_type", "04");//交易类型
        DateArry.put("member_id", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_MER_ID"));
        DateArry.put("dgtl_envlp", dgtl_envlp);
        //DateArry.put("user_id", bindId);//用户在商户平台唯一ID (和绑卡时要一致)
        DateArry.put("protocol_no", ProtocolNo);//签约协议号（密文）

        String SignVStr = FormatUtil.coverMap2String(DateArry);
        logger.info("----------宝付解绑银行卡----------SHA-1摘要字串："+SignVStr);
        String signature = SecurityUtil.sha1X16(SignVStr, "UTF-8");//签名
        logger.info("----------宝付解绑银行卡----------SHA-1摘要结果："+signature);
        String Sign = SignatureUtils.encryptByRSA(
                signature,
                pfxpath,
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_PRI_KEY_PASS")
        );
        logger.info("----------宝付解绑银行卡----------RSA签名结果："+Sign);
        DateArry.put("signature", Sign);//签名域

        String PostString  = HttpUtil.RequestForm(
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.BAOFOO).get("XYZF_URL"),
                DateArry
        );
        logger.info("----------宝付解绑银行卡----------请求返回:"+PostString);

        Map<String, String> ReturnData = FormatUtil.getParm(PostString);

        if(!ReturnData.containsKey("signature")){
            throw new Exception("缺少验签参数！");
        }

        String RSign=ReturnData.get("signature");
        logger.info("----------宝付解绑银行卡----------返回的验签值："+RSign);
        ReturnData.remove("signature");//需要删除签名字段
        String RSignVStr = FormatUtil.coverMap2String(ReturnData);
        logger.info("----------宝付解绑银行卡----------返回SHA-1摘要字串："+RSignVStr);
        String RSignature = SecurityUtil.sha1X16(RSignVStr, "UTF-8");//签名
        logger.info("----------宝付解绑银行卡----------返回SHA-1摘要结果："+RSignature);

        if(SignatureUtils.verifySignature(cerpath,RSignature,RSign)){
            logger.info("----------宝付解绑银行卡----------Yes");//验签成功
        }
        if(!ReturnData.containsKey("resp_code")){
            throw new Exception("缺少resp_code参数！");
        }
        if(StringUtils.equals("S",ReturnData.get("resp_code"))){
            logger.info("----------宝付解绑银行卡----------解绑成功！");
            sms_reMap = new HashMap<>();
            sms_reMap.put("code","00");
            sms_reMap.put("message", "解绑成功");
        }else if(StringUtils.equals("F",ReturnData.get("resp_code"))){
            logger.info("----------宝付解绑银行卡----------解绑失败！");
        }
        return sms_reMap;
    }

}