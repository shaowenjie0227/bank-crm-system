<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人中心</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form ref="basicFormRef" :model="basicForm" :rules="basicRules" label-width="100px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="basicForm.username" :disabled="!isEditing" />
            </el-form-item>
            <el-form-item label="角色">
              <el-input v-model="basicForm.role" disabled />
            </el-form-item>
            <el-form-item label="创建时间">
              <el-input v-model="basicForm.createTime" disabled />
            </el-form-item>
            <el-form-item label="更新时间">
              <el-input v-model="basicForm.updateTime" disabled />
            </el-form-item>
            <el-form-item>
              <el-button v-if="!isEditing" type="primary" @click="startEdit">编辑</el-button>
              <el-button v-if="isEditing" type="primary" @click="saveBasicInfo">保存</el-button>
              <el-button v-if="isEditing" @click="cancelEdit">取消</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 修改密码 -->
        <el-tab-pane label="修改密码" name="password">
          <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="100px">
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input 
                v-model="passwordForm.oldPassword" 
                type="password" 
                placeholder="请输入旧密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input 
                v-model="passwordForm.newPassword" 
                type="password" 
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input 
                v-model="passwordForm.confirmPassword" 
                type="password" 
                placeholder="请再次输入新密码"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitPasswordChange">修改密码</el-button>
              <el-button @click="resetPasswordForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>



        <!-- 操作日志 -->
        <el-tab-pane label="操作日志" name="logs">
          <div class="logs-info">
            <el-form :inline="true" :model="logSearchForm" class="log-search-form">
              <el-form-item label="操作类型">
                <el-select v-model="logSearchForm.action" placeholder="请选择操作类型" clearable style="width: 150px">
                  <el-option label="新增" value="add" />
                  <el-option label="编辑" value="update" />
                  <el-option label="删除" value="delete" />
                  <el-option label="查看" value="view" />
                </el-select>
              </el-form-item>
              <el-form-item label="操作对象">
                <el-select v-model="logSearchForm.type" placeholder="请选择操作对象" clearable style="width: 150px">
                  <el-option label="客户" value="customer" />
                  <el-option label="营销机会" value="marketing" />
                  <el-option label="客户开发" value="development" />
                  <el-option label="用户" value="user" />
                </el-select>
              </el-form-item>
              <el-form-item label="日期范围">
                <el-date-picker
                  v-model="logSearchForm.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  style="width: 240px"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleLogSearch">搜索</el-button>
                <el-button @click="handleLogReset">重置</el-button>
              </el-form-item>
            </el-form>

            <el-table :data="operationLogs" border style="width: 100%" v-loading="logLoading">
              <el-table-column prop="id" label="日志ID" width="80" />
              <el-table-column prop="username" label="操作用户" width="120" />
              <el-table-column prop="action" label="操作类型" width="100">
                <template #default="scope">
                  <el-tag :type="getActionType(scope.row.action)">
                    {{ getActionLabel(scope.row.action) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="type" label="操作对象" width="100">
                <template #default="scope">
                  {{ getTypeLabel(scope.row.type) }}
                </template>
              </el-table-column>
              <el-table-column prop="description" label="操作描述" min-width="200" />
              <el-table-column prop="ip" label="IP地址" width="120" />
              <el-table-column prop="createTime" label="操作时间" width="180" />
            </el-table>

            <div class="pagination-container">
              <el-pagination
                v-model:current-page="logPagination.current"
                v-model:page-size="logPagination.size"
                :page-sizes="[10, 20, 50, 100]"
                :total="logPagination.total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleLogSizeChange"
                @current-change="handleLogCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { updateProfile, changePassword as apiChangePassword } from '@/api/profile'

const userStore = useUserStore()

// 当前激活的标签页
const activeTab = ref('basic')

// 监听标签页切换
watch(activeTab, (newTab) => {
  if (newTab === 'logs') {
    fetchOperationLogs()
  }
})

// 用户信息
const userInfo = ref({
  id: 0,
  username: '',
  role: '',
  createTime: '',
  updateTime: '' as string | undefined
})

// 基本信息表单
const basicFormRef = ref()
const basicForm = reactive({
  username: '',
  role: '',
  createTime: '',
  updateTime: '' as string | undefined
})

// 密码表单
const passwordFormRef = ref()
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 编辑状态
const isEditing = ref(false)

// 操作日志搜索表单
const logSearchForm = reactive({
  action: '',
  type: '',
  dateRange: [] as string[]
})

// 操作日志数据
const operationLogs = ref([])
const logLoading = ref(false)

// 操作日志分页
const logPagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表单验证规则
const basicRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度必须在3-50位之间', trigger: 'blur' }
  ]
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须在6-20位之间', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须在6-20位之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: any) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const user = await userStore.fetchUserInfo()
    userInfo.value = {
      id: user.id || 0,
      username: user.username || '',
      role: user.role || '',
      createTime: user.createTime || '',
      updateTime: user.updateTime || ''
    }
    basicForm.username = user.username || ''
    basicForm.role = user.role || ''
    basicForm.createTime = user.createTime || ''
    basicForm.updateTime = user.updateTime || ''
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 获取操作日志列表
const fetchOperationLogs = async () => {
  try {
    logLoading.value = true
    const params = {
      current: logPagination.current,
      size: logPagination.size,
      action: logSearchForm.action || undefined,
      type: logSearchForm.type || undefined,
      startDate: logSearchForm.dateRange?.[0] || undefined,
      endDate: logSearchForm.dateRange?.[1] || undefined
    }
    
    // 模拟API调用，实际使用时需要后端提供相应的接口
    // const response = await getOperationLogs(params)
    
    // 模拟数据
    const mockData = generateMockLogs()
    operationLogs.value = mockData.records
    logPagination.total = mockData.total
    
  } catch (error) {
    ElMessage.error('获取操作日志失败')
  } finally {
    logLoading.value = false
  }
}

// 生成模拟数据
const generateMockLogs = () => {
  const actions = ['add', 'update', 'delete', 'view']
  const types = ['customer', 'marketing', 'development', 'user']
  const descriptions = {
    add: '添加了新的记录',
    update: '修改了记录信息',
    delete: '删除了记录',
    view: '查看了记录详情'
  }
  
  const records = []
  for (let i = 0; i < logPagination.size; i++) {
    records.push({
      id: (logPagination.current - 1) * logPagination.size + i + 1,
      username: userInfo.value.username,
      action: actions[Math.floor(Math.random() * actions.length)],
      type: types[Math.floor(Math.random() * types.length)],
      description: descriptions[actions[Math.floor(Math.random() * actions.length)] as keyof typeof descriptions],
      ip: `192.168.1.${Math.floor(Math.random() * 255)}`,
      createTime: new Date(Date.now() - Math.random() * 86400000 * 30).toLocaleString('zh-CN')
    })
  }
  
  return {
    records,
    total: 150 // 模拟总记录数
  }
}

// 操作类型标签映射
const getActionType = (action: string) => {
  const typeMap: Record<string, string> = {
    add: 'success',
    update: 'warning',
    delete: 'danger',
    view: 'info'
  }
  return typeMap[action] || 'info'
}

// 操作类型标签文本
const getActionLabel = (action: string) => {
  const labelMap: Record<string, string> = {
    add: '新增',
    update: '编辑',
    delete: '删除',
    view: '查看'
  }
  return labelMap[action] || action
}

// 操作对象标签文本
const getTypeLabel = (type: string) => {
  const labelMap: Record<string, string> = {
    customer: '客户',
    marketing: '营销机会',
    development: '客户开发',
    user: '用户'
  }
  return labelMap[type] || type
}

// 开始编辑
const startEdit = () => {
  isEditing.value = true
}

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false
  basicForm.username = userInfo.value.username
}

// 操作日志搜索
const handleLogSearch = () => {
  logPagination.current = 1
  fetchOperationLogs()
}

// 操作日志重置
const handleLogReset = () => {
  logSearchForm.action = ''
  logSearchForm.type = ''
  logSearchForm.dateRange = []
  handleLogSearch()
}

// 操作日志分页大小改变
const handleLogSizeChange = (val: number) => {
  logPagination.size = val
  fetchOperationLogs()
}

// 操作日志当前页改变
const handleLogCurrentChange = (val: number) => {
  logPagination.current = val
  fetchOperationLogs()
}

// 保存基本信息
const saveBasicInfo = async () => {
  if (!basicFormRef.value) return
  
  await basicFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        await updateProfile({ username: basicForm.username })
        ElMessage.success('个人信息更新成功')
        isEditing.value = false
        fetchUserInfo()
      } catch (error) {
        ElMessage.error('个人信息更新失败')
      }
    }
  })
}

// 修改密码
const submitPasswordChange = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        await apiChangePassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        ElMessage.success('密码修改成功')
        resetPasswordForm()
      } catch (error) {
        ElMessage.error('密码修改失败')
      }
    }
  })
}

// 重置密码表单
const resetPasswordForm = () => {
  if (passwordFormRef.value) {
    passwordFormRef.value.resetFields()
  }
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.profile-card {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.logs-info {
  margin-top: 20px;
}

.log-search-form {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>