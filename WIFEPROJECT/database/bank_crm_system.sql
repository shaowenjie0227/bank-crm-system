/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.31.222_3306
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : 192.168.31.222:3306
 Source Schema         : bank_crm_system

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 20/03/2026 03:30:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qydh_customerchurn
-- ----------------------------
DROP TABLE IF EXISTS `qydh_customerchurn`;
CREATE TABLE `qydh_customerchurn`  (
  `Qydh_ID` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `Qydh_Name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户名称',
  `Qydh_Khjljl` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户经理',
  `Qydh_Xddtime` date NULL DEFAULT NULL COMMENT '下单时间',
  `Qydh_Qtime` date NULL DEFAULT NULL COMMENT '确认时间',
  `Qyls_Yjjb` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预警级别',
  `Qyls_Yjzt` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预警状态',
  `Qyls_Lssj` date NULL DEFAULT NULL COMMENT '流失时间',
  `Qyls_Lsyy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流失原因',
  `Qyls_Clcs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理措施',
  `Qyls_Clxg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理效果',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Qydh_ID`) USING BTREE,
  INDEX `idx_churn_name`(`Qydh_Name`) USING BTREE,
  INDEX `idx_churn_manager`(`Qydh_Khjljl`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户流失表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qydh_customerchurn
-- ----------------------------
INSERT INTO `qydh_customerchurn` VALUES (1, '杭州远景贸易有限公司', 'customer_manager', '2025-12-15', '2026-02-20', '中', '预警', '2026-03-01', '价格敏感，竞争对手报价更低', '已提供专项优惠方案并安排回访', '客户暂未流失，继续跟进', '2026-03-20 01:18:31', '2026-03-20 01:18:31');
INSERT INTO `qydh_customerchurn` VALUES (2, '旧城制造厂', 'sales_manager', '2025-10-09', '2026-01-10', '高', '已流失', '2026-01-18', '服务响应不及时', '完成问题复盘并建立响应SLA', '作为内部案例归档', '2026-03-20 01:18:31', '2026-03-20 01:18:31');

-- ----------------------------
-- Table structure for qydh_customerdevelopment
-- ----------------------------
DROP TABLE IF EXISTS `qydh_customerdevelopment`;
CREATE TABLE `qydh_customerdevelopment`  (
  `Qydh_ID` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `Qydh_Name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户名称',
  `Qykh_Jhzq` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开发阶段',
  `Qykh_Jhzt` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开发状态',
  `Qykh_Jhsj` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开发时间',
  `Qykh_Jhxz` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预计金额/开发性质',
  `Qykh_Jhjj` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开发描述',
  `Qykh_Jhcgjl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '成功记录',
  `Qydh_Tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `Qydh_Gy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司/概要',
  `Qydh_Fzr` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `Qydh_Jl` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '成功率',
  `Qydh_Khdj` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户等级',
  `Qydh_Address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `Qydh_Jlms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '记录描述',
  `Qydh_Jlzp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '记录附件',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created_by` int(0) NULL DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`Qydh_ID`) USING BTREE,
  INDEX `idx_development_name`(`Qydh_Name`) USING BTREE,
  INDEX `idx_development_manager`(`Qydh_Fzr`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户开发表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qydh_customerdevelopment
-- ----------------------------
INSERT INTO `qydh_customerdevelopment` VALUES (1, '上海星河科技有限公司', '需求分析', '推进中', '2026-03-21', '300万', '进入产品匹配阶段', '已完成首次需求梳理', '13800138001', '科技企业客户', 'sales_manager', '45%', 'VIP', '上海浦东新区', '计划安排第二次方案沟通', 'development-001.docx', '2026-03-20 01:18:31', '2026-03-20 01:18:31', 1);
INSERT INTO `qydh_customerdevelopment` VALUES (2, '王晓丽', '方案确认', '待签约', '2026-03-22', '500万', '个人资产配置开发', '客户认可方案方向', '13800138003', '高净值个人客户', 'sales_manager', '75%', 'SVIP', '南京建邺区', '待确认最终签约时间', 'development-002.docx', '2026-03-20 01:18:31', '2026-03-20 01:18:31', 1);

-- ----------------------------
-- Table structure for qydh_customerinfo
-- ----------------------------
DROP TABLE IF EXISTS `qydh_customerinfo`;
CREATE TABLE `qydh_customerinfo`  (
  `Qydh_ID` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `Qydh_KhID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户编号',
  `Qydh_Khname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户名称',
  `Qydh_Diqu` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区',
  `Qydh_Khjl` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户经理',
  `Qydh_Khdj` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户等级',
  `qydhKhlx` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户类型',
  `qydhKhhy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户行业',
  `qydhKhyh` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户银行',
  `qydhKhjj` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户简介',
  `qydhKhjljl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经理记录',
  `qydhKhzt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户状态',
  `contact_person` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Qydh_ID`) USING BTREE,
  UNIQUE INDEX `uk_customer_code`(`Qydh_KhID`) USING BTREE,
  INDEX `idx_customer_name`(`Qydh_Khname`) USING BTREE,
  INDEX `idx_customer_manager`(`Qydh_Khjl`) USING BTREE,
  INDEX `idx_customer_level`(`Qydh_Khdj`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qydh_customerinfo
-- ----------------------------
INSERT INTO `qydh_customerinfo` VALUES (1, 'KH2026001', '上海星河科技有限公司', '上海', 'sales_manager', 'VIP', '企业客户', '科技', '招商银行', '重点维护企业客户', '首次拜访完成，需求明确', '正常', '张经理', '13800138001', 'startech@example.com', '2026-03-20 01:18:31', '2026-03-20 01:18:31');
INSERT INTO `qydh_customerinfo` VALUES (2, 'KH2026002', '杭州远景贸易有限公司', '杭州', 'customer_manager', '普通', '企业客户', '贸易', '工商银行', '存量客户，持续跟进', '续约沟通中', '预警中', '李主管', '13800138002', 'visiontrade@example.com', '2026-03-20 01:18:31', '2026-03-20 01:18:31');
INSERT INTO `qydh_customerinfo` VALUES (3, 'KH2026003', '王晓丽', '南京', 'sales_manager', 'SVIP', '个人客户', '高净值个人', '建设银行', '高价值个人客户', '已完成资产配置建议', '正常', '王晓丽', '13800138003', 'wangxiaoli@example.com', '2026-03-20 01:18:31', '2026-03-20 01:18:31');
INSERT INTO `qydh_customerinfo` VALUES (4, 'KH2026004', '苏州智远制造集团', '苏州', 'sales_manager', 'VIP', '企业客户', '制造', '中国银行', '制造业重点授信客户', '已完成首轮经营情况访谈', '正常', '周总', '13800138004', 'zhiyuan@example.com', '2026-03-20 02:59:36', '2026-03-20 02:59:36');
INSERT INTO `qydh_customerinfo` VALUES (5, 'KH2026005', '宁波海岳供应链有限公司', '宁波', 'customer_manager', '普通', '企业客户', '物流供应链', '浦发银行', '供应链金融潜力客户', '客户已有结算意向，等待授信测算', '正常', '陈经理', '13800138005', 'haiyue@example.com', '2026-03-20 02:59:36', '2026-03-20 02:59:36');
INSERT INTO `qydh_customerinfo` VALUES (6, 'KH2026006', '深圳云澜家族办公室', '深圳', 'sales_manager', 'SVIP', '个人客户', '财富管理', '平安银行', '高净值家族资产管理客户', '已沟通家族信托与保险金信托方向', '正常', '林女士', '13800138006', 'yunlan@example.com', '2026-03-20 02:59:36', '2026-03-20 02:59:36');

-- ----------------------------
-- Table structure for qydh_marketingopportunity
-- ----------------------------
DROP TABLE IF EXISTS `qydh_marketingopportunity`;
CREATE TABLE `qydh_marketingopportunity`  (
  `Qydh_ID` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `Qydh_Name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户名称',
  `Qydh_Jhly` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机会来源',
  `Qydh_Jhzt` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机会状态',
  `Qydh_Jhsj` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预计时间',
  `Qydh_Jhxz` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预计金额/机会性质',
  `Qydh_Jhjj` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机会描述',
  `Qydh_Jhzq` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机会阶段',
  `Qydh_Jhcgjl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '成功率/成功记录',
  `Qydh_Tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `Qydh_Gy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司/概要',
  `Qydh_Fzr` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `Qydh_Jl` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '记录',
  `Qydh_Khdj` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户等级',
  `Qydh_Address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `Qydh_Jlms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '记录描述',
  `Qydh_Jlzp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '记录附件',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created_by` int(0) NULL DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`Qydh_ID`) USING BTREE,
  INDEX `idx_marketing_name`(`Qydh_Name`) USING BTREE,
  INDEX `idx_marketing_manager`(`Qydh_Fzr`) USING BTREE,
  INDEX `idx_marketing_level`(`Qydh_Khdj`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '营销机会表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qydh_marketingopportunity
-- ----------------------------
INSERT INTO `qydh_marketingopportunity` VALUES (1, '上海星河科技有限公司', '官网留资', '跟进中', '2026-03-25', '300万', '企业授信与结算一体化机会', '初步接触', '40%', '13800138001', '科技企业客户', 'sales_manager', '首次接触11', 'VIP', '上海浦东新区', '客户对综合授信有兴趣', 'opportunity-001.pdf', '2026-03-20 01:18:31', '2026-03-20 01:18:31', 1);
INSERT INTO `qydh_marketingopportunity` VALUES (2, '杭州远景贸易有限公司', '客户转介绍', '已立项', '2026-03-28', '150万', '供应链金融合作机会', '方案沟通', '65%', '13800138002', '贸易企业客户', 'customer_manager', '二次沟通', '普通', '杭州滨江区', '已提交初步方案', 'opportunity-002.pdf', '2026-03-20 01:18:31', '2026-03-20 01:18:31', 1);
INSERT INTO `qydh_marketingopportunity` VALUES (3, '苏州智远制造集团', '产业园拜访', '已立项', '2026-04-05', '420万', '智能制造设备授信与现金管理机会', '方案评估', '68%', '13800138004', '制造业企业客户', 'sales_manager', '跟进中', 'VIP', '苏州工业园区', '客户计划同步推进授信与代发', 'opportunity-004.pdf', '2026-03-20 02:59:36', '2026-03-20 02:59:36', 2);
INSERT INTO `qydh_marketingopportunity` VALUES (4, '宁波海岳供应链有限公司', '老客户转介绍', '跟进中', '2026-04-08', '260万', '供应链保理与票据池合作机会', '需求确认', '61%', '13800138005', '供应链企业客户', 'customer_manager', '重点沟通', '普通', '宁波北仑区', '客户对保理额度和结算效率较关注', 'opportunity-005.pdf', '2026-03-20 02:59:36', '2026-03-20 02:59:36', 3);
INSERT INTO `qydh_marketingopportunity` VALUES (5, '深圳云澜家族办公室', '私人银行沙龙', '方案确认', '2026-04-12', '800万', '家族信托与高端保障配置机会', '深度洽谈', '82%', '13800138006', '高净值个人客户', 'sales_manager', '高优先级', 'SVIP', '深圳南山区', '客户已预约下一轮家族架构讨论', 'opportunity-006.pdf', '2026-03-20 02:59:36', '2026-03-20 02:59:36', 2);

-- ----------------------------
-- Table structure for qydh_recommendationinteraction
-- ----------------------------
DROP TABLE IF EXISTS `qydh_recommendationinteraction`;
CREATE TABLE `qydh_recommendationinteraction`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `customer_id` int(0) NOT NULL COMMENT '客户ID',
  `interaction_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '行为类型',
  `score` decimal(6, 2) NOT NULL COMMENT '行为评分',
  `source_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源模块',
  `notes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '行为说明',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_recommend_user`(`user_id`) USING BTREE,
  INDEX `idx_recommend_customer`(`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '推荐行为表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qydh_recommendationinteraction
-- ----------------------------
INSERT INTO `qydh_recommendationinteraction` VALUES (8, 1, 1, 'VIEW', 3.50, 'dashboard', '管理员查看重点科技企业客户档案', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (9, 1, 2, 'FOLLOW_UP', 4.20, 'marketing', '管理员参与贸易客户方案评审', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (10, 1, 3, 'VIEW', 3.00, 'customer', '管理员浏览高净值客户资料', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (11, 1, 4, 'ANALYZE', 4.40, 'dashboard', '管理员评估制造业重点客户潜力', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (12, 2, 1, 'FOLLOW_UP', 5.00, 'marketing', '销售经理持续推进企业授信机会', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (13, 2, 3, 'DEVELOP', 4.60, 'development', '销售经理重点跟进高净值客户开发', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (14, 2, 4, 'FOLLOW_UP', 4.80, 'marketing', '销售经理推进制造集团综合授信', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (15, 2, 5, 'FOLLOW_UP', 4.70, 'marketing', '销售经理评估供应链金融客户合作意向', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (16, 3, 1, 'VIEW', 4.10, 'customer', '客服查看重点企业客户服务记录', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (17, 3, 2, 'FOLLOW_UP', 5.00, 'marketing', '客服持续跟进贸易客户续约方案', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (18, 3, 4, 'VIEW', 4.20, 'customer', '客服跟踪制造业客户服务反馈', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (19, 3, 6, 'FOLLOW_UP', 4.90, 'marketing', '客服跟进家族办公室高端保障需求', '2026-03-20 02:59:37', '2026-03-20 02:59:37');
INSERT INTO `qydh_recommendationinteraction` VALUES (20, 5, 1, 'VIEW', 4.30, 'customer', '李销售查看科技企业客户资料', '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_recommendationinteraction` VALUES (21, 5, 4, 'FOLLOW_UP', 4.90, 'marketing', '李销售重点跟进制造集团授信机会', '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_recommendationinteraction` VALUES (22, 5, 5, 'FOLLOW_UP', 4.70, 'marketing', '李销售评估供应链客户合作潜力', '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_recommendationinteraction` VALUES (23, 6, 2, 'VIEW', 4.10, 'dashboard', '王销售查看贸易客户经营情况', '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_recommendationinteraction` VALUES (24, 6, 5, 'FOLLOW_UP', 4.80, 'marketing', '王销售重点跟进供应链客户方案', '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_recommendationinteraction` VALUES (25, 6, 6, 'DEVELOP', 5.00, 'development', '王销售推进家族办公室高端配置方案', '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_recommendationinteraction` VALUES (26, 7, 3, 'VIEW', 4.00, 'customer', '陈销售查看高净值客户基础画像', '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_recommendationinteraction` VALUES (27, 7, 4, 'FOLLOW_UP', 4.60, 'marketing', '陈销售跟进制造企业现金管理需求', '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_recommendationinteraction` VALUES (28, 7, 6, 'FOLLOW_UP', 4.95, 'marketing', '陈销售重点推进家族办公室信托配置', '2026-03-20 03:03:15', '2026-03-20 03:03:15');

-- ----------------------------
-- Table structure for qydh_recommendationresult
-- ----------------------------
DROP TABLE IF EXISTS `qydh_recommendationresult`;
CREATE TABLE `qydh_recommendationresult`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `customer_id` int(0) NOT NULL COMMENT '推荐客户ID',
  `recommended_opportunity_id` int(0) NULL DEFAULT NULL COMMENT '推荐机会ID',
  `score` decimal(6, 2) NOT NULL COMMENT '推荐分数',
  `reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '推荐原因',
  `algorithm_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '算法版本',
  `generated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '生成时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_result_user`(`user_id`) USING BTREE,
  INDEX `idx_result_customer`(`customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '推荐结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qydh_recommendationresult
-- ----------------------------
INSERT INTO `qydh_recommendationresult` VALUES (37, 3, 5, 4, 5.33, '与你行为相似的客户经理 sales_manager、sales_li、sales_wang 近期重点关注了该客户，并且已有“需求确认”阶段机会可继续推进', 'user-cf-v1', '2026-03-20 03:21:28', '2026-03-20 03:21:28');
INSERT INTO `qydh_recommendationresult` VALUES (38, 3, 3, NULL, 4.77, '与你行为相似的客户经理 admin、sales_manager、sales_chen 近期重点关注了该客户', 'user-cf-v1', '2026-03-20 03:21:28', '2026-03-20 03:21:28');
INSERT INTO `qydh_recommendationresult` VALUES (43, 2, 6, 5, 6.20, '与你行为相似的客户经理 customer_manager、sales_wang、sales_chen 近期重点关注了该客户，并且已有“深度洽谈”阶段机会可继续推进', 'user-cf-v1', '2026-03-20 03:22:37', '2026-03-20 03:22:37');
INSERT INTO `qydh_recommendationresult` VALUES (44, 2, 2, 2, 5.03, '与你行为相似的客户经理 admin、customer_manager、sales_wang 近期重点关注了该客户，并且已有“方案沟通”阶段机会可继续推进', 'user-cf-v1', '2026-03-20 03:22:37', '2026-03-20 03:22:37');
INSERT INTO `qydh_recommendationresult` VALUES (47, 1, 6, 5, 6.20, '与你行为相似的客户经理 customer_manager、sales_wang、sales_chen 近期重点关注了该客户，并且已有“深度洽谈”阶段机会可继续推进', 'user-cf-v1', '2026-03-20 03:25:08', '2026-03-20 03:25:08');
INSERT INTO `qydh_recommendationresult` VALUES (48, 1, 5, 4, 5.33, '与你行为相似的客户经理 sales_manager、sales_li、sales_wang 近期重点关注了该客户，并且已有“需求确认”阶段机会可继续推进', 'user-cf-v1', '2026-03-20 03:25:08', '2026-03-20 03:25:08');

-- ----------------------------
-- Table structure for qydh_role
-- ----------------------------
DROP TABLE IF EXISTS `qydh_role`;
CREATE TABLE `qydh_role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `permissions` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限列表',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_qydh_role_name`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qydh_role
-- ----------------------------
INSERT INTO `qydh_role` VALUES (1, '管理员', 'user:manage,role:manage,customer:view,customer:edit,customer:delete,marketing:view,marketing:edit,marketing:delete,development:view,development:edit,development:delete,profile:view,profile:edit', '系统管理员', '2026-03-20 01:18:31');
INSERT INTO `qydh_role` VALUES (2, '销售', 'customer:view,customer:edit,marketing:view,marketing:edit,development:view,development:edit,profile:view,profile:edit', '销售人员', '2026-03-20 01:18:31');
INSERT INTO `qydh_role` VALUES (3, '客服', 'customer:view,customer:edit,profile:view,profile:edit', '客服人员', '2026-03-20 01:18:31');

-- ----------------------------
-- Table structure for qydh_user
-- ----------------------------
DROP TABLE IF EXISTS `qydh_user`;
CREATE TABLE `qydh_user`  (
  `Qydh_ID` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `Qydh_Name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `Qydh_Js` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `Qydh_Pw` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'BCrypt密码',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '盐值',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '状态:1启用,0禁用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`Qydh_ID`) USING BTREE,
  UNIQUE INDEX `uk_qydh_user_name`(`Qydh_Name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qydh_user
-- ----------------------------
INSERT INTO `qydh_user` VALUES (1, 'admin', '管理员', '$2a$10$mfaAHJrxwoAnQXjyizjnLO0A92odo5PbeQxZsdATMPT0mTl9ipQdu', 'admin_salt', 1, '2026-03-20 01:18:31', '2026-03-20 01:23:45');
INSERT INTO `qydh_user` VALUES (2, 'sales_manager', '销售', '$2a$10$mfaAHJrxwoAnQXjyizjnLO0A92odo5PbeQxZsdATMPT0mTl9ipQdu', 'sales_salt', 1, '2026-03-20 01:18:31', '2026-03-20 02:17:31');
INSERT INTO `qydh_user` VALUES (3, 'customer_manager', '客服', '$2a$10$mfaAHJrxwoAnQXjyizjnLO0A92odo5PbeQxZsdATMPT0mTl9ipQdu', 'customer_salt', 1, '2026-03-20 01:18:31', '2026-03-20 02:17:34');
INSERT INTO `qydh_user` VALUES (4, 'zyx', '管理员', '69cdc8eccbc9716e2f95b3b59c953b77', '137558a4767c383f55cf3d80c346b8df', 1, '2026-03-20 01:54:26', '2026-03-20 01:54:26');
INSERT INTO `qydh_user` VALUES (5, 'sales_li', '销售', '$2a$10$mfaAHJrxwoAnQXjyizjnLO0A92odo5PbeQxZsdATMPT0mTl9ipQdu', 'sales_li_salt', 1, '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_user` VALUES (6, 'sales_wang', '销售', '$2a$10$mfaAHJrxwoAnQXjyizjnLO0A92odo5PbeQxZsdATMPT0mTl9ipQdu', 'sales_wang_salt', 1, '2026-03-20 03:03:15', '2026-03-20 03:03:15');
INSERT INTO `qydh_user` VALUES (7, 'sales_chen', '销售', '$2a$10$mfaAHJrxwoAnQXjyizjnLO0A92odo5PbeQxZsdATMPT0mTl9ipQdu', 'sales_chen_salt', 1, '2026-03-20 03:03:15', '2026-03-20 03:03:15');

SET FOREIGN_KEY_CHECKS = 1;
