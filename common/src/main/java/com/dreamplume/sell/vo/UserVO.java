package com.dreamplume.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Classname UserVO
 * @Description TODO
 * @Date 2022/4/21 11:30
 * @Created by 翊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    /** 用户 id */
    @JsonProperty("id")
    private String userId;

    /** 用户名称 */
    @JsonProperty("username")
    private String userName;

    /** 用户角色 id */
    @JsonProperty("role")
    private String role;

    /** 用户邮箱 */
    private String email;

    /** token */
    @JsonProperty("token")
    private String token;

    /** 用户头像 */
    private String headPortrait;

}
