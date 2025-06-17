import api from './api';

export default {
    // Método genérico para obtener todos los registros de una entidad
    getAll(endpoint) {
        return api.get(`/${endpoint}/`);
    },

    // Método genérico para obtener un registro por ID
    getById(endpoint, id) {
        return api.get(`/${endpoint}/id/${id}`);
    },

    // Método genérico para crear un nuevo registro
    create(endpoint, data) {
        return api.post(`/${endpoint}/`, data);
    },

    // Método genérico para actualizar un registro
    update(endpoint, id, data) {
        return api.put(`/${endpoint}/${id}`, data);
    },

    // Método genérico para eliminar un registro
    delete(endpoint, id) {
        return api.delete(`/${endpoint}/${id}`);
    }
}; 