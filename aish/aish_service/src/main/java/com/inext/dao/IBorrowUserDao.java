package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.BorrowUser;
import com.inext.entity.BorrowUserJxl;
import com.inext.entity.BorrowUserMessage;
import com.inext.entity.BorrowUserPhone;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IBorrowUserDao extends BaseDao<BorrowUser> {

    BorrowUser getBorrowUserByPhone(Map<String, Object> map);

    BorrowUser getBorrowUserByUId(Integer id);

    void saveBorrowUser(BorrowUser bu);

    void updateBorrowUser(BorrowUser bu);

    void saveBorrowUserPhone(List<BorrowUserPhone> list);

    void saveBorrowUserJxl(BorrowUserJxl bj);

    List<BorrowUserMessage> getBorrowUserMessage(Map<String, Object> map);

    List<Map<String, String>> queryBorrowUser(Map<String, Object> map);

    void updateBorrowUserMessage(BorrowUserMessage borrowUserMessage);

    BorrowUser getBorrowUserById(Map<String, Object> map);

    List<BorrowUserPhone> queryBorrowUserPhone(Map<String, Object> map);


    @Select("SELECT * FROM borrow_user where isOperator=1 and isWindControl is null LIMIT 1000")
    @Results({
            @Result(property = "userName", column = "userName")})
    List<BorrowUser> getNeedCreditUser(HashMap<String, Object> params);

    BorrowUser gerBorrowUserByCardNumber(String cardNumber);

	void updateZmExpiredUser(HashMap<String, Object> params);
	
	List<BorrowUser> getUserByPhoneMi(String phone_md5);
	
	List<BorrowUser> getXuyao();

    BorrowUser getUserByIdForAddress(Integer id);

    /** add by */
    List<BorrowUser> getAuthList();

    /**
     * 用户导出
     * @param map
     * @return
     */
    List<Map<String,String>> exportBorrowUser(Map<String, Object> map);

    /**
     * 更新运营商认证状态
     * @param map
     * @return
     */
    int updateBorrowUserMobileAuthStatus(Map<String, Object> map);

    List<Map<String, String>> querySignleBorrowUser(Map<String, Object> map);

}
