package com.tqz.sharding.chapter7;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 多表关联之绑定表。
 *
 * <p>开发时会经常使用主表明细表这样的数据结构，比如订单表和订单明细表，他们通过订单ID进行关联。
 * 现在我们对订单表进行分库分表设计，很显然也需要对订单明细表进行分库分表，他们的分片键都选择使用order_id。
 * 此时当我们对两张表进行关联查询时会产生笛卡尔积，即最终关联的SQL会很多。
 *
 * @author tianqingzhao
 * @since 2023/12/6 14:37
 */
@SpringBootApplication
@MapperScan("com.tqz.sharding.chapter7.mapper")
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }
}