package com.tqz.sharding.chapter12.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类
 *
 * @author tianqingzhao
 * @since 2023/12/1 11:37
 */
@Data
@TableName("T_ORDER")
@Builder
public class Order {

    @TableId(type = IdType.INPUT)
    private Long orderId;

    private Long userId;

    private String orderNo;

    private Long addressId;

    private String orderStatus;

    private BigDecimal totalPrice;

    private Date orderDate;

    private String orderComment;
}