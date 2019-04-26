package com.inext.configuration;

import com.inext.interceptor.AppInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by 李思鸽 on 2017/3/22 0022.
 * WEB MVC配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new AppInterceptor());
        interceptorRegistration.addPathPatterns(
                "/order/*", "/baoFooPay/*", "/baoFooBindCard/*",
                "/borrowUser/*", "/platfromAdvise/*", "/credit-loan/*","/baoFooPay/*"
                //下面是2018-04-08号之前的拦截 是否有效路径待验证
                /*"/borrowRecord/*","/applyForProductInfo*", "/web/*", "/productinfo/applyForProductInfo",
                "/productinfo/applyMaJiaForProductInfo","/forumMessageManagement/add","/forum/like",
        		"/userSign/*","/userPoints/*","/fileUpload/*","/notice/*","/forum/add","/forum/listCorner","/forum/del","/forum/read",
                "/forumMessageManagement/list","/fileUpload/*","/bankCreditCard/*", "/applyProduct/*",
                "/pushlistProduct/oneKeyApplyList", "/flowStatis/*","/cloud/verificationService"*/);
        interceptorRegistration.excludePathPatterns(
                "/borrowUser/loginOne", "/borrowUser/login","/borrowUser/smsLogin", "/borrowUser/getBackOne",
                "/borrowUser/getIndexInfo","/borrowUser/getLoanInfo",
                "/borrowUser/getBackTwo", "/sms/*", "/sysCode/*", "/borrowUser/toTgRedister", "/borrowUser/registered",
                "/baoFooPay/callback","/baoFooPay/homeurl","/baoFooPay/reurl","/baoFooPay/singlePay/callback",
                "order/orderList", "/baoFooBindCard/*","/order/queryStatus",
                "/borrowUser/getBackTwo", "/sms/*", "/sysCode/*", "/borrowUser/toTgRedister",
                "/borrowUser/tgRedister","/borrowUser/goDown","/borrowUser/getEquipmentInfo","platfromAdvise/savePlatfromAdvise"
                ,"/borrowUser/return_applyCollect"
                //                "/baoFooPay/*"

                //下面是2018-04-08号之前的排除 是否有效路径待验证
               /* "/web/toAbout", "/web/sendSmsCode", "/web/checkUserPhone", "/web/registerNew", "/web/gotoLogin",
                "/web/updatePassword", "/web/checkUserPhone", "/web/registerTwo", "/web/registerOne", "/web/sendVerificationCode",
                "/web/verificationCodeLogin","/web/verifySmsCode","/web/settingPassword","/bankCreditCard/bannerList","/bankCreditCard/list",
                "/flowStatis/type"*/);
        super.addInterceptors(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 设置默认跳转页面
        registry.addViewController("/").setViewName("forward:/user/login");
        // 设置优先级
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

        super.addViewControllers(registry);
    }

//    @Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/api/**");
//	}


}
