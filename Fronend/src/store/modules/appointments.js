import medicoService from '@/services/medicoService'
import horarioService from '@/services/horarioService'

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
    async fetchDoctors({ commit }, idEspecialidad) {
      const response = await medicoService.obtenerPorEspecialidad(idEspecialidad);
      // Transforma los datos aquí para que el frontend reciba las propiedades correctas
      return response.data.map(medico => ({
        id: medico.idmedico,
        firstName: medico.nombre,
        lastName: medico.apellido,
        specialty: medico.especialidad?.nombre || '',
        avatar: '', // Puedes poner una imagen por defecto o traerla si existe
        rating: 5,  // Valor por defecto o real si existe
        reviews: 0, // Valor por defecto o real si existe
        location: '' // Valor por defecto o real si existe
      }));
    },
    async fetchDoctorsWithAvailability({ dispatch }, idEspecialidad) {
      // Obtiene todos los médicos de la especialidad y sus horarios
      const doctors = await dispatch('fetchDoctors', idEspecialidad);
      // Para cada médico, obtiene sus horarios (puede ser vacío)
      const doctorsWithAvailability = await Promise.all(
        doctors.map(async doctor => {
          const horarios = await dispatch('fetchAvailability', { doctorId: doctor.id });
          return { ...doctor, availability: horarios };
        })
      );
      return doctorsWithAvailability;
    },
    async fetchAvailability({ commit }, { doctorId }) {
      const response = await horarioService.Obtenerdisponibilidadpormédico(doctorId);
      // Transforma los horarios a un formato útil para el frontend
      // Por ejemplo: [{ date: '2025-06-15', slots: 3, available: true }, ...]
      return response.data.map(horario => {
        // Asegura que start y end sean strings
        let start = horario.horainicio;
        let end = horario.horafin;
        if (typeof start !== 'string') start = start?.toString?.() || '';
        if (typeof end !== 'string') end = end?.toString?.() || '';
        return {
          id: horario.idHorario,
          dayOfWeek: horario.dia, // 1=Lunes, 7=Domingo
          start,
          end,
          // Puedes agregar más campos si lo necesitas
          available: true,
          // Si quieres, puedes calcular la fecha real usando dayjs/date-fns
        };
      });
    },
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