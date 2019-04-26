package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.BackUser;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("backUserDao")
public interface IBackUserDao extends BaseDao<BackUser> {
    public BackUser findByAccount(HashMap<String, Object> params);

    public BackUser findByAccountNotId(HashMap<String, Object> params);

    public BackUser findByNickName(HashMap<String, Object> params);

    public BackUser findByNickNameNotId(HashMap<String, Object> params);

    public List<BackUser> getPageList(Map<String, Object> params);

    public List<BackUser> getListByRoleId(Integer roleId);
}
