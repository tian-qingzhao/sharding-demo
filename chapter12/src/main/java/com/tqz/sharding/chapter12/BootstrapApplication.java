package com.tqz.sharding.chapter12;

import org.apache.shardingsphere.driver.jdbc.core.connection.ConnectionManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 基于Seata实现BASE事务。（这里使用的是1.5.2版本的SEATA，其他版本的集成可能需要自行调整）
 * 集成Seata时我们选用Nacos作为注册、配置中心。
 *
 * @author <a href="https://github.com/tian-qingzhao">tianqingzhao</a>
 * @see ConnectionManager#rollback()
 * @since 2024/02/06 18:23
 */
@SpringBootApplication
@MapperScan("com.tqz.sharding.chapter12.mapper")
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }
}