import api from './api';

export default {
    // Métodos para gestionar médicos

    //obtener todos los médicos
    obtenerTodos() {
        return api.get('/medicos/');
    },
    //obtener un médico por su id
    obtenerPorId(id) {
        return api.get(`api/medicos/id/${id}`);
    },
    //obtener médicos por especialidad
    obtenerPorEspecialidad(idEspecialidad) {
    return api.get(`/api/medicos/especialidad/${idEspecialidad}`);
    },
    //crear un nuevo médico
    crear(medico) {
        return api.post('/medicos/', medico);
    },
    //actualizar un médico por su id
    actualizar(id, medico) {
        return api.put(`api/medicos/${id}`, medico);
    },
    //eliminar un médico por su id
    eliminar(id) {
        return api.delete(`/medicos/${id}`);
    },

    saveAvailability(bloques) {
        // Suponiendo que usas axios
    return api.post('/api/horario/crear-multiples', bloques);
}

};