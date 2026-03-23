# 银行 CRM 客户关系管理系统

一个基于前后端分离架构的银行 CRM 管理系统，覆盖客户信息管理、营销机会跟进、客户开发、用户权限控制与推荐相关能力。

## 项目简介

本项目用于实现银行客户关系管理的基础业务流程，适合作为课程设计、毕业设计、内部练习项目或中小型 CRM 系统原型。

当前仓库包含：

- `frontend`：Vue 3 + TypeScript + Element Plus 前端项目
- `backend`：Spring Boot + MyBatis-Plus + Spring Security 后端服务
- `database`：MySQL 数据库初始化脚本

## 功能模块

- 用户登录与身份认证
- 仪表盘数据展示
- 客户信息管理
- 营销机会管理
- 客户开发管理
- 用户管理
- 个人中心
- 基于用户行为的推荐结果支持

## 技术栈

### 前端

- Vue 3
- TypeScript
- Vue Router
- Pinia
- Element Plus
- Axios
- Vue CLI

### 后端

- Spring Boot 2.6.14
- Spring Security
- MyBatis-Plus
- MySQL 8
- JWT
- Lombok
- Maven

### 数据库

- MySQL
- SQL 初始化脚本位于 `database/bank_crm_system.sql`

## 项目结构

```text
WIFEPROJECT/
├─ backend/                 # Spring Boot 后端
│  ├─ src/main/java/com/bank/crm
│  │  ├─ config/            # 配置类
│  │  ├─ controller/        # 接口控制器
│  │  ├─ dto/               # 数据传输对象
│  │  ├─ entity/            # 实体类
│  │  ├─ mapper/            # MyBatis-Plus Mapper
│  │  ├─ security/          # 安全认证相关
│  │  ├─ service/           # 业务层
│  │  └─ vo/                # 统一返回对象
│  └─ src/main/resources/
│     └─ application.yml    # 后端配置
├─ frontend/                # Vue 前端
│  ├─ src/
│  │  ├─ api/               # 接口封装
│  │  ├─ components/        # 公共组件
│  │  ├─ router/            # 路由配置
│  │  ├─ store/             # 状态管理
│  │  ├─ utils/             # 工具函数
│  │  └─ views/             # 页面视图
│  └─ package.json
└─ database/
   └─ bank_crm_system.sql   # 数据库脚本
```

## 主要页面

- 登录页
- 仪表盘
- 客户管理
- 客户详情
- 营销机会管理
- 营销详情
- 客户开发管理
- 客户开发详情
- 用户管理
- 个人中心

## 后端接口前缀

后端统一接口前缀主要为：

- `/api/auth`
- `/api/customers`
- 以及其他业务模块接口，如 dashboard、marketing、development、user、profile、recommendation 等

示例接口：

```text
POST /api/auth/login
POST /api/auth/logout
GET  /api/auth/info
GET  /api/customers/list
GET  /api/customers/{id}
POST /api/customers
PUT  /api/customers/{id}
DELETE /api/customers/{id}
```

## 数据库说明

项目使用 MySQL，数据库脚本位于：

```text
database/bank_crm_system.sql
```

脚本中包含的核心业务表包括：

- 客户信息表
- 营销机会表
- 客户开发表
- 客户流失表
- 用户表
- 角色表
- 推荐交互表
- 推荐结果表

## 环境要求

- Node.js 16+
- npm 8+
- JDK 8
- Maven 3.6+
- MySQL 8.x

## 快速开始

### 1. 克隆项目

```bash
git clone <your-repo-url>
cd WIFEPROJECT
```

### 2. 初始化数据库

1. 在 MySQL 中创建数据库，例如：`bank_crm_system`
2. 执行 `database/bank_crm_system.sql` 脚本

### 3. 配置后端

编辑文件：

```text
backend/src/main/resources/application.yml
```

根据你的本地环境修改数据库连接信息，例如：

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bank_crm_system?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: 你的数据库密码
```

默认后端端口：

```text
8080
```

### 4. 启动后端

在 `backend` 目录下运行：

```bash
mvn spring-boot:run
```

或者在 Windows 环境下使用：

```bash
.\mvnw.cmd spring-boot:run
```

### 5. 配置前端

编辑文件：

```text
frontend/.env
```

当前接口地址示例：

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

如果你的后端地址有变化，请同步修改。

### 6. 启动前端

在 `frontend` 目录下运行：

```bash
npm install
npm run serve
```

### 7. 打包前端

```bash
npm run build
```

## 默认说明

- 后端使用 Spring Security + JWT 进行认证授权
- 前端通过路由守卫控制访问权限
- 不同角色可见的菜单和页面存在差异
- 数据库脚本中包含部分初始化测试数据

## 注意事项

- 当前仓库中的部分中文注释和文档存在乱码，通常与文件编码有关，建议统一保存为 UTF-8
- `application.yml` 中如果保留真实数据库地址和密码，上传 GitHub 前建议替换为本地示例配置
- `frontend` 目录下已包含 `node_modules`、`dist` 等内容时，建议配合 `.gitignore` 清理后再提交仓库
- 如果前后端端口或接口前缀不一致，需要同步修改前端请求配置

## 可优化方向

- 补充接口文档与系统截图
- 增加单元测试与集成测试
- 增加 Docker 部署配置
- 完善角色权限粒度控制
- 优化推荐模块说明与算法文档

## License

本项目仅用于学习、练习与演示用途，如需商业使用请根据实际情况补充授权说明。
