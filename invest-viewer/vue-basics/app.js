const InvestViewer = {
    data() {
        return {
            title: 'invest-viewer',
            textAreaPlaceholder: 'Добавить свечу',
            textAreaInputValue: '',
            candles: []
        }
    },
    methods: {
        addNewCandle() {
            !this.textAreaInputValue || this.textAreaInputValue.trim() === '' ?
                this.textAreaPlaceholder = 'Нельзя создать пустую свечу' :
                this.candles.push(this.textAreaInputValue)


            this.textAreaInputValue = ''
        },
        deleteLastCandle(index) {
            this.candles.splice(index, 1)
        },
        toUpperCase(item) {
            if (item == null) return undefined
            return item.toUpperCase()
        }
    },
    computed: {
        doubleThisValue() {
            console.log('doubleThisValue: ')
            return this.candles.length * 2
        }
    },
    watch: { // Следим за изменениями
        textAreaInputValue(value) {
            if (value && value.length >= 10) {
                console.warn('Достигнуто максимальное количество символов (10)');
                this.textAreaInputValue = ''; // Очищаем поле ввода
            }
        }
    }
}

Vue.createApp(InvestViewer).mount('#app')

