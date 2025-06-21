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
          <td>{{ appointment.horario?.fecha || 'Sin horario' }}</td>
          <td>{{ appointment.horario?.horainicio || 'Sin horario' }}</td>
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
            <button class="btn-realizada" @click="verFichaMedica(appointment)">
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

    <!-- Modal para mostrar la ficha médica -->
    <div v-if="showFichaModal" class="modal-ficha">
      <div class="modal-content ficha-medica-form">
        <h3>Ficha Médica del Paciente</h3>
        <div class="ficha-section">
          <h4>Datos del Paciente</h4>
          <div class="ficha-row">
            <div>
              <label><strong>Nombre:</strong></label>
              <input type="text" v-model="fichaMedicaSeleccionada.paciente.nombrePa" disabled />
            </div>
            <div>
              <label><strong>Apellido:</strong></label>
              <input type="text" v-model="fichaMedicaSeleccionada.paciente.apellidoPa" disabled />
            </div>
          </div>
          <div class="ficha-row">
            <div>
              <label><strong>Fecha de nacimiento:</strong></label>
              <input type="date" v-model="fichaMedicaSeleccionada.paciente.fechanacimiento" disabled />
            </div>
            <div>
              <label><strong>Sexo:</strong></label>
              <input type="text" v-model="fichaMedicaSeleccionada.paciente.sexo" disabled />
            </div>
          </div>
          <div class="ficha-row">
            <div>
              <label><strong>Peso (kg):</strong></label>
              <input type="number" v-model="fichaMedicaSeleccionada.peso" min="0" step="0.1" />
            </div>
            <div>
              <label><strong>Altura (cm):</strong></label>
              <input type="number" v-model="fichaMedicaSeleccionada.altura" min="0" step="0.1" />
            </div>
          </div>
        </div>
        <div class="ficha-section ficha-clinica-columna">
          <h4>Información Clínica</h4>
          <div class="ficha-field">
            <label><strong>Enfermedades:</strong></label>
            <textarea v-model="fichaMedicaSeleccionada.enfermedades" rows="2" placeholder="Escribe enfermedades..."></textarea>
          </div>
          <div class="ficha-field">
            <label><strong>Síntomas:</strong></label>
            <textarea v-model="fichaMedicaSeleccionada.sintomas" rows="2" placeholder="Escribe síntomas..."></textarea>
          </div>
          <div class="ficha-field">
            <label><strong>Diagnóstico:</strong></label>
            <textarea v-model="fichaMedicaSeleccionada.diagnostico" rows="2" placeholder="Escribe diagnóstico..."></textarea>
          </div>
          <div class="ficha-field">
            <label><strong>Tratamiento:</strong></label>
            <textarea v-model="fichaMedicaSeleccionada.tratamiento" rows="2" placeholder="Escribe tratamiento..."></textarea>
          </div>
        </div>
        <div class="ficha-actions">
          <button @click="cerrarModalFicha">Cerrar</button>
          <!-- Aquí podrías agregar un botón para guardar cambios en el backend -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import appointmentServices from '@/services/appointmentService';

const appointments = ref([]);
const showFichaModal = ref(false);
const fichaMedicaSeleccionada = ref(null);

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

async function verFichaMedica(appointment) {
  // Suponiendo que la cita tiene la ficha médica asociada
  const ficha = appointment.fichamedica;
  console.log('Ficha médica recibida:', ficha); 
  if (ficha) {
    fichaMedicaSeleccionada.value = ficha;
    showFichaModal.value = true;
  } else {
    alert('No hay ficha médica asociada a esta cita.');
  }
}

function cerrarModalFicha() {
  showFichaModal.value = false;
  fichaMedicaSeleccionada.value = null;
}
</script>


<style scoped>
.doctor-dashboard {
  padding: 2rem;
  max-width: 1100px;
  margin: 0 auto;
  background: #f8fafd;
  border-radius: 16px;
  box-shadow: 0 2px 16px rgba(8,117,193,0.07);
}

h2 {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  color: #222;
}

table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(8,117,193,0.04);
}

th, td {
  padding: 1rem 0.8rem;
  text-align: left;
}

th {
  background-color: #f2f6fa;
  color: #0875C1;
  font-weight: 700;
  font-size: 1.05rem;
  border-bottom: 2px solid #e3eafc;
}

td {
  border-bottom: 1px solid #f0f0f0;
  font-size: 1rem;
  color: #333;
}

tr:last-child td {
  border-bottom: none;
}

.btn-realizada {
  background: linear-gradient(90deg, #C51A6F 60%, #0875C1 100%);
  color: #fff;
  border: none;
  padding: 0.5rem 1.2rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 1rem;
  box-shadow: 0 2px 8px rgba(197,26,111,0.08);
  transition: background 0.2s, box-shadow 0.2s;
  margin-bottom: 0.2rem;
}

.btn-realizada:hover {
  background: linear-gradient(90deg, #0875C1 60%, #C51A6F 100%);
  box-shadow: 0 4px 16px rgba(8,117,193,0.10);
}

.ficha-actions button {
  background: linear-gradient(90deg, #C51A6F 60%, #0875C1 100%);
  color: white;
  border: none;
  padding: 0.5rem 1.2rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 1rem;
  transition: background 0.2s, box-shadow 0.2s;
}

.ficha-actions button:hover {
  background: linear-gradient(90deg, #0875C1 60%, #C51A6F 100%);
  box-shadow: 0 4px 16px rgba(8,117,193,0.10);
}

tfoot td {
  text-align: center;
  color: #888;
  padding: 1.5rem 0;
  font-size: 1.1rem;
}

.modal-ficha {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  border-radius: 16px;
  padding: 2rem 2.5rem;
  max-width: 700px;
  min-width: 350px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 4px 32px rgba(8,117,193,0.12);
}

.modal-content h3 {
  margin-top: 0;
  color: #C51A6F;
  font-size: 1.4rem;
  margin-bottom: 1.2rem;
}

.modal-content h4 {
  color: #0875C1;
  margin-bottom: 0.7rem;
}

.ficha-section {
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}
.ficha-section:last-child {
  border-bottom: none;
}
.ficha-row {
  display: flex;
  gap: 2rem;
  margin-bottom: 1rem;
}
.ficha-field {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.ficha-field textarea {
  width: 100%;
  min-height: 40px;
  resize: vertical;
  margin-top: 0.3rem;
  padding: 0.5rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-family: inherit;
}
.ficha-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}

.ficha-row div {
  flex: 1;
}

.ficha-row div:last-child {
  color: #555;
}

.ficha-row div strong {
  color: #333;
}

.ficha-clinica-columna {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

.ficha-row input {
  width: 100%;
  padding: 0.4rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  margin-top: 0.2rem;
  font-family: inherit;
}
.ficha-row input[disabled] {
  background: #f5f5f5;
  color: #888;
}

.ficha-row input[type="date"] {
  padding: 0.4rem;
  font-family: inherit;
}

.ficha-row input[type="number"] {
  padding: 0.4rem;
  font-family: inherit;
}

@media (max-width: 900px) {
  .doctor-dashboard, .modal-content {
    padding: 1rem;
    min-width: 0;
    max-width: 98vw;
  }
  table, thead, tbody, th, td, tr {
    display: block;
  }
  th, td {
    padding: 0.7rem 0.5rem;
  }
  tr {
    margin-bottom: 1rem;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(8,117,193,0.04);
    background: #fff;
  }
  th {
    background: #f2f6fa;
    color: #0875C1;
    border-bottom: none;
  }
}
</style>