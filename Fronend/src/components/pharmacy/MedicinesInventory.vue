<template>
  <div class="medicine-list">
    <h3>Lista de Medicamentos</h3>
    <ul>
      <li v-for="item in medicines" :key="item.id_farmacia_medicamento">
        <template v-if="editId === item.id_farmacia_medicamento">
          <input v-model="editData.nombre" placeholder="Nombre" />
          <input v-model="editData.tipo" placeholder="Tipo" />
          <input v-model="editData.marca" placeholder="Marca" />
          <input v-model.number="editData.cantidad" type="number" placeholder="Cantidad" />
          <input v-model="editData.fecha_vencimieto" type="date" placeholder="Fecha de Vencimiento" />
          <input v-model="editData.descripcion" placeholder="Descripción" />
          <button @click="saveEdit(item)">Guardar</button>
          <button @click="cancelEdit">Cancelar</button>
        </template>
        <template v-else>
          <strong>{{ item.medicamento.nombre }}</strong>
          <span> ({{ item.medicamento.cantidad }} unidades) - {{ item.medicamento.descripcion }}</span>
          <span> | Tipo: {{ item.medicamento.tipo }}</span>
          <span v-if="item.medicamento.marca"> | Marca: {{ item.medicamento.marca }}</span>
          <span> | Vence: {{ item.medicamento.fecha_vencimieto }}</span>
          <div v-if="item.medicamento.cantidad <= 10" class="alert">⚠ Medicamento por agotarse</div>
          <button @click="deleteFarmaciaMedicamento(item.id_farmacia_medicamento)">Eliminar</button>
          <button @click="startEdit(item)">Editar</button>
        </template>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref } from 'vue'
import PharmacyService from '@/services/PharmacyService'

const props = defineProps({
  medicines: Array
})

const emit = defineEmits(['reload'])

const editId = ref(null)
const editData = ref({})

const startEdit = (item) => {
  editId.value = item.id_farmacia_medicamento
  editData.value = {
    nombre: item.medicamento.nombre,
    tipo: item.medicamento.tipo,
    marca: item.medicamento.marca,
    cantidad: item.medicamento.cantidad,
    fecha_vencimieto: item.medicamento.fecha_vencimieto,
    descripcion: item.medicamento.descripcion
  }
}

const cancelEdit = () => {
  editId.value = null
  editData.value = {}
}

const saveEdit = async (item) => {
  try {
    // Construir el objeto completo como espera el backend
    const body = {
      id_farmacia_medicamento: item.id_farmacia_medicamento,
      farmacia: item.farmacia, // mantener referencia
      medicamento: {
        idmedicamento: item.medicamento.idmedicamento,
        nombre: editData.value.nombre,
        tipo: editData.value.tipo,
        marca: editData.value.marca,
        cantidad: editData.value.cantidad,
        fecha_vencimieto: editData.value.fecha_vencimieto,
        descripcion: editData.value.descripcion
      }
    }
    await PharmacyService.updateFarmaciaMedicamento(item.id_farmacia_medicamento, body)
    emit('reload')
    cancelEdit()
  } catch (e) {
    alert('Error al editar medicamento')
    console.error(e)
  }
}

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
