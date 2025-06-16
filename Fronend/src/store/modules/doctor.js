// src/store/doctor.js
import axios from 'axios'; // Descomenta cuando tengas la API
import horarioService from '@/services/horarioService';
import { startOfWeek, addDays, format } from 'date-fns';

function generateTimeBlocks(start, end, blockDuration = 30) {
  const blocks = [];
  let [startHour, startMin] = start.split(':').map(Number);
  let [endHour, endMin] = end.split(':').map(Number);
  let current = startHour * 60 + startMin;
  const endTime = endHour * 60 + endMin;
  while (current < endTime) {
    const hour = Math.floor(current / 60).toString().padStart(2, '0');
    const min = (current % 60).toString().padStart(2, '0');
    blocks.push(`${hour}:${min}`);
    current += blockDuration;
  }
  return blocks;
}

export default {
  namespaced: true,
  state: {
    availability: [],
    appointments: []
  },
  mutations: {
    setAvailability(state, availability) {
      state.availability = availability;
    },
    setAppointments(state, appointments) {
      state.appointments = appointments;
    }
  },
  actions: {
    async fetchAvailability({ commit }, { year, month } = {}) {
      const user = JSON.parse(localStorage.getItem('user'));
      const idMedico = user?.idmedico || user?.usuario?.idmedico;
      if (!idMedico) {
        commit('setAvailability', []);
        return;
      }
      const response = await horarioService.Obtenerdisponibilidadpormédico(idMedico);
      // Solo usar las fechas reales devueltas por el backend
      let availability = [];
      response.data.forEach(horario => {
        const fecha = typeof horario.fecha === 'string' ? horario.fecha : (horario.fecha?.toString?.() || '');
        let startStr = typeof horario.horainicio === 'string' ? horario.horainicio : (horario.horainicio?.toString?.() || '');
        let endStr = typeof horario.horafin === 'string' ? horario.horafin : (horario.horafin?.toString?.() || '');
        const blocks = generateTimeBlocks(startStr.substring(0,5), endStr.substring(0,5));
        blocks.forEach(time => {
          availability.push({
            id: horario.idHorario,
            date: fecha,
            time,
            available: true
          });
        });
      });
      commit('setAvailability', availability);
    },
    async fetchAppointments({ commit }, { start, end }) {
      // Cuando tengas la API, usa este código:
      /*
      const response = await axios.get(`/api/appointments?start=${start}&end=${end}`);
      commit('setAppointments', response.data);
      return;
      */

      // Datos simulados mientras no hay API:
      const fakeAppointments = [
        // { date: start, time: "10:00", patientName: "Juan", type: "Consulta" }
        // ...agrega más si lo necesitas
      ];
      commit('setAppointments', fakeAppointments);
    },
    async saveAvailability({ dispatch }, newAvailability) {
      await dispatch('fetchAvailability');
    },
    async eliminarDisponibilidad({ dispatch }, id) {
      await horarioService.eliminar(id);
      await dispatch('fetchAvailability');
    },
    async updateAppointment({ dispatch }, updatedAppointment) {
      // Cuando tengas la API, usa este código:
      /*
      await axios.put(`/api/appointments/${updatedAppointment.id}`, updatedAppointment);
      await dispatch('fetchAppointments', { start: updatedAppointment.date, end: updatedAppointment.date });
      return;
      */

      // Simulación: solo recarga los datos
      await dispatch('fetchAppointments', { start: updatedAppointment.date, end: updatedAppointment.date });
    },
    async cancelAppointment({ dispatch }, appointmentId) {
      // Cuando tengas la API, usa este código:
      /*
      await axios.delete(`/api/appointments/${appointmentId}`);
      await dispatch('fetchAppointments', { start: "", end: "" });
      return;
      */

      // Simulación: solo recarga los datos
      await dispatch('fetchAppointments', { start: "", end: "" });
    }
  }
}
