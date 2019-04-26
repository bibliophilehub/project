package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.BorrowUser;
import com.inext.result.ApiServiceResult;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface IAssetBorrowOrderDao extends BaseDao<AssetBorrowOrder> {

    AssetBorrowOrder getOrderById(Integer id);

    List<AssetBorrowOrder> getOrderList(Map<String, Object> param);

    List<AssetBorrowOrder> findParams(HashMap<String, Object> params);

    AssetBorrowOrder findAuditFailureOrderByUserId(HashMap<String, Object> params);

    int insertOrder(AssetBorrowOrder order);

    List<AssetBorrowOrder> queryBorrowOrderList();

    //根据商户流水号查询订单
    AssetBorrowOrder getOrderByNoOrder(String noOrder);

    /**
     * 查询前一天注册，但是5项信息没有完全认证完成的客户
     * @return
     */
    @Select("SELECT * FROM borrow_user WHERE DATE_FORMAT(createTime, '%Y-%m-%d') = DATE_SUB(curdate(), INTERVAL 1 DAY) AND CONCAT(isVerified,isPhone,isCard,isOperator,isZhima)!='11111'")
    List<BorrowUser> getYesterdayUnIdentification();

    /**
     * 放款失败
     * @param param
     * @return
     */
    List<AssetBorrowOrder> getOrderListByFail(Map<String, Object> param);
    /**
     * 获取逾期信息
     * @param param
     * @return
     */
    List<Map<String,String>> getOrderBlackList(Map<String, Object> param);

    /**
     * 查询订单状态
     * @param phone
     * @return
     */
    List <AssetBorrowOrder> queyOrderStatus(String phone);
}
