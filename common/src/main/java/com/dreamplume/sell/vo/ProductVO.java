package com.dreamplume.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname ProductVo
 * @Description TODO
 * @Date 2022/4/20 13:27
 * @Created by 翊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO implements Serializable {

    @JsonProperty("name")  // 用于指定传输为 json 格式之后的字段的名称
    private String categoryName;  // 为的是使得在代码中可以定义一个可读性更高的属性名称

    @JsonProperty("type")
    private String  categoryId;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
