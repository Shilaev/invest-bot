import { createApp } from 'vue'
import App from './App.vue'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

// 1. Импорт стилей Vuetify ПЕРВЫМИ
import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

// 2. Настройка ECharts
import ECharts from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import {
  LineChart,
  BarChart,
  CandlestickChart,
  ScatterChart,
  PieChart
} from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DataZoomComponent,
  VisualMapComponent,
  AxisPointerComponent
} from 'echarts/components'

use([
  CanvasRenderer,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DataZoomComponent,
  VisualMapComponent,
  AxisPointerComponent,
  LineChart,
  BarChart,
  CandlestickChart,
  ScatterChart,
  PieChart
])

const vuetify = createVuetify({
  components,
  directives
})

const app = createApp(App)
app.component('v-chart', ECharts)
app.use(vuetify)
app.mount('#app')
