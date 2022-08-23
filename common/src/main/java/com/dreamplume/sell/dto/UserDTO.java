package com.dreamplume.sell.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname UserDTO
 * @Description TODO
 * @Date 2022/4/21 10:19
 * @Created by 翊
 */
@Data
public class UserDTO implements Serializable {

    /** 用户 id */
    private String userId;

    /** 用户名称 */
    private String userName;

    /** 用户密码 */
    private String password;

    /** 用户角色 id */
    private Integer roleId;

    /** 用户创建时间 */
    private Date createTime;

    /** 用户注销标识 */
    private Integer logout;

    /** token */
    private String token;

}
