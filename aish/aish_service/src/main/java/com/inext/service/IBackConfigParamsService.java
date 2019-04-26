package com.inext.service;


import com.inext.entity.BackConfigParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IBackConfigParamsService {
	/**
	 * 
	 * @param params
	 *            sysType参数分类ASSETS_TYPE是资产类型
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
	int updateValue(List<BackConfigParams> list, String type);
	Map<String,String > getBackConfig(String type, String key);

	/**
	 * 根据id获取后台配置对象
	 * @param id
	 * @return
	 */
	BackConfigParams getBackConfigById(Integer id);

	/**
	 * 修改后台配置对象
	 * @param backConfigParams
	 * @return
	 */
	int updateBackConfigParams(BackConfigParams backConfigParams);
}
