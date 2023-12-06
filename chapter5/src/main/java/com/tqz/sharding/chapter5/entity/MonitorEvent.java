package com.tqz.sharding.chapter5.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@TableName("T_EVENT")
public class MonitorEvent {

    @TableId(type = IdType.ASSIGN_ID)
    private Long eventId;

    /**
     * 机器编号
     **/
    private String machineNo;

    /**
     * 监控上报时间
     **/
    private Date eventDate;

    /**
     * 监控上报消息
     **/
    private String message;

    /**
     * 监控上报日期
     **/
    private String dayValue;
}