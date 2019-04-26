package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.BackConfigParams;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Repository
public interface IBackConfigParamsDao extends BaseDao<BackConfigParams> {

    @Select("select  *  from back_config_params  where sys_type=#{0} order by sys_type,id asc")
    public List<BackConfigParams> findParamsBySysType(String sysType);


    @Select("select  sys_type  from back_config_params GROUP by sys_type")
    public List<String> findSysType();

	/**
	 *
	 * @param params
	 *            sysType参数分类
	 * @return
	 */
	public List<BackConfigParams> findParams(HashMap<String, Object> params);

	public List<BackConfigParams> findParamsBySyskeys(HashMap<String, Object> params);

	/**
	 * 更新
	 *
	 * @param list
	 * @return
	 */
	public int updateValue(List<BackConfigParams> list);

	BackConfigParams findById(Integer id);
}
