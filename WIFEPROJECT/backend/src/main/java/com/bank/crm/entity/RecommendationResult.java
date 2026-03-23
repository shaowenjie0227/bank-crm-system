package com.bank.crm.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Qydh_RecommendationResult")
public class RecommendationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("customer_id")
    private Integer customerId;

    @TableField("recommended_opportunity_id")
    private Integer recommendedOpportunityId;

    @TableField("score")
    private Double score;

    @TableField("reason")
    private String reason;

    @TableField("algorithm_version")
    private String algorithmVersion;

    @TableField("generated_at")
    private LocalDateTime generatedAt;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
