package com.dreamplume.sell.service.impl;

import com.alibaba.fastjson.JSON;
import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.service.BuyerService;
import com.dreamplume.sell.service.OrderService;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname BuyerServiceImpl
 * @Description TODO
 * @Date 2022/4/20 19:47
 * @Created by 翊
 */
@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Resource
    OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String buyerId, String orderId) {
        return findOrderOwner(buyerId, orderId);
    }

    @Override
    public void cancelOrder(String buyerId, String orderId) {
        OrderDTO orderDTO = findOrderOwner(buyerId, orderId);
        ResultVO<Object> cancelOrderResultVO = orderService.cancelOrder(orderDTO);
        if (!cancelOrderResultVO.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(cancelOrderResultVO.getCode()));
        }
    }

    @Override
    public void payOrder(String buyerId, String orderId, String payPassword) {
        ResultVO<Object> payOrderResultVo = orderService.payOrder(buyerId, orderId, payPassword);
        if (!payOrderResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(payOrderResultVo.getCode()));
        }
    }

    public OrderDTO findOrderOwner(String buyerId, String orderId) {
        ResultVO<Object> findOrderByOrderIdResultVo = orderService.findOrderByOrderId(orderId);
        if (!findOrderByOrderIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(findOrderByOrderIdResultVo.getCode()));
        }
        OrderDTO orderDTO = JSON.parseObject(JSON.toJSONString(findOrderByOrderIdResultVo.getData()), OrderDTO.class);
        if (!orderDTO.getBuyerId().equals(buyerId)) {
            log.error("【查询订单】订单的 buyerId 不一致. buyerId={}, orderDTO={}", buyerId, orderDTO);
            throw new SellException(SellErrorCode.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
