<template>
  <div class="medical-alerts">
    <h3>Alertas de Recetas Médicas</h3>
    <div class="alert-list">
      <div class="alert-card" v-for="alert in alerts" :key="alert.id">
        <div class="alert-info">
          <strong>Paciente:</strong> {{ alert.paciente }}<br>
          <strong>Medicamentos:</strong>
          <ul>
            <li v-for="med in alert.medicamentos" :key="med.id">
              {{ med.nombre }}
              <input type="number"
                     min="1"
                     max="100"
                     v-model.number="med.cantidadEntregar"
                     @input="onCantidadInput(med)"
                     :disabled="med.cantidad === 0"
                     style="width: 60px; margin-left: 10px;" />
              <span style="font-size: 0.9em; color: #888;">
                (Stock: {{ med.cantidad }})
                <span v-if="med.cantidad === 0" style="color: red; font-weight: bold;"> - Sin stock</span>
              </span>
            </li>
          </ul>
          <strong>Fecha:</strong> {{ alert.fecha }}
        </div>
        <button class="action-btn" :disabled="alert.validando" @click="validarReceta(alert)">
          {{ alert.validando ? 'Validando...' : 'Validar Receta' }}
        </button>
      </div>
      <div v-if="alerts.length === 0" class="no-alerts">No hay alertas de recetas médicas.</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import prescriptionmedicineService from '@/services/prescriptionmedicineService'
import { authService } from '@/services/auth.service'

const alerts = ref([])

function onCantidadInput(med) {
  // Solo permitir valores positivos y máximo 100
  if (med.cantidadEntregar < 1) med.cantidadEntregar = 1;
  if (med.cantidadEntregar > 100) med.cantidadEntregar = 100;
  // Actualizar stock visual en tiempo real
  if (typeof med.cantidadEntregarAnterior === 'undefined') med.cantidadEntregarAnterior = 1;
  med.cantidad += med.cantidadEntregarAnterior - med.cantidadEntregar;
  med.cantidadEntregarAnterior = med.cantidadEntregar;
}

const cargarAlertas = async () => {
  const user = authService.getCurrentUser();
  let farmaciaId = null;
  if (user) {
    const usuario = user.usuario || user;
    farmaciaId = usuario.farmacia?.idFarmacia || usuario.idFarmacia;
  }
  if (!farmaciaId) {
    alerts.value = [];
    console.warn('No se encontró farmacia asociada al usuario.');
    return;
  }
  try {
    // Obtener recetas asociadas a la farmacia
    const response = await prescriptionmedicineService.obtenerRecetasPorFarmacia(farmaciaId);
    const recetas = response.data;
    if (!Array.isArray(recetas)) {
      alerts.value = [];
      console.warn('La respuesta de recetas no es un array:', recetas);
      return;
    }
    // Filtrar recetas no validadas y sin duplicados
    const recetasNoValidadas = recetas.filter(r => (r.Estado || r.estado) !== 'VALIDADA');
    const recetasUnicas = [];
    const idsVistos = new Set();
    for (const r of recetasNoValidadas) {
      const idReceta = r.idreceta || r.id_receta;
      if (!idsVistos.has(idReceta)) {
        recetasUnicas.push(r);
        idsVistos.add(idReceta);
      }
    }
    const alertas = (await Promise.all(
      recetasUnicas.map(async r => {
        let medicamentos = [];
        try {
          const respMed = await prescriptionmedicineService.obtenerPorReceta(r.id_receta);
          medicamentos = Array.isArray(respMed.data) ? respMed.data.map(m => ({
            ...m,
            cantidadEntregar: 1 // valor inicial
          })) : [];
        } catch (e) {
          console.error(`Error obteniendo medicamentos para receta ${r.id_receta}:`, e);
        }
        return {
          id: r.idreceta || r.id_receta,
          paciente: r.paciente?.nombrePa + ' ' + r.paciente?.apellidoPa,
          medicamentos,
          fecha: r.fecha,
          validando: false,
          mensaje: ''
        };
      })
    ))
    .filter(a => !a.validada); // solo mostrar no validadas
    alerts.value = alertas;
  } catch (e) {
    alerts.value = [];
    console.error('Error al obtener alertas de recetas médicas:', e);
  }
}

const validarReceta = async (alert) => {
  alert.validando = true;
  try {
    // Lógica para descontar stock en backend
    const detalles = alert.medicamentos.map(med => ({
      idMedicamento: med.id || med.idmedicamento,
      cantidadEntregar: med.cantidadEntregar
    }));
    console.log('Detalles enviados:', detalles);
    const resp = await prescriptionmedicineService.validarReceta({
      idReceta: alert.id,
      detalles
    });
    console.log('Respuesta del backend al validar receta:', resp);
    // Actualizar stock localmente (por si el backend lo cambia)
    alert.medicamentos.forEach(med => {
      if (med.cantidadEntregar > 0 && med.cantidadEntregar <= med.cantidad + (med.cantidadEntregarAnterior || 0)) {
        med.cantidad -= med.cantidadEntregar;
      }
      delete med.cantidadEntregarAnterior;
    });
    alert.validando = false;
    alert.validada = true;
    alert.mensaje = 'Receta validada correctamente';
    // Mostrar alerta visual
    window.alert('Receta validada correctamente');
    await cargarAlertas();
  } catch (e) {
    alert.validando = false;
    alert.mensaje = 'Error al validar receta';
    console.error('Error al validar receta:', e);
  }
}

onMounted(cargarAlertas);
</script>

<style scoped>
.medical-alerts {
  width: 100%;
}
.alert-list {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}
.alert-card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px var(--color-shadow);
  padding: 1.2rem 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.alert-info {
  color: var(--color-principal);
}
.medicamentos-list {
  margin-top: 0.7rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.medicamento-item {
  display: flex;
  align-items: center;
  gap: 0.7rem;
}
.cantidad-input {
  width: 60px;
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  border: 1px solid #ccc;
}
.action-btn {
  background: var(--color-secundario);
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 0.5rem 1.2rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}
.action-btn:hover {
  background: var(--color-principal);
}
.no-alerts {
  color: #888;
  text-align: center;
  margin-top: 2rem;
}
</style>
