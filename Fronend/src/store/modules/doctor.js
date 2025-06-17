// src/store/doctor.js
import axios from 'axios'; // Descomenta cuando tengas la API
import horarioService from '@/services/horarioService';
import { startOfWeek, addDays, format } from 'date-fns';

// Genera bloques de tiempo de 30 minutos entre dos horas dadas
function generateTimeBlocks(start, end, blockDuration = 30) {
  const blocks = [];
  let [startHour, startMin] = start.split(':').map(Number);//convertir la hora de inicio a números
  let [endHour, endMin] = end.split(':').map(Number);//convertir la hora de fin a números
  let current = startHour * 60 + startMin;//la hora actual en minutos
  const endTime = endHour * 60 + endMin;//la hora de fin en minutos
  //mientras la hora actual sea menor que la hora de fin, agrega bloques de tiempo
  while (current < endTime) {
    const hour = Math.floor(current / 60).toString().padStart(2, '0');//obtener la hora en formato de dos dígitos
    const min = (current % 60).toString().padStart(2, '0');//obtener los minutos en formato de dos dígitos
    blocks.push(`${hour}:${min}`);//agregar el bloque de tiempo al array
    current += blockDuration;
  }
  return blocks;
}
//exporta el módulo Vuex para gestionar la disponibilidad y citas de los médicos
export default {
  namespaced: true,
  state: {
    availability: [],
    appointments: []
  },
  //mutationes para actualizar los datos del estado
  mutations: {
    // Establece la disponibilidad del médico
    setAvailability(state, availability) {
      state.availability = availability;
    },
    // Establece las citas médicas
    setAppointments(state, appointments) {
      state.appointments = appointments;
    }
  },
  //acciones para gestionar la disponibilidad y citas médicas
  actions: {
    // Obtiene la disponibilidad del médico para un mes y año específicos
    async fetchAvailability({ commit }, { year, month } = {}) {
      const user = JSON.parse(localStorage.getItem('user'));// Obtiene el usuario actual del localStorage
      const idMedico = user?.idmedico || user?.usuario?.idmedico;// Obtiene el id del medico
      if (!idMedico) {
        commit('setAvailability', []);// Si no hay id del medico, limpia la disponibilidad
        return;
      }
      //obtiene la disponibilidad del médico
      const response = await horarioService.Obtenerdisponibilidadpormédico(idMedico);
      // Solo usar las fechas reales devueltas por el backend 
      let availability = [];
      // Recorre cada horario y genera bloques de tiempo de 30 minutos
      response.data.forEach(horario => {
        // define la fecha, hora de inicio y hora de fin
        const fecha = typeof horario.fecha === 'string' ? horario.fecha : (horario.fecha?.toString?.() || '');
        // convertir la hora de inicio y la hora de fin a números
        let startStr = typeof horario.horainicio === 'string' ? horario.horainicio : (horario.horainicio?.toString?.() || '');
        // convertir la hora de fin a números
        let endStr = typeof horario.horafin === 'string' ? horario.horafin : (horario.horafin?.toString?.() || '');
        // genera bloques de tiempo de 30 minutos
        const blocks = generateTimeBlocks(startStr.substring(0,5), endStr.substring(0,5));
        // Agrega cada bloque de tiempo a la disponibilidad
        blocks.forEach(time => {
          availability.push({
            id: horario.idHorario,
            date: fecha,
            time,
            available: true
          });
        });
      });
      commit('setAvailability', availability);// Actualiza la disponibilidad
    },

    //función para obtener las citas médicas en un rango de fechas
    async fetchAppointments({ commit }, { start, end }) {
      // Datos simulados mientras no hay API:
      const fakeAppointments = [
      ];  
      commit('setAppointments', fakeAppointments);
    },

    //función para obtener los médicos por especialidad
    async saveAvailability({ dispatch }, newAvailability) {
      await dispatch('fetchAvailability');
    },

    //función para eliminar la disponibilidad de un médico
    async eliminarDisponibilidad({ dispatch }, id) {
      await horarioService.eliminar(id);
      await dispatch('fetchAvailability');
    },

    //función para crear una nueva cita médica
    async updateAppointment({ dispatch }, updatedAppointment) {
      // Simulación: solo recarga los datos
      await dispatch('fetchAppointments', { start: updatedAppointment.date, end: updatedAppointment.date });
    },
    
    //función para cancelar una cita médica
    async cancelAppointment({ dispatch }, appointmentId) {
      // Simulación: solo recarga los datos
      await dispatch('fetchAppointments', { start: "", end: "" });
    }
  }
}
