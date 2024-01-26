package com.tqz.sharding.chapter8.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tqz.sharding.chapter8.entity.Order;
import com.tqz.sharding.chapter8.entity.OrderUserRelation;
import com.tqz.sharding.chapter8.mapper.OrderMapper;
import com.tqz.sharding.chapter8.mapper.OrderUserRelationMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    /**
     * 插入测试数据
     */
    @Test
    public void addOrderWithIndex() {
        for (int i = 0; i < 4; i++) {
            String orderNo = UUID.randomUUID().toString().replaceAll("-", "");
            Long userId = ThreadLocalRandom.current().nextLong(20);
            Order order = Order.builder()
                    .orderComment("comment")
                    .userId(userId)
                    .orderNo(orderNo)
                    .totalPrice(new BigDecimal(200))
                    .orderStatus("1")
                    .orderDate(new Date())
                    .addressId(1587374567240830978L)
                    .build();

            orderMapper.insert(order);

            OrderUserRelation orderUserRelation = OrderUserRelation.builder()
                    .orderId(order.getOrderId())
                    .userId(userId).build();

            orderUserRelationMapper.insert(orderUserRelation);
        }
    }

    /**
     * 没有使用索引表查询
     */
    @Test
    public void queryByOrderId() {
        //原始SQL
        //String sql = "select * from t_order where order_id = 1595983261975236610";

        Long orderId = 1750753010159124481L;

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);

        List<Order> orders = orderMapper.selectList(queryWrapper);

        orders.forEach(System.out::println);

        // 2024-01-26 13:51:09.656  INFO 20972 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER_0
        //
        // WHERE (order_id = ?) UNION ALL SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER_1
        //
        // WHERE (order_id = ?) UNION ALL SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER_2
        //
        // WHERE (order_id = ?) UNION ALL SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER_3
        //
        // WHERE (order_id = ?) ::: [1750753010159124481, 1750753010159124481, 1750753010159124481, 1750753010159124481]
        //Order(orderId=1750753010159124481, userId=14, orderNo=3eeab0df287a48f8b4777cdcad2cfa31, addressId=1587374567240830978, orderStatus=1, totalPrice=200.00, orderDate=Fri Jan 26 13:30:20 CST 2024, orderComment=comment)
        // 由于order_id不是分片键，所以对于order_id的查询需要在每个分片都查询一次而后通过union all进行合并，性能肯定大大受影响。
    }

    /**
     * 利用索引表查询
     */
    @Test
    public void queryByOrderIdWithIndex() {
        //原始SQL
        //String sql = "select * from t_order where order_id = 1595983261975236610";

        Long orderId = 1750753010159124481L;

        //Step1 查询对应对用户信息
        OrderUserRelation orderUserRelation = orderUserRelationMapper.selectOne(
                Wrappers.<OrderUserRelation>query()
                        .eq("order_id", orderId));

        // Step2 根据UserId 和 OrderId查询
        Long userId = orderUserRelation.getUserId();

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("user_id", userId);

        List<Order> orders = orderMapper.selectList(queryWrapper);

        orders.forEach(System.out::println);

        // 第一步从索引表中查询出对应的userId
        // 第二步根据 userId 和 orderId 共同查询，由于userId是分片键，所以可以直接定位到分片。

        // 2024-01-26 14:50:39.191  INFO 15392 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT  order_id,user_id  FROM idx_orderid_userid
        //
        // WHERE (order_id = ?) ::: [1750753010159124481]
        //2024-01-26 14:50:39.326  INFO 15392 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER
        //
        // WHERE (order_id = ? AND user_id = ?)
        //2024-01-26 14:50:39.326  INFO 15392 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(super=SelectStatement(super=AbstractSQLStatement(parameterCount=2, parameterMarkerSegments=[ParameterMarkerExpressionSegment(startIndex=131, stopIndex=131, parameterMarkerIndex=0, parameterMarkerType=QUESTION, alias=Optional.empty), ParameterMarkerExpressionSegment(startIndex=147, stopIndex=147, parameterMarkerIndex=1, parameterMarkerType=QUESTION, alias=Optional.empty)], commentSegments=[]), projections=ProjectionsSegment(startIndex=8, stopIndex=93, projections=[ColumnProjectionSegment(column=ColumnSegment(startIndex=8, stopIndex=15, identifier=IdentifierValue(value=order_id, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=17, stopIndex=23, identifier=IdentifierValue(value=user_id, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=25, stopIndex=32, identifier=IdentifierValue(value=order_no, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=34, stopIndex=43, identifier=IdentifierValue(value=address_id, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=45, stopIndex=56, identifier=IdentifierValue(value=order_status, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=58, stopIndex=68, identifier=IdentifierValue(value=total_price, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=70, stopIndex=79, identifier=IdentifierValue(value=order_date, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty), ColumnProjectionSegment(column=ColumnSegment(startIndex=81, stopIndex=93, identifier=IdentifierValue(value=order_comment, quoteCharacter=NONE), owner=Optional.empty), alias=Optional.empty)], distinctRow=false), from=SimpleTableSegment(tableName=TableNameSegment(startIndex=101, stopIndex=107, identifier=IdentifierValue(value=T_ORDER, quoteCharacter=NONE)), owner=Optional.empty, alias=Optional.empty), where=Optional[WhereSegment(startIndex=113, stopIndex=148, expr=BinaryOperationExpression(startIndex=120, stopIndex=147, left=BinaryOperationExpression(startIndex=120, stopIndex=131, left=ColumnSegment(startIndex=120, stopIndex=127, identifier=IdentifierValue(value=order_id, quoteCharacter=NONE), owner=Optional.empty), right=ParameterMarkerExpressionSegment(startIndex=131, stopIndex=131, parameterMarkerIndex=0, parameterMarkerType=QUESTION, alias=Optional.empty), operator==, text=order_id = ?), right=BinaryOperationExpression(startIndex=137, stopIndex=147, left=ColumnSegment(startIndex=137, stopIndex=143, identifier=IdentifierValue(value=user_id, quoteCharacter=NONE), owner=Optional.empty), right=ParameterMarkerExpressionSegment(startIndex=147, stopIndex=147, parameterMarkerIndex=1, parameterMarkerType=QUESTION, alias=Optional.empty), operator==, text=user_id = ?), operator=AND, text=order_id = ? AND user_id = ?))], groupBy=Optional.empty, having=Optional.empty, orderBy=Optional.empty, combine=Optional.empty), table=Optional.empty, limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
        //2024-01-26 14:50:39.326  INFO 15392 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds0 ::: SELECT  order_id,user_id,order_no,address_id,order_status,total_price,order_date,order_comment  FROM T_ORDER_2
        //
        // WHERE (order_id = ? AND user_id = ?) ::: [1750753010159124481, 14]

        // 通过执行日志可以看到拆分后的 2 条 SQL 都可以通过分片键进行查询，
        // 这样能保证只需要在单个分片中完成查询操作。不论有多少个分片，
        // 也只需要查询 2个分片的信息，这样 SQL 的查询性能可以得到极大的提升。
    }

}
