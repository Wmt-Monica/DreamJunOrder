package com.dreamplume.sell.entity;

import com.dreamplume.sell.enums.AddressStatusEnum;
import lombok.Data;

/**
 * @Classname Address
 * @Description TODO
 * @Date 2022/5/9 11:10
 * @Created by 翊
 */
@Data
public class Address {

    /** 地址编号 */
    private String addressId;

    /** 买家 id */
    private String userId;

    /** 买家名称 */
    private String userName;

    /** 买家电话 */
    private String phone;

    /** 买家地址 */
    private String address;

    /** 是否为默认地址：默认是非默认地址*/
    private Integer isDefault = AddressStatusEnum.NOT_DEFAULT.getCode();
}
