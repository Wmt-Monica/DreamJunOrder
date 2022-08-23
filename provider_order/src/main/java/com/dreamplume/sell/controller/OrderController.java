package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.service.OrderService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.CreateOrderVO;
import com.dreamplume.sell.vo.MonthAmountVO;
import com.dreamplume.sell.vo.MonthSalesVO;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname OrderController
 * @Description TODO
 * @Date 2022/7/30 4:35
 * @Created by 翊
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    OrderService orderService;

    /**
     * 创建订单
     * @param orderDTO  订单
     * @return 订单编号
     */
    @PostMapping("/create")
    public ResultVO<Object> create(@RequestBody OrderDTO orderDTO) {
        OrderDTO createOrderDTO = orderService.create(orderDTO);
        log.info("createOrderDTO={}", createOrderDTO);
        CreateOrderVO createOrderVO = new CreateOrderVO();
        BeanUtils.copyProperties(createOrderDTO, createOrderVO);
        createOrderVO.setOrderDetailList(JSONObject.toJSON(createOrderDTO.getOrderDetailList()));
        log.info("createOrderVO={}", createOrderVO);
        return ResultVOUtil.success(JSONObject.toJSON(createOrderVO));
    }

    /**
     * 查看买家订单列表
     * @param buyerId 买家 id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return
     */
    @GetMapping("/get/by/buyerId")
    public ResultVO<Object> findBuyerOrderList(@RequestParam("buyerId") String buyerId,
                                               @RequestParam("pageNum") Integer pageNum,
                                               @RequestParam("pageSize") Integer pageSize) {
        List<OrderDTO> buyerOrderList = orderService.findBuyerOrderList(buyerId, pageNum, pageSize);
        return ResultVOUtil.success(JSONObject.toJSON(buyerOrderList));
    }

    /**
     * 返回所有的买家订单
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return
     */
    @GetMapping("/get/all")
    public ResultVO<Object> findAllBuyerOrderList(@RequestParam("pageNum") Integer pageNum,
                                                  @RequestParam("pageSize") Integer pageSize) {
        List<OrderDTO> allBuyerOrderList = orderService.findAllBuyerOrderList(pageNum, pageSize);
        return ResultVOUtil.success(JSONObject.toJSON(allBuyerOrderList));
    }

    /**
     * 取消订单
     * @param orderDTO 订单 dto
     */
    @PutMapping("/cancel")
    public ResultVO<Object> cancelOrder(@RequestBody OrderDTO orderDTO) {
        orderService.cancelOrder(orderDTO);
        return ResultVOUtil.success();
    }

    /**
     * 支付订单
     * @param buyerId 买家 id
     * @param orderId 订单 id
     * @return
     */
    @PutMapping("/pay/by/buyerId")
    public ResultVO<Object> payOrder(@RequestParam("buyerId") String buyerId,
                                     @RequestParam("orderId") String orderId,
                                     @RequestParam("payPassword") String payPassword) {
        orderService.payOrder(buyerId, orderId, payPassword);
        return ResultVOUtil.success();
    }

    /**
     * 完结订单
     * @param orderId 订单 id
     */
    @PutMapping("/finish")
    public ResultVO<Object> finishOrder(@RequestParam("orderId") String orderId) {
        orderService.finishOrder(orderId);
        return ResultVOUtil.success();
    }

    /**
     * 查看所有等待完结的所有订单
     * @return
     */
    @GetMapping("/get/all/wait/finish")
    public ResultVO<Object> findAllWaitFinishOrder(@RequestParam("pageNum") Integer pageNum,
                                                   @RequestParam("pageSize") Integer pageSize) {
        List<OrderDTO> allWaitFinishOrder = orderService.findAllWaitFinishOrder(pageNum, pageSize);
        return ResultVOUtil.success(JSONObject.toJSON(allWaitFinishOrder));
    }

    /**
     * 查看等待支付的所有订单
     * @return
     */
    @GetMapping("/get/all/wait/pay")
    public ResultVO<Object> findAllWaitPayOrder(@RequestParam("pageNum") Integer pageNum,
                                                @RequestParam("pageSize") Integer pageSize) {
        List<OrderDTO> allWaitPayOrder = orderService.findAllWaitPayOrder(pageNum, pageSize);
        return ResultVOUtil.success(JSONObject.toJSON(allWaitPayOrder));
    }

    /**
     * 查看所有取消的订单
     * @return
     */
    @GetMapping("/get/all/wait/cancel")
    public ResultVO<Object> findAllCancelOrder(@RequestParam("pageNum") Integer pageNum,
                                               @RequestParam("pageSize") Integer pageSize) {
        List<OrderDTO> allCancelOrder = orderService.findAllCancelOrder(pageNum, pageSize);
        return ResultVOUtil.success(JSONObject.toJSON(allCancelOrder));
    }

    /**
     * 查看所有完结的订单
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/get/all/finish")
    public ResultVO<Object> findAllFinishOrder(@RequestParam("pageNum") Integer pageNum,
                                               @RequestParam("pageSize") Integer pageSize) {
        List<OrderDTO> allFinishOrder = orderService.findAllFinishOrder(pageNum, pageSize);
        return ResultVOUtil.success(JSONObject.toJSON(allFinishOrder));
    }

    /**
     * 根据用户 Id 获取所有订单数量
     * @param buyerId 买家 Id
     * @return 订单总量
     */
    @GetMapping("/get/count/by/buyerId")
    public ResultVO<Object> getOrderCountByBuyerId(@RequestParam("buyerId") String buyerId) {
        Integer count = orderService.getOrderCountByBuyerId(buyerId);
        return ResultVOUtil.success(JSONObject.toJSON(count));
    }

    /**
     * 根据获取所有订单数量
     * @return 订单总量
     */
    @GetMapping("/get/all/count")
    public ResultVO<Object> getAllOrderCount() {
        Integer count = orderService.getAllOrderCount();
        return ResultVOUtil.success(JSONObject.toJSON(count));
    }

    /**
     * 根据订单状态获取相应订单集合个数
     * @param orderStatus 订单状态
     * @return 订单数
     */
    @GetMapping("/get/count/by/status")
    public ResultVO<Object> getOrderCountByStatus(@RequestParam("orderStatus") Integer orderStatus,
                                                  @RequestParam("payStatus") Integer payStatus) {
        Integer count = orderService.getOrderCountByStatus(orderStatus, payStatus);
        return ResultVOUtil.success(JSONObject.toJSON(count));
    }

    /**
     * 获取月销量
     * @return 月销量集合对象
     */
    @GetMapping("/get/month/sales")
    public ResultVO<Object> getMonthSales(@RequestParam("year") Integer year) {
        List<MonthSalesVO> sales = orderService.getMonthSales(year);
        return ResultVOUtil.success(JSONObject.toJSON(sales));
    }

    /**
     * 获取某年月销售总金额
     * @param year 年份
     * @return 月销售总金额集合
     */
    @GetMapping("/get/month/amounts")
    public ResultVO<Object> getMonthAmounts(@RequestParam("year") Integer year) {
        List<MonthAmountVO> amounts = orderService.getMonthAmounts(year);
        return ResultVOUtil.success(JSONObject.toJSON(amounts));
    }

    /**
     * 根据订单编号查找订单
     * @param orderId 订单编号
     * @return 订单
     */
    @GetMapping("/get/by/orderId")
    public ResultVO<Object> findOrderByOrderId(@RequestParam("orderId") String orderId) {
        OrderDTO orderDTO = orderService.findOrderByOrderId(orderId);
        return ResultVOUtil.success(JSONObject.toJSON(orderDTO));
    }
}
