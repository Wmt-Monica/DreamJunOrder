package com.dreamplume.sell.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Classname Wallet
 * @Description TODO
 * @Date 2022/4/20 9:40
 * @Created by 翊
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet implements Serializable {

    /** 用户 id */
    @TableId
    private String userId;

    /** 用户余额 */
    private BigDecimal balance;

    /** 用户支付密码 */
    private String password;
}
