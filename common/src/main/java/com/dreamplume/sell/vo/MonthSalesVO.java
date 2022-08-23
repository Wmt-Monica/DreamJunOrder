package com.dreamplume.sell.vo;

import lombok.Data;

/**
 * @Classname MonthSalesVO
 * @Description TODO
 * @Date 2022/5/15 21:18
 * @Created by 翊
 */
@Data
public class MonthSalesVO {

    /** 月份 */
    private Integer month;

    /** 销售量 */
    private Long sales;
}
