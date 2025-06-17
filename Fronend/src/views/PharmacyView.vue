<template>
  <section class="info-esquina">
  <h1>
    <span v-if="farmaciaNombre" class="farmacia-name">{{ farmaciaNombre }}</span>
    <span v-if="farmaceuticoNombre" class="farmaceutico-name"> - {{ farmaceuticoNombre }}</span>
  </h1>
</section>

  <div class="layout">
  
    <!-- Sidebar -->
    <aside class="sidebar">
      <h3>Menú</h3>
      <ul>
        <li><button @click="vistaActual = 'formulario'">Ingresar Medicamento</button></li>
        <li><button @click="vistaActual = 'inventario'">Inventario</button></li>
        <li><button class="logout-btn" @click="logout">Cerrar sesión</button></li>
      </ul>
    </aside>

    <!-- Contenido dinámico -->
    <main class="main-content">
      <h2>{{ titulo }}</h2>
      <div v-if="vistaActual === 'inventario'">
        <input
          v-model="searchMedicine"
          type="text"
          placeholder="Buscar medicamento por nombre..."
          class="search-bar"
        />
        <Inventory :medicines="medicines" :search="searchMedicine" @reload="loadMedicines" />
      </div>
      <MedicineForm v-else-if="vistaActual === 'formulario'" @medAdded="loadMedicines" />
    </main>
  </div>
</template>

<script setup>
//IMPORTACIONES
import { ref, computed, onMounted } from 'vue'
import MedicineForm from '@/components/pharmacy/MedicineForm.vue'
import Inventory from '@/components/pharmacy/MedicinesInventory.vue'
import pharmacyService from '@/services/PharmacyService'
import { authService } from '@/services/auth.service'
import { useRouter } from 'vue-router'

//VARIABLES REACTIVAS
const vistaActual = ref('inventario')
const farmaciaNombre = ref('')
const farmaceuticoNombre = ref('')
const medicines = ref([])
const searchMedicine = ref("");
const router = useRouter()

//titulo dinamico segun la vista actual
const titulo = computed(() =>
  vistaActual.value === 'inventario'
    ? 'Inventario de Medicamentos'
    : 'Ingreso de Medicamentos'
)

//FUNCIONES PARA CARGAR DATOS DEL USUARIO
const loadUserInfo = () => {
  const user = authService.getCurrentUser();//llamada al servicio de autenticación para obtener el usuario actual
  if (!user) {
    console.warn('No user found in localStorage');
    return;
  }//si no hay usuario no carga nada
  //obtener nombre del farmaceutico y farmacia
  const usuario = user.usuario || user;
  //construccion del nombre de farmacia y farmaceutico
  farmaceuticoNombre.value = `${usuario.nombre || usuario.nombreFarmaceutico || ''} ${usuario.apellido || usuario.apellidoFarmaceutico || ''}`.trim();
  // Si no hay nombre o apellido, se deja vacío
  if (usuario.farmacia && (usuario.farmacia.nombre || usuario.farmacia.nombreFarmacia)) {
    farmaciaNombre.value = usuario.farmacia.nombre || usuario.farmacia.nombreFarmacia;
  } else if (usuario.nombreFarmacia) {//si no hay farmacia, se toma el nombre de la farmacia
    farmaciaNombre.value = usuario.nombreFarmacia;
  } else {//si no hay farmacia ni nombre, se deja vacío
    farmaciaNombre.value = '';
  }
}

//FUNCION PARA CARGAR MEDICAMENTOS
const loadMedicines = async () => {
  //try catch para capturar errores
  try {
    const user = authService.getCurrentUser();//llamada al servicio de autenticación para obtener el usuario actual
    const usuario = user?.usuario || user;//si no hay usuario se devuelve el objeto user
    const farmaciaId = usuario?.farmacia?.idFarmacia || usuario?.idFarmacia;//obtener el id de la farmacia
    //si no hay id de farmacia se devuelve un array vacío
    if (!farmaciaId) {
      medicines.value = [];
      return;
    }
    //llamada al servicio de farmacia para obtener todos los medicamentos de la farmacia
    medicines.value = await pharmacyService.getAllByFarmacia(farmaciaId);
  } catch (e) {
    console.error('Error al cargar medicamentos', e);
  }
}
//funcion para cerrar sesión
const logout = () => {
  authService.logout("auth/logout");
  router.push('/login');
}
// Cargar datos al montar el componente
onMounted(() => {
  loadUserInfo()
  loadMedicines()
})
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  font-family: Arial, Helvetica, sans-serif;
  background: #f4f6fa;
}
.sidebar {
  width: 260px;
  background-color: #f8f8f8;
  padding: 2rem 1.5rem 1.5rem 1.5rem;
  box-shadow: 2px 0 8px rgba(0,0,0,0.04);
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.sidebar h3 {
  font-size: 1.3rem;
  margin-bottom: 1.5rem;
  font-weight: bold;
}
.sidebar ul {
  list-style: none;
  padding: 0;
  width: 100%;
  margin-bottom: 2rem;
}
.sidebar li {
  margin-bottom: 1rem;
}
.sidebar button {
  width: 100%;
  padding: 0.7rem 1rem;
  background: none;
  border: none;
  text-align: left;
  cursor: pointer;
  border-radius: 5px;
  font-size: 1rem;
  font-family: inherit;
}
.sidebar button:hover {
  background: #eaeaea;
}
.sidebar .logout {
  font-weight: bold;
}
.farmacia-info {
  margin-top: 2rem;
  font-size: 1.05rem;
  width: 100%;
  line-height: 1.6;
  font-family: inherit;
}
.farmacia-info span {
  display: block;
  margin-bottom: 0.3rem;
}
.main-content {
  flex: 1;
  padding: 2.5rem 3rem;
  font-family: inherit;
}
.main-content h2 {
  font-size: 2rem;
  margin-bottom: 2rem;
  font-family: inherit;
}

.info-esquina {
  position: absolute;
  top: 1.5rem;
  right: 2rem;
  text-align: right;
  z-index: 10;
}
.search-bar {
  width: 100%;
  max-width: 350px;
  padding: 8px 12px;
  margin-bottom: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
}
</style>
