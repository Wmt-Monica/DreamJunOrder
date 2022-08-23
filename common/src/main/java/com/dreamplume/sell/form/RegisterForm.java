package com.dreamplume.sell.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Classname RegisterForm
 * @Description TODO
 * @Date 2022/4/21 10:28
 * @Created by 翊
 */
@Data
public class RegisterForm {

    /** 用户名称 */
    @NotEmpty(message = "用户 Name 必填")
    @JsonProperty("username")
    public String userName;

    /** 用户密码 */
    @NotEmpty(message = "用户密码必填")
    private String password;

    @NotEmpty(message = "邮箱不能为空")
    private String email;

    @NotEmpty(message = "验证码不能为空")
    private String code;
}
