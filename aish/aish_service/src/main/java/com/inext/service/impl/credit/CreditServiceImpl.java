package com.inext.service.impl.credit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inext.dao.*;
import com.inext.entity.*;
import com.inext.entity.chuqiyou.*;
import com.inext.result.ApiServiceResult;
import com.inext.service.CreditService;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBorrowUserService;
import com.inext.utils.DateUtil;
import com.inext.utils.DateUtils;
import com.inext.utils.JSONUtil;
import com.inext.utils.StringUtils;
import com.inext.utils.baofoo.ChuQiYouFkUtil;
import com.inext.utils.baofoo.PropertiesUtil;
import com.inext.utils.baofoo.util.HttpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: jzhang
 * @Date: 2018-03-28 0028 上午 10:59
 */
@Service
public class CreditServiceImpl implements CreditService {

    private static final Logger logger = LoggerFactory.getLogger(CreditServiceImpl.class);
    @Resource
    BqsServiceImpl bqsService;
    @Resource
    JxlMgServiceImpl jxlMgService;
    @Resource
    JxlServiceImpl jxlService;
    @Resource
    JyzxServiceImpl jyzxService;
    @Resource
    TdServiceImpl tdService;
    @Resource
    ZzcServiceImpl zzcFqzService;
    @Resource
    FkServiceImpl fkService;

    @Resource
    IBorrowUserService iBorrowUserService;

    @Resource
    IRiskCreditUserDao iRiskCreditUserDao;

    @Resource
    private IChannelDao channelDao;

    @Resource
    ChuQiYouFkUtil chuQiYouFkUtil;

    @Resource
    IOutOrdersDao iOutOrdersDao;

    @Resource
    IAssetBorrowOrderDao iAssetBorrowOrderDao;

    @Resource
    IAssetOrderStatusHisDao iAssetOrderStatusHisDao;

    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @Resource
    private IBorrowUserDao iBorrowUserDao;

    @Resource
    private IAssetRepaymentOrderDao iAssetRepaymentOrderDao;

    @Resource
    private CreditService creditService;

    @Override
    @Deprecated
    public void doOrderCredit() {
//        List<RiskCreditUser> list =iRiskCreditUserDao.getStatusIs0Top500();
//        if (list != null && list.size() > 0) {
//            for (RiskCreditUser riskCreditUser : list) {
//                BorrowUser user = iBorrowUserService.getBorrowUserById(riskCreditUser.getUserId());
//                int bqs =riskCreditUser.getBqsBlack()==null?bqsService.run(user, riskCreditUser):200;
//                int jxlMg = riskCreditUser.getMgBlackScore()==null?jxlMgService.run(user, riskCreditUser):200;
//                int jyzx = riskCreditUser.getJyJdBl()==null?jyzxService.run(user, riskCreditUser):200;
//                int td = riskCreditUser.getTdScore()==null?tdService.run(user, riskCreditUser):200;
//                int zzc = riskCreditUser.getZzcFqz()==null?zzcFqzService.run(user, riskCreditUser):200;
//                if ((bqs == 200 && jxlMg == 200 && jyzx == 200 && td == 200 && zzc == 200)) {
//                    //防止覆盖
//                    riskCreditUser.setStatus(1);
//                    riskCreditUser.setJxlStatus(null);
//                    riskCreditUser.setFkStatus(null);
//                    iRiskCreditUserDao.updateByPrimaryKeySelective(riskCreditUser);
//                }
//            }
//        }
    }


    @Override
    public void doGetJxlReport() {
        List<RiskCreditUser> list = iRiskCreditUserDao.getJxlStatusIs0Top500();
        if (list != null && list.size() > 0) {
            for (RiskCreditUser riskCreditUser : list) {
                BorrowUser user = iBorrowUserService.getBorrowUserById(riskCreditUser.getUserId());
                int res = jxlService.run(user, riskCreditUser);
                if (res == 200) {
                    //防止覆盖
                    riskCreditUser.setStatus(null);
                    riskCreditUser.setJxlStatus(1);
                    riskCreditUser.setFkStatus(null);
                    iRiskCreditUserDao.updateByPrimaryKeySelective(riskCreditUser);
                }
            }
        }
    }
//
//    @Override
//    public void doOrderCredit() {
//    }

    @Override
    public void doOrderRisk() {
        List<RiskCreditUser> list = iRiskCreditUserDao.getFkStatusIs0Top500();
        if (list != null && list.size() > 0) {
            for (RiskCreditUser riskCreditUser : list) {
                BorrowUser user = iBorrowUserService.getBorrowUserById(riskCreditUser.getUserId());
                int code = fkService.run(user, riskCreditUser);//这里 只有调用 状态由fkService修改
//                if(code==200){
//                    //防止覆盖
//                    riskCreditUser.setStatus(null);
//                    riskCreditUser.setJxlStatus(null);
//                    riskCreditUser.setFkStatus(1);
//                    iRiskCreditUserDao.updateByPrimaryKeySelective(riskCreditUser);
//                }
            }
        }
    }

    /**
     * 出其右风控
     *
     * @param user
     * @param riskCreditUser
     * @return
     * @throws Exception
     */
    public Map<String, Object> chuqiyouFk(BorrowUser user, RiskCreditUser riskCreditUser) throws Exception {

        //提交无其右风控参数
        ChuqiyouFk_riskData riskData = new ChuqiyouFk_riskData();

        ChuqiyouFk_riskData_present rd_present = new ChuqiyouFk_riskData_present();
        rd_present.setCity(user.getUserCityStr());
        rd_present.setProvince(user.getUserProvinceStr());
        rd_present.setLiveAddr(user.getUserAddress());

        riskData.setMobile(user.getUserPhone());
        riskData.setIdNum(user.getUserCardNo());
        riskData.setGender(getUserSex(user.getUserCardNo()));
        riskData.setApply_time(DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss"));
        riskData.setPresent(rd_present);

        List<Map<String, String>> lxrlist = iRiskCreditUserDao.getContactsForWuqiyou(user.getId());
        if (lxrlist != null && lxrlist.size() > 0) {
            List<ChuqiyouFk_riskData_contacts> contacts_list = new ArrayList<ChuqiyouFk_riskData_contacts>();
            for (int i = 0; i < lxrlist.size(); i++) {
                ChuqiyouFk_riskData_contacts rd_contacts = new ChuqiyouFk_riskData_contacts();
                rd_contacts.setContact_name(lxrlist.get(i).get("contact_name"));
                rd_contacts.setContact_phone(lxrlist.get(i).get("contact_phone"));
                rd_contacts.setUpdate_time(lxrlist.get(i).get("update_time"));
                contacts_list.add(rd_contacts);
            }

            riskData.setContact(JSONArray.parseArray(JSONObject.toJSONString(contacts_list)));
        }

        List<Map<String, String>> jj_lxrlist = iRiskCreditUserDao.getContacts(user.getId());

        if (jj_lxrlist != null && jj_lxrlist.size() >= 2) {
            List<ChuqiyouFk_riskData_emergency_contacts> emergency_list = new ArrayList<ChuqiyouFk_riskData_emergency_contacts>();
            for (int i = 0; i < 2; i++) {
                ChuqiyouFk_riskData_emergency_contacts rd_em = new ChuqiyouFk_riskData_emergency_contacts();
                rd_em.setName(jj_lxrlist.get(i).get("name"));
                rd_em.setPhone(jj_lxrlist.get(i).get("phone"));
                emergency_list.add(rd_em);
            }
            riskData.setEmergency_contacts(emergency_list);
        }

        ChuqiyouFk_p fk_p = new ChuqiyouFk_p();
        fk_p.setOrderId(String.valueOf(riskCreditUser.getAssetId()));
        fk_p.setUserName(user.getUserName());
        fk_p.setCardNum(user.getUserCardNo());
        fk_p.setMobile(user.getUserPhone());
        fk_p.setRiskData(riskData);

        //插入风控记录查询
        OutOrders out_orders = new OutOrders();
        out_orders.setUserId(String.valueOf(user.getId()));
        out_orders.setAssetOrderId(riskCreditUser.getAssetId());
        out_orders.setOrderType("WUQIYOU_FK");
        out_orders.setOrderNo("WQY_" + String.valueOf(riskCreditUser.getAssetId()));
        out_orders.setAct("CreditServiceImpl.chuqiyouFk");
        System.out.print("提交的out_orders=" + JSONObject.toJSONString(fk_p.getRiskData()));
//        out_orders.setReqParams(JSONObject.toJSONString(fk_p.getRiskData()));
        out_orders.setAddTime(new Date());
        out_orders.setStatus("0"); //初始化
        iOutOrdersDao.insert(out_orders);

        Map<String, Object> postMap = chuQiYouFkUtil.doFk(fk_p);

        return postMap;
    }

    @Override
    public void doWuqiyouOrderRisk() throws Exception {
        List<RiskCreditUser> list = iRiskCreditUserDao.getFkStatusIs0Top500();

        if (list != null && list.size() > 0) {
            // 查询需要拒绝的渠道
            Map<String, String> mapRefuse = backConfigParamsService.getBackConfig("CHANNEL_REFUSE", null);
            //拒绝总开关是否开启
            String refuseSwitch =
                    StringUtils.isNotEmpty(mapRefuse.get("CREDIT_REF_SWITCH"))
                            ? mapRefuse.get("CREDIT_REF_SWITCH")
                            : null;
            //取渠道id
            List<String> refuseIds = null;
            if (StringUtils.isNotEmpty(mapRefuse.get("CHANNEL_REF"))) {
                logger.info("风控---直接拒绝---渠道ids：" + mapRefuse.get("CHANNEL_REF").toString());
                refuseIds = Arrays.asList(mapRefuse.get("CHANNEL_REF").split(","));
            }
            for (RiskCreditUser riskCreditUser : list) {
                BorrowUser user = iBorrowUserService.getUserByIdForAddress(riskCreditUser.getUserId());

                if(user == null){
                    continue;
                }
                if(StringUtils.isNotEmpty(refuseSwitch) && StringUtils.equals("1", refuseSwitch)){
                    //拒绝总开关为1，则说明需要所有渠道都直接拒绝
                    logger.info("风控--拒绝总开关为[" + refuseSwitch + "]--拒绝----"
                            + user.getChannelId() + "用户:" + user.getUserAccount() + "," + user.getUserName());
                    //直接拒绝处理
                    refuseUpdate(riskCreditUser, "本地设置拒绝总开关为["+refuseSwitch+"]直接拒绝");
                    //跳过
                    continue;
                }
                //部分渠道有问题临时处理,后面要去掉---20190109
                if (user.getChannelId() != null && refuseIds != null && refuseIds.contains(user.getChannelId().toString())) {
                    logger.info("风控--拒绝--" + user.getChannelId() + "用户:" + user.getUserAccount() + "," + user.getUserName());
                    //直接拒绝处理
                    refuseUpdate(riskCreditUser, "本地设置拒绝渠道id直接拒绝");
                    //跳过
                    continue;
                }

                //平台自建黑白名单用户处理:platForm:2-花小侠
                if(StringUtils.isNotEmpty(user.getUserPhone()) && StringUtils.isNotEmpty(user.getUserName())) {
                    String isBlackWhiteUser = creditService.getIsBlackWhiteUser(user.getUserPhone(), user.getUserName(), "2");
                    if (StringUtils.isNotEmpty(isBlackWhiteUser)) {
                        if (StringUtils.equals("black", isBlackWhiteUser)) {
                            //如果为黑名单则直接拒绝
                            refuseUpdate(riskCreditUser, "本地设置黑名单用户直接拒绝");
                            continue;
                        }
                        if (StringUtils.equals("white", isBlackWhiteUser)) {
                            //如果为白名单则直接通过
                            passUpdate(riskCreditUser, "本地设置白名单用户直接通过");
                            continue;
                        }
                    }
                }
                /*---2019-01-30---------------------老用户风控规则调整添加正常还款次数和逾期判断------------------------------*/
                if(user.getId() != null && user.getId() > 0){
                    if(!checkSend(user.getId())){
                        passUpdate(riskCreditUser,"正常还款次数超过1次直接通过");
                        //跳过
                        continue;
                    }
                }

                //部分渠道用户设置渠道分
                List<Map<String, String>> scoreList = channelDao.queryScore(user.getId());
                if (CollectionUtils.isNotEmpty(scoreList)) {
                    AssetBorrowOrder order = iAssetBorrowOrderDao.getOrderById(riskCreditUser.getAssetId());
                    Map<String, String> map = scoreList.get(0);
                    if (map.containsKey("score")) {
                        BigDecimal score = new BigDecimal(String.valueOf(map.get("score")));
                        //本地渠道分控分数设置为0，直接通过
                        if (score.compareTo(BigDecimal.valueOf(0)) == 0) {
                            passUpdate(riskCreditUser,"本地渠道分控分数设置为[0]直接通过");
                            //跳过
                            continue;
                        }
                        //本地渠道分控设置为-1，直接拒绝
                        if (score.compareTo(BigDecimal.valueOf(-1)) == 0) {
                            //直接拒绝处理
                            refuseUpdate(riskCreditUser, "本地渠道分控设置为[-1]直接拒绝");
                            //跳过
                            continue;
                        }
                    }
                }

                Map<String, Object> postMap = chuqiyouFk(user, riskCreditUser);

                if (String.valueOf(postMap.get("resCode")).equals("0000")) {
                    //更新风控分数
                    riskCreditUser.setFkStatus(2);
                    riskCreditUser.setScore(new BigDecimal(String.valueOf(postMap.get("score"))));
                    if (String.valueOf(postMap.get("result")).equals("OK")) {
                        riskCreditUser.setDetail("通过");
                    } else {
                        riskCreditUser.setDetail("拒绝");
                    }

                    iRiskCreditUserDao.updateByPrimaryKeySelective(riskCreditUser);

                    //更新风控订单
                    OutOrders out_orders = iOutOrdersDao.findByOrderNo("WQY_" + riskCreditUser.getAssetId());

                    out_orders.setReturnParams(JSONObject.toJSONString(postMap));
                    out_orders.setUpdateTime(new Date());
                    out_orders.setStatus("2");
                    iOutOrdersDao.update(out_orders);

                    logger.info("开始本地分控与无其右分控对比");
                    Boolean isPass = queryIsPass(scoreList, postMap);
                    logger.info("风控是否通过：" + isPass);

                    if (isPass) {
                        //更新订单状态
                        AssetBorrowOrder order = iAssetBorrowOrderDao.getOrderById(riskCreditUser.getAssetId());
                        order.setStatus(AssetBorrowOrder.STATUS_DFK);
                        order.setUpdateTime(new Date());
                        order.setAuditTime(new Date());
                        iAssetBorrowOrderDao.updateByPrimaryKeySelective(order);

                        AssetOrderStatusHis ah = new AssetOrderStatusHis();
                        ah.setOrderId(order.getId());
                        ah.setOrderStatus(AssetBorrowOrder.STATUS_DFK);
                        ah.setCreateTime(new Date());
                        ah.setRemark("通过风控审核，等待放款");
                        iAssetOrderStatusHisDao.insert(ah);
                    } else {
                        //更新订单状态
                        AssetBorrowOrder order = iAssetBorrowOrderDao.getOrderById(riskCreditUser.getAssetId());
                        order.setStatus(AssetBorrowOrder.STATUS_SHJJ);
                        order.setUpdateTime(new Date());
                        order.setAuditTime(new Date());
                        order.setOrderEnd(1);
                        iAssetBorrowOrderDao.updateByPrimaryKeySelective(order);

                        AssetOrderStatusHis ah = new AssetOrderStatusHis();
                        ah.setOrderId(order.getId());
                        ah.setOrderStatus(AssetBorrowOrder.STATUS_SHJJ);
                        ah.setCreateTime(new Date());
                        ah.setRemark("未能通过风控审核");
                        iAssetOrderStatusHisDao.insert(ah);
                    }

                } else {

//                        //更新风控订单
//                        OutOrders out_orders =  iOutOrdersDao.findByOrderNo("WQY_"+riskCreditUser.getAssetId());
//
//                        out_orders.setReturnParams(JSONObject.toJSONString(postMap));
//                        out_orders.setUpdateTime(new Date());
//                        out_orders.setStatus("1");
//                        iOutOrdersDao.update(out_orders);


                    //更新风控分数
                    riskCreditUser.setFkStatus(2);
                    riskCreditUser.setScore(new BigDecimal(0));
                    riskCreditUser.setDetail("拒绝");

                    iRiskCreditUserDao.updateByPrimaryKeySelective(riskCreditUser);

                    //更新风控订单
                    OutOrders out_orders = iOutOrdersDao.findByOrderNo("WQY_" + riskCreditUser.getAssetId());

                    out_orders.setReturnParams(JSONObject.toJSONString(postMap));
                    out_orders.setUpdateTime(new Date());
                    out_orders.setStatus("2");
                    iOutOrdersDao.update(out_orders);

                    //更新订单状态
                    AssetBorrowOrder order = iAssetBorrowOrderDao.getOrderById(riskCreditUser.getAssetId());
                    order.setStatus(AssetBorrowOrder.STATUS_SHJJ);
                    order.setUpdateTime(new Date());
                    order.setAuditTime(new Date());
                    order.setOrderEnd(1);
                    iAssetBorrowOrderDao.updateByPrimaryKeySelective(order);

                    AssetOrderStatusHis ah = new AssetOrderStatusHis();
                    ah.setOrderId(order.getId());
                    ah.setOrderStatus(AssetBorrowOrder.STATUS_SHJJ);
                    ah.setCreateTime(new Date());
                    ah.setRemark("未能通过风控审核");
                    iAssetOrderStatusHisDao.insert(ah);
                }
            }
        }

    }

    /**
     * 运营商认证状态更新处理
     * @throws Exception
     */
    @Override
    public void doMobileAuthUpdate() throws Exception {
        logger.info("更新运营商认证过期用户状态--------start-----------");
        /*
         *  更新运营商认证时间超过限制天数的用户
         *  认证状态改为: 0-未认证
         * */
        //查询认证限制天数
        Map<String, String> fkMap = backConfigParamsService.getBackConfig("FK", null);
        if(fkMap != null && StringUtils.isNotEmpty(fkMap.get("FK_MOBILE_AUTH_EXPIRED"))
                && NumberUtils.isNumber(fkMap.get("FK_MOBILE_AUTH_EXPIRED"))){
            Map<String, Object> params = new HashMap<>();
            //过期时间
            params.put("expiredTime",
                    DateUtil.formatDate(
                            DateUtils.DEFAULT_FORMAT,
                            DateUtils.addDay(
                                    new Date(),
                                    -NumberUtils.toInt(
                                            fkMap.get("FK_MOBILE_AUTH_EXPIRED")
                                    )
                            )
                    )
            );
            int rows = iBorrowUserDao.updateBorrowUserMobileAuthStatus(params);
            logger.info("更新运营商认证过期用户状态成功，共计：" + rows +"条记录");
        }
        logger.info("更新运营商认证过期用户状态--------end-----------");
    }

    @Override
    public int getInWhiteListUserCount() {
        return iAssetRepaymentOrderDao.getInWhiteListUserCount();
    }

    @Override
    public List<Map<String, String>> getInWhiteListUser(int start, int pageSize) {
        return iAssetRepaymentOrderDao.getInWhiteListUser(start, pageSize);
    }

    @Override
    public int getInBlackListUserCount() {
        return iAssetRepaymentOrderDao.getInBlackListUserCount();
    }

    @Override
    public List<Map<String, String>> getInBlackListUser(int start, int pageSize) {
        return iAssetRepaymentOrderDao.getInBlackListUser(start, pageSize);
    }

    @Override
    public int getOutBlackListUserCount() {
        return iAssetRepaymentOrderDao.getOutBlackListUserCount();
    }

    @Override
    public List<Map<String, String>> getOutBlackListUser(int start, int pageSize) {
        return iAssetRepaymentOrderDao.getOutBlackListUser(start, pageSize);
    }

    /**
     * 查询用户是否为黑/白名单
     * @param phone 手机号
     * @param name 姓名
     * @param platform 平台：1-爱上花，2-花小侠
     * @return
     */
    @Override
    public String getIsBlackWhiteUser(String phone, String name, String platform) {
        try{
            Map<String, String> params = new HashMap<>();
            params.put("phone", phone);
            params.put("name", name);
            params.put("platForm",platform);
            logger.info("查询---用户是否为黑/白名单------请求参数：" + params.toString());
            String resp = HttpUtil.RequestForm(PropertiesUtil.get("risk.url") + "/blackWhiteList/queryIsBlackWhiteByOptions", params);
            logger.info("查询---用户是否为黑/白名单------返回结果：" + resp);
            if(StringUtils.isNotEmpty(resp)){
                Map<String,Object> resMap = JSONUtil.parseJSON2Map(resp);
                if(StringUtils.equals("0", resMap.get("code").toString())){
                    if(resMap.get("result") != null){
                        Map<String,Object> result = (Map) resMap.get("result");
                        if(result != null && StringUtils.isNotEmpty(result.get("map").toString())){
                            return ((Map)result.get("map")).get("type").toString();
                        }else{
                            return null;
                        }
                    }
                }

                if(StringUtils.equals("-1", resMap.get("code").toString())){
                    return null;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 设置用户为黑/白名单
     * @param phone 手机号
     * @param name 姓名
     * @param platform 平台：1-爱上花，2-花小侠
     * @param optType 操作类型：1-设置白名单，2-设置黑名单
     * @return
     */
    @Override
    public ApiServiceResult saveBlackWhiteUser(String phone, String name, String platform, String optType, String status) {
        try{
            Map<String, String> params = new HashMap<>();
            params.put("phone", phone);
            params.put("name", name);
            params.put("platForm",platform);
            params.put("optType", optType);
            params.put("status", status);
            logger.info("设置-----用户为黑/白名单------请求参数：" + params.toString());
            String result = HttpUtil.RequestForm(PropertiesUtil.get("risk.url") + "/blackWhiteList/saveBlackWhiteByOptions", params);
            logger.info("设置-----用户为黑/白名单------返回结果：" + result);
            if(StringUtils.isNotEmpty(result)){
                Map<String,Object> resMap = JSONUtil.parseJSON2Map(result);
                return new ApiServiceResult(resMap.get("code").toString(),resMap.get("message").toString());
            }else{
                return new ApiServiceResult("-1", "操作失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ApiServiceResult("-1", "操作失败,系统异常！");
        }
    }


    /**
     * 根据身份证的号码算出当前身份证持有者的性别和年龄 18位身份证
     * 1男2女
     *
     * @return
     * @throws Exception
     */
    public static String getUserSex(String CardCode)
            throws Exception {
        String sex;
        if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
            sex = "女";
        } else {
            sex = "男";
        }
        return sex;
    }

    /**
     * 根据本地渠道分和无其右的进行比较
     */
    private boolean queryIsPass(final List<Map<String, String>> scoreList, final Map<String, Object> postMap) {
        if (CollectionUtils.isEmpty(scoreList)) {
            return postMap.get("result").equals("OK");
        }
        Map<String, String> map = scoreList.get(0);
        if (map.containsKey("score")) {
            BigDecimal score = new BigDecimal(String.valueOf(map.get("score")));
            logger.info("风控-------本地信用分-----渠道：" + score + "----------" + String.valueOf(map.get("cid")));
            //无其右分数和本地分数相等或较大，则通过
            if (new BigDecimal(String.valueOf(postMap.get("score"))).compareTo(score) != -1) {
                return true;
            } else {
                return false;
            }
        }
        return String.valueOf(postMap.get("result")).equals("OK");

    }

    /**
     * 直接拒绝处理
     * @param riskCreditUser
     */
    private void refuseUpdate(RiskCreditUser riskCreditUser, String refMsg){
        //更新风控分数
        riskCreditUser.setFkStatus(2);
        riskCreditUser.setScore(new BigDecimal(0));
        riskCreditUser.setDetail(refMsg);
        iRiskCreditUserDao.updateByPrimaryKeySelective(riskCreditUser);

        //更新订单状态
        AssetBorrowOrder order = iAssetBorrowOrderDao.getOrderById(riskCreditUser.getAssetId());
        order.setStatus(AssetBorrowOrder.STATUS_SHJJ);
        order.setUpdateTime(new Date());
        order.setAuditTime(new Date());
        order.setOrderEnd(1);
        iAssetBorrowOrderDao.updateByPrimaryKeySelective(order);

        //新增订单历史状态记录
        AssetOrderStatusHis ah = new AssetOrderStatusHis();
        ah.setOrderId(order.getId());
        ah.setOrderStatus(AssetBorrowOrder.STATUS_SHJJ);
        ah.setCreateTime(new Date());
        ah.setRemark("未能通过风控审核");
        iAssetOrderStatusHisDao.insert(ah);

    }

    /**
     * 直接通过处理
     * @param riskCreditUser
     * @param passMsg
     */
    private void passUpdate(RiskCreditUser riskCreditUser, String passMsg){
        AssetBorrowOrder order = iAssetBorrowOrderDao.getOrderById(riskCreditUser.getAssetId());
        //更新风控分数
        riskCreditUser.setFkStatus(2);
        riskCreditUser.setScore(new BigDecimal(0));
        //20190211 从2次改成1次
        riskCreditUser.setDetail(passMsg);

        iRiskCreditUserDao.updateByPrimaryKeySelective(riskCreditUser);
        //更新订单状态
        order.setStatus(AssetBorrowOrder.STATUS_DFK);
        order.setUpdateTime(new Date());
        order.setAuditTime(new Date());
        iAssetBorrowOrderDao.updateByPrimaryKeySelective(order);

        AssetOrderStatusHis ah = new AssetOrderStatusHis();
        ah.setOrderId(order.getId());
        ah.setOrderStatus(AssetBorrowOrder.STATUS_DFK);
        ah.setCreateTime(new Date());
        ah.setRemark("通过风控审核，等待放款");
        iAssetOrderStatusHisDao.insert(ah);
    }

    /**
     * 校验用户是否满足已下条件
     *  1:有过逾期记录的送无其右风控评分
     *  2:正常还款2次以上的老用户直接通过不送无其右
     * @param userId
     * @return
     */
    private boolean checkSend(int userId){
        boolean flag = true;
        //查询用户逾期/逾期还款记录
        int overCount = iAssetRepaymentOrderDao.getRepaymentExpiredCount(userId);
        if( overCount > 0){
            return flag;
        }
        //查询正常还款记录
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("status", AssetOrderStatusHis.STATUS_YHK);//已还款
        params.put("orderEnd", "1");
        List<AssetBorrowOrder> yhk = iAssetBorrowOrderDao.findParams(params);
        if(CollectionUtils.isNotEmpty(yhk) && yhk.size() > 1){
            flag = false;
            return flag;
        }
        return flag;
    }

}
