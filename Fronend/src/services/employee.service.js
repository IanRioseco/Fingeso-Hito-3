import api from './api';

export default {
    // Registrar un nuevo empleado
    async registerEmployee(employeeData) {
        console.log('Datos recibidos en employee.service:', JSON.stringify(employeeData, null, 2));
        console.log('Tipo de rol:', typeof employeeData.rol);
        
        if (!employeeData) {
            throw new Error('Los datos del empleado son requeridos');
        }

        if (!employeeData.rol || typeof employeeData.rol !== 'object') {
            throw new Error('El rol es requerido y debe ser un objeto');
        }

        if (!employeeData.rol.id_rol || !employeeData.rol.nombre) {
            throw new Error('El rol debe tener id_rol y nombre');
        }

        try {
            // Datos base que son comunes para todos los tipos de empleados
            const baseData = {
                rut: employeeData.rut,
                nombre: employeeData.nombre,
                apellido: employeeData.apellido,
                correo: employeeData.correo,
                telefono: employeeData.telefono,
                rol: employeeData.rol.nombre // Enviamos solo el nombre del rol
            };

            // Si hay datos adicionales específicos del rol, agregarlos
            if (employeeData.especialidad) {
                baseData.especialidad = employeeData.especialidad;
            }

            console.log('Datos a enviar al servidor:', JSON.stringify(baseData, null, 2));
            
            const response = await api.post('/api/employees', baseData);
            return response.data;
        } catch (error) {
            console.error('Error en registerEmployee:', error);
            if (error.response) {
                console.error('Detalles del error:', error.response.data);
            }
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