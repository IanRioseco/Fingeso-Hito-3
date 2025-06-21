<template>
  <div class="medicine-form">
    <h3>Agregar Medicamento</h3>
    <form @submit.prevent="addMedicine">
      <input
        class="form-control-name"
        v-model="nombre"
        placeholder="Nombre"
        required
        maxlength="40"
        pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+"
        title="Solo letras y espacios"
        @input="nombre = nombre.replace(/[^A-Za-zÁÉÍÓÚáéíóúÑñ\s]/g, '').slice(0,40)"
      />
      <input
        class="form-control-tipo"
        v-model="tipo"
        placeholder="Tipo"
        required
        maxlength="30"
        pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+"
        title="Solo letras y espacios"
        @input="tipo = tipo.replace(/[^A-Za-zÁÉÍÓÚáéíóúÑñ\s]/g, '').slice(0,30)"
      />
      <input
        class="form-control-marca"
        v-model="marca"
        placeholder="Marca"
        maxlength="30"
        @input="marca = marca.slice(0,30)"
      />
      <input
        class="form-control-cant"
        v-model.number="cantidad"
        type="number"
        placeholder="Cantidad"
        min="1"
        max="99999"
        required
      />
      <input
        class="form-control-fecha"
        v-model="fecha_vencimieto"
        type="date"
        placeholder="Fecha de Vencimiento"
        :min="minDate"
        required
      />
      <textarea
        class="form-control-desc"
        v-model="descripcion"
        placeholder="Descripción"
        maxlength="200"
        required
        @input="descripcion = descripcion.slice(0,200)"
      ></textarea>
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
const cantidad = ref(1)
const fecha_vencimieto = ref('')
const descripcion = ref('')

// Fecha mínima para el input de fecha (hoy)
const minDate = new Date().toISOString().split('T')[0]

const addMedicine = async () => {
  try {
    const medRes = await PharmacyService.create({
      nombre: nombre.value,
      tipo: tipo.value,
      marca: marca.value,
      cantidad: cantidad.value,
      fecha_vencimieto: fecha_vencimieto.value,
      descripcion: descripcion.value
    })
    const medicamentoId = medRes.data?.idmedicamento || medRes.idmedicamento
    const user = authService.getCurrentUser()
    const usuario = user?.usuario || user
    const farmaciaId = usuario?.farmacia?.idFarmacia || usuario?.idFarmacia
    if (farmaciaId && medicamentoId) {
      await PharmacyService.addMedicamentoToFarmacia(farmaciaId, medicamentoId)
    } else {
      alert('No se pudo asociar el medicamento a la farmacia. Verifica los IDs.')
    }
    emit('medAdded')
    nombre.value = tipo.value = marca.value = descripcion.value = ''
    cantidad.value = 1
    fecha_vencimieto.value = ''
  } catch (e) {
    alert('Error al guardar medicamento')
    console.error(e)
  }
}
</script>

<style scoped>
.medicine-form {
  padding: 2rem 2.5rem;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 16px rgba(8,117,193,0.07);
  max-width: 480px;
  margin: 2rem auto;
}
.medicine-form h3 {
  color: #0875C1;
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
  text-align: center;
}
.medicine-form input,
.medicine-form textarea {
  width: 100%;
  padding: 0.85rem 1rem;
  border: 1.5px solid #C51A6F;
  border-radius: 6px;
  font-size: 1.08rem;
  margin-bottom: 1.1rem;
  background: #f8fafd;
  transition: border 0.2s;
  box-shadow: 0 1px 6px rgba(8,117,193,0.03);
}
.medicine-form input:focus,
.medicine-form textarea:focus {
  outline: none;
  border-color: #0875C1;
  background: #fff;
}
.medicine-form textarea {
  min-height: 70px;
  max-height: 180px;
  resize: vertical;
}
.btn-primary {
  background-color: #C51A6F;
  color: #fff;
  border: none;
  padding: 0.7rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  font-size: 1.1rem;
  box-shadow: 0 2px 8px rgba(197,26,111,0.07);
  transition: background 0.2s;
  width: 100%;
  margin-top: 0.5rem;
}
.btn-primary:hover {
  background-color: #0875C1;
}
</style>
