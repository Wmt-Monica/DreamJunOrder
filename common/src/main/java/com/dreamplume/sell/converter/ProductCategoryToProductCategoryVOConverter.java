package com.dreamplume.sell.converter;

import com.dreamplume.sell.entity.ProductCategory;
import com.dreamplume.sell.vo.ProductCategoryVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname ProductCategoryToProductCategoryVOConverter
 * @Description TODO
 * @Date 2022/4/23 10:52
 * @Created by 翊
 */
@Slf4j
public class ProductCategoryToProductCategoryVOConverter {

    public static List<ProductCategoryVO> converter(List<ProductCategory> productCategoryList) {
        List<ProductCategoryVO> productCategoryVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            productCategoryVO.setCategoryId(productCategory.getCategoryId());
            productCategoryVO.setCategoryName(productCategory.getCategoryName());
            productCategoryVOList.add(productCategoryVO);
        }
        log.info("【ProductCategoryToProductCategoryVOConverter】={}",productCategoryVOList);
        return productCategoryVOList;
    }
}
