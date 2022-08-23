package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.entity.OrderDetail;
import com.dreamplume.sell.service.OrderDetailService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname OrderDetailController
 * @Description TODO
 * @Date 2022/7/30 15:55
 * @Created by 翊
 */
@RestController
@RequestMapping("/order/detail")
@Slf4j
public class OrderDetailController {

    @Resource
    OrderDetailService orderDetailService;

    /**
     * 添加商品详情
     * @param orderDetail 商品详情对象
     */
    @PostMapping("/insert")
    public ResultVO<Object> insert(@RequestBody OrderDetail orderDetail) {
        orderDetailService.insert(orderDetail);
        return ResultVOUtil.success();
    }

    /**
     * 根据 orderID 获取所有的订单详情列表
     * @param orderId 订单编号
     * @return 订单详情集合
     */
    @GetMapping("/get/by/orderId")
    public ResultVO<Object> findAllDetailByOrderId(@RequestParam("orderId") String orderId) {
        List<OrderDetail> allOrderDetail = orderDetailService.findAllDetailByOrderId(orderId);
        return ResultVOUtil.success(JSONObject.toJSON(allOrderDetail));
    }
}
