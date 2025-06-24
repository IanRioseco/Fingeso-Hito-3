<!--CONTENIDO DE CITA AGENDADA-->
<template>
  <div class="appointments-container">
    <h2 class="titulo-citas">Mis Citas</h2>
    <div class="appointment-list" v-if="agendadas.length">
      <li class="appointment-item" v-for="cita in agendadas" :key="cita.id_citamedica">
        <p><strong>📅 Fecha:</strong> {{ formatFecha(cita.horario?.fecha) }}</p>
        <p><strong>⏰ Hora:</strong> {{ formatHora(cita.horario?.horainicio) }}</p>
        <p><strong>👨‍⚕️ Médico:</strong> {{ cita.medico?.nombre }} {{ cita.medico?.apellido }}</p>
        <p><strong>🩺 Especialidad:</strong> {{ cita.medico?.especialidad?.nombre }}</p>
        <p><strong>📌 Estado:</strong> {{ cita.estado }}</p>
        <button class="btn-cancel" @click="cancelarCita(cita.id_citamedica)">Cancelar</button>
      </li>
    </div>
    <div class="no-appointments" v-else>
      No tienes citas agendadas.
    </div>
  </div>
</template>

<script setup>
//IMPORTACIONES
import { ref, onMounted, computed } from 'vue'
import appointmentService from '@/services/appointmentService'
import { useStore } from 'vuex'

//DATOS REACTIVOS PARA EL COMPONENTE
const appointments = ref([])
const store = useStore()

// Computed para solo mostrar citas agendadas
const agendadas = computed(() =>
  appointments.value.filter(cita => cita.estado === 'Cita Agendada')
)

//funciones para formatear la fecha y la hora
function formatFecha(fecha) {
  if (!fecha) return 'Fecha no disponible';
  try {
    // Si es una fecha en formato ISO (YYYY-MM-DD)
    if (typeof fecha === 'string' && fecha.match(/^\d{4}-\d{2}-\d{2}$/)) {
      // Crear la fecha usando la zona horaria local
      const [year, month, day] = fecha.split('-');
      const d = new Date(year, month - 1, day);
      // Ajustar la fecha para la zona horaria local
      d.setMinutes(d.getMinutes() + d.getTimezoneOffset());
      return d.toLocaleDateString('es-CL', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    }
    // Si es un objeto Date
    const d = new Date(fecha);
    if (!isNaN(d)) {
      // Ajustar la fecha para la zona horaria local
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
    // Si es una hora en formato HH:mm o HH:mm:ss
    if (typeof hora === 'string' && hora.match(/^\d{2}:\d{2}(:\d{2})?/)) {
      return hora.substring(0, 5); // Devuelve solo HH:mm
    }
  } catch (error) {
    console.error('Error al formatear hora:', error);
  }
  return 'Formato de hora inválido';
}

const cargarCitas = async () => {
  const paciente = store.getters['auth/currentUser']
  const usuario = paciente?.usuario || paciente
  const idPaciente = usuario?.id_paciente || usuario?.idPaciente || usuario?.id || usuario?.rutPa
  if (!idPaciente) return
  try {
    const res = await appointmentService.obtenerCitasPorPaciente(idPaciente)
    // Filtra duplicados por id_citamedica
    const unicas = res.data.filter(
      (cita, index, self) =>
        index === self.findIndex(c => c.id_citamedica === cita.id_citamedica)
    )
    appointments.value = unicas
    console.log('Citas recibidas (sin duplicados):', appointments.value)
    appointments.value.forEach((cita, idx) => {
      console.log(`Cita #${idx + 1}:`, cita)
    })
  } catch (e) {
    appointments.value = []
  }
}

const cancelarCita = async (id) => {
  console.log('ID a cancelar:', id)
  if (!confirm('¿Estás seguro de cancelar esta cita?')) return
  try {
    await appointmentService.eliminarCita(id)
    await cargarCitas()
  } catch (e) {
    alert('Error al cancelar la cita')
  }
}

onMounted(cargarCitas)
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

.appointments-container button {
  background-color: #C51A6F;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  min-width: 90px;
  transition: background 0.2s;
}
.appointments-container button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
.appointments-container button:hover:not(:disabled) {
  background-color: #0875C1;
}
.no-appointments {
  text-align: center;
  color: #747473;
}

.appointments-container .btn-cancel {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.appointments-container .btn-cancel:hover {
  background-color: #C51A6F;
}

.appointments-container .btn-cancel:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.appointments-container .btn-cancel:disabled:hover {
  background-color: #ccc;
}

.appointments-container .btn-cancel:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.appointments-container .btn-cancel:hover:not(:disabled) {
  background-color: #0875C1;
}
</style>