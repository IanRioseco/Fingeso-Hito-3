export default {
  namespaced: true,
  state: () => ({
    users: []
  }),
  mutations: {
    setUsers(state, users) {
      state.users = users;
    }
  },
  actions: {
    fetchUsers({ commit }) {
      // Aquí iría la lógica para obtener usuarios, por ejemplo desde una API
      // commit('setUsers', datos);
    }
  },
  getters: {
    allUsers(state) {
      return state.users;
    }
  }
}