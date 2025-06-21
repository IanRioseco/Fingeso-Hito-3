import api from './api';

export default {
  // Metodos para gestionar citas medicas

  //funcion para crear una cita medica
  crearCita(cita) {
    // POST a /api/citamedica/crear
    return api.post('/api/citamedica/crear', cita);
  },
  //funcion para obtener todas las citas medicas
  obtenerCitas() {
    return api.get('/api/citamedica/');
  },
  //funcion para obtener una cita medica por su id
  obtenerCitaPorId(id) {
    return api.get(`/api/citamedica/${id}`);
  },
  //funcion para actualizar una cita medica por su id
  actualizarCita(id, cita) {
    return api.put(`/api/citamedica/${id}`, cita);
  },
  //funcion para eliminar una cita medica por su id
  eliminarCita(id) {
    console.log('ID recibido en appointmentService.eliminarCita:', id); // <-- agrega este log aquí
    return api.delete(`/api/citamedica/${id}`);
  },
  //funcion para obtener citas medicas por paciente
  obtenerCitasPorPaciente(idPaciente) {
    return api.get(`/api/citamedica/paciente/${idPaciente}`);
  },
  //funcion para obtener citas con horario por paciente
  obtenerCitasConHorarioPorPaciente(idPaciente) {
    return api.get(`/api/citamedica/paciente/${idPaciente}`);
  },

  obtenerCitaConHorarioPorMedico(idMedico) {
    return api.get(`/api/citamedica/medico/${idMedico}`);
  },


}
