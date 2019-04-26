package com.inext.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 订单状态历史
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssetOrderStatusHis implements Serializable {

    /**
     * 1审核中
     */
    public static final Integer STATUS_DSH = 1;
    /**
     * 2待放款
     */
    public static final Integer STATUS_DFK = 2;
    /**
     * 3放款成功 (待寄出)
     */
    public static final Integer STATUS_FKCG = 3;
    /**
     * 4放款失败
     */
    public static final Integer STATUS_FKSB = 4;
    /**
     * 5审核拒绝
     */
    public static final Integer STATUS_SHJJ = 5;
    /**
     * 6待还款
     */
    public static final Integer STATUS_DHK = 6;
    /**
     * 7已逾期 (已违约)
     */
    public static final Integer STATUS_YYQ = 7;
    /**
     * 8已还款(已取消)
     */
    public static final Integer STATUS_YHK = 8;
    /**
     * 9已续期
     */
    public static final Integer STATUS_YXQ = 9;
    /**
     * 10已寄出
     */
    public static final Integer STATUS_YJC = 10;
    /**
     * 11检测合格 (已关闭)
     */
    public static final Integer STATUS_JCHG = 11;
    /**
     * 12不合格
     */
    public static final Integer STATUS_BHG = 12;
    /**
     * 13待收款
     */
    public static final Integer STATUS_DSK = 13;
    /**
     * 14待人工审核
     */
    public static final Integer STATUS_DRGSH = 14;
    private static final long serialVersionUID = 8098342754628169918L;
    public static Map<Integer, String> orderStatusMap = new LinkedHashMap<Integer, String>();

    static {
        orderStatusMap.put(STATUS_DSH, "审核中");
        orderStatusMap.put(STATUS_DFK, "待放款");
        orderStatusMap.put(STATUS_FKCG, "已放款");
        orderStatusMap.put(STATUS_FKSB, "审核拒绝");//放款失败 显示审核拒绝
        orderStatusMap.put(STATUS_SHJJ, "审核拒绝");
        orderStatusMap.put(STATUS_DHK, "待放款");
        orderStatusMap.put(STATUS_YYQ, "已逾期");
        orderStatusMap.put(STATUS_YHK, "已完成");
        orderStatusMap.put(STATUS_YXQ, "已续期");
        orderStatusMap.put(STATUS_YJC, "已寄出");
        orderStatusMap.put(STATUS_JCHG, "已完成");
        orderStatusMap.put(STATUS_BHG, "已违约");//不合格 叫 已违约
        orderStatusMap.put(STATUS_DSK, "处理中");//待收款叫 处理中
        orderStatusMap.put(STATUS_DRGSH, "待人工审核");
    }

    @Id
    private Integer id;
    private Integer orderId;//订单id
    private Integer orderStatus;//订单状态：1待审核，2待放款，3放款成功，4放款失败，5审核拒绝，6待还款，7已逾期，8已还款，9已续期，10已寄出，11检测合格，12不合格
    private Date createTime;//创建时间
    private String remark;//备注
    private String operator;//操作人账号
    private String operatorRemark;//操作人填写的备注
}
