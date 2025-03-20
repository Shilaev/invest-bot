const investViewer = {
    data() {
        return {
            title: 'invest-viewer',
            textAreaPlaceholder: 'Добавить свечу',
            textAreaInputValue: '',
            candles: []
        }
    },
    methods: {
        textAreaInputValueHandler(event){
          this.textAreaInputValue = event.target.value
        },
        addNewCandle() {
            this.candles.push(this.textAreaInputValue)
            this.textAreaInputValue = null
        }
    }
}

Vue.createApp(investViewer).mount('#app')

