<template>
  <div class="customer-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户管理</span>
          <el-button type="primary" @click="handleAdd">新增客户</el-button>
        </div>
      </template>

      <div class="search-container">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="客户名称">
            <el-input v-model="searchForm.name" placeholder="请输入客户名称" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item label="客户类型">
            <el-select v-model="searchForm.type" placeholder="请选择客户类型" clearable style="width: 160px">
              <el-option label="个人客户" value="个人客户" />
              <el-option label="企业客户" value="企业客户" />
            </el-select>
          </el-form-item>
          <el-form-item label="客户等级">
            <el-select v-model="searchForm.level" placeholder="请选择客户等级" clearable style="width: 160px">
              <el-option label="普通" value="普通" />
              <el-option label="VIP" value="VIP" />
              <el-option label="SVIP" value="SVIP" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="customerName" label="客户名称" min-width="150" />
        <el-table-column prop="customerCode" label="客户编号" min-width="140" />
        <el-table-column prop="region" label="地区" width="120" />
        <el-table-column prop="customerManager" label="客户经理" width="120" />
        <el-table-column prop="customerLevel" label="客户等级" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" min-width="170">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
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
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="客户编号" prop="customerCode">
          <el-input v-model="form.customerCode" placeholder="系统自动生成，可手动覆盖" />
        </el-form-item>
        <el-form-item label="客户类型" prop="customerType">
          <el-select v-model="form.customerType" placeholder="请选择客户类型" style="width: 100%">
            <el-option label="个人客户" value="个人客户" />
            <el-option label="企业客户" value="企业客户" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户行业" prop="customerIndustry">
          <el-input v-model="form.customerIndustry" placeholder="请输入客户行业" />
        </el-form-item>
        <el-form-item label="客户银行" prop="customerBank">
          <el-input v-model="form.customerBank" placeholder="请输入客户银行" />
        </el-form-item>
        <el-form-item label="地区" prop="region">
          <el-input v-model="form.region" placeholder="请输入地区" />
        </el-form-item>
        <el-form-item label="客户等级" prop="customerLevel">
          <el-select v-model="form.customerLevel" placeholder="请选择客户等级" style="width: 100%">
            <el-option label="普通" value="普通" />
            <el-option label="VIP" value="VIP" />
            <el-option label="SVIP" value="SVIP" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="客户经理" prop="customerManager">
          <el-input v-model="form.customerManager" :disabled="isSales" placeholder="请输入客户经理用户名" />
        </el-form-item>
        <el-form-item label="客户简介" prop="customerBrief">
          <el-input v-model="form.customerBrief" type="textarea" :rows="3" placeholder="请输入客户简介" />
        </el-form-item>
        <el-form-item label="经理记录" prop="managerRecord">
          <el-input v-model="form.managerRecord" type="textarea" :rows="3" placeholder="请输入经理记录" />
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
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { addCustomer, deleteCustomer, getCustomerDetail, getCustomerList, updateCustomer } from '@/api/customer'
import { useUserStore } from '@/store/user'
import type { CustomerInfo, PageResult } from '@/api/types'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const tableData = ref<CustomerInfo[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增客户')
const formRef = ref<FormInstance>()
const submitLoading = ref(false)

const searchForm = reactive({
  name: '',
  type: '',
  level: ''
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const form = reactive<CustomerInfo>({
  id: undefined,
  customerCode: '',
  customerName: '',
  customerType: '',
  customerIndustry: '',
  customerBank: '',
  region: '',
  customerManager: '',
  customerLevel: '',
  contactPerson: '',
  contactPhone: '',
  email: '',
  customerBrief: '',
  managerRecord: '',
  createTime: undefined,
  updateTime: undefined
})

const isSales = computed(() => userStore.roles.includes('销售'))
const currentUsername = computed(() => userStore.userInfo?.username || userStore.loginInfo?.username || '')

const rules: FormRules = {
  customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
  customerCode: [{ required: true, message: '请输入客户编号', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  customerManager: [{ required: true, message: '请输入客户经理', trigger: 'blur' }]
}

const ensureUserInfo = async () => {
  if (!userStore.userInfo) {
    await userStore.fetchUserInfo()
  }
}

const syncSalesManager = () => {
  if (isSales.value && currentUsername.value) {
    form.customerManager = currentUsername.value
  }
}

const formatDateTime = (date?: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN')
}

const resetForm = () => {
  Object.assign(form, {
    id: undefined,
    customerCode: '',
    customerName: '',
    customerType: '',
    customerIndustry: '',
    customerBank: '',
    region: '',
    customerManager: '',
    customerLevel: '',
    contactPerson: '',
    contactPhone: '',
    email: '',
    customerBrief: '',
    managerRecord: '',
    createTime: undefined,
    updateTime: undefined
  })
  syncSalesManager()
}

const fetchCustomerList = async () => {
  loading.value = true
  try {
    const response = await getCustomerList({
      current: pagination.current,
      size: pagination.size,
      name: searchForm.name,
      type: searchForm.type,
      level: searchForm.level
    }) as unknown as PageResult<CustomerInfo>
    tableData.value = response.records || []
    pagination.total = response.total || 0
  } catch (error) {
    ElMessage.error('获取客户列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  void fetchCustomerList()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.type = ''
  searchForm.level = ''
  handleSearch()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  void fetchCustomerList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  void fetchCustomerList()
}

const handleAdd = () => {
  dialogTitle.value = '新增客户'
  resetForm()
  dialogVisible.value = true
}

const handleView = (row: CustomerInfo) => {
  if (row.id) {
    router.push(`/customer/${row.id}`)
  }
}

const handleEdit = (row: CustomerInfo) => {
  dialogTitle.value = '编辑客户'
  resetForm()
  Object.assign(form, row)
  syncSalesManager()
  dialogVisible.value = true
}

const handleDelete = (row: CustomerInfo) => {
  if (!row.id) return
  ElMessageBox.confirm(`确认删除客户“${row.customerName}”吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCustomer(row.id)
      ElMessage.success('删除成功')
      await fetchCustomerList()
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
      syncSalesManager()
      if (!form.id && !form.customerCode) {
        const timestamp = Date.now().toString().slice(-6)
        const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
        form.customerCode = `KH${timestamp}${random}`
      }
      if (form.id) {
        await updateCustomer(form.id, form)
        ElMessage.success('更新成功')
      } else {
        await addCustomer(form)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      await fetchCustomerList()
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

const openEditFromRoute = async (customerId: number) => {
  const existing = tableData.value.find(item => item.id === customerId)
  if (existing) {
    handleEdit(existing)
    return
  }
  try {
    const data = await getCustomerDetail(customerId) as unknown as CustomerInfo
    handleEdit(data)
  } catch (error) {
    ElMessage.warning('未找到指定的客户信息')
  }
}

onMounted(async () => {
  await ensureUserInfo()
  resetForm()
  await fetchCustomerList()
  if (route.name === 'CustomerEdit' && route.params.id) {
    await openEditFromRoute(Number(route.params.id))
  }
})
</script>

<style scoped>
.customer-list-container {
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