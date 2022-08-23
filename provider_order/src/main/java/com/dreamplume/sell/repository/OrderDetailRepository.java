package com.dreamplume.sell.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dreamplume.sell.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Classname OrderDetailRepository
 * @Description TODO
 * @Date 2022/4/19 23:51
 * @Created by 翊
 */
@Mapper
public interface OrderDetailRepository extends BaseMapper<OrderDetail> {
}
