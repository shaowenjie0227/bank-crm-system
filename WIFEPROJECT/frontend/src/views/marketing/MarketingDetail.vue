<template>
  <div class="marketing-detail-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>营销机会详情</span>
          <div>
            <el-button @click="handleBack">返回</el-button>
            <el-button type="primary" @click="handleEdit">编辑</el-button>
          </div>
        </div>
      </template>
      
      <!-- 营销机会基本信息 -->
      <el-descriptions title="基本信息" :column="2" border>
        <el-descriptions-item label="机会名称">{{ marketing.name }}</el-descriptions-item>
        <el-descriptions-item label="机会来源">{{ marketing.source }}</el-descriptions-item>
        <el-descriptions-item label="机会状态">
          <el-tag :type="getStatusType(marketing.status)">
            {{ marketing.status }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ marketing.createTime }}</el-descriptions-item>
        <el-descriptions-item label="机会性质">{{ marketing.description }}</el-descriptions-item>
        <el-descriptions-item label="截止时间">{{ marketing.estimatedDate }}</el-descriptions-item>
        <el-descriptions-item label="机会描述" :span="2">{{ marketing.record }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getMarketingDetail } from '@/api/marketing'
import type { MarketingOpportunity } from '@/api/types'

const router = useRouter()
const route = useRoute()

// 营销机会数据
const marketing = reactive<MarketingOpportunity>({
  id: 0,
  name: '',
  source: '',
  status: '',
  createTime: '',
  description: '',
  estimatedDate: '',
  record: ''
})

// 获取营销机会详情
const fetchMarketingDetail = async () => {
  try {
    const id = Number(route.params.id)
    if (isNaN(id)) {
      ElMessage.error('无效的营销机会ID')
      router.back()
      return
    }
    
    const data = await getMarketingDetail(id)
    Object.assign(marketing, data)
  } catch (error) {
    ElMessage.error('获取营销机会详情失败')
    router.back()
  }
}

// 返回列表
const handleBack = () => {
  router.back()
}

// 编辑营销机会
const handleEdit = () => {
  router.push(`/marketing/${marketing.id}/edit`)
}

// 获取状态标签类型
const getStatusType = (status: string | undefined) => {
  switch (status) {
    case '待处理':
      return 'info'
    case '进行中':
      return 'warning'
    case '已完成':
      return 'success'
    case '已关闭':
      return 'danger'
    default:
      return 'info'
  }
}

// 初始化
onMounted(() => {
  fetchMarketingDetail()
})
</script>

<style scoped>
.marketing-detail-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>