import AuthService from '@/services/auth.service'

const state = {
  user: null,
  token: null,
  isAuthenticated: false
}

const getters = {
  isAuthenticated: state => state.isAuthenticated,
  userRole: state => state.user?.role || null,
  currentUser: state => state.user
}

const mutations = {
  loginSuccess(state, payload) {
    state.user = payload.user
    state.token = payload.token
    state.isAuthenticated = true
  }
}

const actions = {
  async login({ commit }, credentials) {
    const response = await AuthService.login(credentials)
    commit('loginSuccess', response)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}