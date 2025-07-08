import medicoService from '@/services/medicoService'
import horarioService from '@/services/horarioService'

export default {
  // Modulo Vuex para gestionar citas médicas y médicos
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
    //funcion asincrona para obtener todos los medicos
    async fetchDoctors({ commit }, idEspecialidad) {
      const response = await medicoService.obtenerPorEspecialidad(idEspecialidad);// Llama al servicio para obtener médicos por especialidad
      // Transforma los datos aquí para que el frontend reciba las propiedades correctas
      //retorna un array de objetos con las propiedades requeridas
      return response.data.map(medico => ({
        id: medico.idmedico,
        firstName: medico.nombre,
        lastName: medico.apellido,
        specialty: medico.especialidad?.nombre || '',
        avatar: '', 
        rating: 5,  
        reviews: 0, 
        location: '' 
      }));
    },
    //funcion asincrona para obtener todos los medicos con sus horarios
    async fetchDoctorsWithAvailability({ dispatch }, idEspecialidad) {
      // Obtiene todos los médicos de la especialidad y sus horarios
      const doctors = await dispatch('fetchDoctors', idEspecialidad);
      // Para cada médico, obtiene sus horarios (puede ser vacío)
      
      //utiliza Promise.all para esperar a que todas las promesas se resuelvan
      const doctorsWithAvailability = await Promise.all(
        doctors.map(async doctor => {// Mapea cada medico para obtener su disponibilidad
          const horarios = await dispatch('fetchAvailability', { doctorId: doctor.id });// Llama a la acción para obtener la disponibilidad del médico
          // Retorna el médico con sus horarios
          return { ...doctor, availability: horarios };
        })
      );
      return doctorsWithAvailability;
    },
    //funcion asincrona para obtener la disponibilidad de un medico
    async fetchAvailability({ commit }, { doctorId }) {
      const response = await horarioService.fetchAvailableHorariosByMedico(doctorId);// Llama al servicio para obtener la disponibilidad del médico
      // Devuelve los objetos tal cual, con fecha, horainicio y horafin
      return response.data.map(horario => ({
        id: horario.idHorario,
        fecha: horario.fecha,
        horainicio: typeof horario.horainicio === 'string' ? horario.horainicio : (horario.horainicio?.toString?.() || ''),
        horafin: typeof horario.horafin === 'string' ? horario.horafin : (horario.horafin?.toString?.() || ''),
        available: true
      }));
    },
    //funcion asincrona para obtener todos los usuarios
    fetchUsers({ commit }) {
    },
    //funcion asincrona para crear una cita médica
    async createAppointment({ commit, rootGetters }, cita) {
      const appointmentService = (await import('@/services/appointmentService')).default;// Importa el servicio de citas
      const pacienteRaw = rootGetters['auth/currentUser'];// Obtiene el usuario autenticado
      console.log('APPOINTMENTS - Paciente raw autenticado:', pacienteRaw);//debugging
      
      if (!pacienteRaw) {
        console.error('APPOINTMENTS - No hay usuario autenticado');
        return { success: false, error: 'Usuario no autenticado.' };
      }
      
      // Usar la misma lógica robusta de extracción que en AppointmentScheduler
      let idPaciente = null;
      
      // Primero intentar con el objeto principal (ya procesado por el getter)
      idPaciente = pacienteRaw?.id_paciente || 
                  pacienteRaw?.idPaciente || 
                  pacienteRaw?.id || 
                  pacienteRaw?.rutPa || 
                  pacienteRaw?.rut;
      
      // Si no se encontró, intentar con el objeto usuario anidado
      if (!idPaciente && pacienteRaw?.usuario) {
        console.log('APPOINTMENTS - Intentando extraer ID del objeto usuario anidado');
        idPaciente = pacienteRaw.usuario.id_paciente || 
                    pacienteRaw.usuario.idPaciente || 
                    pacienteRaw.usuario.id || 
                    pacienteRaw.usuario.rutPa || 
                    pacienteRaw.usuario.rut;
      }
      
      // Intentos adicionales con más variaciones
      if (!idPaciente) {
        idPaciente = pacienteRaw?.pacienteId ||
                    pacienteRaw?.idpaciente ||
                    pacienteRaw?.ID ||
                    pacienteRaw?.Id;
      }
      
      console.log('APPOINTMENTS - Proceso de búsqueda de ID:', {
        desde_pacienteRaw: {
          id_paciente: pacienteRaw?.id_paciente,
          idPaciente: pacienteRaw?.idPaciente,
          id: pacienteRaw?.id,
          rutPa: pacienteRaw?.rutPa,
          rut: pacienteRaw?.rut
        },
        desde_usuario_anidado: pacienteRaw?.usuario ? {
          id_paciente: pacienteRaw.usuario.id_paciente,
          idPaciente: pacienteRaw.usuario.idPaciente,
          id: pacienteRaw.usuario.id,
          rutPa: pacienteRaw.usuario.rutPa,
          rut: pacienteRaw.usuario.rut
        } : 'No hay usuario anidado',
        idPaciente_final: idPaciente
      });
      
      console.log('APPOINTMENTS - ID del paciente encontrado para cita:', idPaciente);
      
      if (!idPaciente) {
        console.error('APPOINTMENTS - No se pudo extraer ID del paciente de:', pacienteRaw);
        return { success: false, error: 'No se encontró un ID de paciente válido.' };
      }
      
      // Construir el payload exactamente como espera el backend
      const citaPayload = {
        estado: cita.estado || 'CitaAgendada',
        idMedico: cita.medicoId || cita.idMedico || cita.medico?.id || cita.doctorId,
        idPaciente: idPaciente,
        idHorario: cita.idHorario, // Debe venir del slot seleccionado
        id_fichamedica: cita.id_fichamedica || cita.idFichaMedica || cita.idFichamedica || cita.IdFichamedica// Asegura que se incluya el campo correcto si viene del frontend
      };
      console.log('APPOINTMENTS - citaPayload FINAL', citaPayload);//debugging
      // Validar que todos los campos requeridos estén presentes
      if (!citaPayload.estado || !citaPayload.idMedico || !citaPayload.idPaciente || !citaPayload.idHorario || !citaPayload.id_fichamedica) {
        console.error('Faltan datos requeridos:', {
          estado: citaPayload.estado,
          idMedico: citaPayload.idMedico,
          idPaciente: citaPayload.idPaciente,
          idHorario: citaPayload.idHorario,
          id_fichamedica: citaPayload.id_fichamedica
        });
        return { success: false, error: 'Faltan datos requeridos para crear la cita.' };
      }
      //try catch para capturar errores
      try {
        const response = await appointmentService.crearCita(citaPayload);//llamada al servicio de citas
        // Verifica si la respuesta es exitosa y contiene datos
        if (response && response.status === 201 && response.data) {
          return { success: true, data: response.data };// Retorna la cita creada
        } else {
          return { success: false, error: 'No se pudo crear la cita. Intenta nuevamente.' };// Retorna un error si no se pudo crear la cita
        }
        //atrapado errores
      } catch (error) {
        const errorMsg = error.response?.data?.message || error.message || 'Error al crear la cita';// Obtiene el mensaje de error
        return { success: false, error: errorMsg };
      }
    }
  },
  //getters para acceder a los datos del estado
  getters: {
    allUsers(state) {
      return state.users;
    }
  }
}