package com.inext.service;


import com.inext.constants.Constants;
import com.inext.dao.ICreditGatherDataLogDao;
import com.inext.dao.IRiskCreditUserDao;
import com.inext.entity.*;
import com.inext.service.impl.credit.JxlTokenServiceImpl;
import com.inext.utils.WebClient;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取征信数据抽象类
 *
 * @Author: jzhang
 * @Date: 2018-03-28 0028 下午 12:14
 */
public abstract class CreditGatherService {
    private static Logger logger = LoggerFactory.getLogger(JxlTokenServiceImpl.class);
    protected int _riskCreditUserId = 0;//解析数据存储id
    protected RiskCreditUser _riskCreditUser;//解析数据存储id
    protected int _userId = 0;
    protected String _url = "http://{host}:{port}/api/credit_info/report";//run 初始化
    @Resource
    protected ICreditGatherDataLogDao iCreditGatherDataLogDao;
    @Resource
    protected IRiskCreditUserDao iRiskCreditUserDao;
    
    private Map<String, Object> _params = new HashMap();
    private boolean is_override_send=true;//send方法是否被重写



    /**
     * 定时器调用方法
     */
    public int run(BorrowUser user, RiskCreditUser riskCreditUser) {
        if (riskCreditUser != null) {
            this._riskCreditUserId = riskCreditUser.getId();
            this._riskCreditUser = riskCreditUser;

        } else {
            return 0;
        }
        if (user != null) {
            //4个必要参数
            this._params.put("type", getCreditName().toString());
            this._params.put("name", user.getUserName());
            this._params.put("phoneNum", user.getUserPhone().toString());
            this._params.put("idCard", user.getUserCardNo().toString());
            this._userId = user.getId();
        } else {
            return 0;
        }
        _url = "http://"+
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK).get("FK_HOST")+ ":"+
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK).get("FK_PORT")+"/api/credit_info/report";
        try {
            return run(user);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 获取征信类型
     *
     * @return
     */
    protected abstract CreditName getCreditName();

    /**
     * 带参 定时器调用方法
     */
    protected abstract int run(BorrowUser user);


    protected int doGetCredit() {
        return doGetCredit(null);
    }

    /**
     * 获取征信数据
     *
     * @param params url 参数
     * @param values 非必要参数 需要回传到 doSuc or doException 的参数
     */
    protected int doGetCredit(Map<String, Object> params, Object... values) {
        logger.info("+++++++++++++++++" + getName() + ":begin++++++++++++++");
        if (params != null) {
            this._params.putAll(params);//如果有传入参数 以传入参数为主
        }
        String result = null;
        String ys_result = null;
        Integer code = 100;
        try {
            result = doSend(this._url, this._params);
            ys_result=result;
            if(is_override_send){
                //如果是重写的发送方法 不是默认的url就直接调成功
                doSuc(result, this._params, values);
            }else {
                JSONObject jsonObject = JSONObject.fromObject(result);
                if(jsonObject.containsKey("code")){
                    code = jsonObject.getInt("code");
                    if (code.equals(200)) {
                        jsonObject = jsonObject.getJSONObject("obj");
                        result = jsonObject.getString("report");
                        doSuc(result, this._params, values);
                    }
                }
            }
        } catch (Exception e) {
            doException(e, this._params, values);
        }

        try {//保存征信数据原始
            CreditGatherDataLog creditGatherDataLog = new CreditGatherDataLog();
            creditGatherDataLog.setCode(code);
            creditGatherDataLog.setRiskCreditUserId(this._riskCreditUserId);
            creditGatherDataLog.setData(ys_result);
            creditGatherDataLog.setServiceImpl(getName());
            iCreditGatherDataLogDao.insertSelective(creditGatherDataLog);
        } catch (Exception e) {
            logger.error("保存征信数据原始数据失败：" + e.getMessage());
        }
        logger.info("+++++++++++++++++" + getName() + ":end++++++++++++++");
        if(code.equals(50003)){
            code=200;//50003请求异常 不需要再次发起 所以 直接放回200
        }
        return code;
    }

    /**
     * 发送命令 需要特殊处理的重写此方法
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    protected String doSend(String url, Map<String, Object> params) throws Exception {
        is_override_send=false;//调用默认方法 认为没有被重写
        Map<String, String> keys = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK);
        String plainCredentials = keys.get("FK_ZX_NAME")+":"+keys.get("FK_ZX_PWD");
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        String jsonStrData=JSONObject.fromObject(params).toString();
        return new WebClient().postJsonData(_url,jsonStrData,new HashMap(){{put("Authorization", "Basic " + base64Credentials);}});
    }

    /**
     * 请求成功回调
     *
     * @param result 数据结果集
     * @param params 请求数据参数
     * @param values 回传参数集
     * @throws Exception
     */
    protected abstract void doSuc(String result, Map<String, Object> params, Object... values) throws Exception;

    /**
     * 请求失败回调
     *
     * @param e      Exception
     * @param params 请求数据参数
     * @param values 回传参数集
     * @throws Exception
     */
    protected abstract void doException(Exception e, Map<String, Object> params, Object... values);

    /**
     * 获取当前子类
     *
     * @return
     */
    private String getName() {
        return getClass().getSimpleName();
    }
}
