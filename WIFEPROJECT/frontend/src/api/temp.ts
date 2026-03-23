import request from '@/utils/request'

// 临时更新密码（用于管理员重置密码）
export function updateTempPassword(username: string, newPassword: string) {
  return request({
    url: '/temp/update-password',
    method: 'post',
    params: { username, newPassword }
  })
}