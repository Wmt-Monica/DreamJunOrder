package com.dreamplume.sell.exception;

import com.dreamplume.sell.enums.SellErrorCode;
import lombok.Getter;


@Getter
public class SellException extends RuntimeException{

    private SellErrorCode errorCode;

    public SellException() {
        super();
    }

    // 用于返回结果的枚举类型（应用程序正常运行出现的一些提示信息）
    public SellException(SellErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
