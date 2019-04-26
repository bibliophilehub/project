package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IRepaymentStatisticsDao;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.service.IRepaymentStatisticsService;
import com.inext.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service("repaymentStatisticsService")
public class RepaymentStatisticsImpl implements IRepaymentStatisticsService {

    @Resource
    IRepaymentStatisticsDao repaymentStatisticsDao;


    @Override
    public PageInfo<Map<String, Object>> getPageList(Map<String, Object> params) {
        if (params != null) {
            int currentPage = Constants.INITIAL_CURRENT_PAGE;
            int pageSize = Constants.INITIAL_PAGE_SIZE;
            if (params.get(Constants.CURRENT_PAGE) != null && !"".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = com.inext.utils.StringUtils.getInteger(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.get(Constants.PAGE_SIZE) != null && !"".equals(params.get(Constants.PAGE_SIZE))) {
                pageSize = com.inext.utils.StringUtils.getInteger(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        List<Map<String, Object>> list = repaymentStatisticsDao.getRepaymentStatistics(params);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
        return pageInfo;
    }

    @Override
    public List<Map<String, Object>> exportExcelFile(Map<String, Object> params) {
        List<Map<String, Object>> list = repaymentStatisticsDao.getRepaymentStatistics(params);
        return list;
    }

    @Override
    public PageInfo<Map<String, Object>> getRepayStatistics(Map<String, Object> params) {


        if (params != null) {

            List<AssetRepaymentOrder> details = repaymentStatisticsDao.queryStatic(params);
            Map<String, List<AssetRepaymentOrder>> detailmap = details.stream()
                    .collect(Collectors.groupingBy(d -> fetchGroupKey(d)));

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            Map<String, Object> myMap = new HashMap<String, Object>();

            int currentPage = Constants.INITIAL_CURRENT_PAGE;
            int pageSize = Constants.INITIAL_PAGE_SIZE;
            if (params.get(Constants.CURRENT_PAGE) != null && !"".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = com.inext.utils.StringUtils.getInteger(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.get(Constants.PAGE_SIZE) != null && !"".equals(params.get(Constants.PAGE_SIZE))) {
                pageSize = com.inext.utils.StringUtils.getInteger(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);

            if (params.get("repaymentDate") != null) {
                List<AssetRepaymentOrder> data_list = detailmap.get(String.valueOf(params.get("repaymentDate")));
                myMap = comMy(data_list);
                list.add(myMap);
                PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
                pageInfo.setTotal(1);
                pageInfo.setPages(1);
                pageInfo.setPageNum(currentPage);
                return pageInfo;
            } else {

                for (int i = 0; i < pageSize; i++) {
                    String data_query = DateUtils.dateToString(DateUtils.addDay(new Date(), 0 - (currentPage - 1) * pageSize - i), "yyyy-MM-dd");
                    List<AssetRepaymentOrder> data_list = detailmap.get(data_query);
                    if (data_list != null) {
                        myMap = comMy(data_list);
                        list.add(myMap);
                    }
                }
                PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
                if (detailmap.size() == 0) {
                    pageInfo.setTotal(0);
                } else if (detailmap.size() > 0 && detailmap.size() <= 4) {
                    pageInfo.setTotal(0);
                } else {
                    pageInfo.setTotal(detailmap.size() - 4);
                }

                pageInfo.setPages(Integer.valueOf(String.valueOf(Math.round(Math.ceil((double) (detailmap.size() - 4) / pageSize)))));
                pageInfo.setPageNum(currentPage);
                return pageInfo;
            }
        }
        return null;

    }

    /**
     * @param repaymentlist
     * @return
     */
    private Map<String, Object> comMy(List<AssetRepaymentOrder> repaymentlist) {

        int loan_all_count = repaymentlist.size(); //放款总单数
        int shoujie = 0;  //首借
        int fujie = 0; //复借
        int history_xuqi = 0; //历史续期到期数
        int xuqi = 0;  //续期单数
        int anshi_huankuan = 0;  //按时还款单数
        int yuqi_danshu = 0;   //逾期总单数
        int yuqi_yihuan = 0;    //逾期已还单数
        int daihuan_danshu = 0;   //当前待还单数

        int xuqishu = 0;    //续期单数

        int shouri_yuqishu = 0;   //首日逾期数
        String shouri_yuqilv;   //首日逾期率

        int day7_yuqishu = 0;   //7日逾期数
        String day7_yuqilv;        //7日逾期率
        String now_huikuanlv_jine;//当前回款率（含金额）
        int repaymented_amount_sum = 0; //已还金额
        int money_amount_sum = 0;//放款金额


        String now_huikuanlv_xuqi;  //当前回款率(含续期)

        int shoujie_xudanshu = 0;   //首借续单数
        int shoujie_huankuanshu = 0;  //首借还款数
        String shoujie_huikuanlv_xuqi;   //首借回款率(含续期

        int fujie_xudanshu = 0;   //复借续单数
        int fujie_huankuanshu = 0;  //复借还款数
        String fujie_huikuanlv_xuqi;    //复借回款率(含续期
        String loan_date = "";
        String repay_date = "";

        int huankuan_shu = 0;   //还款数
        int xuqi_weihuan = 0;  //续期还未还

        int shoujie_xuqi_weihuan = 0;  //首借续期还未还
        int fujie_xuqi_weihuan = 0;  //复借续期还未还

        for (int i = 0; i < repaymentlist.size(); i++) {

            AssetRepaymentOrder rep = repaymentlist.get(i);

            loan_date = DateUtils.dateToString(rep.getCreditRepaymentTime(), "yyyy-MM-dd");

            repay_date = DateUtils.dateToString(DateUtils.addDay(rep.getCreditRepaymentTime(), 6), "yyyy-MM-dd");

            //首借 复借
            if (rep.getIsOld() == 0) {
                shoujie = shoujie + 1;

                //首借续单数
                if (rep.getOrderRenewal() != null && rep.getOrderRenewal() == 9) {

                    shoujie_xudanshu = shoujie_xudanshu + 1;
                }

                //首借还款数
                if (rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)) {
                    shoujie_huankuanshu = shoujie_huankuanshu + 1;
                }

                //首借续期还未还
                if (rep.getOrderRenewal() != null && rep.getOrderRenewal() == 9 && rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_DHK)) {

                    shoujie_xuqi_weihuan = shoujie_xuqi_weihuan + 1;
                }


            } else {

                fujie = fujie + 1;

                //复借续单数
                if (rep.getOrderRenewal() != null && rep.getOrderRenewal() == 9) {

                    fujie_xudanshu = fujie_xudanshu + 1;
                }

                //复借还款数
                if (rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)) {
                    fujie_huankuanshu = fujie_huankuanshu + 1;
                }

                //复借续期还未还
                if (rep.getOrderRenewal() != null && rep.getOrderRenewal() == 9 && rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_DHK)) {

                    fujie_xuqi_weihuan = fujie_xuqi_weihuan + 1;
                }

            }

            //历史续期到期单数
            if (DateUtils.compareDay(DateUtils.addDay(rep.getCreditRepaymentTime(), 6),
                    rep.getRepaymentTime()) < 0 && rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)) {

                history_xuqi = history_xuqi + 1;
            }


            //续期单数
            if (rep.getOrderRenewal() != null && rep.getOrderRenewal() == 9) {

                xuqi = xuqi + 1;
            }

            //按时还款单数
            if (DateUtils.compareDay(rep.getRepaymentTime(), rep.getRepaymentRealTime()) >= 0
                    && rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)) {

                anshi_huankuan = anshi_huankuan + 1;
            }

            //逾期总单数
            if (DateUtils.compareDay(rep.getRepaymentTime(), rep.getRepaymentRealTime()) < 0
                    || rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YYQ)) {

                yuqi_danshu = yuqi_danshu + 1;
            }

            //逾期已还单数
            if (DateUtils.compareDay(rep.getRepaymentTime(), rep.getRepaymentRealTime()) < 0
                    && rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)) {

                yuqi_yihuan = yuqi_yihuan + 1;
            }

            //当前待还单数
            if (rep.getRepaymentRealTime() == null) {
                daihuan_danshu = daihuan_danshu + 1;
            }

            //首逾数
            if (rep.getLateDay() > 0 && rep.getOrderRenewal() == null) {
                shouri_yuqishu = shouri_yuqishu + 1;
            }

            //还款数
            if (rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_YHK)) {
                huankuan_shu = huankuan_shu + 1;

                repaymented_amount_sum += rep.getRepaymentedAmount().intValue();
            }
            money_amount_sum += rep.getMoneyAmount().intValue();

            //续期还未还
            if (rep.getOrderRenewal() != null && rep.getOrderRenewal() == 9 && rep.getOrderStatus().equals(AssetOrderStatusHis.STATUS_DHK)) {

                xuqi_weihuan = xuqi_weihuan + 1;
            }


        }

        now_huikuanlv_jine = String.format("%.2f", (double) repaymented_amount_sum / money_amount_sum * 100);

        //首日逾期率
        shouri_yuqilv = String.format("%.2f", (double) shouri_yuqishu / loan_all_count * 100);

        //7日逾期数
        day7_yuqishu = yuqi_danshu - yuqi_yihuan;
        day7_yuqilv = String.format("%.2f", (double) day7_yuqishu / loan_all_count * 100);

        //当前回款率(含续期)
        now_huikuanlv_xuqi = String.format("%.2f", (double) (xuqi_weihuan + huankuan_shu) / loan_all_count * 100);

        //首借回款率(含续期
        shoujie_huikuanlv_xuqi = String.format("%.2f", (double) (shoujie_xuqi_weihuan + shoujie_huankuanshu) / shoujie * 100);

        //复借回款率(含续期
        fujie_huikuanlv_xuqi = String.format("%.2f", (double) (fujie_xuqi_weihuan + fujie_huankuanshu) / fujie * 100);


        Map<String, Object> retMap = new HashMap<String, Object>();

        retMap.put("loan_all_count", loan_all_count);
        retMap.put("shoujie", shoujie);
        retMap.put("fujie", fujie);
        retMap.put("history_xuqi", history_xuqi);
        retMap.put("xuqi", xuqi);
        retMap.put("anshi_huankuan", anshi_huankuan);
        retMap.put("yuqi_danshu", yuqi_danshu);
        retMap.put("yuqi_yihuan", yuqi_yihuan);
        retMap.put("daihuan_danshu", daihuan_danshu);
        retMap.put("shouri_yuqilv", shouri_yuqilv + "%");
        retMap.put("day7_yuqilv", day7_yuqilv + "%");
        retMap.put("now_huikuanlv_jine", now_huikuanlv_jine + "%");
        retMap.put("now_huikuanlv_xuqi", now_huikuanlv_xuqi + "%");
        retMap.put("shoujie_huikuanlv_xuqi", shoujie_huikuanlv_xuqi + "%");
        retMap.put("fujie_huikuanlv_xuqi", fujie_huikuanlv_xuqi + "%");
        retMap.put("loan_date", loan_date);
        retMap.put("repay_date", repay_date);
        return retMap;

    }


    /**
     * 按还款日合计
     *
     * @param detail
     * @return
     */
    private String fetchGroupKey(AssetRepaymentOrder detail) {
        return detail.getStatisticsLoanTime();
    }


    @Override
    public List<Map<String, Object>> exportExcelFileStatistics(Map<String, Object> params) {

        if (params != null) {

            List<AssetRepaymentOrder> details = repaymentStatisticsDao.queryStatic(params);
            Map<String, List<AssetRepaymentOrder>> detailmap = details.stream()
                    .collect(Collectors.groupingBy(d -> fetchGroupKey(d)));

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            Map<String, Object> myMap = new HashMap<String, Object>();

            if (params.get("repaymentDate") != null) {
                List<AssetRepaymentOrder> data_list = detailmap.get(String.valueOf(params.get("repaymentDate")));
                myMap = comMy(data_list);
                list.add(myMap);
                return list;
            } else {

                for (int i = 0; i < detailmap.size(); i++) {
                    String data_query = DateUtils.dateToString(DateUtils.addDay(new Date(), 0 - i), "yyyy-MM-dd");
                    List<AssetRepaymentOrder> data_list = detailmap.get(data_query);
                    if (data_list != null) {
                        myMap = comMy(data_list);
                        list.add(myMap);
                    }
                }
                return list;
            }
        }
        return null;
    }
}
