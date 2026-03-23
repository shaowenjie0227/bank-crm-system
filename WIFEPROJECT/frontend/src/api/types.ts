// 登录表单
export interface LoginForm {
  username: string
  password: string
}

// 登录响应
export interface LoginResponse {
  code: number
  message: string
  data: {
    token: string
    username: string
    role: string
    expiresIn: number
  }
}

// 用户
export interface User {
  id?: number
  username: string
  role?: string
  password?: string
  salt?: string
  status?: number
  createTime?: string
  updateTime?: string
  token?: string
}

// 角色
export interface Role {
  id?: number
  roleName: string
  permissions?: string
  description?: string
  createTime?: string
}

// 通用响应
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 分页请求参数
export interface PageParams {
  current: number
  size: number
  [key: string]: any
}

// 分页响应 - 对应MyBatis-Plus的IPage接口
export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

// 客户信息
export interface CustomerInfo {
  id?: number
  customerCode: string
  customerName: string
  region?: string
  customerManager?: string
  customerLevel?: string
  contactPerson?: string
  contactPhone?: string
  customerType?: string
  customerStatus?: string
  customerIndustry?: string
  customerBank?: string
  address?: string
  email?: string
  createTime?: string
  updateTime?: string
}

// 营销机会
export interface MarketingOpportunity {
  id?: number
  name: string
  source?: string
  status?: string
  estimatedDate?: string
  estimatedAmount?: string
  description?: string
  stage?: string
  successRate?: string
  contactPhone?: string
  company?: string
  manager?: string
  record?: string
  customerLevel?: string
  address?: string
  recordDescription?: string
  recordAttachment?: string
  createTime?: string
  updateTime?: string
  createdBy?: number
}

// 客户开发
export interface CustomerDevelopment {
  id?: number
  name: string
  developmentStage?: string
  developmentStatus?: string
  estimatedDate?: string
  estimatedAmount?: string
  description?: string
  successRate?: string
  successRecord?: string
  contactPhone?: string
  company?: string
  manager?: string
  record?: string
  customerLevel?: string
  address?: string
  recordDescription?: string
  recordAttachment?: string
  developmentTime?: string
  contactPerson?: string
  createTime?: string
  updateTime?: string
  createdBy?: number
}