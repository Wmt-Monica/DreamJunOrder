package com.dreamplume.sell.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Classname UpdateProductInfo
 * @Description TODO
 * @Date 2022/5/16 14:15
 * @Created by 翊
 */
@Data
@AllArgsConstructor
public class UpdateProductInfoForm implements Serializable {

    /** 商品 ID */
    private String productId;

    /** 商品名称 */
    private String productName;

    /** 商品单价 */
    private BigDecimal productPrice;

    /** 商品描述 */
    private String productDescription;

    /** 商品小图 */
    private MultipartFile productIcon;

    /** 商品类目名称 */
    private String categoryName;
}
