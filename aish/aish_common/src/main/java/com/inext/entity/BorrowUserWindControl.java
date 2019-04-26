package com.inext.entity;

/**
 * app用户风控信息
 *
 * @author ttj
 */
public class BorrowUserWindControl {
    private int id;//`id` int(19) NOT NULL AUTO_INCREMENT,
    private Integer userId;//  `userId` int(19) DEFAULT NULL COMMENT 'app用户id',
    private Integer isBlack;//  `isBlack` int(19) DEFAULT NULL COMMENT '是否命中黑名单（1：是，0：否）',
    private Integer shieldMinute;//  `shieldMinute` int(19) DEFAULT '0' COMMENT '同盾分（分值越高风险越大）',
    private Integer shieldPhoneIsBlack;//  `shieldPhoneIsBlack` int(19) DEFAULT '0' COMMENT '同盾手机网贷黑名单个数',
    private Integer shieldCardIsBlack;//  `shieldCardIsBlack` int(19) DEFAULT '0' COMMENT '同盾身份证号网贷黑名单个数',
    private String whiteKnight;// `whiteKnight` varchar(255) DEFAULT NULL COMMENT '白骑士状态',
    private Integer loanNum;//  `loanNum` int(19) DEFAULT '0' COMMENT '91征信借款总数',
    private Integer rejectionNum;//  `rejectionNum` int(19) DEFAULT '0' COMMENT '征信拒单记录数',
    private String rejectionRatio;//  `rejectionRatio` varchar(124) DEFAULT NULL COMMENT '拒单占比',
    private Integer overdueNum;//  `overdueNum` int(19) DEFAULT NULL COMMENT '逾期记录数',
    private String overdueRatio;//  `overdueRatio` varchar(255) DEFAULT NULL COMMENT '91征信逾期记录占比',
    private String honeyJar;//  `honeyJar` varchar(512) DEFAULT NULL COMMENT '蜜罐黑中介分（分值越小风险越大）：',
    private String yixin;//  `yixin` varchar(512) DEFAULT NULL COMMENT '宜信',
    private String zzc;//  `zzc` varchar(255) DEFAULT NULL COMMENT '中智城',
    private Integer quota;//  `quota` int(19) DEFAULT NULL COMMENT '机审建议额度',
    private Integer score;//  `score` int(19) DEFAULT NULL COMMENT '机审评分',

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(Integer isBlack) {
        this.isBlack = isBlack;
    }

    public Integer getShieldMinute() {
        return shieldMinute;
    }

    public void setShieldMinute(Integer shieldMinute) {
        this.shieldMinute = shieldMinute;
    }

    public Integer getShieldPhoneIsBlack() {
        return shieldPhoneIsBlack;
    }

    public void setShieldPhoneIsBlack(Integer shieldPhoneIsBlack) {
        this.shieldPhoneIsBlack = shieldPhoneIsBlack;
    }

    public Integer getShieldCardIsBlack() {
        return shieldCardIsBlack;
    }

    public void setShieldCardIsBlack(Integer shieldCardIsBlack) {
        this.shieldCardIsBlack = shieldCardIsBlack;
    }

    public String getWhiteKnight() {
        return whiteKnight;
    }

    public void setWhiteKnight(String whiteKnight) {
        this.whiteKnight = whiteKnight;
    }

    public Integer getLoanNum() {
        return loanNum;
    }

    public void setLoanNum(Integer loanNum) {
        this.loanNum = loanNum;
    }

    public Integer getRejectionNum() {
        return rejectionNum;
    }

    public void setRejectionNum(Integer rejectionNum) {
        this.rejectionNum = rejectionNum;
    }

    public String getRejectionRatio() {
        return rejectionRatio;
    }

    public void setRejectionRatio(String rejectionRatio) {
        this.rejectionRatio = rejectionRatio;
    }

    public Integer getOverdueNum() {
        return overdueNum;
    }

    public void setOverdueNum(Integer overdueNum) {
        this.overdueNum = overdueNum;
    }

    public String getOverdueRatio() {
        return overdueRatio;
    }

    public void setOverdueRatio(String overdueRatio) {
        this.overdueRatio = overdueRatio;
    }

    public String getHoneyJar() {
        return honeyJar;
    }

    public void setHoneyJar(String honeyJar) {
        this.honeyJar = honeyJar;
    }

    public String getYixin() {
        return yixin;
    }

    public void setYixin(String yixin) {
        this.yixin = yixin;
    }

    public String getZzc() {
        return zzc;
    }

    public void setZzc(String zzc) {
        this.zzc = zzc;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
