export default {
  namespaced: true,
  // Estado del módulo Vuex para gestionar los usuarios
  state: () => ({
    users: []
  }),
  mutations: {
    // Establece los usuarios en el estado
    setUsers(state, users) {
      state.users = users;
    }
  },
  //accions para gestionar los usuarios
  actions: {
    // Función para obtener todos los usuarios
    fetchUsers({ commit }) {
    }
  },
  //getters para obtener los usuarios
  getters: {
    // Obtiene todos los usuarios del estado
    allUsers(state) {
      return state.users;
    }
  }
}