<!--CONTENIDO DE CITA AGENDADA-->
<template>
  <div class="appointments-container">
    <h2>Mis Citas</h2>
    <!--solo se muestra si hay citas-->
    <ul class="appointment-list" v-if="appointments.length">
      <!--por cada cita en el array se muestra un elemento del array, se identifica con la key ID-->
      <li class="appointment-item" v-for="cita in appointments" :key="cita.id_citamedica">
        <!--contenido dentro de cada elemento del array-->
        <p><strong>📅 Fecha:</strong> {{ formatFecha(cita.horario?.fecha) }}</p>
        <p><strong>⏰ Hora:</strong> {{ formatHora(cita.horario?.horainicio) }}</p>
        <p><strong>👨‍⚕️ Médico:</strong> {{ cita.medico?.nombre }} {{ cita.medico?.apellido }}</p>
        <p><strong>🩺 Especialidad:</strong> {{ cita.medico?.especialidad?.nombre }}</p>
        <p><strong>📌 Estado:</strong> {{ cita.estado }}</p>
        <button class="btn-cancel" @click="cancelarCita(cita.id_citamedica)">Cancelar</button>
      </li>
    </ul>
    <div class="no-appointments" v-else>
      No tienes citas agendadas.
    </div>
  </div>
</template>


<script setup>
//IMPORTACIONES
import { ref, onMounted } from 'vue'
import appointmentService from '@/services/appointmentService'
import { useStore } from 'vuex'

//DATOS REACTIVOS PARA EL COMPONENTE
const appointments = ref([])
const store = useStore()
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
.appointments-container {
  max-width: 700px;
  margin: 2rem auto;
  padding: 1rem;
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


</style>