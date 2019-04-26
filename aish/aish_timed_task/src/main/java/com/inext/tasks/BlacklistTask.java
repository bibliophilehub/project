//package com.inext.tasks;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.inext.service.IAssetBorrowOrderService;
//import com.inext.utils.DateUtils;
//import com.inext.utils.HttpUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import javax.annotation.Resource;
//import java.util.*;
//
///**
// * @Author: jzhang
// * @Date: 2018-03-28 0028 上午 10:53
// */
//
//@Configuration
//@EnableScheduling // 启用定时任务
//public class BlacklistTask {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(BlacklistTask.class);
//    @Resource
//    IAssetBorrowOrderService iAssetBorrowOrderService;
//
//    @Value("${hmd.pl_url}")
//    private String hmd_pl_url;
//    @Value("${hmd.one_url}")
//    private String hmd_one_url;
//    @Value("${hmd.source_id}")
//    private String sourceId;
//
//    @Scheduled(cron = "0 0 3 */3 * ? ")
//    public void doOrderRisk() {
//        saveBatchOpen();
//    }
//
//    /**
//     * 批量保存用户数据到黑名单
//     */
//    public void saveBatchOpen() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("status", 7);
//        map.put("leanEndTime", DateUtils.formatDateTime(new Date()));
//        List<Map<String,String>> list = iAssetBorrowOrderService.getOrderBlackList(map);
//
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        resultMap.put("sourceId", sourceId);//生产环境：leId,测试环境：test
//        if (list != null && list.size() > 0) {
//            Map<String, String> maps = new HashMap<>();
//            int listSize = list.size();
//            if (list.size() > 500) {
//                int toIndex = 500;
//                for (int i = 0; i < listSize; i += 500) {
//                    if (i + 100 > listSize) {        //作用为toIndex最后没有100条数据则剩余几条newList中就装几条
//                        toIndex = listSize - i;
//                    }
//                    List newList = list.subList(i, i + toIndex);
//                    map.put("users", newList);
//                    maps.put("jsonString", JSON.toJSONString(map));
//                    try {
//                        LOGGER.info("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单请求:{}", JSON.toJSONString(maps));
//                        JSONObject result = HttpUtil.postForForm(hmd_pl_url, maps);
//                        LOGGER.info("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单响应:{}", JSON.toJSONString(result));
//                    } catch (Exception e) {
//                        LOGGER.error("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单异常:{}", e.getMessage());
//                    }
//                }
//            } else {
//                resultMap.put("users", list);
//                maps.put("jsonString", JSON.toJSONString(resultMap));
//                try {
//                    LOGGER.info("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单请求:{}", JSON.toJSONString(maps));
//                    JSONObject result = HttpUtil.postForForm(hmd_pl_url, maps);
//                    LOGGER.info("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单响应:{}", JSON.toJSONString(result));
//                } catch (Exception e) {
//                    LOGGER.error("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单异常:{}", e.getMessage());
//                }
//            }
//        }
//    }
//
//    /**
//     * 保存用户数据到黑名单
//     */
//    public void saveOpen() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("status", 7);
//        map.put("leanEndTime", DateUtils.formatDateTime(new Date()));
//        List<Map<String, String>> list = iAssetBorrowOrderService.getOrderBlackList(map);
//        if (list != null && list.size() > 0) {
//            int listSize = list.size();
//            for (int i = 0; i < listSize; i++) {
//                list.get(i).put("sourceId", sourceId);
//                try {
//                    LOGGER.info("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单请求:{}", JSON.toJSONString(list.get(i)));
//                    JSONObject result = HttpUtil.postForForm(hmd_one_url, list.get(i));
//                    LOGGER.info("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单响应:{}", JSON.toJSONString(result));
//                } catch (Exception e) {
//                    LOGGER.error("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单异常:{}", e.getMessage());
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        BlacklistTask task = new BlacklistTask();
//        //task.doOrderRisk();
//        task.saveOpen();
//       /* List<OrderBlacklistDto> list = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            OrderBlacklistDto dto = new OrderBlacklistDto();
//            dto.setIdCardNo("33090319900101111" + i);
//            dto.setUserName("张三" + i);
//            dto.setUserPhone("1821078771" + i);
//            list.add(dto);
//        }
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        resultMap.put("sourceId ", "le_test");//生产环境：leId,测试环境：test
//        resultMap.put("users", list);
//        LOGGER.info("BlacklistTask >>> doOrderRisk 用户逾期添加黑名单请求:{}", JSON.toJSONString(resultMap));
//        System.out.println(JSON.toJSONString(resultMap));*/
//    }
//
//}
