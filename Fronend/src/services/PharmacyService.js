import api from './api'

export default {
  // Métodos para gestionar medicamentos

  //obtener todos los medicamentos
  async getAll() {
    const res = await api.get('/api/medicamentos/')
    return res.data
  },
  //obtener un medicamento por su id
  async create(data) {
    return await api.post('/api/medicamentos/', data)
  },
  //actualizar un medicamento por su id
  async updateMedicine(id, data) {
    return await api.put(`/api/medicamentos/${id}`, data)
  },
  //eliminar un medicamento por su id
  async delete(id) {
    return await api.delete(`/api/medicamentos/${id}`)
  },
  //obtener un medicamento por su id
  async getAllByFarmacia(farmaciaId) {
    const res = await api.get(`/api/farmacia-medicamento/farmacia/${farmaciaId}`)
    return res.data
  },
  //metodos para gestionar la relación entre farmacias y medicamentos
  async addMedicamentoToFarmacia(farmaciaId, medicamentoId) {
    return await api.post('/api/farmacia-medicamento/', {
      farmacia: { idFarmacia: farmaciaId },
      medicamento: { idmedicamento: medicamentoId }
    })
  },
  //eliminar la relación entre farmacias y medicamentos
  async deleteFarmaciaMedicamento(id) {
    // Elimina la relación farmacia-medicamento por ID
    return await api.delete(`/api/farmacia-medicamento/${id}`)
  },
  async getAllFarmacias() {
    const res = await api.get('/api/farmacia/');
    return res.data;
  },
  // Obtener recetas médicas con medicamentos asociados a una farmacia
  async getMedicalAlertsByFarmacia(farmaciaId) {
    // Ajusta el endpoint según tu backend
    const res = await api.get(`/api/recetas/alertas/farmacia/${farmaciaId}`)
    return res.data
  }
}
