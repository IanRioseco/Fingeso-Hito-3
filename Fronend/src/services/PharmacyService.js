import api from './api'

export default {
  async getAll() {
    const res = await api.get('/api/medicamentos/')
    return res.data
  },
  async create(data) {
    return await api.post('/api/medicamentos/', data)
  },
  async updateMedicine(id, data) {
    return await api.put(`/api/medicamentos/${id}`, data)
  },
  async delete(id) {
    return await api.delete(`/api/medicamentos/${id}`)
  },
  async getAllByFarmacia(farmaciaId) {
    const res = await api.get(`/api/farmacia-medicamento/farmacia/${farmaciaId}`)
    return res.data
  },
  async addMedicamentoToFarmacia(farmaciaId, medicamentoId) {
    return await api.post('/api/farmacia-medicamento/', {
      farmacia: { idFarmacia: farmaciaId },
      medicamento: { idmedicamento: medicamentoId }
    })
  },
  async deleteFarmaciaMedicamento(id) {
    // Elimina la relación farmacia-medicamento por ID
    return await api.delete(`/api/farmacia-medicamento/${id}`)
  }
}
