import request from '@/utils/request'
import type { CustomerDevelopment, PageParams, ApiResponse, PageResult } from './types'

// 获取客户开发列表 - 返回ApiResponse包装的分页结果
export const getDevelopmentList = (params: PageParams): Promise<ApiResponse<PageResult<CustomerDevelopment>>> => {
  return request({
    url: '/developments/list',
    method: 'get',
    params
  })
}

// 根据客户ID获取客户开发列表 - 返回ApiResponse包装的客户开发数组
export const getDevelopmentByCustomerId = (customerId: number): Promise<ApiResponse<CustomerDevelopment[]>> => {
  return request({
    url: `/developments/customer/${customerId}`,
    method: 'get'
  })
}

// 获取客户开发详情 - 返回ApiResponse包装的客户开发信息
export const getDevelopmentDetail = (id: number): Promise<ApiResponse<CustomerDevelopment>> => {
  return request({
    url: `/developments/${id}`,
    method: 'get'
  })
}

// 添加客户开发 - 返回ApiResponse包装的客户开发信息
export const addDevelopment = (data: CustomerDevelopment): Promise<ApiResponse<CustomerDevelopment>> => {
  return request({
    url: '/developments',
    method: 'post',
    data
  })
}

// 更新客户开发 - 返回ApiResponse包装的客户开发信息
export const updateDevelopment = (id: number, data: CustomerDevelopment): Promise<ApiResponse<CustomerDevelopment>> => {
  return request({
    url: `/developments/${id}`,
    method: 'put',
    data
  })
}

// 删除客户开发 - 返回ApiResponse包装的字符串消息
export const deleteDevelopment = (id: number): Promise<ApiResponse<string>> => {
  return request({
    url: `/developments/${id}`,
    method: 'delete'
  })
}