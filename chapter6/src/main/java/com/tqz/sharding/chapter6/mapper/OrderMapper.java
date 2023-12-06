package com.tqz.sharding.chapter6.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tqz.sharding.chapter6.entity.Order;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

public interface OrderMapper extends BaseMapper<Order> {

    @Select("select t1.*, t2.DICT_VALUE AS order_status_value from T_ORDER t1 " +
            "left join T_DICT t2 on t1.ORDER_STATUS = t2.DICT_CODE and t2.DICT_TYPE = 'ORDER_STATUS' " +
            "where t1.ORDER_ID = #{orderId}")
    @ResultType(Order.class)
    Order selectWithDict(long orderId);
}