package com.dreamplume.sell.converter;

import com.dreamplume.sell.entity.ProductCategory;
import com.dreamplume.sell.entity.ProductInfo;
import com.dreamplume.sell.vo.ProductInfoVO;
import com.dreamplume.sell.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Classname ProductorConvertor
 * @Description TODO
 * @Date 2022/4/20 13:35
 * @Created by 翊
 */
// 将转换类封装成单例类
@Slf4j
public class ProductInfoToProductVOConverter {


    public static List<ProductVO> converter(List<ProductInfo> productInfoList, List<ProductCategory> productCategoryList) throws IOException {
        // 组装 ProductVOList
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryId(productCategory.getCategoryId());
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryId().equals(productCategory.getCategoryId())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVO.setCategoryName(productCategory.getCategoryName());
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return productVOList;
    }
}
