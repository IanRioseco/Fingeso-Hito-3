import api from './api';


export default {
    // Métodos para gestionar horarios médicos

    //obtener todos los horarios
    obtenerTodos() {
        return api.get('/api/fichamedica/');
    },
    //obtener un horario por su id
    obtenerPorId(id) {
        return api.get(`/api/fichamedica/${id}`);
    },
    //crear un nuevo horario
    crear(fichamedica) {
        return api.post('/api/fichamedica/', fichamedica);
    },
    //actualizar un horario por su id
    actualizar(id, fichamedica) {
        return api.put(`/api/fichamedica/${id}`, fichamedica);
    },
    //eliminar un horario por su id
    eliminar(id) {
        return api.delete(`/api/fichamedica/${id}`);
    },

    obtenerPorIdPaciente(idPaciente) {
    return api.get(`/api/fichamedica/paciente/${idPaciente}`);
},

};