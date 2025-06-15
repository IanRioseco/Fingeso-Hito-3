import api from './api';

export default {
    // Registrar un nuevo paciente
    async registerPatient(patientData) {
        console.log('Datos recibidos en patient.service:', JSON.stringify(patientData, null, 2));
        console.log('Tipo de especialidad:', typeof patientData.especialidad);
        
        if (!patientData) {
            throw new Error('Los datos del paciente son requeridos');
        }

        if (!patientData.especialidad || !patientData.especialidad.id) {
            throw new Error('El especialidad es requerida y debe ser un objeto');
        }

        try {
            // Datos base que son comunes para todos los tipos de pacientes
            const baseData = {
                rut: patientData.rut,
                nombre: patientData.nombre,
                apellido: patientData.apellido,
                correo: patientData.correo,
                telefono: patientData.telefono,
                especialidad: patientData.especialidad.nombre // Enviamos solo el nombre del especialidad
            };

            // Si hay datos adicionales específicos del especialidad, agregarlos
            if (patientData.especialidad) {
                baseData.especialidad = patientData.especialidad;
            }

            console.log('Datos a enviar al servidor:', JSON.stringify(baseData, null, 2));
            
            const response = await api.post('/api/pacientes/', baseData);
            return response.data;
        } catch (error) {
            console.error('Error en registerPatient:', error);
            if (error.response) {
                console.error('Detalles del error:', error.response.data);
            }
            throw error;
        }
    },

    // Obtener todos los pacientes
    async getAllPatients() {
        try {
            console.log('Obteniendo todos los pacientes...'); // Para debugging
            const response = await api.get('/api/pacientes');
            console.log('Respuesta de getAllPatients:', response.data); // Para debugging
            return response.data;
        } catch (error) {
            console.error('Error en getAllPatients:', error);
            if (error.response) {
                console.log('Detalles del error:', error.response.data);
            }
            throw error;
        }
    },

    // Obtener un paciente por RUT
    async getPatientByRut(rut) {
        try {
            const response = await api.get(`/api/pacientes/${rut}`);
            return response.data;
        } catch (error) {
            console.error('Error en getPatientByRut:', error);
            throw error;
        }
    },

    // Actualizar un paciente
    async updatePatient(rut, patientData) {
        try {
            const response = await api.put(`/api/pacientes/${rut}`, patientData);
            return response.data;
        } catch (error) {
            console.error('Error en updatePatient:', error);
            throw error;
        }
    },

    // Eliminar un paciente
    async deletePatient(rut) {
        try {
            const response = await api.delete(`/api/pacientes/${rut}`);
            return response.data;
        } catch (error) {
            console.error('Error en deletePatient:', error);
            throw error;
        }
    }
};  