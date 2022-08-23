package com.dreamplume.sell.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Classname OrderDetailVO
 * @Description TODO
 * @Date 2022/4/22 14:00
 * @Created by 翊
 */
@Data
public class OrderDetailVO implements Serializable {

    /** 详情 detailId */
    private String detailId;

    /** 订单编号 */
    private String orderId;

    /** 商品 Id */
    private String productId;

    /** 商品名称 */
    private String productName;

    /** 商品单价 */
    private BigDecimal productPrice;

    /** 商品数量 */
    private int productQuantity;

    /** 商品小图 */
    private String productIcon;

}
