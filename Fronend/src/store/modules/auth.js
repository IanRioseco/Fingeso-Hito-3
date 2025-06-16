import { authService } from '@/services/auth.service.js'

const state = {
  user: JSON.parse(localStorage.getItem('user')) || null,
  token: localStorage.getItem('token') || null,
  isAuthenticated: false
}

const getters = {
  isAuthenticated: state => !!state.token,
  userRole: state => state.user ? state.user.role : null,
  currentUser: state => {
    // Compatibilidad: soporta id_paciente, idPaciente o id
    if (!state.user) return null;
    if (state.user.id_paciente) return state.user;
    if (state.user.idPaciente) return { ...state.user, id_paciente: state.user.idPaciente };
    if (state.user.id) return { ...state.user, id_paciente: state.user.id };
    return state.user;
  }
}

const mutations = {
  SET_USER(state, user) {
    state.user = user;
    localStorage.setItem('user', JSON.stringify(user));
  },
  SET_TOKEN(state, token) {
    state.token = token;
    localStorage.setItem('token', token);
  },
  loginSuccess(state, payload) {
    state.user = payload.user;
    state.token = payload.token;
    state.isAuthenticated = true;
    localStorage.setItem('user', JSON.stringify(payload.user));
    localStorage.setItem('token', payload.token);
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
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  },
  async loginOld({ commit }, credentials) {
    const response = await authService.login(credentials.rut, credentials.password, credentials.role)
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