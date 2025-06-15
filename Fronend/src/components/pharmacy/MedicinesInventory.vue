<template>
  <div class="medicine-list">
    <h3>Lista de Medicamentos</h3>
    <ul>
      <li v-for="med in medicines" :key="med.id">
        <strong>{{ med.nombre }}</strong> ({{ med.cantidad }} unidades) - {{ med.descripcion }}
        <div v-if="med.cantidad <= 10" class="alert">⚠ Medicamento por agotarse</div>
        <button @click="deleteMed(med.id)">Eliminar</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import pharmacyService from '@/services/PharmacyService'

const props = defineProps({
  medicines: Array
})

const emit = defineEmits(['reload'])

const deleteMed = async (id) => {
  try {
    await pharmacyService.delete(id)
    emit('reload')
  } catch (e) {
    alert('Error al eliminar medicamento')
    console.error(e)
  }
}
</script>

<style scoped>
.alert {
  color: red;
  font-weight: bold;
}
.medicine-list {
  padding: 1rem;
  background-color: #f9f9f9;
  border-radius: 8px;
}
.medicine-list h3 {
  color: #0875C1;
}
.medicine-list ul {
  list-style-type: none;
  padding: 0;
}
.medicine-list li {
  margin: 0.5rem 0;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.medicine-list button {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}
.medicine-list button:hover {
  background-color: #c0392b;
}
</style>
