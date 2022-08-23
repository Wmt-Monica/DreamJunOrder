package com.dreamplume.sell.configure;

import org.springframework.context.annotation.Configuration;

/**
 * @Classname JWTConfig
 * @Description TODO
 * @Date 2022/4/21 10:52
 * @Created by 翊
 */
@Configuration
public class JWTConfig {

    public final static String SIGNATURE = "dreamplume";

    public final static Long TOKEN_TIMEOUT = 1000*60*60*24L;  // token 过期时间
}
