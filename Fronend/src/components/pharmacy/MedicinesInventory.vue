<template>
  <div class="medicine-list">
    <h3>Lista de Medicamentos</h3>
    <div class="medicine-scroll">
      <ul>
        <li v-for="item in uniqueFilteredMedicines" :key="getFarmaciaMedicamentoId(item)" class="medicine-card">
          <div class="medicine-header">
            <strong class="medicine-name">{{ item.medicamento?.nombre }}</strong>
            <span class="badge" v-if="item.medicamento?.cantidad <= 10">⚠ Por agotarse</span>
          </div>
          <div class="medicine-details-grid">
            <div class="detail-box cantidad">
              <span class="label">Cantidad</span>
              <span class="value">{{ item.medicamento?.cantidad }} unidades</span>
            </div>
            <div class="detail-box tipo">
              <span class="label">Tipo</span>
              <span class="value">{{ item.medicamento?.tipo }}</span>
            </div>
            <div class="detail-box marca" v-if="item.medicamento?.marca">
              <span class="label">Marca</span>
              <span class="value">{{ item.medicamento?.marca }}</span>
            </div>
            <div class="detail-box vence">
              <span class="label">Vence</span>
              <span class="value">{{ item.medicamento?.fecha_vencimieto }}</span>
            </div>
            <div class="detail-box descripcion-box">
              <span class="label">Descripción</span>
              <span class="value">{{ item.medicamento?.descripcion }}</span>
            </div>
          </div>
          <div class="button-row" v-if="editingId === null || editingId !== getFarmaciaMedicamentoId(item)">
            <button class="btn-edit"
              @click="editMedicine(item)" 
              :disabled="editingId !== null && editingId !== getFarmaciaMedicamentoId(item)">
              Editar
            </button>
            <button class="btn-delete"
              @click="deleteFarmaciaMedicamento(item)"
              :disabled="editingId !== null && editingId !== getFarmaciaMedicamentoId(item)">
              Eliminar
            </button>
          </div>
          <transition name="fade">
            <div v-if="editingId === getFarmaciaMedicamentoId(item)" class="edit-form-container">
              <form class="edit-form" @submit.prevent="saveEdit(item)">
                <transition name="fade">
                  <div v-if="editingId === getFarmaciaMedicamentoId(item)" class="edit-form-container">
                    <form class="edit-form" @submit.prevent="saveEdit(item)">
                      <div class="form-row">
                        <label>Nombre</label>
                        <input v-model="editData.nombre" required maxlength="40" />
                      </div>
                      <div class="form-row">
                        <label>Tipo</label>
                        <input v-model="editData.tipo" required maxlength="30" />
                      </div>
                      <div class="form-row">
                        <label>Marca</label>
                        <input v-model="editData.marca" maxlength="30" />
                      </div>
                      <div class="form-row">
                        <label>Cantidad</label>
                        <input v-model.number="editData.cantidad" type="number" min="0" max="99999" required />
                      </div>
                      <div class="form-row">
                        <label>Vence</label>
                        <input v-model="editData.fecha_vencimieto" type="date" :min="minDate" required />
                      </div>
                      <div class="form-row">
                        <label>Descripción</label>
                        <textarea v-model="editData.descripcion" maxlength="200" required></textarea>
                      </div>
                      <div class="form-actions">
                        <button class="btn-guardar" type="submit">Guardar</button>
                        <button class="btn-cancelar" type="button" @click="cancelEdit">Cancelar</button>
                      </div>
                    </form>
                  </div>
                </transition>
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

// Fecha mínima para el input de fecha (hoy)
const minDate = new Date().toISOString().split('T')[0]

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
    await PharmacyService.updateMedicine(item.medicamento.idmedicamento || item.medicamento.id, editData.value)
    editingId.value = null
    emit('reload')
  } catch (e) {
    alert('Error al editar medicamento')
    console.error(e)
  }
}
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
.medicine-list {
  padding: 2rem 2.5rem;
  background-color: #f8fafd;
  border-radius: 16px;
  box-shadow: 0 2px 16px rgba(8,117,193,0.07);
  max-width: 700px;
  margin: 2rem auto;
}
.medicine-list h3 {
  color: #0875C1;
  margin-bottom: 1.5rem;
  text-align: center;
  font-size: 1.5rem;
}
.medicine-scroll {
  max-height: 420px;
  overflow-y: auto;
  padding-right: 6px;
}
.medicine-list ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}
.medicine-card {
  margin-bottom: 1.2rem;
  padding: 1.2rem 1rem 1rem 1rem;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(8,117,193,0.04);
  border: 1.5px solid #e3eafc;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  position: relative;
  transition: box-shadow 0.2s;
}
.medicine-card:hover {
  box-shadow: 0 4px 20px rgba(197,26,111,0.08);
}
.medicine-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 0.2rem;
}
.medicine-name {
  font-size: 1.15rem;
  color: #0875C1;
  font-weight: bold;
}
.badge {
  background: #ffe0e6;
  color: #C51A6F;
  font-weight: bold;
  border-radius: 8px;
  padding: 0.2rem 0.7rem;
  font-size: 0.95rem;
  margin-left: 0.5rem;
}
.button-row {
  display: flex;
  gap: 0.7rem;
  margin-bottom: 0.5rem;
}
.btn-edit {
  background-color: #0875C1;
  color: white;
  border: none;
  padding: 0.5rem 1.1rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s;
}
.btn-edit:hover {
  background-color: #065a99;
}
.btn-delete {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 0.5rem 1.1rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s;
}
.btn-delete:hover {
  background-color: #c0392b;
}
.medicine-list button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
.edit-form-container {
  background: #f4f8fb;
  border: 1.5px solid #b3d1e7;
  border-radius: 10px;
  padding: 1.2rem 1rem;
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
.form-row input,
.form-row textarea {
  padding: 0.5rem;
  border-radius: 4px;
  border: 1.2px solid #ccc;
  font-size: 1rem;
  background: #f8fafd;
  transition: border 0.2s;
}
.form-row input:focus,
.form-row textarea:focus {
  border-color: #0875C1;
  outline: none;
  background: #fff;
}
.form-actions {
  display: flex;
  gap: 0.7rem;
  justify-content: flex-end;
  margin-top: 0.5rem;
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


.medicine-details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 0.7rem;
  margin-bottom: 0.5rem;
}
.detail-box {
  background: #f4f8fb;
  border: 1.2px solid #e3eafc;
  border-radius: 8px;
  padding: 0.5rem 0.7rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  min-width: 0;
  min-height: 48px;
  box-sizing: border-box;
}
.detail-box .label {
  font-size: 0.88rem;
  color: #0875C1;
  font-weight: 600;
  margin-bottom: 0.1rem;
  letter-spacing: 0.5px;
}
.detail-box .value {
  font-size: 1.05rem;
  color: #444;
  font-weight: 500;
  word-break: break-word;
}
.descripcion-box {
  grid-column: 1 / -1;
  margin-top: 0.3rem;
  background: #f9f3fa;
  border: 1.2px solid #e3eafc;
  min-height: 48px;
}
.descripcion-box .label {
  color: #C51A6F;
}
.descripcion-box .value {
  color: #444;
  font-size: 1rem;
  margin-top: 0.1rem;
  word-break: break-word;
}
</style>