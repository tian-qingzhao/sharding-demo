package com.tqz.sharding.chapter9.algorithm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义复合分片算法。
 *
 * <p>在使用基因法的时候需要通过两个字段都可以直接定位到路由，而ShardingSphere中的标准分片算法只能支持单分片键，
 * 所以我们需要实现复合分片算法ComplexKeysShardingAlgorithm ，让其能支持多个分片键查询。
 * 复合分片策略主要是实现 {@link #doSharding} 方法，由于order_id和订单id的基因会被改成一样，所以对其取模结果肯定也一致。
 *
 * @author <a href="https://github.com/tian-qingzhao">tianqingzhao</a>
 * @since 2024/1/26 15:32
 */
public class UserAndOrderComplexTableAlgorithm implements ComplexKeysShardingAlgorithm<Comparable<?>> {

    private static final String SHARDING_COUNT_KEY = "sharding-count";

    private int shardingCount;

    @Getter
    private Properties props;

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames,
                                         ComplexKeysShardingValue<Comparable<?>> shardingValue) {
        Map<String, Collection<Comparable<?>>> columnNameAndShardingValuesMap =
                shardingValue.getColumnNameAndShardingValuesMap();
        // 获取订单id
        Collection<Comparable<?>> orderIds = columnNameAndShardingValuesMap
                .getOrDefault("order_id", new ArrayList<>(1));
        // 获取客户id
        Collection<Comparable<?>> customerIds = columnNameAndShardingValuesMap
                .getOrDefault("user_id", new ArrayList<>(1));
        // 整合订单id和客户id
        List<Long> ids = Lists.newArrayList();
        ids.addAll(ids2Long(orderIds));
        ids.addAll(ids2Long(customerIds));

        return ids.stream()
                // 对可用的表名求余数，获取到真实的表的后缀
                .map(idSuffix -> idSuffix % shardingCount)
                // 转换成string
                .map(String::valueOf)
                // 获取到真实的表
                .map(tableSuffix -> availableTargetNames.stream()
                        .filter(targetName -> targetName.endsWith(tableSuffix))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties props) {
        this.props = props;
        shardingCount = getShardingCount(props);
    }

    private int getShardingCount(final Properties props) {
        Preconditions.checkArgument(props.containsKey(SHARDING_COUNT_KEY),
                "Sharding count can not be null.");
        return Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY));
    }

    private Collection<Long> ids2Long(Collection<Comparable<?>> ids) {
        List<Long> result = new ArrayList<>(ids.size());
        ids.forEach(id -> result.add((Long) id));
        return result;
    }
}
