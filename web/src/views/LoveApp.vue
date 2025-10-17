<template>
  <div>
    <h2>AI 学术分析</h2>
    <p class="meta">聊天记录左侧为 AI，右侧为你的消息。进入页面自动生成聊天室 ID。</p>
    <div class="space"></div>
    <SseChat sse-path="/ai/app/sse" :combine-chunks="true" :ai-avatar="aiAvatar" :user-avatar="userAvatar" />
  </div>
</template>

<script setup lang="ts">
import SseChat from '../components/SseChat.vue'
const aiAvatar = 'https://cdn.jsdelivr.net/gh/tabler/tabler-icons/icons/brain.svg'
const userAvatar = 'https://cdn.jsdelivr.net/gh/tabler/tabler-icons/icons/user.svg'
</script>

<style scoped>
  h2 { margin: 0 0 8px; }
</style>

