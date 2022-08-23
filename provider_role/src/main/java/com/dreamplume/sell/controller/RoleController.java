package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.entity.Role;
import com.dreamplume.sell.service.RoleService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname RoleController
 * @Description TODO
 * @Date 2022/7/30 1:55
 * @Created by 翊
 */
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Resource
    RoleService roleService;

    @GetMapping("/get/by/roleId")
    public ResultVO<Object> getByRoleId(@RequestParam("roleId") Integer roleId) {
        Role role = roleService.findOne(roleId);
        return ResultVOUtil.success(JSONObject.toJSON(role));
    }
}
