import api from './api'

export default {
  // Métodos para gestionar medicamentos

  //obtener todos los medicamentos
  async getAllEspecialidad() {
    console.log('Obteniendo todas las especialidades...'); //para debugging
    return api.get('/api/especialidad/')


  },

  
  //obtener un medicamento por su id
  async createEspecialidad(data) {
    return await api.post('/api/especialidad/', data)
  },
  //actualizar un medicamento por su id
  async updateEspecialidad(id, data) {
    return await api.put(`/api/especialidad/${id}`, data)
  },
  //eliminar un medicamento por su id
  async deleteEspecialidad(id) {
    return await api.delete(`/api/especialidad/${id}`)
  },

}