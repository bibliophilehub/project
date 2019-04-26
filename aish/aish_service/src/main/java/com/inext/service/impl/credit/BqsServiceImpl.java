package com.inext.service.impl.credit;

import com.inext.entity.BorrowUser;
import com.inext.entity.CreditName;
import com.inext.entity.RiskCreditUser;
import com.inext.service.CreditGatherService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 白骑士
 *
 * @Author: jzhang
 * @Date: 2018-03-30 0028 下午 02:24
 */
@Service
public class BqsServiceImpl extends CreditGatherService {
    private static Logger logger = LoggerFactory.getLogger(BqsServiceImpl.class);

    @Override
    protected CreditName getCreditName() {
        return CreditName.BQS;
    }

    @Override
    protected int run(BorrowUser user) {
        return this.doGetCredit();
    }

    @Override
    protected void doSuc(String results, Map<String, Object> params, Object... values) {
        Integer bqsBlack = 3;
        JSONObject jsonObject = JSONObject.fromObject(results);
        String result = jsonObject.getString("finalDecision");
        if ("Accept".equals(result)) {
            bqsBlack = 1;
        } else if ("Reject".equals(result)) {
            bqsBlack = 2;
        } else {
            // 建议人工审核
            bqsBlack = 3;
        }
        RiskCreditUser user = new RiskCreditUser(_riskCreditUserId);
        user.setBqsBlack(bqsBlack);
        iRiskCreditUserDao.updateByPrimaryKeySelective(user);
    }

    @Override
    protected void doException(Exception e, Map<String, Object> params, Object... values) {
        logger.error("_riskCreditUserId:"+_riskCreditUserId+",msg:"+e.getMessage());
    }
}
