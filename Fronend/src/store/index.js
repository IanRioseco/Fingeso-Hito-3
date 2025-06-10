import { createStore } from 'vuex'
import auth from './modules/auth'
import users from './modules/users'
import appointments from './modules/appointments'
import patients from './modules/patients'

const store = createStore({
  modules: {
    auth,
    users,
    appointments,
    patients
  }
})

export default store