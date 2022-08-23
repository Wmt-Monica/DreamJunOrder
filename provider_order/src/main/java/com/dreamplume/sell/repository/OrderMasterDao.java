package com.dreamplume.sell.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dreamplume.sell.entity.OrderMaster;
import com.dreamplume.sell.vo.MonthAmountVO;
import com.dreamplume.sell.vo.MonthSalesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Classname OrderMasterRepository
 * @Description TODO
 * @Date 2022/4/19 23:50
 * @Created by 翊
 */
@Mapper
public interface OrderMasterDao extends BaseMapper<OrderMaster> {

    @Select("SELECT COUNT(1) as 'sales', MONTH(create_time) as 'month' from order_master WHERE YEAR(create_time) = #{year} AND order_status = #{orderStatus} GROUP BY MONTH(create_time) ORDER BY MONTH(create_time)")
    List<MonthSalesVO> getMonthSales(@Param("year") Integer year, @Param("orderStatus") Integer orderStatus);

    @Select("SELECT SUM(order_amount) as 'amount', MONTH(create_time) as 'month' from order_master WHERE YEAR(create_time) = #{year} AND order_status = #{orderStatus} GROUP BY MONTH(create_time) ORDER BY MONTH(create_time)")
    List<MonthAmountVO> getMonthAmounts(@Param("year") Integer year, @Param("orderStatus") Integer orderStatus);
}
