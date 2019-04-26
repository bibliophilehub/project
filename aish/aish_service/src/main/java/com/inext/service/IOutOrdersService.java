package com.inext.service;



import com.inext.entity.OutOrders;

import java.util.HashMap;
import java.util.List;


public interface IOutOrdersService {
	/**
	 * 发出请求
	 * 
	 * @param orders
	 * @return
	 */
	public int insert(OutOrders orders);
	

	/**
	 * 更新
	 *
	 * @param orders
	 * @return
	 */
	public int update(OutOrders orders);
	
	/**
	 * 更新
	 *
	 * @param orders
	 * @return
	 */
	public int updateByOrderNo(OutOrders orders);

	/**
	 * 根据主键查询
	 * 
	 * @param id
	 * @return
	 */
	public OutOrders findById(Integer id);
	/**
	 * 查询
	 *
	 * @param
	 * @return
	 */
	public OutOrders findByOrderNo(String orderNo);
	
	
	
	/**
	 * 通过表名发出请求
	 * 
	 * @param orders
	 * @return
	 */
	public int insertByTablelastName(OutOrders orders);
	/**
	 * 通过表名更新
	 * @param orders
	 * @param
	 * @return
	 */
	public int updateByTablelastName(OutOrders orders);
	
	/**
	 * 更新
	 *
	 * @param orders
	 * @return
	 */
	public int updateByOrderNoByTablelastName(OutOrders orders);

	/**
	 * 根据主键查询
	 * 
	 * @param id
	 * @return
	 */
	public OutOrders findByIdByTablelastName(Integer id, String TablelastName);
	/**
	 * 查询
	 *
	 * @param
	 * @return
	 */
	public OutOrders findByOrderNoByTablelastName(String orderNo, String TablelastName);
	
	/**
	 * 查询支付未回调的放款详情
	 * @param params
	 * @return
	 */
	List<OutOrders> findByOrderNoByTablelast(HashMap<String, Object> params);

	int insertSelectiveStatus(OutOrders orders);
}
