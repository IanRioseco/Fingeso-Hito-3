<template>
  <div class="modal">
    <div class="modal-content">
      <h3>Configurar Disponibilidad</h3>
      <form @submit.prevent="guardarDisponibilidad">
        <label class="label-container">Fecha:
          <input v-model="fecha" required type="date" />
        </label>
        <label class="label-container">Hora inicio:
          <input v-model="horaInicio" required type="time" />
        </label>
        <label class="label-container">Hora fin:
          <input v-model="horaFin" required type="time" />
        </label>
        <div class="buttons-container">
          <button class="btn-save" type="submit">Guardar</button>
          <button class="btn-close" type="button" @click="$emit('close')">Cerrar</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
//IMPORTACIONES
import { ref } from 'vue'
import horarioService from '@/services/horarioService'

// Composición del componente
// Variables reactivas para la fecha y horas
const fecha = ref('')
const horaInicio = ref('')
const horaFin = ref('')

// Definición de eventos para emitir al componente padre
const emit = defineEmits(['close', 'save'])

// Función para guardar la disponibilidad
function guardarDisponibilidad() {
  //json.parse para obtener el usuario del localStorage
  const user = JSON.parse(localStorage.getItem('user'))
  // Verifica si el usuario está autenticado
  const idMedico = user?.idmedico || user?.usuario?.idmedico
  if (!idMedico) {
    alert('No se encontró el ID del médico autenticado')
    return
  }
  // Solo emite el rango en el formato esperado
  emit('save', [{
    date: fecha.value,
    startTime: horaInicio.value,
    endTime: horaFin.value
  }])
  emit('close')// Maneja errores en la creación de la disponibilidad
}
  
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;

}
.buttons-container {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
  justify-content: center;
}
.btn-close {
  background-color: #0875C1;
  color: #fff;
  font-weight: bold;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}
.btn-save {
  background-color: #C51A6F;
  color: #fff;
  font-weight: bold;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}
.btn-close:hover, .btn-save:hover {
  opacity: 0.9;
}
.label-container {
  padding: 0.5rem;
  margin-bottom: 1rem;
  font-weight: bold;
}
</style>