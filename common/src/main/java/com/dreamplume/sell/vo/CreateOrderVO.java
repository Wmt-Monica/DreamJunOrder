package com.dreamplume.sell.vo;

import com.dreamplume.sell.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Classname CreateOrderVO
 * @Description TODO
 * @Date 2022/8/6 2:32
 * @Created by 翊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderVO {

    /** 订单id. */
    private String orderId;

    /** 买家id */
    public String buyerId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus;

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus;

    /** 订单详情集合 */
    Object orderDetailList;

    /** 创建时间. */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间. */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
