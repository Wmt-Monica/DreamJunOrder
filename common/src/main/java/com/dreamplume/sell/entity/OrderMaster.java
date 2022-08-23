package com.dreamplume.sell.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.dreamplume.sell.enums.OrderStatusEnum;
import com.dreamplume.sell.enums.PayStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname OrderMaster
 * @Description TODO
 * @Date 2022/4/19 21:43
 * @Created by 翊
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMaster implements Serializable {

    /** 订单id. */
    @TableId
    private String orderId;

    /** 买家id */
    private String buyerId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private int orderStatus= OrderStatusEnum.NEW.getCode();

    /** 支付状态, 默认为0未支付. */
    private int payStatus = PayStatusEnum.WAIT.getCode();

    /** 创建时间. */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间. */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
