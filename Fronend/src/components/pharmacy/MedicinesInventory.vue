<template>
  <div class="medicine-list">
    <h3>Lista de Medicamentos</h3>
    <div class="medicine-scroll">
      <ul>
        <li v-for="item in uniqueFilteredMedicines" :key="getFarmaciaMedicamentoId(item)">
          <strong>{{ item.medicamento?.nombre }}</strong>
          ({{ item.medicamento?.cantidad }} unidades) - {{ item.medicamento?.descripcion }}
          <span> | Tipo: {{ item.medicamento?.tipo }}</span>
          <span v-if="item.medicamento?.marca"> | Marca: {{ item.medicamento?.marca }}</span>
          <span> | Vence: {{ item.medicamento?.fecha_vencimieto }}</span>
          <div v-if="item.medicamento?.cantidad <= 10" class="alert">⚠ Medicamento por agotarse</div>
          <div class="button-row" v-if="editingId !== getFarmaciaMedicamentoId(item)">
            <button 
              @click="editMedicine(item)" 
              :disabled="editingId !== null">
              Editar
            </button>
            <button @click="deleteFarmaciaMedicamento(item)" :disabled="editingId !== null">Eliminar</button>
          </div>
          <transition name="fade">
            <div v-if="editingId === getFarmaciaMedicamentoId(item)" class="edit-form-container">
              <form @submit.prevent="saveEdit(item)" class="edit-form">
                <div class="form-row">
                  <label>Nombre:</label>
                  <input v-model="editData.nombre" placeholder="Nombre" required />
                </div>
                <div class="form-row">
                  <label>Tipo:</label>
                  <input v-model="editData.tipo" placeholder="Tipo" required />
                </div>
                <div class="form-row">
                  <label>Marca:</label>
                  <input v-model="editData.marca" placeholder="Marca" />
                </div>
                <div class="form-row">
                  <label>Cantidad:</label>
                  <input v-model.number="editData.cantidad" type="number" placeholder="Cantidad" min="0" required />
                </div>
                <div class="form-row">
                  <label>Fecha de Vencimiento:</label>
                  <input v-model="editData.fecha_vencimieto" type="date" placeholder="Fecha de Vencimiento" required />
                </div>
                <div class="form-row">
                  <label>Descripción:</label>
                  <textarea v-model="editData.descripcion" placeholder="Descripción" required></textarea>
                </div>
                <div class="form-actions">
                  <button type="submit" class="btn-guardar">Guardar</button>
                  <button type="button" class="btn-cancelar" @click="cancelEdit">Cancelar</button>
                </div>
              </form>
            </div>
          </transition>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed } from 'vue'
import PharmacyService from '@/services/PharmacyService'
//DATOS REACTIVOS PARA EL COMPONENTE
const props = defineProps({
  medicines: Array,
  search: {
    type: String,
    default: ''
  }
})
//emisiones de eventos
const emit = defineEmits(['reload'])
//DATOS REACTIVOS PARA EL FORMULARIO
const editingId = ref(null)
const editData = ref({})

const getFarmaciaMedicamentoId = (item) => {
  return (
    item.idFarmaciaMedicamento ||
    item.Id_farmacia_medicamento ||
    item.id_farmacia_medicamento ||
    item.id ||
    item.ID
  );
};

const filteredMedicines = computed(() => {
  if (!props.search) return props.medicines;
  return props.medicines.filter(item =>
    item.medicamento?.nombre?.toLowerCase().includes(props.search.toLowerCase())
  );
});

// Elimina duplicados por idFarmaciaMedicamento o id
const uniqueFilteredMedicines = computed(() => {
  const seen = new Set();
  return filteredMedicines.value.filter(item => {
    const id = getFarmaciaMedicamentoId(item);
    if (seen.has(id)) return false;
    seen.add(id);
    return true;
  });
});

const editMedicine = (item) => {
  editingId.value = getFarmaciaMedicamentoId(item)
  editData.value = {
    // Obtiene los datos del medicamento
    nombre: item.medicamento?.nombre || '',
    tipo: item.medicamento?.tipo || '',
    marca: item.medicamento?.marca || '',
    cantidad: item.medicamento?.cantidad || 0,
    fecha_vencimieto: item.medicamento?.fecha_vencimieto || '',
    descripcion: item.medicamento?.descripcion || ''
  }
}
// Función para cancelar la edición del formulario
const cancelEdit = () => {
  editingId.value = null
  editData.value = {}
}
// Función para guardar los cambios en el formulario
const saveEdit = async (item) => {
  try {
    await PharmacyService.updateMedicine(item.medicamento.idmedicamento || item.medicamento.id, editData.value)
    editingId.value = null
    emit('reload')
    // atrapado de errores
  } catch (e) {
    alert('Error al editar medicamento')
    console.error(e)
  }
}
const deleteFarmaciaMedicamento = async (item) => {
  // Obtiene el ID del medicamento
  const id = getFarmaciaMedicamentoId(item);
  // Verifica que el ID del medicamento sea valido
  if (!id) {
    console.error('Estructura del item al eliminar:', item);
    alert('ID de farmacia-medicamento no encontrado. Revisa la estructura de los datos.');
    return;
  }
  // Intenta eliminar el medicamento de la farmacia
  try {
    await PharmacyService.deleteFarmaciaMedicamento(id)
    emit('reload')
    // atrapado de errores
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
.medicine-scroll {
  max-height: 400px;
  overflow-y: auto;
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
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.03);
}
.button-row {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}
.medicine-list button {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  min-width: 90px;
  transition: background 0.2s;
}
.medicine-list button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
.medicine-list button:hover:not(:disabled) {
  background-color: #c0392b;
}
.edit-form-container {
  background: #f4f8fb;
  border: 1px solid #b3d1e7;
  border-radius: 8px;
  padding: 1rem;
  margin-top: 0.5rem;
  box-shadow: 0 2px 8px rgba(8,117,193,0.08);
}
.edit-form {
  display: flex;
  flex-direction: column;
  gap: 0.7rem;
}
.form-row {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}
.form-row label {
  font-weight: 500;
  color: #0875C1;
  margin-bottom: 0.1rem;
}
.form-actions {
  display: flex;
  gap: 0.7rem;
  justify-content: flex-end;
}
.btn-guardar {
  background-color: #0875C1;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1.2rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}
.btn-guardar:hover {
  background-color: #065a99;
}
.btn-cancelar {
  background-color: #e74c3c;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1.2rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}
.btn-cancelar:hover {
  background-color: #c0392b;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>
