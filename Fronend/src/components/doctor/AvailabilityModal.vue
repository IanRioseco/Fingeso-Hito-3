<template>
  <div class="modal">
    <div class="modal-content">
      <h3>Configurar Disponibilidad</h3>
      <form @submit.prevent="guardarDisponibilidad">
        <label>Fecha:
          <input v-model="fecha" required type="date" />
        </label>
        <label>Hora inicio:
          <input v-model="horaInicio" required type="time" />
        </label>
        <label>Hora fin:
          <input v-model="horaFin" required type="time" />
        </label>
        <button class="btn-save" type="submit">Guardar</button>
      </form>
      <button class="btn-close" @click="$emit('close')">Cerrar</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import horarioService from '@/services/horarioService'

const fecha = ref('')
const horaInicio = ref('')
const horaFin = ref('')

const emit = defineEmits(['close', 'save'])

function guardarDisponibilidad() {
  const user = JSON.parse(localStorage.getItem('user'))
  const idMedico = user?.idmedico || user?.usuario?.idmedico
  if (!idMedico) {
    alert('No se encontró el ID del médico autenticado')
    return
  }
  const disponibilidad = {
    fecha: fecha.value, // yyyy-MM-dd
    horainicio: horaInicio.value,
    horafin: horaFin.value,
    medico: {
      idmedico: idMedico
    }
  }
  horarioService.crear(disponibilidad)
    .then(() => {
      emit('save') // Notifica al calendario para refrescar
      emit('close') // Cierra el modal
    })
    .catch(err => {
      alert('Error al guardar')
      console.error(err)
    })
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
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
}
.btn-close {
  background-color: #0875C1;
  color: #fff;
  font-weight: bold;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
}
.btn-save {
  background-color: #C51A6F;
  color: #fff;
  font-weight: bold;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
}
</style>