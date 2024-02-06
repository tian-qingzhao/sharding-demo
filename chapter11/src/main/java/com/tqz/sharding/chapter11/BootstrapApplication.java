package com.tqz.sharding.chapter11;

import org.apache.shardingsphere.driver.jdbc.core.connection.ConnectionManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 基于Atomikos实现XA分布式事务。
 *
 * <p>XA是啥？
 * 在使用 Atomikos 之前，我们先来了解一下什么是 XA。XA 是由 X/Open  组织提出的分布式事务的一种协议（或者称之为分布式架构）。
 * 它主要定义了两部分的管理器，全局事务管理器（TM）及资源管理器（RM）。在 XA 的设计理念中，把不同资源纳入到一个事务管理器进行统一管理，
 * 例如数据库资源，消息中间件资源等，从而进行全部资源的事务提交或者取消，目前主流的数据库，消息中间件都支持 XA 协议。
 *
 * <p>JTA又是啥？
 * 上面讲完 XA 协议，我们来聊聊 JTA，JTA 叫做 Java Transaction API，它是 XA 协议的 JAVA 实现。
 * 目前在 JAVA 里面，关于 JTA 的定义主要是两部分
 * 1、事务管理器接口-----javax.transaction.TransactionManager
 * 2、资源管理器接口-----javax.transaction.xa.XAResource
 * 在一般应用采用 JTA 接口实现事务，需要一个外置的 JTA 容器来存储这些事务，像 Tomcat。默认实现的 Atomikos，
 * 它是一个独立实现了 JTA 的框架，能够在我们的应用服务器中运行 JTA 事务。
 *
 * @author <a href="https://github.com/tian-qingzhao">tianqingzhao</a>
 * @see ConnectionManager#rollback()
 * @since 2024/02/06 18:23
 */
@SpringBootApplication
@MapperScan("com.tqz.sharding.chapter11.mapper")
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }
}