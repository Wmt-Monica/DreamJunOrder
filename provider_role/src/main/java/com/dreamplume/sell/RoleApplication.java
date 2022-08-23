package com.dreamplume.sell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Classname RoleApplication
 * @Description TODO
 * @Date 2022/7/30 0:19
 * @Created by 翊
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class RoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoleApplication.class, args);
    }
}
