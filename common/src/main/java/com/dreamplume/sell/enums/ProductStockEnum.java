package com.dreamplume.sell.enums;

import lombok.Getter;

/**
 * @Classname ProductStockEnum
 * @Description TODO
 * @Date 2022/7/26 15:43
 * @Created by 翊
 */
@Getter
public enum ProductStockEnum implements CodeEnum {

    INCREASE(1, "增库存"),
    DECREASE(-1, "减库存");

    private Integer code;

    private String message;

    ProductStockEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
