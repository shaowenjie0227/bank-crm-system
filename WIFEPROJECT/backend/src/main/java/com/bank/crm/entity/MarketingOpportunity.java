package com.bank.crm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 营销机会信息表实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Qydh_MarketingOpportunity")
public class MarketingOpportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Qydh_ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Qydh_Name")
    private String name;

    @TableField("Qydh_Jhly")
    private String source;

    @TableField("Qydh_Jhzt")
    private String status;

    @TableField("Qydh_Jhsj")
    private String estimatedDate;

    @TableField("Qydh_Jhxz")
    private String estimatedAmount;

    @TableField("Qydh_Jhjj")
    private String description;

    @TableField("Qydh_Jhzq")
    private String stage;

    @TableField("Qydh_Jhcgjl")
    private String successRate;

    @TableField("Qydh_Tel")
    private String contactPhone;

    @TableField("Qydh_Gy")
    private String company;

    @TableField("Qydh_Fzr")
    private String manager;

    @TableField("Qydh_Jl")
    private String record;

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