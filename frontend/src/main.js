import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router/index.js'

// Create application instance
const app = createApp(App)

// Use Pinia for state management
const pinia = createPinia()
app.use(pinia)

// Use router
app.use(router)

// Initialize authentication state
import { useAuthStore } from './stores/authStore'
const authStore = useAuthStore()
authStore.init()

// Mount application
app.mount('#app')