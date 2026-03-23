<template>
  <div class="development-detail-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户开发详情</span>
          <div>
            <el-button @click="handleBack">返回</el-button>
            <el-button type="primary" @click="handleEdit">编辑</el-button>
          </div>
        </div>
      </template>
      
      <!-- 客户开发基本信息 -->
      <el-descriptions title="基本信息" :column="2" border>
        <el-descriptions-item label="客户名称">{{ development.name }}</el-descriptions-item>
        <el-descriptions-item label="开发周期">{{ development.developmentStage }}</el-descriptions-item>
        <el-descriptions-item label="开发状态">
          <el-tag :type="getStatusType(development.developmentStatus)">
            {{ development.developmentStatus }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开发时间">{{ formatDateTime(development.developmentTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(development.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="开发描述">{{ development.description }}</el-descriptions-item>
        <el-descriptions-item label="开发记录" :span="2">
          {{ development.successRecord }}
        </el-descriptions-item>
        <el-descriptions-item label="负责人">{{ development.manager }}</el-descriptions-item>
        <el-descriptions-item label="客户等级">{{ development.customerLevel }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ development.address }}</el-descriptions-item>
        <el-descriptions-item label="记录描述">{{ development.recordDescription }}</el-descriptions-item>
        <el-descriptions-item label="记录" v-if="development.record">{{ development.record }}</el-descriptions-item>
        <el-descriptions-item label="记录附件" v-if="development.recordAttachment">{{ development.recordAttachment }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getDevelopmentDetail } from '@/api/development'
import type { CustomerDevelopment } from '@/api/types'

const router = useRouter()
const route = useRoute()

// 客户开发数据
const development = reactive<CustomerDevelopment>({
  id: 0,
  name: '',
  developmentStage: '',
  developmentStatus: '',
  successRecord: '',
  createTime: '',
  updateTime: '',
  developmentTime: '',
  createdBy: 0,
  company: '',
  contactPerson: '',
  contactPhone: '',
  description: '',
  record: '',
  customerLevel: '',
  address: '',
  recordDescription: '',
  recordAttachment: ''
})

// 获取客户开发详情
const fetchDevelopmentDetail = async () => {
  try {
    const id = Number(route.params.id)
    if (isNaN(id)) {
      ElMessage.error('无效的客户开发ID')
      router.back()
      return
    }
    
    // 调用API获取真实数据
    const data = await getDevelopmentDetail(id)
    if (data) {
      Object.assign(development, data)
    } else {
      ElMessage.error('获取客户开发详情失败')
      router.back()
      return
    }
  } catch (error) {
    ElMessage.error('获取客户开发详情失败')
    router.back()
  }
}

// 返回列表
const handleBack = () => {
  router.back()
}

// 编辑客户开发
const handleEdit = () => {
  // 返回列表页面并打开编辑对话框
  router.push({
    path: '/development',
    query: { editId: String(development.id) }
  } as any)
}

// 格式化日期时间
const formatDateTime = (dateTime: string | undefined) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString()
}

// 获取状态标签类型
const getStatusType = (status: string | undefined) => {
  switch (status) {
    case '初步接触':
      return 'info'
    case '需求分析':
      return 'primary'
    case '方案提供':
      return 'warning'
    case '进行中':
      return 'warning'
    case '已完成':
      return 'success'
    case '已暂停':
      return 'info'
    case '已取消':
      return 'danger'
    case '待处理':
      return 'info'
    case '已关闭':
      return 'danger'
    default:
      return 'info'
  }
}

// 获取状态文本（保持与原有逻辑一致）
const getStatusText = (status: string | undefined) => {
  return status || ''
}

// 初始化
onMounted(() => {
  fetchDevelopmentDetail()
})
</script>

<style scoped>
.development-detail-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>