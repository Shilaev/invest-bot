const InvestViewer = {
    data() {
        return {
            message: 'mes',
            inputData: '',
            response: null,
            error: null
        }
    },
    methods: {
        async sendData() {
            console.log(this.message)
        }
    }
}

Vue.createApp(InvestViewer).mount('#app');