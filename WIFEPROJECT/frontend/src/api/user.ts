import request from '@/utils/request'
import type { User, PageParams, PageResult } from './types'

// 获取用户列表
export function getUserList(params: {
  current: number
  size: number
  username?: string
  role?: string
}): Promise<PageResult<User>> {
  return request({
    url: '/users/list',
    method: 'get',
    params
  }).then(data => data as unknown as PageResult<User>)
}

// 获取用户详情
export function getUserDetail(id: number) {
  return request({
    url: `/users/${id}`,
    method: 'get'
  }).then(data => data as unknown as User)
}

// 添加用户
export function addUser(data: {
  username: string
  password: string
  role: string
}) {
  return request({
    url: '/users',
    method: 'post',
    data
  }).then(data => data as unknown as User)
}

// 更新用户
export function updateUser(data: {
  id: number
  username: string
  role: string
}) {
  return request({
    url: `/users/${data.id}`,
    method: 'put',
    data
  }).then(data => data as unknown as User)
}

// 删除用户
export function deleteUser(id: number) {
  return request({
    url: `/users/${id}`,
    method: 'delete'
  })
}

// 重置密码
export function resetPassword(id: number) {
  return request({
    url: `/users/${id}/reset-password`,
    method: 'put'
  })
}

// 启用用户
export function enableUser(id: number) {
  return request({
    url: `/users/${id}/status`,
    method: 'put',
    params: { enabled: true }
  })
}

// 禁用用户
export function disableUser(id: number) {
  return request({
    url: `/users/${id}/status`,
    method: 'put',
    params: { enabled: false }
  })
}

// 获取角色列表
export function getRoleList() {
  return request({
    url: '/users/roles',
    method: 'get'
  })
}