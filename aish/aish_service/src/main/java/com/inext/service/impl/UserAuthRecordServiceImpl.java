package com.inext.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

//import com.inext.utils.PropertiesUtil;
import com.inext.utils.StringUtils;
import com.inext.utils.baofoo.PropertiesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inext.dao.IBorrowUserDao;
import com.inext.dao.IUserAuthRecordDao;
import com.inext.dao.IUserMobileAuthLogDao;
import com.inext.entity.BorrowUser;
import com.inext.entity.UserAuthRecord;
import com.inext.entity.UserMobileAuthLog;
import com.inext.service.UserAuthRecordService;
import com.inext.utils.baofoo.ChuQiYouAuthUtil;

@Service("userAuthRecordService")
public class UserAuthRecordServiceImpl implements UserAuthRecordService {

	Logger logger = LoggerFactory.getLogger(UserAuthRecordServiceImpl.class);
	@Resource
	private IUserAuthRecordDao userAuthRecordDao;

	@Resource
	private IUserMobileAuthLogDao userMobileAuthLogDao;

	@Resource
	ChuQiYouAuthUtil chuQiYouAuthUtil;

	@Resource
	IBorrowUserDao userDao;

	@Override
	public void saveRecord(UserAuthRecord record) {
		userAuthRecordDao.saveRecord(record);

	}
	@Override
	public void updateRecord(UserAuthRecord record) {
		userAuthRecordDao.updateRecord(record);

	}
	@Override
	public List<UserAuthRecord> getRecordByUserId(Map<String, Object> params) {
		return userAuthRecordDao.getRecordByUserId(params);
	}



	/**------ 新风控运营商认证  ----- */
	@Override
	public void saveUserMobileAuthLog(UserMobileAuthLog log) {
		userMobileAuthLogDao.saveMobileAuth(log);

	}
	@Override
	public void updateUserMobileAuthLog(UserMobileAuthLog log) {
		userMobileAuthLogDao.updateMobileAuth(log);

	}
	@Override
	public List<UserMobileAuthLog> getUserMobileAuthLogByUserId(Map<String, Object> params) {
		return userMobileAuthLogDao.getMobileAuthLog(params);
	}
	@Override
	public Map<String, String> doMobileAuth(BorrowUser user) {

		if(user!=null){

			Map<String, String> ret_map = new HashMap<String, String>();

			try {

				Map<String, Object> postMap =
						chuQiYouAuthUtil.Auth(String.valueOf(user.getId()), user.getUserName(), user.getUserPhone(),
								user.getUserCardNo(), "F5C533");

				ret_map.put("code", String.valueOf(postMap.get("code")));
				if(String.valueOf(postMap.get("code")).equals("0000")){
					ret_map.put("message", "等待认证结果");
					ret_map.put("operatorUrl", String.valueOf(postMap.get("operatorUrl")));
					UserMobileAuthLog auth_log = new UserMobileAuthLog();

					auth_log.setUserId(String.valueOf(user.getId()));
					auth_log.setUserName(user.getUserName());
					auth_log.setMobile(user.getUserPhone());
					auth_log.setCardNo(user.getUserCardNo());
					auth_log.setBackUrl(PropertiesUtil.get("local_url")+"/borrowUser/return_applyCollect");
					auth_log.setThemeColor("F5C533");
					auth_log.setAuth_status(1);
					auth_log.setCreate_time(new Date());

					JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(postMap));

					auth_log.setReturn_json(itemJSONObj.toJSONString());

					userMobileAuthLogDao.saveMobileAuth(auth_log);


				}else if(String.valueOf(postMap.get("code")).equals("0001")){

					//更新用户表状态
					user.setIsOperator(1);  //运营商已认证
					user.setOperatorTime(new Date());  //运营商已认证
					userDao.updateBorrowUser(user);
					ret_map.put("message", "已认证，勿重复认证");
				}else if(String.valueOf(postMap.get("code")).equals("0101")){
					ret_map.put("message", "参数缺失");
				}else if(String.valueOf(postMap.get("code")).equals("0200")){
					ret_map.put("message", "系统错误");
				}

				return ret_map;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;


	}

	/**
	 * 运营商认证结果查询
	 */
	@Override
	public void queryMobileAuth() {
	    //查询未获取认证结果的用户
        List<UserMobileAuthLog> list = userMobileAuthLogDao.getUnAuthUserList();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        for (UserMobileAuthLog authLog: list) {
            if(authLog == null
                    || authLog.getUserId() == null || StringUtils.isEmpty(authLog.getUserId())
                    || authLog.getMobile() == null || StringUtils.isEmpty(authLog.getMobile())){
                continue;
            }
            try {
                Map<String,Object> postMap =
                        chuQiYouAuthUtil.AuthQuery(authLog.getUserId(), authLog.getMobile());
                if(postMap != null && postMap.get("code") != null){
                    if(StringUtils.equals("0000", postMap.get("code").toString())){
                        //认证成功
                        authLog.setNotify_json(JSON.toJSONString(postMap));
                        authLog.setAuth_status(2); //成功
                        userMobileAuthLogDao.updateMobileAuth(authLog);
                    }else if(StringUtils.equals("0106", postMap.get("code").toString())){
                        //认证失败
                        authLog.setNotify_json(JSON.toJSONString(postMap));
                        authLog.setAuth_status(9); //失败
                        userMobileAuthLogDao.updateMobileAuth(authLog);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	/**
	 * 获取运营商原始数据
	 */
	@Override
	public void queryOperatorOriginalData() {
		//查询未获取认证结果的用户
//		List<BorrowUser> list = userDao.getNeedOperatorOriginalDataList();
//		if(CollectionUtils.isEmpty(list)){
//			return;
//		}
//		for (BorrowUser borrowUser: list) {
//			if(borrowUser == null
//					|| borrowUser.getUserPhone() == null || StringUtils.isEmpty(borrowUser.getUserPhone())){
//				continue;
//			}
			try {
//				Map<String,Object> postMap = chuQiYouAuthUtil.queryOperatorOriginalData(borrowUser.getUserPhone());
				Map<String,Object> postMap = chuQiYouAuthUtil.queryOperatorOriginalData("13817400429");
				if(postMap != null && postMap.get("code") != null){
					if(StringUtils.equals("0000", postMap.get("code").toString())){
						//成功
					}else {
						//失败
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
//		}
	}


}
