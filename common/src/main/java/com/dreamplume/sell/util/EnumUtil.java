package com.dreamplume.sell.util;

import com.dreamplume.sell.enums.CodeEnum;

/**
 * @Classname EnumUtil
 * @Description TODO
 * @Date 2022/4/20 15:24
 * @Created by 翊
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
