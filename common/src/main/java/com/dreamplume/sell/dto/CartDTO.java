package com.dreamplume.sell.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 购物车
 */
@Data
public class CartDTO implements Serializable {

    /** 商品Id. */
    private String productId;

    /** 数量. */
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
