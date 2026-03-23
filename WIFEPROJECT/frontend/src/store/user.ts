import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, getCurrentUser } from '@/api/auth'
import { updateProfile } from '@/api/profile'
import type { LoginForm, User, LoginResponse } from '@/api/types'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<User | null>(null)
  const roles = ref<string[]>([])
  const loginInfo = ref<LoginResponse['data'] | null>(null)

  // 登录
  const userLogin = async (loginForm: LoginForm) => {
    try {
      const response = await login(loginForm)
      token.value = response.token
      loginInfo.value = response
      roles.value = [response.role || '']
      
      localStorage.setItem('token', response.token)
      return Promise.resolve(response)
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const user = await getCurrentUser()
      userInfo.value = user
      roles.value = [user.role || '']
      return Promise.resolve(user)
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 更新个人信息
  const updateUserInfo = async (data: { username: string }) => {
    try {
      await updateProfile(data)
      // 更新成功后重新获取用户信息
      await fetchUserInfo()
      return Promise.resolve()
    } catch (error) {
      return Promise.reject(error)
    }
  }

  // 登出
  const logout = () => {
    token.value = ''
    userInfo.value = null
    loginInfo.value = null
    roles.value = []
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    loginInfo,
    roles,
    userLogin,
    fetchUserInfo,
    updateUserInfo,
    logout
  }
})