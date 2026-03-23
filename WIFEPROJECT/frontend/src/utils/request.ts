import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  baseURL: (typeof import.meta !== 'undefined' && import.meta.env?.VITE_API_BASE_URL) || 'http://localhost:8080/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    // 确保请求使用UTF-8编码
    if (!config.headers['Content-Type']) {
      config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果响应码不是200，则判断为错误
    if (res.code !== 200) {
      ElMessage.error(res.message || '系统错误')
      
      // 401: 未登录或token过期
      if (res.code === 401) {
        localStorage.removeItem('token')
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message || '系统错误'))
    } else {
      return res.data
    }
  },
  error => {
    console.error('请求错误:', error)
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request