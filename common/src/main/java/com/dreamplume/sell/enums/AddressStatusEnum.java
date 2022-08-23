package com.dreamplume.sell.enums;

import lombok.Getter;

/**
 * @Classname AddressStatusEnum
 * @Description TODO
 * @Date 2022/5/9 11:19
 * @Created by 翊
 */
@Getter
public enum AddressStatusEnum implements CodeEnum {

    NOT_DEFAULT(0, "非默认地址"),
    DEFAULT(1, "默认地址");

    private Integer code;

    private String state;

    AddressStatusEnum (Integer code, String state) {
        this.code = code;
        this.state = state;
    }
}
