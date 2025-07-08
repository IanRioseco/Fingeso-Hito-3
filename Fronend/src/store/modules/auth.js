import { authService } from '@/services/auth.service.js'

// Obtiene el usuario de localStorage de forma segura
function getUserFromStorage() {
  const userRaw = localStorage.getItem('user');
  console.log('AUTH MODULE - Usuario raw desde localStorage:', userRaw);
  
  if (userRaw && userRaw !== 'undefined' && userRaw !== 'null') {
    try {
      const parsedUser = JSON.parse(userRaw);
      console.log('AUTH MODULE - Usuario parseado desde localStorage:', parsedUser);
      console.log('AUTH MODULE - Campos disponibles en usuario parseado:', parsedUser ? Object.keys(parsedUser) : 'No hay datos');
      
      // Debug específico para IDs
      if (parsedUser) {
        console.log('AUTH MODULE - IDs detectados:', {
          id_paciente: parsedUser.id_paciente,
          idPaciente: parsedUser.idPaciente,
          id: parsedUser.id,
          rut: parsedUser.rut,
          rutPa: parsedUser.rutPa,
          usuario: parsedUser.usuario ? 'Objeto usuario presente' : 'No hay objeto usuario'
        });
      }
      
      return parsedUser;
    } catch (e) {
      console.error('AUTH MODULE - Error al parsear el usuario desde localStorage:', e);
      return null;
    }
  }
  
  console.log('AUTH MODULE - No hay usuario válido en localStorage');
  return null;
}

const state = {
  user: getUserFromStorage(),
  token: localStorage.getItem('token') || null,
  isAuthenticated: !!localStorage.getItem('token') && !!getUserFromStorage()
}

//getters para acceder a los datos del estado
const getters = {
  isAuthenticated: state => {
    const hasToken = !!state.token;
    const hasUser = !!state.user;
    const isAuth = state.isAuthenticated;
    console.log('AUTH GETTER - isAuthenticated check:', { hasToken, hasUser, isAuth });
    return hasToken && hasUser && isAuth;
  },
  userRole: state => state.user ? state.user.role : null,
  currentUser: state => {
    // Compatibilidad: soporta múltiples variaciones de campos de ID del paciente
    if (!state.user) {
      console.log('AUTH GETTER - No hay usuario en el state');
      return null;
    }
    
    console.log('AUTH GETTER - Usuario original:', state.user);
    console.log('AUTH GETTER - Campos disponibles:', Object.keys(state.user));
    
    // Verificar todas las posibles ubicaciones del ID del paciente
    const posibleIds = {
      id_paciente: state.user.id_paciente,
      idPaciente: state.user.idPaciente,
      id: state.user.id,
      rut: state.user.rut,
      rutPa: state.user.rutPa,
      usuario_id_paciente: state.user.usuario?.id_paciente,
      usuario_idPaciente: state.user.usuario?.idPaciente,
      usuario_id: state.user.usuario?.id,
      usuario_rut: state.user.usuario?.rut,
      usuario_rutPa: state.user.usuario?.rutPa
    };
    
    console.log('AUTH GETTER - IDs posibles encontrados:', posibleIds);
    
    // Si ya tiene id_paciente, devolverlo tal como está
    if (state.user.id_paciente) {
      console.log('AUTH GETTER - Usando id_paciente:', state.user.id_paciente);
      return state.user;
    }
    
    // Si tiene idPaciente, crear compatibilidad
    if (state.user.idPaciente) {
      console.log('AUTH GETTER - Usando idPaciente:', state.user.idPaciente);
      return { ...state.user, id_paciente: state.user.idPaciente };
    }
    
    // Si tiene id, asumir que es el ID del paciente
    if (state.user.id) {
      console.log('AUTH GETTER - Usando id:', state.user.id);
      return { ...state.user, id_paciente: state.user.id };
    }
    
    // Si tiene rut, usarlo como identificador
    if (state.user.rut) {
      console.log('AUTH GETTER - Usando rut:', state.user.rut);
      return { ...state.user, id_paciente: state.user.rut };
    }
    
    // Si tiene rutPa, usarlo como identificador
    if (state.user.rutPa) {
      console.log('AUTH GETTER - Usando rutPa:', state.user.rutPa);
      return { ...state.user, id_paciente: state.user.rutPa };
    }
    
    // Verificar si hay un objeto usuario anidado
    if (state.user.usuario) {
      console.log('AUTH GETTER - Verificando usuario anidado:', state.user.usuario);
      
      if (state.user.usuario.id_paciente) {
        console.log('AUTH GETTER - Usando usuario.id_paciente:', state.user.usuario.id_paciente);
        return { ...state.user, id_paciente: state.user.usuario.id_paciente };
      }
      
      if (state.user.usuario.idPaciente) {
        console.log('AUTH GETTER - Usando usuario.idPaciente:', state.user.usuario.idPaciente);
        return { ...state.user, id_paciente: state.user.usuario.idPaciente };
      }
      
      if (state.user.usuario.id) {
        console.log('AUTH GETTER - Usando usuario.id:', state.user.usuario.id);
        return { ...state.user, id_paciente: state.user.usuario.id };
      }
      
      if (state.user.usuario.rut) {
        console.log('AUTH GETTER - Usando usuario.rut:', state.user.usuario.rut);
        return { ...state.user, id_paciente: state.user.usuario.rut };
      }
      
      if (state.user.usuario.rutPa) {
        console.log('AUTH GETTER - Usando usuario.rutPa:', state.user.usuario.rutPa);
        return { ...state.user, id_paciente: state.user.usuario.rutPa };
      }
    }
    
    console.log('AUTH GETTER - No se encontró ID de paciente válido en ningún campo');
    console.log('AUTH GETTER - Estructura completa del usuario:', JSON.stringify(state.user, null, 2));
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
  SET_AUTHENTICATED(state, isAuth) {
    state.isAuthenticated = isAuth;
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
  initializeAuth({ commit }) {
    const user = getUserFromStorage();
    const token = localStorage.getItem('token');
    
    console.log('AUTH STORE - Inicializando auth:', { user, token });
    
    if (user && token) {
      commit('SET_USER', user);
      commit('SET_TOKEN', token);
      commit('SET_AUTHENTICATED', true);
      console.log('AUTH STORE - Auth inicializado exitosamente');
    } else {
      console.log('AUTH STORE - No hay datos de autenticación previos');
    }
  },
  async login({ commit }, userData) {
    console.log('AUTH STORE - Datos recibidos para login:', userData);
    commit('SET_USER', userData.user);
    commit('SET_TOKEN', userData.token);
    
    // Forzar actualización del estado de autenticación
    commit('SET_AUTHENTICATED', true);
    
    console.log('AUTH STORE - Usuario guardado en store:', userData.user);
  },
  logout({ commit }) {
    commit('SET_USER', null);
    commit('SET_TOKEN', null);
    commit('SET_AUTHENTICATED', false);
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