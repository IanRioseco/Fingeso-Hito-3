import api from './api';

export default {
    obtenerTodos() {
        return api.get('/administrador/');
    },

    obtenerPorId(id) {
        return api.get(`/administrador/id/${id}`);
    },

    crear(administrador) {
        return api.post('/administrador/', administrador);
    },

    actualizar(id, administrador) {
        return api.put(`/administrador/${id}`, administrador);
    },

    eliminar(id) {
        return api.delete(`/administrador/${id}`);
    },

    obtenerPorCorreo(correo) {
        return api.get(`/administrador/correo/${correo}`);
    }
}; 