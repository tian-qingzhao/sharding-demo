package com.tqz.sharding.chapter6.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author tianqingzhao
 * @since 2023/12/6 14:43
 */
@Data
@Builder
@TableName("t_dict")
public class Dict {

    @TableId(type = IdType.ASSIGN_ID)
    private Long dictId;

    private String dictType;

    private String dictCode;

    private String dictValue;
}
