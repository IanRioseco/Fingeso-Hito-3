import axios from 'axios';

const API_URL = import.meta.env.VITE_BASE_URL || 'http://localhost:8080';

export const authService = {
    //metodo para iniciar sesión
    login(rut, password, role) {
        return axios.post(API_URL + '/api/auth/login', {
            rut,
            password,
            role
        }).then(response => {
            console.log('AUTH SERVICE - Respuesta completa del login:', response.data);
            console.log('AUTH SERVICE - Estructura del usuario:', {
                keys: response.data ? Object.keys(response.data) : 'No hay datos',
                id: response.data?.id,
                idPaciente: response.data?.idPaciente,
                id_paciente: response.data?.id_paciente,
                rut: response.data?.rut,
                rutPa: response.data?.rutPa,
                usuario: response.data?.usuario ? Object.keys(response.data.usuario) : 'No hay usuario anidado'
            });
            
            if (response.data) {
                console.log('AUTH SERVICE - Guardando usuario en localStorage:', JSON.stringify(response.data, null, 2));
                localStorage.setItem('user', JSON.stringify(response.data));
                
                // Debug adicional: verificar qué se guardó realmente
                const savedUser = localStorage.getItem('user');
                console.log('AUTH SERVICE - Usuario guardado verificado:', savedUser);
            }
            return response.data;
        });
    },
    //metodo para registrar un usuario
    register(userData) {
        // Transformar los datos para que coincidan con el backend
        const pacienteData = {
            nombrePa: userData.nombrePa,
            apellidoPa: userData.apellidoPa,
            rut: userData.rut,
            fechanacimiento: userData.fechanacimiento,
            sexo: userData.sexo,
            correo: userData.correo,
            telefono: userData.telefono ? userData.telefono.toString() : '',
            direccion: userData.direccion,
            password: userData.password,
            rol: {
                id_rol: 6, // ID del rol "Paciente"
                nombre: "Paciente"
            },
            fichamedica: {
                edad: this.obtenerEdad(userData.fechanacimiento),
                sexo: userData.sexo,
                sintomas: '',
                enfermedades: '',
                peso:null,
                altura: null
            }
        };
        // Log para depuración
        console.log('Payload registro paciente:', pacienteData);
        //llamada al servicio de pacientes para registrar el usuario
        return axios.post(API_URL + '/api/pacientes/', pacienteData)
            .then(response => {
                if (response.data) {
                    // No guardamos el usuario en localStorage aquí eso se hace en el login
                    //devolvemos el usuario registrado
                    return {
                        success: true,
                        usuario: response.data
                    };
                }
                return response.data;
            });
    },
    //metodo para cerrar sesión
    logout() {
        localStorage.removeItem('user');
    },
    //metodo para obtener el usuario actual
    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));
    },

    obtenerEdad(fechaNacimiento) {
        const fechaActual = new Date();
        const fechaNac = new Date(fechaNacimiento);
        let edad = fechaActual.getFullYear() - fechaNac.getFullYear();
        const mes = fechaActual.getMonth() - fechaNac.getMonth();
        if (mes < 0 || (mes === 0 && fechaActual.getDate() < fechaNac.getDate())) {
            edad--;
        }
        return edad;
    }
};