import request from '@/utils/request'
import type { LoginForm, LoginResponse, User } from './types'

// 登录
export const login = (data: LoginForm): Promise<LoginResponse['data']> => {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  }).then(data => data as unknown as LoginResponse['data'])
}

// 登出
export const logout = (): Promise<void> => {
  return request({
    url: '/auth/logout',
    method: 'post'
  }).then(data => data as unknown as void)
}

// 获取当前用户信息
export const getCurrentUser = (): Promise<User> => {
  return request({
    url: '/auth/info',
    method: 'get'
  }).then(data => data as unknown as User)
}