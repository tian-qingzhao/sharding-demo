package com.tqz.sharding.chapter5.shardingalgorithm;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 自定义日期分片算法
 *
 * @author tianqingzhao
 * @since 2023/12/5 9:14
 */
@Slf4j
public class DateShardingAlgorithm implements StandardShardingAlgorithm<Comparable<?>> {

    public static final String FORMAT_LINK_DAY = "yyyyMMdd";

    /**
     * 插入分片算法
     *
     * @param availableTargetNames 可用的数据表
     * @param preciseShardingValue 分片值
     * @return 返回选中的数据表
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Comparable<?>> preciseShardingValue) {
        Date shardingValue = (Date) preciseShardingValue.getValue();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(shardingValue.toInstant(), ZoneId.systemDefault());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_LINK_DAY);

        String dayFormat = dateTimeFormatter.format(localDateTime);

        for (String availableTargetName : availableTargetNames) {
            if (availableTargetName.endsWith(dayFormat)) {
                return availableTargetName;
            }
        }
        return null;
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Comparable<?>> shardingValue) {

        return null;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {

    }
}