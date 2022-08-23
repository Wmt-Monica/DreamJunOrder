package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Classname RoleService
 * @Description TODO
 * @Date 2022/4/21 11:47
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-role", configuration = MultipartSupportConfig.class)
public interface RoleService {

    /**
     * 根据 roleId 返回对应的角色名称
     * @param roleId
     * @return
     */
    @GetMapping("/role/get/by/roleId")
    ResultVO<Object> findOne(@RequestParam("roleId") Integer roleId);
}
