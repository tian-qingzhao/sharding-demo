package com.tqz.sharding.chapter9.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tqz.sharding.chapter9.entity.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {

    @Select("select t1.*,t2.DICT_VALUE ORDER_STATUS_VALUE,t3.ITEM_PRICE,t3.ITEM_NAME from " +
            "T_ORDER t1 left join T_DICT t2 on t1.ORDER_STATUS = t2.DICT_CODE and t2.DICT_TYPE = 'ORDER_STATUS' " +
            "left join T_ORDER_ITEM t3 on t1.order_id = t3.order_id where t1.ORDER_ID = #{orderId}")
    List<Order> selectWithItem(long orderId);
}