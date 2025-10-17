<template>
  <div>
    <h2>AI 超级智能体</h2>
    <p class="meta">调用 Manus 智能体接口，实时流式输出。</p>
    <div class="space"></div>
    <SseChat sse-path="/ai/manus/chat" user-param="userPrompt" :chunk-newline="true" :ai-avatar="aiAvatar" :user-avatar="userAvatar" />
  </div>
</template>

<script setup lang="ts">
import SseChat from '../components/SseChat.vue'
const aiAvatar = 'https://cdn.jsdelivr.net/gh/tabler/tabler-icons/icons/robot.svg'
const userAvatar = 'https://cdn.jsdelivr.net/gh/tabler/tabler-icons/icons/user.svg'
</script>

<style scoped>
  h2 { margin: 0 0 8px; }
</style>

