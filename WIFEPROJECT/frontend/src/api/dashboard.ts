import request from '@/utils/request'
import type { ApiResponse } from './types'

// 获取仪表板统计数据
export const getDashboardStats = (): Promise<ApiResponse<any>> => {
  return request({
    url: '/dashboards/stats',
    method: 'get'
  })
}

// 获取客户图表数据
export const getCustomerChartData = (period?: string): Promise<ApiResponse<any>> => {
  return request({
    url: '/dashboards/customer-chart',
    method: 'get',
    params: { period }
  })
}

// 获取转化图表数据
export const getConversionChartData = (period?: string): Promise<ApiResponse<any>> => {
  return request({
    url: '/dashboards/conversion-chart',
    method: 'get',
    params: { period }
  })
}