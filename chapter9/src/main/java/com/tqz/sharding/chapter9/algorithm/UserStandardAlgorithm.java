package com.tqz.sharding.chapter9.algorithm;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

/**
 * 自定义分库算法，解决分片以后数据倾斜问题。
 *
 * <p>a、某个数据库实例中，部分表的数据很多，而其他表中的数据却寥寥无几，
 * 业务上的表现经常是延迟忽高忽低，飘忽不定。
 * b、数据库集群中，部分集群的磁盘使用增长特别块，而部分集群的磁盘增长却很缓慢。
 * 每个库的增长步调不一致，这种情况会给后续的扩容带来步调不一致，无法统一操作的问题。
 * 如上这2种问题被称之为 数据倾斜 问题。
 *
 * <p>那如何判断发生了数据倾斜呢？
 * 这里我们选用分库分表最大数据偏斜率作为判定依据，最大数据偏斜率 = （数据量最大样本 - 数据量最小样本）/ 数据量最小样本 。
 * 一般来说，如果我们的最大数据偏斜率在5%以内是可以接受的。
 *
 * @author <a href="https://github.com/tian-qingzhao">tianqingzhao</a>
 * @since 2024/2/5 14:58
 */
public class UserStandardAlgorithm implements StandardShardingAlgorithm<Comparable<?>> {

    private static final String SHARDING_COUNT_KEY = "sharding-count";

    private int shardingCount;

    @Getter
    private Properties props;

    @Override
    public void init(final Properties props) {
        this.props = props;
        shardingCount = getShardingCount(props);
    }

    private int getShardingCount(final Properties props) {
        Preconditions.checkArgument(props.containsKey(SHARDING_COUNT_KEY),
                "Sharding count can not be null.");
        return Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY));
    }

    /**
     * 插入分片算法
     *
     * @param availableTargetNames 可用的数据表
     * @param preciseShardingValue 分片值
     * @return 返回选中的数据表
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames,
                             PreciseShardingValue<Comparable<?>> preciseShardingValue) {
        Long shardingValue = (Long) preciseShardingValue.getValue();

        // 自定义的分库算法比较简单，使用userId的hashCode的值计算出分库序号。
        int shardingNum = (String.valueOf(shardingValue).hashCode() & Integer.MAX_VALUE) % shardingCount;

        return availableTargetNames.stream()
                .filter(targetName -> targetName.endsWith(String.valueOf(shardingNum)))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Collection<String> doSharding(Collection<String> collection,
                                         RangeShardingValue<Comparable<?>> rangeShardingValue) {
        return null;
    }

}