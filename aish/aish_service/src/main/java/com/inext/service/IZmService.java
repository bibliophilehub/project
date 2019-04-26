package com.inext.service;

import com.inext.result.ApiGxbServiceResult;
import com.inext.result.ApiServiceResult;

public interface IZmService {

	/**
	 * 芝麻授信
	 *
	 * @param isNew  是否新版  是：1，否：0  因为要兼容老版本的app 授权地址所以返回值可能存在两个协议的地址
	 * @param userId
	 * @return
	 */
	public ApiServiceResult creditReportZm(String isNew, Integer userId) throws Exception;

	public void zmAuthExpire();

	public ApiGxbServiceResult creditReportGxbZm(String isNew, Integer userId) throws Exception;

}
