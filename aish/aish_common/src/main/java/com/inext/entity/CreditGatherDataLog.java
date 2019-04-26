package com.inext.entity;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: jzhang
 * @Date: 2018-03-29 0029 上午 11:25
 */
@Data
public class CreditGatherDataLog {

    @Id
    private Integer id;
    private Integer riskCreditUserId;
    private Integer code;
    private String serviceImpl;
    private String data;
    private Date createTime;
    private String remarks;
}
