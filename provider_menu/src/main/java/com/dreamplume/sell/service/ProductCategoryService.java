package com.dreamplume.sell.service;

import com.dreamplume.sell.entity.ProductCategory;

import java.util.List;

/**
 * @Classname ProductCategoryService
 * @Description TODO
 * @Date 2022/4/20 13:47
 * @Created by 翊
 */
public interface ProductCategoryService {

    // 查询所有的商品类目集合
    List<ProductCategory> findAll();

    // 获取所有类目个数
    Integer findCount();

    // 分页查询所有的商品类目集合
    List<ProductCategory> findAll(Integer pageNum, Integer pageSize);

    // 根据商品类目 name 寻找商品类目
    ProductCategory findOne(String categoryName);

    // 根据商品类目 name 寻找商品类目
    String findNameById(String categoryId);

    // 添加商品类目
    void add(String categoryName);

    // 删除商品类目
    void delete(String categoryId);

    // 根绝类目 ID 修改类目名称
    void update(String categoryId, String categoryName);

}
