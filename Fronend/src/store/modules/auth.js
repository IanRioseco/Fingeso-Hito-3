import AuthService from '@/services/auth.service.js'

const state = {
  user: null,
  token: null,
  isAuthenticated: false
}

const getters = {
  isAuthenticated: state => !!state.token,
  userRole: state => state.user ? state.user.role : null,
  currentUser: state => state.user
}

const mutations = {
  SET_USER(state, user) {
    state.user = user;
  },
  SET_TOKEN(state, token) {
    state.token = token;
  },
  loginSuccess(state, payload) {
    state.user = payload.user
    state.token = payload.token
    state.isAuthenticated = true
  }
}

const actions = {
  async login({ commit }, userData) {
    commit('SET_USER', userData.user);
    commit('SET_TOKEN', userData.token);
  },
  logout({ commit }) {
    commit('SET_USER', null);
    commit('SET_TOKEN', null);
    localStorage.removeItem('token');
  },
  async loginOld({ commit }, credentials) {
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