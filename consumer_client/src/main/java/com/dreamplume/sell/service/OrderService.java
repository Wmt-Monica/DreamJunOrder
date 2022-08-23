package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.entity.OrderDetail;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname OrderService
 * @Description TODO
 * @Date 2022/4/19 23:56
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-order", configuration = MultipartSupportConfig.class)
public interface OrderService {

    // 创建订单
    @PostMapping("/order/create")
    ResultVO<Object> create(@RequestBody OrderDTO orderDTO);

    // 查看买家订单列表
    @GetMapping("/order/get/by/buyerId")
    ResultVO<Object> findBuyerOrderList(@RequestParam("buyerId") String buyerId,
                                        @RequestParam("pageNum") Integer pageNum,
                                        @RequestParam("pageSize") Integer pageSize);

    // 返回所有的买家订单
    @GetMapping("/order/get/all")
    ResultVO<Object> findAllBuyerOrderList(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize);

    // 取消订单
    @PutMapping("/order/cancel")
    ResultVO<Object> cancelOrder(@RequestBody OrderDTO orderDTO);

    // 支付订单
    @PutMapping("/order/pay/by/buyerId")
    ResultVO<Object> payOrder(@RequestParam("buyerId") String buyerId,
                              @RequestParam("orderId") String orderId,
                              @RequestParam("payPassword") String payPassword);

    // 完结订单
    @PutMapping("/order/finish")
    ResultVO<Object> finishOrder(@RequestParam("orderId") String orderId);

    // 查看所有等待完结的所有订单
    @GetMapping("/order/get/all/wait/finish")
    ResultVO<Object> findAllWaitFinishOrder(@RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("pageSize") Integer pageSize);

    // 查看等待支付的所有订单
    @GetMapping("/order/get/all/wait/pay")
    ResultVO<Object> findAllWaitPayOrder(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize);

    // 查看所有取消的订单
    @GetMapping("/order/get/all/wait/cancel")
    ResultVO<Object> findAllCancelOrder(@RequestParam("pageNum") Integer pageNum,
                                        @RequestParam("pageSize") Integer pageSize);

    // 查看所有完结的订单
    @GetMapping("/order/get/all/finish")
    ResultVO<Object> findAllFinishOrder(@RequestParam("pageNum") Integer pageNum,
                                        @RequestParam("pageSize") Integer pageSize);

    // 根据用户 Id 获取所有订单数量
    @GetMapping("/order/get/count/by/buyerId")
    ResultVO<Object> getOrderCountByBuyerId(@RequestParam("buyerId") String buyerId);

    // 根据获取所有订单数量
    @GetMapping("/order/get/all/count")
    ResultVO<Object> getAllOrderCount();

    // 根据订单状态获取相应订单集合个数
    @GetMapping("/order/get/count/by/status")
    ResultVO<Object> getOrderCountByStatus(@RequestParam("orderStatus") Integer orderStatus,
                                           @RequestParam("payStatus") Integer payStatus);

    // 获取月销量
    @GetMapping("/order/get/month/sales")
    ResultVO<Object> getMonthSales(@RequestParam("year") Integer year);

    // 获取某年月销售总金额
    @GetMapping("/order/get/month/amounts")
    ResultVO<Object> getMonthAmounts(@RequestParam("year") Integer year);

    // 根据订单编号查找订单
    @GetMapping("/order/get/by/orderId")
    ResultVO<Object> findOrderByOrderId(@RequestParam("orderId") String orderId);

    // 添加商品详情
    @PostMapping("/order/detail/insert")
    ResultVO<Object> insert(@RequestBody OrderDetail orderDetail);

    // 根据 orderID 获取所有的订单详情列表
    @GetMapping("/order/detail/get/by/orderId")
    ResultVO<Object> findAllDetailByOrderId(@RequestParam("orderId") String orderId);
}
