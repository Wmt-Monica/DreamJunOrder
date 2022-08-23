package com.dreamplume.sell.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname ProductInfo
 * @Description TODO
 * @Date 2022/4/19 21:44
 * @Created by 翊
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo implements Serializable {

    /** 商品 id */
    @TableId
    private String productId;

    /** 商品名称 */
    private String productName;

    /** 商品单价 */
    private BigDecimal productPrice;

    /** 商品库存 */
    private Integer productStock;

    /** 商品描述 */
    private String productDescription;

    /** 商品小图 */
    private String productIcon;

    /** 商品图片文件名称 */
    private String pictureFile;

    /** 商品状态 */
    private Integer productStatus;

    /** 商品类目编号 */
    private String  categoryId;

    /** 商品售量 */
    private Long productSales;

    /** 商品创建时间 */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 商品修改时间 */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
