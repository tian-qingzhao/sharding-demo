package com.tqz.sharding.chapter9;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 对于toC的业务，需要选择用户属性如 user_id 作为分片键。
 * 那问题来了，对于订单表来说，选择了user_id作为分片键以后如何查看订单详情呢？比如下面这样一条SQL：
 * SELECT * FROM T_ORDER WHERE order_id = 801462878019256325
 * 由于查询条件中的order_id不是分片键，所以需要查询所有分片才能得到最终的结果。
 * 如果下面有 1000 个分片，那么就需要执行 1000 次这样的 SQL，这时性能就比较差了。
 * 可以通过ShardingSphere-JDBC生成的SQL得知，根据order_id查询会对所有分片进行查询然后通过UNION ALL 进行合并。
 *
 * <p>但是，我们知道 order_id 是主键，应该只有一条返回记录，也就是说，order_id 只存在于一个分片中。这时，可以有以下三种设计：
 * 有冗余法、索引表法、基因法三种，索引表法是冗余法的改进。
 *
 * <p>基于基因法实现非sharding key 查询。
 *
 * @author <a href="https://github.com/tian-qingzhao">tianqingzhao</a>
 * @since 2024/1/26 9:48
 */
@SpringBootApplication
@MapperScan("com.tqz.sharding.chapter9.mapper")
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }
}