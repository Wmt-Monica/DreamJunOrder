package com.dreamplume.sell.form;

import lombok.Data;

/**
 * @Classname UpdateWalletPassword
 * @Description TODO
 * @Date 2022/5/8 11:57
 * @Created by 翊
 */
@Data
public class UpdateWalletPasswordForm {

    /** 用户 ID **/
    private String userId;

    /** 用户邮箱 **/
    private String email;

    /** 用户支付原密码 **/
    private String oldPayPassword;

    /** 用户支付新密码 **/
    private String newPayPassword;

    /** 验证码 **/
    private String code;
}
