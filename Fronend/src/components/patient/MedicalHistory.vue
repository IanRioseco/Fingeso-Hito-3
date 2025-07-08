<template>
  <div class="appointments-container">
    <h2 class="titulo-citas">Historia Clínica</h2>
    <div class="appointment-list" v-if="realizadas.length">
      <li class="appointment-item" v-for="cita in realizadas" :key="cita.id_citamedica">
        <p><strong>📅 Fecha:</strong> {{ formatFecha(cita.horario?.fecha || cita.fecha || cita.horario_fecha) }}</p>
        <p><strong>⏰ Hora:</strong> {{ formatHora(cita.horario?.horainicio || cita.horainicio || cita.horario_horainicio) }}</p>
        <p><strong>👨‍⚕️ Médico:</strong> {{ cita.medico?.nombre || 'Sin médico' }} {{ cita.medico?.apellido || '' }}</p>
        <p><strong>🩺 Especialidad:</strong> {{ cita.medico?.especialidad?.nombre || 'Sin especialidad' }}</p>
        <p><strong>📌 Estado:</strong> {{ cita.estado }}</p>
        <button
          class="btn-ver-receta"
          @click="verReceta(cita.id_citamedica)"
          style="margin-top: 0.7rem;"
        >
          Ver receta
        </button>
      </li>
    </div>
    <div class="no-appointments" v-else>
      No hay citas realizadas.
    </div>
    <!-- Modal profesional para mostrar la receta -->
    <div v-if="showRecetaModal" class="modal-overlay">
      <div class="modal-content receta-modal">
        <h2 class="receta-title">Receta Médica</h2>
        <div class="receta-scroll">
          <!-- DEPURACIÓN: Mostrar todas las recetas recibidas -->
        
          <!-- FIN DEPURACIÓN -->
          <div v-if="recetaSeleccionada && recetaSeleccionada.length">
            <div
              v-for="receta in recetaSeleccionada.filter(r =>
                formatFecha(r.fechaEmision || r.fecha_emision || r.fecha) === fechaCitaSeleccionada
              )"
              :key="receta.id_receta"
              class="receta-card"
            >
              <div class="receta-header">
                <span><strong>Fecha:</strong> {{ formatFecha(receta.fechaEmision || receta.fecha_emision || receta.fecha) }}</span>
                <span><strong>Médico:</strong> {{ receta.citamedica?.medico?.nombre }} {{ receta.citamedica?.medico?.apellido }}</span>
                <span><strong>Especialidad:</strong> {{ receta.citamedica?.medico?.especialidad?.nombre }}</span>
              </div>
              <div class="receta-body">
                <p><strong>Paciente:</strong> {{ receta.paciente?.nombrePa }} {{ receta.paciente?.apellidoPa }}</p>
                <div v-if="receta.medicamentos && receta.medicamentos.length">
                  <p><strong>Medicamentos:</strong></p>
                  <ul>
                    <li v-for="med in receta.medicamentos" :key="med.id_medicamento">
                      {{ med.nombre }}
                    </li>
                  </ul>
                </div>
                <p v-if="receta.descripcion"><strong>Descripcion:</strong> {{ receta.descripcion }}</p>
              </div>
            </div>
            <div v-if="recetaSeleccionada.filter(r =>
                formatFecha(r.fechaEmision || r.fecha_emision || r.fecha) === fechaCitaSeleccionada
              ).length === 0">
              <p>No se encontró receta para esta cita.</p>
            </div>
          </div>
          <div v-else>
            <p>No se encontró receta para esta cita.</p>
          </div>
        </div>
        <button @click="showRecetaModal = false" class="btn-cerrar-modal">Cerrar</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import appointmentService from '@/services/appointmentService';
import prescriptionService from '@/services/prescriptionService';
import prescriptionmedicineService from '@/services/prescriptionmedicineService';

let user = null;
try {
  const userRaw = localStorage.getItem('user');
  if (userRaw && userRaw !== 'undefined') {
    user = JSON.parse(userRaw);
  }
} catch (e) {
  console.error('Error leyendo usuario de localStorage:', e);
}

// Unificación robusta para obtener el idPaciente
const paciente = user?.usuario || user;
const idPaciente =
  paciente?.idPaciente ||
  paciente?.id_paciente ||
  paciente?.id ||
  paciente?.rutPa;

const medicalHistory = ref([]);
const showRecetaModal = ref(false);
const recetaSeleccionada = ref([]);
const fechaCitaSeleccionada = ref(null);

const fetchMedicalHistory = async () => {
  try {
    const response = await appointmentService.obtenerCitasConHorarioPorPaciente(idPaciente);
    medicalHistory.value = response.data;
  } catch (error) {
    console.error('Error fetching medical history:', error);
  }
};

onMounted(fetchMedicalHistory);

const realizadas = computed(() => {
  return medicalHistory.value.filter(cita => cita.estado === 'Realizada');
});

function formatFecha(fecha) {
  if (!fecha) return 'Fecha no disponible';
  try {
    if (typeof fecha === 'string' && fecha.match(/^\d{4}-\d{2}-\d{2}$/)) {
      const [year, month, day] = fecha.split('-');
      const d = new Date(year, month - 1, day);
      d.setMinutes(d.getMinutes() + d.getTimezoneOffset());
      return d.toLocaleDateString('es-CL', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    }
    const d = new Date(fecha);
    if (!isNaN(d)) {
      d.setMinutes(d.getMinutes() + d.getTimezoneOffset());
      return d.toLocaleDateString('es-CL', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    }
  } catch (error) {
    console.error('Error al formatear fecha:', error);
  }
  return 'Formato de fecha inválido';
}

function formatHora(hora) {
  if (!hora) return 'Hora no disponible';
  try {
    if (typeof hora === 'string' && hora.match(/^\d{2}:\d{2}(:\d{2})?/)) {
      return hora.substring(0, 5);
    }
  } catch (error) {
    console.error('Error al formatear hora:', error);
  }
  return 'Formato de hora inválido';
}

async function verReceta(idCita) {
  try {
    const cita = realizadas.value.find(c => c.id_citamedica === idCita);
    const fechaCita = cita?.horario?.fecha || cita?.fecha || cita?.horario_fecha || null;
    fechaCitaSeleccionada.value = formatFecha(fechaCita);

    const res = await prescriptionService.obtenerRecetaPorCita(idCita);
    const recetas = Array.isArray(res.data) ? res.data : [res.data];

    // Obtener medicamentos para cada receta
    for (const receta of recetas) {
      const lismed = await prescriptionmedicineService.obtenerPorReceta(receta.id_receta);
      receta.medicamentos = Array.isArray(lismed.data) ? lismed.data : [lismed.data];
    }

    // DEPURACIÓN: Mostrar en consola los datos relevantes

    recetaSeleccionada.value = recetas;
  } catch (e) {
    recetaSeleccionada.value = [];
    console.error('Error en verReceta:', e);
  }
  showRecetaModal.value = true;
}
</script>

<style scoped>
.appointments-container h2.titulo-citas {
  text-align: left;
  margin-bottom: 0.5rem;
  max-width: 700px;
  margin-top: 0;
  padding-left: 0;
  font-family: 'Segoe UI', sans-serif;
}

.appointments-container h2 { 
  color: black;
  padding-bottom: 2px;
}

.appointment-list {
  list-style: none;
  padding: 0;
}

.appointment-item {
  background: #f5f5f5;
  border-left: 5px solid #3498db;
  margin-bottom: 1rem;
  padding: 1rem;
  border-radius: 6px;
}

.appointment-item p {
  margin: 0.3rem 0;
}

.no-appointments {
  text-align: center;
  color: #888;
  margin-top: 2rem;
  font-style: italic;
}

.appointments-container ul {
  list-style-type: none;
  padding: 0;
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.25);
  border-radius: 12px;
}

.btn-ver-receta {
  background: #0875C1;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 0.4rem 1.2rem;
  cursor: pointer;
  font-size: 1rem;
  transition: background 0.2s;
}
.btn-ver-receta:hover {
  background: #005fa3;
}

.receta-scroll {
  max-height: 60vh;
  overflow-y: auto;
  padding-right: 8px;
}

/* Solo una definición de .modal-overlay y .modal-content */
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: #fff;
  padding: 2rem;
  border-radius: 10px;
  min-width: 320px;
  max-width: 90vw;
  box-shadow: 0 2px 16px rgba(0,0,0,0.25);
  text-align: center;
}

.receta-modal {
  width: 100%;
  max-width: 600px;
}
.receta-title {
  margin-bottom: 1.5rem;
  color: #0875C1;
  font-size: 2rem;
  font-weight: bold;
}
.receta-card {
  background: #f9f9f9;
  border: 1px solid #3498db;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  padding: 1.2rem 1rem;
  text-align: left;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07);
}
.receta-header {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  margin-bottom: 1rem;
  font-size: 1.05rem;
}
.receta-body {
  font-size: 1.1rem;
}
.receta-body ul {
  margin: 0.5rem 0 0.5rem 1.2rem;
  padding: 0;
}
.btn-cerrar-modal {
  margin-top: 1rem;
  background: #C51A6F;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 0.4rem 1.2rem;
  cursor: pointer;
  font-size: 1rem;
  transition: background 0.2s;
}
.btn-cerrar-modal:hover {
  background: #0875C1;
}

.receta-body ul {
  list-style-type: disc;
  background: none;
  border: none;
  box-shadow: none;
  padding: 0 0 0 1.5em;
  margin: 0.5rem 0 0.5rem 0;
  border-radius: 0;
}
.receta-body li {
  background: none;
  border: none;
  box-shadow: none;
  padding: 0;
  margin: 0;
  font-size: 1rem;
}
</style>