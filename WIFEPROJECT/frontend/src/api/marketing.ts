import request from '@/utils/request'
import type { MarketingOpportunity, PageParams, ApiResponse, PageResult } from './types'

// 获取营销机会列表 - 返回ApiResponse包装的分页结果
export const getMarketingList = (params: PageParams): Promise<ApiResponse<PageResult<MarketingOpportunity>>> => {
  return request({
    url: '/marketing-opportunities/list',
    method: 'get',
    params
  })
}

// 根据客户ID获取营销机会列表 - 返回ApiResponse包装的营销机会数组
export const getMarketingByCustomerId = (customerId: number): Promise<ApiResponse<MarketingOpportunity[]>> => {
  return request({
    url: `/marketing-opportunities/customer/${customerId}`,
    method: 'get'
  })
}

// 获取营销机会详情 - 返回ApiResponse包装的营销机会信息
export const getMarketingDetail = (id: number): Promise<ApiResponse<MarketingOpportunity>> => {
  return request({
    url: `/marketing-opportunities/${id}`,
    method: 'get'
  })
}

// 添加营销机会 - 返回ApiResponse包装的营销机会信息
export const addMarketing = (data: MarketingOpportunity): Promise<ApiResponse<MarketingOpportunity>> => {
  return request({
    url: '/marketing-opportunities',
    method: 'post',
    data
  })
}

// 更新营销机会 - 返回ApiResponse包装的营销机会信息
export const updateMarketing = (id: number, data: MarketingOpportunity): Promise<ApiResponse<MarketingOpportunity>> => {
  return request({
    url: `/marketing-opportunities/${id}`,
    method: 'put',
    data
  })
}

// 删除营销机会 - 返回ApiResponse包装的字符串消息
export const deleteMarketing = (id: number): Promise<ApiResponse<string>> => {
  return request({
    url: `/marketing-opportunities/${id}`,
    method: 'delete'
  })
}