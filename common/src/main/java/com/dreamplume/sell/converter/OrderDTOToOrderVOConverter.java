package com.dreamplume.sell.converter;

import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.entity.OrderDetail;
import com.dreamplume.sell.vo.OrderDetailVO;
import com.dreamplume.sell.vo.OrderVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname OrderDTOToOrderVOConverter
 * @Description TODO
 * @Date 2022/4/22 14:38
 * @Created by 翊
 */
public class OrderDTOToOrderVOConverter {

    public static OrderVO converter(OrderDTO orderDTO) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderDTO, orderVO);
        orderVO.setOrderStatus(orderDTO.getOrderStatusEnum().getMessage());
        orderVO.setPayState(orderDTO.getPayStatusEnum().getMessage());
        List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            BeanUtils.copyProperties(orderDetail, orderDetailVO);
            orderDetailVOList.add(orderDetailVO);
        }
        orderVO.setOrderDetailVOList(orderDetailVOList);
        return orderVO;
    }

    public static List<OrderVO> converterList(List<OrderDTO> orderDTOList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (OrderDTO orderDTO : orderDTOList) {
            orderVOList.add(converter(orderDTO));
        }

        return orderVOList;
    }
}
