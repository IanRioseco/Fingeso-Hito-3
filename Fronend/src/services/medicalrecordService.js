import api from './api';

export default {
    // Métodos para gestionar fichas médicas

    //obtener todas las fichas médicas
    obtenerTodos() {
        return api.get('/api/fichamedica/');
    },
    //obtener una ficha médica por su id
    obtenerPorId(id) {
        return api.get(`/api/fichamedica/${id}`);
    },
    //crear una nueva ficha médica
    crear(fichamedica) {
        return api.post('/api/fichamedica/', fichamedica);
    },
    //actualizar una ficha médica por su id
    actualizar(id, fichamedica) {
        return api.put(`/api/fichamedica/${id}`, fichamedica);
    },
    //eliminar una ficha médica por su id
    eliminar(id) {
        return api.delete(`/api/fichamedica/${id}`);
    },
    //obtener ficha médica por id de paciente
    obtenerPorIdPaciente(idPaciente) {
        console.log('Buscando ficha médica para paciente:', idPaciente);
        // Si no hay ficha médica, crear una nueva
        return api.get(`/api/fichamedica/paciente/${idPaciente}`).catch(error => {
            if (error.response && error.response.status === 404) {
                console.log('No se encontró ficha médica, creando una nueva');
                // Crear una nueva ficha médica con datos por defecto
                const nuevaFicha = {
                    edad: 0,
                    sexo: '',
                    sintomas: '',
                    enfermedades: '',
                    peso: 0,
                    altura: 0,
                    paciente: {
                        rutPa: idPaciente
                    }
                };
                return this.crear(nuevaFicha);
            }
            throw error;
        });
    }
};