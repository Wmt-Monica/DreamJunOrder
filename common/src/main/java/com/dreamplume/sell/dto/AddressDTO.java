package com.dreamplume.sell.dto;

import lombok.Data;

/**
 * @Classname AddressDTO
 * @Description TODO
 * @Date 2022/5/11 19:46
 * @Created by 翊
 */
@Data
public class AddressDTO {
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
    private String isDefault = "非默认地址";
}
