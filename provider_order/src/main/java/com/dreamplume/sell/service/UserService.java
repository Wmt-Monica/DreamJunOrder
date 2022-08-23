package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2022/7/30 3:49
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-user", configuration = MultipartSupportConfig.class)
public interface UserService {

    /**
     * 查找商家用户
     * @return
     */
    @GetMapping("/user/get/business")
    ResultVO<Object> findBusiness();
}
