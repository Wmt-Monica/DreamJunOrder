package com.dreamplume.sell.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Classname ProductCategoryForm
 * @Description TODO
 * @Date 2022/4/22 0:26
 * @Created by 翊
 */
@Data
@AllArgsConstructor
public class ProductInfoForm implements Serializable {

    /** 商品名称 */
    @NotEmpty(message = "商品名称必填")
    private String productName;

    /** 商品单价 */
    @NotEmpty(message = "商品单价必填")
    private BigDecimal productPrice;

    /** 商品描述 */
    @NotEmpty(message = "商品描述必填")
    private String productDescription;

    /** 商品小图 */
    private MultipartFile productIcon;

    /** 商品类目名称 */
    @NotEmpty(message = "商品类目名称必填")
    private String categoryName;

}
