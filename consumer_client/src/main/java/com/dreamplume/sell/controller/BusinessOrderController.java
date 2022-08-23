package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.converter.OrderDTOToOrderVOConverter;
import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.enums.OrderStatusEnum;
import com.dreamplume.sell.enums.PayStatusEnum;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.service.OrderService;
import com.dreamplume.sell.service.ProductService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname BusinessOrderController
 * @Description TODO
 * @Date 2022/4/22 11:43
 * @Created by 翊
 */
@RestController
@RequestMapping("/business/order")
@Slf4j
public class BusinessOrderController {

    @Resource
    OrderService orderService;

    @Resource
    ProductService productService;

    public List<OrderVO> orderDTOToOrderVOConverter(List<OrderDTO> orderDTOList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (OrderDTO orderDTO : orderDTOList) {
            OrderVO orderVO = OrderDTOToOrderVOConverter.converter(orderDTO);
            List<OrderDetailVO> orderDetailVOList = orderVO.getOrderDetailVOList();
            for (OrderDetailVO orderDetailVO : orderDetailVOList) {
                ResultVO<Object> findProductIconResultVo = productService.findProductIcon(orderDetailVO.getProductId());
                if (!findProductIconResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
                    throw new SellException(SellErrorCode.get(findProductIconResultVo.getCode()));
                }
                String productIcon = JSON.parseObject(JSON.toJSONString(findProductIconResultVo.getData()), String.class);
                orderDetailVO.setProductIcon(productIcon);
            }
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    // 查看所有的订单
    @GetMapping("/list")
    public ResultVO<Object> findAllOrder(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize) {
        ResultVO<Object> findAllBuyerOrderListResultVo = orderService.findAllBuyerOrderList(pageNum, pageSize);
        if (!findAllBuyerOrderListResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findAllBuyerOrderListResultVo.getCode()));
        }
        List<OrderDTO> allBuyerOrderList = JSON.parseArray(JSONArray.toJSONString(findAllBuyerOrderListResultVo.getData()), OrderDTO.class);
        Map<String, Object> map = new HashMap<>();
        map.put("allBuyerOrderList", orderDTOToOrderVOConverter(allBuyerOrderList));
        ResultVO<Object> getAllOrderCountResultVo = orderService.getAllOrderCount();
        if (!getAllOrderCountResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getAllOrderCountResultVo.getCode()));
        }
        map.put("count", JSON.parseObject(JSON.toJSONString(getAllOrderCountResultVo.getData()), Integer.class));
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 查看所有等待完结的所有订单
    @GetMapping("/list/wait/finish")
    public ResultVO<Object> findAllWaitFinishOrder(@RequestParam("pageNum") Integer pageNum,
                                                   @RequestParam("pageSize") Integer pageSize) {
        ResultVO<Object> findAllWaitFinishOrderResultVo = orderService.findAllWaitFinishOrder(pageNum, pageSize);
        if (!findAllWaitFinishOrderResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findAllWaitFinishOrderResultVo.getCode()));
        }
        List<OrderDTO> allWaitFinishOrder = JSON.parseArray(JSONArray.toJSONString(findAllWaitFinishOrderResultVo.getData()), OrderDTO.class);
        Map<String, Object> map = new HashMap<>();
        map.put("allWaitFinishOrder", orderDTOToOrderVOConverter(allWaitFinishOrder));
        ResultVO<Object> getOrderCountByStatusResultVo = orderService.getOrderCountByStatus(OrderStatusEnum.NEW.getCode(), PayStatusEnum.SUCCESS.getCode());
        if (!getOrderCountByStatusResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getOrderCountByStatusResultVo.getCode()));
        }
        map.put("count", JSON.parseObject(JSON.toJSONString(getOrderCountByStatusResultVo.getData()), Integer.class));
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 查看等待支付的所有订单
    @GetMapping("/list/wait/pay")
    public ResultVO<Object> findAllWaitPayOrder(@RequestParam("pageNum") Integer pageNum,
                                        @RequestParam("pageSize") Integer pageSize) {
        ResultVO<Object> findAllWaitPayOrderResultVo = orderService.findAllWaitPayOrder(pageNum, pageSize);
        if (!findAllWaitPayOrderResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findAllWaitPayOrderResultVo.getCode()));
        }
        List<OrderDTO> allWaitPayOrder = JSON.parseArray(JSONArray.toJSONString(findAllWaitPayOrderResultVo.getData()), OrderDTO.class);
        Map<String, Object> map = new HashMap<>();
        map.put("allWaitPayOrder", orderDTOToOrderVOConverter(allWaitPayOrder));
        ResultVO<Object> getOrderCountByStatusResultVo = orderService.getOrderCountByStatus(OrderStatusEnum.NEW.getCode(), PayStatusEnum.WAIT.getCode());
        if (!getOrderCountByStatusResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getOrderCountByStatusResultVo.getCode()));
        }
        map.put("count", JSON.parseObject(JSON.toJSONString(getOrderCountByStatusResultVo.getData()), Integer.class));
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 查看所有取消的订单
    @GetMapping("/list/cancel")
    public ResultVO<Object> findAllCancelOrder(@RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize) {
        ResultVO<Object> findAllCancelOrderResultVo = orderService.findAllCancelOrder(pageNum, pageSize);
        if (!findAllCancelOrderResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findAllCancelOrderResultVo.getCode()));
        }
        List<OrderDTO> allCancelOrder = JSON.parseArray(JSONArray.toJSONString(findAllCancelOrderResultVo.getData()), OrderDTO.class);
        Map<String, Object> map = new HashMap<>();
        map.put("allCancelOrder", orderDTOToOrderVOConverter(allCancelOrder));
        ResultVO<Object> getOrderCountByStatusResultVo = orderService.getOrderCountByStatus(OrderStatusEnum.CANCEL.getCode(), PayStatusEnum.CANCEL.getCode());
        if (!getOrderCountByStatusResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getOrderCountByStatusResultVo.getCode()));
        }
        map.put("count", JSON.parseObject(JSON.toJSONString(getOrderCountByStatusResultVo.getData()), Integer.class));
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 查看所有完结的订单
    @GetMapping("/list/finish")
    public ResultVO<Object> findAllFinishOrder(@RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize) {
        ResultVO<Object> findAllFinishOrderResultVo = orderService.findAllFinishOrder(pageNum, pageSize);
        if (!findAllFinishOrderResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findAllFinishOrderResultVo.getCode()));
        }
        List<OrderDTO> allCancelOrder = JSON.parseArray(JSONArray.toJSONString(findAllFinishOrderResultVo.getData()), OrderDTO.class);
        Map<String, Object> map = new HashMap<>();
        map.put("allFinishOrder", orderDTOToOrderVOConverter(allCancelOrder));
        ResultVO<Object> getOrderCountByStatusResultVo = orderService.getOrderCountByStatus(OrderStatusEnum.FINISHED.getCode(), PayStatusEnum.SUCCESS.getCode());
        if (!getOrderCountByStatusResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getOrderCountByStatusResultVo.getCode()));
        }
        map.put("count", JSON.parseObject(JSON.toJSONString(getOrderCountByStatusResultVo.getData()), Integer.class));
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 完结订单
    @PutMapping("/finish")
    public ResultVO<Object>finishOrder(@RequestParam("orderId") String orderId) {
        ResultVO<Object> finishOrder = orderService.finishOrder(orderId);
        if (!finishOrder.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.success(SellErrorCode.get(finishOrder.getCode()));
        }
        return ResultVOUtil.success();
    }

    // 获取某年的月销售量
    @GetMapping("/month/sales")
    public ResultVO<Object> getMonthSales(@RequestParam("year") Integer year) {
        Map<String, Object> map = new HashMap<>();
        ResultVO<Object> getMonthSalesResultVo = orderService.getMonthSales(year);
        if (!getMonthSalesResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getMonthSalesResultVo.getCode()));
        }
        map.put("sales", JSON.parseArray(JSONArray.toJSONString(getMonthSalesResultVo.getData()), MonthSalesVO.class));
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 获取某年的月销售额
    @GetMapping("/month/amount")
    public ResultVO<Object> getMonthlyAmount(@RequestParam("year") Integer year) {
        Map<String, Object> map = new HashMap<>();
        ResultVO<Object> getMonthAmountsResultVo = orderService.getMonthAmounts(year);
        if (!getMonthAmountsResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getMonthAmountsResultVo.getCode()));
        }
        map.put("amount", JSON.parseArray(JSONArray.toJSONString(getMonthAmountsResultVo.getData()), MonthAmountVO.class));
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 根据订单编号查找指定订单
    @GetMapping("/find")
    public ResultVO<Object> findOrderByOrderId(@RequestParam("orderId") String orderId) {
        ResultVO<Object> findOrderByOrderIdResultVo = orderService.findOrderByOrderId(orderId);
        if (!findOrderByOrderIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findOrderByOrderIdResultVo.getCode()));
        }
        OrderDTO orderDTO = JSON.parseObject(JSON.toJSONString(findOrderByOrderIdResultVo.getData()), OrderDTO.class);
        OrderVO orderVO = OrderDTOToOrderVOConverter.converter(orderDTO);
        orderVO.getOrderDetailVOList().forEach(item -> item.setProductIcon(
                JSON.parseObject(JSON.toJSONString(
                        productService.findProductIcon(
                                item.getProductId()).getData()), String.class)));
        Map<String, Object> map = new HashMap<>();
        map.put("orderVO", orderVO);
        return ResultVOUtil.success(map);
    }

}
