package com.inext.service;

import com.alibaba.fastjson.JSONObject;
import com.inext.entity.BorrowUser;
import com.inext.result.ApiServiceResult;
import com.inext.view.params.ApplyCollectParams;
import com.inext.view.result.JxlCollectResult;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IBorrowUserService {

    public void loginOne(JSONObject json, HttpServletRequest request);

    public void login(JSONObject json, HttpServletRequest request);

    /**
     * 短信验证码登陆
     * @param json
     * @param request
     */
    void smsLogin(JSONObject json, HttpServletRequest request);

    public void registered(JSONObject json, HttpServletRequest request);

    public void getBackOne(JSONObject json, HttpServletRequest request);

    public void getBackTwo(JSONObject json, HttpServletRequest request);

    public void setTransPassWord(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void updatePassWord(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void updateBorrowUserInfo(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void queryBorrowUserInfo(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void queryBorrowUserIndex(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void uploadBorrowUserPhone(JSONObject json, HttpServletRequest request, BorrowUser bu);

    ApiServiceResult<JxlCollectResult> updateOperatorInfo(BorrowUser bu, String operatorPwd, String queryPwd);

    /**
     * 提交验证码接口
     * @param borrowUser
     * @param applyCollectParams
     * @return
     */
    ApiServiceResult<JxlCollectResult> applyCollect(BorrowUser borrowUser, ApplyCollectParams applyCollectParams);

    public BorrowUser getBorrowUserById(Integer id);

    public BorrowUser getBorrowUserByCardNumber(String cardNumber);


    public void getIsTransPassWord(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void updateBlackInfo(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void getBorrowUserMessage(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void getBorrowUserMessageDetail(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void getIndexInfo(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void queryBorrowUser(HttpServletRequest request);

    public void updateBorrowUserIsBlack(HttpServletRequest request);

    public List<Map<String, String>> exportBorroUserExcelFile(HttpServletRequest request);

    public void borrowUserDetail(HttpServletRequest request);

    public void updateUser(BorrowUser bu);

    /**
     * 获取需要征信的用户 只获取
     *
     * @param params
     * @return
     */
    public List<BorrowUser> getNeedCreditUser(HashMap<String, Object> params);

    public void checkTRansPassword(JSONObject json, HttpServletRequest request, BorrowUser bu);

    public void getEquipmentInfo(JSONObject json);

	public void tgRedister(Map<String, String> params, JSONObject json);

	public void updateZmExpiredUser(HashMap<String, Object> param);

	public BorrowUser getBorrowUserByPhone(Map<String, Object> params);
	
	public int partnerRedister(Map<String, Object> params);

    /**
     * 催收获取用户信息
     * @param userId
     * @param orderId
     * @return
     */
    Object getUserAllInfo(Integer userId, Integer orderId);
    
    public List<BorrowUser> getXuyao();

    BorrowUser getUserByIdForAddress(Integer id);

    /** add by */
    List<BorrowUser> queryAuthList(Map<String, Object> params);

    Map<String, Object> gnhvalidate(String appId, String channelId, String timestamp, String mobile, String sign, String mobileMd5) throws Exception;

    Map<String, Object> gnhRegister(String appId, String channelId, String timestamp, String mobile, String sign) throws Exception;
    public void queryBorrowUserAmount(JSONObject json, HttpServletRequest request, BorrowUser bu);


    void partner360Register(String mobile, String channelCode, String pre_orderid);

    Map<String, Object> rylValidate(String phone, String timestamp, String sign, String channelCode);

    Map<String, Object> rydRegister(String channelCode, String timestamp, String bizData, String sign) throws UnsupportedEncodingException;
}
