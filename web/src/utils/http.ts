import axios from 'axios'

const http = axios.create({
  baseURL: 'http://localhost:33333/api',
  timeout: 30000,
})

export default http

