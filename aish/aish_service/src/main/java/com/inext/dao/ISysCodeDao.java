package com.inext.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ISysCodeDao {


    List<Map<String, Object>> selectArea(Map<String, Object> map);

    List<Map<String, Object>> queryUserEducation(@Param("type") String type);

}
