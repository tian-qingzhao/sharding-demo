package com.tqz.sharding.chapter12.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class ShardingTransactionConfiguration {

    @Bean
    public PlatformTransactionManager txTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}