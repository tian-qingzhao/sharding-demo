-- 创建数据库的脚本

CREATE
DATABASE order_0;
CREATE
DATABASE order_1;

-- 2、在每个库下分别执行如下语句创建数据表

CREATE TABLE `T_ORDER_0`
(
    `ORDER_ID`      bigint                                                        NOT NULL COMMENT '订单ID',
    `ORDER_NO`      VARCHAR(36)                                                   NOT NULL COMMENT '订单编号',
    `USER_ID`       bigint                                                        NOT NULL COMMENT '用户ID',
    `ADDRESS_ID`    bigint                                                        NOT NULL COMMENT '地址ID',
    `ORDER_STATUS`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NOT NULL COMMENT '订单状态',
    `TOTAL_PRICE`   decimal(15, 2)                                                NOT NULL COMMENT '总价格',
    `ORDER_DATE`    datetime                                                      NOT NULL COMMENT '订单时间',
    `ORDER_COMMENT` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单说明',
    PRIMARY KEY (`ORDER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `T_ORDER_1`
(
    `ORDER_ID`      bigint                                                        NOT NULL COMMENT '订单ID',
    `ORDER_NO`      VARCHAR(36)                                                   NOT NULL COMMENT '订单编号',
    `USER_ID`       bigint                                                        NOT NULL COMMENT '用户ID',
    `ADDRESS_ID`    bigint                                                        NOT NULL COMMENT '地址ID',
    `ORDER_STATUS`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NOT NULL COMMENT '订单状态',
    `TOTAL_PRICE`   decimal(15, 2)                                                NOT NULL COMMENT '总价格',
    `ORDER_DATE`    datetime                                                      NOT NULL COMMENT '订单时间',
    `ORDER_COMMENT` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单说明',
    PRIMARY KEY (`ORDER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `T_ORDER_2`
(
    `ORDER_ID`      bigint                                                        NOT NULL COMMENT '订单ID',
    `ORDER_NO`      VARCHAR(36)                                                   NOT NULL COMMENT '订单编号',
    `USER_ID`       bigint                                                        NOT NULL COMMENT '用户ID',
    `ADDRESS_ID`    bigint                                                        NOT NULL COMMENT '地址ID',
    `ORDER_STATUS`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NOT NULL COMMENT '订单状态',
    `TOTAL_PRICE`   decimal(15, 2)                                                NOT NULL COMMENT '总价格',
    `ORDER_DATE`    datetime                                                      NOT NULL COMMENT '订单时间',
    `ORDER_COMMENT` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单说明',
    PRIMARY KEY (`ORDER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `T_ORDER_3`
(
    `ORDER_ID`      bigint                                                        NOT NULL COMMENT '订单ID',
    `ORDER_NO`      VARCHAR(36)                                                   NOT NULL COMMENT '订单编号',
    `USER_ID`       bigint                                                        NOT NULL COMMENT '用户ID',
    `ADDRESS_ID`    bigint                                                        NOT NULL COMMENT '地址ID',
    `ORDER_STATUS`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NOT NULL COMMENT '订单状态',
    `TOTAL_PRICE`   decimal(15, 2)                                                NOT NULL COMMENT '总价格',
    `ORDER_DATE`    datetime                                                      NOT NULL COMMENT '订单时间',
    `ORDER_COMMENT` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单说明',
    PRIMARY KEY (`ORDER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 在ShardingSphere加载的两个数据源中分别插入undo_log表

CREATE TABLE `undo_log`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20) NOT NULL,
    `xid`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `context`       varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `rollback_info` longblob                                                NOT NULL,
    `log_status`    int(11) NOT NULL,
    `log_created`   datetime(0) NOT NULL,
    `log_modified`  datetime(0) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
)