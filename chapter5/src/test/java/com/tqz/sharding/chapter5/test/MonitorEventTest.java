package com.tqz.sharding.chapter5.test;

import com.google.common.collect.Lists;
import com.tqz.sharding.chapter5.entity.MonitorEvent;
import com.tqz.sharding.chapter5.mapper.MonitorEventMapper;
import com.tqz.sharding.chapter5.shardingalgorithm.DateShardingAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorEventTest {

    @Resource
    private MonitorEventMapper monitorEventMapper;

    private static final List<String> machineList = Lists.newArrayList(
            "100000016520", "100000016521", "100000016522", "100000016523", "100000016524",
            "100000016525", "100000016526", "100000016527", "100000016528", "100000016529");

    @Test
    public void addEvent() throws IOException {
        for (int i = 0; i < 10; i++) {
            MonitorEvent event = MonitorEvent.builder()
                    .machineNo(machineList.get(ThreadLocalRandom.current().nextInt(10)))
                    .eventDate(new Date())
                    .message(UUID.randomUUID().toString())
                    .dayValue(getData())
                    .build();
            System.out.println(event);
            monitorEventMapper.insert(event);
        }
    }

    private String getData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateShardingAlgorithm.FORMAT_LINK_DAY);
        return simpleDateFormat.format(new Date());
    }
}
