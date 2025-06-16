<template>
  <div class="medicine-list">
    <h3>Lista de Medicamentos</h3>
    <ul>
      <li v-for="item in filteredMedicines" :key="item.idFarmaciaMedicamento || item.Id_farmacia_medicamento || item.id || item.ID">
        <strong>{{ item.medicamento?.nombre }}</strong>
        ({{ item.medicamento?.cantidad }} unidades) - {{ item.medicamento?.descripcion }}
        <span> | Tipo: {{ item.medicamento?.tipo }}</span>
        <span v-if="item.medicamento?.marca"> | Marca: {{ item.medicamento?.marca }}</span>
        <span> | Vence: {{ item.medicamento?.fecha_vencimieto }}</span>
        <div v-if="item.medicamento?.cantidad <= 10" class="alert">⚠ Medicamento por agotarse</div>
        <button @click="editMedicine(item)">Editar</button>
        <button @click="deleteFarmaciaMedicamento(item)">Eliminar</button>
        <div v-if="editingId === (item.idFarmaciaMedicamento || item.Id_farmacia_medicamento || item.id || item.ID)">
          <form @submit.prevent="saveEdit(item)">
            <input v-model="editData.nombre" placeholder="Nombre" required />
            <input v-model="editData.tipo" placeholder="Tipo" required />
            <input v-model="editData.marca" placeholder="Marca" />
            <input v-model.number="editData.cantidad" type="number" placeholder="Cantidad" min="0" required />
            <input v-model="editData.fecha_vencimieto" type="date" placeholder="Fecha de Vencimiento" required />
            <textarea v-model="editData.descripcion" placeholder="Descripción" required></textarea>
            <button type="submit">Guardar</button>
            <button type="button" @click="cancelEdit">Cancelar</button>
          </form>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed } from 'vue'
import PharmacyService from '@/services/PharmacyService'

const props = defineProps({
  medicines: Array,
  search: {
    type: String,
    default: ''
  }
})
const emit = defineEmits(['reload'])

const editingId = ref(null)
const editData = ref({})

const filteredMedicines = computed(() => {
  if (!props.search) return props.medicines;
  return props.medicines.filter(item =>
    item.medicamento?.nombre?.toLowerCase().includes(props.search.toLowerCase())
  );
});

const editMedicine = (item) => {
  editingId.value = item.idFarmaciaMedicamento || item.Id_farmacia_medicamento || item.id || item.ID
  editData.value = {
    nombre: item.medicamento?.nombre || '',
    tipo: item.medicamento?.tipo || '',
    marca: item.medicamento?.marca || '',
    cantidad: item.medicamento?.cantidad || 0,
    fecha_vencimieto: item.medicamento?.fecha_vencimieto || '',
    descripcion: item.medicamento?.descripcion || ''
  }
}

const cancelEdit = () => {
  editingId.value = null
  editData.value = {}
}

const saveEdit = async (item) => {
  try {
    // Actualiza el medicamento en el backend
    await PharmacyService.updateMedicine(item.medicamento.idmedicamento || item.medicamento.id, editData.value)
    editingId.value = null
    emit('reload')
  } catch (e) {
    alert('Error al editar medicamento')
    console.error(e)
  }
}

const getFarmaciaMedicamentoId = (item) => {
  // Prueba todas las variantes posibles de ID
  return (
    item.idFarmaciaMedicamento ||
    item.Id_farmacia_medicamento ||
    item.id_farmacia_medicamento ||
    item.id ||
    item.ID
  );
};

const deleteFarmaciaMedicamento = async (item) => {
  const id = getFarmaciaMedicamentoId(item);
  if (!id) {
    console.error('Estructura del item al eliminar:', item);
    alert('ID de farmacia-medicamento no encontrado. Revisa la estructura de los datos.');
    return;
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
