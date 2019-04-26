package com.inext.entity.chuqiyou;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChuqiyouAuthReturn {

    private String mxcode;
    
    private String taskId;
    
    private String taskType;
    
    private String account;
    
    private String userId;
    
    private String message;
    
    private String loginDone;
    

}