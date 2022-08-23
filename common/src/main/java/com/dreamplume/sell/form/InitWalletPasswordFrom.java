package com.dreamplume.sell.form;

import lombok.Data;

/**
 * @Classname InitWalletPasswordFrom
 * @Description TODO
 * @Date 2022/5/8 13:49
 * @Created by 翊
 */
@Data
public class InitWalletPasswordFrom {

    /** 用户 ID **/
    private String userId;

    /** 支付密码 **/
    private String password;

    /** 用户邮箱 **/
    private String email;

    /** 验证码 **/
    private String code;
}
