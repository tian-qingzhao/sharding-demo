package com.tqz.sharding.chapter2.test;

import com.tqz.sharding.chapter2.entity.Order;
import com.tqz.sharding.chapter2.mapper.OrderMapper;
import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void addOrder() {
        for (int i = 0; i <= 11; i++) {
            Order order = new Order();
            order.setOrderComment("comment");
            order.setUserId(20160169L);
            order.setOrderNo("100" + i);
            order.setTotalPrice(new BigDecimal(200));
            order.setOrderStatus("1");
            order.setOrderDate(new Date());
            order.setAddressId(1587374567240830978L);
            orderMapper.insert(order);
        }
    }

    @Test
    public void generateKeyTest() {
        SnowflakeKeyGenerateAlgorithm algorithm = new SnowflakeKeyGenerateAlgorithm();

        for (int i = 0; i <= 10; i++) {
            System.out.println(algorithm.generateKey() % 4);
        }
    }
}