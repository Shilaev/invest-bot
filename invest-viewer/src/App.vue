<template>
  <v-app>
    <!-- Верхняя панель -->
    <v-app-bar>
      <!-- Кнопка показать/скрыть левое меню -->
      <v-app-bar-nav-icon @click="toggleAsideBar"></v-app-bar-nav-icon>
      <v-spacer/>
      <!-- Управляющие кнопки -->
      <v-list>
        <v-list-item
            class="text-center"
            title="Кнопка"
            @click="handleClick"
            active-class="bg-primary text-white"
            link
        />
      </v-list>
      <v-avatar color="surface-variant" size="48" class="mr-5"></v-avatar>
    </v-app-bar>

    <!-- Боковое меню -->
    <v-navigation-drawer v-model="isAsideBarShowed">
      <v-list nav density="comfortable">
        <!-- Элементы меню -->
        <v-list-item
            class="text-center"
            title="Кнопка"
            @click="handleClick"
            active-class="bg-primary text-white"
            link
        />
      </v-list>
    </v-navigation-drawer>

    <!-- Основной контент -->
    <v-main>
      <v-container>
        <!-- Заголовок страницы -->
        <v-row>
          <v-col><h1 class="text-h2">Заголовок</h1></v-col>
        </v-row>

        <!-- Контент -->
        <v-row>
          <!-- Контент слева длинный -->
          <v-col>
            <v-sheet rounded height="624">
              <the-chart/>
            </v-sheet>
          </v-col>

          <!-- Контент справа -->
          <v-col>
            <!-- Контент справа верх -->
            <v-row>
              <v-col>
                <v-sheet elevation="2" rounded height="300" color="blue"></v-sheet>
              </v-col>
            </v-row>

            <!-- Контент справа низ -->
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
      isAsideBarShowed: false // Боковое меню открыто?
    }
  },

  methods: {
    toggleAsideBar () {
      this.isAsideBarShowed = !this.isAsideBarShowed // Переключает состояние открытости бокового меню
    },
    clickChecker () {
      console.log('check') // утилитарный метод, для проверки, что кнопка срабатывает нормально
    }
  }
}
</script>
