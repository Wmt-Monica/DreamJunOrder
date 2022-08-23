package com.dreamplume.sell.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderForm {

    @NotEmpty(message = "买家 id 必填")
    public String buyerId;

    /**
     * 买家姓名
     */
    @NotEmpty(message = "姓名必填")
    private String name;

    /**
     * 买家手机号
     */
    @NotEmpty(message = "手机号必填")
    private String phone;

    /**
     * 买家地址
     */
    @NotEmpty(message = "地址必填")
    private String address;

    /**
     * 购物车
     */
    @NotEmpty(message = "购物车不能为空")
    private String items;
}
