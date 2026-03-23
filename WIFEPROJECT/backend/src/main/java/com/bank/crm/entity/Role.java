package com.bank.crm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色信息表实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Qydh_Role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("role_name")
    private String roleName;

    @TableField("permissions")
    private String permissions;

    @TableField("description")
    private String description;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}