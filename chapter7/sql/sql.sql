-- 继续使用 chapter4 和 chapter6 模块的数据，在此基础上在每个库新建4张订单明细表

use
    order_0;
USE
    order_1;

CREATE TABLE `T_ORDER_ITEM_0`
(
    `ORDER_ITEM_ID` bigint                                                        NOT NULL,
    `ORDER_ID`      bigint                                                        NOT NULL COMMENT '订单ID',
    `USER_ID`       bigint                                                        NOT NULL COMMENT '用户ID',
    `ITEM_PRICE`    decimal(15, 2)                                                NOT NULL COMMENT '商品价格',
    `ITEM_NAME`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
    PRIMARY KEY (`ORDER_ITEM_ID`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `T_ORDER_ITEM_1`
(
    `ORDER_ITEM_ID` bigint                                                        NOT NULL,
    `ORDER_ID`      bigint                                                        NOT NULL COMMENT '订单ID',
    `USER_ID`       bigint                                                        NOT NULL COMMENT '用户ID',
    `ITEM_PRICE`    decimal(15, 2)                                                NOT NULL COMMENT '商品价格',
    `ITEM_NAME`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
    PRIMARY KEY (`ORDER_ITEM_ID`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `T_ORDER_ITEM_2`
(
    `ORDER_ITEM_ID` bigint                                                        NOT NULL,
    `ORDER_ID`      bigint                                                        NOT NULL COMMENT '订单ID',
    `USER_ID`       bigint                                                        NOT NULL COMMENT '用户ID',
    `ITEM_PRICE`    decimal(15, 2)                                                NOT NULL COMMENT '商品价格',
    `ITEM_NAME`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
    PRIMARY KEY (`ORDER_ITEM_ID`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `T_ORDER_ITEM_3`
(
    `ORDER_ITEM_ID` bigint                                                        NOT NULL,
    `ORDER_ID`      bigint                                                        NOT NULL COMMENT '订单ID',
    `USER_ID`       bigint                                                        NOT NULL COMMENT '用户ID',
    `ITEM_PRICE`    decimal(15, 2)                                                NOT NULL COMMENT '商品价格',
    `ITEM_NAME`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
    PRIMARY KEY (`ORDER_ITEM_ID`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;