package com.inext.service;

import com.inext.entity.BorrowUser;
import com.inext.result.ApiServiceResult;

import java.util.HashMap;

public interface IJxlService {

    /**
     * 获得运营商的token，其他操作需按照token参数
     *
     * @param params 必要参数：<br>
     *               userName用户姓名<br>
     *               cardNum用户身份证号码<br>
     *               userPhone用户手机号码<br>
     *               userId用户主键ID<br>
     * @return 500是是服务器异常，本地代码错误或者连接异常<br>
     * 400是必要参数缺失<br>
     * 300是解析数据出错<br>
     * 仅当code=200时，msg是获得的token，ext是website(手机运营商)<br>
     * 100时，请求成功，返回数据无效<br>
     */
    public ApiServiceResult getToken(BorrowUser bu);

    /**
     * 获取运营商验证码。
     *
     * @param params 必要参数：<br>
     *               userPhone用户手机号码<br>
     *               mobilePwd用户用户手机服务密码<br>
     *               token运营商令牌<br>
     *               userId用户主键ID<br>
     *               非必要参数：<br>
     *               queryPwd北京移动用户查询密码
     * @return 500是是服务器异常，本地代码错误或者连接芝麻信用异常<br>
     * 400是必要参数缺失<br>
     * 300是请求成功发送到运营商，但未被正常解析或其他异常<br>
     * 仅当code=200时，发送验证码成功<br>
     * 100时是用户不需要出入验证码，直接进行数据采集<br>
     */
    public ApiServiceResult getCaptcha(HashMap<String, Object> params);

    /**
     * 提交运营商验证码(北京移动需要参数queryPwd)，提交验证码，则开始采集数据
     *
     * @param params 必要参数：<br>
     *               userPhone用户手机号码<br>
     *               mobilePwd用户用户手机服务密码<br>
     *               token运营商令牌<br>
     *               userId用户主键ID<br>
     *               非必要参数：<br>
     *               queryPwd北京移动用户查询密码
     * @return 500是是服务器异常，本地代码错误或者连接芝麻信用异常<br>
     * 400是必要参数缺失<br>
     * 300是请求成功发送到运营商，但未被正常解析或其他异常(其中包括验证码失效)<br>
     * 仅当code=200时，提交验证码成功，开始数据采集<br>
     * 100时是需要输入验证码<br>
     */
    public ApiServiceResult applyCollect(HashMap<String, Object> params);
}
