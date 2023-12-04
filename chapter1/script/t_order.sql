-- 订单表
DROP TABLE IF EXISTS `T_ORDER`;
CREATE TABLE `T_ORDER`
(
    `ORDER_ID`      bigint                                                        NOT NULL COMMENT '订单ID',
    `USER_ID`       bigint                                                        NOT NULL COMMENT '用户ID',
    `ADDRESS_ID`    bigint                                                        NOT NULL COMMENT '地址ID',
    `ORDER_STATUS`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NOT NULL COMMENT '订单状态',
    `TOTAL_PRICE`   decimal(15, 2)                                                NOT NULL COMMENT '总价格',
    `ORDER_DATE`    datetime                                                      NOT NULL COMMENT '订单时间',
    `ORDER_COMMENT` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单说明',
    PRIMARY KEY (`ORDER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;