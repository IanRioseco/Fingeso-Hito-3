import api from './api';

export default {
    // Registrar un nuevo paciente
    async registerPatient(patientData) {
        //para debugging
        console.log('Datos recibidos en patient.service:', JSON.stringify(patientData, null, 2));
        console.log('Tipo de especialidad:', typeof patientData.especialidad);
        
        // Validaciones básicas
        // Si no hay datos del paciente error
        if (!patientData) {
            throw new Error('Los datos del paciente son requeridos');
        }
        // Si no hay especialidad o no es un objeto error
        if (!patientData.especialidad || !patientData.especialidad.id) {
            throw new Error('El especialidad es requerida y debe ser un objeto');
        }
        //try catch para capturar errores
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

            //para debugging
            console.log('Datos a enviar al servidor:', JSON.stringify(baseData, null, 2));
            
            const response = await api.post('/api/pacientes/', baseData);//llamada a la API para registrar el paciente
            return response.data;
            // Atrapar errores
        } catch (error) {
            console.error('Error en registerPatient:', error);// Para debugging
            // Si hay una respuesta de error del servidor, mostrar detalles
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
            const response = await api.get('/api/pacientes');//llamada a la API para obtener todos los pacientes
            console.log('Respuesta de getAllPatients:', response.data); // Para debugging
            return response.data;
            //atarpado de errores
        } catch (error) {
            console.error('Error en getAllPatients:', error);//debbuging
            if (error.response) {
                console.log('Detalles del error:', error.response.data);//debbugging
            }
            throw error;
        }
    },

    // Obtener un paciente por RUT
    async getPatientByRut(rut) {
        try {
            const response = await api.get(`/api/pacientes/${rut}`);//llamada a la API para obtener un paciente por RUT
            return response.data;
        } catch (error) {
            console.error('Error en getPatientByRut:', error);//debbugging
            throw error;
        }
    },

    // Actualizar un paciente
    async updatePatient(rut, patientData) {
        try {
            const response = await api.put(`/api/pacientes/${rut}`, patientData);//llamada a la API para actualizar un paciente
            return response.data;
        } catch (error) {
            console.error('Error en updatePatient:', error);//debbugging
            throw error;
        }
    },

    // Eliminar un paciente
    async deletePatient(rut) {
        try {
            const response = await api.delete(`/api/pacientes/${rut}`);//llamada a la API para eliminar un paciente
            return response.data;
        } catch (error) {
            console.error('Error en deletePatient:', error);//debbugging
            throw error;
        }
    }
};  