import { createApp } from 'vue'
import App from './App.vue'
import TheHeader from '@/components/TheHeader.vue'

// global registration
createApp(App)
  .component('the-header', TheHeader)
  .mount('#app')
