package com.inext.entity;

import javax.persistence.Id;
import javax.persistence.Transient;

public class BackConfigParams {
    /**
     * configParams表中邮箱的sys_type
     */
    public static final String SMTP = "SMTP";
    /**
     * configParams表中短信的sys_type
     */
    public static final String SMS = "SMS";
    /**
     * configParams表中天畅短信的sys_type
     */
    public static final String SMS_TIANCHANG = "SMS_TIANCHANG";
    /**
     * configParams表中短信验证各种限制的的sys_type
     */
    public static final String SMSCODE = "SMSCODE";
    /**
     * configParams表中网站参数的sys_type
     */
    public static final String WEBSITE = "WEBSITE";
    /**
     * configParams表中关于我们的sys_type
     */
    public static final String CHANNEL = "CHANNEL";
    /**
     * configParams表中聚信立的sys_type
     */
    public static final String JXL = "JXL";
    /**
     * configParams表中服务费账户的sys_type
     */
    public static final String FEE_ACCOUNT = "FEE_ACCOUNT";
    /**
     * configParams表中同盾的sys_type
     */
    public static final String TD = "TD";
    /**
     * configParams表中白骑士的sys_type
     */
    public static final String BQS = "BQS";
    /**
     * configParams表中算话的sys_type
     */
    public static final String SH = "SH";
    /**
     * configParams表中白骑士的sys_type
     */
    public static final String JYZX = "JYZX";
    /**
     * configParams表中爬虫规则的费率
     */
    public static final String SYS_FEE = "SYS_FEE";
    /**
     * configParams表中富友的配置
     */
    public static final String FUYOU = "FUYOU";
    /**
     * configParams表中连连的配置
     */
    public static final String LIANLIAN = "LIANLIAN";
    /**
     * configParams表中银生的配置
     */
    public static final String YINSHENG = "YINSHENG";
    /**
     * configParams表中催收的配置
     */
    public static final String COLLECTION = "COLLECTION";
    /**
     * configParams表中宜信的费率
     */
    public static final String YX = "YX";
    /**
     * configParams表中易宝的配置
     */
    public static final String YIBAO = "YIBAO";
    /**
     * configParams表中APP_URL
     */
    public static final String APP_IMG_URL = "APP_IMG_URL";
    /**
     * configParams表中合利宝的配置
     */
    public static final String HELIPAY = "HELIPAY";

    /**
     * configParams表中合利宝的配置
     */
    public static final String KOUDAI = "KOUDAI";

    /**
     * 二次调用接口需要间隔的时间
     */
    public static final String INTERVAL = "INTERVAL";
    /**
     * 中智诚请求
     */
    public static final String ZZC = "ZZC";
    /**
     * 中智诚风险名单
     */
    public static final String ZZC_BLACK = "ZZC_BLACK";
    /**
     * 系统级，不可页面编辑，仅能后台配置项
     */
    public static final String SYSTEM = "SYSTEM";
    /**
     * 闪银推送的systype
     */
    public static final String SY_TS = "SY_TS";
    /**
     * 魔蝎的sys_type
     */
    public static final String MX = "MX";
    /**
     * 借点钱的sys_type
     */
    public static final String JDQ = "JDQ";

    public static final String YSPAY_SUCCESS_CODE = "0000";

    /**
     * 风控
     */
    public static final String FK = "FK";
    /**
     * 卖手机相关配置
     */
    public static final String LOAN = "LOAN";
    /**
     * 芝麻相关配置
     */
    public static final String ZM = "ZM";

    /**
     * IOS应用相关
     */
    public static final String IOS = "IOS";

    /**
     * configParams表中催收的配置
     */
    public static final String CUISHOU = "CUISHOU";
    
    /**
     * configParams表中版本的配置
     */
    public static final String VERSION = "VERSION";
    
    /**
    * configParams表中关于我们的sys_type
    */
    public static final String BACK_USER_MOBILE = "BACK_USER_MOBILE";

    public static final String KQ = "KQ";

    /**
     * configParams表中汇潮的配置
     */
    public static final String HUICHAO = "HUICHAO";
    /**
     * configParams表中宝付的配置
     */
    public static final String BAOFOO = "BAOFOO";
    /**
     * 延期还款按钮
     */
    public static final String POSTPONE = "POSTPONE";

    /**
     * 贷款额度
     */
    public static final String CREDIT_AMOUNT ="CREDIT_AMOUNT";
    /**
     * 財焱短信验证
     */
    public static final String SMS_CAI_YAN ="SMS_CAI_YAN";

    static {
    }

    @Id
    private Integer id;
    private String sysName;
    private String sysValue;
    private String sysValueBig;
    // 实体类比数据库表多出的字段
    @Transient
    private String sysValueView;
    private String sysKey;
    private String sysType;
    private String inputType;
    private String remark;
    private String limitCode;

    public String getSysValueView() {
        return sysValueView;
    }

    public void setSysValueView(String sysValueView) {
        this.sysValueView = sysValueView;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName == null ? null : sysName.trim();
    }

    public String getSysValue() {
        return sysValue;
    }

    public void setSysValue(String sysValue) {
        this.sysValue = sysValue == null ? null : sysValue.trim();
    }

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey == null ? null : sysKey.trim();
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType == null ? null : sysType.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getLimitCode() {
        return limitCode;
    }

    public void setLimitCode(String limitCode) {
        this.limitCode = limitCode == null ? null : limitCode.trim();
    }

    public String getSysValueBig() {
        return sysValueBig;
    }

    public void setSysValueBig(String sysValueBig) {
        this.sysValueBig = sysValueBig == null ? null : sysValueBig.trim();
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    /**
     * 自动匹配，不用关心类型 但是InputType必须有值
     *
     * @param value
     */
    public String getSysValueAuto() {
        String sysValueAuto = null;
        if (inputTypeEnum.textarea.name().equals(this.getInputType())) {
            sysValueAuto = this.getSysValueBig();
        } else if (inputTypeEnum.text.name().equals(this.getInputType())) {
            sysValueAuto = this.getSysValue();
        } else if (inputTypeEnum.password.name().equals(this.getInputType())) {
            sysValueAuto = this.getSysValue();
        } else if (inputTypeEnum.image.name().equals(this.getInputType())) {
            sysValueAuto = this.getSysValue();
        }
        return sysValueAuto;
    }

    /**
     * 自动匹配，不用关心类型 但是InputType必须有值
     *
     * @param value
     */
    public void setSysValueAuto(String value) {

        if (inputTypeEnum.textarea.name().equals(this.getInputType())) {
            this.setSysValueBig(value);
        } else if (inputTypeEnum.text.name().equals(this.getInputType())) {
            this.setSysValue(value);
        } else if (inputTypeEnum.password.name().equals(this.getInputType())) {
            this.setSysValue(value);
        } else if (inputTypeEnum.image.name().equals(this.getInputType())) {
            this.setSysValue(value);
        }
    }

    @Override
    public String toString() {
        return "BackConfigParams [id=" + id + ", inputType=" + inputType + ", limitCode=" + limitCode + ", remark=" + remark + ", sysKey=" + sysKey
                + ", sysName=" + sysName + ", sysType=" + sysType + ", sysValue=" + sysValue + ", sysValueBig=" + sysValueBig + ", sysValueView="
                + sysValueView + "]";
    }

    public enum inputTypeEnum {
        text, textarea, password, image;
    }

}