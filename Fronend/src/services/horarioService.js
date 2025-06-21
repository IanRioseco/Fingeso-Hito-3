import api from './api';


export default {
    // Métodos para gestionar horarios médicos

    //obtener todos los horarios
    obtenerTodos() {
        return api.get('/api/horario/');
    },
    //obtener un horario por su id
    obtenerPorId(id) {
        return api.get(`/api/horario/${id}`);
    },
    //crear un nuevo horario
    crear(horario) {
        return api.post('/api/horario/', horario);
    },
    //actualizar un horario por su id
    actualizar(id, horario) {
        return api.put(`/api/horario/horario/${id}`, horario);
    },
    //eliminar un horario por su id
    eliminar(id) {
        return api.delete(`/api/horario/horario/${id}`);
    },
    //obtener horarios por médico
    Obtenerdisponibilidadpormédico(idmedico) {
        return api.get(`/api/horario/horario/medico/${idmedico}`);
    },

    fetchAvailableHorariosByMedico(doctorId) {
    return api.get(`/api/horario/disponibles/medico/${doctorId}`);
  },
};