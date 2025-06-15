<template>
  <div class="medicine-form">
    <h3>Agregar Medicamento</h3>
    <form @submit.prevent="addMedicine">
      <input class="form-control-name" v-model="nombre" placeholder="Nombre" required />
      <input class="form-control-desc" v-model="descripcion" placeholder="Descripción" required />
      <input class="form-control-cant" v-model.number="cantidad" type="number" placeholder="Cantidad" min="0" required />
      <textarea class="form-control-espec" v-model="especificaciones" placeholder="Especificaciones"></textarea>
      <button class="btn-primary" type="submit">Guardar</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import pharmacyService from '@/services/pharmacyService'

const emit = defineEmits(['medAdded'])

const nombre = ref('')
const descripcion = ref('')
const cantidad = ref(0)
const especificaciones = ref('')

const addMedicine = async () => {
  try {
    await pharmacyService.create({
      nombre: nombre.value,
      descripcion: descripcion.value,
      cantidad: cantidad.value,
      especificaciones: especificaciones.value
    })
    emit('medAdded')
    nombre.value = descripcion.value = especificaciones.value = ''
    cantidad.value = 0
  } catch (e) {
    alert('Error al guardar medicamento')
    console.error(e)
  }
}
</script>

<style scoped>
.medicine-form {
  padding: 1rem;
  background-color: #f9f9f9;
  border-radius: 8px;
}
.medicine-form h3 {
  color: #0875C1;
}
.medicine-form input, .medicine-form textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}
.medicine-form button {
  background-color: #C51A6F;
  color: #fff;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}
.medicine-form button:hover {
  background-color: #099;
}

</style>
