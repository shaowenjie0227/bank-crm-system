package com.bank.crm.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充处理器
 * 用于自动填充实体类中的创建时间和更新时间字段
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时自动填充创建时间和更新时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        
        // 为 CustomerDevelopment 实体填充 Qydh_Time 字段
        if (metaObject.hasGetter("qydhTime")) {
            this.strictInsertFill(metaObject, "qydhTime", LocalDateTime.class, LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}