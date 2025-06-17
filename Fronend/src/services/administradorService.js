import api from './api';

export default {
    //metodos para consultar datos de administradores

    //funcion para obtener todos los administradores
    obtenerTodos() {
        return api.get('/administrador/');
    },
    //funcion para obtener un administrador por su id
    obtenerPorId(id) {
        return api.get(`/administrador/id/${id}`);
    },
    //funcion para obtener un administrador por su correo
    crear(administrador) {
        return api.post('/administrador/', administrador);
    },
    //funcion para actualizar un administrador por su id
    actualizar(id, administrador) {
        return api.put(`/administrador/${id}`, administrador);
    },  
    //funcion para eliminar un administrador por su id
    eliminar(id) {
        return api.delete(`/administrador/${id}`);
    },
    //funcion para obtener un administrador por su correo
    obtenerPorCorreo(correo) {
        return api.get(`/administrador/correo/${correo}`);
    }
}; 