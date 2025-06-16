<template>
  <div class="pharmacy-dashboard">
    <header class="pharmacy-header">
      <div>
        <h2>Gestión de Medicamentos</h2>
        <div class="pharmacy-info">
          <span v-if="farmaciaNombre">Farmacia: <b>{{ farmaciaNombre }}</b></span>
          <span v-if="farmaceuticoNombre">Farmacéutico: <b>{{ farmaceuticoNombre }}</b></span>
        </div>
      </div>
      <button class="logout-btn" @click="logout">Cerrar sesión</button>
    </header>
    <MedicineForm @medAdded="loadMedicines" />
    <MedicineList :medicines="medicines" @reload="loadMedicines" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import MedicineForm from '@/components/pharmacy/MedicineForm.vue'
import MedicineList from '@/components/pharmacy/MedicinesInventory.vue'
import PharmacyService from '@/services/PharmacyService'
import { authService } from '@/services/auth.service'
import { useRouter } from 'vue-router'

const medicines = ref([])
const farmaciaNombre = ref('')
const farmaceuticoNombre = ref('')
const router = useRouter()

const loadMedicines = async () => {
  try {
    // Obtén el id de la farmacia del usuario autenticado
    const user = authService.getCurrentUser();
    const usuario = user?.usuario || user;
    const farmaciaId = usuario?.farmacia?.idFarmacia || usuario?.idFarmacia;
    if (!farmaciaId) {
      medicines.value = [];
      return;
    }
    medicines.value = await PharmacyService.getAllByFarmacia(farmaciaId);
  } catch (e) {
    console.error('Error al cargar medicamentos', e);
  }
}

const loadUserInfo = () => {
  const user = authService.getCurrentUser();
  if (!user) return;

  // Variante 1: backend retorna { usuario: { ... } }
  const usuario = user.usuario || user;

  // Variante 2: backend retorna farmaceutico directo o con otros nombres
  farmaceuticoNombre.value = `${usuario.nombre || usuario.nombreFarmaceutico || ''} ${usuario.apellido || usuario.apellidoFarmaceutico || ''}`.trim();

  if (usuario.farmacia && (usuario.farmacia.nombre || usuario.farmacia.nombreFarmacia)) {
    farmaciaNombre.value = usuario.farmacia.nombre || usuario.farmacia.nombreFarmacia;
  } else if (usuario.nombreFarmacia) {
    farmaciaNombre.value = usuario.nombreFarmacia;
  }
}

const logout = () => {
  authService.logout()
  router.push('/login')
}

onMounted(() => {
  loadMedicines()
  loadUserInfo()
})
</script>

<style scoped>
.pharmacy-dashboard {
  padding: 2rem;
}
.pharmacy-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}
.pharmacy-info span {
  margin-right: 1.5rem;
  font-size: 1.1rem;
}
.logout-btn {
  background: #e74c3c;
  color: #fff;
  border: none;
  padding: 0.5rem 1.2rem;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}
.logout-btn:hover {
  background: #c0392b;
}
</style>
