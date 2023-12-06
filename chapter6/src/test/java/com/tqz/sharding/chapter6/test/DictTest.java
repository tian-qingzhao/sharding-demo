package com.tqz.sharding.chapter6.test;

import com.tqz.sharding.chapter6.entity.Dict;
import com.tqz.sharding.chapter6.mapper.DictMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tianqingzhao
 * @since 2023/12/6 14:44
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DictTest {

    @Autowired
    private DictMapper dictMapper;

    @Test
    public void addDict() {
        Dict dict = Dict.builder()
                .dictType("ORDER_STATUS")
                .dictCode("4")
                .dictValue("已完成")
                .build();
        dictMapper.insert(dict);
    }
}
