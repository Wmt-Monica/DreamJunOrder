package com.dreamplume.sell.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dreamplume.sell.entity.Role;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.repository.RoleDao;
import com.dreamplume.sell.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname RoleServiceImpl
 * @Description TODO
 * @Date 2022/4/21 11:47
 * @Created by 翊
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    RoleDao roleDao;

    @Override
    public Role findOne(Integer roleId) {
        Role role = roleDao.selectOne(new QueryWrapper<Role>().eq("role_id", roleId));
        if (role == null) {
            log.error("【用户注册】用户角色未查找，roleId={}",roleId);
            throw new SellException(SellErrorCode.ROLE_NULL);
        } else {
            return role;
        }
    }
}
