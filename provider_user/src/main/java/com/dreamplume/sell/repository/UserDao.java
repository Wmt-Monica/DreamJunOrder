package com.dreamplume.sell.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dreamplume.sell.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Classname UserRepository
 * @Description TODO
 * @Date 2022/4/19 21:35
 * @Created by 翊
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

    @Select("SELECT email FROM user WHERE role_id = #{roleId} AND logout = 0")
    List<String> findAllUserEmail(Integer roleId);
}
