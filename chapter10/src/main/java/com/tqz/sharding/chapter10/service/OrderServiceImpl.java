package com.tqz.sharding.chapter10.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.tqz.sharding.chapter10.entity.Order;
import com.tqz.sharding.chapter10.mapper.OrderMapper;
import com.tqz.sharding.chapter10.util.GenIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    private static final List<Long> userList = Lists.newArrayList(20160160L, 20160161L,
            20160162L, 20160163L, 20160164L, 20160165L, 20160166L, 20160167L, 20160168L, 20160169L,
            20160170L, 20160171L, 20160172L, 20160173L, 20160174L, 20160175L, 20160176L, 20160177L,
            20160178L, 20160179L);


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void addOrder() {
        int shardNum = 4;

        for (int i = 0; i < 10; i++) {
            String orderNo = UUID.randomUUID().toString().replaceAll("-", "");
            Long userId = userList.get(ThreadLocalRandom.current().nextInt(20));

            long snowId = IdWorker.getId(Order.class);
            log.info("原始订单Id:{}", snowId);

            long orderId = GenIdUtil.buildGenId(userId, snowId, shardNum);
            log.info("生成后的订单Id:{}", orderId);

            Order order = Order.builder()
                    .orderId(orderId)
                    .orderComment("comment")
                    .userId(userId)
                    .orderNo(orderNo)
                    .totalPrice(new BigDecimal(200))
                    .orderStatus("1")
                    .orderDate(new Date())
                    .addressId(1587374567240830978L)
                    .build();

            this.baseMapper.insert(order);

            //手动模拟异常  
            if (i == 5) {
                throw new RuntimeException("人为制造异常");
            }
        }
    }
}