<template>
  <div class="medicine-list">
    <h3>Lista de Medicamentos</h3>
    <ul>
      <li v-for="item in medicines" :key="item.idFarmaciaMedicamento || item.Id_farmacia_medicamento || item.id || item.ID">
        <pre>{{ item }}</pre>
        <strong>{{ item.medicamento?.nombre }}</strong>
        ({{ item.medicamento?.cantidad }} unidades) - {{ item.medicamento?.descripcion }}
        <span> | Tipo: {{ item.medicamento?.tipo }}</span>
        <span v-if="item.medicamento?.marca"> | Marca: {{ item.medicamento?.marca }}</span>
        <span> | Vence: {{ item.medicamento?.fecha_vencimieto }}</span>
        <div v-if="item.medicamento?.cantidad <= 10" class="alert">⚠ Medicamento por agotarse</div>
        <button @click="deleteFarmaciaMedicamento(item.idFarmaciaMedicamento || item.Id_farmacia_medicamento || item.id || item.ID)">Eliminar</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, onMounted, watch } from 'vue'
import PharmacyService from '@/services/PharmacyService'

const props = defineProps({
  medicines: Array
})

const emit = defineEmits(['reload'])

watch(() => props.medicines, (newVal) => {
  console.log('Medicines updated:', newVal)
}, { immediate: true })

onMounted(() => {
  // Mostrar la estructura de los datos para depuración
  console.log('Medicines:', props.medicines)
})

const deleteFarmaciaMedicamento = async (id) => {
  if (!id) {
    alert('ID de farmacia-medicamento no encontrado. Revisa la estructura de los datos.')
    return
  }
  try {
    await PharmacyService.deleteFarmaciaMedicamento(id)
    emit('reload')
  } catch (e) {
    alert('Error al eliminar medicamento de la farmacia')
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
  flex-direction: column;
  gap: 0.2rem;
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
