import { createRouter, createWebHistory, type RouteRecordRaw, type NavigationGuardNext, type RouteLocationNormalized } from 'vue-router'
import { useUserStore } from '@/store/user'

// 扩展RouteMeta接口
declare module 'vue-router' {
  interface RouteMeta {
    title?: string
    icon?: string
    requiresAuth?: boolean
    hidden?: boolean
    roles?: string[]
  }
}

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'DataBoard' }
      },
      {
        path: 'customer',
        name: 'Customer',
        component: () => import('@/views/customer/CustomerList.vue'),
        meta: { title: '客户管理', icon: 'User' }
      },
      {
        path: 'customer/:id',
        name: 'CustomerDetail',
        component: () => import('@/views/customer/CustomerDetail.vue'),
        meta: { title: '客户详情', hidden: true }
      },
      {
        path: 'customer/edit/:id',
        name: 'CustomerEdit',
        component: () => import('@/views/customer/CustomerList.vue'),
        meta: { title: '编辑客户', hidden: true }
      },
      {
        path: 'marketing',
        name: 'Marketing',
        component: () => import('@/views/marketing/MarketingList.vue'),
        meta: { title: '营销机会', icon: 'TrendCharts' }
      },
      {
        path: 'marketing/:id',
        name: 'MarketingDetail',
        component: () => import('@/views/marketing/MarketingDetail.vue'),
        meta: { title: '营销详情', hidden: true }
      },
      {
        path: 'development',
        name: 'Development',
        component: () => import('@/views/development/DevelopmentList.vue'),
        meta: { title: '客户开发', icon: 'Promotion' }
      },
      {
        path: 'development/:id',
        name: 'DevelopmentDetail',
        component: () => import('@/views/development/DevelopmentDetail.vue'),
        meta: { title: '开发详情', hidden: true }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/UserManagement.vue'),
        meta: { title: '用户管理', icon: 'UserFilled', roles: ['管理员'] }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/Profile.vue'),
        meta: { title: '个人中心', icon: 'Avatar' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/404.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: any) => {
  const userStore = useUserStore()
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else if (to.meta.roles && !to.meta.roles.some((role: string) => userStore.roles.includes(role))) {
    // 如果路由需要特定角色但用户不具备该角色，则跳转到首页
    next('/')
  } else {
    next()
  }
})

export default router