package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.BorrowUser;
import com.inext.result.ApiServiceResult;
import com.inext.result.JsonResult;
import com.inext.view.params.PagingParams;
import com.inext.view.result.AssetBorrowOrderDetailsResult;
import com.inext.view.result.AssetBorrowOrderResult;
import com.inext.view.result.PagingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IAssetBorrowOrderService {


    List<AssetBorrowOrder> getOrderList(Map<String, Object> param);

    PageInfo<AssetBorrowOrder> getOrderPageList(Map<String, Object> params);

    AssetBorrowOrder getOrderById(Integer id);

    Map<String, Object> saveLoan(Map<String, String> params, BorrowUser bu);

    /**
     * 检查当前用户或当前设备号是否存在未还款完成的订单
     *
     * @param userId deviceNumber
     * @return 1：是；0：否
     */
    public Integer checkBorrow(Integer userId, String deviceNumber);

    public Map<String, String> findAuditFailureOrderByUserId(Integer userId);

    int updateByPrimaryKeySelective(AssetBorrowOrder assetBorrowOrder);

    /**
     * API接口获取分页列表
     *
     * @param pagingParams
     * @param param
     * @return
     */
    ApiServiceResult<PagingResult<AssetBorrowOrderResult>> getOrderList(PagingParams pagingParams, Map<String, Object> param);


    /**
     * API接口 获取订单详情
     *
     * @param id
     * @return
     */
    ApiServiceResult<AssetBorrowOrderDetailsResult> getById(Integer id);

    ApiServiceResult getOrderBefore(String equipmentId, BorrowUser bu);

    List<AssetBorrowOrder> findAll(HashMap<String, Object> params);

    /**
     * 代付
     * @param assetBorrowOrders
     * @return
     * @throws Exception
     */
    ApiServiceResult payForAnother(List<AssetBorrowOrder> assetBorrowOrders) throws Exception;

    /**
     * 查询代付进行对账
     * @param list
     * @return
     * @throws Exception
     */
    ApiServiceResult queryPayForAnother(List<AssetBorrowOrder> list) throws Exception;
    
    //逾期
    public void  overdueTask();
    
    public void overdue(AssetRepaymentOrder repayment) ;

	void sendRemindMessage(int gapDay);

    /**
     * 风控贷后回掉
     * @param params
     */
    Map fkDhCallback(Map<String,String> params);

    void auditOrder(Integer orderId, Integer status, String operatorRemark, String operatorAccount);

    //查询前一天注册，但是5项信息没有完全认证完成的客户
    void sendUnIdentification();

    /**
     * 获取逾期信息
     * @param param
     * @return
     */
    List<Map<String,String>> getOrderBlackList(Map<String, Object> param);
    /**
     * 没有设备号
     * @param bu
     * @return
     */
    ApiServiceResult getOrderBeforeNoEquipmentId(BorrowUser bu);

    List<AssetBorrowOrder> queyOrderStatus(String phone);
}
