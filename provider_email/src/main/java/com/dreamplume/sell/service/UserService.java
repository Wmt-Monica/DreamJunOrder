package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2022/7/30 1:35
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-user", configuration = MultipartSupportConfig.class)
public interface UserService {
    /**
     * 获取所有用户的邮箱
     * @return 邮箱集合
     */
    @GetMapping("/user/get/all/user/email")
    ResultVO<Object> findAllUserEmail();
}
