package com.inext.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

/**
 * 
 * @author cannavaro
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMobileAuthLog {

    @Id
    private Integer id;
    
    private String userId;
    
    private String userName;
    
    private String mobile;
    
    private String cardNo;
    
    private String backUrl;
    
    private String themeColor;
    
    private int auth_status;
    
    private Date create_time;
    
    private Date update_time;
    
    private String return_json;
    
    private String notify_json;
    
}
