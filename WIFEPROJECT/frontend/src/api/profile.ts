import request from '@/utils/request'

// 更新个人信息
export function updateProfile(data: { username: string }) {
  return request({
    url: '/profile',
    method: 'put',
    data
  })
}

// 修改密码
export function changePassword(data: { oldPassword: string; newPassword: string }) {
  return request({
    url: '/profile/password',
    method: 'put',
    data
  })
}

// 获取权限信息
export function getPermissions() {
  return request({
    url: '/profile/permissions',
    method: 'get'
  }).then((response: any) => {
    // 确保返回的数据包含permissions属性
    return response.data || { permissions: [] }
  })
}