package com.dreamplume.sell.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Classname UpdateUserForm
 * @Description TODO
 * @Date 2022/4/25 19:23
 * @Created by 翊
 */
@Data
public class UpdateUserForm {

    /** 用户ID */
    @NotEmpty(message = "用户 userId 必填")
    @JsonProperty("userId")
    public String userId;

    /** 用户名称 */
    @NotEmpty(message = "用户 userName 必填")
    @JsonProperty("username")
    public String userName;

}
