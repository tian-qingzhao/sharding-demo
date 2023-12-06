package com.tqz.sharding.chapter6.test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tqz.sharding.chapter6.entity.Order;
import com.tqz.sharding.chapter6.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author tianqingzhao
 * @since 2023/12/6 14:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void selectByOrderIdTest() {
        long orderId = 938475752242806788L;
        System.out.println(orderMapper.selectWithDict(orderId));
    }

    @Test
    public void selectAllTest() {
        List<Order> orders = orderMapper.selectList(Wrappers.emptyWrapper());

        orders.forEach(System.out::println);
    }

}
