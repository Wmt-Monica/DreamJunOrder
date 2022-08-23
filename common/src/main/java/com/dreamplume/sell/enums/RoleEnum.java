package com.dreamplume.sell.enums;

import lombok.Getter;

/**
 * @Classname RoleEnum
 * @Description TODO
 * @Date 2022/4/21 12:59
 * @Created by 翊
 */
@Getter
public enum RoleEnum  implements CodeEnum {

    BUYER(0, "买家"),
    BUSINESS(1, "商家")
    ;
    private Integer code;

    private String role;

    RoleEnum(Integer code, String role) {
        this.code = code;
        this.role = role;
    }
}
