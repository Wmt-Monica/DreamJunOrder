package com.dreamplume.sell.interceptor;

import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.util.JWTUnit;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户拦截器
 * @Classname UserInterceptor
 * @Description TODO
 * @Date 2022/4/21 14:14
 * @Created by 翊
 */
@Slf4j
public class UserLogonInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;  // key, value 都是字符串

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        log.info("【用户登录】拦截器生效");
        String token = request.getHeader("token");
        log.info("token={}", token);
        String url = request.getRequestURI().substring(request.getContextPath().length());
        log.info("url={}", url);
        if (url.equals("/client/user/logon") ||
                url.equals("/client/user/register") ||
                url.equals("/client/email/code/send") ||
                url.equals("/client/user/forget/password") ||
                url.equals("/error") ||
                url.equals("/client/down")) return true;
        if (ObjectUtils.isEmpty(token)) {
            log.info("【用户未登录】");
            throw new SellException(SellErrorCode.USER_NO_LOGON);
        }
        try {
            Claims claims = JWTUnit.parseJwt(token);
            log.info("claims={}", claims);
            if (ObjectUtils.isEmpty(stringRedisTemplate.opsForValue().get(claims.getId()))) {
                log.info("【用户登录拦截】token 过期，请重新登录,tokenId={}", claims.getId());
                throw new SellException(SellErrorCode.USER_TOKEN_TIMEOUT);
            }
        } catch (ExpiredJwtException e) {
            log.info("【用户登录拦截】token 过期，请重新登录");
            throw new SellException(SellErrorCode.USER_TOKEN_INVALID);
        } catch (Exception e) {
            log.info("【用户登录拦截】token 无效");
            throw new SellException(SellErrorCode.USER_TOKEN_INVALID);
        }
        return true;
    }
}
