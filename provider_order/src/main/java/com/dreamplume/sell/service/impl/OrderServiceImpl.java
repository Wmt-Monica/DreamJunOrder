package com.dreamplume.sell.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dreamplume.sell.converter.OrderDTOToOrderMaterConverter;
import com.dreamplume.sell.dto.CartDTO;
import com.dreamplume.sell.dto.OrderDTO;
import com.dreamplume.sell.entity.*;
import com.dreamplume.sell.enums.OrderStatusEnum;
import com.dreamplume.sell.enums.PayStatusEnum;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.repository.OrderMasterDao;
import com.dreamplume.sell.service.*;
import com.dreamplume.sell.util.generator.IDGenerator;
import com.dreamplume.sell.vo.MonthAmountVO;
import com.dreamplume.sell.vo.MonthSalesVO;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname OrderServiceImpl
 * @Description TODO
 * @Date 2022/4/20 15:16
 * @Created by 翊
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderMasterDao orderMasterDao;

    @Resource
    OrderDetailService orderDetailService;

    @Resource
    WalletService walletService;

    @Resource
    ProductService productService;

    @Resource
    UserService userService;

    // 创建订单
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        // 使用唯一 ID 生成器创建 Order Id
        String orderId = IDGenerator.getInstance().getId();

        // 订单总金额
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        //1. 查询商品（数量, 价格）
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
            ResultVO<Object> findByIdResultVo = productService.findById(orderDetail.getProductId());
            if (!findByIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
                throw new SellException(SellErrorCode.get(findByIdResultVo.getCode()));
            }
            ProductInfo productInfo = JSON.parseObject(JSON.toJSONString(findByIdResultVo.getData()), ProductInfo.class);
            // 商品详情为空
            if (ObjectUtils.isEmpty(productInfo)) {
                log.info("【生成订单】购物车为空");
                throw new SellException(SellErrorCode.PRODUCT_NOT_EXIST);
            }

            //2. 计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            // 3. 写入订单详情数据库（orderDetail）
            orderDetail.setDetailId(IDGenerator.getInstance().getId());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            log.info("生成的订单详情{}",orderDetail);
            orderDetailService.insert(orderDetail);
        }


        // 4. 写入订单数据库（orderMaster）
        orderDTO.setOrderId(orderId);
        log.info("orderDTO:{}",orderDTO);
        OrderMaster orderMaster = new OrderDTOToOrderMaterConverter()
                .setOrderDTO(orderDTO)
                .setOrderAmount(orderAmount)
                .setOrderStatus(OrderStatusEnum.NEW.getCode())
                .setPayStatus(PayStatusEnum.WAIT.getCode())
                .converter();
        log.info("orderMaster:{}",orderMaster);
        orderMasterDao.insert(orderMaster);

        //4. 扣库存
        List<CartDTO> cartDTOList = orderDTO
                .getOrderDetailList()
                .stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        log.info("cartDTOList={}", cartDTOList);
        // 减库存
        String cartDTOListString = JSONObject.toJSONString(cartDTOList);
        log.info("cartDTOListString={}", cartDTOListString);
        ResultVO<Object> decreaseStockResultVO = productService.decreaseStock(cartDTOListString);
        if (!decreaseStockResultVO.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(decreaseStockResultVO.getCode()));
        }
        log.info("orderDTO={}", orderDTO);
        return orderDTO;
    }

    // 查找指定买家所有订单
    @Override
    public List<OrderDTO> findBuyerOrderList(String buyerId, Integer pageNum, Integer pageSize) {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        // 根据订单创建的时间进行降序分页互获取数据
        List<OrderMaster> buyerOrderList = orderMasterDao.selectPage(
                new Page<>(pageNum, pageSize),
                new QueryWrapper<OrderMaster>().eq("buyer_id", buyerId).orderByDesc("create_time"))
                .getRecords();
        if (buyerOrderList == null) {
            return orderDTOList;
        } else {
            for (OrderMaster orderMaster : buyerOrderList) {
                List<OrderDetail> orderDetailList = orderDetailService.findAllDetailByOrderId(orderMaster.getOrderId());
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(orderMaster, orderDTO);
                orderDTO.setOrderDetailList(orderDetailList);
                orderDTOList.add(orderDTO);
            }
            return orderDTOList;
        }
    }

    // 返回所有的买家订单
    @Override
    public List<OrderDTO> findAllBuyerOrderList(Integer pageNum, Integer pageSize) {
        List<OrderMaster> allOrderList = orderMasterDao
                .selectPage(new Page<>(pageNum, pageSize),
                        new QueryWrapper<OrderMaster>()
                                .orderByDesc("create_time"))
                .getRecords();
        return OrderMaterListToOrderDTOList(allOrderList);
    }

    // 查看所有等待完结的所有订单
    @Override
    public List<OrderDTO> findAllWaitFinishOrder(Integer pageNum, Integer pageSize) {
        List<OrderMaster> allOrderList = orderMasterDao.selectPage(
                new Page<>(pageNum, pageSize),
                new QueryWrapper<OrderMaster>()
                        .eq("order_status", OrderStatusEnum.NEW.getCode())
                        .eq("pay_status", PayStatusEnum.SUCCESS.getCode())
                        .orderByDesc("create_time"))
                .getRecords();
        return OrderMaterListToOrderDTOList(allOrderList);
    }

    // 查看等待支付的所有订单
    @Override
    public List<OrderDTO> findAllWaitPayOrder(Integer pageNum, Integer pageSize) {
        List<OrderMaster> allOrderList = orderMasterDao.selectPage(
                new Page<>(pageNum, pageSize),
                new QueryWrapper<OrderMaster>()
                        .eq("order_status", OrderStatusEnum.NEW.getCode())
                        .eq("pay_status", PayStatusEnum.WAIT.getCode())
                        .orderByDesc("create_time"))
                .getRecords();
        return OrderMaterListToOrderDTOList(allOrderList);
    }

    // 查看所有取消的订单
    @Override
    public List<OrderDTO> findAllCancelOrder(Integer pageNum, Integer pageSize) {
        List<OrderMaster> allOrderList = orderMasterDao.selectPage(
                new Page<>(pageNum, pageSize),
                new QueryWrapper<OrderMaster>()
                        .eq("order_status", OrderStatusEnum.CANCEL.getCode())
                        .orderByDesc("create_time"))
                .getRecords();
        return OrderMaterListToOrderDTOList(allOrderList);
    }

    // 查看所有完结的订单
    @Override
    public List<OrderDTO> findAllFinishOrder(Integer pageNum, Integer pageSize) {
        List<OrderMaster> allOrderList = orderMasterDao.selectPage(
                new Page<>(pageNum, pageSize),
                new QueryWrapper<OrderMaster>()
                        .eq("order_status", OrderStatusEnum.FINISHED.getCode())
                        .orderByDesc("create_time"))
                .getRecords();
        return OrderMaterListToOrderDTOList(allOrderList);
    }

    // 根据用户 Id 获取所有订单数量
    @Override
    public Integer getOrderCountByBuyerId(String buyerId) {
        return orderMasterDao.selectCount(new QueryWrapper<OrderMaster>().eq("buyer_id", buyerId));
    }

    // 根据商家 Id 获取所有订单数量
    @Override
    public Integer getAllOrderCount() {
        return orderMasterDao.selectCount(null);
    }

    // 根据订单状态获取相应订单集合个数
    @Override
    public Integer getOrderCountByStatus(Integer orderStatus, Integer payStatus) {
        return orderMasterDao.selectCount(new QueryWrapper<OrderMaster>().eq("order_status", orderStatus).eq("pay_status", payStatus));
    }

    // 获取月销量
    @Override
    public List<MonthSalesVO> getMonthSales(Integer year) {
        List<MonthSalesVO> monthSales = orderMasterDao.getMonthSales(year, OrderStatusEnum.FINISHED.getCode());
        log.info("【月销量】monthSales={}", monthSales);
        return monthSales;
    }

    // 获取某年月销售总金额
    @Override
    public List<MonthAmountVO> getMonthAmounts(Integer year) {
        List<MonthAmountVO> monthAmounts = orderMasterDao.getMonthAmounts(year, OrderStatusEnum.FINISHED.getCode());
        log.info("【月销售金额】monthAmounts={}",monthAmounts);
        return monthAmounts;
    }

   // 将 OrderMater List 集合封装成 OrderDTO List 集合
    public List<OrderDTO> OrderMaterListToOrderDTOList(List<OrderMaster> allOrderList) {
        List<OrderDTO> aLLOrderDTOList = new ArrayList<>();
        if (allOrderList == null) {
            return aLLOrderDTOList;
        } else {
            for (OrderMaster orderMaster : allOrderList) {
                aLLOrderDTOList.add(orderMasterToOrderDTO(orderMaster));
            }
            return aLLOrderDTOList;
        }
    }

    public OrderDTO orderMasterToOrderDTO(OrderMaster orderMaster) {
        List<OrderDetail> orderDetailList = orderDetailService.findAllDetailByOrderId(orderMaster.getOrderId());
        log.info("【orderMasterToOrderDTO】orderDetailList={}",orderDetailList);
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        log.info("【orderMasterToOrderDTO】orderDTO={}",orderDTO);
        return orderDTO;
    }

    // 取消订单
    @Override
    @Transactional
    public void cancelOrder(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(SellErrorCode.ORDER_STATUS_ERROR);
        }

        // 修改订单支付状态状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        if (orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            // 未支付则修改订单支付状态为取消订单即可
            orderDTO.setPayStatus(PayStatusEnum.CANCEL.getCode());
        } else if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            // 已支付订单则需退还相应的支付金额
            orderDTO.setPayStatus(PayStatusEnum.BACK.getCode());
            // 买家返回订单金额
            ResultVO<Object> backResultVo = walletService.back(orderMaster.getOrderAmount(), orderDTO.getBuyerId());
            if (!backResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
                throw new SellException(SellErrorCode.get(backResultVo.getCode()));
            }
            // 商家返还订单金额
            ResultVO<Object> findBusinessResultVo = userService.findBusiness();
            if (!findBusinessResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
                throw new SellException(SellErrorCode.get(findBusinessResultVo.getCode()));
            }
            User user = JSON.parseObject(JSON.toJSONString(findBusinessResultVo.getData()), User.class);
            ResultVO<Object> payResultVo = walletService.businessBack(orderMaster.getOrderAmount(), user.getUserId());
            if (!payResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
                throw new SellException(SellErrorCode.get(payResultVo.getCode()));
            }
        }

        // 修改订单状态
        BeanUtils.copyProperties(orderDTO, orderMaster);
        log.info("【即将取消】orderMaster:{}", orderMaster);
        int updateResult = orderMasterDao.update(orderMaster, new QueryWrapper<OrderMaster>().eq("order_id", orderDTO.getOrderId()));
        log.info("updateResult={}", updateResult);
        if (updateResult == 0) {
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(SellErrorCode.ORDER_UPDATE_FAIL);
        }

        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(SellErrorCode.ORDER_NULL);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        log.info("cartDTOList={}", cartDTOList);
        String cartDTOListString = JSONObject.toJSONString(cartDTOList);
        log.info("cartDTOListString={}", cartDTOListString);
        ResultVO<Object> increaseStockResultVo = productService.increaseStock(cartDTOListString);
        if (!increaseStockResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(increaseStockResultVo.getCode()));
        }
        log.info("【取消订单】更新成功, orderMaster:{}", orderMaster);
    }

    // 支付订单
    @Override
    @Transactional
    public void payOrder(String buyerId, String orderId, String payPassword) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper<OrderMaster>().eq("order_id", orderId);
        OrderMaster orderMaster = orderMasterDao.selectOne(wrapper);
        if (orderMaster == null) {
            log.error("【支付订单】查不到该订单, orderId={}", orderId);
            throw new SellException(SellErrorCode.ORDER_NOT_EXIST);
        } else if (!orderMaster.getBuyerId().equals(buyerId)) {
            log.error("【支付订单】订单的 buyerId 不一致. buyerId={}, orderMaster={}", buyerId, orderMaster);
            throw new SellException(SellErrorCode.ORDER_OWNER_ERROR);
        } else if (orderMaster.getPayStatus() != PayStatusEnum.WAIT.getCode()) {
            log.error("【支付订单】订单的支付状态不正确，buyerId={}", buyerId);
            throw new SellException(SellErrorCode.ORDER_PAY_STATUS_ERROR);
        }
        // 修改订单的支付状态
        orderMaster.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        if (orderMasterDao.update(orderMaster, wrapper) <= 0) {
            log.error("【支付订单】订单支付状态更新失败");
            throw new SellException(SellErrorCode.ORDER_PAY_STATE_UPDATE_FAIL);
        }

        // 买家钱包支付订单
        ResultVO<Object> payResultVo = walletService.pay(orderMaster.getOrderAmount(), buyerId, payPassword);
        if (!payResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(payResultVo.getCode()));
        }
        // 商家钱包入账
        ResultVO<Object> findBusinessResultVo = userService.findBusiness();
        if (!findBusinessResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())){
            throw new SellException(SellErrorCode.get(findBusinessResultVo.getCode()));
        }
        ResultVO<Object> backResultVo = walletService.back(orderMaster.getOrderAmount(), JSON.parseObject(JSON.toJSONString(findBusinessResultVo.getData()), User.class).getUserId());
        if (!backResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            throw new SellException(SellErrorCode.get(backResultVo.getCode()));
        }
    }

    // 完结订单
    @Override
    public void finishOrder(String orderId) {
        QueryWrapper<OrderMaster> wrapper = new QueryWrapper<OrderMaster>().eq("order_id", orderId);
        OrderMaster orderMaster = orderMasterDao.selectOne(wrapper);
        if (orderMaster == null) {
            log.error("【完结订单】订单查询失败，orderId={}", orderId);
            throw new SellException(SellErrorCode.ORDER_NOT_EXIST);
        } else if (orderMaster.getPayStatus() == PayStatusEnum.WAIT.getCode() ||
                orderMaster.getOrderStatus() == OrderStatusEnum.CANCEL.getCode() ||
                orderMaster.getOrderStatus() == OrderStatusEnum.FINISHED.getCode()) {
            log.error("【完结订单】订单状态");
            throw new SellException(SellErrorCode.ORDER_STATUS_ERROR);
        }
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        log.info("【完结订单】updateTime={}",orderMaster.getUpdateTime());
        orderMaster.setUpdateTime(new Date());
        if (orderMasterDao.update(orderMaster, wrapper) <= 0) {
            log.error("【完结订单】订单完结状态修改失败");
            throw new SellException(SellErrorCode.ORDER_STATE_UPDATE_FAIL);
        }
        log.info("【完结订单】updateTime={}",orderMaster.getUpdateTime());
    }

    // 根据订单编号查找订单
    @Override
    public OrderDTO findOrderByOrderId(String orderId) {
        OrderMaster orderMaster = orderMasterDao.selectOne(new QueryWrapper<OrderMaster>().eq("order_id", orderId));
        if (ObjectUtils.isEmpty(orderMaster)) {
            log.info("【查询订单】未查询到指定的订单");
            throw new SellException(SellErrorCode.ORDER_NOT_EXIST);
        }
        log.info("【查询订单】orderDTO={}",orderMasterToOrderDTO(orderMaster));
        return orderMasterToOrderDTO(orderMaster);
    }
}
