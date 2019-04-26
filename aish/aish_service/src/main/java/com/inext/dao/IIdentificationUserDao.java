package com.inext.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface IIdentificationUserDao {


    List<Map<String,Object>> getOrderList(Map<String, Object> params);

}
