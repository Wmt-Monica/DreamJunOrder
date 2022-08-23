package com.dreamplume.sell.service;

import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.vo.ResultVO;

/**
 * @Classname BuyerService
 * @Description TODO
 * @Date 2022/4/20 19:47
 * @Created by 翊
 */
public interface BuyerService {

    // 查询一个订单
    OrderDTO findOrderOne(String userId, String orderId);

    // 取消订单
    void cancelOrder(String userId, String orderId);

    // 支付订单
    void payOrder(String userId, String orderId, String payPassword);
}
