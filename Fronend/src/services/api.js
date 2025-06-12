import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true
});

// Interceptor para manejar errores
api.interceptors.response.use(
    response => response,
    error => {
        console.error('Error en la petición:', error);
        return Promise.reject(error);
    }
);

export default api; 