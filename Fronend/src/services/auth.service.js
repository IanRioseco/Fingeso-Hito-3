import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export const authService = {
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

    register(userData) {
        // Transformar los datos para que coincidan con el backend
        const pacienteData = {
            nombrePa: userData.nombrePa,
            apellidoPa: userData.apellidoPa,
            rut: userData.rut,
            correo: userData.correo,
            telefono: userData.telefono.toString(),
            direccion: userData.direccion,
            password: userData.password,
            rol: {
                id_rol: 1, // ID del rol "Paciente"
                nombre: "Paciente"
            }
        };
        
        return axios.post(API_URL + '/api/pacientes/', pacienteData)
            .then(response => {
                if (response.data) {
                    // No guardamos el usuario en localStorage aquí, eso se hace en el login
                    return {
                        success: true,
                        usuario: response.data
                    };
                }
                return response.data;
            });
    },

    logout() {
        localStorage.removeItem('user');
    },

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));
    }
};