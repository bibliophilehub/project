package com.inext.entity.chuqiyou;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChuqiyouFk_p {

    private String merchantId;
    
    private String productId;
    
    private String appKey;
    
    private String orderId;
    
    private String userName;
    
    private String cardNum;
    
    private String mobile;
    
    private ChuqiyouFk_riskData riskData;
    
    private String sign;
    

}