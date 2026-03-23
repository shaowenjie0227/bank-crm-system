<template>
  <div class="marketing-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>营销机会</span>
          <el-button type="primary" @click="handleAdd">新增营销机会</el-button>
        </div>
      </template>
      
      <!-- 搜索区域 -->
      <div class="search-container">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="机会名称">
            <el-input v-model="searchForm.name" placeholder="请输入机会名称" clearable />
        </el-form-item>
        <el-form-item label="机会来源">
          <el-select v-model="searchForm.source" placeholder="请选择机会来源" clearable>
            <el-option label="客户推荐" value="客户推荐" />
            <el-option label="系统推荐" value="系统推荐" />
            <el-option label="市场活动" value="市场活动" />
            <el-option label="自主开发" value="自主开发" />
          </el-select>
        </el-form-item>
        <el-form-item label="机会状态">
          <el-select v-model="searchForm.status" placeholder="请选择机会状态" clearable>
              <el-option label="待处理" value="待处理" />
              <el-option label="进行中" value="进行中" />
              <el-option label="已完成" value="已完成" />
              <el-option label="已关闭" value="已关闭" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 表格区域 -->
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="机会名称" />
        <el-table-column prop="source" label="机会来源" />
        <el-table-column prop="status" label="机会状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="description" label="机会性质" />
        <el-table-column prop="estimatedDate" label="截止时间" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button 
              type="warning" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
              v-if="userStore.roles.includes('管理员') || (userStore.roles.includes('客户经理') && scope.row.createdBy === userStore.userInfo?.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页区域 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 营销机会表单对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="50%"
      @close="handleDialogClose"
    >
      <el-form 
        ref="formRef" 
        :model="form" 
        :rules="rules" 
        label-width="100px"
      >
        <el-form-item label="机会名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入机会名称" />
        </el-form-item>
        <el-form-item label="机会来源" prop="source">
          <el-select v-model="form.source" placeholder="请选择机会来源" style="width: 100%">
            <el-option label="客户推荐" value="客户推荐" />
            <el-option label="系统推荐" value="系统推荐" />
            <el-option label="市场活动" value="市场活动" />
            <el-option label="自主开发" value="自主开发" />
          </el-select>
        </el-form-item>
        <el-form-item label="机会状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择机会状态" style="width: 100%">
            <el-option label="待处理" value="待处理" />
            <el-option label="进行中" value="进行中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已关闭" value="已关闭" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <el-date-picker
            v-model="form.createTime"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="机会性质" prop="description">
          <el-input v-model="form.description" placeholder="请输入机会性质" />
        </el-form-item>
        <el-form-item label="截止时间" prop="estimatedDate">
          <el-date-picker
            v-model="form.estimatedDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="机会描述" prop="record">
          <el-input 
            v-model="form.record" 
            type="textarea" 
            placeholder="请输入机会描述"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { getMarketingList, addMarketing, updateMarketing, deleteMarketing } from '@/api/marketing'
import { useUserStore } from '@/store/user'
import type { MarketingOpportunity } from '@/api/types'

const router = useRouter()
const userStore = useUserStore()

// 表格数据
const tableData = ref<MarketingOpportunity[]>([])
const loading = ref(false)

// 搜索表单
const searchForm = reactive({
  name: '',
  source: '',
  status: ''
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('新增营销机会')
const formRef = ref<FormInstance>()
const submitLoading = ref(false)

// 表单数据
const form = reactive<MarketingOpportunity>({
  name: '',
  source: '',
  status: '',
  createTime: '',
  description: '',
  estimatedDate: '',
  record: ''
})

// 表单验证规则
const rules: FormRules = {
  name: [
    { required: true, message: '请输入机会名称', trigger: 'blur' }
  ],
  source: [
    { required: true, message: '请选择机会来源', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择机会状态', trigger: 'change' }
  ],
  createTime: [
    { required: true, message: '请选择创建时间', trigger: 'change' }
  ]
}

// 获取营销机会列表
const fetchMarketingList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      name: searchForm.name,
      source: searchForm.source,
      status: searchForm.status
    }
    
    const response = await getMarketingList(params)
    tableData.value = response.records || []
    pagination.total = response.total || 0
  } catch (error) {
    ElMessage.error('获取营销机会列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchMarketingList()
}

// 重置
const handleReset = () => {
  searchForm.name = ''
  searchForm.source = ''
  searchForm.status = ''
  handleSearch()
}

// 分页大小改变
const handleSizeChange = (size: number) => {
  pagination.size = size
  fetchMarketingList()
}

// 当前页改变
const handleCurrentChange = (current: number) => {
  pagination.current = current
  fetchMarketingList()
}

// 新增营销机会
const handleAdd = () => {
  dialogTitle.value = '新增营销机会'
  dialogVisible.value = true
  resetForm()
}

// 查看营销机会
const handleView = (row: MarketingOpportunity) => {
  router.push(`/marketing/${row.id}`)
}

// 编辑营销机会
const handleEdit = (row: MarketingOpportunity) => {
  dialogTitle.value = '编辑营销机会'
  dialogVisible.value = true
  Object.assign(form, row)
}

// 删除营销机会
const handleDelete = (row: MarketingOpportunity) => {
  // 权限控制：只有客户经理或管理员可以删除
  const currentUser = userStore.userInfo
  const userRoles = userStore.roles
  
  if (!userRoles.includes('管理员') && !userRoles.includes('客户经理')) {
    ElMessage.error('您没有权限删除营销机会')
    return
  }
  
  // 如果是客户经理，只能删除自己创建的营销机会
  if (userRoles.includes('客户经理') && row.createdBy !== currentUser?.id) {
    ElMessage.error('您只能删除自己创建的营销机会')
    return
  }
  
  ElMessageBox.confirm(
    `确认删除营销机会 "${row.name}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteMarketing(row.id!)
      ElMessage.success('删除成功')
      fetchMarketingList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) {
          await updateMarketing(form.id, form)
          ElMessage.success('更新成功')
        } else {
          await addMarketing(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        fetchMarketingList()
      } catch (error) {
        ElMessage.error('操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetForm()
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: undefined,
    name: '',
    source: '',
    status: '',
    createTime: '',
    description: '',
    estimatedDate: '',
    record: ''
  })
}

// 获取状态标签类型
const getStatusType = (status: string) => {
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
  fetchMarketingList()
})

// 获取当前用户信息
onMounted(async () => {
  if (!userStore.userInfo) {
    await userStore.fetchUserInfo()
  }
})
</script>

<style scoped>
.marketing-list-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-container {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>