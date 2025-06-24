<template>
  <div class="medical-alerts">
    <h3>Alertas de Recetas Médicas</h3>
    <div class="alert-list">
      <!-- Aquí se mostrarán las alertas reales cuando se conecte a datos -->
      <div class="alert-card" v-for="alert in alerts" :key="alert.id">
        <div class="alert-info">
          <strong>Paciente:</strong> {{ alert.paciente }}<br>
          <strong>Medicamento:</strong> {{ alert.medicamento }}<br>
          <strong>Fecha:</strong> {{ alert.fecha }}
        </div>
        <button class="action-btn">Ver Detalle</button>
      </div>
      <div v-if="alerts.length === 0" class="no-alerts">No hay alertas de recetas médicas.</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import pharmacyService from '@/services/PharmacyService'
import { authService } from '@/services/auth.service'

const alerts = ref([])

onMounted(async () => {
  // Obtener usuario actual y farmacia asociada
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
    // Llama al backend para obtener recetas médicas con medicamentos de esta farmacia
    // Debes tener este endpoint en tu backend
    const recetas = await pharmacyService.getMedicalAlertsByFarmacia(farmaciaId);
    // Mapear a formato de alerta
    alerts.value = recetas.map(r => ({
      id: r.idreceta,
      paciente: r.paciente?.nombrePa + ' ' + r.paciente?.apellidoPa,
      medicamento: r.medicamentos?.map(m => m.nombre).join(', '),
      fecha: r.fecha
    }));
    console.log('Alertas de recetas médicas:', alerts.value);
  } catch (e) {
    alerts.value = [];
    console.error('Error al obtener alertas de recetas médicas:', e);
  }
})
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
