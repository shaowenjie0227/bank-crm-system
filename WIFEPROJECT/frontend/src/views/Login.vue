<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>银行CRM系统</h2>
        </div>
      </template>
      
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="loginRules" 
        label-width="0"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            style="width: 100%; background: linear-gradient(90deg, #ff69b4, #ff1493); border: none;"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useUserStore } from '@/store/user'
import type { LoginForm } from '@/api/types'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive<LoginForm>({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.userLogin(loginForm)
        ElMessage.success('登录成功')
        router.push('/')
      } catch (error: any) {
        ElMessage.error(error.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #FFEFF4;
  background-image: url('~@/assets/login_background.svg');
  background-size: cover;
  background-position: center;
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

/* 装饰性圆形元素 */
.login-container::after {
  content: '';
  position: absolute;
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background-color: rgba(255, 214, 224, 0.15);
  top: 10%;
  left: 10%;
  box-shadow: 
    500px 200px 0 -50px rgba(255, 194, 209, 0.2),
    200px 500px 0 -100px rgba(255, 175, 204, 0.15),
    800px 400px 0 -70px rgba(255, 229, 229, 0.1);
  animation: float 15s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) scale(1);
  }
  25% {
    transform: translate(20px, -20px) scale(1.05);
  }
  50% {
    transform: translate(0, 20px) scale(0.95);
  }
  75% {
    transform: translate(-20px, -10px) scale(1.02);
  }
}

.login-card {
  width: 400px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(255, 182, 193, 0.3);
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 32px rgba(255, 182, 193, 0.4);
}

.card-header {
  text-align: center;
  padding: 20px 0;
  background: linear-gradient(90deg, #ff69b4, #ff1493);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.card-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.el-form {
  padding: 20px;
}

.el-form-item {
  margin-bottom: 24px;
}

.el-input__wrapper {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.el-input__wrapper.is-focused {
  box-shadow: 0 0 0 2px rgba(255, 105, 180, 0.2);
  border-color: #ff69b4;
}

.el-input__prefix-inner {
  color: #ff69b4;
}
</style>