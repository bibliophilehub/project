package com.inext.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.ChannelStatisticsDao;
import com.inext.dao.IChannelDao;
import com.inext.entity.ChannelInfo;
import com.inext.entity.ChannelStatistics;
import com.inext.service.IChannelStatisticsService;
import com.inext.utils.DateUtil;
import com.inext.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("channelStatisticsServiceImpl")
public class ChannelStatisticsServiceImpl implements IChannelStatisticsService {

    @Autowired
    private ChannelStatisticsDao channelStatisticsDao;
    @Autowired
    private IChannelDao channelDao;

    @Override
    public void updatePreviousChannelStatistics() {
        List<ChannelStatistics> channelStatisticsList = channelStatisticsDao.selectAll();
        List<ChannelInfo> channelInfoList = channelDao.selectAll();
        Date yesterday = DateUtil.addDay(new Date(), -1);

        // 若渠道统计数据表是空的，则根据已有渠道生成自渠道创建日至昨日的每天的渠道统计数据
        if(channelStatisticsList == null || channelStatisticsList.size() == 0) {
            for(ChannelInfo channelInfo : channelInfoList) {
                Date channelCreateTime = channelInfo.getCreateTime();
                while (DateUtils.daysBetween(channelCreateTime, yesterday) > -1) {
                    ChannelStatistics channelStatistics = channelStatisticsDao.generateChannelStatisticsByDay(channelInfo.getId(), channelCreateTime);
                    channelStatisticsDao.insertSelective(channelStatistics);
                    channelCreateTime = DateUtils.addDay(channelCreateTime, 1);
                }
            }
        } else {
            for (ChannelInfo channelInfo : channelInfoList) {
                ChannelStatistics channelStatistics = channelStatisticsDao.generateChannelStatisticsByDay(channelInfo.getId(), yesterday);
                ChannelStatistics oldChannelStatistics = channelStatisticsDao.selectByChannelIdAndstatisticsDate(channelStatistics.getChannelId(), channelStatistics.getStatisticsDate());
                if(oldChannelStatistics == null) {
                    channelStatisticsDao.insertSelective(channelStatistics);
                } else {
                    channelStatistics.setId(oldChannelStatistics.getId());
                    channelStatisticsDao.updateByPrimaryKeySelective(channelStatistics);
                }
            }
        }
    }

    @Override
    public void updateTodayChannelStatistics() {
        List<ChannelInfo> channelInfoList = channelDao.selectAll();
        Date today = new Date();

        for (ChannelInfo channelInfo : channelInfoList) {
            ChannelStatistics channelStatistics = channelStatisticsDao.generateChannelStatisticsByHour(channelInfo.getId(), today);
            ChannelStatistics oldChannelStatistics = channelStatisticsDao.selectByChannelIdAndstatisticsDate(channelStatistics.getChannelId(), channelStatistics.getStatisticsDate());
            if(oldChannelStatistics == null) {
                channelStatisticsDao.insertSelective(channelStatistics);
            } else {
                channelStatistics.setId(oldChannelStatistics.getId());
                channelStatisticsDao.updateByPrimaryKeySelective(channelStatistics);
            }
        }
    }

    //获取分流渠道商的统计数据
    @Override
    public PageInfo<Map<String, Object>> findDiversionStatisticsHisPage(int channelId, Map<String, Object> params) {
        if (params != null) {
            int currentPage = Constants.INITIAL_CURRENT_PAGE;
            int pageSize = Constants.INITIAL_PAGE_SIZE;
            if (params.get(Constants.CURRENT_PAGE) != null && !"".equals(params.get(Constants.CURRENT_PAGE))) {
                currentPage = com.inext.utils.StringUtils.getInteger(params.get(Constants.CURRENT_PAGE) + "");
            }
            if (params.get(Constants.PAGE_SIZE) != null && !"".equals(params.get(Constants.PAGE_SIZE))) {
                pageSize = com.inext.utils.StringUtils.getInteger(params.get(Constants.PAGE_SIZE) + "");
            }
            PageHelper.startPage(currentPage, pageSize);
        }
        List<Map<String, Object>> list = this.channelStatisticsDao.getDiversionStatisticsHisList(channelId);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
        return pageInfo;
    }
}
