package com.dreamplume.sell.converter;

import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.entity.OrderMaster;
import com.dreamplume.sell.enums.OrderStatusEnum;
import com.dreamplume.sell.enums.PayStatusEnum;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;

import java.math.BigDecimal;

/**
 * @Classname OrderDTOToOrderMaterConverter
 * @Description TODO
 * @Date 2022/4/20 18:39
 * @Created by 翊
 */
public class OrderDTOToOrderMaterConverter {

    /** 订单 */
    private OrderMaster orderMaster = new OrderMaster();

    public OrderDTOToOrderMaterConverter setOrderDTO(OrderDTO orderDTO) {
        orderMaster.setBuyerId(orderDTO.getBuyerId());
        orderMaster.setOrderId(orderDTO.getOrderId());
        orderMaster.setBuyerName(orderDTO.getBuyerName());
        orderMaster.setBuyerAddress(orderDTO.getBuyerAddress());
        orderMaster.setBuyerPhone(orderDTO.getBuyerPhone());
        return this;
    }

    public OrderDTOToOrderMaterConverter setOrderAmount(BigDecimal orderAmount) {
        orderMaster.setOrderAmount(orderAmount);
        return this;
    }

    public OrderDTOToOrderMaterConverter setOrderStatus(int orderStatus) {
        orderMaster.setOrderStatus(orderStatus);
        return this;
    }

    public OrderDTOToOrderMaterConverter setPayStatus(int payStatus) {
        orderMaster.setPayStatus(payStatus);
        return this;
    }

    public OrderMaster converter() {
        if (orderMaster.getOrderStatus() != OrderStatusEnum.NEW.getCode()) {
            throw new SellException(SellErrorCode.ORDER_STATUS_ERROR);
        } else if (orderMaster.getPayStatus() != PayStatusEnum.WAIT.getCode()) {
            throw new SellException(SellErrorCode.ORDER_PAY_STATUS_ERROR);
        } else if (orderMaster.getOrderAmount() == null) {
            throw new SellException(SellErrorCode.ORDER_AMOUNT);
        }
        return orderMaster;
    }
}
