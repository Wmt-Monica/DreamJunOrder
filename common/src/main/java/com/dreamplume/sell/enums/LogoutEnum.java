package com.dreamplume.sell.enums;

import lombok.Getter;

/**
 * @Classname LogoutEnum
 * @Description TODO
 * @Date 2022/4/21 13:11
 * @Created by 翊
 */
@Getter
public enum LogoutEnum implements CodeEnum {

    NO_LOGOFF(0, "未注销"),
    LOGOFF(1, "已注销")
    ;
    private Integer code;

    private String state;

    LogoutEnum(Integer code, String state) {
        this.code = code;
        this.state = state;
    }
}
