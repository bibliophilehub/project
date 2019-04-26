package com.inext.service.impl;

import java.util.List;
import java.util.Map;

import com.inext.dao.IChannelBalanceStatisticsDao;
import com.inext.dao.IChannelLoanQuotaDao;
import com.inext.entity.ChannelBalanceStatistics;
import com.inext.entity.ChannelLoanQuota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.dao.IChannelDao;
import com.inext.entity.ChannelInfo;
import com.inext.service.IChannelService;

/**
 * 借条业务接口
 */
@Service
public  class ChannelService implements IChannelService {

    @Autowired
    private IChannelDao channelDao;
    @Autowired
    private IChannelBalanceStatisticsDao channelBalanceStatisticsDao;
    @Autowired
    private IChannelLoanQuotaDao channelLoanQuotaDao;

    @Override
    public void updateById(ChannelInfo channel) {
        channelDao.updateById(channel);
    }


    @Override
    public PageInfo<ChannelInfo> getPageList(Map<String, Object> params) {
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
        List<ChannelInfo> list = this.channelDao.getList(params);
        PageInfo<ChannelInfo> pageInfo = new PageInfo<ChannelInfo>(list);
        return pageInfo;

    }


    @Override
    public ChannelInfo getChannelById(Integer id) {

        return channelDao.getChannelById(id);
    }


    @Override
    public int insert(ChannelInfo channel) {
        return channelDao.insert(channel);
    }


	@Override
	public ChannelInfo getChannelByCode(String channelCode) {
		return channelDao.getChannelByCode(channelCode);
	}


	@Override
	public List<ChannelInfo> getList(Map<String, Object> params) {
		return channelDao.getList(params);
	}


	@Override
	public PageInfo<Map<String, Object>> findStatisticsPage(Map<String, Object> params) {
		
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
        List<Map<String, Object>> list = this.channelDao.getStatisticsList(params);
        
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
        
        return pageInfo;
	}

	@Override
    public Map<String, Object> getStatisticsByChannelId(int channelId) {
        return this.channelDao.getStatisticsByChannelId(channelId);
    }

	@Override
	public List<Map<String, Object>> getStatisticsList(Map<String, Object> params) {
		
		return channelDao.getStatisticsList(params);
	}

    @Override
    public PageInfo<ChannelBalanceStatistics> findBalanceStatisticsPage(Map<String, Object> params) {
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

        List<ChannelBalanceStatistics> list = this.channelBalanceStatisticsDao.getBalanceStatisticsList(params);
        PageInfo<ChannelBalanceStatistics> pageInfo = new PageInfo<ChannelBalanceStatistics>(list);
        return pageInfo;
    }

    @Override
    public Map<String, Object> findBalanceStatistics(Map<String, Object> params) {
        return channelBalanceStatisticsDao.findBalanceStatistics(params);
    }
    
    @Override
	public List<ChannelBalanceStatistics> channelBalanceStatisticsExcel(Map<String, Object> param) {
		return channelBalanceStatisticsDao.getBalanceStatisticsList(param);
	}

    /**
     * 新增渠道借款额度记录
     * @param channelLoanQuota
     */
    @Override
    public int insertChannelLoanQuota(ChannelLoanQuota channelLoanQuota) {
       return channelLoanQuotaDao.insert(channelLoanQuota);
    }

    /**
     * 更新渠道借款额度记录
     * @param channelLoanQuota
     */
    @Override
    public int updateChannelLoanQuota(ChannelLoanQuota channelLoanQuota) {
         return channelDao.updateChannelLoanQuotaById(channelLoanQuota);
    }

    /**
     * 查询渠道借款额度记录
     * @param channelId
     * @return
     */
    @Override
    public ChannelLoanQuota getChannelLoanQuotaByChannelId(Integer channelId) {
        return channelDao.getChannelLoanQuotaByChannelId(channelId);
    }

}
