<template>
  <div class="user-management">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="请选择角色" clearable>
            <el-option label="管理员" value="管理员" />
            <el-option label="销售" value="销售" />
            <el-option label="客服" value="客服" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="table-header">
        <el-button type="primary" @click="handleAdd" v-if="hasPermission('user:manage')">
          <el-icon><Plus /></el-icon>添加用户
        </el-button>
      </div>

      <el-table 
        :data="tableData" 
        v-loading="loading" 
        border 
        style="width: 100%"
        element-loading-text="加载中..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 0.8)"
        empty-text="暂无数据"
      >
        <el-table-column prop="id" label="用户ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="role" label="角色" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
              v-if="hasPermission('user:manage')"
            >
              编辑
            </el-button>
            <el-button 
              type="warning" 
              size="small" 
              @click="handleResetPassword(scope.row)"
              v-if="hasPermission('user:manage')"
            >
              重置密码
            </el-button>
            <el-button 
              :type="scope.row.status === 1 ? 'danger' : 'success'" 
              size="small" 
              @click="handleToggleStatus(scope.row)"
              v-if="hasPermission('user:manage')"
            >
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
              v-if="hasPermission('user:manage')"
            >
              删除
            </el-button>
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

    <!-- 添加/编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加用户' : '编辑用户'"
      width="500px"
    >
      <el-form ref="userFormRef" :model="userForm" :rules="userRules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="dialogType === 'add'">
          <el-input 
            v-model="userForm.password" 
            type="password" 
            placeholder="请输入密码" 
            show-password 
          />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="管理员" value="管理员" />
            <el-option label="销售" value="销售" />
            <el-option label="客服" value="客服" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status" v-if="dialogType === 'edit'">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getUserList, addUser, updateUser, deleteUser, resetPassword, enableUser, disableUser } from '@/api/user'
import { useUserStore } from '@/store/user'

interface User {
  id: number
  username: string
  role: string
  status: number
  createTime: string
  updateTime: string
}

const userStore = useUserStore()

// 权限检查
const hasPermission = (permission: string) => {
  // 从用户store获取权限列表
  const userPermissions = userStore.roles || []
  
  // 如果是管理员，拥有所有权限
  if (userPermissions.some(role => role === '管理员' || role === 'admin' || role === 'ADMIN')) {
    return true
  }
  
  // 检查特定权限
  switch (permission) {
    case 'user:manage':
      return userPermissions.some(role => role === '管理员' || role === 'admin' || role === 'ADMIN')
    case 'user:view':
      return userPermissions.some(role => role === '管理员' || role === 'admin' || role === 'ADMIN' || role === '销售' || role === '客服')
    default:
      return false
  }
}

// 搜索表单
const searchForm = reactive({
  username: '',
  role: '',
  status: null
})

// 表格数据
const tableData = ref<User[]>([])
const loading = ref(false)

// 分页配置
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 对话框配置
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const userFormRef = ref()

// 用户表单
const userForm = reactive({
  id: undefined,
  username: '',
  password: '',
  role: '',
  status: 1
})

// 表单验证规则
const userRules = computed(() => {
  const rules: Record<string, Array<{ required?: boolean; message?: string; trigger?: string; min?: number; max?: number; pattern?: RegExp }>> = {
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 3, max: 50, message: '用户名长度必须在3-50位之间', trigger: 'blur' },
      { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
    ],
    role: [
      { required: true, message: '请选择角色', trigger: 'change' }
    ]
  }
  
  // 添加用户时密码必填
  if (dialogType.value === 'add') {
    rules.password = [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 6, max: 20, message: '密码长度必须在6-20位之间', trigger: 'blur' }
    ]
  }
  
  return rules
})

// 获取用户列表
const fetchUserList = async () => {
  loading.value = true
  try {
    // 构建查询参数，只包含非空值
    const params: any = {
      current: pagination.current,
      size: pagination.size
    }
    
    // 添加搜索条件
    if (searchForm.username) {
      params.username = searchForm.username
    }
    if (searchForm.role) {
      params.role = searchForm.role
    }
    // 注释掉status参数，因为后端API不支持
    // if (searchForm.status !== null && searchForm.status !== undefined) {
    //   params.status = searchForm.status
    // }
    
    const response = await getUserList(params)
    tableData.value = response.records || []
    pagination.total = response.total || 0
  } catch (error: any) {
    console.error('获取用户列表失败:', error)
    const errorMessage = error.response?.data?.message || error.message || '获取用户列表失败'
    ElMessage.error(errorMessage)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchUserList()
}

// 重置
const handleReset = () => {
  searchForm.username = ''
  searchForm.role = ''
  searchForm.status = null
  pagination.current = 1
  fetchUserList()
}

// 添加用户
const handleAdd = () => {
  dialogType.value = 'add'
  dialogVisible.value = true
  // 重置表单
  userForm.id = undefined
  userForm.username = ''
  userForm.password = ''
  userForm.role = ''
  userForm.status = 1
}

// 编辑用户
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  // 填充表单
  userForm.id = row.id
  userForm.username = row.username
  userForm.password = '' // 编辑时不显示密码
  userForm.role = row.role
  userForm.status = row.status || 1
}

// 提交表单
const handleSubmit = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          // 添加用户时需要密码
          const userData = {
            username: userForm.username,
            password: userForm.password,
            role: userForm.role
          }
          await addUser(userData)
          ElMessage.success('用户添加成功')
        } else {
          // 编辑用户时不需要密码
          const userData = {
            id: userForm.id!,
            username: userForm.username,
            role: userForm.role,
            status: userForm.status
          }
          await updateUser(userData)
          ElMessage.success('用户更新成功')
        }
        dialogVisible.value = false
        fetchUserList()
      } catch (error: any) {
        console.error('操作失败:', error)
        const errorMessage = error.response?.data?.message || error.message || '操作失败'
        ElMessage.error(errorMessage)
      }
    }
  })
}

// 重置密码
const handleResetPassword = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要重置用户 "${row.username}" 的密码吗？重置后密码将变为123456`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    await resetPassword(row.id)
    ElMessage.success('密码重置成功，新密码为123456')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('密码重置失败')
    }
  }
}

// 切换用户状态
const handleToggleStatus = async (row: any) => {
  try {
    const action = row.status === 1 ? '禁用' : '启用'
    await ElMessageBox.confirm(
      `确定要${action}用户 "${row.username}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    if (row.status === 1) {
      await disableUser(row.id)
    } else {
      await enableUser(row.id)
    }
    ElMessage.success(`${action}成功`)
    fetchUserList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('状态更新失败')
    }
  }
}

// 删除用户
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${row.username}" 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    fetchUserList()
  } catch (error) {
    if (error !== 'cancel') {
      const errorMessage = (error as any).response?.data?.message || (error as any).message || '删除失败'
      ElMessage.error(errorMessage)
    }
  }
}



// 分页大小改变
const handleSizeChange = (val: number) => {
  pagination.size = val
  fetchUserList()
}

// 当前页改变
const handleCurrentChange = (val: number) => {
  pagination.current = val
  fetchUserList()
}

onMounted(() => {
  fetchUserList()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.table-card {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.table-header {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-table {
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>