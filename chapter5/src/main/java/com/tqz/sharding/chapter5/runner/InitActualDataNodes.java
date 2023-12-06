package com.tqz.sharding.chapter5.runner;

import com.google.common.collect.Lists;
import com.tqz.sharding.chapter5.shardingalgorithm.DateShardingAlgorithm;
import com.tqz.sharding.chapter5.util.ApplicationContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.infra.database.DefaultDatabase;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自动建表，实际使用时需要改成通过定时任务的方式来调用
 *
 * @author tianqingzhao
 * @since 2023/12/5 9:24
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class InitActualDataNodes implements ApplicationRunner {

    private final DataSource dataSource;

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            r -> new Thread(r, "update-shard-rule-actual-data-node"));

    private static final long INITIAL_DELAY = 0;

    private static final long PERIOD = 24;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ShardingSphereDataSource shardingSphereDataSource = (ShardingSphereDataSource) dataSource;

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                log.info("更新分片路由节点数据...");
                updateShardRuleActualDataNodes(shardingSphereDataSource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, INITIAL_DELAY, PERIOD, TimeUnit.HOURS);
    }

    private ContextManager getContextManager(ShardingSphereDataSource dataSource) {
        try {
            Field contextManagerField = dataSource.getClass().getDeclaredField("contextManager");
            contextManagerField.setAccessible(true);
            return (ContextManager) contextManagerField.get(dataSource);
        } catch (Exception e) {
            throw new RuntimeException("系统异常");
        }
    }

    /**
     * 重新构建actualDataNodes
     *
     * @param shardingSphereDataSource
     */
    private void updateShardRuleActualDataNodes(ShardingSphereDataSource shardingSphereDataSource) {
        // Context Manager
        ContextManager contextManager = getContextManager(shardingSphereDataSource);

        Collection<RuleConfiguration> newRuleConfigList = new LinkedList<>();

        Collection<RuleConfiguration> oldRuleConfigList = contextManager.getMetaDataContexts()
                .getMetaData().getDatabases()
                .get(DefaultDatabase.LOGIC_NAME).getRuleMetaData()
                .getConfigurations();

        for (RuleConfiguration configuration : oldRuleConfigList) {

            if (configuration instanceof ShardingRuleConfiguration) {

                ShardingRuleConfiguration oldAlgorithmConfig = (ShardingRuleConfiguration) configuration;

                ShardingRuleConfiguration newAlgorithmConfig = new ShardingRuleConfiguration();

                Collection<ShardingTableRuleConfiguration> addTableConfigurations = new LinkedList<>();

                // 构建表
                for (ShardingTableRuleConfiguration shardingTableRuleConfiguration : oldAlgorithmConfig.getTables()) {

                    // 重新构建ShardingTableRuleConfiguration
                    ShardingTableRuleConfiguration addTableConfiguration = createTableRule(shardingTableRuleConfiguration);

                    addTableConfigurations.add(addTableConfiguration);
                }

                newAlgorithmConfig.setTables(addTableConfigurations);
                newAlgorithmConfig.setAutoTables(oldAlgorithmConfig.getAutoTables());
                newAlgorithmConfig.setBindingTableGroups(oldAlgorithmConfig.getBindingTableGroups());
                newAlgorithmConfig.setBroadcastTables(oldAlgorithmConfig.getBroadcastTables());
                newAlgorithmConfig.setDefaultDatabaseShardingStrategy(oldAlgorithmConfig.getDefaultDatabaseShardingStrategy());
                newAlgorithmConfig.setDefaultTableShardingStrategy(oldAlgorithmConfig.getDefaultTableShardingStrategy());
                newAlgorithmConfig.setDefaultKeyGenerateStrategy(oldAlgorithmConfig.getDefaultKeyGenerateStrategy());
                newAlgorithmConfig.setDefaultShardingColumn(oldAlgorithmConfig.getDefaultShardingColumn());
                newAlgorithmConfig.setShardingAlgorithms(oldAlgorithmConfig.getShardingAlgorithms());
                newAlgorithmConfig.setKeyGenerators(oldAlgorithmConfig.getKeyGenerators());

                newRuleConfigList.add(newAlgorithmConfig);
            } else {
                newRuleConfigList.add(configuration);
            }

        }
        contextManager.alterRuleConfiguration("logic_db", newRuleConfigList);
    }

    private ShardingTableRuleConfiguration createTableRule(ShardingTableRuleConfiguration shardingTableRuleConfiguration) {
        String logicTable = shardingTableRuleConfiguration.getLogicTable();

        //自动建表
        reCreateShardingTables(logicTable);

        String actualDataNodes = buildActualDataNodes(shardingTableRuleConfiguration.getActualDataNodes(), logicTable);

        ShardingTableRuleConfiguration addTableConfiguration = new ShardingTableRuleConfiguration(logicTable,
                actualDataNodes);

        addTableConfiguration.setTableShardingStrategy(shardingTableRuleConfiguration.getTableShardingStrategy());
        addTableConfiguration.setDatabaseShardingStrategy(shardingTableRuleConfiguration.getDatabaseShardingStrategy());
        addTableConfiguration.setKeyGenerateStrategy(shardingTableRuleConfiguration.getKeyGenerateStrategy());
        return addTableConfiguration;
    }

    /**
     * 重新构建ActualDataNodes，实现自动刷新数据节点和自动建表
     * 如果项目集成了配置中心，要实现刷新数据节点只需要修改配置中心的值即可。
     * 如果未集成配置中心则需要通过定时任务周期性的刷新actual-data-nodes ，比如实现方案中会每天将数据节点刷新成包含前15天的数据节点。
     * ds$->{0..1}.T_EVENT_$->{[20221114,20221113,20221112,20221111,20221110,20221109,20221108,20221107,
     * 20221106,20221105,20221104,20221103,20221102,20221101,20221031,20221030]}
     *
     * @param actualDataNodes 原真实节点
     * @param logicTable      逻辑表
     * @return 新节点
     */
    private String buildActualDataNodes(String actualDataNodes, String logicTable) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateShardingAlgorithm.FORMAT_LINK_DAY);
        List<String> timeList = Lists.newArrayList(today.format(dateFormatter));

        // 倒推15天
        // 数据的表没有当前天往前推15天的表会报错：
        // bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException:
        // Table 'monitor_0.t_event_20231202' doesn't exist
        for (int i = 1; i <= 15; i++) {
            LocalDate day = today.minusDays(i);
            timeList.add(day.format(dateFormatter));
        }

        String el = StringUtils.join(timeList, ",");

        String dataNodesPrefix = actualDataNodes.substring(0, actualDataNodes.indexOf(logicTable));

        String actualDataNodesNew = dataNodesPrefix + logicTable + "_$->{[" + el + "]}";

        log.info("actualDataNodesNew is {}", actualDataNodesNew);

        return actualDataNodesNew;
    }

    /**
     * 自动创建表我们需要通过传统的JDBC方式，使用DriverManager创建数据库连接来执行类似如下的语句：
     * <code>Create table if not exists `T_EVENT_20221130` like `T_EVENT_20221129`</code>
     *
     * @param logicTable sharding逻辑表
     */
    private void reCreateShardingTables(String logicTable) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateShardingAlgorithm.FORMAT_LINK_DAY);
        LocalDate today = LocalDate.now();
        String todayTable = logicTable + "_" + today.format(dateFormatter);
        //根据日期分表配置的最后一天
        LocalDate tomorrow = today.plusDays(1);
        String tomorrowTable = logicTable + "_" + tomorrow.format(dateFormatter);

        String sql = "Create table if not exists `" + tomorrowTable + "` like `" + todayTable + "`";

        log.info("自动建表语句{}", sql);

        Environment env = ApplicationContextUtil.getApplicationContext().getEnvironment();
        String dataNames = env.getProperty("spring.shardingsphere.datasource.names");
        String url, username, password;
        // 这里没用原生的ShardingDataSource是因为ShardingDataSource只能在一个数据源中使用
        for (String dataName : dataNames.split(",")) {
            url = Objects.requireNonNull(env.getProperty("spring.shardingsphere.datasource." + dataName + ".jdbc-url"));
            username = Objects.requireNonNull(env.getProperty("spring.shardingsphere.datasource." + dataName + ".username"));
            password = Objects.requireNonNull(env.getProperty("spring.shardingsphere.datasource." + dataName + ".password"));

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 Statement statement = connection.createStatement()) {
                statement.execute(sql);
            } catch (Exception e) {
                throw new RuntimeException("自动建表失败");
            }
        }
    }

}
