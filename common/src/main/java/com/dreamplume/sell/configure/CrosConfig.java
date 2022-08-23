package com.dreamplume.sell.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决用于前后端分离的跨域问题
 * @Classname CrosConfig
 * @Description TODO
 * @Date 2022/4/21 14:00
 * @Created by 翊
 */
@Configuration
public class CrosConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
//                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowCredentials(true).maxAge(3600)
                .allowedHeaders("*");
    }
}
