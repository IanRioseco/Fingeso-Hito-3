<template>
  <div>
    <h2>Mis Citas</h2>
    <ul v-if="appointments.length">
      <li v-for="cita in appointments" :key="cita.id_citamedica">
        <strong>Fecha: {{ formatFecha(cita.horario?.fecha) }}</strong><br />
        <span>Hora: {{ formatHora(cita.horario?.horainicio) }}</span><br />
        Médico: {{ cita.medico?.nombre }} {{ cita.medico?.apellido }}<br />
        Especialidad: {{ cita.medico?.especialidad?.nombre }}<br />
        Estado: {{ cita.estado }}
        <button @click="cancelarCita(cita.id_citamedica)">Cancelar cita</button>
      </li>
    </ul>
    <div v-else>
      No tienes citas agendadas.
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import appointmentService from '@/services/appointmentService'
import { useStore } from 'vuex'

const appointments = ref([])
const store = useStore()

function formatFecha(fecha) {
  if (!fecha) return ''
  const d = new Date(fecha)
  if (!isNaN(d)) {
    return d.toLocaleDateString('es-CL')
  }
  if (typeof fecha === 'string' && fecha.match(/^\d{4}-\d{2}-\d{2}$/)) {
    const [y, m, d] = fecha.split('-')
    return `${d}/${m}/${y}`
  }
  return fecha
}

function formatHora(hora) {
  if (!hora) return ''
  if (typeof hora === 'string' && hora.match(/^\d{2}:\d{2}/)) {
    return hora.substring(0,5)
  }
  return hora
}

const cargarCitas = async () => {
  const paciente = store.getters['auth/currentUser']
  const usuario = paciente?.usuario || paciente
  const idPaciente = usuario?.id_paciente || usuario?.idPaciente || usuario?.id || usuario?.rutPa
  if (!idPaciente) return
  try {
    const res = await appointmentService.obtenerCitasPorPaciente(idPaciente)
    appointments.value = res.data
  } catch (e) {
    appointments.value = []
  }
}

const cancelarCita = async (id) => {
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