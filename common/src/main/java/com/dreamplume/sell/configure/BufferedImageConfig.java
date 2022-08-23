package com.dreamplume.sell.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Classname BufferedImageConfig
 * @Description TODO
 * @Date 2022/4/25 9:10
 * @Created by 翊
 */
@Configuration
public class BufferedImageConfig implements WebMvcConfigurer {

    /**
     * 增加图片转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new BufferedImageHttpMessageConverter());
    }
}
