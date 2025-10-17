<template>
  <div class="panel chat-wrapper">
    <div ref="scrollRef" class="chat-scroll">
      <div v-for="(m, idx) in messages" :key="idx" :class="['message', m.role === 'user' ? 'right' : 'left']">
        <img :src="m.role==='assistant'? aiAvatar : userAvatar" class="avatar" alt="avatar" />
        <div :class="['bubble', m.role === 'user' ? 'right' : 'left']">
          <template v-if="m.role === 'assistant'">
            <div class="answer-header">
              <span class="meta">AI · {{ formatTime(m.ts) }}</span>
              <button class="copy-btn" @click="copyMarkdown(m.content)">复制</button>
            </div>
            <div class="markdown" style="text-align: left;" v-html="renderedMarkdown(m.content)"></div>
          </template>
          <template v-else>
            <div style="white-space: pre-wrap;">{{ m.content }}</div>
            <div class="meta">你 · {{ formatTime(m.ts) }}</div>
          </template>
        </div>
      </div>
    </div>

    <div class="chat-input">
      <textarea v-model="input" placeholder="请输入问题，Shift+Enter 换行" @keydown.enter.exact.prevent="onSend" @keydown.enter.shift.stop></textarea>
      <button class="btn" :disabled="loading || !input.trim()" @click="onSend">发送</button>
      <button class="btn secondary" v-if="loading" @click="stop">停止</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, ref, nextTick } from 'vue'
import { marked } from 'marked'
import DOMPurify from 'dompurify'

type Message = { role: 'user' | 'assistant'; content: string; ts: number }

const props = defineProps<{ 
  // SSE endpoint, e.g. '/ai/app/sse' or '/ai/manus/chat'
  ssePath: string,
  // query param names used by backend
  userParam?: string, // default 'content' or 'userPrompt'
  chatIdParam?: string, // default 'chatId'
  // optional existing chatId; if not provided we generate one
  chatId?: string,
  // if true, stream chunks will be concatenated into ONE assistant bubble
  combineChunks?: boolean,
  // per-chunk newline when streaming (for manus)
  chunkNewline?: boolean,
  // avatars
  aiAvatar?: string,
  userAvatar?: string
}>()

const input = ref('')
const messages = ref<Message[]>([])
const loading = ref(false)
const evtSource = ref<EventSource | null>(null)
const scrollRef = ref<HTMLDivElement | null>(null)
const chatId = ref(props.chatId || generateId())
const pendingAssistantIndex = ref<number | null>(null)
const aiAvatar = props.aiAvatar || 'https://cdn.jsdelivr.net/gh/tabler/tabler-icons/icons/brand-octopus.svg'
const userAvatar = props.userAvatar || 'https://cdn.jsdelivr.net/gh/tabler/tabler-icons/icons/user.svg'

function generateId() {
  return `${Date.now().toString(36)}-${Math.random().toString(36).slice(2, 8)}`
}

function formatTime(ts: number) {
  const d = new Date(ts)
  return d.toLocaleTimeString()
}

function append(role: Message['role'], content: string) {
  messages.value.push({ role, content, ts: Date.now() })
  nextTick(() => {
    if (scrollRef.value) {
      scrollRef.value.scrollTop = scrollRef.value.scrollHeight
    }
  })
}

function renderedMarkdown(md: string) {
  const html = marked.parse(md, { breaks: true }) as string
  return DOMPurify.sanitize(html)
}

async function copyMarkdown(md: string) {
  try {
    await navigator.clipboard.writeText(md)
  } catch (e) {
    // ignore silently
  }
}

function openStream(query: string) {
  const base = 'http://localhost:33333/api'
  const userKey = props.userParam || (props.ssePath.includes('manus') ? 'userPrompt' : 'content')
  const chatKey = props.chatIdParam || 'chatId'

  const url = new URL(base + props.ssePath)
  url.searchParams.set(userKey, query)
  url.searchParams.set(chatKey, chatId.value)

  // Close previous if any
  if (evtSource.value) {
    evtSource.value.close()
    evtSource.value = null
  }

  loading.value = true
  const es = new EventSource(url.toString())
  evtSource.value = es

  es.onmessage = (ev) => {
    // Backend may stream plain chunks; append incrementally
    if (ev.data === '[DONE]' || ev.data === 'EOF') {
      stop()
      return
    }
    if (props.combineChunks) {
      // create a draft assistant bubble if not yet
      if (pendingAssistantIndex.value === null) {
        pendingAssistantIndex.value = messages.value.push({ role: 'assistant', content: props.chunkNewline ? (ev.data + "\n") : ev.data, ts: Date.now() }) - 1
      } else {
        messages.value[pendingAssistantIndex.value].content += props.chunkNewline ? (ev.data + "\n") : ev.data
        messages.value[pendingAssistantIndex.value].ts = Date.now()
      }
      nextTick(() => {
        if (scrollRef.value) scrollRef.value.scrollTop = scrollRef.value.scrollHeight
      })
    } else {
      append('assistant', props.chunkNewline ? (ev.data + "\n") : ev.data)
    }
  }

  es.onerror = () => {
    stop()
  }
}

function stop() {
  if (evtSource.value) {
    evtSource.value.close()
    evtSource.value = null
  }
  loading.value = false
  pendingAssistantIndex.value = null
}

function onSend() {
  const q = input.value.trim()
  if (!q) return
  append('user', q)
  input.value = ''
  openStream(q)
}

onBeforeUnmount(() => {
  stop()
})

// expose generated chat id to parent if needed later
defineExpose({ chatId })
</script>

<style scoped>
</style>

