import api from './api';


export default {
    obtenerTodos() {
        return api.get('/api/horario/');
    },

    obtenerPorId(id) {
        return api.get(`/api/horario/${id}`);
    },

    crear(horario) {
        return api.post('/api/horario/', horario);
    },

    actualizar(id, horario) {
        return api.put(`/api/horario/horario/${id}`, horario);
    },

    eliminar(id) {
        return api.delete(`/api/horario/horario/${id}`);
    },

    Obtenerdisponibilidadpormédico(idmedico) {
        return api.get(`/api/horario/horario/medico/${idmedico}`);
    }
};