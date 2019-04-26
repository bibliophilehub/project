package com.inext.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.inext.constants.SmsContentConstant;
import com.inext.dao.*;
import com.inext.entity.*;
import com.inext.enums.GnhResultEnum;
import com.inext.utils.partner.D360.Base64Utils;
import com.inext.utils.partner.D360.D360Client;
import com.inext.utils.partner.GnhRSAUtil;
import com.inext.utils.partner.GnhSortUtils;
import com.inext.utils.sms.SmsSendUtil;
import com.stylefeng.guns.modular.system.model.BizAssetBorrowOrder;
import com.stylefeng.guns.modular.system.model.BorrowUserAllInfoDto;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.inext.constants.Constants;
import com.inext.constants.RedisCacheConstants;
import com.inext.enumerate.ApiStatus;
import com.inext.exception.CheckException;
import com.inext.result.ApiServiceResult;
import com.inext.result.ServiceResult;
import com.inext.service.IAppAutoLoginLogService;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBannerInfoService;
import com.inext.service.IBorrowUserService;
import com.inext.service.IChannelService;
import com.inext.service.IJxlService;
import com.inext.service.IRiskCreditUserService;
import com.inext.utils.FileUtil;
import com.inext.utils.MD5Utils;
import com.inext.utils.OkHttpUtils;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import com.inext.utils.TokenUtils;
import com.inext.utils.encrypt.AESUtil;
import com.inext.utils.face.FaceUtils;
import com.inext.view.params.ApplyCollectParams;
import com.inext.view.result.JxlCollectResult;

@Service("borrowUserService")
@Transactional(rollbackFor = Exception.class)
public class BorrowUserServiceImpl implements IBorrowUserService {

    Logger logger = LoggerFactory.getLogger(BorrowUserServiceImpl.class);
    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    @Resource
    ISysCodeDao sysCodeDao;
    @Resource
    IBannerInfoService bannerInfoService;
    @Autowired
    IAppAutoLoginLogService appAutoLoginLogService;
    @Autowired
    IJxlService jxlService;
    @Autowired
    private IBorrowUserDao userDao;
    @Autowired
    private IEquipmentDao equipmentDao;
    @Autowired
    private IAssetOrderStatusHisDao assetOrderStatusHisDao;

    @Autowired
    private IChannelService channelService;

    @Autowired
    IBankAllInfoDao bankAllInfoDao;
    @Autowired
    IRiskCreditUserService iRiskCreditUserService;

    @Autowired
    FaceUtils faceUtils;

    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @Resource
    private IChannelService iChannelService;

    @Autowired
    private IAssetBorrowOrderDao iAssetBorrowOrderDao;

    @Autowired
    IAssetRepaymentOrderDao iAssetRepaymentOrderDao;

    @Resource
    IAssetRenewalOrderDao iAssetRenewalOrderDao;

    @Override
    public void loginOne(JSONObject json, HttpServletRequest request) {
        String userPhone = request.getParameter("userPhone");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", userPhone);
        BorrowUser bu = userDao.getBorrowUserByPhone(map);
        JSONObject js = new JSONObject();
        json.put("code", "0");

        if (bu != null) {

            // 黑名单用户不允许登录
            if ("1".equals(bu.getIsBlack() + "")) {

                json.put("code", "-1");
                json.put("message", "帐号异常，请联系客服");

                js.put("userPhone", userPhone);
                js.put("code", "-1");
                json.put("result", js);
                return;
            }

            json.put("code", "0");
            json.put("msg", "手机号码存在，进行登录操作");
            js.put("userPhone", userPhone);
            //是否设置密码：0-未设置，1-已设置
            if(StringUtils.isNotEmpty(bu.getUserPassword())){
                js.put("isSetPass", "1");
            }else{
                js.put("isSetPass", "0");
            }
            js.put("code", "1");
            json.put("result", js);
        } else {
            json.put("message", "手机号码不存在，进行注册操作");
            js.put("userPhone", userPhone);
            js.put("code", "0");
            json.put("result", js);
        }
    }

    @Override
    public void login(JSONObject json, HttpServletRequest request) {
        try {
            BorrowUser bu = checkLogin(json, request);
            loginEdit(json, bu);
        } catch (Exception e) {
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }
    }

    /**
     * 短信验证码登陆
     * @param json
     * @param request
     */
    @Override
    public void smsLogin(JSONObject json, HttpServletRequest request) {
        try {
            //校验
            BorrowUser bu = checkSmsLogin(json, request);
            //登录处理
            loginEdit(json, bu);
        } catch (Exception e) {
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }
    }

    /**
     * 短信验证码登陆-校验
     * @param json
     * @param request
     * @return
     * @throws Exception
     */
    private BorrowUser checkSmsLogin(JSONObject json, HttpServletRequest request) throws Exception {
        String userPhone = request.getParameter("userPhone");
        if (userPhone == null || "".equals(userPhone)) {
            throw new Exception("请输入手机号码！");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", userPhone);
        BorrowUser bu = userDao.getBorrowUserByPhone(map);
        if (bu == null) {
            throw new Exception("手机号码不存在！");
        }
        String smsCode = request.getParameter("smsCode");
        if (StringUtils.isEmpty(smsCode)) {
            throw new Exception("请输入短信验证码！");
        }
        String key = RedisCacheConstants.SMS_CODE_VERIFICATION_LOGIN_PREFIX + userPhone;
        String oldSmsCode = StringUtils.getString(redisUtil.get(key));
        if (StringUtils.isEmpty(oldSmsCode)) {
            throw new Exception("短信验证码已失效！");
        }
        if (!smsCode.equals(oldSmsCode)) {
            throw new Exception("验证码错误！");
        }
        // 黑名单用户不允许登录
        if ("1".equals(bu.getIsBlack())) {
            throw new Exception("帐号异常，请联系客服");
        }
        return bu;
    }

    private BorrowUser checkLogin(JSONObject json, HttpServletRequest request) throws Exception {
        String userPhone = request.getParameter("userPhone");
        if (userPhone == null || "".equals(userPhone)) {
            throw new Exception("请输入手机号码！");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", userPhone);
        BorrowUser bu = userDao.getBorrowUserByPhone(map);
        if (bu == null) {
            throw new Exception("手机号码不存在！");
        }
        String userPassword = request.getParameter("userPassword");
        if (userPassword == null || "".equals(userPassword)) {
            throw new Exception("请输入密码！");
        }
        userPassword = MD5Utils.MD5Encode(userPassword);
        if (!userPassword.equals(bu.getUserPassword())) {
            throw new Exception("密码不正确！");
        }

        // 黑名单用户不允许登录
        if ("1".equals(bu.getIsBlack())) {
            throw new Exception("帐号异常，请联系客服");
        }


        return bu;
    }

    private void loginEdit(JSONObject json, BorrowUser bu) {
        // 生成自动登录的有效token
        ApiServiceResult apiServiceResult = appAutoLoginLogService.generateToken(bu);

        String key = RedisCacheConstants.TOKENCODE + bu.getUserAccount();
        String token = TokenUtils.generateToken(bu);
        redisUtil.set(key, token, RedisCacheConstants.TOKENTIME);
        String tempToken = apiServiceResult.getExt().toString();
        json.put("code", "0");
        if (json.get("message") == null) {
            json.put("message", "登录成功！");
        }
        JSONObject js = new JSONObject();
        js.put("token", tempToken);
        js.put("userPhone", bu.getUserPhone());
        json.put("result", js);
    }

    @Override
    public void registered(JSONObject json, HttpServletRequest request) {
        try {
            checkRegistered(json, request);
            BorrowUser bu = registeredEdit(json, request);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", bu.getUserPhone());
            BorrowUser buu = userDao.getBorrowUserByPhone(map);
            json.put("message", "注册成功！");
            this.loginEdit(json, buu);
//            String success = SmsContentConstant.getRegisterSuccess(bu.getUserPhone());
//            SmsSendUtil.sendSmsDiyCLandYX(bu.getUserPhone(),success);
        } catch (Exception e) {
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }

    }

    private BorrowUser registeredEdit(JSONObject json, HttpServletRequest request) {
        String userPhone = request.getParameter("userPhone");
        String userPassword = request.getParameter("userPassword");
        userPassword = MD5Utils.MD5Encode(userPassword);
        BorrowUser bu = new BorrowUser();
        bu.setUserAccount(userPhone);
        bu.setUserPhone(userPhone);
        bu.setStatus(0);
        bu.setUserPassword(userPassword);
        String channelCode = request.getParameter("channelCode");
        ChannelInfo channelInfo = iChannelService.getChannelByCode( channelCode );
        Integer channelId = 0;
        if( channelInfo == null ){
            logger.warn( "channel is empty channelCode:{}" , channelCode );
        }else{
            channelId = channelInfo.getId();
        }
        bu.setChannelId(channelId);
        /** 去掉芝麻认证 */
        bu.setIsZhima(1);
        bu.setZhimaScore("2");
        userDao.saveBorrowUser(bu);
        return bu;
    }

    private void checkRegistered(JSONObject json, HttpServletRequest request) throws Exception {
        String userPhone = request.getParameter("userPhone");
        if (userPhone == null || "".equals(userPhone)) {
            throw new Exception("请输入手机号码！");
        }
        String userPassword = request.getParameter("userPassword");
        if (userPassword == null || "".equals(userPassword)) {
            throw new Exception("请输入密码！");
        }
        String smsCode = request.getParameter("smsCode");
        if (smsCode == null || "".equals(smsCode)) {
            throw new Exception("请输入验证码！");
        }
        String key = RedisCacheConstants.SMS_CODE_VERIFICATION_LOGIN_PREFIX + userPhone;
        String oldSmsCode = StringUtils.getString(redisUtil.get(key));
        if (StringUtils.isEmpty(oldSmsCode)) {
            throw new Exception("短信验证码已失效！");
        }
        if (!smsCode.equals(oldSmsCode)) {
            throw new Exception("验证码错误！");
        }
        if(NumberUtils.isNumber(userPassword) || userPassword.matches("^[A-Za-z]+$")){
            throw new Exception("请输入数字加字母组合的密码！");
        }
    }

    @Override
    public void getBackOne(JSONObject json, HttpServletRequest request) {
        try {
            checkGetBack(request);
            String userPhone = request.getParameter("userPhone");
            json.put("code", "0");
            json.put("message", "校验通过！");
            JSONObject js = new JSONObject();
            js.put("userPhone", userPhone);
            json.put("result", js);
        } catch (Exception e) {
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }

    }

    private void checkGetBack(HttpServletRequest request) throws Exception {
        String userPhone = request.getParameter("userPhone");
        if (userPhone == null || "".equals(userPhone)) {
            throw new Exception("请输入手机号码！");
        }
        String smsCode = request.getParameter("smsCode");
        if (smsCode == null || "".equals(smsCode)) {
            throw new Exception("请输入验证码！");
        }
        String key = RedisCacheConstants.SMS_CODE_VERIFICATION_LOGIN_PREFIX + userPhone;
        Object obj = redisUtil.get(key);
        if (obj == null) {
            throw new Exception("短信验证码已失效！");
        }
        String oldSmsCode = StringUtils.getString(obj);
        if (!smsCode.equals(oldSmsCode)) {
            throw new Exception("短信验证码错误！");
        }
    }

    @Override
    public void getBackTwo(JSONObject json, HttpServletRequest request) {
        try {
            checkBackTwo(request);
            String userPhone = request.getParameter("userPhone");
            String userPassword = request.getParameter("userPassword");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", userPhone);
            BorrowUser bu = userDao.getBorrowUserByPhone(map);
            userPassword = MD5Utils.MD5Encode(userPassword);
            bu.setUserPassword(userPassword);
            userDao.updateBorrowUser(bu);
            json.put("code", "0");
            json.put("message", "修改成功！");
            JSONObject js = new JSONObject();
            js.put("userPhone", userPhone);
            json.put("result", js);
        } catch (Exception e) {
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }

    }

    private void checkBackTwo(HttpServletRequest request) throws Exception {
        String userPhone = request.getParameter("userPhone");
        if (userPhone == null || "".equals(userPhone)) {
            throw new Exception("请输入手机号码！");
        }
        String userPassword = request.getParameter("userPassword");
        if (userPassword == null || "".equals(userPassword)) {
            throw new Exception("请输入密码！");
        }
        //密码长度校验
        if(userPassword.length() < 6 || userPassword.length() > 16){
            throw new Exception("请输入6-16位数字加字母组合的密码！");
        }
        //密码组合校验
        if(NumberUtils.isNumber(userPassword) || userPassword.matches("^[A-Za-z]+$")){
            throw new Exception("请输入6-16位数字加字母组合的密码！");
        }
    }

    @Override
    public void setTransPassWord(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            checkTransPassWord(request, bu);
            String transPasswordTwo = request.getParameter("transPasswordTwo");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", bu.getUserPhone());
            BorrowUser buu = userDao.getBorrowUserByPhone(map);
            transPasswordTwo = MD5Utils.MD5Encode(transPasswordTwo);
            buu.setTransactionPassword(transPasswordTwo);
            buu.setUpdateTime(Calendar.getInstance().getTime());
            userDao.updateBorrowUser(buu);
            json.put("code", "0");
            json.put("message", "修改成功！");
            JSONObject js = new JSONObject();
            js.put("userPhone", buu.getUserPhone());
            json.put("result", js);
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }

    }

    private void checkTransPassWord(HttpServletRequest request, BorrowUser bu) throws CheckException, Exception {
        if (bu == null) {
            throw new CheckException("登录信息已超时，请重新登录！");
        }
        String transPasswordOne = request.getParameter("transPasswordOne");
        if (transPasswordOne == null || "".equals(transPasswordOne)) {
            throw new Exception("请输入交易密码！");
        }
        String transPasswordTwo = request.getParameter("transPasswordTwo");
        if (transPasswordTwo == null || "".equals(transPasswordTwo)) {
            throw new Exception("请输入交易密码！");
        }
        if (!transPasswordOne.equals(transPasswordTwo)) {
            throw new Exception("第一次交易密码和第二次的交易密码不一致！");
        }
    }

    @Override
    public void updatePassWord(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            Map<String, Object> mapTmp = new HashMap<>();
            mapTmp.put("userPhone", bu.getUserPhone());
            bu = userDao.getBorrowUserByPhone(mapTmp);
            checkUpdatePassWord(request, bu);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", bu.getUserPhone());
            BorrowUser buu = userDao.getBorrowUserByPhone(map);
            String newPassWord = request.getParameter("newPassWord");
            newPassWord = MD5Utils.MD5Encode(newPassWord);
            String type = StringUtils.getString(request.getParameter("type"));
            if ("1".equals(type)) {
                buu.setUserPassword(newPassWord);
            } else if ("0".equals(type)) {
                buu.setTransactionPassword(newPassWord);
            }
            buu.setUpdateTime(Calendar.getInstance().getTime());
            userDao.updateBorrowUser(buu);
            json.put("code", "0");
            json.put("message", "登录密码修改成功！");
            JSONObject js = new JSONObject();
            js.put("userPhone", buu.getUserPhone());
            json.put("result", js);
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }

    }

    private void checkUpdatePassWord(HttpServletRequest request, BorrowUser bu) throws CheckException, Exception {
        if (bu == null) {
            throw new CheckException("登录信息已超时，请重新登录！");
        }
        String oldPassWord = request.getParameter("oldPassWord");
        if (oldPassWord == null || "".equals(oldPassWord)) {
            throw new Exception("请输入原密码！");
        }
        String newPassWord = request.getParameter("newPassWord");
        if (newPassWord == null || "".equals(newPassWord)) {
            throw new Exception("请输入新密码！");
        }
        oldPassWord = MD5Utils.MD5Encode(oldPassWord);
        String type = StringUtils.getString(request.getParameter("type"));
        if ("1".equals(type)) {
            if (!oldPassWord.equals(bu.getUserPassword())) {
                throw new Exception("原密码不正确，请重新输入！");
            }
        } else if ("0".equals(type)) {
            if (!oldPassWord.equals(bu.getTransactionPassword())) {
                throw new Exception("原密码不正确，请重新输入！");
            }
        }

    }

    @Override
    public void updateBorrowUserInfo(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            checkUpdateBorrowUserInfo(request, bu);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", bu.getUserPhone());
            BorrowUser buu = userDao.getBorrowUserByPhone(map);
            //处理上传的图片信息数据
            //editImg(buu,request);
            buu.setUserName(request.getParameter("userName"));
            buu.setUserCardNo(request.getParameter("userCardNo"));
            buu.setUserEducation(StringUtils.getInteger(request.getParameter("userEducation")));
            buu.setUserMarriage(StringUtils.isEmpty(request.getParameter("userMarriage")) ? null : Integer.parseInt(request.getParameter("userMarriage")));
            buu.setUserProvince(request.getParameter("userProvince"));
            buu.setUserCity(request.getParameter("userCity"));
            buu.setUserArea(request.getParameter("userArea"));
            buu.setUserAddress(request.getParameter("userAddress"));
            buu.setLengthOfStay(StringUtils.getInteger(request.getParameter("lengthOfStay")));
            userDao.updateBorrowUser(buu);
            boolean fls = faceUtils.checkBorrowUserImg(buu);
            buu.setIsVerified(1);
            buu.setVerifiedTime(new Date());
            userDao.updateBorrowUser(buu);
            json.put("code", "0");
            json.put("message", "操作成功！");
            JSONObject js = new JSONObject();
            js.put("userPhone", buu.getUserPhone());
            json.put("result", js);
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }

    }

    private void editImg(BorrowUser buu, MultipartHttpServletRequest request, String operUrl) throws Exception {
        //获取到图片
        List<MultipartFile> files = request.getFiles("files");
        if (files == null || files.size() < 3) {
            throw new Exception("请上传头像及身份证图片！");
        }
        Map<String, Object> map = FileUtil.uploadFile(files);
        buu.setHumanFaceImg(StringUtils.getString(map.get("0")));
        buu.setCardPositiveImg(StringUtils.getString(map.get("1")));
        buu.setCardAntiImg(StringUtils.getString(map.get("2")));
    }

    private void checkUpdateBorrowUserInfo(HttpServletRequest request, BorrowUser bu) throws CheckException, Exception {
        if (bu == null) {
            throw new CheckException("登录信息已超时，请重新登录！");
        }
        String userName = request.getParameter("userName");
        if (userName == null || "".equals(userName)) {
            throw new Exception("请输入姓名！");
        }
        String userCardNo = request.getParameter("userCardNo");
        if (userCardNo == null || "".equals(userCardNo)) {
            throw new Exception("请输入身份证号码！");
        }
        String userEducation = request.getParameter("userEducation");
        if (userEducation == null || "".equals(userEducation)) {
            throw new Exception("请选择学历！");
        }
        String userProvince = request.getParameter("userProvince");
        if (userProvince == null || "".equals(userProvince)) {
            throw new Exception("居住地址省不能为空！");
        }
        String userCity = request.getParameter("userCity");
        if (userCity == null || "".equals(userCity)) {
            throw new Exception("居住地址市不能为空！");
        }
        String userArea = request.getParameter("userArea");
        if (userArea == null || "".equals(userArea)) {
            throw new Exception("居住地址区不能为空！");
        }
        String userAddress = request.getParameter("userAddress");
        if (userAddress == null || "".equals(userAddress)) {
            throw new Exception("居住详细地址不能为空！");
        }

    }

    @Override
    public void queryBorrowUserInfo(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", bu.getUserPhone());
            BorrowUser buu = userDao.getBorrowUserByPhone(map);
            Map<String, Object> resultmap = new HashMap<String, Object>();
            resultmap.put("id", buu.getId());
            resultmap.put("humanFaceImg", StringUtils.getString(buu.getHumanFaceImg()));
            resultmap.put("cardPositiveImg", StringUtils.getString(buu.getCardPositiveImg()));
            resultmap.put("cardAntiImg", StringUtils.getString(buu.getCardAntiImg()));
            resultmap.put("userName", StringUtils.getString(buu.getUserName()));
            resultmap.put("userCardNo", StringUtils.getString(buu.getUserCardNo()));
            resultmap.put("userEducation", StringUtils.getString(buu.getUserEducation()));
            resultmap.put("userMarriage", StringUtils.getString(buu.getUserMarriage()));
            resultmap.put("userProvince", StringUtils.getString(buu.getUserProvince()));
            resultmap.put("userCity", StringUtils.getString(buu.getUserCity()));
            resultmap.put("userArea", StringUtils.getString(buu.getUserArea()));
            resultmap.put("userAddress", StringUtils.getString(buu.getUserAddress()));
            resultmap.put("lengthOfStay", StringUtils.getString(buu.getLengthOfStay()));
            resultmap.put("isVerified", StringUtils.toString(buu.getIsVerified()));
            resultmap.put("cardCode", StringUtils.toString(buu.getCardCode()));

            Map<String, String> iosMap = backConfigParamsService.getBackConfig(BackConfigParams.IOS, null);
            // IOS是否审核中 审核通过后需要改动该值
            resultmap.putAll(iosMap);
            BankAllInfo bankAllInfo = this.bankAllInfoDao.selectByPrimaryKey(buu.getCardType());
            if (null != bankAllInfo) {
                resultmap.put("cardName", bankAllInfo.getBankName());
            }
            //添加学历下拉框数据
            List<Map<String, Object>> ueList = sysCodeDao.queryUserEducation("userEducation");
            resultmap.put("ueList", ueList == null ? new ArrayList<Map<String, Object>>() : ueList);
            //添加婚姻状态数据
            List<Map<String, Object>> umList = sysCodeDao.queryUserEducation("userMarriage");
            resultmap.put("umList", umList == null ? new ArrayList<Map<String, Object>>() : umList);
            //添加居住时长数据
            List<Map<String, Object>> lsList = sysCodeDao.queryUserEducation("lengthOfStay");
            resultmap.put("lsList", lsList == null ? new ArrayList<Map<String, Object>>() : lsList);
            json.put("code", "0");
            json.put("message", "查询成功！");
            json.put("result", JSONObject.toJSON(resultmap));
        } catch (Exception e) {
            e.printStackTrace();
            //登录信息超时重新登录
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }

    }

    private void checkQueryBorrowUserInfo(BorrowUser bu) throws CheckException {
        if (bu == null) {
            throw new CheckException("登录信息已超时，请重新登录！");
        }
    }

    @Override
    public void queryBorrowUserIndex(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            checkQueryBorrowUserInfo(bu);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", bu.getUserPhone());
            BorrowUser buu = userDao.getBorrowUserByPhone(map);
            Map<String, Object> resultmap = new HashMap<String, Object>();
            resultmap.put("isVerified", StringUtils.getString(buu.getIsVerified()));
            resultmap.put("isPhone", StringUtils.getString(buu.getIsPhone()));
            resultmap.put("isOperator", StringUtils.getString(buu.getIsOperator()));
            resultmap.put("isCard", StringUtils.getString(buu.getIsCard()));
            resultmap.put("isZhima", StringUtils.getString(buu.getIsZhima()));

            Map<String, String> loan = backConfigParamsService.getBackConfig(BackConfigParams.LOAN, null);
            Integer moneyAmount = Integer.parseInt(loan.get("LOAN_ALL"));//应退还总额
            //查询用户是否设置渠道借款额度
            ChannelLoanQuota channelLoanQuota = channelService.getChannelLoanQuotaByChannelId(bu.getChannelId());
            if(channelLoanQuota != null
                    && org.apache.commons.lang.StringUtils.isNotEmpty(channelLoanQuota.getLoanPer())
                    && org.apache.commons.lang.StringUtils.isNotEmpty(channelLoanQuota.getLoanWy())
                    && org.apache.commons.lang.StringUtils.isNotEmpty(channelLoanQuota.getLoanAll())
                    && NumberUtils.toInt(channelLoanQuota.getLoanPer()) > 0
                    && NumberUtils.toInt(channelLoanQuota.getLoanWy()) > 0
                    && NumberUtils.toInt(channelLoanQuota.getLoanAll()) > 0
            ){
                //取渠道设置的借款额度
                moneyAmount = NumberUtils.toInt(channelLoanQuota.getLoanAll());
            }
            //老用户提额
            if(bu.getIsOld() == 1) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("userId", bu.getId());
                param.put("intervalDays", Integer.parseInt(loan.get("LOAN_OLD_INTERVAL_DAY"))); //实际付款与放款时间的间隔天数
                int effectCount = iAssetRepaymentOrderDao.getEffectCount(param); //提额有效的还款记录数
                if(effectCount > 0) {
                    int oldAdd = Integer.parseInt(loan.get("LOAN_OLD_ADD")); //老用户提额金额
                    int oldMax = Integer.parseInt(loan.get("LOAN_OLD_MAX")); //老用户提额金额
                    moneyAmount = moneyAmount + oldAdd * effectCount;
                    if(moneyAmount > oldMax) {
                        moneyAmount = oldMax;
                    }
                }
            }
            resultmap.put("creditAmount", moneyAmount);
            json.put("code", "0");
            json.put("message", "查询成功！");
            json.put("result", JSONObject.toJSON(resultmap));
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            //登录信息超时重新登录
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }

    }

    @Override
    public void uploadBorrowUserPhone(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", bu.getUserPhone());
        BorrowUser buu = userDao.getBorrowUserByPhone(map);
        Map<String, Object> resultmap = new HashMap<String, Object>();
        String userPhones = request.getParameter("userPhones");
        JSONArray ja = JSONObject.parseArray(userPhones);
        List<BorrowUserPhone> list = new ArrayList<BorrowUserPhone>();
        for (int i = 0; i < ja.size(); i++) {
            BorrowUserPhone bup = new BorrowUserPhone();
            bup.setName(ja.getJSONObject(i).getString("name"));
            bup.setPhone(ja.getJSONObject(i).getString("phone"));
            bup.setUserId(buu.getId());
            list.add(bup);
        }
        userDao.saveBorrowUserPhone(list);
        buu.setIsPhone(1);
        buu.setPhoneTime(new Date());
        userDao.updateBorrowUser(buu);
        json.put("code", "0");
        json.put("message", "上传成功！");
        json.put("result", JSONObject.toJSON(resultmap));
    }

    @Override
    public ApiServiceResult<JxlCollectResult> updateOperatorInfo(BorrowUser bu, String operatorPwd, String queryPwd) {
        BorrowUser buu = this.getBorrowUserById(bu.getId());
        BorrowUser updateOperator = new BorrowUser();

        ApiServiceResult<JxlCollectResult> apiServiceResult = jxlService.getToken(buu);
        if (apiServiceResult.isSuccessed()) {
            JxlCollectResult jxlCollectResult = apiServiceResult.getExt();
            HashMap<String, Object> params = Maps.newHashMap();
            params.put("userPhone", buu.getUserPhone());
            params.put("userId", buu.getId());
            params.put("token", jxlCollectResult.getJxlToken());
            params.put("website", jxlCollectResult.getWebsite());
            params.put("password", operatorPwd);
            params.put("queryPwd", queryPwd);

            apiServiceResult = jxlService.getCaptcha(params);
            //code 传入 result里面 外面code 还是0
            jxlCollectResult.setCode(apiServiceResult.getCode());

            // 如果成功完成了运营商的授权  那么当前意味着当前token已经可以采集数据了  那么修改认证状态为通过
            if (apiServiceResult.isSuccessed()) {
                {
                    //手机运营商认证状态
                    updateOperator.setIsOperator(1);
                }
                BorrowUserJxl borrowUserJxl = new BorrowUserJxl();
                borrowUserJxl.setToken(jxlCollectResult.getJxlToken());
                borrowUserJxl.setUserId(bu.getId());
                this.userDao.saveBorrowUserJxl(borrowUserJxl);
            }

            // 如果CODE 状态码不等于失败  那么将code重置为成功
            if (!apiServiceResult.getCode().equals(ApiStatus.FAIL.getCode())) {
                apiServiceResult.setCode(ApiStatus.SUCCESS.getCode());
            }


            // 设置返回给API接口的值
            apiServiceResult.setExt(new JxlCollectResult(jxlCollectResult.getJxlToken(), jxlCollectResult.getWebsite(), jxlCollectResult.getCode()));
        }

        {
            updateOperator.setId(buu.getId());
            updateOperator.setOperatorPassword(operatorPwd);
            logger.info("update user operator id:" + updateOperator.getId());
            //手机运营商进行认证
            userDao.updateBorrowUser(updateOperator);
        }

        return apiServiceResult;

    }


    @Override
    public ApiServiceResult<JxlCollectResult> applyCollect(BorrowUser borrowUser, ApplyCollectParams applyCollectParams) {

        HashMap<String, Object> params = Maps.newHashMap();

        params.put("userPhone", borrowUser.getUserPhone());
        params.put("website", applyCollectParams.getWebsite());
        params.put("password", applyCollectParams.getOperatorPassword());
        params.put("queryPwd", applyCollectParams.getQueryPwd());
        params.put("userId", borrowUser.getId());
        params.put("token", applyCollectParams.getJxlToken());
        params.put("smsCaptcha", applyCollectParams.getSmsCaptcha());

        ApiServiceResult apiServiceResult = jxlService.applyCollect(params);
        // 如果成功完成了运营商的授权  那么当前意味着当前token已经可以采集数据了  那么修改认证状态为通过
        if (apiServiceResult.isSuccessed()) {
            BorrowUser user = this.getBorrowUserById(borrowUser.getId());
            user.setIsOperator(1);
            userDao.updateBorrowUser(user);

            BorrowUserJxl borrowUserJxl = new BorrowUserJxl();
            borrowUserJxl.setToken(applyCollectParams.getJxlToken());
            borrowUserJxl.setUserId(borrowUser.getId());
            this.userDao.saveBorrowUserJxl(borrowUserJxl);

        }
        String jxlCode = apiServiceResult.getCode();
        // 如果CODE 状态码不等于失败  那么将code重置为成功
        if (!apiServiceResult.getCode().equals(ApiStatus.FAIL.getCode())) {
            apiServiceResult.setCode(ApiStatus.SUCCESS.getCode());
        }

        return apiServiceResult.setExt(new JxlCollectResult(applyCollectParams.getJxlToken(), applyCollectParams.getWebsite(), jxlCode));
    }

    @Override
    public void getIsTransPassWord(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            checkQueryBorrowUserInfo(bu);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", bu.getUserPhone());
            BorrowUser buu = userDao.getBorrowUserByPhone(map);
            Map<String, Object> resultmap = new HashMap<String, Object>();
            json.put("code", "0");
            json.put("message", "查询成功！");
            String status = buu.getTransactionPassword() != null ? "1" : "0";
            json.put("result", status);
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            //登录信息超时重新登录
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }

    }

    @Override
    public void updateBlackInfo(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            checkQueryBorrowUserInfo(bu);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", bu.getUserPhone());
            BorrowUser buu = userDao.getBorrowUserByPhone(map);
            //对用户进行绑卡操作调用接口？？？？？
            buu.setCardCode(request.getParameter("cardCode"));
            buu.setCardType(new Integer(request.getParameter("cardType")));
            buu.setCardPhone(request.getParameter("cardPhone"));
            userDao.updateBorrowUser(buu);
            Map<String, Object> resultmap = new HashMap<String, Object>();
            json.put("code", "0");
            json.put("message", "查询成功！");
            String status = buu.getIsCard() != null ? "1" : "0";
            resultmap.put("status", status);
            json.put("result", JSONObject.toJSON(resultmap));
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            //登录信息超时重新登录
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }
    }

    @Override
    public void getBorrowUserMessage(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            checkQueryBorrowUserInfo(bu);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", bu.getId());
            int currentPage = Constants.INITIAL_CURRENT_PAGE;
            int pageSize = Constants.INITIAL_PAGE_SIZE;
            if (request.getParameter(Constants.CURRENT_PAGE) != null && !"".equals(request.getParameter(Constants.CURRENT_PAGE))) {
                currentPage = com.inext.utils.StringUtils.getInteger(request.getParameter(Constants.CURRENT_PAGE) + "");
            }
            if (request.getParameter(Constants.PAGE_SIZE) != null && !"".equals(request.getParameter(Constants.PAGE_SIZE))) {
                pageSize = com.inext.utils.StringUtils.getInteger(request.getParameter(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
            List<BorrowUserMessage> list = userDao.getBorrowUserMessage(map);
            json.put("code", "0");
            json.put("message", "查询成功！");
            json.put("result", JSONObject.toJSON(list));
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            //登录信息超时重新登录
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }
    }

    @Override
    public void getBorrowUserMessageDetail(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            checkQueryBorrowUserInfo(bu);
            Map<String, Object> map = new HashMap<String, Object>();
            String id = request.getParameter("id");
            if (id == null) {
                throw new CheckException("缺少站内信id！");
            }
            map.put("id", id);
            List<BorrowUserMessage> list = userDao.getBorrowUserMessage(map);
            BorrowUserMessage borrowUserMessage = list.get(0);
            borrowUserMessage.setIsread(1);
            //标记已读
            userDao.updateBorrowUserMessage(borrowUserMessage);
            json.put("code", "0");
            json.put("message", "查询成功！");
            json.put("result", JSONObject.toJSON(borrowUserMessage));
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            //登录信息超时重新登录
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }
    }

    @Override
    public void getIndexInfo(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        Map<String, Object> resultmap = new HashMap<String, Object>();
        //获取banner数据
        Map<String, Object> param = new HashMap<String, Object>();
        // 只查询上架状态的banner图
        param.put("status", "1");
        List<BannerInfo> bannerList = bannerInfoService.getBannerList(param);
        resultmap.put("bannerList", bannerList);
        //获取滚动公告
        List<AssetBorrowOrder> assetBorrowOrderList = bannerInfoService.queryBorrowOrderList();
        List<String> stringList = new ArrayList<>();
        StringBuffer sb = null;
        int max = 9999;
        int min = 1000;

        int maxjg = 3000;
        int minjg = 1000;


        int maxsj = 20;
        int minsj = 1;
        for (int i = 0; i < 10; i++) {
            //2018-04-26 郭敏需求 公告由 1000-9999卡号 与 100-1999价格 1-59（分）时间 随机数组成

            Random random = new Random();
            for (int money=1000;money<=3500;money=money+1000){
                sb = new StringBuffer();
                //sb.append("尾号");
                sb.append("***");
                sb.append(random.nextInt(max) % (max - min + 1) + min);
                sb.append("用户");
                sb.append(random.nextInt(maxsj) % (maxsj - minsj + 1) + minsj);
                //sb.append("分钟前卖出一部手机");
//            sb.append(assetBorrowOrder.getDeviceModel());
                //sb.append("获得");
                sb.append("分钟前成功借款");
                //sb.append(random.nextInt(maxjg) % (maxjg - minjg + 1) + minjg);
                if(money==2000)
                {
                    sb.append(money-500);
                }else
                {
                    sb.append(money);
                }
                //sb.append(money);
                sb.append("元,");
                sb.append("耗时");
                sb.append(random.nextInt(maxsj) % (maxsj - minsj + 1) + minsj);
                sb.append("分钟");
                stringList.add(sb.toString());
            }

        }


//        for (int i = 0; i < assetBorrowOrderList.size(); i++) {
//            //尾号的用户X分钟前卖出一部XXXX获得XXXX元
//            AssetBorrowOrder assetBorrowOrder = assetBorrowOrderList.get(i);
//            sb = new StringBuffer();
//            sb.append("尾号");
//            sb.append(assetBorrowOrder.getUserPhone().substring(8));
//            sb.append("的用户5分钟前卖出一部");
//            sb.append(assetBorrowOrder.getDeviceModel());
//            sb.append("获得");
//            sb.append(assetBorrowOrder.getMoneyAmount());
//            sb.append("元");
//            stringList.add(sb.toString());
//        }
        resultmap.put("borrowOrderList", stringList);

        {
            // 该块代码已无效
            if (bu != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userPhone", bu.getUserPhone());
                BorrowUser buu = userDao.getBorrowUserByPhone(map);
                //如果风控评估不通过
                if (buu.getIsWindControl() != null) {
                    if (buu.getIsWindControl().equals(0)) {
                        resultmap.put("isWindControl", 0);
                    } else if (buu.getIsWindControl().equals(2)) {
                        //风控评估中
                        resultmap.put("isWindControl", 2);
                    } else {
                        //风控通过但是有未完成的订单
                        String advCode = request.getParameter("advCode");
                        resultmap.put("isWindControl", 3);
                    }
                }
            } else {
                resultmap.put("isWindControl", 401);
            }
        }
        json.put("code", "0");
        json.put("message", "查询成功！");
        json.put("result", JSONObject.toJSON(resultmap));
    }

    @Override
    public void queryBorrowUser(HttpServletRequest request) {
    	
    	List<ChannelInfo> channelList=channelService.getList(new HashMap<String, Object>());
    	request.setAttribute("channelList", channelList);
    	
        String userId = request.getParameter("userId");
        String isBlack = request.getParameter("isBlack");
        String channelId = request.getParameter("channelId");
        String userName = request.getParameter("userName");
        String userCardNo = request.getParameter("userCardNo");
        String userPhone = request.getParameter("userPhone");
        String startDate = StringUtils.getString(request.getParameter("startDate"));
        String endDate = StringUtils.getString(request.getParameter("endDate"));
        if ("".equals(startDate)) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -10);
            startDate = DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
        }
        if ("".equals(endDate)) {
            endDate = DateUtils.formatDate(Calendar.getInstance().getTime(), "yyyy-MM-dd");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("isBlack", isBlack);
        map.put("userName", userName);
        map.put("userCardNo", userCardNo);
        map.put("userPhone", userPhone);
        if( org.apache.commons.lang.StringUtils.isNotBlank(channelId) && Integer.valueOf(channelId) > -1 ){
            map.put("channelId", channelId);
        }
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        
        
        int currentPage = Constants.INITIAL_CURRENT_PAGE;
        int pageSize = Constants.INITIAL_PAGE_SIZE;
        if (request.getParameter(Constants.CURRENT_PAGE) != null && !"".equals(request.getParameter(Constants.CURRENT_PAGE))) {
            currentPage = StringUtils.getInteger(request.getParameter(Constants.CURRENT_PAGE) + "");
        }
        if (request.getParameter(Constants.PAGE_SIZE) != null && !"".equals(request.getParameter(Constants.PAGE_SIZE))) {
            pageSize = StringUtils.getInteger(request.getParameter(Constants.PAGE_SIZE) + "");
        }
        PageHelper.startPage(currentPage, pageSize);
        List<Map<String , String>> list=null;
        if(null == request.getParameter("init"))
            list = userDao.queryBorrowUser(map);
        PageInfo pageInfo = new PageInfo(list);
        request.setAttribute("userId", userId);
        request.setAttribute("isBlack", isBlack);
        request.setAttribute("userName", userName);
        request.setAttribute("userCardNo", userCardNo);
        request.setAttribute("userPhone", userPhone);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("channelId", channelId);
        request.setAttribute("pageInfo", pageInfo);
        //是否黑名单下拉框
        request.setAttribute("isBlackMap", BorrowUser.isBlackMap);

    }

    @Override
    public void updateBorrowUserIsBlack(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", userId);
        BorrowUser bu = userDao.getBorrowUserById(map);
        Integer isBlack = StringUtils.getInteger(request.getParameter("isBlack"));
        bu.setIsBlack(isBlack);
        userDao.updateBorrowUser(bu);
    }

    @Override
    public List<Map<String, String>> exportBorroUserExcelFile(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String isBlack = request.getParameter("isBlack");
        String userName = request.getParameter("userName");
        String userCardNo = request.getParameter("userCardNo");
        String channelId = request.getParameter("channelId");
        String userPhone = request.getParameter("userPhone");
        String startDate = StringUtils.getString(request.getParameter("startDate"));
        String endDate = StringUtils.getString(request.getParameter("endDate"));
        if ("".equals(startDate)) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DATE, 1);
            startDate = DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
        }
        if ("".equals(endDate)) {
            endDate = DateUtils.formatDate(Calendar.getInstance().getTime(), "yyyy-MM-dd");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("isBlack", isBlack);
        map.put("userName", userName);
        map.put("userCardNo", userCardNo);
        map.put("userPhone", userPhone);
        map.put("channelId", channelId);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        List<Map<String, String>> list = userDao.exportBorrowUser(map);
        return list;
    }

    @Override
    public void borrowUserDetail(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String orderId = request.getParameter("orderId");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        request.setAttribute("orderId", orderId);
        List<Map<String, String>> list = userDao.querySignleBorrowUser(map); //userDao.queryBorrowUser(map);
        Map<String, String> resultMap = list.get(0);
        Object obj = resultMap.get("cardType");
        if (obj != null && StringUtils.isNotEmpty(obj + "")) {
            resultMap.put("cardTName", bankAllInfoDao.selectByPrimaryKey(Integer.parseInt(obj + "")).getBankName());
        } else {
            resultMap.put("cardTName", "");
        }
        request.setAttribute("resultMap", resultMap);
        //查询手机通讯录信息数据
        List<BorrowUserPhone> listUserPhone = userDao.queryBorrowUserPhone(map);
        request.setAttribute("listUserPhone", listUserPhone);
        //风控信息
        RiskCreditUser user = iRiskCreditUserService.getNewestByUserId(userId);
        if (user == null) {
            user = new RiskCreditUser();
        }else {
//            ServiceResult serviceResult= isBlack(resultMap.get("userName"),resultMap.get("userPhone"),resultMap.get("userCardNo"));
//            if(serviceResult.getCode().equals("200")){
//                user.setDetail(serviceResult.getMsg());
//            }
        }
        request.setAttribute("fkList", user);
        // 审核记录
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("remark", AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_DFK));
        List<AssetOrderStatusHis> assetOrderStatusHisList = assetOrderStatusHisDao.getAuditRecordList(params);
        request.setAttribute("auditHistoryList", assetOrderStatusHisList);

        //续期信息
        params=new HashMap<>();
        params.put("userPhone", resultMap.get("userPhone"));
        params.put("orderId",orderId);
        List<AssetRenewalOrder> renewalOrderIList= iAssetRenewalOrderDao.getPageList(params);
        request.setAttribute("renewalOrderList",renewalOrderIList);
        List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
        request.setAttribute("channelList", channelList);
        request.setAttribute("paymentChannels", PaymentChannel.DESCRIPTION);
    }
    private ServiceResult isBlack(String userName, String userPhone, String cardNum)
    {
        try
        {
            Map<String, String> keys = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK);

            String url = "http://"+
                    Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK).get("FK_HOST")+ ":"+
                    Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK).get("FK_PORT")+"/api/credit_info/black?";

            if(StringUtils.isEmpty(cardNum))
            {
                return new ServiceResult("400","缺少必要参数[cardNum]");
            }

            String param = "name="+userName+"&phoneNum="+userPhone+"&idCard="+cardNum;

            String result = OkHttpUtils.get(url.concat(param),keys.get("FK_NAME"),keys.get("FK_PWD"));

            if(StringUtils.isEmpty(result))
            {
                return new ServiceResult("500","集团黑名单接口查询维护中，请稍候再试！");
            }

            JSONObject jsonObject = JSONObject.parseObject(result);
            if("200".equals(jsonObject.getString("code")))
            {

                if("命中黑名单".equals(jsonObject.getString("msg")))
                {
                    return new ServiceResult("200",jsonObject.getString("obj"));
                }

                if("未命中黑名单".equals(jsonObject.getString("msg")))
                {
                    return new ServiceResult("300","未命中黑名单");
                }

            }

            return new ServiceResult("300",jsonObject.getString("msg"));// 接口其它错误信息


        }catch (Exception e){
            logger.info("isBlack error----------->"+e.getMessage());

        }


        return new ServiceResult("300","查询黑名单接口失败");
    }

    @Override
    public List<BorrowUser> getNeedCreditUser(HashMap<String, Object> params) {
        return userDao.getNeedCreditUser(params);
    }

    @Override
    public BorrowUser getBorrowUserById(Integer id) {

        return userDao.getBorrowUserByUId(id);
    }

    @Override
    public void updateUser(BorrowUser bu) {

        userDao.updateBorrowUser(bu);
    }

    @Override
    public BorrowUser getBorrowUserByCardNumber(String cardNumber) {

        return userDao.gerBorrowUserByCardNumber(cardNumber);
    }

    @Override
    public void checkTRansPassword(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        try {
            Map<String, Object> mapTmp = new HashMap<>();
            mapTmp.put("userPhone", bu.getUserPhone());
            bu = userDao.getBorrowUserByPhone(mapTmp);
            String transPassword = MD5Utils.MD5Encode(request.getParameter("transPassword"));
            if (!transPassword.equals(bu.getTransactionPassword())) {
                throw new Exception("交易密码不正确，请重新输入！");
            }
            json.put("code", "0");
            json.put("message", "校验成功！");
            JSONObject js = new JSONObject();
            js.put("userPhone", bu.getUserPhone());
            json.put("result", js);
        } catch (CheckException e) {
            //登录信息超时重新登录
            json.put("code", "401");
            json.put("message", e.getMessage());
        } catch (Exception e) {
            json.put("code", "-1");
            json.put("message", e.getMessage());
        }
    }

    @Override
    public void getEquipmentInfo(JSONObject json) {
        List<EquipmentInfo> equipmentInfoList = equipmentDao.getEquipmentList();

        json.put("code", "0");
        json.put("message", "查询成功！");
        json.put("result", JSONObject.toJSON(equipmentInfoList));
    }

    @Override
    public void tgRedister(Map<String, String> params, JSONObject json) {
        String code = "";
        String message = "";

        String channelCode = params.get("channelCode");
        String userPhone = params.get("userPhone");
        String password = params.get("password");
        String smsCode = params.get("smsCode");

        if (StringUtils.isEmpty(channelCode)) {
            message = "参数错误";
        }
        if (StringUtils.isEmpty(userPhone)) {
            message = "手机号码不能为空";
        }
//        if (StringUtils.isEmpty(password)) {
//            message = "密码不能为空";
//        }
        if (StringUtils.isEmpty(smsCode)) {
            message = "短信验证码不能为空";
        }
//        if(NumberUtils.isNumber(password) || password.matches("^[A-Za-z]+$")){
//            message ="请输入数字加字母组合的密码！";
//        }
        //userPhone
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userPhone", userPhone);
        BorrowUser borrowUser = userDao.getBorrowUserByPhone(map);
        if (null != borrowUser) {
//            message = "该手机号已被注册";
            code="100";
        }


        String key = RedisCacheConstants.SMS_CODE_VERIFICATION_LOGIN_PREFIX + userPhone;
        String oldSmsCode = StringUtils.getString(redisUtil.get(key));
        if (!smsCode.equals(oldSmsCode)) {
            message = "短信验证码错误";
        }

        String channelCodeMing = AESUtil.decrypt(channelCode, "");

        ChannelInfo channel = channelService.getChannelByCode(channelCodeMing);

        if (null == channel || channel.getId() == null) {
            message = "暂无此渠道商";
        }

        if (!StringUtils.isEmpty(message)) {
            code = "-1";
            json.put("code", code);
            json.put("message", message);
            return;
        }
        if("100".equals(code)){
        	json.put("code", code);
        	json.put("message", message);
        	return;
        }
        


        //password = MD5Utils.MD5Encode(password);

        BorrowUser bu = new BorrowUser();
        bu.setUserAccount(userPhone);
        bu.setUserPhone(userPhone);
        bu.setStatus(0);
        bu.setUserPassword(password);
        bu.setChannelId(channel.getId());
        
        /** 去掉芝麻分认证 */
        bu.setIsZhima(1);
        bu.setZhimaScore("2");
        userDao.saveBorrowUser(bu);
        code = "0";
        json.put("code", code);
        json.put("message", "注册成功");
        String success = SmsContentConstant.getRegisterSuccess(bu.getUserPhone());
        SmsSendUtil.sendSmsDiyCLandYXByRepaymentRemind(bu.getUserPhone(),success);

    }

	@Override
	public void updateZmExpiredUser(HashMap<String, Object> params){
		 userDao.updateZmExpiredUser(params);
	}

	@Override
	public BorrowUser getBorrowUserByPhone(Map<String, Object> map) {
        BorrowUser buu = userDao.getBorrowUserByPhone(map);
		return buu;
	}

	@Override
	public int partnerRedister(Map<String, Object> params) {
		
		//要对渠道判断
		
		//密文
		if(params.get("maskType").equals("1")){
			
			List<BorrowUser> bulist = userDao.getUserByPhoneMi(String.valueOf(params.get("userPhone")));
			
			if(bulist.size()>0){
				
				return 1; //表示存量用户，拒绝
//				return 2; //表示黑名单用户，拒绝
				
			}else{
				
				return 0; //表示接受用户
			}
		}else if(params.get("maskType").equals("0")){
			
	        BorrowUser bu = new BorrowUser();
	        bu.setUserAccount(String.valueOf(params.get("userPhone")));
	        bu.setUserPhone(String.valueOf(params.get("userPhone")));
	        bu.setStatus(0);
	        bu.setUserPassword(String.valueOf(params.get("password_md5")));
	        ChannelInfo channel = channelService.getChannelByCode(String.valueOf(params.get("registerFrom")));

	        if (null == channel) {
	            return 2;
	        }else{
	        	 bu.setChannelId(channel.getId());
	        }
	        
		       
	        /** 去掉芝麻分认证 */
	        bu.setIsZhima(1);
	        bu.setZhimaScore("2");
	       
	        userDao.saveBorrowUser(bu);
	        
	        return 0;
	        
		}else if(params.get("maskType").equals("511")){ //51卡宝加密验证
			
			List<BorrowUser> bulist = userDao.getUserByPhoneMi(String.valueOf(params.get("userPhone")));
			if(bulist.size()>0){
				
				if(bulist.get(0).getChannelId()!=null){
    				
    				ChannelInfo channel = channelService.getChannelById(bulist.get(0).getChannelId());
    				if(channel!=null){
    					if(channel.getChannelName().equals(com.inext.constants.Constants.CHANNEL_51_KABAO)){
    						return 3;  //3表示卡宝引流的老用户
    					}else{
    						return 1;  //1表示老用户
    					}	
    				}
    			}else{
    				return 1;  //1表示老用户
    			}
				
			}else{
				
				return 0; //表示接受用户
			}	
			
		}else if(params.get("maskType").equals("510")){ //51卡宝注册
			
			List<BorrowUser> bulist = userDao.getUserByPhoneMi(String.valueOf(params.get("mobile")));
			if(bulist.size()>0){
				return 2;
			}
			
	        BorrowUser bu = new BorrowUser();
	        bu.setUserAccount(String.valueOf(params.get("mobile")));
	        bu.setUserPhone(String.valueOf(params.get("mobile")));
	        bu.setStatus(0);
	        bu.setUserPassword(String.valueOf(params.get("password_md5")));
	        ChannelInfo channel = channelService.getChannelByCode(String.valueOf(params.get("channelId")));

	        if (null == channel) {
	            return 2;
	        }else{
	        	 bu.setChannelId(channel.getId());
	        }
	        
		       
	        /** 去掉芝麻分认证 */
	        bu.setIsZhima(1);
	        bu.setZhimaScore("2");
	       
	        userDao.saveBorrowUser(bu);
	        
	        return 0;
		
		}
		
		return 1;
	}


    @Override
    public Object getUserAllInfo(Integer userId,Integer oderId)  {
        BorrowUserAllInfoDto userAllInfo = new BorrowUserAllInfoDto();
        BorrowUser borrowUser ;
        List<AssetBorrowOrder> assetBorrowOrders = new ArrayList<>();//客户历史订单
        com.stylefeng.guns.modular.system.model.BorrowUser borrowUserDto = new com.stylefeng.guns.modular.system.model.BorrowUser();
        borrowUser = this.userDao.getBorrowUserByUId(userId);//客户基本信息
        if(null == borrowUser){
            userAllInfo.setCode(500);
            return userAllInfo;
        }

        BeanUtils.copyProperties(borrowUser,borrowUserDto);
        userAllInfo.setBorrowUser(borrowUserDto);

        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        assetBorrowOrders = this.iAssetBorrowOrderDao.findParams((HashMap<String, Object>) map);
        List<BizAssetBorrowOrder> bizAssetBorrowOrders = new ArrayList<>();
        for (AssetBorrowOrder assetBorrowOrder : assetBorrowOrders) {
            BizAssetBorrowOrder bizAssetBorrowOrder = new BizAssetBorrowOrder();
            BeanUtils.copyProperties(assetBorrowOrder,bizAssetBorrowOrder);
            bizAssetBorrowOrders.add(bizAssetBorrowOrder);
        }
        userAllInfo.setAssetBorrowOrders(bizAssetBorrowOrders);
        userAllInfo.setCode(200);
        return userAllInfo;
    }

	@Override
	public List<BorrowUser> getXuyao() {
		return userDao.getXuyao();
	}

    @Override
    public BorrowUser getUserByIdForAddress(Integer userId) {
        return userDao.getUserByIdForAddress(userId);
    }

    @Override
    public List<BorrowUser> queryAuthList(Map<String, Object> params) {
        return userDao.getAuthList();
    }

    /**
     * 给你花 撞库验证
     * @param appId
     * @param channelId
     * @param timestamp
     * @param mobile
     * @param sign
     * @param mobileMd5
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> gnhvalidate(String appId, String channelId, String timestamp, String mobile, String sign,String mobileMd5) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = verifySign(appId, channelId, timestamp, mobile, sign, mobileMd5);

        if (!result) {
            resultMap.put("code", GnhResultEnum.SIGN_ERROR.getCode());
            resultMap.put("message",GnhResultEnum.SIGN_ERROR.getMsg());
            return resultMap;
        }

        List<BorrowUser> bulist = userDao.getUserByPhoneMi(mobileMd5);
        if (bulist == null || bulist.size()==0){
            //新用户
            resultMap.put("code", GnhResultEnum.NEW_USER.getCode());
            resultMap.put("message",GnhResultEnum.NEW_USER.getMsg());
            return resultMap;
        }

        ChannelInfo channelInfo = iChannelService.getChannelById(bulist.get(0).getChannelId());
        if (channelInfo==null){
            //1004：其他渠道老用户
            resultMap.put("code", GnhResultEnum.OTHER_OLD_USER.getCode());
            resultMap.put("message",GnhResultEnum.OTHER_OLD_USER.getMsg());
            return resultMap;
        }else if (channelId.equals(channelInfo.getChannelCode())){
            //1000：本渠道老用户
            resultMap.put("code", GnhResultEnum.GNH_OLD_USER.getCode());
            resultMap.put("message",GnhResultEnum.GNH_OLD_USER.getMsg());
            return resultMap;
        }else {
            //1004：其他渠道老用户
            resultMap.put("code", GnhResultEnum.OTHER_OLD_USER.getCode());
            resultMap.put("message",GnhResultEnum.OTHER_OLD_USER.getMsg());
            return resultMap;
        }

    }

    /**
     * 给你花，联合注册
     * @param appId
     * @param channelId
     * @param timestamp
     * @param mobile
     * @param sign
     * @return
     */
    @Override
    public Map<String, Object> gnhRegister(String appId, String channelId, String timestamp, String mobile, String sign) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = verifySign(appId, channelId, timestamp, mobile, sign, null);
        if (!result) {
            logger.info("method:gnhRegister  验证签名错误");
            resultMap.put("code", GnhResultEnum.SIGN_ERROR.getCode());
            resultMap.put("message",GnhResultEnum.SIGN_ERROR.getMsg());
            return resultMap;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userPhone",mobile);
        BorrowUser existUser = userDao.getBorrowUserByPhone(paramMap);
        if (existUser != null){
            //老用户，不用注册啦，直接返回落地页url
            logger.info("给你花，联合注册  账号："+mobile+" 是【花小侠】老用户");

            resultMap.put("code", GnhResultEnum.GNH_OLD_USER.getCode());
            resultMap.put("message",GnhResultEnum.GNH_OLD_USER.getMsg());
            resultMap.put("url","http://api.hxx368.com/app_sys/down_new_version");
            return resultMap;
        }

        ChannelInfo channelInfo = iChannelService.getChannelByCode(channelId);
        Integer channelId1 = 0;
        if( channelInfo == null ){
            logger.warn( "channel is empty channelCode:{}" , channelId );
            resultMap.put("code", GnhResultEnum.REGISTER_FAILURE.getCode());
            resultMap.put("message","该渠道无效！");
            return resultMap;
        }else{
            channelId1 = channelInfo.getId();
        }
        insertBorrowUser(mobile, channelId1);


        //String msg_content = "恭喜您已成为花小侠会员，登录账号: "+mobile+" 登录密码:"+password+"，请妥善保管，建议马上登录APP修改密码。";

//        SmsSendUtil.sendSmsDiyCLandYX(mobile, msg_content);
        logger.info("给你花，联合注册，联合注册成功，账号："+mobile+" 恭喜成为【花小侠】会员");
        resultMap.put("code", GnhResultEnum.REGISTER_SUCCESS.getCode());
        resultMap.put("message",GnhResultEnum.REGISTER_SUCCESS.getMsg());
        resultMap.put("url","http://api.flower369.com/app_sys/down_new_version");
        return resultMap;
    }



    @Override
    public void partner360Register(String mobile, String channelCode, String pre_orderid) {
        //判断新老用户，并调用360接口返还数据；如果是新用户，则注册
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> bizData = new HashMap<String, Object>();//组装请求360的数据
        bizData.put("pre_orderid", pre_orderid);
        bizData.put("event", "regist");

        Map<String, String> mapRefuse = backConfigParamsService.getBackConfig("PARTNER_360", null);
        String notifyUrl = mapRefuse.get("NOTIFY_URL");
        Map<String, String> map2 = backConfigParamsService.getBackConfig("PARTNER_360", null);
        String privateKey = map2.get("PRIVATE_KEY");
        Map<String, String> map3 = backConfigParamsService.getBackConfig("PARTNER_360", null);
        String appId = map3.get("APP_ID");

        paramMap.put("userPhone",mobile);
        BorrowUser existUser = userDao.getBorrowUserByPhone(paramMap);
        if (existUser != null){
            //老用户，调用360接口

            bizData.put("user_flag", "2");//1-新用户 2-老用户
            bizData.put("update_time", String.valueOf(System.currentTimeMillis() / 1000));


            try {
                new D360Client().call360(bizData, notifyUrl, privateKey, appId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("partner callback 360 success");
            return;
        }

        //新用户，注册并调用360接口
        ChannelInfo channelInfo = iChannelService.getChannelByCode(channelCode);
        Integer channelId1 = 0;
        if( channelInfo == null ){
            logger.warn( "channel is empty channelCode:{}" , channelCode );
            logger.warn("partner callback 360 failure");
            return;
        }else{
            channelId1 = channelInfo.getId();
        }

        insertBorrowUser(mobile, channelId1);


        bizData.put("user_flag", "1");//1-新用户 2-老用户
        bizData.put("update_time", String.valueOf(System.currentTimeMillis() / 1000));

        try {
            new D360Client().call360(bizData, notifyUrl,privateKey,appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("partner call 360 success");
        return;
    }

    /**
     * 融易来（贷上线）撞库
     * @param phone
     * @param timestamp
     * @param sign
     * @param channelCode
     * @return
     */
    @Override
    public Map<String, Object> rylValidate(String phone, String timestamp, String sign, String channelCode) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = verifySignRyl(phone, timestamp, sign, channelCode);

        if (!result) {
            resultMap.put("code", -1);
            resultMap.put("message",GnhResultEnum.SIGN_ERROR.getMsg());
            return resultMap;
        }

        List<BorrowUser> bulist = userDao.getUserByPhoneMi(MD5Utils.md5(phone));
        if (bulist == null || bulist.size()==0){
            //新用户
            resultMap.put("code", 0);
            resultMap.put("message",GnhResultEnum.NEW_USER.getMsg());
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("status",1);
            resultMap.put("data",data);
            return resultMap;
        }

        ChannelInfo channelInfo = iChannelService.getChannelById(bulist.get(0).getChannelId());
        if (channelInfo==null){
            //1004：其他渠道老用户
            resultMap.put("code", 0);
            resultMap.put("message",GnhResultEnum.OTHER_OLD_USER.getMsg());
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("status",3);
            resultMap.put("data",data);
            return resultMap;
        }else if (channelCode.equals(channelInfo.getChannelCode())){
            //1000：本渠道老用户
            resultMap.put("code", 0);
            resultMap.put("message",GnhResultEnum.GNH_OLD_USER.getMsg());
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("status",2);
            resultMap.put("data",data);
            return resultMap;
        }else {
            //1004：其他渠道老用户
            resultMap.put("code", 0);
            resultMap.put("message",GnhResultEnum.OTHER_OLD_USER.getMsg());
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("status",3);
            resultMap.put("data",data);
            return resultMap;
        }
    }

    /**
     * 融易来（贷上线）联合注册
     * @param channelCode
     * @param timestamp
     * @param bizData
     * @param sign
     * @return
     */
    @Override
    public Map<String, Object> rydRegister(String channelCode, String timestamp, String bizData, String sign) throws UnsupportedEncodingException {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String phone = new String(Base64Utils.decode(bizData));
        boolean result = verifySignRyl(phone, timestamp, sign, channelCode);
        if (!result) {
            resultMap.put("code", -1);
            resultMap.put("message",GnhResultEnum.SIGN_ERROR.getMsg());
            return resultMap;
        }
        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("userPhone",phone);
        BorrowUser existUser = userDao.getBorrowUserByPhone(paramMap);
        if (existUser != null){
            //老用户，不用注册啦，直接返回落地页url
            logger.info("融易来，联合注册  账号："+phone+" 是【花小侠】老用户");
            resultMap.put("code", 0);

            HashMap<String, Object> data = new HashMap<String, Object>();
            ChannelInfo channelInfo = iChannelService.getChannelById(existUser.getChannelId());
            if (channelInfo==null){
                //1004：其他渠道老用户
                data.put("status",3);
                resultMap.put("message",GnhResultEnum.OTHER_OLD_USER.getMsg());
                return resultMap;
            }else if (channelCode.equals(channelInfo.getChannelCode())){
                //1000：本渠道老用户
                data.put("status",2);
                resultMap.put("message",GnhResultEnum.GNH_OLD_USER.getMsg());
            }else {
                //1004：其他渠道老用户
                resultMap.put("message",GnhResultEnum.OTHER_OLD_USER.getMsg());
                data.put("status",3);
            }
            data.put("url","http://api.flower369.com/app_sys/down_new_version");
            resultMap.put("data",data);
            return resultMap;
        }

        ChannelInfo channelInfo = iChannelService.getChannelByCode(channelCode);
        Integer channelId1 = 0;
        if( channelInfo == null ){
            logger.warn( "channel is empty channelCode:{}" , channelCode );
            resultMap.put("code", -1);
            resultMap.put("message","该渠道无效！");
            return resultMap;
        }else{
            channelId1 = channelInfo.getId();
        }
        insertBorrowUser(phone, channelId1);

        logger.info("融易来，联合注册，联合注册成功，账号："+phone+" 恭喜成为【花小侠】会员");
        resultMap.put("code", 0);
        resultMap.put("message",GnhResultEnum.REGISTER_SUCCESS.getMsg());
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("status",1);
        data.put("url","http://api.flower369.com/app_sys/down_new_version");
        resultMap.put("data",data);
        return resultMap;
    }

    /**
     * 融易来 验签
     * @param phone
     * @param timestamp
     * @param sign
     * @param channelCode
     * @return
     */
    private boolean verifySignRyl(String phone, String timestamp, String sign, String channelCode) {
        HashMap<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("channelCode", channelCode);
        paraMap.put("timestamp", timestamp);//时间戳
        paraMap.put("phone", phone);
        String data = GnhSortUtils.formatUrlMap(paraMap);
        boolean flag = sign.equals(MD5Utils.md5(data));
        return flag;
    }

    /**
     * 生成随机密码
     * @param mobile
     * @return
     */
    private String randomPassword(String mobile) {
        String random_str = "";
        for (int i = 0;i<4;i++){
            random_str = random_str+ (char)(Math.random()*26+'a');
        }

        return random_str+mobile;
    }

    private boolean verifySign(String appId, String channelId, String timestamp, String mobile, String sign,
                               String mobileMd5) throws Exception {
        HashMap<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("appId", appId);
        paraMap.put("channelId", channelId);
        paraMap.put("timestamp", timestamp);//时间戳
        paraMap.put("mobile", mobile);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(mobileMd5)) {
            paraMap.put("mobileMd5", mobileMd5);
        }
        /**校验数字签名**/
        String data = GnhSortUtils.formatUrlMap(paraMap);
        logger.info("------签名明文排序数据-------------"+data);
        Map<String, String> mapRefuse = backConfigParamsService.getBackConfig("PALT_PUBLIC_KEY", null);
        String publicKey = mapRefuse.get("PUBLIC_KEY");
//        boolean equals = publicKey.equals(GnhConstant.PALT_PUBLIC_KEY);
        boolean result = GnhRSAUtil.verify(data.getBytes(), publicKey, sign);
        logger.info("------校验签名结果-------------"+result);
        return result;
    }

    @Override
    public void queryBorrowUserAmount(JSONObject json, HttpServletRequest request, BorrowUser bu) {
        Map<String, Object> resultmap = new HashMap<String, Object>();
        Map<String, String> loan = backConfigParamsService.getBackConfig(BackConfigParams.LOAN, null);
        Map<String, String> creditAmount = backConfigParamsService.getBackConfig(BackConfigParams.CREDIT_AMOUNT, null);
        Integer realAmount = Integer.parseInt(loan.get("LOAN_ALL"));//应退还总额;
        String lowAmount =creditAmount.get("LOW_AMOUNT");//最低贷款金额
        System.out.println("lowAmount:"+ lowAmount);
        //String highAmount = loan.get("LOAN_OLD_MAX");//最高贷款金额
        String highAmount = creditAmount.get("HIGH_AMOUNT");//最高贷款金额
        //老用户提额
        if(bu !=null){
            if(bu.getIsBlack()==0 && bu.getIsVerified()==0 && bu.getIsPhone()==0 && bu.getIsYop()==0 && bu.getIsCard()==0 && bu.getIsOperator()==0 && bu.getIsWindControl()==0 && bu.getIsOld()==0 && bu.getIsZhima()==1)
            {
                resultmap.put("realAmount", highAmount);
            }
            else if (bu.getIsBlack()==0 && bu.getIsVerified()==1 && bu.getIsPhone()==1 && bu.getIsYop()==0 && bu.getIsCard()==0 && bu.getIsOperator()==1 && bu.getIsWindControl()==0 && bu.getIsOld()==0 && bu.getIsZhima()==1)
            {
                resultmap.put("realAmount", highAmount);
            }else {
                resultmap.put("realAmount", realAmount);
            }

            if(bu.getIsOld() == 1) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("userId", bu.getId());
                param.put("intervalDays", Integer.parseInt(loan.get("LOAN_OLD_INTERVAL_DAY"))); //实际付款与放款时间的间隔天数
                int effectCount = iAssetRepaymentOrderDao.getEffectCount(param); //提额有效的还款记录数
                if(effectCount > 0) {
                    int oldAdd = Integer.parseInt(loan.get("LOAN_OLD_ADD")); //老用户提额金额
                    int oldMax = Integer.parseInt(loan.get("LOAN_OLD_MAX")); //老用户提额金额

                    realAmount = realAmount + oldAdd * effectCount;
                    if(realAmount > oldMax) {
                        realAmount = oldMax;
                    }
                }
                resultmap.put("realAmount", realAmount);
            }
//            else{
//                resultmap.put("realAmount", realAmount);
//            }
        }else{
            resultmap.put("realAmount", highAmount);
        }


        //resultmap.put("realAmount", realAmount);
        resultmap.put("lowAmount",lowAmount);
        resultmap.put("highAmount",highAmount);
        json.put("code", "0");
        json.put("message", "查询成功！");
        json.put("result", JSONObject.toJSON(resultmap));
    }
    /**
     * save borrowUser
     * @param mobile
     * @param channelId1
     */
    private void insertBorrowUser(String mobile, Integer channelId1) {
        //生成密码
        String password = randomPassword(mobile);

        BorrowUser bu = new BorrowUser();
        bu.setUserAccount(mobile);
        bu.setUserPhone(mobile);
        bu.setStatus(0);
        String md5Password = MD5Utils.MD5Encode(password);
        bu.setUserPassword(md5Password);

        bu.setChannelId(channelId1);
        /** 去掉芝麻分认证 */
        bu.setIsZhima(1);
        bu.setZhimaScore("2");
        userDao.saveBorrowUser(bu);
    }
    public static void main(String[] args) {
        HashMap<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("channelCode", "ryl");
        paraMap.put("timestamp", "1541071426523");//时间戳
        paraMap.put("phone", "17521028972");
        String data = GnhSortUtils.formatUrlMap(paraMap);
        String s = MD5Utils.md5(data);
        System.out.println(s);
    }

}
