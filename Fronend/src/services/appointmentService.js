import api from './api';

export default {
  crearCita(cita) {
    // POST a /api/citamedica/
    return api.post('/api/citamedica/', cita);
  },
  obtenerCitas() {
    return api.get('/api/citamedica/');
  },
  obtenerCitaPorId(id) {
    return api.get(`/api/citamedica/${id}`);
  },
  actualizarCita(id, cita) {
    return api.put(`/api/citamedica/${id}`, cita);
  },
  eliminarCita(id) {
    return api.delete(`/api/citamedica/${id}`);
  }
};
