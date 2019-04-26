package com.inext.service.impl.credit;

import com.inext.constants.Constants;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BorrowUser;
import com.inext.entity.CreditName;
import com.inext.entity.RiskCreditUser;
import com.inext.service.CreditGatherService;
import com.inext.utils.WebClient;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 聚信立token
 *
 * @Author: jzhang
 * @Date: 2018-03-28 0028 下午 02:24
 */
@Service
public class JxlTokenServiceImpl extends CreditGatherService {


    private static Logger logger = LoggerFactory.getLogger(JxlTokenServiceImpl.class);
    private String token = "";

    @Override
    protected CreditName getCreditName() {
        return CreditName.JXL;
    }

    @Override
    protected int run(BorrowUser user) {
        return this.doGetCredit();
    }

    @Override
    protected String doSend(String url,Map<String, Object> params) throws Exception{
        Object userName = params.get("name");
        Object cardNum = params.get("idCard");
        Object userPhone = params.get("phoneNum");
        Object userId = _userId;
        Object contacts = "";
        String result = "";
        // Object contactType = params.get("contactType");
        if (userName != null && cardNum != null && userPhone != null && userId != null) {
            Map<String, String> keys = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.JXL);
            String jxlUrl = keys.get("JXL_cj_url") + "orgApi/rest/v3/applications/" + keys.get("JXL_NAME");
            String cardNo = (String) cardNum;
            String uid = "";
            String value = "{\"selected_website\":[],\"basic_info\":{\"name\"	:\"" + userName + "\",\"id_card_num\":\"" + cardNo
                    + "\",\"cell_phone_num\":\"" + userPhone + "\"},\"skip_mobile\":" + false + ",\"uid\":\"" + uid + "\",\"contacts\":["
                    + contacts + "]}";
            HashMap<String, Object> times = new HashMap<String, Object>();
            times.put("soketOut", 180000);
            times.put("connOut", 180000);
            result= WebClient.getInstance().postJsonData(jxlUrl, value, times);
        }
        return result;
    }
    @Override
    protected void doSuc(String result, Map<String, Object> params, Object... values) {
        JSONObject jsonObject = JSONObject.fromObject(result);
        if ("65557".equals(jsonObject.getString("code"))) {
            jsonObject = jsonObject.getJSONObject("data");
            String token = jsonObject.getString("token");
            RiskCreditUser user = new RiskCreditUser(_riskCreditUserId);
            user.setJxlToken(token);
            iRiskCreditUserDao.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    protected void doException(Exception e, Map<String, Object> params, Object... values) {
        logger.error("_riskCreditUserId:"+_riskCreditUserId+",msg:"+e.getMessage());
    }
}
