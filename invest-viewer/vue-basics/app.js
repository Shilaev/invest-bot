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
            // Преобразуем candles в формат, который ожидает ECharts для свечного графика
            const candlestickData = this.candles.map(candle => {
                const open = candle;
                const close = candle+10;
                const lowest = open - 1; // Минимальное значение
                const highest = open + 1; // Максимальное значение
                return [close, open, highest, lowest]; // Правильный порядок данных
            });

            const option = {
                xAxis: {
                    type: 'category',
                    data: this.candles.map((_, index) => `Свеча ${index + 1}`) // Подписи для оси X
                },
                yAxis: {},
                series: [
                    {
                        type: 'candlestick',
                        data: candlestickData // Данные для свечного графика
                    }
                ]
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