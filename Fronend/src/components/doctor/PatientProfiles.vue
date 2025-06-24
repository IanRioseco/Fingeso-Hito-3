<template>
  <div>
    <h2>Gestión de citas</h2>
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
        <tr v-for="appointment in appointments.filter(a => a.estado !== 'Realizada')" :key="appointment.id_citamedica">
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
      <div class="modal-content ficha-medica-form ficha-medica-flex">
        <!-- Ficha médica a la izquierda -->
        <div class="ficha-medica-main">
          <h3>Ficha Médica del Paciente</h3>
          <div class="ficha-section">
            <h4>Datos del Paciente</h4>
            <!-- ...datos del paciente... -->
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
                <label><strong>Edad:</strong></label>
                <input type="text" :value="fichaMedicaSeleccionada.edad" disabled />
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
            <button @click="ActualizarFichaMedica">Guardar</button>
          </div>
        </div>
        <!-- Receta médica a la derecha -->
        <div class="receta-medica-side">
          <h3 class="receta-title">Generar Receta Médica</h3>
          <div class="receta-card">
            <div class="receta-field">
              <label for="farmacia-select" class="receta-label"><strong>Farmacia:</strong></label>
              <select id="farmacia-select" v-model="farmaciaSeleccionada" @change="cargarMedicamentosPorFarmacia(farmaciaSeleccionada)" class="receta-select">
                <option v-for="farmacia in farmaciasDisponibles" :key="farmacia.idFarmacia || farmacia.id" :value="farmacia.idFarmacia || farmacia.id">
                  {{ farmacia.nombre || farmacia.nombreFarmacia || 'Farmacia' }}
                </option>
              </select>
            </div>
            <div class="receta-field">
              <h4 class="receta-subtitle">Medicamentos</h4>
              <div class="receta-meds-list">
                <label v-for="med in medicamentosDisponibles" :key="med.idmedicamento" class="receta-checkbox-label">
                  <input
                    type="checkbox"
                    :value="med.idmedicamento"
                    v-model="medicamentosSeleccionados"
                    class="receta-checkbox"
                  />
                  <span class="receta-checkbox-custom"></span>
                  <span class="receta-med-name">{{ med.nombre }}</span>
                  <span class="receta-med-stock">Stock: {{ med.cantidad }}</span>
                </label>
              </div>
            </div>
            <div class="receta-field">
              <label for="indicaciones" class="receta-label"><strong>Indicaciones generales:</strong></label>
              <textarea
                id="indicaciones"
                v-model="textoRecetaMedica"
                rows="2"
                class="receta-textarea"
                placeholder="Ej: Tomar 1 comprimido cada 8 horas"
              ></textarea>
            </div>
            <button
              @click="crearRecetamedica"
              :disabled="medicamentosSeleccionados.length === 0"
              class="receta-btn"
            >
              Añadir receta médica
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import appointmentServices from '@/services/appointmentService';
import medicalrecordService from '@/services/medicalrecordService';
import prescriptionService from '@/services/prescriptionService';
import medicineService from '@/services/medicineService';
import pharmacyService from '@/services/PharmacyService';
import { id } from 'date-fns/locale';

const appointments = ref([]);
const showFichaModal = ref(false);
const fichaMedicaSeleccionada = ref(null);
const medicamentosDisponibles = ref([]);
const medicamentosSeleccionados = ref([]);
const citaSeleccionada = ref(null);
const textoRecetaMedica = ref('');
const farmaciasDisponibles = ref([]);
const farmaciaSeleccionada = ref(null);

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
    const resp = await medicineService.obtenerTodos(); // Debes tener este método
    medicamentosDisponibles.value = resp.data;
    appointments.value = response.data;
    console.log('Medicamentos disponibles:', JSON.stringify(medicamentosDisponibles.value, null, 2));
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
  appointmentServices.actualizarCita(appointment.id_citamedica, appointment)
    .then(() => {
      alert('Cita marcada como realizada.');
      // Actualiza la lista de citas
      appointments.value = appointments.value.map(a =>
        a.id_citamedica === appointment.id_citamedica ? appointment : a
      );
    })
    .catch(error => {
      console.error('Error al marcar la cita como realizada:', error);
      alert('Error al marcar la cita como realizada.');
    });
  }
    

async function verFichaMedica(appointment) {
  const ficha = appointment.fichamedica;
  if (ficha) {
    fichaMedicaSeleccionada.value = ficha;
    citaSeleccionada.value = appointment;
    try {
      // Obtener todas las farmacias y setear la primera como seleccionada
      const farmacias = await pharmacyService.getAllFarmacias();
      console.log('Farmacias obtenidas:', farmacias);
      farmaciasDisponibles.value = farmacias;
      if (farmacias.length > 0) {
        farmaciaSeleccionada.value = farmacias[0].idFarmacia || farmacias[0].id;
        console.log('Farmacia seleccionada por defecto:', farmaciaSeleccionada.value);
        await cargarMedicamentosPorFarmacia(farmaciaSeleccionada.value);
      } else {
        medicamentosDisponibles.value = [];
        console.warn('No hay farmacias disponibles.');
      }
    } catch (e) {
      medicamentosDisponibles.value = [];
      console.error('Error al obtener farmacias o medicamentos:', e);
    }
    showFichaModal.value = true;
  } else {
    alert('No hay ficha médica asociada a esta cita.');
  }
}

async function cargarMedicamentosPorFarmacia(farmaciaId) {
  console.log('Cargando medicamentos para farmaciaId:', farmaciaId);
  try {
    const farmacia = farmaciasDisponibles.value.find(f => (f.idFarmacia || f.id) == farmaciaId);
    if (!farmacia) {
      console.warn('No se encontró la farmacia seleccionada en farmaciasDisponibles:', farmaciaId);
      medicamentosDisponibles.value = [];
      return;
    }
    const farmaciaMeds = await pharmacyService.getAllByFarmacia(farmaciaId);
    console.log('Respuesta de getAllByFarmacia:', farmaciaMeds);
    medicamentosDisponibles.value = farmaciaMeds
      .filter(fm => {
        const tieneMed = !!fm.medicamento;
        if (!tieneMed) {
          console.warn('Elemento sin .medicamento:', fm);
        }
        // Filtrar por cantidad dentro de medicamento
        return tieneMed && fm.medicamento.cantidad > 0;
      })
      .map(fm => {
        const med = fm.medicamento || fm;
        return {
          ...med,
          cantidad: med.cantidad, // cantidad real del medicamento
          farmacia: farmacia?.nombre || farmacia?.nombreFarmacia || 'Farmacia',
          farmaciaId: farmaciaId
        };
      });
    console.log('Medicamentos mapeados para mostrar:', medicamentosDisponibles.value);
  } catch (e) {
    medicamentosDisponibles.value = [];
    console.error('Error al cargar medicamentos por farmacia:', e);
  }
}

async function ActualizarFichaMedica() {
  const ficha = fichaMedicaSeleccionada.value;
  if (!ficha) {
    alert('No hay ficha médica seleccionada.');
    return;
  }
  try {
    await medicalrecordService.actualizar(ficha.id_fichamedica, ficha);
    alert('Ficha médica actualizada correctamente.');
    showFichaModal.value = false;
  } catch (error) {
    alert('Error al actualizar la ficha médica.');
    console.error(error);
  }
}

async function crearRecetamedica() {
  const appointment = citaSeleccionada.value;
  if (!appointment) {
    alert('No hay cita médica seleccionada para crear una receta.');
    return;
  }
  if (medicamentosSeleccionados.value.length === 0) {
    alert('Debes seleccionar al menos un medicamento.');
    return;
  }

  // Usa la fecha de la cita agendada
  const fechaCita = appointment.horario?.fecha || appointment.fecha || appointment.horario_fecha;
  const recetaDTO = {
    fecha: fechaCita, // <-- aquí va la fecha de la cita, no la fecha actual
    id_medico: idMedico,
    id_paciente: fichaMedicaSeleccionada.value.paciente.rutPa,
    id_citamedica: citaSeleccionada.value.id_citamedica,
    medicamentos: medicamentosSeleccionados.value.filter(id => id != null),
    descripcion: textoRecetaMedica.value
  };
  console.log('Enviando recetaDTO:', JSON.stringify(recetaDTO, null, 2));
  try {
    await prescriptionService.crearConMedicamentos(recetaDTO);
    alert('Receta médica creada correctamente.');
    medicamentosSeleccionados.value = [];
  } catch (error) {
    alert('Error al crear la receta médica.');
    console.error(error);
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
  max-width: 900px;
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

/* FLEX LAYOUT PARA FICHA Y RECETA */
.ficha-medica-flex {
  display: flex;
  gap: 2.5rem;
  align-items: flex-start;
}
.ficha-medica-main {
  flex: 2;
  min-width: 0;
}
.receta-medica-side {
  flex: 1;
  min-width: 260px;
  background: transparent;
  border-radius: 12px;
  box-shadow: none;
  padding: 0;
  margin-left: 1rem;
  display: flex;
  flex-direction: column;
  align-items: stretch;
}
.receta-card {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 12px rgba(8,117,193,0.10);
  padding: 1.5rem 1.2rem 1.7rem 1.2rem;
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
  border: 1px solid #e3eafc;
}
.medicamentos-list {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem 1.5rem;
  margin-bottom: 0.5rem;
}
.receta-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #222;
  margin-bottom: 1rem;
}
.receta-subtitle {
  font-size: 1.2rem;
  font-weight: 600;
  color: #0875C1;
  margin-bottom: 0.5rem;
}
.receta-field {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.receta-label {
  font-weight: 500;
  color: #333;
}
.receta-textarea {
  width: 100%;
  min-height: 48px;
  resize: vertical;
  padding: 0.7rem;
  border-radius: 6px;
  border: 1.5px solid #e3eafc;
  font-family: inherit;
  font-size: 1rem;
  background: #f8fafd;
  transition: border 0.2s, box-shadow 0.2s;
}
.receta-textarea:focus {
  border: 1.5px solid #0875C1;
  outline: none;
  box-shadow: 0 0 0 2px #cbe7ff;
}
.receta-checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
  position: relative;
  cursor: pointer;
  user-select: none;
  padding: 0.2rem 0.5rem 0.2rem 0;
  transition: background 0.15s;
}
.receta-checkbox {
  opacity: 0;
  width: 0;
  height: 0;
  position: absolute;
}
.receta-checkbox-custom {
  height: 18px;
  width: 18px;
  background-color: #f2f6fa;
  border: 2px solid #0875C1;
  border-radius: 4px;
  display: inline-block;
  position: relative;
  transition: background 0.2s, border 0.2s;
}
.receta-checkbox:checked + .receta-checkbox-custom {
  background-color: #0875C1;
  border-color: #0875C1;
}
.receta-checkbox-custom:after {
  content: "";
  position: absolute;
  display: none;
}
.receta-checkbox:checked + .receta-checkbox-custom:after {
  display: block;
}
.receta-checkbox-custom:after {
  left: 5px;
  top: 1px;
  width: 5px;
  height: 10px;
  border: solid #fff;
  border-width: 0 2.5px 2.5px 0;
  transform: rotate(45deg);
  content: "";
  position: absolute;
}
.receta-med-name {
  font-weight: 500;
  color: #222;
}
.receta-btn {
  margin-top: 0.7rem;
  width: 100%;
  font-size: 1.08rem;
  padding: 0.7rem 0;
  border-radius: 8px;
  background: linear-gradient(90deg, #0875C1 60%, #C51A6F 100%);
  color: #fff;
  font-weight: 700;
  border: none;
  box-shadow: 0 2px 8px rgba(8,117,193,0.10);
  transition: background 0.2s, box-shadow 0.2s;
}
.receta-btn:disabled {
  background: #b3cbe3;
  color: #fff;
  cursor: not-allowed;
  opacity: 0.7;
}
.receta-btn:not(:disabled):hover {
  background: linear-gradient(90deg, #C51A6F 60%, #0875C1 100%);
  box-shadow: 0 4px 16px rgba(197,26,111,0.10);
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
  .ficha-medica-flex {
    flex-direction: column;
    gap: 1rem;
  }
  .receta-medica-side {
    margin-left: 0;
    margin-top: 1.5rem;
    width: 100%;
    min-width: 0;
    padding: 0;
  }
  .ficha-medica-main {
    width: 100%;
  }

  .ficha-section {
    margin-bottom: 1rem;
    padding-bottom: 0.5rem;
    border-bottom: 1px solid #eee;
  }

  .ficha-actions {
    justify-content: center;
  }

  .ficha-actions button {
    width: 100%;
    margin-top: 0.5rem;
  }

  .ficha-row {
    flex-direction: column;
  }

  .ficha-row div {
    margin-bottom: 0.5rem;
  }

  .ficha-row div:last-child {
    margin-bottom: 0;
  }

  .ficha-row input {
    width: 100%;
  }

  .ficha-field textarea {
    width: 100%;
  }

  .ficha-field label {
    margin-bottom: 0.3rem;
    font-weight: 600;
  }

  .ficha-field {
    margin-bottom: 1rem;
  }

  .ficha-clinica-columna {
    flex-direction: column;
  }
}
</style>