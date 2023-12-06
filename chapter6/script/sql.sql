-- 在order_0和order_1库下分别粗昂见 T_DICT 表
USE
    order_0;

USE
    order_1;

CREATE TABLE `T_DICT`
(
    `DICT_ID`    bigint                                                        NOT NULL,
    `DICT_TYPE`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `DICT_CODE`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `DICT_VALUE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`DICT_ID`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

INSERT INTO `T_DICT`
VALUES (1, 'ORDER_STATUS', '1', '待付款');
INSERT INTO `T_DICT`
VALUES (2, 'ORDER_STATUS', '2', '待发货');
INSERT INTO `T_DICT`
VALUES (3, 'ORDER_STATUS', '3', '待签收');