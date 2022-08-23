package com.dreamplume.sell.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dreamplume.sell.entity.ProductInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Classname ProductInfoRepository
 * @Description TODO
 * @Date 2022/4/19 23:41
 * @Created by 翊
 */
@Mapper
public interface ProductInfoRepository extends BaseMapper<ProductInfo> {
}
