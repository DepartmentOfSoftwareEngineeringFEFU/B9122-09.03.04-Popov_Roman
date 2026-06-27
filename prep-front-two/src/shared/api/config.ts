const assessmentUrl = import.meta.env.VITE_ASSESSMENT_BASE_URL ?? 'http://localhost:8080'
const authUrl = import.meta.env.VITE_AUTH_BASE_URL ?? 'http://localhost:8083'

export const API_URLS = {
  auth: authUrl,
  tests: assessmentUrl,
  materials: assessmentUrl,
  recommendations: assessmentUrl,
  readiness: assessmentUrl,
} as const

export type ApiServiceName = keyof typeof API_URLS
