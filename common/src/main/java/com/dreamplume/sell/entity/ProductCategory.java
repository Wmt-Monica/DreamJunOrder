package com.dreamplume.sell.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname ProductCategory
 * @Description TODO
 * @Date 2022/4/19 21:44
 * @Created by 翊
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory implements Serializable {

    /** 商品类目 id */
    @TableId
    private String categoryId;

    /** 商品类目名称 */
    private String categoryName;

    /** 商品类目创建时间 */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 商品类目修改时间 */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
