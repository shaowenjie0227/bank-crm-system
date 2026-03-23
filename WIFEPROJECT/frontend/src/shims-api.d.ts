// 声明模块
declare module '@/api/profile' {
  export function updateProfile(data: { username: string }): Promise<any>
  export function changePassword(data: { oldPassword: string; newPassword: string }): Promise<any>
  export function getPermissions(): Promise<{ permissions: string[] }>
}

declare module '@/api/user' {
  export function getUserList(params: {
    current: number
    size: number
    username?: string
    role?: string
  }): Promise<any>
  export function getUserDetail(id: number): Promise<any>
  export function addUser(data: { username: string; role: string }): Promise<any>
  export function updateUser(data: { id: number; username: string; role: string }): Promise<any>
  export function deleteUser(id: number): Promise<any>
  export function resetPassword(id: number): Promise<any>
  export function enableUser(id: number): Promise<any>
  export function disableUser(id: number): Promise<any>
}