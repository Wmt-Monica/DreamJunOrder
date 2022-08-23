package com.dreamplume.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品详情
 * Created by 廖师兄
 * 2017-05-12 14:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoVO implements Serializable {

    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

    @JsonProperty("categoryName")
    private String categoryName;

    @JsonProperty("description")
    private String productDescription;

    @JsonProperty("icon")
    private String productIcon;

    /** 商品图片文件名称 */
    private String pictureFile;

    /** 商品库存 */
    @JsonProperty("stock")
    private Integer productStock;

    /** 商品售量 */
    private Long productSales;
}
