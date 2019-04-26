package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.ChannelStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ChannelStatisticsDao extends BaseDao<ChannelStatistics> {
    ChannelStatistics generateChannelStatisticsByDay(@Param("channelId") Integer channelId, @Param("statisticsDate") Date statisticsDate);

    ChannelStatistics generateChannelStatisticsByHour(@Param("channelId") Integer channelId, @Param("statisticsDate") Date statisticsDate);

    ChannelStatistics selectByChannelIdAndstatisticsDate(@Param("channelId") Integer channelId, @Param("statisticsDate") Date statisticsDate);

    List<ChannelStatistics> selectByParams(Map<String, Object> params);

    List<Map<String, Object>> getDiversionStatisticsHisList(Integer channelId);
}
