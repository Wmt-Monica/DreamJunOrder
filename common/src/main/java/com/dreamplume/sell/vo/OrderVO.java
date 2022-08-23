package com.dreamplume.sell.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Classname OrderVO
 * @Description TODO
 * @Date 2022/4/22 13:53
 * @Created by 翊
 */
@Data
public class OrderVO implements Serializable {

    /** 订单 id */
    private String orderId;

    /** 买家id */
    private String buyerId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 总金额 */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private String orderStatus;

    /** 支付状态 */
    private String payState;

    /** 订单详情集合 */
    private List<OrderDetailVO> orderDetailVOList;

    /** 创建时间. */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间. */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
