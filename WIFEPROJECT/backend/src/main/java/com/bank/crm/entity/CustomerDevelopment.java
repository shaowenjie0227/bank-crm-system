package com.bank.crm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户开发计划表实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Qydh_CustomerDevelopment")
public class CustomerDevelopment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Qydh_ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Qydh_Name")
    private String name;

    @TableField("Qykh_Jhzq")
    private String developmentStage;

    @TableField("Qykh_Jhzt")
    private String developmentStatus;

    @TableField("Qykh_Jhsj")
    private String developmentTime;

    @TableField("Qykh_Jhxz")
    private String estimatedAmount;

    @TableField("Qykh_Jhjj")
    private String description;

    @TableField("Qykh_Jhcgjl")
    private String successRecord;

    @TableField("Qydh_Tel")
    private String contactPhone;

    @TableField("Qydh_Gy")
    private String company;

    @TableField("Qydh_Fzr")
    private String manager;

    @TableField("Qydh_Jl")
    private String successRate;

    @TableField("Qydh_Khdj")
    private String customerLevel;

    @TableField("Qydh_Address")
    private String address;

    @TableField("Qydh_Jlms")
    private String recordDescription;

    @TableField("Qydh_Jlzp")
    private String recordAttachment;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("created_by")
    private Integer createdBy;
}