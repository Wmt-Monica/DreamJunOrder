package com.dreamplume.sell.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dreamplume.sell.entity.OrderDetail;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.repository.OrderDetailRepository;
import com.dreamplume.sell.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname OrderDetailServiceImpl
 * @Description TODO
 * @Date 2022/5/11 20:00
 * @Created by 翊
 */
@Service
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {

    @Resource
    OrderDetailRepository orderDetailRepository;

    /**
     * 添加商品详情
     * @param orderDetail 商品详情对象
     */
    @Override
    public void insert(OrderDetail orderDetail) {
        int insert = orderDetailRepository.insert(orderDetail);
        if (insert <= 0) {
            log.error("【添加订单详情】添加失败");
            throw new SellException(SellErrorCode.ORDER_DETAIL_ADD_FAIL);
        }
    }

    /**
     * 根据订单编号查找订单所有的详情
     * @param orderId 订单编号
     * @return 订单详情集合
     */
    @Override
    public List<OrderDetail> findAllDetailByOrderId(String orderId) {
        List<OrderDetail> orderDetailList = orderDetailRepository.selectList(new QueryWrapper<OrderDetail>().eq("order_id", orderId));
        if (orderDetailList == null) {
            throw new SellException(SellErrorCode.ORDER_NULL);
        }
        return orderDetailList;
    }
}
