package com.tqz.sharding.chapter9.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.tqz.sharding.chapter9.entity.Order;
import com.tqz.sharding.chapter9.mapper.OrderMapper;
import com.tqz.sharding.chapter9.mapper.OrderUserRelationMapper;
import com.tqz.sharding.chapter9.util.GenIdUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author tianqingzhao
 * @since 2023/12/6 15:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderUserRelationMapper orderUserRelationMapper;

    private static final Logger log = LoggerFactory.getLogger(OrderTest.class);

    @Test
    public void addOrderWithIndex() {
        int shardNum = 4;
        for (int i = 0; i < 1; i++) {
            String orderNo = UUID.randomUUID().toString().replaceAll("-", "");
            Long userId = ThreadLocalRandom.current().nextLong(20);
            long snowId = IdWorker.getId(Order.class);
            log.info("原始订单Id:{}", snowId);
            long orderId = GenIdUtil.buildGenId(userId, snowId, shardNum);
            log.info("生成后的订单Id:{}", orderId);
            Order order = Order.builder()
                    .orderId(orderId)
                    .orderComment("comment")
                    .userId(userId)
                    .orderNo(orderNo)
                    .totalPrice(new BigDecimal(200))
                    .orderStatus("1")
                    .orderDate(new Date())
                    .addressId(1587374567240830978L)
                    .build();
            orderMapper.insert(order);
        }
    }

    /**
     * 根据user_id查询
     */
    @Test
    public void queryByUserId() {
        Long userId = 14L;
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Order> orders = orderMapper.selectList(queryWrapper);
        orders.forEach(System.out::println);

        // 根据user_id进行查询时可以直接定位到分片键
        // 024-01-26 16:38:20.839  INFO 13808 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER
        //
        // WHERE (user_id = ?)
        //2024-01-26 16:38:20.839  INFO 13808 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(super=SelectStatement(super=AbstractSQLStatement(parameterCount=1, parameterMarkerSegments=[ParameterMarkerExpressionSegment(startIndex=130, stopIndex=130, parameterMarkerIndex=0, parameterMarkerType=QUESTION, alias=Optional.empty)], commentSegments=[]), projections=ProjectionsSegment(startIndex=8, stopIndex=93, projections=[ColumnProjectionSegment(column=ColumnSegment(startIndex=8, stopIndex=15, identifier=IdentifierValue(value=order_id, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=17, stopIndex=23, identifier=IdentifierValue(value=user_id, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=25, stopIndex=32, identifier=IdentifierValue(value=order_no, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=34, stopIndex=43, identifier=IdentifierValue(value=address_id, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=45, stopIndex=56, identifier=IdentifierValue(value=order_status, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=58, stopIndex=68, identifier=IdentifierValue(value=total_price, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=70, stopIndex=79, identifier=IdentifierValue(value=order_date, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=81, stopIndex=93, identifier=IdentifierValue(value=order_comment, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty)], distinctRow=false), from=SimpleTableSegment(tableName=TableNameSegment(startIndex=101, stopIndex=107, identifier=IdentifierValue(value=T_ORDER, quoteCharacter=NONE)), owner=Optional.empty, alias=Optional.empty), where=Optional[WhereSegment(startIndex=113, stopIndex=131, expr=BinaryOperationExpression(startIndex=120, stopIndex=130, left=ColumnSegment(startIndex=120, stopIndex=126, identifier=IdentifierValue(value=user_id, quoteCharacter=NONE), owner=Optional.empty), right=ParameterMarkerExpressionSegment(startIndex=130, stopIndex=130, parameterMarkerIndex=0, parameterMarkerType=QUESTION, alias=Optional.empty), operator==, text=user_id = ?))], groupBy=Optional.empty, having=Optional.empty, orderBy=Optional.empty, combine=Optional.empty), table=Optional.empty, limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
        //2024-01-26 16:38:20.839  INFO 13808 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER_2
        //
        // WHERE (user_id = ?) ::: [14]
        //Order(orderId=1750753010159124481, userId=14, orderNo=3eeab0df287a48f8b4777cdcad2cfa31, addressId=1587374567240830978, orderStatus=1, totalPrice=200.00, orderDate=Fri Jan 26 13:30:20 CST 2024, orderComment=comment)
    }

    /**
     * 根据order_id查询
     */
    @Test
    public void queryByOrderId() {
        Long orderId = 1750753015943069698L;
        Long userId = 17L;
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);

        // 在使用了 `UserStandardAlgorithm` 分库算法之后，并且使用了 userId 的hashCode进行计算分库号，
        // 所以查询的时候要带上 userId 字段
        queryWrapper.eq("user_id", userId);

        List<Order> orders = orderMapper.selectList(queryWrapper);
        orders.forEach(System.out::println);

        // 当使用order_id进行查询时可以直接定位到对应的分片，只需要查询一次即可。
        // 2024-01-26 16:43:47.485  INFO 23072 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER
        //
        // WHERE (order_id = ?)
        //2024-01-26 16:43:47.485  INFO 23072 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(super=SelectStatement(super=AbstractSQLStatement(parameterCount=1, parameterMarkerSegments=[ParameterMarkerExpressionSegment(startIndex=131, stopIndex=131, parameterMarkerIndex=0, parameterMarkerType=QUESTION, alias=Optional.empty)], commentSegments=[]), projections=ProjectionsSegment(startIndex=8, stopIndex=93, projections=[ColumnProjectionSegment(column=ColumnSegment(startIndex=8, stopIndex=15, identifier=IdentifierValue(value=order_id, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=17, stopIndex=23, identifier=IdentifierValue(value=user_id, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=25, stopIndex=32, identifier=IdentifierValue(value=order_no, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=34, stopIndex=43, identifier=IdentifierValue(value=address_id, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=45, stopIndex=56, identifier=IdentifierValue(value=order_status, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=58, stopIndex=68, identifier=IdentifierValue(value=total_price, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=70, stopIndex=79, identifier=IdentifierValue(value=order_date, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=81, stopIndex=93, identifier=IdentifierValue(value=order_comment, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty)], distinctRow=false), from=SimpleTableSegment(tableName=TableNameSegment(startIndex=101, stopIndex=107, identifier=IdentifierValue(value=T_ORDER, quoteCharacter=NONE)), owner=Optional.empty, alias=Optional.empty), where=Optional[WhereSegment(startIndex=113, stopIndex=132, expr=BinaryOperationExpression(startIndex=120, stopIndex=131, left=ColumnSegment(startIndex=120, stopIndex=127, identifier=IdentifierValue(value=order_id, quoteCharacter=NONE), owner=Optional.empty), right=ParameterMarkerExpressionSegment(startIndex=131, stopIndex=131, parameterMarkerIndex=0, parameterMarkerType=QUESTION, alias=Optional.empty), operator==, text=order_id = ?))], groupBy=Optional.empty, having=Optional.empty, orderBy=Optional.empty, combine=Optional.empty), table=Optional.empty, limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
        //2024-01-26 16:43:47.486  INFO 23072 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER_2
        //
        // WHERE (order_id = ?) ::: [1750753015943069698]
    }

}
