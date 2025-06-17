import { authService } from '@/services/auth.service.js'

// Módulo de autenticación para Vuex

// Este modulo gestiona el estado de autenticación del usuario, incluyendo el token y los datos del usuario.
// Utiliza localStorage para persistir el estado entre recargas de página.
const state = {
  user: JSON.parse(localStorage.getItem('user')) || null,
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
    // Si el usuario es nulo, limpia el localStorage
    state.user = user;
    localStorage.setItem('user', JSON.stringify(user));
  },
  // Establece el token de autenticación y lo guarda en localStorage
  SET_TOKEN(state, token) {
    // Si el token es nulo, limpia el localStorage
    state.token = token;
    localStorage.setItem('token', token);
  },
  // Limpia el estado de autenticación y elimina los datos del localStorage
  loginSuccess(state, payload) {
    // Establece el usuario y el token en el estado
    state.user = payload.user;
    state.token = payload.token;
    state.isAuthenticated = true;
    // Guarda los datos del usuario y el token en localStorage
    localStorage.setItem('user', JSON.stringify(payload.user));
    localStorage.setItem('token', payload.token);
  }
}
// Acciones para manejar la lógica de negocio y llamadas a servicios externos
const actions = {
  async login({ commit }, userData) {
    //limpia el estado de autenticación antes de iniciar sesión
    commit('SET_USER', userData.user);
    commit('SET_TOKEN', userData.token);
  },
  // Accion para registrar un nuevo usuario
  logout({ commit }) {
    // Limpia el estado de autenticación
    commit('SET_USER', null);
    commit('SET_TOKEN', null);
    // Limpia el localStorage al cerrar sesión
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  },
  // Accion para iniciar sesión con un usuario existente
  async loginOld({ commit }, credentials) {
    // Llama al servicio de autenticación para iniciar sesión
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