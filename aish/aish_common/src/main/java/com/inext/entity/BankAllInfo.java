package com.inext.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 银行卡
 *
 * @author user
 */
@Data
@NoArgsConstructor
public class BankAllInfo implements Serializable {

    public static final String BANK_STATUS_NO = "0";
    @Id
    private Integer id;
    private String bankName;
    private Integer bankStatus;
    private Date bankUpdatetime;
    private String bankCode;
    private Integer bankSequence;

    private Integer bankNumber;
    private String openBank;
}
