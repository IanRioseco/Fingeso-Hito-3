import './assets/css/main.css'
import '@fortawesome/fontawesome-free/css/all.css'
import axios from 'axios'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // Asegúrate de que este archivo exista
import store from './store'


// importaciones de Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import {aliases, mdi} from 'vuetify/iconsets/mdi'
import '@mdi/font/css/materialdesignicons.css'
// configuración de Vuetify
const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi,
    },
  },
  theme: {
    defaultTheme: 'light',
    themes:{
      light:{
        dark: false,
        colors: {
          primary: '#1976D2',
          secondary: '#424242',
          accent: '#82B1FF',
          error: '#FF5252',
          info: '#2196F3',
          success: '#4CAF50',
          warning: '#FFC107',
          background: '#FFFFFF',
          surface: '#F5F5F5',
          onPrimary: '#FFFFFF',
          onSecondary: '#000000',
          onBackground: '#000000',
          onSurface: '#000000'
        }
      }
    }
  },
  defaults:{
    Vcard:{
      rounded: 'lg',
    },
    Vbtn:{
      rounded: 'lg',
      fontweight: '500',
    },
    VtextField:{
      variant: 'outlined',
      rounded: 'lg',
      density: 'comfortable',
      color: 'primary'
    },
    Vselect:{
      variant: 'outlined',
      rounded: 'lg',
      density: 'comfortable',
      color: 'primary'
    },
  },
});


//configuración de axios
axios.defaults.baseURL = 'http://localhost:5432/api' // Cambia la URL según tu configuración
axios.defaults.headers.common['content-type'] = 'application/json'

// aplicacion vue
createApp(App)
  .use(router)
  .use(store)
  .use(vuetify) // Asegúrate de que store esté importado correctamente
  .mount('#app')
