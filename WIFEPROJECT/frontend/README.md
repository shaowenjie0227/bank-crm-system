# 银行CRM系统前端

这是一个基于Vue3 + TypeScript + Element Plus构建的银行CRM系统前端项目。

## 技术栈

- Vue 3
- TypeScript
- Vite
- Vue Router
- Pinia
- Element Plus
- Axios

## 项目结构

```
frontend/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API接口
│   ├── assets/            # 资源文件
│   ├── components/        # 公共组件
│   ├── router/            # 路由配置
│   ├── store/             # 状态管理
│   ├── styles/            # 样式文件
│   ├── utils/             # 工具函数
│   ├── views/             # 页面组件
│   │   ├── customer/      # 客户管理
│   │   ├── marketing/     # 营销机会
│   │   ├── development/   # 客户开发
│   │   ├── churn/         # 客户流失
│   │   ├── Dashboard.vue  # 仪表盘
│   │   ├── Layout.vue     # 布局组件
│   │   └── Login.vue      # 登录页面
│   ├── App.vue            # 根组件
│   └── main.ts            # 入口文件
├── .env                   # 环境变量
├── index.html             # HTML模板
├── package.json           # 项目依赖
├── tsconfig.json          # TypeScript配置
├── tsconfig.node.json     # TypeScript Node配置
└── vite.config.ts         # Vite配置
```

## 安装与运行

### 安装依赖

```bash
npm install
```

### 开发环境运行

```bash
npm run dev
```

### 生产环境构建

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

### 代码检查

```bash
npm run lint
```

## 功能模块

### 登录模块
- 用户登录
- 登录状态管理

### 仪表盘
- 数据统计
- 图表展示

### 客户管理
- 客户列表
- 客户详情
- 客户增删改查

### 营销机会
- 营销机会列表
- 营销机会详情
- 营销机会增删改查

### 客户开发
- 客户开发列表
- 客户开发详情
- 客户开发增删改查

### 客户流失
- 客户流失列表
- 客户流失详情
- 客户流失增删改查

## 开发规范

- 使用TypeScript进行类型检查
- 遵循Vue3 Composition API规范
- 使用Pinia进行状态管理
- 使用Element Plus组件库
- 遵循ESLint代码规范

## 注意事项

1. 确保后端API服务已启动，默认端口为8080
2. 修改.env文件中的API_BASE_URL以匹配后端服务地址
3. 开发环境默认端口为3000，可在vite.config.ts中修改