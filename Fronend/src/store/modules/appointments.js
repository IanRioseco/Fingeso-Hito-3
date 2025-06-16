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
      // Devuelve los objetos tal cual, con fecha, horainicio y horafin
      return response.data.map(horario => ({
        id: horario.idHorario,
        fecha: horario.fecha,
        horainicio: typeof horario.horainicio === 'string' ? horario.horainicio : (horario.horainicio?.toString?.() || ''),
        horafin: typeof horario.horafin === 'string' ? horario.horafin : (horario.horafin?.toString?.() || ''),
        available: true
      }));
    },
    fetchUsers({ commit }) {
      // Aquí iría la lógica para obtener usuarios, por ejemplo desde una API
      // commit('setUsers', datos);
    },
    async createAppointment({ commit, rootGetters }, cita) {
      const appointmentService = (await import('@/services/appointmentService')).default;
      const paciente = rootGetters['auth/currentUser'];
      const usuario = paciente?.usuario || paciente;
      console.log('Paciente autenticado:', usuario);
      // Construir el payload exactamente como espera el backend
      const citaPayload = {
        estado: 'CitaAgendada',
        idMedico: cita.medicoId || cita.idMedico || cita.medico?.id || cita.doctorId,
        idPaciente: usuario?.id_paciente || usuario?.idPaciente || usuario?.id || usuario?.rutPa,
        idHorario: cita.idHorario // Debe venir del slot seleccionado
      };
      console.log('citaPayload', citaPayload);
      // Validar que todos los campos requeridos estén presentes
      if (!citaPayload.estado || !citaPayload.idMedico || !citaPayload.idPaciente || !citaPayload.idHorario) {
        return { success: false, error: 'Faltan datos requeridos para crear la cita.' };
      }
      try {
        const response = await appointmentService.crearCita(citaPayload);
        if (response && response.status === 201 && response.data) {
          return { success: true, data: response.data };
        } else {
          return { success: false, error: 'No se pudo crear la cita. Intenta nuevamente.' };
        }
      } catch (error) {
        const errorMsg = error.response?.data?.message || error.message || 'Error al crear la cita';
        return { success: false, error: errorMsg };
      }
    }
  },
  getters: {
    allUsers(state) {
      return state.users;
    }
  }
}