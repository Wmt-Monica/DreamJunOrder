package com.dreamplume.sell.vo;

import lombok.Data;

/**
 * @Classname MonthAmountVO
 * @Description TODO
 * @Date 2022/5/15 22:05
 * @Created by 翊
 */
@Data
public class MonthAmountVO {

    /** 月份 */
    private Integer month;

    /** 销售金总金额 */
    private Long amount;
}
