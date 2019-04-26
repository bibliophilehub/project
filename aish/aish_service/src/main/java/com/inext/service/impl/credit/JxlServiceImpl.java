package com.inext.service.impl.credit;

import com.inext.entity.BorrowUser;
import com.inext.entity.CreditName;
import com.inext.entity.RiskCreditUser;
import com.inext.service.CreditGatherService;
import com.inext.utils.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 聚信立
 *
 * @Author: jzhang
 * @Date: 2018-03-28 0028 下午 02:24
 */
@Service("credit/jxlService")
public class JxlServiceImpl extends CreditGatherService {


    private static Logger logger = LoggerFactory.getLogger(JxlServiceImpl.class);
    private String token = "";

    @Override
    protected CreditName getCreditName() {
        return CreditName.JXL;
    }

    @Override
    protected int run(BorrowUser user) {
        return this.doGetCredit(new HashMap() {{
            put("jxlToken", _riskCreditUser.getJxlToken());
        }});
    }

    @Override
    protected void doSuc(String result, Map<String, Object> params, Object... values) {
        JSONObject jsonObject = JSONObject.fromObject(result);
        String jxlRptId;//'报告编号',
        String jxlUpdateTime;//'报告生成时间',
        String jxlGender = null;//'性别',
        Integer jxlAge = null;//'年龄',
        String jxlKeyValueName = null;//'姓名',
        String jxlKeyValueCard = null;//'身份证号',
        String jxlPhoneRegDays=null;//手机号使用时间
        String jxlJmDayNum=null;//总静默天数
        String jxlHtPhoneNum=null;//通讯联系人互通电话数量
        BigDecimal yjHf = BigDecimal.ZERO;//聚信立月均话费
        int yjHfHelp = 0;
        String jxlCheckBlackInfo = null;//'用户黑名单信息',
        String jxlEbusinessExpense = null;//'电商月消费',
        String jxlDeliverAddress = null;//'电商地址分析',
        String jxlBehaviorCheck = null;//'运营商信息(用户行为检测)',
        String jxlCollectionContact = null;//'联系人信息(联系人名单)',
        String jxlContactList = null;//'通话数据分析(运营商联系人通话详情)',
        String jxlContactRegion = null;//'联系人所在地区(联系人区域汇总)',
        String jxlCellBehavior = null;//'月使用情况(运营商数据整理)',
        String jxlMainService = null;//'服务企业类型(常用服务)',
        String jxlTripInfo = null;//'出行数据分析',
        String jxlUserInfoCheck = null;//'报告被查询情况(用户信息检测)',
        if (jsonObject.containsKey("report_data")) {
            JSONObject reportData = jsonObject.getJSONObject("report_data");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            jsonObject = reportData.getJSONObject("report");
            jxlRptId = jsonObject.get("rpt_id").toString();
            jxlUpdateTime = jsonObject.get("update_time").toString();

            JSONArray jsonArray = reportData.getJSONArray("application_check");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                JSONObject jsonObject4 = jsonObject3.getJSONObject("check_points");
                String appPoint = jsonObject3.getString("app_point");
                if (appPoint.equalsIgnoreCase("user_name")) {
                    jxlKeyValueName = jsonObject4.getString("key_value");
                }else if (appPoint.equalsIgnoreCase("id_card")) {
                    jxlGender = jsonObject4.getString("gender");
                    jxlAge = Integer.parseInt(jsonObject4.getString("age").toString());
                    jxlKeyValueCard = jsonObject4.getString("key_value");
                }else if ("cell_phone".equals(appPoint)) {
                    if (jsonObject4.containsKey("reg_time")) {
                        String regTime = jsonObject4.getString("reg_time");
                        if (StringUtils.isNotBlank(regTime)) {
                            try {
                                Date end = sdf.parse(regTime.substring(0, 10));
                                jxlPhoneRegDays = DateUtil.daysBetween(end, new Date())+"";
                            } catch (ParseException e) {
                            }
                        }
                    }
                }
            }
            if(reportData.containsKey("user_info_check")) {
                if (reportData.getJSONObject("user_info_check").containsKey("check_black_info")) {
                    jxlCheckBlackInfo = reportData.getJSONObject("user_info_check").get("check_black_info").toString();

                }
            }

            if (reportData.containsKey("ebusiness_expense")) {
                jxlEbusinessExpense = reportData.get("ebusiness_expense").toString();
            }

            if (reportData.containsKey("deliver_address")) {
                jxlDeliverAddress = reportData.get("deliver_address").toString();
            }

            if (reportData.containsKey("deliver_address")) {
                jxlDeliverAddress = reportData.get("deliver_address").toString();
            }

            if (reportData.containsKey("behavior_check")) {
                jxlBehaviorCheck = reportData.get("behavior_check").toString();
                jsonArray = reportData.getJSONArray("behavior_check");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                    String appPoint = jsonObject3.getString("check_point");
                    String evidence = jsonObject3.getString("evidence");
                    if (appPoint.equalsIgnoreCase("contact_each_other")) {
                        String flag = "互通过电话的号码有";
                        String flag2 = "个";
                        int index = evidence.indexOf(flag);
                        if (index >= 0) {
                            int index2 = evidence.indexOf(flag2);
                            jxlHtPhoneNum = evidence.substring(index + flag.length(), index2);
                        }
                    } else if (appPoint.equalsIgnoreCase("phone_silent")) {
                        String results = jsonObject3.getString("result");
                        String flag0 = "天内有";
                        if (results.indexOf(flag0) >= 0) {
                            String[] array = results.split(flag0);
                            if (array != null && array.length > 0) {
                                String flag = "天";
                                for (int j = 0; j < array.length; j++) {
                                    String tmp = array[j];
                                    if (j == 0) {
//                                        jxlTotal += Integer.valueOf(tmp);
                                    } else {
                                        int index = tmp.indexOf(flag);
                                        if (index >= 0) {
                                            jxlJmDayNum =tmp.substring(0, index);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }

            if (reportData.containsKey("collection_contact")) {
                jxlCollectionContact = reportData.get("collection_contact").toString();
            }

            if (reportData.containsKey("contact_list")) {
                jxlContactList = reportData.get("contact_list").toString();
            }

            if (reportData.containsKey("contact_region")) {
                jxlContactRegion = reportData.get("contact_region").toString();
            }

            if (reportData.containsKey("cell_behavior")) {
                jxlCellBehavior = reportData.get("cell_behavior").toString();
                jsonArray = reportData.getJSONArray("cell_behavior");
                if (jsonArray != null && jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        JSONArray jsonArray2 = jsonObject3.getJSONArray("behavior");
                        for (int j = 0; j < jsonArray2.size(); j++) {
                            JSONObject jsonObject4 = jsonArray2.getJSONObject(j);
                            String amount = jsonObject4.getString("total_amount");
                            if (!"-1".equals(amount)) {
                                yjHfHelp += 1;
                                yjHf = yjHf.add(new BigDecimal(amount));
                            }
                        }
                    }
                }
                if (yjHfHelp != 0) {
                    yjHf = yjHf.divide(new BigDecimal(yjHfHelp), 2, BigDecimal.ROUND_HALF_UP);
                }
            }

            if (reportData.containsKey("main_service")) {
                jxlMainService = reportData.get("main_service").toString();
            }

            if (reportData.containsKey("trip_info")) {
                jxlTripInfo = reportData.get("trip_info").toString();
            }

            if (reportData.containsKey("user_info_check")) {
                jxlUserInfoCheck = reportData.get("user_info_check").toString();
            }
            RiskCreditUser user = new RiskCreditUser(_riskCreditUserId);
            user.setJxlRptId(jxlRptId);
            user.setJxlUpdateTime(jxlUpdateTime);
            user.setJxlGender(jxlGender);
            user.setJxlAge(jxlAge);
            user.setJxlKeyValueName(jxlKeyValueName);
            user.setJxlKeyValueCard(jxlKeyValueCard);

            user.setJxlPhoneRegDays(jxlPhoneRegDays);
            user.setJxlJmDayNum(jxlJmDayNum);
            user.setJxlHtPhoneNum(jxlHtPhoneNum);
            user.setJxlYjHf(yjHf.toString());

            user.setJxlCheckBlackInfo(jxlCheckBlackInfo);
            user.setJxlEbusinessExpense(jxlEbusinessExpense);
            user.setJxlDeliverAddress(jxlDeliverAddress);
            user.setJxlBehaviorCheck(jxlBehaviorCheck);
            user.setJxlCollectionContact(jxlCollectionContact);
            user.setJxlContactList(jxlContactList);
            user.setJxlContactRegion(jxlContactRegion);
            user.setJxlCellBehavior(jxlCellBehavior);
            user.setJxlMainService(jxlMainService);
            user.setJxlTripInfo(jxlTripInfo);
            user.setJxlUserInfoCheck(jxlUserInfoCheck);
            iRiskCreditUserDao.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    protected void doException(Exception e, Map<String, Object> params, Object... values) {
        logger.error("_riskCreditUserId:"+_riskCreditUserId+",msg:"+e.getMessage());
    }
}
