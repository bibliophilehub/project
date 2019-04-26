package com.inext.service.impl.credit;

import com.inext.constants.Constants;
import com.inext.dao.IBorrowUserDao;
import com.inext.entity.*;
import com.inext.service.CreditGatherService;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IChannelService;
import com.inext.utils.WebClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分控数据获取
 *
 * @Author: jzhang
 * @Date: 2018-04-17 0028 下午 02:24
 */
@Service
public class FkServiceImpl extends CreditGatherService {
    private static Logger logger = LoggerFactory.getLogger(FkServiceImpl.class);

    @Resource
    IAssetBorrowOrderService iAssetBorrowOrderService;
    @Resource
    IChannelService channelService;
    @Autowired
    private IBorrowUserDao userDao;


    @Override
    protected CreditName getCreditName() {
        return CreditName.FK;
    }

    @Override
    protected int run(BorrowUser user) {
        Map<String, Object> params = new HashMap<>();
        AssetBorrowOrder order = iAssetBorrowOrderService.getOrderById(_riskCreditUser.getAssetId());
        params.put("serialId", _riskCreditUserId);//流水号，回调时会带上
        params.put("deviceType", order.getDeviceType() + "");//设备类型(android,ios,h5)
        params.put("platformName", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK).get("FK_ZX_NAME"));//平台名称(piggy,junyi等)
        params.put("businessName", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK).get("FK_ZX_NAME"));//商户类型，如果是单商户主体，名字与platformName相同
        params.put("jxlToken", _riskCreditUser.getJxlToken() + "");//type为JXL时为必填

        String channelName = "";
        if (user.getChannelId().intValue() == 0) {
            channelName = "花小侠";
        } else {
            ChannelInfo channel = channelService.getChannelById(user.getChannelId());
            if (null != channel && StringUtils.isNotEmpty(channel.getChannelName())) {
                channelName = channel.getChannelName();
            } else {
                channelName = "未知渠道";
            }
        }
        params.put("channelName", channelName);
        params.put("customerType", user.getIsOld());

        //以上必填
        params.put("amount", order.getMoneyAmount().intValue());//借款申请金额
        params.put("deviceType", order.getDeviceType() + "");//设备id类型(安卓imei，ios的mac地址等)
//        params.put("deviceIdType",order.getDeviceNumber());//设备id类型(安卓imei，ios的mac地址等)
        params.put("deviceId", order.getDeviceNumber());//设备id类型(安卓imei，ios的mac地址等)

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", _userId);
        List<Map<String, String>> list = userDao.queryBorrowUser(map);
        Map<String, String> resultMap = list.get(0);

        params.put("education", user.getUserEducation());//教育程度
        params.put("ip", _riskCreditUser.getIp());//ip
        params.put("address", resultMap.get("userProvince") + resultMap.get("userCity") + resultMap.get("userArea") + resultMap.get("userAddress"));//居住地址

        if (StringUtils.isNotEmpty(user.getZhimaScore()))
            params.put("zmScore", Integer.parseInt(user.getZhimaScore()));//芝麻
        List<Map<String, String>> lxrlist = iRiskCreditUserDao.getContacts(_userId);
        if (lxrlist != null && lxrlist.size() > 0) {
            params.put("contacts", JSONArray.fromObject(lxrlist));// JsonArray 联系人列表
        }


        //覆盖征信url，风控的 url 和征信不一样
        _url = "http://" +
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK).get("FK_HOST") + ":" +
                Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK).get("FK_PORT") + "/api/credit";
        return this.doGetCredit(params);
    }

    /**
     * 发送命令 需要特殊处理的重写此方法
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    protected String doSend(String url, Map<String, Object> params) throws Exception {
        Map<String, String> keys = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FK);
        String plainCredentials = keys.get("FK_NAME") + ":" + keys.get("FK_PWD");
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        String jsonStrData = JSONObject.fromObject(params).toString();
        return new WebClient().postJsonData(_url, jsonStrData, new HashMap() {{
            put("Authorization", "Basic " + base64Credentials);
        }});
    }

    @Override
    protected void doSuc(String results, Map<String, Object> params, Object... values) {
        JSONObject jsonObject = JSONObject.fromObject(results);
        String code = jsonObject.getString("code");
        if (code.equals("200")) {
            jsonObject = jsonObject.getJSONObject("obj");
            String riskId = jsonObject.getString("riskId");
            RiskCreditUser user = new RiskCreditUser(_riskCreditUserId);
            user.setFkStatus(1);
            user.setRiskId(riskId);
            iRiskCreditUserDao.updateByPrimaryKeySelective(user);
        }

    }

    @Override
    protected void doException(Exception e, Map<String, Object> params, Object... values) {
        logger.error("_riskCreditUserId:" + _riskCreditUserId + ",msg:" + e.getMessage());
    }
}
