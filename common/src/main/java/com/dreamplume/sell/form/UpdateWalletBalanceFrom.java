package com.dreamplume.sell.form;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname UpdateWalletBalance
 * @Description TODO
 * @Date 2022/5/8 13:42
 * @Created by 翊
 */
@Data
public class UpdateWalletBalanceFrom {

    private String userId;

    private BigDecimal updateBigDecimal;

    private String email;

    private String code;
}
