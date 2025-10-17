<template>
  <div ref="wrap" class="pc-wrap">
    <canvas ref="canvas"></canvas>
  </div>
  
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from 'vue'

const props = defineProps<{ height?: number; density?: number }>()

const canvas = ref<HTMLCanvasElement | null>(null)
const wrap = ref<HTMLDivElement | null>(null)
let ctx: CanvasRenderingContext2D | null = null
let raf = 0
let particles: { x: number; y: number; vx: number; vy: number }[] = []
let mouse = { x: 0, y: 0, active: false }

function resize() {
  if (!canvas.value || !wrap.value) return
  const dpr = Math.min(2, window.devicePixelRatio || 1)
  const w = wrap.value.clientWidth
  const h = props.height || 320
  canvas.value.width = Math.floor(w * dpr)
  canvas.value.height = Math.floor(h * dpr)
  canvas.value.style.width = w + 'px'
  canvas.value.style.height = h + 'px'
  ctx = canvas.value.getContext('2d')
  if (ctx) ctx.setTransform(dpr, 0, 0, dpr, 0, 0)
  init()
}

function init() {
  const w = wrap.value?.clientWidth || 0
  const h = props.height || 320
  const target = Math.max(48, Math.floor((props.density ?? 0.24) * w))
  particles = Array.from({ length: target }).map(() => ({
    x: Math.random() * w,
    y: Math.random() * h,
    vx: (Math.random() - 0.5) * 0.6,
    vy: (Math.random() - 0.5) * 0.6,
  }))
}

function step() {
  if (!ctx || !wrap.value) { raf = requestAnimationFrame(step); return }
  const w = wrap.value.clientWidth
  const h = props.height || 320
  ctx.clearRect(0, 0, w, h)

  // draw connections
  for (let i = 0; i < particles.length; i++) {
    const p = particles[i]
    p.x += p.vx
    p.y += p.vy
    if (p.x < 0 || p.x > w) p.vx *= -1
    if (p.y < 0 || p.y > h) p.vy *= -1

    // parallax toward mouse
    if (mouse.active) {
      const dx = mouse.x - p.x
      const dy = mouse.y - p.y
      const dist = Math.hypot(dx, dy)
      if (dist < 120) {
        p.vx -= dx / 2400
        p.vy -= dy / 2400
      }
    }
  }

  // lines
  for (let i = 0; i < particles.length; i++) {
    for (let j = i + 1; j < particles.length; j++) {
      const a = particles[i], b = particles[j]
      const dx = a.x - b.x, dy = a.y - b.y
      const d2 = dx * dx + dy * dy
      if (d2 < 140 * 140) {
        const alpha = 1 - d2 / (140 * 140)
        ctx.strokeStyle = `rgba(100,140,255,${alpha * 0.6})`
        ctx.lineWidth = 1
        ctx.beginPath()
        ctx.moveTo(a.x, a.y)
        ctx.lineTo(b.x, b.y)
        ctx.stroke()
      }
    }
  }

  // dots
  for (const p of particles) {
    ctx.fillStyle = 'rgba(79,108,255,0.9)'
    ctx.beginPath()
    ctx.arc(p.x, p.y, 2, 0, Math.PI * 2)
    ctx.fill()
  }

  raf = requestAnimationFrame(step)
}

onMounted(() => {
  resize()
  window.addEventListener('resize', resize)
  wrap.value?.addEventListener('mousemove', (e) => {
    const rect = wrap.value!.getBoundingClientRect()
    mouse.x = e.clientX - rect.left
    mouse.y = e.clientY - rect.top
    mouse.active = true
  })
  wrap.value?.addEventListener('mouseleave', () => (mouse.active = false))
  raf = requestAnimationFrame(step)
})

onBeforeUnmount(() => {
  cancelAnimationFrame(raf)
  window.removeEventListener('resize', resize)
})
</script>

<style scoped>
.pc-wrap { position: relative; width: 100%; border-radius: 16px; overflow: hidden; }
canvas { display: block; width: 100%; height: 320px; background: linear-gradient(180deg, rgba(110,168,255,.12), rgba(174,236,255,.08)); }
@media (max-width: 480px) { canvas { height: 240px; } }
</style>

