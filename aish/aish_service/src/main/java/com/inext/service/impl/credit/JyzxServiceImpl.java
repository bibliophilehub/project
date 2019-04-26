package com.inext.service.impl.credit;

import com.inext.entity.BorrowUser;
import com.inext.entity.CreditName;
import com.inext.entity.RiskCreditUser;
import com.inext.service.CreditGatherService;
import com.inext.utils.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获得91征信数据
 *
 * @Author: jzhang
 * @Date: 2018-03-29 0028 下午 02:24
 */
@Service
public class JyzxServiceImpl extends CreditGatherService {


    private static Logger logger = LoggerFactory.getLogger(JyzxServiceImpl.class);

    @Override
    protected CreditName getCreditName() {
        return CreditName.JYZX;
    }

    @Override
    protected int run(BorrowUser user) {
        return this.doGetCredit();
    }

    @Override
    protected void doSuc(String result, Map<String, Object> params, Object... values) {
        JSONObject jsonObject = JSONObject.fromObject(result);
        String loanInfos = jsonObject.getString("loanInfos");
        Integer jyLoanNum = 0;//'91征信借款总数',
        Integer jyJdNum = 0;//'91征信拒单记录数',
        BigDecimal jyJdBl = BigDecimal.ZERO;//'91征信拒单记录占比',
        Integer jyOverNum = 0;//'91征信逾期记录数、逾期金额大于0或者状态是逾期',
        BigDecimal jyOverBl = BigDecimal.ZERO;//'91征信逾期记录占比',x
        if(StringUtils.isNotEmpty(loanInfos)&&!loanInfos.equals("[]")) {
            List<Map> list = (List) JSONArray.fromObject(loanInfos);
            if (list != null && list.size() > 0) {
                jyLoanNum = list.size();
                for (Map map : list) {
                    if (map.get("borrowState").toString().equals("1")) {
                        jyJdNum++;
                    }
                    int status = Integer.parseInt(map.get("repayState").toString());
                    if ((status != 0 && status != 1 && status != 9) || Integer.parseInt(map.get("arrearsAmount").toString()) > 0) {
                        jyOverNum++;
                    }
                    jyJdBl = new BigDecimal(jyJdNum).divide(new BigDecimal(jyLoanNum), 2, BigDecimal.ROUND_UP);
                    jyOverBl = new BigDecimal(jyOverNum).divide(new BigDecimal(jyLoanNum), 2, BigDecimal.ROUND_UP);
                }
            }
        }
        RiskCreditUser user = new RiskCreditUser(_riskCreditUserId);
        user.setJyLoanNum(jyLoanNum);
        user.setJyJdNum(jyJdNum);
        user.setJyJdBl(jyJdBl);
        user.setJyOverNum(jyOverNum);
        user.setJyOverBl(jyOverBl);
        iRiskCreditUserDao.updateByPrimaryKeySelective(user);
    }

    @Override
    protected void doException(Exception e, Map<String, Object> params, Object... values) {
        logger.error("_riskCreditUserId:"+_riskCreditUserId+",msg:"+e.getMessage());
    }
}
