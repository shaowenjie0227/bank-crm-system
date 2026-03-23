const { defineConfig } = require('@vue/cli-service')
const path = require('path')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    port: 8891,
    host: 'localhost',
    open: true
    // 移除代理配置，直接使用绝对路径
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src')
      }
    }
  }
})