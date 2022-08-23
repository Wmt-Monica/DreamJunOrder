package com.dreamplume.sell.util;

import com.dreamplume.sell.configure.JWTConfig;
import com.dreamplume.sell.entity.User;
import io.jsonwebtoken.*;

import java.util.Date;

/**
 * @Classname JWTUnit
 * @Description TODO
 * @Date 2022/4/21 10:51
 * @Created by 翊
 */
public class JWTUnit {

    // 生成 JWT
    public static String createJwt(User user) {
        System.out.println("current = "+System.currentTimeMillis());
        Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        System.out.println("date = "+date.getTime());
        return Jwts.builder()
                // header
                .setHeaderParam("type", "JWT")  // 令牌的类型 JWT
                .setHeaderParam("alg", "HS256")  // 令牌进行加密的算法
                // payload
                .claim("userName", user.getUserName())  // 负载的用户的信息数据
                .claim("password", user.getPassword())
                // JWT Token 的过期时间
                .setExpiration(new Date(System.currentTimeMillis() + JWTConfig.TOKEN_TIMEOUT))
                .setId(user.getUserId())  // 标识 JWT 的唯一 ID
                .signWith(SignatureAlgorithm.HS256, JWTConfig.SIGNATURE)  // 进行加密的算法和私盐
                .compact();  // 进行签发
    }

    // Jwt 解密
    public static Claims parseJwt(String token) {
        return Jwts.parser()
                .setSigningKey(JWTConfig.SIGNATURE)
                .parseClaimsJws(token)
                .getBody();
    }
}
