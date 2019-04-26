package com.inext.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.inext.entity.ChannelBalanceStatistics;
import com.inext.entity.ChannelInfo;
import com.inext.entity.ChannelLoanQuota;

/**
 * Created by user on 2018/1/6.
 */
public interface IChannelService {
	
	PageInfo<Map<String, Object>> findStatisticsPage(Map<String, Object> params);

    Map<String, Object> getStatisticsByChannelId(int channelId);

    public List<Map<String, Object>> getStatisticsList(Map<String, Object> params);

    public void updateById(ChannelInfo channel);

    public int insert(ChannelInfo channel);

    PageInfo<ChannelInfo> getPageList(Map<String, Object> params);
    
    List<ChannelInfo> getList(Map<String, Object> params);

    ChannelInfo getChannelById(Integer id);
    
    ChannelInfo getChannelByCode(String channelCode);

    PageInfo<ChannelBalanceStatistics> findBalanceStatisticsPage(Map<String, Object> params);

    Map<String, Object> findBalanceStatistics(Map<String, Object> params);


	List<ChannelBalanceStatistics> channelBalanceStatisticsExcel(Map<String, Object> param);

    /**
     * 新增渠道借款额度记录
     * @param channelLoanQuota
     */
	int insertChannelLoanQuota(ChannelLoanQuota channelLoanQuota);

    /**
     * 更新渠道借款额度记录
     * @param channelLoanQuota
     * @return
     */
	int updateChannelLoanQuota(ChannelLoanQuota channelLoanQuota);

    /**
     * 查询渠道借款额度记录
     * @param channelId
     * @return
     */
    ChannelLoanQuota getChannelLoanQuotaByChannelId(Integer channelId);


}
