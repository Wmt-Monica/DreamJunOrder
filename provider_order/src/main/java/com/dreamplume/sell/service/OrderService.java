package com.dreamplume.sell.service;

import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.vo.MonthAmountVO;
import com.dreamplume.sell.vo.MonthSalesVO;

import java.util.List;

/**
 * @Classname OrderService
 * @Description TODO
 * @Date 2022/4/19 23:56
 * @Created by 翊
 */
public interface OrderService {

    /**
     * 创建订单
     * @param orderDTO  订单
     * @return 订单编号
     */
    OrderDTO create(OrderDTO orderDTO);

    /**
     * 查看买家订单列表
     * @param buyerId 买家 id
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return
     */
    List<OrderDTO> findBuyerOrderList(String buyerId, Integer pageNum, Integer pageSize);

    /**
     * 返回所有的买家订单
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @return
     */
    List<OrderDTO> findAllBuyerOrderList(Integer pageNum, Integer pageSize);

    /**
     * 取消订单
     * @param orderDTO 订单 dto
     */
    void cancelOrder(OrderDTO orderDTO);

    /**
     * 支付订单
     * @param buyerId 买家 id
     * @param orderId 订单 id
     * @return
     */
    void payOrder(String buyerId, String orderId, String payPassword);

    /**
     * 完结订单
     * @param orderId 订单 id
     */
    void finishOrder(String orderId);

    /**
     * 查看所有等待完结的所有订单
     * @return
     */
    List<OrderDTO> findAllWaitFinishOrder(Integer pageNum, Integer pageSize);

    /**
     * 查看等待支付的所有订单
     * @return
     */
    List<OrderDTO> findAllWaitPayOrder(Integer pageNum, Integer pageSize);

    /**
     * 查看所有取消的订单
     * @return
     */
    List<OrderDTO> findAllCancelOrder(Integer pageNum, Integer pageSize);

    /**
     * 查看所有完结的订单
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<OrderDTO> findAllFinishOrder(Integer pageNum, Integer pageSize);

    /**
     * 根据用户 Id 获取所有订单数量
     * @param buyerId 买家 Id
     * @return 订单总量
     */
    Integer getOrderCountByBuyerId(String buyerId);

    /**
     * 根据获取所有订单数量
     * @return 订单总量
     */
    Integer getAllOrderCount();

    /**
     * 根据订单状态获取相应订单集合个数
     * @param orderStatus 订单状态
     * @return 订单数
     */
    Integer getOrderCountByStatus(Integer orderStatus, Integer payStatus);

    /**
     * 获取月销量
     * @return 月销量集合对象
     */
    List<MonthSalesVO> getMonthSales(Integer year);

    /**
     * 获取某年月销售总金额
     * @param year 年份
     * @return 月销售总金额集合
     */
    List<MonthAmountVO> getMonthAmounts(Integer year);

    /**
     * 根据订单编号查找订单
     * @param orderId 订单编号
     * @return 订单
     */
    OrderDTO findOrderByOrderId(String orderId);
}
