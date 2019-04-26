package com.inext.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class StatisticsUtil extends Thread {
	
	private HttpServletRequest request;
	private String channelCode;
	private final String CUSTOMER_IDENTIFICATION = "CusID";  
	private String uvFlag;  
	
	
	public StatisticsUtil(HttpServletRequest request, String channelCode) {
		super();
		this.request = request;
		this.channelCode = channelCode;
	}



	public void run()
	{
		Cookie[] cookies = request.getCookies();  
        int count = 0;  
        if (cookies != null) {  
            for (Cookie cookie : cookies) {  
                if (org.apache.commons.lang3.StringUtils.equals(cookie.getName(), CUSTOMER_IDENTIFICATION)) {  
                    uvFlag = cookie.getValue();  
                    break;  
                }  
                count++;  
                // 该cookie在cookie列表中不存在  
                if (count == cookies.length) {  
                    autoSetCid();  
                }  
            }  
        } else {  
            autoSetCid();  
        }  
		
	}
	
	//用户首次访问
	private void autoSetCid() {  
		
        uvFlag = UUID.randomUUID().toString().replaceAll("[-]", "");  
        Cookie cidCookie = new Cookie(CUSTOMER_IDENTIFICATION, uvFlag);  
//        cidCookie.setDomain(PropertyFileUtil.get("domain"));  
        cidCookie.setMaxAge((int)DateUtils.nowToMidNight());  
        cidCookie.setPath("/");  
//        getResponse().addCookie(cidCookie);  
    }  

}
