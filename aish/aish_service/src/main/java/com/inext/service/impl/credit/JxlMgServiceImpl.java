package com.inext.service.impl.credit;

import com.inext.entity.BorrowUser;
import com.inext.entity.CreditName;
import com.inext.entity.RiskCreditUser;
import com.inext.service.CreditGatherService;
import com.inext.utils.StringUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 聚信立密罐
 *
 * @Author: jzhang
 * @Date: 2018-03-29 0028 下午 02:24
 */
@Service
public class JxlMgServiceImpl extends CreditGatherService {


    private static Logger logger = LoggerFactory.getLogger(JxlMgServiceImpl.class);

    @Override
    protected CreditName getCreditName() {
        return CreditName.MG;
    }

    @Override
    protected int run(BorrowUser user) {
        return this.doGetCredit();
    }

    @Override
    protected void doSuc(String result, Map<String, Object> params, Object... values) {
        JSONObject jsonObject = JSONObject.fromObject(result);
        jsonObject = jsonObject.getJSONObject("data");
        jsonObject = jsonObject.getJSONObject("user_gray");
        if(!jsonObject.containsKey("phone_gray_score")){
            RiskCreditUser user = new RiskCreditUser(_riskCreditUserId);
            user.setMgBlackScore(0);
            iRiskCreditUserDao.updateByPrimaryKeySelective(user);
        }
        String phone_gray_score = jsonObject.getString("phone_gray_score");
        if (StringUtils.isNotEmpty(phone_gray_score)) {
            RiskCreditUser user = new RiskCreditUser(_riskCreditUserId);
            user.setMgBlackScore(Integer.parseInt(phone_gray_score));
            iRiskCreditUserDao.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    protected void doException(Exception e, Map<String, Object> params, Object... values) {
        logger.error("_riskCreditUserId:"+_riskCreditUserId+",msg:"+e.getMessage());
    }
}
