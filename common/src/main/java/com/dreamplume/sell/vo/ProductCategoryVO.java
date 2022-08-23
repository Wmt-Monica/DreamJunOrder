package com.dreamplume.sell.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname ProductCategoryVO
 * @Description TODO
 * @Date 2022/4/23 10:48
 * @Created by 翊
 */
@Data
public class ProductCategoryVO implements Serializable {

    /** 商品类目 ID */
    private String categoryId;

    /** 商品类目名称 */
    private String categoryName;
}
