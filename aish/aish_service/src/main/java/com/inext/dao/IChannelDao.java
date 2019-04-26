package com.inext.dao;


import com.github.pagehelper.PageInfo;
import com.inext.configuration.BaseDao;
import com.inext.entity.ChannelBalanceStatistics;
import com.inext.entity.ChannelInfo;
import com.inext.entity.ChannelLoanQuota;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/1/6.
 */
public interface IChannelDao extends BaseDao<ChannelInfo> {

    public void updateById(ChannelInfo channel);

    public List<ChannelInfo> getList(Map<String, Object> params);

    public ChannelInfo getChannelById(Integer id);

    public ChannelInfo getChannelByCode(String channelCode);

    public List<Map<String, Object>> getStatisticsList(Map<String, Object> params);

    List<Map<String, Object>> channelBalanceStatistics(Map<String, Object> params);

    PageInfo<ChannelBalanceStatistics> findBalanceStatisticsPage(Map<String, Object> params);

    Map<String, Object> getStatisticsByChannelId(int channelId);

    /**
     * 获取本地渠道分控分数信息
     *
     * @return
     */
    List<Map<String, String>> queryScore(Integer id);

    /**
     * 查询渠道借款额度
     * @param channelId
     * @return
     */
    ChannelLoanQuota getChannelLoanQuotaByChannelId(int channelId);

    /**
     * 更新渠道借款额度
     * @param channelLoanQuota
     * @return
     */
    int updateChannelLoanQuotaById(ChannelLoanQuota channelLoanQuota);
}
