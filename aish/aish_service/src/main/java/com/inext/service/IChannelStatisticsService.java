package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.ChannelStatistics;

import java.util.Map;

public interface IChannelStatisticsService {
    void updatePreviousChannelStatistics();

    void updateTodayChannelStatistics();

    PageInfo<Map<String, Object>> findDiversionStatisticsHisPage(int channelId, Map<String, Object> params);
}
