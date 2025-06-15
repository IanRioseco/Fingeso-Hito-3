import api from './api';

export default {
    obtenerTodos() {
        return api.get('/medicos/');
    },

    obtenerPorId(id) {
        return api.get(`/medicos/id/${id}`);
    },

    obtenerPorEspecialidad(idEspecialidad) {
    return api.get(`/api/medicos/especialidad/${idEspecialidad}`);
    },

    crear(medico) {
        return api.post('/medicos/', medico);
    },

    actualizar(id, medico) {
        return api.put(`/medicos/${id}`, medico);
    },

    eliminar(id) {
        return api.delete(`/medicos/${id}`);
    }

};