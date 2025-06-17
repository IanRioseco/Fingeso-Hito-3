import api from './api';

export default {
    // Metodos para gestionar empleados

    // Registrar un nuevo empleado
    async registerEmployee(employeeData) {
        //para debbugging
        console.log('Datos recibidos en employee.service:', JSON.stringify(employeeData, null, 2));
        console.log('Tipo de rol:', typeof employeeData.rol);
        
        // Validaciones básicas
        //si no hay datos del empleado error
        if (!employeeData) {
            throw new Error('Los datos del empleado son requeridos');
        }
        //si no hay rol error
        if (!employeeData.rol || typeof employeeData.rol !== 'object') {
            throw new Error('El rol es requerido y debe ser un objeto');
        }
        //si no hay rut error
        if (!employeeData.rol.id_rol || !employeeData.rol.nombre) {
            throw new Error('El rol debe tener id_rol y nombre');
        }
        //try catch para capturar errores
        try {
            // Datos base que son comunes para todos los tipos de empleados
            const baseData = {
                rut: employeeData.rut.replaceAll("[.-]", ""), 
                nombre: employeeData.nombre,
                apellido: employeeData.apellido,
                correo: employeeData.correo,
                telefono: employeeData.telefono,
                rol: employeeData.rol.nombre
                    .normalize("NFD")
                    .replace(/[\u0300-\u036f]/g, "") // quitar tildes
                    .replace(/\s+/g, "") // quitar espacios
                    .toLowerCase() // minúsculas
                ,password: employeeData.password
            };

            // Si hay datos adicionales especificos del rol, agregarlos
            if (employeeData.especialidad) {
                baseData.especialidad = employeeData.especialidad;
            }
<<<<<<< Updated upstream
            // Si el empleado es farmacéutico y tiene farmaciaId, agregarlo
            if (employeeData.farmaciaId) {
                baseData.farmaciaId = employeeData.farmaciaId;
            }

=======
            //para debbugging
>>>>>>> Stashed changes
            console.log('Datos a enviar al servidor:', JSON.stringify(baseData, null, 2));
            
            //envio de solicitud post al servidor
            const response = await api.post('/api/employees', baseData);
            return response.data;
            
            //atrapar errores
        } catch (error) {
            console.error('Error en registerEmployee:', error);//para debugging
            if (error.response) {
<<<<<<< Updated upstream
                console.error('Detalles del error:', error.response.data);
                // Mostrar el error del backend en un alert para depuración rápida
                alert('Error backend: ' + JSON.stringify(error.response.data));
            } else {
                alert('Error desconocido al registrar empleado');
=======
                console.error('Detalles del error:', error.response.data);//para debugging
>>>>>>> Stashed changes
            }
            throw error;
        }
    },

    // Obtener todos los empleados
    async getAllEmployees() {
        try {
            console.log('Obteniendo todos los empleados...'); //para debugging
            const response = await api.get('/api/employees');//llamada a la API para obtener todos los empleados
            console.log('Respuesta de getAllEmployees:', response.data); //para debugging
            return response.data;
            //atrapar errores
        } catch (error) {
            console.error('Error en getAllEmployees:', error);//para debugging
            if (error.response) {
                console.log('Detalles del error:', error.response.data);//para debugging
            }
            throw error;
        }
    },

    //obtener un empleado por RUT
    async getEmployeeByRut(rut) {
        try {
            const response = await api.get(`/api/employees/${rut}`);//llamada a la API para obtener el empleado
            return response.data;
            // atrapamos errores
        } catch (error) {
            console.error('Error en getEmployeeByRut:', error);//para debugging
            throw error;
        }
    },

    // Actualizar un empleado
    async updateEmployee(rut, employeeData) {
        try {
            const response = await api.put(`/api/employees/${rut}`, employeeData);//llamada a la API para actualizar el empleado
            return response.data;
            //atrapado de error
        } catch (error) {
            console.error('Error en updateEmployee:', error);//para debugging
            throw error;
        }
    },

    // Eliminar un empleado
    async deleteEmployee(rut) {
        try {
            const response = await api.delete(`/api/employees/${rut}`);//llamada a la API para eliminar el empleado
            return response.data;
            // atrapamos errores
        } catch (error) {
            console.error('Error en deleteEmployee:', error);//para debugging
            throw error;
        }
    }
};