<template>
  <div class="development-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户开发</span>
          <el-button type="primary" @click="handleAdd">新增客户开发</el-button>
        </div>
      </template>

      <div class="search-container">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="客户名称">
            <el-input v-model="searchForm.customerName" placeholder="请输入客户名称" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item label="开发状态">
            <el-select v-model="searchForm.status" placeholder="请选择开发状态" clearable style="width: 160px">
              <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="开发阶段">
            <el-input v-model="searchForm.progress" placeholder="请输入开发阶段" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="客户名称" min-width="160" />
        <el-table-column prop="developmentStage" label="开发阶段" min-width="120" />
        <el-table-column prop="developmentStatus" label="开发状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.developmentStatus)">
              {{ scope.row.developmentStatus || '未设置' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="developmentTime" label="开发时间" min-width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.developmentTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="开发描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="warning" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%" @close="handleDialogClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="客户名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="开发阶段" prop="developmentStage">
          <el-input v-model="form.developmentStage" placeholder="请输入开发阶段" />
        </el-form-item>
        <el-form-item label="开发状态" prop="developmentStatus">
          <el-select v-model="form.developmentStatus" placeholder="请选择开发状态" style="width: 100%">
            <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="开发时间" prop="developmentTime">
          <el-date-picker v-model="form.developmentTime" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="开发描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入开发描述" />
        </el-form-item>
        <el-form-item label="成功记录" prop="successRecord">
          <el-input v-model="form.successRecord" type="textarea" :rows="3" placeholder="请输入成功记录" />
        </el-form-item>
        <el-form-item label="负责人" prop="manager">
          <el-input v-model="form.manager" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="记录" prop="record">
          <el-input v-model="form.record" placeholder="请输入记录" />
        </el-form-item>
        <el-form-item label="客户等级" prop="customerLevel">
          <el-select v-model="form.customerLevel" placeholder="请选择客户等级" style="width: 100%">
            <el-option label="普通" value="普通" />
            <el-option label="VIP" value="VIP" />
            <el-option label="SVIP" value="SVIP" />
          </el-select>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="记录描述" prop="recordDescription">
          <el-input v-model="form.recordDescription" type="textarea" :rows="3" placeholder="请输入记录描述" />
        </el-form-item>
        <el-form-item label="记录附件" prop="recordAttachment">
          <el-input v-model="form.recordAttachment" placeholder="请输入记录附件" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { addDevelopment, deleteDevelopment, getDevelopmentDetail, getDevelopmentList, updateDevelopment } from '@/api/development'
import type { CustomerDevelopment, PageResult } from '@/api/types'

const router = useRouter()
const route = useRoute()

const statusOptions = ['初步接触', '需求分析', '方案提供', '进行中', '已完成', '已暂停', '已取消', '待处理', '已关闭']

const tableData = ref<CustomerDevelopment[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增客户开发')
const formRef = ref<FormInstance>()
const submitLoading = ref(false)

const searchForm = reactive({
  customerName: '',
  status: '',
  progress: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive<CustomerDevelopment>({
  id: undefined,
  name: '',
  developmentStage: '',
  developmentStatus: '',
  developmentTime: '',
  description: '',
  successRecord: '',
  manager: '',
  record: '',
  customerLevel: '',
  address: '',
  recordDescription: '',
  recordAttachment: '',
  createTime: '',
  updateTime: '',
  createdBy: 0
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
  developmentStage: [{ required: true, message: '请输入开发阶段', trigger: 'blur' }],
  developmentStatus: [{ required: true, message: '请选择开发状态', trigger: 'change' }],
  developmentTime: [{ required: true, message: '请选择开发时间', trigger: 'change' }],
  successRecord: [{ required: true, message: '请输入成功记录', trigger: 'blur' }],
  record: [{ required: true, message: '请输入记录', trigger: 'blur' }]
}

const resetForm = () => {
  Object.assign(form, {
    id: undefined,
    name: '',
    developmentStage: '',
    developmentStatus: '',
    developmentTime: '',
    description: '',
    successRecord: '',
    manager: '',
    record: '',
    customerLevel: '',
    address: '',
    recordDescription: '',
    recordAttachment: '',
    createTime: '',
    updateTime: '',
    createdBy: 0
  })
}

const formatDateTime = (dateTime?: string) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString()
}

const getStatusType = (status?: string) => {
  switch (status) {
    case '需求分析':
      return 'primary'
    case '方案提供':
    case '进行中':
      return 'warning'
    case '已完成':
      return 'success'
    case '已取消':
    case '已关闭':
      return 'danger'
    default:
      return 'info'
  }
}

const fetchDevelopmentList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      customerName: searchForm.customerName || '',
      progress: searchForm.progress || '',
      status: searchForm.status || ''
    }
    const response = await getDevelopmentList(params) as unknown as PageResult<CustomerDevelopment>
    tableData.value = response.records || []
    pagination.total = response.total || 0
  } catch (error) {
    ElMessage.error('获取客户开发列表失败')
    console.error('获取客户开发列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  void fetchDevelopmentList()
}

const handleReset = () => {
  searchForm.customerName = ''
  searchForm.progress = ''
  searchForm.status = ''
  handleSearch()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  void fetchDevelopmentList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  void fetchDevelopmentList()
}

const handleAdd = () => {
  dialogTitle.value = '新增客户开发'
  resetForm()
  dialogVisible.value = true
}

const handleView = (row: CustomerDevelopment) => {
  if (row.id) {
    router.push(`/development/${row.id}`)
  }
}

const handleEdit = (row: CustomerDevelopment) => {
  dialogTitle.value = '编辑客户开发'
  resetForm()
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: CustomerDevelopment) => {
  if (!row.id) {
    ElMessage.error('客户开发ID无效')
    return
  }

  ElMessageBox.confirm(`确认删除客户开发“${row.name}”吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDevelopment(row.id as number)
      ElMessage.success('删除成功')
      await fetchDevelopmentList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => undefined)
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (form.id) {
        await updateDevelopment(form.id, form)
        ElMessage.success('更新成功')
      } else {
        await addDevelopment(form)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      await fetchDevelopmentList()
    } catch (error) {
      ElMessage.error('操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetForm()
}

const handleEditFromDetail = async (editId: number) => {
  try {
    const data = await getDevelopmentDetail(editId) as unknown as CustomerDevelopment
    dialogTitle.value = '编辑客户开发'
    resetForm()
    Object.assign(form, data)
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取客户开发信息失败')
  }
}

onMounted(() => {
  void fetchDevelopmentList()
  const editId = route.query.editId
  if (editId) {
    void handleEditFromDetail(Number(editId))
    void router.replace('/development')
  }
})
</script>

<style scoped>
.development-list-container {
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
  gap: 8px 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>