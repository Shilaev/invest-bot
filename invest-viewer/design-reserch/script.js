import {createApp} from 'vue';
import {createVuetify} from 'vuetify';

const VueProject = {
    data() {
        return {
            message: 'hello',
            chart: null
        }
    },
    methods: {
        initChart() {
            this.chart = echarts.init(document.getElementById('chart'));
            this.updateChart();
        },
        updateChart() {
            const option = {
                xAxis: {
                    data: ['2017-10-24', '2017-10-25', '2017-10-26', '2017-10-27']
                },
                yAxis: {},
                series: [
                    {
                        type: 'candlestick',
                        data: [
                            [34, 20, 38, 10],
                            [35, 40, 50, 30],
                            [38, 31, 44, 33],
                            [15, 38, 42, 5]
                        ]
                    }
                ]
            };
            this.chart.setOption(option);
        }
    },
    mounted() {
        this.initChart()
    }
};

const vuetify = createVuetify();

// Используйте Vuetify в приложении
createApp(VueProject)
    .use(vuetify)
    .mount('#vueProject');