package com.dreamplume.sell.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dreamplume.sell.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Classname ProductCategoryRepository
 * @Description TODO
 * @Date 2022/4/19 23:44
 * @Created by 翊
 */
@Mapper
public interface ProductCategoryRepository extends BaseMapper<ProductCategory> {
}
