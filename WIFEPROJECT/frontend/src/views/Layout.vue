<template>
  <div class="layout-container">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px" class="aside">
        <div class="logo">
          <h2>银行CRM</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="menu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item index="/dashboard">
            <el-icon><DataBoard /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>
          
          <el-menu-item index="/customer">
            <el-icon><User /></el-icon>
            <span>客户管理</span>
          </el-menu-item>
          
          <el-menu-item index="/marketing">
            <el-icon><TrendCharts /></el-icon>
            <span>营销机会</span>
          </el-menu-item>
          
          <el-menu-item index="/development">
            <el-icon><Promotion /></el-icon>
            <span>客户开发</span>
          </el-menu-item>
          
          <el-menu-item index="/user" v-if="userStore.roles.some(role => role === '管理员' || role === 'admin' || role === 'ADMIN')">
            <el-icon><UserFilled /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <!-- 顶部导航 -->
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ currentPage }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-icon><UserFilled /></el-icon>
                {{ userStore.userInfo?.username || '用户' }}
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 主要内容区 -->
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 当前页面标题
const currentPage = computed(() => {
  const title = route.meta?.title as string
  return title || '仪表盘'
})

// 处理下拉菜单命令
const handleCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确认退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        userStore.logout()
        ElMessage.success('退出成功')
        router.push('/login')
      } catch {
        // 用户取消操作
      }
      break
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background-color: #304156;
  color: #fff;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b2f3a;
}

.logo h2 {
  margin: 0;
  color: #409EFF;
  font-size: 18px;
}

.menu {
  border-right: none;
  height: calc(100vh - 60px);
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #606266;
}

.user-info:hover {
  color: #409EFF;
}

.main {
    min-height: calc(100vh - 120px);
    padding: 20px;
    background-color: #FFF9E6;
    background-image: url('~@/assets/dashboard_background.svg');
    background-size: cover;
    background-position: center;
    position: relative;
    overflow: hidden;
  }
  
  .main::after {
    content: '';
    position: absolute;
    width: 250px;
    height: 250px;
    border-radius: 50%;
    background-color: rgba(255, 237, 213, 0.15);
    top: 15%;
    right: 10%;
    box-shadow: 
      -400px 300px 0 -80px rgba(254, 226, 210, 0.2),
      100px 400px 0 -50px rgba(255, 249, 230, 0.15),
      -200px 100px 0 -100px rgba(253, 230, 138, 0.1);
    animation: floatDashboard 20s ease-in-out infinite;
  }
  
  @keyframes floatDashboard {
    0%, 100% {
      transform: translate(0, 0) scale(1);
    }
    25% {
      transform: translate(-30px, 20px) scale(1.08);
    }
    50% {
      transform: translate(20px, 10px) scale(0.92);
    }
    75% {
      transform: translate(10px, -20px) scale(1.05);
    }
  }
</style>