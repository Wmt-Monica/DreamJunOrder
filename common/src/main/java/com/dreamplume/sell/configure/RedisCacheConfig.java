package com.dreamplume.sell.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname RedisCacheConfig
 * @Description TODO
 * @Date 2022/7/26 11:22
 * @Created by 翊
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisCacheConfig {
    /**
     * 自定义缓存管理器
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("semiPermanent");
        cacheNames.add("name");
        ConcurrentHashMap<String, RedisCacheConfiguration> configMap = new ConcurrentHashMap<>();
        configMap.put("semiPermanent", config);
        // 设置 cacheName = name 的缓存存活时间为 6 分钟
        configMap.put("name", config.entryTtl(Duration.ofMinutes(6L)));

        //需要先初始化缓存名称，再初始化其它的配置。
        return RedisCacheManager.builder(factory).initialCacheNames(cacheNames).withInitialCacheConfigurations(configMap).build();
    }
}
