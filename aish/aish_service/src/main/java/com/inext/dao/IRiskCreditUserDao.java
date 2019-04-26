package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.RiskCreditUser;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: jzhang
 * @Date: 2018-04-04 0004 上午 09:51
 */
public interface IRiskCreditUserDao extends BaseDao<RiskCreditUser> {
    @Select("SELECT * from risk_credit_user where jxl_token is not NULL and jxl_rpt_id is null")
    List<RiskCreditUser> getJxlReportToken();

    @Insert("INSERT INTO risk_credit_user SET user_id = #{user.userId}, asset_id = #{user.assetId}, ip = #{user.ip}, jxl_token = ( SELECT token FROM borrow_userjxl WHERE userId = #{user.userId} ORDER BY createTime DESC LIMIT 1) ")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    int insertUser(@Param("user") RiskCreditUser user);

    @Select("SELECT id,user_id,asset_id,ip,status,jxl_status,fk_status,jxl_token from risk_credit_user where status =0 LIMIT 60")
    List<RiskCreditUser> getStatusIs0Top500();

    @Select("SELECT id,user_id,asset_id,ip,status,jxl_status,fk_status,jxl_token from risk_credit_user where jxl_status =0 and jxl_token is not NULL LIMIT 60")
    List<RiskCreditUser> getJxlStatusIs0Top500();

    @Select("SELECT id,user_id,asset_id,ip,status,jxl_status,fk_status,jxl_token from risk_credit_user where fk_status =0 LIMIT 60")
    List<RiskCreditUser> getFkStatusIs0Top500();

    @Select("SELECT name,phone from borrow_userphone where userId=#{userId}")
    List<Map<String,String>> getContacts(@Param("userId") int userId);
    @Select("SELECT * FROM risk_credit_user where `status`=1 and jxl_status=1 and fk_status=2 and user_id=#{0} ORDER BY id DESC LIMIT 1")
    RiskCreditUser getNewestByUserId(String userId);
    @Select("SELECT * FROM risk_credit_user force INDEX(INDEX_USER_ID) where `status`=1 and jxl_status=1 and fk_status=2 and user_id in ( ${userIds} ) ORDER BY id DESC LIMIT 1")
    List<RiskCreditUser> getNewestByUserIds(@Param("userIds") String userIds);

    @Select("SELECT * FROM risk_credit_user where `status`=1 and jxl_status=1 and fk_status=2 and asset_id=#{0} ORDER BY id DESC LIMIT 1")
    RiskCreditUser getByAssetId(String userId);


    @Select("SELECT name as contact_name,phone as contact_phone, DATE_FORMAT(createTime,'%Y-%m-%d %H:%i:%s') as update_time from borrow_userphone where userId=#{userId}")
    List<Map<String,String>> getContactsForWuqiyou(@Param("userId") int userId);
}
