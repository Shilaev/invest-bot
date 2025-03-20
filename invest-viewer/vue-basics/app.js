const App = {
    data() {
        return {
            title: 'Список заметок:',
            addButtonLable: 'Добавить',
            placeholderTest: 'Текст заметки',
            inputValue: '',
            notes: []
        }
    },
    methods: {
        inputChangeHandler(event){
            this.inputValue = event.target.value
        },
        addNewNote(){
            this.notes.push(this.inputValue)
            this.inputValue = ''
        }
    }
}

Vue.createApp(App).mount('#app')