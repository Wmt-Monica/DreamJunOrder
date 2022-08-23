package com.dreamplume.sell.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname OrderDetail
 * @Description TODO
 * @Date 2022/4/19 21:43
 * @Created by 翊
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    /** 详情 detailId */
    @TableId
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

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
