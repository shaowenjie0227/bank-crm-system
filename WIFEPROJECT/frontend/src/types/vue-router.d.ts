declare module 'vue-router' {
  import type { Component, App } from 'vue'
  
  export interface RouteMeta {
    requiresAuth?: boolean
    title?: string
    icon?: string
    hidden?: boolean
  }
  
  export interface RouteRecordRaw {
    path: string
    name?: string
    component?: Component
    components?: Record<string, Component>
    redirect?: string
    meta?: RouteMeta
    children?: RouteRecordRaw[]
    props?: boolean | Record<string, any> | string
  }
  
  export interface RouteLocationNormalized {
    path: string
    name: string | symbol | undefined | null
    params: Record<string, any>
    query: Record<string, any>
    hash: string
    fullPath: string
    matched: RouteRecordRaw[]
    meta: RouteMeta
  }
  
  export type NavigationGuardNext = (to?: any) => void
  
  export interface Router {
    push(to: string | { path: string }): Promise<void>
    replace(to: string | { path: string }): Promise<void>
    go(delta: number): void
    back(): void
    forward(): void
    beforeEach(guard: (to: any, from: any, next: any) => void): void
    afterEach(hook: (to: any, from: any) => void): void
    currentRoute: any
    options: any
    install(app: App<any>): void
  }
  
  export function createRouter(options: {
    history: any
    routes: RouteRecordRaw[]
  }): Router
  
  export function createWebHistory(base?: string): any
  
  export function useRouter(): Router
  
  export function useRoute(): RouteLocationNormalized
}