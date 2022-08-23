package com.dreamplume.sell.form;

import lombok.Data;

/**
 * @Classname UserUpdatePassword
 * @Description TODO
 * @Date 2022/5/8 11:50
 * @Created by 翊
 */
@Data
public class UpdateUserPasswordForm {

    private String userId;

    private String email;

    private String oldPassword;

    private String newPassword;

    private String code;
}
