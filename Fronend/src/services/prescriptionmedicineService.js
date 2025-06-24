import api from './api';

export default {

obtenerPorReceta(idReceta) {
  return api.get(`/recetas-medicamentos/medicamentos-por-receta/${idReceta}`);
}
};
