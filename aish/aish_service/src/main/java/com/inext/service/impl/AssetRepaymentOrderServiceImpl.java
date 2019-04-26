package com.inext.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.*;
import com.inext.dto.AssetRepaymentOrderByWithholdDto;
import com.inext.dto.AssetRepaymentOrderDto;
import com.inext.entity.*;
import com.inext.service.IAssetRepaymentOrderService;
import com.inext.service.IRiskCreditUserService;
import com.inext.utils.WebClient;
import com.stylefeng.guns.modular.system.model.BizCollectionOrder;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 开发人员：wenkk 创建时间：2018-03-21 18:47
 */

@Service
@Transactional
public class AssetRepaymentOrderServiceImpl extends BaseSerivceImpl<AssetRepaymentOrder> implements IAssetRepaymentOrderService {

    @Resource
    IAssetRepaymentOrderDao iAssetRepaymentOrderDao;
    @Resource
    IAssetBorrowOrderDao iAssetBorrowOrderDao;
    @Resource
    IAssetOrderStatusHisDao iAssetOrderStatusHisDao;
    @Resource
    IAssetRepaymentDetailDao iAssetRepaymentDetailDao;
    @Autowired
    private IRiskCreditUserService iRiskCreditUserService;
    @Resource
    IAssetRepaymentOrderWithholdDao iAssetRepaymentOrderWithholdDao;

    org.slf4j.Logger logger = LoggerFactory.getLogger(AssetRepaymentOrderServiceImpl.class);
    private final Integer productId = 23772;

    @Override
    public PageInfo<AssetRepaymentOrder> getPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage;
            int pageSize = Constants.INITIAL_PAGE_SIZE;

            if (params.get(Constants.CURRENT_PAGE) == null || "".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = Constants.INITIAL_CURRENT_PAGE;
            } else {
                currentPage = Integer.parseInt(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.containsKey(Constants.PAGE_SIZE)) {
                pageSize = Integer.parseInt(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
            if ("-1".equals(params.get("channelId"))) {
                params.remove("channelId");
            }
        }
        List<AssetRepaymentOrder> list = this.iAssetRepaymentOrderDao.getPageList(params);
        for (AssetRepaymentOrder item : list) {
            RiskCreditUser riskCreditUser = iRiskCreditUserService.getByAssetId(item.getOrderId().toString());
            if (riskCreditUser == null) {
                item.setScore("未风控");
                item.setDetail("");
                continue;
            }
            item.setScore(riskCreditUser.getScore().toString());
            item.setDetail(riskCreditUser.getDetail());
        }
        PageInfo<AssetRepaymentOrder> pageInfo = new PageInfo<AssetRepaymentOrder>(list);
        return pageInfo;
    }


    @Override
    public List<Map<String, String>> getList(Map<String, Object> params) {
        List<Map<String, String>> list = iAssetRepaymentOrderDao.getList(params);
        return list;
    }

    @Override
    public AssetRepaymentOrder getByUserId(Integer userId) {
        return this.iAssetRepaymentOrderDao.getByUserId(userId);
    }

    @Override
    public int updateRepaymentByOrderId(AssetRepaymentOrder assetRepaymentOrder) {
        return iAssetRepaymentOrderDao.updateRepaymentByOrderId(assetRepaymentOrder);
    }

    @Override
    public void trueRepaymentMoney(AssetRepaymentDetail assetRepaymentDetail) {
        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderDao.getByOrderId(assetRepaymentDetail.getOrderId());
        assetRepaymentOrder.setRepaymentedAmount(assetRepaymentOrder.getRepaymentedAmount().add(assetRepaymentDetail.getTrueRepaymentMoney()));
        //如果已还款金额等于应还款金额表示还款成功
        if (assetRepaymentOrder.getRepaymentAmount().intValue() == assetRepaymentOrder.getRepaymentedAmount().intValue()) {
            // 后台手动还款
            assetRepaymentOrder.setRepaymentType(AssetRepaymentOrder.REPAYMENT_TYPE_BACK);
            assetRepaymentOrder.setRepaymentRealTime(assetRepaymentDetail.getCreatedAt());
            assetRepaymentOrder.setOrderStatus(AssetRepaymentOrder.STATUS_YHK);
            //关闭订单
            AssetBorrowOrder assetBorrowOrder = new AssetBorrowOrder();
            assetBorrowOrder.setId(assetRepaymentDetail.getOrderId());
            assetBorrowOrder.setStatus(AssetOrderStatusHis.STATUS_YHK);
            assetBorrowOrder.setOrderEnd(1);
            assetBorrowOrder.setUpdateTime(new Date());
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrder);
            //记录状态变更
            AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
            assetOrderStatusHis.setOrderId(assetRepaymentDetail.getOrderId());
            assetOrderStatusHis.setOrderStatus(AssetOrderStatusHis.STATUS_YHK);
            assetOrderStatusHis.setCreateTime(new Date());
            //assetOrderStatusHis.setRemark("已取消交易，返还预付款+违约金，共" + assetRepaymentOrder.getRepaymentAmount() + "元");
            assetOrderStatusHis.setRemark(AssetOrderStatusHis.orderStatusMap.get(AssetOrderStatusHis.STATUS_YHK));
            iAssetOrderStatusHisDao.insert(assetOrderStatusHis);
        }
        assetRepaymentOrder.setUpdateTime(new Date());
        assetRepaymentOrder.setUpdateAccount(assetRepaymentDetail.getAdminUsername());
        iAssetRepaymentOrderDao.updateRepaymentById(assetRepaymentOrder);
        assetRepaymentDetail.setUserId(assetRepaymentOrder.getUserId());
        iAssetRepaymentDetailDao.insert(assetRepaymentDetail);
    }

    @Override
    public int doSave(AssetRepaymentDetail assetRepaymentDetail) {
        return iAssetRepaymentDetailDao.insert(assetRepaymentDetail);
    }

    @Override
    public AssetRepaymentOrder getRepaymentByOrderId(Integer orderId) {
        return iAssetRepaymentOrderDao.getRepaymentByOrderId(orderId);
    }

    @Override
    public int update(AssetRepaymentOrder assetRepaymentOrder) {
        return iAssetRepaymentOrderDao.updateByPrimaryKeySelective(assetRepaymentOrder);
    }

    @Override
    public int insertRepaymentOrder(AssetRepaymentOrder assetRepaymentOrder) {
        return iAssetRepaymentOrderDao.insertSelective(assetRepaymentOrder);
    }

    @Override
    public void trueRepaymentMoney(AssetRepaymentOrder aro, AssetRepaymentDetail paramsDto) {
        AssetRepaymentOrder aroModel = new AssetRepaymentOrder();
        aroModel.setId(aro.getId());
        aroModel.setRepaymentedAmount(aro.getRepaymentedAmount().add(paramsDto.getTrueRepaymentMoney()));

        if (paramsDto.getCeditAmount() != null && paramsDto.getCeditAmount().compareTo(new BigDecimal(0)) > 0) {
            aroModel.setCeditAmount(paramsDto.getCeditAmount().add(aro.getCeditAmount() == null ? new BigDecimal(0) : aro.getCeditAmount()));
        } else {
            paramsDto.setCeditAmount(new BigDecimal(0));
        }

        Integer status = AssetRepaymentOrder.STATUS_YHK;
        Date now = new Date();

        //已还金额 + 已减免金额 + 当前减免金额  +  当前还款金额 +  大于等于  累计应总还金额 表示用户已经结清
        BigDecimal sumAmount = aro.getRepaymentedAmount().add(aro.getCeditAmount() == null ? new BigDecimal(0) : aro.getCeditAmount()).add(paramsDto.getCeditAmount()).add(paramsDto.getTrueRepaymentMoney());

        if (sumAmount.compareTo(aro.getRepaymentAmount()) >= 0) {
            // 后台手动还款
            aroModel.setRepaymentType(AssetRepaymentOrder.REPAYMENT_TYPE_BACK);
            aroModel.setRepaymentRealTime(now);

            status = AssetOrderStatusHis.STATUS_YHK;
            //关闭订单
            AssetBorrowOrder assetBorrowOrder = new AssetBorrowOrder();
            assetBorrowOrder.setId(aro.getOrderId());
            assetBorrowOrder.setStatus(status);
            assetBorrowOrder.setOrderEnd(1);
            assetBorrowOrder.setUpdateTime(now);
            iAssetBorrowOrderDao.updateByPrimaryKeySelective(assetBorrowOrder);

            //记录状态变更
            AssetOrderStatusHis assetOrderStatusHis = new AssetOrderStatusHis();
            assetOrderStatusHis.setOrderId(aro.getOrderId());
            assetOrderStatusHis.setOrderStatus(status);
            assetOrderStatusHis.setCreateTime(now);
            //assetOrderStatusHis.setRemark("已取消交易，返还预付款+违约金，共" + aro.getRepaymentAmount() + "元");
            assetOrderStatusHis.setRemark(AssetOrderStatusHis.orderStatusMap.get(status));
            iAssetOrderStatusHisDao.insert(assetOrderStatusHis);

            aroModel.setOrderStatus(status);
        }

        aroModel.setUpdateTime(now);
        aroModel.setUpdateAccount(paramsDto.getAdminUsername());

        //更新还款信息
        iAssetRepaymentOrderDao.updateRepaymentById(aroModel);


        paramsDto.setUserId(aro.getUserId());
        iAssetRepaymentDetailDao.insert(paramsDto);
    }

    @Override
    public List<AssetRepaymentOrder> getYOPDKList(AssetRepaymentOrderDto assetRepaymentOrderDto) {

        int pageNum = assetRepaymentOrderDto == null || assetRepaymentOrderDto.getPageNum() == null ? 1 : assetRepaymentOrderDto.getPageNum();
        int pageSize = assetRepaymentOrderDto == null || assetRepaymentOrderDto.getPageSize() == null ? 10 : assetRepaymentOrderDto.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        List<AssetRepaymentOrder> list = iAssetRepaymentOrderDao.getYOPDKList(assetRepaymentOrderDto);
        return list;
    }

    @Override
    public List<AssetRepaymentOrder> getDKList(AssetRepaymentOrderDto dto) {
        int pageNum = dto == null || dto.getPageNum() == null ? 1 : dto.getPageNum();
        int pageSize = dto == null || dto.getPageSize() == null ? 10 : dto.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        List<AssetRepaymentOrder> list = iAssetRepaymentOrderDao.getDKList(dto);
        return list;
    }

    @Override
    public int calculationDKOrderNum(AssetRepaymentOrderDto dto) {
        return iAssetRepaymentOrderDao.calculationDKOrderNum(dto);
    }


    @Override
    public void pushCuiShouTask() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String repaymentTimeEnd = dateFormat.format(date);
        Map<String, Object> params = new HashMap<>();

        params.put("repaymentTimeEnd", repaymentTimeEnd);

        List<AssetRepaymentOrder> list = iAssetRepaymentOrderDao.yuyiYihuankuanOrder(params);//已处理

        if (list != null && list.size() > 0) {
            List<BizCollectionOrder> listBiz = new ArrayList<>();
            //有数据
            for (AssetRepaymentOrder orderDto : list) {
                BizCollectionOrder biz = new BizCollectionOrder();
                //返回对象中
                biz.setUserId(orderDto.getUserId());
                biz.setUserPhone(orderDto.getUserPhone());
                biz.setUserName(orderDto.getUserName());
                biz.setOrderId(orderDto.getOrderId());


                //借款本金
                biz.setMoneyAmount(orderDto.getMoneyAmount());
                //违约金
                biz.setPenaltyAmount(orderDto.getPenaltyAmount());


                biz.setPlanLateFee(orderDto.getPlanLateFee());
                biz.setCeditAmount(new BigDecimal(0));
                biz.setRepaymentAmount(orderDto.getRepaymentAmount());
                biz.setRepaymentedAmount(new BigDecimal(0));

                biz.setRepaymentType(orderDto.getRepaymentType());//还款状态
                biz.setLateFeeApr(orderDto.getLateFeeApr());
                biz.setCreditRepaymentTime(orderDto.getCreditRepaymentTime());
                biz.setRepaymentTime(orderDto.getRepaymentTime());
                biz.setPeriod(0);
                biz.setRepaymentRealTime(orderDto.getRepaymentRealTime());
                biz.setLateFeeStartTime(orderDto.getLateFeeStartTime());
                biz.setInterestUpdateTime(orderDto.getInterestUpdateTime());
                biz.setLateDay(1);

                if (orderDto.getOrderStatus() == 12) {
                    biz.setOrderStatus(7);
                } else {
                    biz.setOrderStatus(orderDto.getOrderStatus());//未还款
                }

                //biz.setRepaymentPayStatus(orderDto.getRepaymentPayStatus());
                biz.setDkPayStatus(2);

                biz.setOrderRenewal(orderDto.getOrderRenewal());
                biz.setOrderMail(orderDto.getOrderMail());
                biz.setNoOrder(orderDto.getNoOrder());
                biz.setCompanyId(null);
                //项目id
                biz.setItemId(productId);
                biz.setCollectorId(null);
                biz.setCollectorName(null);
                biz.setCollectorGroupName(null);
                biz.setDistributeTime(null);
                biz.setContactStatus(null);
                //是否逾期;0否 1是
                biz.setIsLate(1);
                biz.setNextLoanAdvice(null);
                biz.setLastCollectionRecord(null);
                biz.setUpdateTime(null);
                biz.setUpdateAccount(null);
                listBiz.add(biz);

            }
            Map<Integer, List<BizCollectionOrder>> map = new HashMap<>();
            map.put(productId, listBiz);
            String param = JSONObject.toJSONString(map);
            logger.info("查询后总数：" + listBiz.size());
            String ch_od_key = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_OD_KEY");
            try {
                logger.info("==================调用催收逾期接口");
                String plainCredentials = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_AU_KEY");
                String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
                HashMap<String, Object> fkParams = new HashMap<String, Object>();
                fkParams.put("Authorization", "Basic " + base64Credentials);
                String result = new WebClient().postJsonData(ch_od_key, param, fkParams);
                // String result = HttpsUtil.doPostByJson(ch_od_key, param, "UTF-8");
                logger.info("==================调用催收逾期接口结束,结果：" + result.toString());
            } catch (Exception e) {
                logger.error("调用催收逾期接口失败", e.getMessage(), e);
            }
        }

    }


    //查询已逾期,已还款的订单或者已逾期部分还款的订单到催收系统
    @Override
    public void findRaymentedHuanKuan() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String repaymentTimeEnd = dateFormat.format(date);
        Map<String, Object> params = new HashMap<>();
        params.put("repaymentTimeEnd", repaymentTimeEnd);

        //先去查询订单的时间,用当前时间和订单的实际还款时间进行比较,

        List<AssetRepaymentOrder> list = iAssetRepaymentOrderDao.findRaymentedHuanKuan(params);

        List<BizCollectionOrder> listBiz = new ArrayList<>();

        if (list != null && list.size() > 0) {
            for (AssetRepaymentOrder order :
                    list) {
                BizCollectionOrder biz = new BizCollectionOrder();
                biz.setUserId(order.getUserId());
                biz.setRepaymentedAmount(order.getRepaymentedAmount());
                biz.setRepaymentRealTime(order.getRepaymentRealTime());
                biz.setOrderId(order.getOrderId());
                biz.setOrderStatus(14);//14是催收系统中的已还款
                biz.setCeditAmount(order.getCeditAmount());

                biz.setLateDay(order.getLateDay());
                biz.setPlanLateFee(order.getPlanLateFee());
                biz.setRepaymentAmount(order.getRepaymentAmount());

                biz.setItemId(productId);
                listBiz.add(biz);
            }
            String param = JSONObject.toJSONString(listBiz);
            logger.info("==================传递数据：" + listBiz.size());
            String ch_od_key = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_RM_KEY");
            try {
                logger.info("==================调用催收还款接口");
                String plainCredentials = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_AU_KEY");
                String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
                HashMap<String, Object> fkParams = new HashMap<String, Object>();
                fkParams.put("Authorization", "Basic " + base64Credentials);
                String result = new WebClient().postJsonData(ch_od_key, param, fkParams);
                //String result = HttpsUtil.doPostByJson(ch_od_key, param, "UTF-8");
                logger.info("==================调用催收还款接口结束,结果：" + result);
            } catch (Exception e) {
                logger.error("调用催收还款接口失败", e.getMessage(), e);
            }
        }

    }


    @Override
    public void findCurrentOverdue() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Map<String, Object> params = new HashMap<>();
        params.put("repaymentTime", dateFormat.format(date));
        List<AssetRepaymentOrder> list = iAssetRepaymentOrderDao.currentTimeOrder(params);

        if (list != null && list.size() > 0) {
            List<BizCollectionOrder> listBiz = new ArrayList<>();
            try {
                //有数据
                for (AssetRepaymentOrder orderDto : list) {
                    BizCollectionOrder biz = new BizCollectionOrder();
                    //返回对象中
                    biz.setUserId(orderDto.getUserId());
                    biz.setUserPhone(orderDto.getUserPhone());
                    biz.setUserName(orderDto.getUserName());
                    biz.setOrderId(orderDto.getOrderId());
                    biz.setOrderStatus(orderDto.getOrderStatus());
                    //借款本金
                    biz.setMoneyAmount(orderDto.getMoneyAmount());
                    //违约金
                    biz.setPenaltyAmount(orderDto.getPenaltyAmount());
                    //应还款总金额
                    biz.setRepaymentAmount(orderDto.getRepaymentAmount());

                    biz.setRepaymentType(orderDto.getRepaymentType());//还款状态
                    biz.setRepaymentTime(orderDto.getRepaymentTime());
                    biz.setCreditRepaymentTime(orderDto.getCreditRepaymentTime());
                    biz.setOrderRenewal(orderDto.getOrderRenewal());
                    //项目id
                    biz.setItemId(productId);
                    listBiz.add(biz);

                }
                Map<Integer, List<BizCollectionOrder>> map = new HashMap<>();
                map.put(productId, listBiz);
                logger.info("==================传递传输:" + listBiz.size());
                String param = JSONObject.toJSONString(map);
                String ch_od_key = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_NO_KEY");

                logger.info("==================调用催收最后一天逾期接口");
                String plainCredentials = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_AU_KEY");
                String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
                HashMap<String, Object> fkParams = new HashMap<String, Object>();
                fkParams.put("Authorization", "Basic " + base64Credentials);
                String result = new WebClient().postJsonData(ch_od_key, param, fkParams);
                //String result = HttpsUtil.doPostByJson(ch_od_key, param, "UTF-8");
                logger.info("==================调用催收最后一天逾期接口结束,结果：" + result);
            } catch (Exception e) {
                logger.error("催收最后一天逾期接口失败", e.getMessage(), e);
            }
        }
    }

    @Override
    public void pushCuishouNotOverdue() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String repaymentTimeEnd = dateFormat.format(date);
        Map<String, Object> params = new HashMap<>();
        params.put("repaymentTimeEnd", repaymentTimeEnd);
        List<BizCollectionOrder> listBiz = new ArrayList<>();
        List<AssetRepaymentOrder> list = iAssetRepaymentOrderDao.pushCuishouNotOverdue(params);

        if (list != null && list.size() > 0) {
            for (AssetRepaymentOrder order :
                    list) {
                BizCollectionOrder biz = new BizCollectionOrder();
                biz.setUserId(order.getUserId());
                biz.setRepaymentedAmount(order.getRepaymentedAmount());
                biz.setRepaymentRealTime(order.getRepaymentRealTime());
                biz.setOrderId(order.getOrderId());
                biz.setCeditAmount(order.getCeditAmount());
                biz.setOrderStatus(order.getOrderStatus());
                biz.setItemId(productId);
                listBiz.add(biz);
            }

            String param = JSONObject.toJSONString(listBiz);
            logger.info("------------------传递数据：" + listBiz.size());
            String ch_od_key = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_YU_KEY");
            try {
                logger.info("---------------------推送未逾期已还款接口");
                String plainCredentials = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_AU_KEY");
                String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
                HashMap<String, Object> fkParams = new HashMap<String, Object>();
                fkParams.put("Authorization", "Basic " + base64Credentials);
                String result = new WebClient().postJsonData(ch_od_key, param, fkParams);
                //String result = HttpsUtil.doPostByJson(ch_od_key, param, "UTF-8");
                logger.info("----------------推送未逾期已还款接口,结果：" + result);
            } catch (Exception e) {
                logger.error("推送未逾期已还款接口失败！", e.getMessage(), e);
            }
        }

    }


    @Override
    public String delUnderLineRe(Integer orderId, String userPhone) {
        logger.info("service delUnderLineRe start");
        String result = "";
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("userPhone", userPhone);
        map.put("productId", productId);
        //map.put("companyId",96);
        //根据传递的数据查询
        List<AssetRepaymentOrder> data = iAssetRepaymentOrderDao.selectCountByHand(map);
        if (data == null || data.size() == 0) {
            return "没有查到数据！";
        }

        map.put("userId", data.get(0).getUserId());
        String param = JSONObject.toJSONString(map);
        logger.info("------------------传递数据：" + param);
        String ch_od_key = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_DEL_KEY");
        logger.info("---------------------推送催收删除接口");
        String plainCredentials = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CUISHOU).get("CH_AU_KEY");
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        HashMap<String, Object> fkParams = new HashMap<String, Object>();
        fkParams.put("Authorization", "Basic " + base64Credentials);
        result = new WebClient().postJsonData(ch_od_key, param, fkParams);
        //String result = HttpsUtil.doPostByJson(ch_od_key, param, "UTF-8");
        logger.info("----------------推送催收删除接口,结果：" + result);

        return result;
    }

    @Override
    public List<AssetRepaymentOrderByWithholdDto> getListByWithhold(Map<String, Object> params) {
        List<AssetRepaymentOrderByWithholdDto> list = iAssetRepaymentOrderDao.getListByWithhold(params);
        return list;
    }

    @Override
    public int insertOrderWithhold(AssetRepaymentOrderWithhold withhold) {
        return iAssetRepaymentOrderWithholdDao.insertOrderWithhold(withhold);
    }

    @Override
    public int updateRepaymentByIdWithhold(Integer withholdId, Integer dkPayStatus, Integer repaymentPayStatus, String errMessage, List<Integer> list) {
        return iAssetRepaymentOrderDao.updateRepaymentByIdWithhold(withholdId, dkPayStatus, repaymentPayStatus, errMessage, list);
    }

    @Override
    public int updateWithhold(String batchId, Integer chargeStatus, Date updateTime, String errMessage, Integer id) {
        return iAssetRepaymentOrderWithholdDao.updateWithhold(batchId, chargeStatus, updateTime, errMessage, id);
    }

    @Override
    public AssetRepaymentOrderWithhold getWithholdByOther(String requestId, Integer id) {
        return iAssetRepaymentOrderWithholdDao.selectWithholdByOther(requestId, id);
    }

    @Override
    public List<AssetRepaymentOrderWithhold> getWithholdByStatus(Integer chargeStatus) {
        return iAssetRepaymentOrderWithholdDao.getWithholdByStatus(chargeStatus);
    }

    @Override
    public List<AssetRepaymentOrder> getListByRapaymentOrder(Map<String, Object> params) {
        return iAssetRepaymentOrderDao.getListByRepaymentOrder(params);
    }

    /**
     * 已还款查询
     *
     * @param params
     * @return
     */
    @Override
    public PageInfo<AssetRepaymentOrder> getListForRepaymented(Map<String, Object> params) {
        if (params != null) {
            int currentPage;
            int pageSize = Constants.INITIAL_PAGE_SIZE;

            if (params.get(Constants.CURRENT_PAGE) == null || "".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = Constants.INITIAL_CURRENT_PAGE;
            } else {
                currentPage = Integer.parseInt(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.containsKey(Constants.PAGE_SIZE)) {
                pageSize = Integer.parseInt(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
            if ("-1".equals(params.get("channelId"))) {
                params.remove("channelId");
            }
        }
        List<AssetRepaymentOrder> list = this.iAssetRepaymentOrderDao.getListForRepaymented(params);
        for (AssetRepaymentOrder item : list) {
            RiskCreditUser riskCreditUser = iRiskCreditUserService.getByAssetId(item.getOrderId().toString());
            if (riskCreditUser == null) {
                item.setScore("未风控");
                item.setDetail("");
                continue;
            }
            item.setScore(riskCreditUser.getScore().toString());
            item.setDetail(riskCreditUser.getDetail());
        }
        PageInfo<AssetRepaymentOrder> pageInfo = new PageInfo<AssetRepaymentOrder>(list);
        return pageInfo;
    }

    @Override
    public HcRepaymentInfo getOneByHcRepaymentOrder(int orderId) {
        return iAssetRepaymentOrderDao.getOneByHcRepaymentOrder(orderId);
    }

    @Override
    public int updateCeditAndRepaymented(int orderId, Integer amount) {

        AssetRepaymentOrder assetRepaymentOrder = iAssetRepaymentOrderDao.getByOrderId(orderId);
        BigDecimal repaymentedAmount = assetRepaymentOrder.getRepaymentedAmount();
        if (repaymentedAmount.compareTo(new BigDecimal(amount)) == -1) {
            return -1;
        }
        return iAssetRepaymentOrderDao.updateCeditAndRepaymented(orderId, amount);
    }


    @Override
    public long getOrderByRepaymentOrderNo(String repaymentOrderNo) {
        return iAssetRepaymentDetailDao.getARDetailByRepaymentOrderNo(repaymentOrderNo);
    }
}
