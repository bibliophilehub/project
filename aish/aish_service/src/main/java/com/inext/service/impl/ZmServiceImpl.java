package com.inext.service.impl;

import com.alibaba.fastjson.JSON;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BorrowUser;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiGxbResult;
import com.inext.result.ApiGxbServiceResult;
import com.inext.result.ApiServiceResult;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBorrowUserService;
import com.inext.service.IZmService;
import com.inext.utils.DateUtil;
import com.inext.utils.DateUtils;
import com.inext.utils.IdUtil;
import com.inext.utils.OkHttpUtils;
import com.inext.utils.zhima.AESHelper;
import com.inext.view.result.ZhimaResult;
import com.squareup.okhttp.Request.Builder;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("zmService")
public class ZmServiceImpl implements IZmService {

    Logger logger = LoggerFactory.getLogger(ZmServiceImpl.class);
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    IBorrowUserService borrowUserService;

    /**
     * 请求芝麻授权平台接口  获取用户授权信息
     *
     * @param isNew  哪种授权方式  1 新(唤起支付宝app授权) 2旧 H5授权
     * @param userId
     * @return ApiServiceResult
     * code = 0     说明用户已经授权成功 分数在 msg参数中 过期时间在ext参数中
     * code = 100   需要用户授权 msg是需要app引导用户完成授权操作的链接
     * code = 300   用户已经授权成功 但是分数需要通过回调接口获取
     * @throws Exception
     */
    public ApiServiceResult getZmInfo(String isNew, Integer userId) throws Exception {
        BorrowUser bu = borrowUserService.getBorrowUserById(userId);

        Map<String, String> zm = backConfigParamsService.getBackConfig(BackConfigParams.ZM, null);
        String userName = zm.get("zm_user_name");
        String passwd = zm.get("zm_password");
        // 两个接口 使用哪个取决于app是否新版
        String zmUrl = "1".equals(isNew) ? zm.get("new_zm_url") : zm.get("zm_url");
        // 是否强制使用新版芝麻授信
        if ("1".equals(zm.get("constraint_new"))) {
            zmUrl = zm.get("new_zm_url");
            // 在强制使用新版的情况下判定是旧版 那么给出提示
            if (!"1".equals(isNew)) {
                return new ApiServiceResult(ApiStatus.FAIL.getCode(), "授权失败,请更新至最新版app");
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", userName);
        jsonObject.put("passwd", passwd);
        jsonObject.put("expiration", System.currentTimeMillis());

        String token = AESHelper.encrypt(jsonObject.toString());
        Builder builder = new Builder();
        builder.addHeader("Accept", "application/json");
        builder.addHeader("Content-type", "application/json; charset=utf-8");
        builder.addHeader("DKCS-HT-AT", token);

        JSONObject params = new JSONObject();
        params.put("realname", bu.getUserName());
        params.put("idCard", bu.getUserCardNo());
        params.put("userPhone", bu.getUserPhone());

        logger.info("userId: {} zhima param: {}", userId, params.toString());
        String ret = OkHttpUtils.post(zmUrl, params.toString(), builder);
        logger.info("userId: {} zhima ret: {}", userId, ret);

        JSONObject result = JSONObject.fromObject(ret);
        if (!"success".equals(result.getString("code"))) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), result.getString("msg"));
        }

        JSONObject data = result.getJSONObject("data");

        if (!data.containsKey("need")) {
            //直接获取芝麻分
            //说明该用户已在第三方平台上通过其它App授权
            String score = data.getString("score");
            String scoreLastTime = data.getString("scoreLastTime");
            return new ApiServiceResult(ApiStatus.SUCCESS.getCode(), "授权成功",
                    new ZhimaResult("", "1", "1", score, scoreLastTime));
        }

        String need = data.getString("need");
        switch (need) {
            case "0":
                //已授权 通过回调接口获取芝麻分
                return new ApiServiceResult("300", "已授权,待获取",
                        new ZhimaResult("", "1", "", "", ""));
            case "1":
                //未授权，让用户授权
                return new ApiServiceResult("100", "",
                        new ZhimaResult(data.getString("url"), "0", "0", "", ""));
            default:
                return new ApiServiceResult(ApiStatus.FAIL.getCode(), "授权失败");
        }
    }

    @Override
    public ApiServiceResult<ZhimaResult> creditReportZm(String isNew, Integer userId) throws Exception {

        ApiServiceResult<ZhimaResult> apiServiceResult = this.getZmInfo(isNew, userId);

        if (ApiStatus.FAIL.getCode().equals(apiServiceResult.getCode())) {
            return apiServiceResult;
        }

        ZhimaResult zhimaResult = apiServiceResult.getExt();
        if (apiServiceResult.isSuccessed()) {
            String score = zhimaResult.getScore();
            String scoreLastTime = zhimaResult.getScoreLastTime();

            //更新芝麻分
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String d = format.format(new Long(scoreLastTime));
            Date date = format.parse(d);

            BorrowUser updateUser = new BorrowUser();
            updateUser.setId(userId);
            updateUser.setIsZhima(1);
            updateUser.setZhimaScore(score);
            updateUser.setZmAuthTime(date);

            borrowUserService.updateUser(updateUser);
        }

        // ZhimaResult zhimaResult = new ZhimaResult();
        // switch (apiServiceResult.getCode()) {
        //     case "100":
        //         zhimaResult.setAuthUrl(apiServiceResult.getMsg());
        //         zhimaResult.setIsAuth("0");
        //         break;
        //     case "300":
        //         //等待回调，更新芝麻分
        //         zhimaResult.setIsAuth("1");
        //         break;
        // }

        return new ApiServiceResult(ApiStatus.SUCCESS.getCode(), ApiStatus.SUCCESS.getValue(), zhimaResult);
    }

    @Override
    public void zmAuthExpire() {

        Map<String, String> zm = backConfigParamsService.getBackConfig(BackConfigParams.ZM, null);
        String zmExpire = zm.get("zm_expire");
        DateUtil.addDay(new Date(), -Integer.parseInt(zmExpire));


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("expireTime", DateUtil.addDay(calendar.getTime(), -30));

        borrowUserService.updateZmExpiredUser(param);
    }

    @Override
    public ApiGxbServiceResult creditReportGxbZm(String isNew, Integer userId) throws Exception {
        ApiGxbServiceResult apiServiceResult = new ApiGxbServiceResult();
        BorrowUser bu = borrowUserService.getBorrowUserById(userId);
        if(bu.getIsZhima()==1){
            apiServiceResult.setMessage("用户已授权");
            apiServiceResult.setCode(ApiStatus.FAIL.getCode());
            return apiServiceResult;
        }else if(bu.getIsZhima()==0 && DateUtils.getDistanceOfTwoHour(bu.getCreateTime(),
                DateUtils.formatDate("2018-11-30 16:00:00"))>0){
            apiServiceResult.setMessage("请先更新最新app");
            apiServiceResult.setCode(ApiStatus.FAIL.getCode());
            return apiServiceResult;
        }



        Map<String, String> zm = backConfigParamsService.getBackConfig(BackConfigParams.ZM, null);
        String appId = zm.get("zm_gxb_appId");
        String appSecurity = zm.get("zm_gxb_appsecurity");
        // 两个接口 使用哪个取决于app是否新版
        String zmUrl = zm.get("zm_gxb_url");
        JSONObject result = this.getZmGxbToken(appId,appSecurity,zmUrl,bu);
        System.out.println(result.get("retCode"));
        if (!ApiStatus.SUCCESS_GXB.getCode().equals(result.get("retCode").toString())) {
            apiServiceResult.setMessage(result.get("retMsg").toString());
            apiServiceResult.setCode(ApiStatus.FAIL.getCode());
            return apiServiceResult;
        }
        Map<String,Object> tokenMap = (Map<String, Object>) JSON.parse(result.get("data").toString());

        String auth_url = zm.get("zm_gxb_auth_url");
        String urls = zm.get("zm_gxb_callback_api");
        String url = auth_url+"?returnUrl="+ URLEncoder.encode(urls,"utf-8") +"&token="+tokenMap.get("token");
        System.out.println(url);
        //String results = OkHttpUtils.get(url);
        return new ApiGxbServiceResult(ApiStatus.SUCCESS.getCode(), ApiStatus.SUCCESS.getValue(), url);
    }


    /**
     * 请求芝麻授权平台接口  获取用户授权信息
     *
     * @param bu
     * @return ApiServiceResult
     * code = 0     说明用户已经授权成功 分数在 msg参数中 过期时间在ext参数中
     * code = 100   需要用户授权 msg是需要app引导用户完成授权操作的链接
     * code = 300   用户已经授权成功 但是分数需要通过回调接口获取
     * @throws Exception
     */
    public JSONObject getZmGxbToken(String appId,String appSecurity,String zmUrl,BorrowUser bu) throws Exception {


        String authItem = "sesame_multiple";
        String sequenceNo =  IdUtil.getRandomString(32);
        Long timestamp = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId", appId);
        jsonObject.put("authItem",authItem);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("sequenceNo",sequenceNo);
        jsonObject.put("phone", bu.getUserPhone());
        jsonObject.put("name", bu.getUserName());
        jsonObject.put("idcard", bu.getUserCardNo());
        String sign =  DigestUtils.md5Hex(String.format("%s%s%s%s%s", appId, appSecurity, authItem, timestamp, sequenceNo));
        jsonObject.put("sign", sign);
       // String token = AESHelper.encrypt(jsonObject.toString());
        Builder builder = new Builder();
        builder.addHeader("Accept", "application/json");
        builder.addHeader("Content-type", "application/json; charset=utf-8");

        logger.info("userId: {} zhima param: {}", bu.getId(), jsonObject.toString());
        String ret = OkHttpUtils.post(zmUrl, jsonObject.toString(),builder);
        logger.info("userId: {} zhima ret: {}", bu.getId(), ret);

        JSONObject result = JSONObject.fromObject(ret);
        return result;
    }


}
