package com.dreamplume.sell.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Classname ForgetPasswordFrom
 * @Description TODO
 * @Date 2022/5/12 20:17
 * @Created by 翊
 */
@Data
public class ForgetPasswordForm {

    @NotEmpty(message = "邮箱不能为空")
    private String email;

    @NotEmpty(message = "重置密码")
    private String newPassword;

    @NotEmpty(message = "验证码不能为空")
    private String code;
}
