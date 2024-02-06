package com.tqz.sharding.chapter12.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author <a href="https://github.com/tian-qingzhao">tianqingzhao</a>
 * @since 2024/1/26 10:39
 */
@Data
@TableName("idx_orderid_userid")
@Builder
@EqualsAndHashCode
public class OrderUserRelation {

    private Long orderId;

    private Long userId;
}
