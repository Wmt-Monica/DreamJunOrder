package com.dreamplume.sell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Classname AddressApplication
 * @Description TODO
 * @Date 2022/7/30 0:20
 * @Created by 翊
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class AddressApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddressApplication.class, args);
    }
}
