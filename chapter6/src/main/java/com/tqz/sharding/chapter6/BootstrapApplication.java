package com.tqz.sharding.chapter6;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 我们在系统开发时经常会使用字典表保存数据的各种状态，这些表的数据量不大，但是却被很多表关联。
 * 在使用微服务开发由于需要按照服务进行分库，那为了保证每个服务可以方便的与字典数据进行关联，
 * 我们要么通过定时任务将字典同步到各个服务所在库中，要么通过消息队列分发到各个服务。
 * 在ShardingSphere中把这样的表叫做广播表，借助ShardingSphere提供的能力可以很好的完成数据同步，
 * 每次保存数据时，每个数据库的广播表都会插入相同的数据。
 * 广播表也叫全局表，指所有的分片数据源中都存在的表，表结构及其数据在每个数据库中均完全一致。
 * 适用于数据量不大且需要与海量数据的表进行关联查询的场景，例如：字典表。
 *
 * @author tianqingzhao
 * @since 2023/12/6 14:37
 */
@SpringBootApplication
@MapperScan("com.tqz.sharding.chapter6.mapper")
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }
}