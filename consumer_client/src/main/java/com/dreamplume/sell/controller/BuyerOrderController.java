package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.converter.OrderDTOToOrderVOConverter;
import com.dreamplume.sell.converter.OrderFormToOrderDTOConverter;
import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.service.BuyerService;
import com.dreamplume.sell.service.OrderService;
import com.dreamplume.sell.service.ProductService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.CreateOrderVO;
import com.dreamplume.sell.vo.OrderDetailVO;
import com.dreamplume.sell.vo.OrderVO;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname BuyerOrderController
 * @Description TODO
 * @Date 2022/4/20 15:16
 * @Created by 翊
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Resource
    OrderService orderService;

    @Resource
    BuyerService buyerService;

    @Resource
    ProductService productService;

    // 生成订单
    @PostMapping("/create")
    public ResultVO<Object> create(@RequestBody @Valid String orderFormJson, BindingResult bindingResult) {
        log.info("orderForm:{}", orderFormJson);
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderFormJson={}", orderFormJson);
            return ResultVOUtil.error(SellErrorCode.PARAM_ERROR);
        }

        OrderDTO orderDTO = OrderFormToOrderDTOConverter.convert(orderFormJson);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单】购物车不能为空");
            return ResultVOUtil.error(SellErrorCode.CART_EMPTY);
        }

        ResultVO<Object> createResultVo = orderService.create(orderDTO);
        log.info("createResultVo={}", createResultVo);
        if (!createResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(createResultVo.getCode()));
        }
        CreateOrderVO createResult = JSON.parseObject(JSON.toJSONString(createResultVo.getData()), CreateOrderVO.class);
        log.info("createResult={}", createResult);
        Map<String, CreateOrderVO> map = new HashMap<>();
        map.put("order", createResult);
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    //订单列表
    @GetMapping("/list")
    public ResultVO<Object> list(@RequestParam("buyerId") String buyerId,
                                 @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        if (ObjectUtils.isEmpty(buyerId)) {
            log.error("【查询订单列表】buyerId 为空");
            throw new SellException(SellErrorCode.PARAM_ERROR);
        }
        ResultVO<Object> findBuyerOrderListResultVo = orderService.findBuyerOrderList(buyerId, pageNum, pageSize);
        if (!findBuyerOrderListResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findBuyerOrderListResultVo.getCode()));
        }
        List<OrderDTO> allBuyerOrderList = JSON.parseArray(JSONArray.toJSONString(findBuyerOrderListResultVo.getData()), OrderDTO.class);
        List<OrderVO> allBuyerOrderVOList = new ArrayList<>();
        for (OrderDTO orderDTO : allBuyerOrderList) {
            OrderVO orderVO = OrderDTOToOrderVOConverter.converter(orderDTO);
            orderVO.getOrderDetailVOList().forEach(item -> item.setProductIcon(
                    JSON.parseObject(JSON.toJSONString(
                            productService.findProductIcon(
                                    item.getProductId()).getData()), String.class)));
            allBuyerOrderVOList.add(orderVO);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("allBuyerOrderVOList", allBuyerOrderVOList);
        ResultVO<Object> getOrderCountByBuyerIdResultVo = orderService.getOrderCountByBuyerId(buyerId);
        if (!getOrderCountByBuyerIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getOrderCountByBuyerIdResultVo.getCode()));
        }
        map.put("count", JSON.parseObject(JSON.toJSONString(getOrderCountByBuyerIdResultVo.getData()), Integer.class));
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    //订单详情
    @GetMapping("/detail")
    public ResultVO<Object> detail(@RequestParam("buyerId") String buyerId,
                                   @RequestParam("orderId") String orderId) {
        OrderDTO orderDTO = buyerService.findOrderOne(buyerId, orderId);
        OrderVO orderVO = OrderDTOToOrderVOConverter.converter(orderDTO);
        List<OrderDetailVO> orderDetailVOList = orderVO.getOrderDetailVOList();
        for (OrderDetailVO orderDetailVO : orderDetailVOList) {
            ResultVO<Object> findProductIconResultVo = productService.findProductIcon(orderDetailVO.getProductId());
            if (!findProductIconResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
                return ResultVOUtil.error(SellErrorCode.get(findProductIconResultVo.getCode()));
            }
            orderDetailVO.setProductIcon(findProductIconResultVo.getData().toString());
        }
        return ResultVOUtil.success(JSONObject.toJSON(orderVO));
    }

    //取消订单
    @PutMapping("/cancel")
    public ResultVO<Object> cancel(@RequestParam("buyerId") String buyerId,
                                   @RequestParam("orderId") String orderId) {
        buyerService.cancelOrder(buyerId, orderId);
        return ResultVOUtil.success();
    }

    // 支付订单
    @PostMapping("/pay")
    public ResultVO<Object> pay(@RequestParam("buyerId") String buyerId,
                                @RequestParam("orderId") String orderId,
                                @RequestParam("payPassword") String payPassword) {
        buyerService.payOrder(buyerId, orderId, payPassword);
        return ResultVOUtil.success();
    }
}
