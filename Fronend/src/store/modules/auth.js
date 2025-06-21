import { authService } from '@/services/auth.service.js'

// Obtiene el usuario de localStorage de forma segura
function getUserFromStorage() {
  const userRaw = localStorage.getItem('user');
  if (userRaw && userRaw !== 'undefined') {
    try {
      return JSON.parse(userRaw);
    } catch (e) {
      console.error('Error al parsear el usuario desde localStorage:', e);
      return null;
    }
  }
  return null;
}

const state = {
  user: getUserFromStorage(),
  token: localStorage.getItem('token') || null,
  isAuthenticated: false
}

//getters para acceder a los datos del estado
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

// Mutations y acciones para gestionar el estado de autenticación
const mutations = {
  SET_USER(state, user) {
    state.user = user;
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    } else {
      localStorage.removeItem('user');
    }
  },
  SET_TOKEN(state, token) {
    state.token = token;
    if (token) {
      localStorage.setItem('token', token);
    } else {
      localStorage.removeItem('token');
    }
  },
  loginSuccess(state, payload) {
    state.user = payload.user;
    state.token = payload.token;
    state.isAuthenticated = true;
    localStorage.setItem('user', JSON.stringify(payload.user));
    localStorage.setItem('token', payload.token);
  }
}

// Acciones para manejar la lógica de negocio y llamadas a servicios externos
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

// Exporta el módulo de autenticación para ser utilizado en la tienda Vuex
export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}