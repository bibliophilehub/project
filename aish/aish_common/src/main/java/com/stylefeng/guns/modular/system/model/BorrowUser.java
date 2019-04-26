
package com.stylefeng.guns.modular.system.model;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * app用户对象
 *
 * @author ttj
 */
public class BorrowUser implements Serializable
{
    private static final long serialVersionUID = -6193288028931761228L;
    public static Map<String, String> isBlackMap = new HashMap<String, String>();// 是否黑名单

    static
    {
        isBlackMap.put("0", "否");
        isBlackMap.put("1", "是");
    }

    @Id
    private Integer id;//`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序列号',
    private String userAccount;//  `userAccount` varchar(32) DEFAULT NULL COMMENT '用户名',
    private String userPassword;// `userPassword` varchar(50) DEFAULT NULL COMMENT '用户密码',
    private String transactionPassword;// `transactionPassword` varchar(50) DEFAULT NULL COMMENT '交易密码',
    private String userPhone;// `userPhone` varchar(50) DEFAULT NULL COMMENT '用户手机号码',
    private String userName;// `userName` varchar(255) DEFAULT NULL COMMENT '用户姓名',
    private String userCardNo;// `userCardNo` varchar(50) DEFAULT NULL COMMENT '用户身份证号',
    private Integer userEducation;//  `userEducation` int(19) DEFAULT '0' COMMENT '学历',
    private Integer userMarriage;// `userMarriage` int(19) DEFAULT NULL COMMENT '婚姻状态(1:已婚，0：未婚)',
    private String userProvince;// `userProvince` varchar(255) DEFAULT NULL COMMENT '省',
    private String userCity;// `userCity` varchar(255) DEFAULT NULL COMMENT '市',
    private String userArea;// `userArea` varchar(255) DEFAULT NULL COMMENT '区',、
    private String userAddress;//用户详细地址
    private Integer lengthOfStay;//  `lengthOfStay` int(19) DEFAULT '0' COMMENT '居住时长',
    private Integer isBlack;// `isBlack` int(19) DEFAULT '0' COMMENT '是否黑名单（1:是，0：否）',
    private Integer isVerified;// `isVerified` int(19) DEFAULT '0' COMMENT '是否实名认证（1：是，0：否）',
    private Date verifiedTime;// `verifiedTime` datetime DEFAULT NULL COMMENT '实名认证时间',
    private String cardCode;// `cardCode` varchar(255) DEFAULT NULL COMMENT '银行卡号',
    private Integer cardType;//  `cardType` int(255) DEFAULT NULL COMMENT '所属银行',
    private String cardPhone;// `cardPhone` varchar(255) DEFAULT NULL COMMENT '银行卡预留手机号',
    private Integer isPhone;// `isPhone` int(19) DEFAULT '0' COMMENT '是否提交了通讯录（1：是，0：否）',
    private Date phoneTime;// `phoneTime` datetime DEFAULT NULL COMMENT '提交通讯录时间',
    private Integer isYop;// 是否是易宝鉴权绑卡: 0 否 1 是
    private Integer isCard;// `isCard` int(19) DEFAULT '0' COMMENT '是否绑定银行卡（1：是，0：否）',
    private Date cardTime;// `cardTime` datetime DEFAULT NULL COMMENT '绑卡时间',
    private String operatorPassword;// `operatorPassword` varchar(255) DEFAULT NULL COMMENT '运营商服务器密码',
    private Integer isOperator;//  `isOperator` int(19) DEFAULT '0' COMMENT '运营商认证（1：是，0：否）',
    private Date operatorTime;// `operatorTime` datetime DEFAULT NULL COMMENT '运营商认证时间',
    private Date createTime;//  `createTime` datetime DEFAULT NULL COMMENT '添加时间',
    private Date updateTime;// `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
    private String humanFaceImg;// `humanFaceImg` varchar(1024) DEFAULT NULL COMMENT '人脸照片地址',
    private String humanFaceImgUrl;
    private String cardPositiveImg;// `cardPositiveImg` varchar(1024) DEFAULT NULL COMMENT '身份证正面照片',
    private String cardPositiveImgUrl;
    private String cardAntiImg;// `cardAntiImg` varchar(1024) DEFAULT NULL COMMENT '身份证反面图片',
    private String cardAntiImgUrl;
    private Integer isWindControl;//  `isWindControl` int(19) DEFAULT NULL COMMENT '风控评估（1：通过，0：不通过）',
    private Integer status;//是否有效（1：无效，0：有效）
    private Integer channelId;
    private Integer isZhima;
    private String zhimaScore;
    private Date zmAuthTime;
    /**
     * 新老用户标识 老用户标识为1(还款成功后的用户为老用户)
     */
    private Integer isOld;

    private String createIp;

    public static Map<String, String> getIsBlackMap()
    {
        return isBlackMap;
    }

    public static void setIsBlackMap(Map<String, String> isBlackMap)
    {
        BorrowUser.isBlackMap = isBlackMap;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUserAccount()
    {
        return userAccount;
    }

    public void setUserAccount(String userAccount)
    {
        this.userAccount = userAccount;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }

    public String getTransactionPassword()
    {
        return transactionPassword;
    }

    public void setTransactionPassword(String transactionPassword)
    {
        this.transactionPassword = transactionPassword;
    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserCardNo()
    {
        return userCardNo;
    }

    public void setUserCardNo(String userCardNo)
    {
        this.userCardNo = userCardNo;
    }

    public Integer getUserEducation()
    {
        return userEducation;
    }

    public void setUserEducation(Integer userEducation)
    {
        this.userEducation = userEducation;
    }

    public Integer getUserMarriage()
    {
        return userMarriage;
    }

    public void setUserMarriage(Integer userMarriage)
    {
        this.userMarriage = userMarriage;
    }

    public String getUserProvince()
    {
        return userProvince;
    }

    public void setUserProvince(String userProvince)
    {
        this.userProvince = userProvince;
    }

    public String getUserCity()
    {
        return userCity;
    }

    public void setUserCity(String userCity)
    {
        this.userCity = userCity;
    }

    public String getUserArea()
    {
        return userArea;
    }

    public void setUserArea(String userArea)
    {
        this.userArea = userArea;
    }

    public String getUserAddress()
    {
        return userAddress;
    }

    public void setUserAddress(String userAddress)
    {
        this.userAddress = userAddress;
    }

    public Integer getLengthOfStay()
    {
        return lengthOfStay;
    }

    public void setLengthOfStay(Integer lengthOfStay)
    {
        this.lengthOfStay = lengthOfStay;
    }

    public Integer getIsBlack()
    {
        return isBlack;
    }

    public void setIsBlack(Integer isBlack)
    {
        this.isBlack = isBlack;
    }

    public Integer getIsVerified()
    {
        return isVerified;
    }

    public void setIsVerified(Integer isVerified)
    {
        this.isVerified = isVerified;
    }

    public Date getVerifiedTime()
    {
        return verifiedTime;
    }

    public void setVerifiedTime(Date verifiedTime)
    {
        this.verifiedTime = verifiedTime;
    }

    public String getCardCode()
    {
        return cardCode;
    }

    public void setCardCode(String cardCode)
    {
        this.cardCode = cardCode;
    }

    public Integer getCardType()
    {
        return cardType;
    }

    public void setCardType(Integer cardType)
    {
        this.cardType = cardType;
    }

    public String getCardPhone()
    {
        return cardPhone;
    }

    public void setCardPhone(String cardPhone)
    {
        this.cardPhone = cardPhone;
    }

    public Integer getIsPhone()
    {
        return isPhone;
    }

    public void setIsPhone(Integer isPhone)
    {
        this.isPhone = isPhone;
    }

    public Date getPhoneTime()
    {
        return phoneTime;
    }

    public void setPhoneTime(Date phoneTime)
    {
        this.phoneTime = phoneTime;
    }

    public Integer getIsYop()
    {
        return isYop;
    }

    public void setIsYop(Integer isYop)
    {
        this.isYop = isYop;
    }

    public Integer getIsCard()
    {
        return isCard;
    }

    public void setIsCard(Integer isCard)
    {
        this.isCard = isCard;
    }

    public Date getCardTime()
    {
        return cardTime;
    }

    public void setCardTime(Date cardTime)
    {
        this.cardTime = cardTime;
    }

    public String getOperatorPassword()
    {
        return operatorPassword;
    }

    public void setOperatorPassword(String operatorPassword)
    {
        this.operatorPassword = operatorPassword;
    }

    public Integer getIsOperator()
    {
        return isOperator;
    }

    public void setIsOperator(Integer isOperator)
    {
        this.isOperator = isOperator;
    }

    public Date getOperatorTime()
    {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime)
    {
        this.operatorTime = operatorTime;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getHumanFaceImg()
    {
        return humanFaceImg;
    }

    public void setHumanFaceImg(String humanFaceImg)
    {
        this.humanFaceImg = humanFaceImg;
    }

    public String getHumanFaceImgUrl()
    {
        return humanFaceImgUrl;
    }

    public void setHumanFaceImgUrl(String humanFaceImgUrl)
    {
        this.humanFaceImgUrl = humanFaceImgUrl;
    }

    public String getCardPositiveImg()
    {
        return cardPositiveImg;
    }

    public void setCardPositiveImg(String cardPositiveImg)
    {
        this.cardPositiveImg = cardPositiveImg;
    }

    public String getCardPositiveImgUrl()
    {
        return cardPositiveImgUrl;
    }

    public void setCardPositiveImgUrl(String cardPositiveImgUrl)
    {
        this.cardPositiveImgUrl = cardPositiveImgUrl;
    }

    public String getCardAntiImg()
    {
        return cardAntiImg;
    }

    public void setCardAntiImg(String cardAntiImg)
    {
        this.cardAntiImg = cardAntiImg;
    }

    public String getCardAntiImgUrl()
    {
        return cardAntiImgUrl;
    }

    public void setCardAntiImgUrl(String cardAntiImgUrl)
    {
        this.cardAntiImgUrl = cardAntiImgUrl;
    }

    public Integer getIsWindControl()
    {
        return isWindControl;
    }

    public void setIsWindControl(Integer isWindControl)
    {
        this.isWindControl = isWindControl;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getChannelId()
    {
        return channelId;
    }

    public void setChannelId(Integer channelId)
    {
        this.channelId = channelId;
    }

    public Integer getIsZhima()
    {
        return isZhima;
    }

    public void setIsZhima(Integer isZhima)
    {
        this.isZhima = isZhima;
    }

    public String getZhimaScore()
    {
        return zhimaScore;
    }

    public void setZhimaScore(String zhimaScore)
    {
        this.zhimaScore = zhimaScore;
    }

    public Date getZmAuthTime()
    {
        return zmAuthTime;
    }

    public void setZmAuthTime(Date zmAuthTime)
    {
        this.zmAuthTime = zmAuthTime;
    }

    public Integer getIsOld()
    {
        return isOld;
    }

    public void setIsOld(Integer isOld)
    {
        this.isOld = isOld;
    }

    public String getCreateIp()
    {
        return createIp;
    }

    public void setCreateIp(String createIp)
    {
        this.createIp = createIp;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
    
    
}
