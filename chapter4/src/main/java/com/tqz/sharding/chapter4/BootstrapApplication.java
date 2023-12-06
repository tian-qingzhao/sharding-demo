package com.tqz.sharding.chapter4;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 分库分表的demo
 *
 * @author tianqingzhao
 * @since 2023/12/5 8:44
 */
@SpringBootApplication
@MapperScan("com.tqz.sharding.chapter4.mapper")
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }
}