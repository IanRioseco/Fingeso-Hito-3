<template>
  <div class="medicine-form">
    <h3>Agregar Medicamento</h3>
    <form @submit.prevent="addMedicine">
      <input class="form-control-name" v-model="nombre" placeholder="Nombre" required />
      <input class="form-control-tipo" v-model="tipo" placeholder="Tipo" required />
      <input class="form-control-marca" v-model="marca" placeholder="Marca" />
      <input class="form-control-cant" v-model.number="cantidad" type="number" placeholder="Cantidad" min="0" required />
      <input class="form-control-fecha" v-model="fecha_vencimieto" type="date" placeholder="Fecha de Vencimiento" required />
      <textarea class="form-control-desc" v-model="descripcion" placeholder="Descripción" required></textarea>
      <button class="btn-primary" type="submit">Guardar</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PharmacyService from '@/services/PharmacyService'
import { authService } from '@/services/auth.service'

const emit = defineEmits(['medAdded'])

const nombre = ref('')
const tipo = ref('')
const marca = ref('')
const cantidad = ref(0)
const fecha_vencimieto = ref('')
const descripcion = ref('')

const addMedicine = async () => {
  try {
    // 1. Crear el medicamento con todos los campos requeridos
    const medRes = await PharmacyService.create({
      nombre: nombre.value,
      tipo: tipo.value,
      marca: marca.value,
      cantidad: cantidad.value,
      fecha_vencimieto: fecha_vencimieto.value,
      descripcion: descripcion.value
    })
    // 2. Obtener el id del medicamento creado
    const medicamentoId = medRes.data?.idmedicamento || medRes.idmedicamento
    // 3. Obtener la farmacia del usuario autenticado
    const user = authService.getCurrentUser()
    const usuario = user?.usuario || user
    const farmaciaId = usuario?.farmacia?.idFarmacia || usuario?.idFarmacia
    console.log('farmaciaId', farmaciaId, 'medicamentoId', medicamentoId)
    if (farmaciaId && medicamentoId) {
      const res = await PharmacyService.addMedicamentoToFarmacia(farmaciaId, medicamentoId)
      console.log('Respuesta addMedicamentoToFarmacia:', res)
    } else {
      alert('No se pudo asociar el medicamento a la farmacia. Verifica los IDs.')
    }
    emit('medAdded')
    nombre.value = tipo.value = marca.value = descripcion.value = ''
    cantidad.value = 0
    fecha_vencimieto.value = ''
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
