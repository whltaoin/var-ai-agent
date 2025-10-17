<template>
  <div class="panel" style="padding: 24px;">
    <h2>主页</h2>
    <p class="meta">请选择一个应用进入对话。</p>
    <div class="space"></div>
    <div class="row" style="gap: 12px; flex-wrap: wrap;">
      <RouterLink class="btn" to="/love">进入 AI 学术分析</RouterLink>
      <RouterLink class="btn" to="/manus">进入 AI 超级智能体</RouterLink>
    </div>
    <div class="space"></div>
    <ParticlesCanvas :height="320" :density="0.28" />
    <div class="space"></div>
    <div class="badges">
      <span class="badge">Vue 3</span>
      <span class="badge">Vite 5</span>
      <span class="badge">Axios</span>
      <span class="badge">SSE Streaming</span>
      <span class="badge">Markdown Render</span>
      <span class="badge">Responsive UI</span>
    </div>
    <div class="space"></div>
    <!-- 页脚已全局化，保留页面内无需重复渲染 -->
  </div>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
import ParticlesCanvas from '../components/ParticlesCanvas.vue'
</script>

<style scoped>
  h2 { margin: 0 0 8px; }
  .badges{ display:flex; flex-wrap:wrap; gap:10px; }
  .badge{ padding:6px 10px; border-radius:999px; background:#f0f5ff; border:1px solid #e1e8ff; color:#35476b; font-size:12px; }
  
</style>

