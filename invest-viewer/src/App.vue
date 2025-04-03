<template>
  <v-app>
    <!-- Верхняя панель -->
    <v-app-bar color="red">
      <v-app-bar-nav-icon @click="toggleAsideBar"></v-app-bar-nav-icon>
      <v-spacer/>
      <v-container class="d-flex align-center justify-end pr-3">
        <v-btn>Кнопка 1</v-btn>
        <v-btn>Кнопка 2</v-btn>
        <v-btn>Кнопка 3</v-btn>
        <v-avatar color="surface-variant" size="48"></v-avatar>
      </v-container>
    </v-app-bar>

    <!-- Боковое меню -->
    <v-navigation-drawer v-model="isAsideBarShowed" color="red">
      <v-list nav>
        <v-list-item title="Боковое меню" link></v-list-item>
        <v-list-item>
          <v-btn color="white" text="asdfasdf"></v-btn>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <!-- Основной контент -->
    <v-main>
      <v-container>
        <v-row>
          <v-col><h1 class="text-h2">Заголовок</h1></v-col>
        </v-row>

        <v-row>
          <v-col>
            <v-sheet elevation="2" rounded height="624">
              <the-chart/>
            </v-sheet>
          </v-col>

          <v-col>
            <v-row>
              <v-col>
                <v-sheet elevation="2" rounded height="300" color="blue"></v-sheet>
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <v-sheet elevation="2" rounded height="300" color="orange"></v-sheet>
              </v-col>
            </v-row>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import TheChart from '@/components/TheChart.vue'

export default {
  name: 'App',
  components: { TheChart },

  data () {
    return {
      isAsideBarShowed: false // Управляет отображением бокового меню
    }
  },

  methods: {
    toggleAsideBar () {
      this.isAsideBarShowed = !this.isAsideBarShowed // Переключает состояние
    }
  }
}
</script>
