package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.BankAllInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IBankAllInfoDao extends BaseDao<BankAllInfo> {

    BankAllInfo findBankAllInfoByName(@Param("bankName") String bankName);

    List<BankAllInfo> findBankAllInfo();

}
