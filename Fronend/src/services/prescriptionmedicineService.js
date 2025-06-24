import api from './api';

export default {

obtenerPorReceta(idReceta) {
  return api.get(`/api/recetas-medicamentos/medicamentos-por-receta/${idReceta}`);
},

obtenerRecetasPorFarmacia(farmaciaId) {
    return api.get(`/api/recetas-medicamentos/alertas/farmacia/${farmaciaId}`);
  },

validarReceta(payload) {
  // payload: { idReceta, detalles: [{ idMedicamento, cantidadEntregar }] }
  return api.post('/api/recetas-medicamentos/validar', payload);
},

};
