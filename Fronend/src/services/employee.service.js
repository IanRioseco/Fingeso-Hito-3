import api from './api';

export default {
    // Registrar un nuevo empleado
    async registerEmployee(employeeData) {
        if (!employeeData) {
            throw new Error('Los datos del empleado son requeridos');
        }

        if (!employeeData.rol) {
            throw new Error('El rol es requerido');
        }

        console.log('Datos recibidos en registerEmployee:', employeeData); // Para debugging

        try {
            const response = await api.post('/api/employees', employeeData);
            return response.data; // Devolver solo los datos de la respuesta
        } catch (error) {
            console.error('Error en registerEmployee:', error);
            throw error;
        }
    },

    // Obtener todos los empleados
    async getAllEmployees() {
        try {
            console.log('Obteniendo todos los empleados...'); // Para debugging
            const response = await api.get('/api/employees');
            console.log('Respuesta de getAllEmployees:', response.data); // Para debugging
            return response.data;
        } catch (error) {
            console.error('Error en getAllEmployees:', error);
            if (error.response) {
                console.log('Detalles del error:', error.response.data);
            }
            throw error;
        }
    },

    // Obtener un empleado por RUT
    async getEmployeeByRut(rut) {
        try {
            const response = await api.get(`/api/employees/${rut}`);
            return response.data;
        } catch (error) {
            console.error('Error en getEmployeeByRut:', error);
            throw error;
        }
    },

    // Actualizar un empleado
    async updateEmployee(rut, employeeData) {
        try {
            const response = await api.put(`/api/employees/${rut}`, employeeData);
            return response.data;
        } catch (error) {
            console.error('Error en updateEmployee:', error);
            throw error;
        }
    },

    // Eliminar un empleado
    async deleteEmployee(rut) {
        try {
            const response = await api.delete(`/api/employees/${rut}`);
            return response.data;
        } catch (error) {
            console.error('Error en deleteEmployee:', error);
            throw error;
        }
    }
}; 