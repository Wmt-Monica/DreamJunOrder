package com.dreamplume.sell.dto;

import com.dreamplume.sell.entity.OrderDetail;
import com.dreamplume.sell.enums.OrderStatusEnum;
import com.dreamplume.sell.enums.PayStatusEnum;
import com.dreamplume.sell.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Classname OrderDTO
 * @Description TODO
 * @Date 2022/4/20 8:48
 * @Created by 翊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {

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
    List<OrderDetail> orderDetailList;

    /** 创建时间. */
    // 指定使用的 json 序列化的类进行转换
//    @JsonSerialize(using = DateToLongSerializer.class)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern="yyyy-MM-dd HH-mm-ss")
    private Date createTime;

    /** 更新时间. */
//    @JsonSerialize(using = DateToLongSerializer.class)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern="yyyy-MM-dd HH-mm-ss")
    private Date updateTime;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }
}
