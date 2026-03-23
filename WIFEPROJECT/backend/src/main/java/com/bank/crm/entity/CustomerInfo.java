package com.bank.crm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户信息表实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Qydh_CustomerInfo")
public class CustomerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Qydh_ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Qydh_KhID")
    private String customerCode;

    @TableField("Qydh_Khname")
    private String customerName;

    @TableField("Qydh_Diqu")
    private String region;

    @TableField("Qydh_Khjl")
    private String customerManager;

    @TableField("Qydh_Khdj")
    private String customerLevel;

    @TableField("qydhKhlx")
    private String customerType;

    @TableField("qydhKhhy")
    private String customerIndustry;

    @TableField("qydhKhyh")
    private String customerBank;

    @TableField("qydhKhjj")
    private String customerBrief;

    @TableField("qydhKhjljl")
    private String managerRecord;

    @TableField("qydhKhzt")
    private String customerStatus;

    @TableField("contact_person")
    private String contactPerson;

    @TableField("contact_phone")
    private String contactPhone;

    @TableField("email")
    private String email;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}