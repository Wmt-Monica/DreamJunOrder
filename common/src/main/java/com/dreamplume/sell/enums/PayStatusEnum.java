package com.dreamplume.sell.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum implements CodeEnum {

    WAIT(0, "等待支付"),
    SUCCESS(1, "支付成功"),
    BACK(2, "支付退还"),
    CANCEL(3, "取消支付")
    ;

    private Integer code;

    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
