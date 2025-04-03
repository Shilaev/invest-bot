<template>
  <v-card class="ma-4 pa-4">
    <v-card-title>Пример графика</v-card-title>
    <v-card-text>
      <v-chart
          class="chart"
          :option="chartOptions"
          autoresize
      />
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  title: {
    text: 'Продажи за неделю'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: 'Продажи',
      type: 'line',
      data: [150, 230, 224, 218, 135, 147, 260],
      smooth: true,
      itemStyle: {
        color: '#1976D2' // Цвет в стиле Vuetify
      }
    }
  ],
  grid: {
    containLabel: true,
    left: '3%',
    right: '3%'
  }
})
</script>

<style scoped>
.chart {
  width: 100%;
  height: 400px;
}
</style>
