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
            title="AxiosBtn"
            @click="handleAxiosBtnClick"
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
              {{ handleAxiosBtnClickResponseData }}
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
import { ref } from 'vue'
import axios from 'axios'

export default {
  name: 'App',
  setup () {
    const isAsideBarShowed = ref(false) // Боковое меню открыто?
    const handleAxiosBtnClickResponseData = ref(null)

    const toggleAsideBar = () => {
      isAsideBarShowed.value = !isAsideBarShowed.value // Переключает состояние открытости бокового меню
    }

    const handleAxiosBtnClick = async () => {
      try {
        const response = await axios.post('http://localhost:9981/statistics/api/find-instrument', {
          query: 'ROSN',
          instrumentType: 'INSTRUMENT_TYPE_SHARE',
          apiTradeAvailableFlag: 'true'
        })
        handleAxiosBtnClickResponseData.value = response.data
        console.log('Response: ', response.data)
      } catch (error) {
        console.error('Error: ', error)
      }
    }
    return {
      isAsideBarShowed,
      toggleAsideBar,
      handleAxiosBtnClick,
      handleAxiosBtnClickResponseData
    }
  }
}
</script>
