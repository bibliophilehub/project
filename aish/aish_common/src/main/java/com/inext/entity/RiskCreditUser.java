package com.inext.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: jzhang
 * @Date: 2018-04-10 0010 下午 03:47
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RiskCreditUser implements Serializable {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private Integer userId;//'用户id',
    private Integer assetId;// 借款信息申请表主键ID
    private String ip;// ip
    private Integer status;// 0 未获取 1获取除聚信立数据
    private Integer jxlStatus;// 0 未获取 1获取聚信立数据
    private Integer fkStatus;// 0待风控，1已创建风控订单，2风控完成'
    private Integer bqsBlack;//'白骑士1.通过；2.拒绝（命中黑名单）；3.建议人工审核(命中灰名单)',
    private Integer mgBlackScore;//'密罐黑中介分,默认100，分值越小风险越大',
    private Integer zzcFqz;//'中智诚反欺诈风险级别1.高(最差)；2中(中等)；3低(最好)',
    private BigDecimal tdScore;//'同盾分，分值越高风险越大',
    private Integer tdPhoneBlack;//'同盾手机网贷黑名单个数',
    private Integer tdCardNumBlack;//'同盾身份证号网贷黑名单个数',
    private Integer jyLoanNum;//'91征信借款总数',
    private Integer jyJdNum;//'91征信拒单记录数',
    private BigDecimal jyJdBl;//'91征信拒单记录占比',
    private Integer jyOverNum;//'91征信逾期记录数、逾期金额大于0或者状态是逾期',
    private BigDecimal jyOverBl;//'91征信逾期记录占比',
    private String jxlToken;//'聚信立开始采集数据时存入token',
    private String jxlRptId;//'报告编号',
    private String jxlUpdateTime;//'报告生成时间',
    private String jxlGender;//'性别',
    private Integer jxlAge;//'年龄',
    private String jxlKeyValueName;//'姓名',
    private String jxlKeyValueCard;//'身份证号',
    private String jxlPhoneRegDays;//'手机号使用时间',
    private String jxlJmDayNum;//'总静默天数',
    private String jxlHtPhoneNum;//'通讯联系人互通电话数量',
    private String jxlYjHf;//'聚信立月均话费',
    private String jxlCheckBlackInfo;//'用户黑名单信息',
    private String jxlEbusinessExpense;//'电商月消费',
    private String jxlDeliverAddress;//'电商地址分析',
    private String jxlBehaviorCheck;//'运营商信息(用户行为检测)',
    private String jxlCollectionContact;//'联系人信息(联系人名单)',
    private String jxlContactList;//'通话数据分析(运营商联系人通话详情)',
    private String jxlContactRegion;//'联系人所在地区(联系人区域汇总)',
    private String jxlCellBehavior;//'月使用情况(运营商数据整理)',
    private String jxlMainService;//'服务企业类型(常用服务)',
    private String jxlTripInfo;//'出行数据分析',
    private String jxlUserInfoCheck;//'报告被查询情况(用户信息检测)',
    private String riskId;//'风控流水号
    private BigDecimal score;//风控分数(0-100)
    private String detail;//风控返回消息
    private Date createTime;
    private Date updateTime;

    public RiskCreditUser(Integer id) {
        this.id = id;
    }

}
