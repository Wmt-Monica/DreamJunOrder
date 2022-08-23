package com.dreamplume.sell.configure;

import com.dreamplume.sell.interceptor.UserLogonInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Classname InterceptorConfig
 * @Description TODO
 * @Date 2022/4/21 14:04
 * @Created by 翊
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /*
        避免在拦截器中 StringRedisTemplate 注入无效，拦截器加载时间
        在 SpringContext 之前，此时 Bean 对象还未初始化，因此就处出现在
        拦截器中注入的 StringRedisTemplate 为 null，无法使用，因此可以
        通过下面的 @Bean 创建拦截器对象，将拦截器的加载时间推后。
     */
    @Bean
    public UserLogonInterceptor getUserLogonInterceptor() {
        return new UserLogonInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getUserLogonInterceptor())
                .addPathPatterns("/client/**");
    }

}
