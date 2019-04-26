package com.inext.service.impl.credit;

import com.inext.entity.BorrowUser;
import com.inext.entity.CreditName;
import com.inext.entity.RiskCreditUser;
import com.inext.service.CreditGatherService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * 获得中智诚反欺诈报告
 *
 * @Author: jzhang
 * @Date: 2018-03-30 0028 下午 02:24
 */
@Service
public class ZzcServiceImpl extends CreditGatherService {


    private static Logger logger = LoggerFactory.getLogger(ZzcServiceImpl.class);

    @Override
    protected CreditName getCreditName() {
        return CreditName.ZZC;
    }

    @Override
    protected int run(BorrowUser user) {
        return this.doGetCredit();
    }


    @Override
    protected void doSuc(String result, Map<String, Object> params, Object... values) {
        JSONObject jsonObject = JSONObject.fromObject(result);
        String risk_level ="";
        if(jsonObject.containsKey("risk_level")){
            risk_level=jsonObject.getString("risk_level");
        }else {
            JSONObject rule_result = jsonObject.getJSONObject("rule_result");
            risk_level = rule_result.getString("risk_level");
        }
        Integer zzcFqz = 3;
        if ("high".equals(risk_level)) {
            zzcFqz = 1;
        } else if ("medium".equals(risk_level)) {
            zzcFqz = 2;
        }
        RiskCreditUser user = new RiskCreditUser(_riskCreditUserId);
        user.setZzcFqz(zzcFqz);
        iRiskCreditUserDao.updateByPrimaryKeySelective(user);
    }

    @Override
    protected void doException(Exception e, Map<String, Object> params, Object... values) {
        logger.error("_riskCreditUserId:"+_riskCreditUserId+",msg:"+e.getMessage());
    }
}
