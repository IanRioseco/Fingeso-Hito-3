import api from './api';

export default {
  // Crear un medicamento
  crear(medicamento) {
    return api.post('/api/medicamentos/', medicamento);
  },

  // Obtener todos los medicamentos
  obtenerTodos() {
    return api.get('/api/medicamentos/');
  },

  // Obtener un medicamento por ID
  obtenerPorId(id) {
    return api.get(`/api/medicamentos/${id}`);
  },

  // Actualizar un medicamento por ID
  actualizar(id, medicamento) {
    return api.put(`/api/medicamentos/${id}`, medicamento);
  },

  // Eliminar un medicamento por ID
  eliminar(id) {
    return api.delete(`/api/medicamentos/${id}`);
  }

  
};