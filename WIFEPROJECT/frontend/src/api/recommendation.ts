import request from '@/utils/request'

export interface RecommendationItem {
  customerId?: number
  customerName?: string
  customerLevel?: string
  customerIndustry?: string
  region?: string
  opportunityId?: number
  opportunityTitle?: string
  opportunityStage?: string
  score?: number
  reason?: string
  similarUsers?: string[]
}

export interface RecommendationDashboardData {
  username?: string
  algorithm?: string
  summary?: string
  generatedAt?: string
  interactionCount?: number
  recommendations?: RecommendationItem[]
}

export const getDashboardRecommendations = (): Promise<RecommendationDashboardData> => {
  return request({
    url: '/recommendations/dashboard',
    method: 'get'
  })
}
