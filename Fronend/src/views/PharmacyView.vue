<template>
  <div class="pharmacy-dashboard">
    <h2>Gestión de Medicamentos</h2>
    <MedicineForm @medAdded="loadMedicines" />
    <MedicineList :medicines="medicines" @reload="loadMedicines" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import MedicineForm from '@/components/pharmacy/MedicineForm.vue'
import MedicineList from '@/components/pharmacy/MedicinesInventory.vue'
import pharmacyService from '@/services/PharmacyService'

const medicines = ref([])

const loadMedicines = async () => {
  try {
    medicines.value = await pharmacyService.getAll()
  } catch (e) {
    console.error('Error al cargar medicamentos', e)
  }
}

onMounted(loadMedicines)
</script>

<style scoped>
.pharmacy-dashboard {
  padding: 2rem;
}
</style>
