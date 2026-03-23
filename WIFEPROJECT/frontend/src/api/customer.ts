import request from '@/utils/request'
import type { CustomerInfo, PageParams, ApiResponse, PageResult } from './types'

// 获取客户列表 - 返回ApiResponse包装的分页结果
export const getCustomerList = (params: PageParams): Promise<ApiResponse<PageResult<CustomerInfo>>> => {
  return request({
    url: '/customers/list',
    method: 'get',
    params
  })
}

// 获取客户详情 - 返回ApiResponse包装的客户信息
export const getCustomerDetail = (id: number): Promise<ApiResponse<CustomerInfo>> => {
  return request({
    url: `/customers/${id}`,
    method: 'get'
  })
}

// 添加客户 - 返回ApiResponse包装的客户信息
export const addCustomer = (data: CustomerInfo): Promise<ApiResponse<CustomerInfo>> => {
  return request({
    url: '/customers',
    method: 'post',
    data
  })
}

// 更新客户 - 返回ApiResponse包装的客户信息
export const updateCustomer = (id: number, data: CustomerInfo): Promise<ApiResponse<CustomerInfo>> => {
  return request({
    url: `/customers/${id}`,
    method: 'put',
    data
  })
}

// 删除客户 - 返回ApiResponse包装的字符串消息
export const deleteCustomer = (id: number): Promise<ApiResponse<string>> => {
  return request({
    url: `/customers/${id}`,
    method: 'delete'
  })
}