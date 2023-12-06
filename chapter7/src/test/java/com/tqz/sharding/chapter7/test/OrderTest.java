package com.tqz.sharding.chapter7.test;

import com.tqz.sharding.chapter7.entity.Order;
import com.tqz.sharding.chapter7.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author tianqingzhao
 * @since 2023/12/6 15:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void testWithItem() {
        List<Order> orderList = orderMapper.selectWithItem(1593185843537887233L);
        System.out.println(orderList);

        System.out.println(orderList.size());
    }

}
