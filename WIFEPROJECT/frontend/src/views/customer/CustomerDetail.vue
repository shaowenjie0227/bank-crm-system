<template>
  <div class="customer-detail-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户详情</span>
          <div>
            <el-button @click="$router.go(-1)">返回</el-button>
            <el-button v-if="!isEditing" type="primary" @click="handleEdit">编辑</el-button>
            <el-button v-else type="success" @click="handleSave" :loading="saveLoading">保存</el-button>
            <el-button v-if="isEditing" @click="handleCancel">取消</el-button>
          </div>
        </div>
      </template>
      
      <div v-if="customerInfo" class="customer-info">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="客户ID">
            {{ customerInfo.customerCode }}
          </el-descriptions-item>
          <el-descriptions-item label="客户名称">
            {{ customerInfo.customerName }}
          </el-descriptions-item>
          <el-descriptions-item label="客户类型">
            <el-select v-if="isEditing" v-model="customerInfo.customerType" placeholder="请选择客户类型" style="width: 100%">
              <el-option label="个人客户" value="个人客户" />
              <el-option label="企业客户" value="企业客户" />
            </el-select>
            <span v-else>{{ customerInfo.customerType || '' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="客户级别">
            <el-tag :type="getLevelType(customerInfo.customerLevel || '')">
              {{ customerInfo.customerLevel }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="客户行业">
            <el-input v-if="isEditing" v-model="customerInfo.customerIndustry" placeholder="请输入客户行业" />
            <span v-else>{{ customerInfo.customerIndustry || '' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="客户地址">
            {{ customerInfo.region || '' }}
          </el-descriptions-item>
          <el-descriptions-item label="邮箱">
            {{ customerInfo.email || '' }}
          </el-descriptions-item>
          <el-descriptions-item label="电话">
            {{ customerInfo.contactPhone || '' }}
          </el-descriptions-item>
          <el-descriptions-item label="客户经理">
            {{ customerInfo.customerManager || '' }}
          </el-descriptions-item>
          <el-descriptions-item label="客户银行">
            <el-input v-if="isEditing" v-model="customerInfo.customerBank" placeholder="请输入客户银行" />
            <span v-else>{{ customerInfo.customerBank || '' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ customerInfo.createTime || '' }}
          </el-descriptions-item>
          <el-descriptions-item label="客户简介" :span="2">
            <el-input 
              v-if="isEditing" 
              v-model="customerInfo.customerBrief" 
              type="textarea" 
              :rows="3"
              placeholder="请输入客户简介" 
            />
            <span v-else>{{ customerInfo.customerBrief || '' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="经理记录" :span="2">
            <el-input 
              v-if="isEditing" 
              v-model="customerInfo.managerRecord" 
              type="textarea" 
              :rows="3"
              placeholder="请输入经理记录" 
            />
            <span v-else>{{ customerInfo.managerRecord || '' }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <div v-else class="loading-container">
        <el-skeleton :rows="10" animated />
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCustomerDetail, updateCustomer } from '@/api/customer'
import type { CustomerInfo } from '@/api/types'

const route = useRoute()
const router = useRouter()

const customerInfo = ref<CustomerInfo | null>(null)
const isEditing = ref(false)
const saveLoading = ref(false)
const originalCustomerInfo = ref<CustomerInfo | null>(null)

// 获取客户详情
const fetchCustomerDetail = async () => {
  try {
    const id = Number(route.params.id)
    const data = await getCustomerDetail(id)
    customerInfo.value = data
    // 保存原始数据，用于取消编辑时恢复
    originalCustomerInfo.value = JSON.parse(JSON.stringify(data))
  } catch (error) {
    ElMessage.error('获取客户详情失败')
  }
}

// 编辑客户
const handleEdit = () => {
  isEditing.value = true
}

// 保存客户信息
const handleSave = async () => {
  if (!customerInfo.value?.id) {
    ElMessage.warning('客户信息未加载完成')
    return
  }
  
  saveLoading.value = true
  try {
    await updateCustomer(customerInfo.value.id, customerInfo.value)
    ElMessage.success('保存成功')
    isEditing.value = false
    // 更新原始数据
    originalCustomerInfo.value = JSON.parse(JSON.stringify(customerInfo.value))
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saveLoading.value = false
  }
}

// 取消编辑
const handleCancel = () => {
  if (originalCustomerInfo.value) {
    customerInfo.value = JSON.parse(JSON.stringify(originalCustomerInfo.value))
  }
  isEditing.value = false
}

// 获取级别标签类型
const getLevelType = (level: string | undefined) => {
  switch (level) {
    case 'VIP':
      return 'warning'
    case 'SVIP':
      return 'danger'
    case '普通':
      return 'info'
    case '重要':
      return 'primary'
    default:
      return 'info'
  }
}

// 获取状态标签类型
// const getStatusType = (status: string | undefined) => {
//   switch (status) {
//     case '正常':
//       return 'success'
//     case '流失':
//       return 'danger'
//     case '预警':
//       return 'warning'
//     case '暂停':
//       return 'warning'
//     case '取消':
//       return 'info'
//     default:
//       return 'info'
//   }
// }

// 初始化
onMounted(() => {
  fetchCustomerDetail()
})
</script>

<style scoped>
.customer-detail-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.customer-info {
  margin-bottom: 20px;
}

.loading-container {
  padding: 20px 0;
}
</style>