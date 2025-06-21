<template>
  <div>
    <h2>Gestion de citas</h2>
    <table>
      <thead>
        <tr>
          <th>Fecha</th>
          <th>Hora Inicio</th>
          <th>Paciente</th>
          <th>Estado</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="appointment in appointments" :key="appointment.id_citamedica">
          <td>{{ appointment.horario.fecha }}</td>
          <td>{{ appointment.horario.horainicio }}</td>
          <td>
            <span v-if="appointment.paciente">
              {{ appointment.paciente.nombrePa }} {{ appointment.paciente.apellidoPa }}
            </span>
            <span v-else>
              Sin paciente
            </span>
          </td>
          <td>
            {{ appointment.estado }}
          </td>
          <td>
            <button class="btn-realizada">
              Ver ficha médica
            </button>
            <button
              v-if="appointment.estado !== 'Realizada'"
              @click="marcarComoRealizada(appointment)"
              class="btn-realizada"
              style="margin-left: 8px;"
            >
              Marcar como realizada
            </button>
          </td>
        </tr>
      </tbody>
      <tfoot v-if="appointments.length === 0">
        <tr>
          <td colspan="5">No hay citas programadas.</td>
        </tr>
      </tfoot>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import appointmentServices from '@/services/appointmentService';

const appointments = ref([]);

// Obtiene el objeto del médico desde localStorage de forma segura
const medicoRaw = localStorage.getItem('user');
let medico = null;
if (medicoRaw && medicoRaw !== 'undefined') {
  medico = JSON.parse(medicoRaw);
} else {
  console.error('No se encontró el usuario en localStorage o está mal guardado.');
}

// Intenta obtener el id del médico de distintas formas
const idMedico =
  medico?.idmedico ||
  medico?.usuario?.idmedico ||
  medico?.id ||
  medico?.usuario?.id;

console.log('ID del médico detectado:', idMedico);

onMounted(async () => {
  if (!idMedico) {
    console.error('No se encontró el id del médico en localStorage.');
    return;
  }
  try {
    const response= await appointmentServices.obtenerCitaConHorarioPorMedico(idMedico);
    appointments.value = response.data;
    console.log('Citas recibidas:', appointments.value);
    // Muestra el primer elemento para ver su estructura
    if (appointments.value.length > 0) {
      console.log('Primer cita:', JSON.parse(JSON.stringify(appointments.value)));
    } else {
      console.log('No se encontraron citas para este médico.');
    }
  } catch (error) {
    console.error('Error fetching appointments:', error);
  }
});

function marcarComoRealizada(appointment) {
  appointment.estado = 'Realizada';
  // Aquí puedes agregar la llamada al backend para persistir el cambio si lo necesitas
}
</script>


<style scoped>

.doctor-dashboard {
  padding: 2rem;
  max-width: 100%;
  margin: 0 auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 0.5rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}

.doctor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #ddd;
}

.doctor-header h2 {
  margin: 0;
}

.doctor-header button {
  background-color: #0875C1;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.doctor-header button:hover {
  background-color: #C51A6F;
}

.doctor-header span {
  font-size: 1.1rem;
  color: #747473;
}

.btn-realizada {
  background-color: #C51A6F;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-realizada:hover {
  background-color: #0875C1;
}


</style>