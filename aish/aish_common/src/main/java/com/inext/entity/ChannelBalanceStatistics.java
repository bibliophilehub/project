package com.inext.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @version
 *
 */
public class ChannelBalanceStatistics implements Serializable {
    
    
    private Integer id;
    private String channelCode;
    private String channelName;
    private Integer pvCount;
    private Integer uvCount;
    private String  reUvRate;
    private Integer registerCount;
    private Integer applyCount;
    private Integer newUserCount;
    private Integer oldUserCount;
    private Integer loanCount;
    private Integer newLoanCount;
    private Integer oldLoanCount;
    private Integer loanMoneyMount;
    private Date statisticsDate;
    
	public String getReUvRate() {
		return reUvRate;
	}
	public void setReUvRate(String reUvRate) {
		this.reUvRate = reUvRate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Integer getPvCount() {
		return pvCount;
	}
	public void setPvCount(Integer pvCount) {
		this.pvCount = pvCount;
	}
	public Integer getUvCount() {
		return uvCount;
	}
	public void setUvCount(Integer uvCount) {
		this.uvCount = uvCount;
	}
	public Integer getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}
	public Integer getApplyCount() {
		return applyCount;
	}
	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}
	public Integer getNewUserCount() {
		return newUserCount;
	}
	public void setNewUserCount(Integer newUserCount) {
		this.newUserCount = newUserCount;
	}
	public Integer getOldUserCount() {
		return oldUserCount;
	}
	public void setOldUserCount(Integer oldUserCount) {
		this.oldUserCount = oldUserCount;
	}
	public Integer getLoanCount() {
		return loanCount;
	}
	public void setLoanCount(Integer loanCount) {
		this.loanCount = loanCount;
	}
	public Integer getNewLoanCount() {
		return newLoanCount;
	}
	public void setNewLoanCount(Integer newLoanCount) {
		this.newLoanCount = newLoanCount;
	}
	public Integer getOldLoanCount() {
		return oldLoanCount;
	}
	public void setOldLoanCount(Integer oldLoanCount) {
		this.oldLoanCount = oldLoanCount;
	}
	public Integer getLoanMoneyMount() {
		return loanMoneyMount;
	}
	public void setLoanMoneyMount(Integer loanMoneyMount) {
		this.loanMoneyMount = loanMoneyMount;
	}
	public Date getStatisticsDate() {
		return statisticsDate;
	}
	public void setStatisticsDate(Date statisticsDate) {
		this.statisticsDate = statisticsDate;
	}
    
    
    
    

}
