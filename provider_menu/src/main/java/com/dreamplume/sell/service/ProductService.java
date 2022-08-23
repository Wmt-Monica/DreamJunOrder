package com.dreamplume.sell.service;

import com.dreamplume.sell.dto.CartDTO;
import com.dreamplume.sell.entity.ProductInfo;
import com.dreamplume.sell.enums.ProductStockEnum;
import com.dreamplume.sell.form.ProductInfoForm;
import com.dreamplume.sell.form.UpdateProductInfoForm;

import java.util.List;

/**
 * @Classname ProductService
 * @Description TODO
 * @Date 2022/4/19 23:57
 * @Created by 翊
 */
public interface ProductService {

    /**
     * 根据商品类目获取一以下所有的商品（上架 + 下架）
     * @param categoryId 商品类目 ID
     * @param status 商品状态
     * @return 指定商品类目 categoryId 及其状态的以下所有的商品集合
     */
    List<ProductInfo> findByCategoryId(String categoryId, Integer status);

    // 查看所有的上架菜品
    List<ProductInfo> findAllUp();

    // 查看火热的菜品
    List<ProductInfo> findHost(Integer productCount);

    // 根据 ProductId 查找商品详情
    ProductInfo findById(String productId);

    // 订单生成：减库存
    void decreaseStock(List<CartDTO> cartDTOList);

    // 订单取消：加库存
    void increaseStock(List<CartDTO> cartDTOList);

    // 添加菜品
    ProductInfo add(ProductInfoForm categoryForm);

    // 上架菜品
    ProductInfo up(String productId);

    // 下架菜品
    ProductInfo down(String productId);

    /**
     * 商家端进行商品的修改
     * @param productId 商品 Id
     * @param updateStock 修改库存数量
     * @param stockEnum 商品修改库存的枚举对象,符号(-1/1) 用于判断是增库存还是减库存
     * @return 修改后的商品数量
     */
    ProductInfo updateProductStock(String productId, Integer updateStock, ProductStockEnum stockEnum);

    // 查找商品的图片
    String findProductIcon(String productId);

    // 根据商品类目 ID 删除下面的所有的菜品
    List<ProductInfo> deleteByCategoryId(String categoryId);

    /**
     * 更新菜品详情
     * @param form 表单
     */
    ProductInfo updateProductInfo(UpdateProductInfoForm form);
}
