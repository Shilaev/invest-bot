const InvestViewer = {
    data() {
        return {
            title: 'invest-viewer',
            textAreaPlaceholder: 'Добавить свечу',
            textAreaInputValue: '',
            candles: [],
            chart: null // Добавляем переменную для хранения экземпляра графика
        }
    },
    methods: {
        addNewCandle() {
            if (!this.textAreaInputValue || this.textAreaInputValue.trim() === '') {
                this.textAreaPlaceholder = 'Нельзя создать пустую свечу';
                return;
            }
            this.candles.push(this.textAreaInputValue);
            this.textAreaInputValue = '';
            this.updateChart(); // Обновляем график при добавлении свечи
        },
        deleteLastCandle(index) {
            this.candles.splice(index, 1);
            this.updateChart(); // Обновляем график при удалении свечи
        },
        toUpperCase(item) {
            if (item == null) return;
            return item.toUpperCase();
        },
        initChart() {
            // Инициализация графика
            this.chart = echarts.init(document.getElementById('chart'));
            this.updateChart();
        },
        updateChart() {
            const option = {
                title: {
                    text: 'График свечей'
                },
                tooltip: {},
                xAxis: {
                    type: 'category',
                    data: this .candles.map((_, index) => `Свеча ${index + 1}`)
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    name: 'Свечи',
                    type: 'bar',
                    data: this.candles.map(candle => parseFloat(candle) || 0) // Преобразуем свечи в числа
                }]
            };
            this.chart.setOption(option);
        }
    },
    mounted() {
        this.initChart(); // Инициализируем график при монтировании компонента
    },
    computed: {
        doubleThisValue() {
            console.log('doubleThisValue: ');
            return this.candles.length * 2;
        }
    },
    watch: {
        textAreaInputValue(value) {
            if (value && value.length >= 10) {
                console.warn('Достигнуто максимальное количество символов (10)');
                this.textAreaInputValue = ''; // Очищаем поле ввода
            }
        }
    }
}

Vue.createApp(InvestViewer).mount('#app');