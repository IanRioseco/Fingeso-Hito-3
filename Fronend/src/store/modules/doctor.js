// src/store/doctor.js
// import axios from 'axios'; // Descomenta cuando tengas la API

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
    async fetchAvailability({ commit }, { start, end }) {
      // Cuando tengas la API, usa este código:
      /*
      const response = await axios.get(`/api/availability?start=${start}&end=${end}`);
      commit('setAvailability', response.data);
      return;
      */

      // Datos simulados mientras no hay API:
      const fakeAvailability = [
        { date: start, time: "08:00", available: false },
        { date: start, time: "09:00", available: false }
        // ...agrega más si lo necesitas
      ];
      commit('setAvailability', fakeAvailability);
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
      // Cuando tengas la API, usa este código:
      /*
      await axios.post('/api/availability', newAvailability);
      await dispatch('fetchAvailability', { start: newAvailability[0].date, end: newAvailability[0].date });
      return;
      */

      // Simulación: solo recarga los datos
      await dispatch('fetchAvailability', { start: newAvailability[0].date, end: newAvailability[0].date });
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
