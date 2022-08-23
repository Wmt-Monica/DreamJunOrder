package com.dreamplume.sell.service;

import com.dreamplume.sell.entity.OrderDetail;

import java.util.List;

/**
 * @Classname OrderDetailService
 * @Description TODO
 * @Date 2022/5/11 19:36
 * @Created by 翊
 */
public interface OrderDetailService {

    /**
     * 添加商品详情
     * @param orderDetail 商品详情对象
     */
    void insert(OrderDetail orderDetail);

    /**
     * 根据 orderID 获取所有的订单详情列表
     * @param orderId 订单编号
     * @return 订单详情集合
     */
    List<OrderDetail> findAllDetailByOrderId(String orderId);
}
