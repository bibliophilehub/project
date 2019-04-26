package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.ChannelLoanQuota;

import java.util.List;
import java.util.Map;

/**
 * 渠道借款额度配置
 */
public interface IChannelLoanQuotaDao extends BaseDao<ChannelLoanQuota> {

    List<ChannelLoanQuota> getList(Map<String, Object> params);

    ChannelLoanQuota getChannelById(Integer id);

}
