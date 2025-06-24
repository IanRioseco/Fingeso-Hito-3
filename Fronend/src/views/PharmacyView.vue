<template>
  <div class="pharmacy-dashboard">
    <!-- Header con info de farmacia y farmacéutico -->
    <header class="pharmacy-header">
      <div class="header-logo-title">
        <img src="@/assets/img/logoUH.png" alt="RedSalud Logo" class="logo" />
        <h1>
          <span class="farmacia-name">{{ farmaciaNombre || 'Farmacia' }}</span>
          <span v-if="farmaceuticoNombre" class="farmaceutico-name">- {{ farmaceuticoNombre }}</span>
        </h1>
      </div>
      <button class="logout-btn" @click="logout">Cerrar sesión</button>
    </header>

    <div class="layout">
      <!-- Sidebar -->
      <aside class="sidebar">
        <h3>Menú</h3>
        <ul>
          <li>
            <button :class="{active: vistaActual === 'formulario'}" @click="vistaActual = 'formulario'">
              <i class="fas fa-plus-circle"></i> Ingresar Medicamento
            </button>
          </li>
          <li>
            <button :class="{active: vistaActual === 'inventario'}" @click="vistaActual = 'inventario'">
              <i class="fas fa-pills"></i> Inventario
            </button>
          </li>
          <li>
            <button :class="{active: vistaActual === 'Recetas medicas'}" @click="vistaActual = 'Recetas medicas'">
              <i class="fas fa-notes-medical"></i> Recetas Médicas
            </button>
          </li>
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
        <MedicalAlerts v-else-if="vistaActual === 'Recetas medicas'" />
      </main>
    </div>
  </div>
</template>

<script setup>
//IMPORTACIONES
import { ref, computed, onMounted } from 'vue'
import MedicineForm from '@/components/pharmacy/MedicineForm.vue'
import Inventory from '@/components/pharmacy/MedicinesInventory.vue'
import MedicalAlerts from '@/components/pharmacy/MedicalAlerts.vue'
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
    : vistaActual.value === 'formulario'
      ? 'Ingreso de Medicamentos'
      : vistaActual.value === 'Recetas medicas'
        ? 'Alertas de Recetas Médicas'
        : ''
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
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css');

.header-logo-title {
  display: flex;
  align-items: center;
  gap: 1.2rem;
}
.logo {
  height: 48px;
  width: auto;
  display: block;
}

.pharmacy-dashboard {
  min-height: 100vh;
  background: var(--color-fondo);
  font-family: 'Segoe UI', Arial, Helvetica, sans-serif;
}

.pharmacy-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2rem 3rem 1.5rem 3rem;
  background: var(--color-fondo);
  box-shadow: 0 2px 12px var(--color-shadow);
  border-bottom-left-radius: 24px;
  border-bottom-right-radius: 24px;
}

.pharmacy-header h1 {
  margin: 0;
  font-size: 2rem;
  font-weight: bold;
  color: var(--color-principal);
  letter-spacing: 1px;
}

.farmacia-name {
  color: var(--color-principal);
  font-weight: bold;
}

.farmaceutico-name {
  color: var(--color-secundario);
  font-weight: bold;
  margin-left: 0.7rem;
}

.logout-btn {
  background-color: var(--color-secundario);
  color: white;
  border: none;
  padding: 0.6rem 1.4rem;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
  transition: background 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 8px var(--color-shadow);
}
.logout-btn:hover {
  background-color: var(--color-principal);
  box-shadow: 0 4px 16px var(--color-shadow);
}

.layout {
  display: flex;
  max-width: 1300px;
  margin: 2.5rem auto 0 auto;
  gap: 2.5rem;
}

.sidebar {
  width: 260px;
  background: #f8fafd;
  padding: 2rem 1.5rem 1.5rem 1.5rem;
  border-radius: 18px;
  box-shadow: 2px 0 16px var(--color-shadow);
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  min-height: 400px;
}

.sidebar h3 {
  font-size: 1.3rem;
  margin-bottom: 1.5rem;
  font-weight: bold;
  color: var(--color-principal);
}

.sidebar ul {
  list-style: none;
  padding: 0;
  width: 100%;
  margin-bottom: 2rem;
}

.sidebar li {
  margin-bottom: 1.2rem;
}

.sidebar button {
  width: 100%;
  padding: 0.8rem 1rem;
  background: none;
  border: none;
  text-align: left;
  cursor: pointer;
  border-radius: 7px;
  font-size: 1.08rem;
  font-family: inherit;
  color: var(--color-principal);
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 0.7rem;
  transition: background 0.18s, color 0.18s;
}
.sidebar button.active,
.sidebar button:hover {
  background: #e3eafc;
  color: var(--color-secundario);
}

.main-content {
  flex: 1;
  background: var(--color-fondo);
  border-radius: 18px;
  padding: 2.5rem 3rem;
  box-shadow: 0 0 16px var(--color-shadow);
  min-height: 500px;
  display: flex;
  flex-direction: column;
}

.main-content h2 {
  font-size: 2rem;
  margin-bottom: 2rem;
  color: var(--color-principal);
  font-family: inherit;
}

.search-bar {
  width: 100%;
  max-width: 350px;
  padding: 10px 14px;
  margin-bottom: 18px;
  border: 1.5px solid var(--color-secundario);
  border-radius: 6px;
  font-size: 1.08rem;
  transition: border 0.2s;
}
.search-bar:focus {
  outline: none;
  border-color: var(--color-principal);
}

@media (max-width: 1100px) {
  .layout {
    flex-direction: column;
    gap: 1.5rem;
    padding: 0 1rem;
  }
  .sidebar {
    width: 100%;
    min-height: unset;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    border-radius: 12px;
    margin-bottom: 1.5rem;
  }
  .sidebar ul {
    display: flex;
    gap: 1.2rem;
    margin-bottom: 0;
  }
  .sidebar li {
    margin-bottom: 0;
  }
}

@media (max-width: 700px) {
  .pharmacy-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
    padding: 1.2rem 1rem 1rem 1rem;
  }
  .main-content {
    padding: 1.2rem 0.7rem;
  }
  .sidebar {
    padding: 1rem 0.7rem;
  }
}
</style>
