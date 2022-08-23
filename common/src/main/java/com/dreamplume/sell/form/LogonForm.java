package com.dreamplume.sell.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Classname LogonForm
 * @Description TODO
 * @Date 2022/4/21 10:24
 * @Created by 翊
 */
@Data
public class LogonForm {

    /** 用户名称 */
    @NotEmpty(message = "用户 userName 必填")
    @JsonProperty("username")
    public String userName;

    /** 用户密码 */
    @NotEmpty(message = "用户密码必填")
    private String password;
}
