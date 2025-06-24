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
    return api.get(`/api/fichamedica/paciente/${idPaciente}`).catch(async error => {
        if (error.response && error.response.status === 404) {
            console.log('No se encontró ficha médica, creando una nueva');
            // Obtener datos del paciente para la fecha de nacimiento
            const pacienteResp = await api.get(`/api/paciente/${idPaciente}`);
            const paciente = pacienteResp.data;
            console.debug('Datos del paciente obtenidos:', paciente); // <-- DEBUG
            // Calcular edad
            let edad = 0;
            if (paciente.fechanacimiento) {
                console.debug('Fecha de nacimiento recibida:', paciente.fechanacimiento); // <-- DEBUG
                const [anio, mes, dia] = paciente.fechanacimiento.split('-').map(Number);
                const nacimiento = new Date(anio, mes - 1, dia);
                const hoy = new Date();
                edad = hoy.getFullYear() - nacimiento.getFullYear();
                const m = hoy.getMonth() - nacimiento.getMonth();
                if (m < 0 || (m === 0 && hoy.getDate() < nacimiento.getDate())) {
                    edad--;
                }
                console.debug('Edad calculada:', edad); // <-- DEBUG
            }
            // Crear una nueva ficha médica con la edad calculada
            const nuevaFicha = {
                edad,
                sexo: paciente.sexo || '',
                sintomas: '',
                enfermedades: '',
                peso: 0,
                altura: 0,
                paciente: {
                    rutPa: idPaciente
                }
            };
            console.debug('Ficha médica a crear:', nuevaFicha); // <-- DEBUG
            return this.crear(nuevaFicha);
        }
        throw error;
    });
    }
};