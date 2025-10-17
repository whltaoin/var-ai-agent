import { createRouter, createWebHistory } from 'vue-router'

const Home = () => import('../views/Home.vue')
const Love = () => import('../views/LoveApp.vue')
const Manus = () => import('../views/ManusApp.vue')

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/love', name: 'love', component: Love },
    { path: '/manus', name: 'manus', component: Manus },
  ]
})

export default router

