import api from './api';

export default {
  // Crear una receta
  crear(receta) {
    return api.post('/api/receta/', receta);
  },

  // Obtener todas las recetas
  obtenerTodas() {
    return api.get('/api/receta/');
  },

  // Obtener una receta por ID
  obtenerPorId(id) {
    return api.get(`/api/receta/${id}`);
  },

  // Actualizar una receta por ID
  actualizar(id, receta) {
    return api.put(`/api/receta/${id}`, receta);
  },

  // Eliminar una receta por ID
  eliminar(id) {
    return api.delete(`/api/receta/${id}`);
  },

  crearConMedicamentos(recetaDTO) {
  return api.post('/api/receta/con-medicamentos', recetaDTO);
  },

 obtenerRecetaPorCita(idCita) {
  return api.get(`/api/receta/por-cita/${idCita}`);
 }
};