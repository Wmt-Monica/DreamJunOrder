package com.dreamplume.sell.form;

import lombok.Data;

/**
 * @Classname AddressForm
 * @Description TODO
 * @Date 2022/5/9 14:12
 * @Created by 翊
 */
@Data
public class AddressForm {

    /** 买家 id */
    private String userId;

    /** 买家名称 */
    private String userName;

    /** 买家电话 */
    private String phone;

    /** 买家地址 */
    private String address;

    /** 是否为默认地址 */
    private String isDefault;
}
