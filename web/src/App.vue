<template>
  <div class="container">
    <header class="app-header row">
      <div class="row brand">
        <strong>VAR AI</strong>
        <span class="meta">专注学术分析与多智能体协作 · 实时流式对话 · 一键复制与响应式体验</span>
      </div>
      <nav class="row nav-actions">
        <RouterLink class="btn secondary" to="/">主页</RouterLink>
        <RouterLink class="btn secondary" to="/love">AI 学术分析</RouterLink>
        <RouterLink class="btn secondary" to="/manus">AI 超级智能体</RouterLink>
      </nav>
    </header>
    <RouterView />
    <SiteFooter />
  </div>
  
</template>

<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import SiteFooter from './components/SiteFooter.vue'
</script>

<style scoped>
.app-header { 
  justify-content: space-between; 
  padding: 14px 0; 
  position: sticky; top: 0; z-index: 20;
  backdrop-filter: saturate(130%) blur(10px);
}
.app-header::after{
  content: ""; display: block; position: absolute; left: 0; right: 0; bottom: -1px; height: 2px;
  background: var(--rainbow);
  opacity: .6; border-radius: 2px; filter: drop-shadow(0 0 10px rgba(111,107,255,.35));
}
.brand { gap: 10px; }
.nav-actions { gap: 8px; }
</style>

