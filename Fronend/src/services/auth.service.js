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
            if (response.data) {
                localStorage.setItem('user', JSON.stringify(response.data));
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
            correo: userData.correo,
            telefono: userData.telefono ? userData.telefono.toString() : '',
            direccion: userData.direccion,
            password: userData.password,
            rol: {
                id_rol: 6, // ID del rol "Paciente"
                nombre: "Paciente"
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
    }
};