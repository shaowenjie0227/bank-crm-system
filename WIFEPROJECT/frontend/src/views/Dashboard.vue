<template>
  <div class="dashboard-shell">
    <div class="dashboard-backdrop backdrop-one"></div>
    <div class="dashboard-backdrop backdrop-two"></div>

    <section class="dashboard-hero glass-panel">
      <div class="hero-copy">
        <span class="hero-kicker">Dashboard</span>
        <h1 class="page-title">数据总览</h1>
        <p class="page-subtitle">实时查看客户、营销机会和客户开发进展。</p>
      </div>

      <div class="hero-actions">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleDateChange"
        />
        <el-button class="refresh-button" type="primary" :icon="Refresh" :loading="loading" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </section>

    <el-row :gutter="20" class="stats-grid">
      <el-col v-for="card in statsCards" :key="card.title" :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <article class="stat-card glass-panel" :class="card.type">
          <div class="stat-icon-wrap">
            <el-icon :size="24">
              <component :is="card.icon" />
            </el-icon>
          </div>
          <div class="stat-main">
            <span class="stat-label">{{ card.title }}</span>
            <strong class="stat-value">{{ card.value }}</strong>
            <span class="stat-meta">{{ card.description }}</span>
          </div>
          <div class="stat-trend" :class="{ positive: card.change >= 0, negative: card.change < 0 }">
            {{ formatTrend(card.change) }}
          </div>
        </article>
      </el-col>
    </el-row>

    <section class="content-panel glass-panel recommendation-panel">
      <div class="panel-heading">
        <div>
          <h3>智能推荐</h3>
          <p>{{ recommendationOverview?.summary || '基于客户经理历史跟进行为，动态推荐值得优先推进的客户和机会。' }}</p>
        </div>
        <el-tag class="recommendation-tag" effect="light">{{ recommendationOverview?.algorithm || '协同过滤推荐' }}</el-tag>
      </div>

      <div v-if="recommendationItems.length" class="recommendation-list">
        <article v-for="item in recommendationItems" :key="item.customerId" class="recommendation-item">
          <div class="recommendation-score">
            <span>推荐分</span>
            <strong>{{ formatRecommendationScore(item.score) }}</strong>
          </div>

          <div class="recommendation-body">
            <div class="recommendation-top">
              <div class="recommendation-title-block">
                <strong>{{ item.customerName || '未命名客户' }}</strong>
                <span>
                  {{ [item.customerLevel || '未分级', item.customerIndustry || '行业待补充', item.region || '地区待补充'].join(' · ') }}
                </span>
              </div>
              <span v-if="item.similarUsers?.length" class="recommendation-similar">
                相似用户：{{ item.similarUsers.join('、') }}
              </span>
            </div>

            <p class="recommendation-reason">{{ item.reason || '系统已根据协同过滤结果生成推荐。' }}</p>

            <div class="recommendation-tags">
              <el-tag v-if="item.opportunityTitle" size="small" effect="light" type="warning">
                {{ item.opportunityTitle }}
              </el-tag>
              <el-tag v-if="item.opportunityStage" size="small" effect="light" type="success">
                {{ item.opportunityStage }}
              </el-tag>
            </div>
          </div>

          <div class="recommendation-actions">
            <el-button text type="primary" @click="viewCustomer(item.customerId)">查看客户</el-button>
            <el-button v-if="item.opportunityId" text @click="goMarketing">营销机会</el-button>
          </div>
        </article>
      </div>
      <el-empty v-else description="暂无可用推荐" />

      <p class="recommendation-footnote">
        行为样本 {{ recommendationOverview?.interactionCount || 0 }} 条
        <span v-if="recommendationOverview?.generatedAt"> · 生成时间 {{ recommendationOverview.generatedAt }}</span>
      </p>
    </section>

    <el-row :gutter="20" class="dashboard-content">
      <el-col :xs="24" :xl="15">
        <section class="content-panel glass-panel table-panel">
          <div class="panel-heading">
            <div>
              <h3>最新客户</h3>
              <p>按创建时间展示最近录入的客户资料</p>
            </div>
            <el-button text @click="router.push('/customer')">查看全部</el-button>
          </div>

          <el-table v-if="latestCustomers.length" :data="latestCustomers" class="dashboard-table" stripe>
            <el-table-column prop="customerName" label="客户名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="customerLevel" label="客户等级" width="110">
              <template #default="scope">
                <el-tag effect="light" round :type="getCustomerLevelTag(scope.row.customerLevel)">
                  {{ scope.row.customerLevel || '未设置' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="contactPerson" label="联系人" width="120" />
            <el-table-column prop="contactPhone" label="联系电话" width="140" />
            <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
            <el-table-column prop="createTime" label="创建时间" width="120">
              <template #default="scope">
                {{ formatDateTime(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="96" fixed="right">
              <template #default="scope">
                <el-button text type="primary" @click="viewCustomer(scope.row.id)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-else description="暂无客户数据" />
        </section>
      </el-col>

      <el-col :xs="24" :xl="9">
        <section class="content-panel glass-panel activity-panel">
          <div class="panel-heading">
            <div>
              <h3>最近动态</h3>
              <p>聚合客户、营销机会和开发记录变更</p>
            </div>
          </div>

          <div v-if="latestActivities.length" class="activity-list">
            <article v-for="activity in latestActivities" :key="activity.id" class="activity-item">
              <div class="activity-icon">
                <el-icon>
                  <component :is="getActivityIcon(activity.type, activity.action)" />
                </el-icon>
              </div>
              <div class="activity-body">
                <div class="activity-row">
                  <strong>{{ activity.title }}</strong>
                  <span>{{ activity.time }}</span>
                </div>
                <p>{{ activity.description }}</p>
              </div>
            </article>
          </div>
          <el-empty v-else description="暂无动态" />
        </section>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Avatar, Delete, Edit, Plus, Promotion, Refresh, TrendCharts } from '@element-plus/icons-vue'
import { getCustomerList } from '@/api/customer'
import { getDashboardStats } from '@/api/dashboard'
import { getDashboardRecommendations, type RecommendationDashboardData, type RecommendationItem } from '@/api/recommendation'
import { getDevelopmentList } from '@/api/development'
import { getMarketingList } from '@/api/marketing'
import type { CustomerDevelopment, CustomerInfo, MarketingOpportunity } from '@/api/types'

interface Activity {
  id: string
  type: 'customer' | 'opportunity' | 'development'
  action?: 'add' | 'update' | 'delete'
  title: string
  description: string
  time: string
  timestamp: number
}

interface DashboardStats {
  customerCount?: number
  customerChange?: number
  opportunityCount?: number
  opportunityChange?: number
  developmentCount?: number
  developmentChange?: number
}

interface PageData<T> {
  records?: T[]
  total?: number
  size?: number
  current?: number
  pages?: number
}

const router = useRouter()
const loading = ref(false)
const dateRange = ref<string[]>([])

const customerCount = ref(0)
const customerChange = ref(0)
const opportunityCount = ref(0)
const opportunityChange = ref(0)
const developmentCount = ref(0)
const developmentChange = ref(0)

const latestCustomers = ref<CustomerInfo[]>([])
const latestActivities = ref<Activity[]>([])
const recommendationOverview = ref<RecommendationDashboardData | null>(null)
const recommendationItems = ref<RecommendationItem[]>([])

const statsCards = computed(() => [
  {
    title: '客户总数',
    value: customerCount.value,
    change: customerChange.value,
    description: '当前系统中的客户档案数量',
    icon: Avatar,
    type: 'customer'
  },
  {
    title: '营销机会',
    value: opportunityCount.value,
    change: opportunityChange.value,
    description: '正在持续跟进的营销机会数',
    icon: TrendCharts,
    type: 'opportunity'
  },
  {
    title: '客户开发',
    value: developmentCount.value,
    change: developmentChange.value,
    description: '当前处于开发流程中的记录数',
    icon: Promotion,
    type: 'development'
  }
])

const formatDateTime = (dateTime?: string): string => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  const now = new Date()
  const diffTime = Math.max(0, now.getTime() - date.getTime())
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))

  if (diffDays === 0) return '今天'
  if (diffDays === 1) return '昨天'
  if (diffDays < 7) return `${diffDays}天前`
  return date.toLocaleDateString('zh-CN')
}

const formatRelativeTime = (dateTime?: string): string => {
  if (!dateTime) return '-'

  const timestamp = new Date(dateTime).getTime()
  if (Number.isNaN(timestamp)) return '-'

  const diffTime = Math.max(0, Date.now() - timestamp)
  const diffMinutes = Math.floor(diffTime / (1000 * 60))
  const diffHours = Math.floor(diffTime / (1000 * 60 * 60))
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))

  if (diffMinutes < 1) return '刚刚'
  if (diffMinutes < 60) return `${diffMinutes}分钟前`
  if (diffHours < 24) return `${diffHours}小时前`
  if (diffDays < 30) return `${diffDays}天前`
  return new Date(dateTime).toLocaleDateString('zh-CN')
}

const formatTrend = (value: number): string => {
  const prefix = value >= 0 ? '+' : ''
  return `${prefix}${value}%`
}

const formatRecommendationScore = (value?: number): string => {
  return Number(value || 0).toFixed(2)
}

const getCustomerLevelTag = (level?: string): '' | 'success' | 'warning' | 'danger' | 'info' => {
  switch (level) {
    case 'SVIP':
    case 'A级':
      return 'danger'
    case 'VIP':
    case 'B级':
      return 'warning'
    case '普通':
    case 'C级':
      return 'success'
    default:
      return 'info'
  }
}

const getActivityIcon = (type: Activity['type'], action?: Activity['action']) => {
  if (action === 'add') return Plus
  if (action === 'update') return Edit
  if (action === 'delete') return Delete

  switch (type) {
    case 'customer':
      return Avatar
    case 'opportunity':
      return TrendCharts
    case 'development':
      return Promotion
  }
}

const buildActivity = (
  id: string,
  type: Activity['type'],
  title: string,
  description: string,
  sourceTime?: string,
  action: Activity['action'] = 'add'
): Activity | null => {
  if (!sourceTime) return null

  const timestamp = new Date(sourceTime).getTime()
  if (Number.isNaN(timestamp)) return null

  return {
    id,
    type,
    action,
    title,
    description,
    time: formatRelativeTime(sourceTime),
    timestamp
  }
}

const viewCustomer = (id?: number) => {
  if (id) {
    router.push(`/customer/${id}`)
  }
}

const goMarketing = () => {
  router.push('/marketing')
}

const refreshData = async () => {
  loading.value = true
  try {
    await fetchDashboardData()
  } finally {
    loading.value = false
  }
}

const handleDateChange = () => {
  void fetchDashboardData()
}

const applyStats = (statsRes?: DashboardStats | null) => {
  if (!statsRes) return
  customerCount.value = Number(statsRes.customerCount || 0)
  customerChange.value = Number(statsRes.customerChange || 0)
  opportunityCount.value = Number(statsRes.opportunityCount || 0)
  opportunityChange.value = Number(statsRes.opportunityChange || 0)
  developmentCount.value = Number(statsRes.developmentCount || 0)
  developmentChange.value = Number(statsRes.developmentChange || 0)
}

const applyRecommendations = (data?: RecommendationDashboardData | null) => {
  recommendationOverview.value = data || null
  recommendationItems.value = data?.recommendations || []
}

const fetchLatestActivities = async () => {
  const [customerRes, marketingRes, developmentRes] = await Promise.allSettled([
    getCustomerList({ current: 1, size: 5 }),
    getMarketingList({ current: 1, size: 5 }),
    getDevelopmentList({ current: 1, size: 5 })
  ])

  const activities: Activity[] = []

  if (customerRes.status === 'fulfilled') {
    const data = customerRes.value as PageData<CustomerInfo>
    data.records?.forEach((customer) => {
      const created = buildActivity(
        `customer-add-${customer.id}`,
        'customer',
        '新增客户',
        `成功添加客户“${customer.customerName}”`,
        customer.createTime,
        'add'
      )
      if (created) activities.push(created)

      if (customer.updateTime && customer.createTime && new Date(customer.updateTime).getTime() > new Date(customer.createTime).getTime()) {
        const updated = buildActivity(
          `customer-update-${customer.id}`,
          'customer',
          '更新客户',
          `更新了客户“${customer.customerName}”的信息`,
          customer.updateTime,
          'update'
        )
        if (updated) activities.push(updated)
      }
    })
  }

  if (marketingRes.status === 'fulfilled') {
    const data = marketingRes.value as PageData<MarketingOpportunity>
    data.records?.forEach((marketing) => {
      const created = buildActivity(
        `marketing-add-${marketing.id}`,
        'opportunity',
        '新增营销机会',
        `新增了营销机会“${marketing.name}”`,
        marketing.createTime,
        'add'
      )
      if (created) activities.push(created)

      if (marketing.updateTime && marketing.createTime && new Date(marketing.updateTime).getTime() > new Date(marketing.createTime).getTime()) {
        const updated = buildActivity(
          `marketing-update-${marketing.id}`,
          'opportunity',
          '更新营销机会',
          `更新了营销机会“${marketing.name}”的信息`,
          marketing.updateTime,
          'update'
        )
        if (updated) activities.push(updated)
      }
    })
  }

  if (developmentRes.status === 'fulfilled') {
    const data = developmentRes.value as PageData<CustomerDevelopment>
    data.records?.forEach((development) => {
      const created = buildActivity(
        `development-add-${development.id}`,
        'development',
        '新增客户开发',
        `新增了客户开发“${development.name}”`,
        development.createTime,
        'add'
      )
      if (created) activities.push(created)

      if (development.updateTime && development.createTime && new Date(development.updateTime).getTime() > new Date(development.createTime).getTime()) {
        const updated = buildActivity(
          `development-update-${development.id}`,
          'development',
          '更新客户开发',
          `更新了客户开发“${development.name}”的信息`,
          development.updateTime,
          'update'
        )
        if (updated) activities.push(updated)
      }
    })
  }

  latestActivities.value = activities.sort((a, b) => b.timestamp - a.timestamp).slice(0, 10)
}

const fetchDashboardData = async () => {
  const [statsRes, customerRes, recommendationRes] = await Promise.allSettled([
    getDashboardStats(),
    getCustomerList({ current: 1, size: 8 }),
    getDashboardRecommendations()
  ])

  if (statsRes.status === 'fulfilled') {
    applyStats(statsRes.value as DashboardStats)
  }

  if (customerRes.status === 'fulfilled') {
    const data = customerRes.value as PageData<CustomerInfo>
    latestCustomers.value = data.records || []
  }

  if (recommendationRes.status === 'fulfilled') {
    applyRecommendations(recommendationRes.value as RecommendationDashboardData)
  }

  try {
    await fetchLatestActivities()
  } catch (error) {
    console.error('获取最新动态失败:', error)
  }
}

onMounted(() => {
  void fetchDashboardData()
})
</script>

<style scoped>
.dashboard-shell {
  --glass-bg: rgba(255, 255, 255, 0.66);
  --glass-border: rgba(255, 255, 255, 0.45);
  --text-main: #17324d;
  --text-muted: #5f7288;
  --shadow-soft: 0 20px 45px rgba(31, 66, 110, 0.12);
  position: relative;
  min-height: calc(100vh - 84px);
  padding: 24px;
  overflow: hidden;
  background:
    radial-gradient(circle at top left, rgba(112, 173, 255, 0.2), transparent 32%),
    radial-gradient(circle at 85% 12%, rgba(94, 211, 190, 0.18), transparent 22%),
    linear-gradient(180deg, #f4f8fc 0%, #eef3f9 42%, #e8eef7 100%);
}

.dashboard-backdrop {
  position: absolute;
  border-radius: 50%;
  filter: blur(10px);
  pointer-events: none;
}

.backdrop-one {
  top: -80px;
  right: -60px;
  width: 260px;
  height: 260px;
  background: rgba(106, 181, 255, 0.22);
}

.backdrop-two {
  bottom: -120px;
  left: -40px;
  width: 320px;
  height: 320px;
  background: rgba(86, 201, 177, 0.18);
}

.glass-panel {
  position: relative;
  z-index: 1;
  border: 1px solid var(--glass-border);
  background: var(--glass-bg);
  box-shadow: var(--shadow-soft);
  backdrop-filter: blur(18px);
  -webkit-backdrop-filter: blur(18px);
}

.dashboard-hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 28px 30px;
  border-radius: 28px;
  margin-bottom: 20px;
}

.hero-copy {
  display: grid;
  gap: 8px;
}

.hero-kicker {
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.24em;
  text-transform: uppercase;
  color: #6b86a3;
}

.page-title {
  margin: 0;
  font-size: clamp(28px, 4vw, 38px);
  line-height: 1.05;
  color: var(--text-main);
}

.page-subtitle {
  margin: 0;
  color: var(--text-muted);
  font-size: 15px;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 12px;
}

.refresh-button {
  min-width: 116px;
  border: none;
  box-shadow: 0 14px 26px rgba(64, 158, 255, 0.22);
}

.stats-grid {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  min-height: 156px;
  padding: 22px;
  border-radius: 24px;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 24px 48px rgba(31, 66, 110, 0.18);
}

.stat-card.customer {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.7), rgba(228, 241, 255, 0.82));
}

.stat-card.opportunity {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.7), rgba(231, 248, 239, 0.82));
}

.stat-card.development {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.7), rgba(255, 244, 226, 0.82));
}

.stat-icon-wrap {
  display: grid;
  place-items: center;
  width: 52px;
  height: 52px;
  border-radius: 16px;
  color: #fff;
  flex-shrink: 0;
}

.stat-card.customer .stat-icon-wrap {
  background: linear-gradient(135deg, #4a9cff, #6fbcff);
}

.stat-card.opportunity .stat-icon-wrap {
  background: linear-gradient(135deg, #33b58d, #62d7b1);
}

.stat-card.development .stat-icon-wrap {
  background: linear-gradient(135deg, #f0a53b, #f6c46d);
}

.stat-main {
  display: grid;
  gap: 8px;
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: var(--text-muted);
}

.stat-value {
  font-size: 34px;
  line-height: 1;
  color: var(--text-main);
}

.stat-meta {
  font-size: 13px;
  line-height: 1.5;
  color: var(--text-muted);
}

.stat-trend {
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  align-self: flex-start;
  background: rgba(137, 151, 168, 0.12);
  color: #6f7d8d;
}

.stat-trend.positive {
  background: rgba(49, 183, 122, 0.16);
  color: #1f8f5a;
}

.stat-trend.negative {
  background: rgba(230, 90, 90, 0.16);
  color: #c74f4f;
}

.dashboard-content {
  align-items: stretch;
}

.content-panel {
  height: 100%;
  padding: 22px;
  border-radius: 26px;
}

.recommendation-panel {
  margin-bottom: 20px;
}

.recommendation-tag {
  border: none;
  color: #2666a8;
  background: rgba(64, 158, 255, 0.14);
}

.recommendation-list {
  display: grid;
  gap: 14px;
}

.recommendation-item {
  display: flex;
  gap: 16px;
  align-items: flex-start;
  padding: 18px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.46);
  border: 1px solid rgba(255, 255, 255, 0.42);
}

.recommendation-score {
  min-width: 82px;
  padding: 14px 12px;
  border-radius: 18px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.18), rgba(95, 211, 190, 0.2));
  text-align: center;
  color: #1b5688;
}

.recommendation-score span {
  display: block;
  font-size: 12px;
  color: #5f7288;
}

.recommendation-score strong {
  display: block;
  margin-top: 6px;
  font-size: 24px;
  line-height: 1;
}

.recommendation-body {
  flex: 1;
  min-width: 0;
}

.recommendation-top {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
}

.recommendation-title-block strong {
  display: block;
  color: var(--text-main);
  font-size: 16px;
}

.recommendation-title-block span,
.recommendation-similar,
.recommendation-footnote {
  color: var(--text-muted);
  font-size: 12px;
}

.recommendation-reason {
  margin: 0 0 12px;
  color: var(--text-main);
  font-size: 13px;
  line-height: 1.6;
}

.recommendation-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.recommendation-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex-shrink: 0;
}

.recommendation-footnote {
  margin: 14px 0 0;
}

.panel-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 18px;
}

.panel-heading h3 {
  margin: 0;
  color: var(--text-main);
  font-size: 20px;
}

.panel-heading p {
  margin: 6px 0 0;
  color: var(--text-muted);
  font-size: 13px;
}

:deep(.dashboard-table) {
  --el-table-border-color: rgba(197, 212, 229, 0.55);
  --el-table-header-bg-color: rgba(255, 255, 255, 0.45);
  --el-table-tr-bg-color: transparent;
  --el-table-row-hover-bg-color: rgba(255, 255, 255, 0.48);
  border-radius: 18px;
  overflow: hidden;
}

:deep(.dashboard-table th.el-table__cell) {
  color: #63788f;
  font-weight: 700;
  background: rgba(255, 255, 255, 0.18);
}

:deep(.dashboard-table td.el-table__cell) {
  background: transparent;
}

.activity-list {
  display: grid;
  gap: 14px;
}

.activity-item {
  display: flex;
  gap: 14px;
  align-items: flex-start;
  padding: 16px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.42);
  border: 1px solid rgba(255, 255, 255, 0.38);
}

.activity-icon {
  display: grid;
  place-items: center;
  width: 42px;
  height: 42px;
  border-radius: 14px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.18), rgba(95, 211, 190, 0.22));
  color: #2f6ea8;
  flex-shrink: 0;
}

.activity-body {
  min-width: 0;
  flex: 1;
}

.activity-row {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 6px;
}

.activity-row strong {
  color: var(--text-main);
  font-size: 14px;
}

.activity-row span,
.activity-body p {
  color: var(--text-muted);
  font-size: 13px;
}

.activity-body p {
  margin: 0;
  line-height: 1.6;
}

@media (max-width: 992px) {
  .dashboard-shell {
    padding: 18px;
  }

  .dashboard-hero {
    flex-direction: column;
    align-items: flex-start;
  }

  .hero-actions {
    width: 100%;
    justify-content: flex-start;
  }
}

@media (max-width: 768px) {
  .stat-card {
    min-height: auto;
  }

  .content-panel {
    padding: 18px;
    border-radius: 22px;
  }

  .activity-row {
    flex-direction: column;
    gap: 6px;
  }

  .recommendation-item,
  .recommendation-top,
  .recommendation-actions {
    flex-direction: column;
    align-items: flex-start;
  }

  .hero-actions :deep(.el-date-editor) {
    width: 100%;
  }
}
</style>









