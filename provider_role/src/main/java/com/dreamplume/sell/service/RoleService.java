package com.dreamplume.sell.service;

import com.dreamplume.sell.entity.Role;

/**
 * @Classname RoleService
 * @Description TODO
 * @Date 2022/4/21 11:47
 * @Created by 翊
 */
public interface RoleService {

    /**
     * 根据 roleId 返回对应的角色名称
     * @param roleId
     * @return
     */
    Role findOne(Integer roleId);
}
