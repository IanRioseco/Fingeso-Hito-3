import api from './api'

export default {
  async getAll() {
    const res = await api.get('/api/medicamentos')
    return res.data
  },
  async create(data) {
    return await api.post('/api/medicamentos', data)
  },
  async delete(id) {
    return await api.delete(`/api/medicamentos/${id}`)
  }
}
