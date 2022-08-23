package com.dreamplume.sell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Classname ClientApplication
 * @Description TODO
 * @Date 2022/7/31 1:40
 * @Created by 翊
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
