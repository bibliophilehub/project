package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.PlatfromAdvise;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IAdviseDao extends BaseDao<PlatfromAdvise> {

    List<PlatfromAdvise> getList(Map<String, Object> param);

    void savePlatfromAdvise(PlatfromAdvise pa);


}
