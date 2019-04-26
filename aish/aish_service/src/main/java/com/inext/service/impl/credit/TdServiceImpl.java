package com.inext.service.impl.credit;

import com.inext.entity.BorrowUser;
import com.inext.entity.CreditName;
import com.inext.entity.RiskCreditUser;
import com.inext.service.CreditGatherService;
import com.inext.utils.ConstantRisk;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 获得同盾数据
 *
 * @Author: jzhang
 * @Date: 2018-03-29 0028 下午 02:24
 */
@Service
public class TdServiceImpl extends CreditGatherService {

    private static Logger logger = LoggerFactory.getLogger(TdServiceImpl.class);

    @Override
    protected CreditName getCreditName() {
        return CreditName.TD;
    }

    @Override
    protected int run(BorrowUser user) {
        return this.doGetCredit(null);
    }

    @Override
    protected void doSuc(String result, Map<String, Object> params, Object... values) {
        JSONObject jsonObject = JSONObject.fromObject(result);
        if (jsonObject.getBoolean("success")) {
            BigDecimal tdScore = new BigDecimal(jsonObject.getString("final_score"));
            JSONArray jsonArray = jsonObject.getJSONArray("risk_items");

            Integer tdPhoneBlack = 0;//'同盾手机网贷黑名单个数',
            Integer tdCardNumBlack = 0;//'同盾身份证号网贷黑名单个数',
            if (jsonArray != null && jsonArray.size() >= 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    String itemName = jsonObject2.getString("item_name");
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("item_detail");
                    if (ConstantRisk.TD_PHONE_BLACK.equals(itemName)) {
                        tdPhoneBlack = jsonObject3.getJSONArray("namelist_hit_details").size();
                    } else if (ConstantRisk.TD_CARD_BLACK.equals(itemName)) {
                        tdCardNumBlack = jsonObject3.getJSONArray("namelist_hit_details").size();
                    }
                }
            }

            RiskCreditUser user = new RiskCreditUser(_riskCreditUserId);
            user.setTdScore(tdScore);
            user.setTdPhoneBlack(tdPhoneBlack);
            user.setTdCardNumBlack(tdCardNumBlack);
            iRiskCreditUserDao.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    protected void doException(Exception e, Map<String, Object> params, Object... values) {
        logger.error("_riskCreditUserId:"+_riskCreditUserId+",msg:"+e.getMessage());
    }
}
