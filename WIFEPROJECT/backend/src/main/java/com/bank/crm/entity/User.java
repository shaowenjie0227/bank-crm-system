package com.bank.crm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息表实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Qydh_User")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Qydh_ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Qydh_Name")
    private String username;

    @TableField("Qydh_Js")
    private String role;

    @TableField("Qydh_Pw")
    private String password;

    @TableField("salt")
    private String salt;

    @TableField("status")
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String token;
}