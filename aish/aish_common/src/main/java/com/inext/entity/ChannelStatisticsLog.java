package com.inext.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @version
 *
 */
public class ChannelStatisticsLog implements Serializable {
    private Integer id;
    private Integer channelId;
    private Date statisticsDate;
    private Integer type;//1:pv ;2:uv
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getStatisticsDate() {
		return statisticsDate;
	}
	public void setStatisticsDate(Date statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

}
