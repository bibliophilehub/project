package com.inext.entity;

import lombok.Data;

import javax.persistence.Id;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SupermarketInfo {

    @Id
    private Integer id;
    private String name;
    private Integer beginMoney;
    private Integer endMoney;
    private String describes;
    private String urlPath;
    private String imgPath;
    private Integer clickNum;
    private Integer status;
    private Date createTime;
    private Integer type;
    private BigDecimal sort;
}
