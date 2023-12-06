-- 创建数据库的脚本

CREATE
DATABASE monitor_0;
CREATE
DATABASE monitor_1;

-- 每个库各自创建一张表
CREATE TABLE monitor_0.t_event_20231205
(
    event_id   bigint       not null comment '事件id',
    machine_no varchar(50)  not null comment '机器编号',
    event_date datetime     NOT NULL COMMENT '订单时间',
    message    varchar(500) not null comment '消息',
    day_value  varchar(50)  not null comment '上报日期',
    PRIMARY KEY (`event_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE monitor_1.t_event_20231205
(
    event_id   bigint       not null comment '事件id',
    machine_no varchar(50)  not null comment '机器编号',
    event_date datetime     NOT NULL COMMENT '订单时间',
    message    varchar(500) not null comment '消息',
    day_value  varchar(50)  not null comment '上报日期',
    PRIMARY KEY (`event_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;