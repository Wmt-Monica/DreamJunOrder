package com.dreamplume.sell.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.dto.CartDTO;
import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.entity.OrderDetail;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname OrderFormToOrderDTOConverter
 * @Description TODO
 * @Date 2022/4/20 16:28
 * @Created by 翊
 */
@Slf4j
public class OrderFormToOrderDTOConverter {

    public static OrderDTO convert(String orderFormJson) {
        OrderDTO orderDTO = new OrderDTO();
        OrderForm orderForm = null;
        try {
            orderForm = JSONObject.parseObject(orderFormJson, OrderForm.class);
            List<CartDTO> cartDTOList = JSONArray.parseArray(orderForm.getItems(), CartDTO.class);
            log.info("orderForm:{}",orderForm);
            log.info("转换后的购物车集合：{}", cartDTOList);

//            BeanUtils.copyProperties(orderForm, orderDTO);
            orderDTO.setBuyerId(orderForm.getBuyerId());
            orderDTO.setBuyerName(orderForm.getName());
            orderDTO.setBuyerPhone(orderForm.getPhone());
            orderDTO.setBuyerAddress(orderForm.getAddress());

            List<OrderDetail> orderDetailList = new ArrayList<>();

            for (CartDTO cartDTO : cartDTOList) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProductId(cartDTO.getProductId());
                orderDetail.setProductQuantity(cartDTO.getProductQuantity());
                orderDetailList.add(orderDetail);
            }

            log.info("json 转换后的订单集合{}", orderDetailList);
            orderDTO.setOrderDetailList(orderDetailList);
        } catch (Exception e) {
            log.error("【对象转换】错误, string={}", orderFormJson);
            throw new SellException(SellErrorCode.PARAM_ERROR);
        }

        return orderDTO;
    }

}
