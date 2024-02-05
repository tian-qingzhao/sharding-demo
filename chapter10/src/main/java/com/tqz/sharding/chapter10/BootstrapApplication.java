package com.tqz.sharding.chapter10;

import org.apache.shardingsphere.driver.jdbc.core.connection.ConnectionManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用 @Transactional 居然能实现分布式事务？
 *
 * <p>ShardingSphere-JDBC中的本地事务在以下两种情况是完全支持的：
 * 支持非跨库事务，比如仅分表、在单库中操作
 * 支持因逻辑异常导致的跨库事务，比如上述的操作，跨两个库插入数据，插入完成后抛出异常。
 *
 * <p>ShardingSphere-JDBC中的一条SQL会经过改写，拆分成不同数据源的SQL，比如一条select语句，
 * 会按照其中分片键拆分成对应数据源的SQL，然后在不同数据源中的执行，最终会提交或者回滚。
 *
 * <p>仅仅依靠Spring自带的本地事务(@Transactional)是无法保证跨库的分布式事务，不要被ShardingSphere-JDBC的假象迷惑了。
 * 当然ShardingSphere-JDBC对于跨库事务也提供了两种方案：<br/>
 * 1.强一致性的XA协议事务
 * 2.基于Base的柔性事务<br/>
 * <b>ShardingSphere XA 事务使用场景</b>
 * 对于 XA 事务，提供了分布式环境下，对数据强一致性的保证。但是由于存在同步阻塞问题，
 * 对性能会有一定影响。适用于对数据一致性要求非常高且对并发性能要求不是很高的业务场景。
 * <b>ShardingSphere BASE 事务使用场景</b>
 * 对于 BASE 事务，提供了分布式环境下，对数据最终一致性的保证。由于在整个事务过程中，
 * 不会像 XA 事务那样全程锁定资源，所以性能较好。适用于对并发性能要求很高并且允许出现短暂数据不一致的业务场景。
 *
 * @author <a href="https://github.com/tian-qingzhao">tianqingzhao</a>
 * @see ConnectionManager#rollback()
 * @since 2024/02/05 18:23
 */
@SpringBootApplication
@MapperScan("com.tqz.sharding.chapter10.mapper")
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }
}