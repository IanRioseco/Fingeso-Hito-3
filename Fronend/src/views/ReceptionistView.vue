<template>
  <div class="receptionist-dashboard">
    <header class="receptionist-header">
      <div class="header-logo-title">
        <img src="@/assets/img/logoUH.png" alt="RedSalud Logo" class="logo" />
        <div>
          <h1>Panel Recepcionista</h1>
          <div class="receptionist-info" v-if="nombre && rut">
            <span>{{ nombre }}</span>
            <span>RUT: {{ formatRut(rut) }}</span>
          </div>
        </div>
      </div>
      <button class="logout-btn" @click="logout">Cerrar sesión</button>
    </header>

    <nav class="receptionist-nav">
      <button @click="activeTab = 'appointments'" :class="{ active: activeTab === 'appointments' }">Gestión de Citas</button>
      <button @click="activeTab = 'patients'" :class="{ active: activeTab === 'patients' }">Gestión de Pacientes</button>
      <button @click="activeTab = 'reports'" :class="{ active: activeTab === 'reports' }">Reportes</button>
      <button @click="activeTab = 'settings'" :class="{ active: activeTab === 'settings' }">Configuración</button>
    </nav>

    <div class="receptionist-content">
      <div v-if="activeTab === 'appointments'">
        <input
          v-model="searchAppointment"
          type="text"
          placeholder="Buscar cita..."
          class="search-bar"
        />
        <p>Aquí se gestionan las citas.</p>
      </div>
      <div v-if="activeTab === 'patients'">
        <input
          v-model="searchPatient"
          type="text"
          placeholder="Buscar paciente..."
          class="search-bar"
        />
        <p>Aquí se gestionan los pacientes.</p>
      </div>
      <p v-if="activeTab === 'reports'">Aquí se visualizan los reportes.</p>
      <p v-if="activeTab === 'settings'">Configuración del perfil.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '@/services/auth.service'

const nombre = ref('')
const rut = ref('')
const activeTab = ref('appointments')
const router = useRouter()
const searchAppointment = ref("");
const searchPatient = ref("");

// Formatea el RUT con puntos y guion
function formatRut(rutValue) {
  if (!rutValue) return '';
  let rut = rutValue.replace(/[^\dkK]/g, '').toUpperCase();
  if (rut.length < 2) return rut;
  let cuerpo = rut.slice(0, -1).replace(/\B(?=(\d{3})+(?!\d))/g, '.');
  let dv = rut.slice(-1);
  return `${cuerpo}-${dv}`;
}

const loadUserInfo = () => {
  const user = authService.getCurrentUser()
  if (!user || !user.usuario) {
    console.warn('Usuario no encontrado. Redirigiendo a login...')
    router.push('/login')
    return
  }

  const u = user.usuario
  nombre.value = `${u.nombre || ''} ${u.apellido || ''}`.trim()
  rut.value = u.rut || ''
}

const logout = () => {
  authService.logout("auth/logout")
  router.push('/login')
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.receptionist-dashboard {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  background: var(--color-fondo);
  border-radius: 16px;
  box-shadow: 0 2px 16px var(--color-shadow);
}

.receptionist-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--color-borde);
  background: var(--color-fondo);
  border-radius: 12px 12px 0 0;
}

.header-logo-title {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.logo {
  height: 48px;
  width: auto;
  display: block;
}

.receptionist-header h1 {
  color: var(--color-principal);
  margin: 0 0 0.5rem 0;
  font-size: 1.6rem;
}

.receptionist-info {
  display: flex;
  gap: 1.5rem;
  font-size: 1.1rem;
  color: var(--color-texto);
  margin-top: 0.2rem;
}

.receptionist-info span:first-child {
  font-weight: 600;
  color: var(--color-principal);
}

.receptionist-info span:last-child {
  font-weight: 600;
  color: var(--color-secundario);
}

.logout-btn {
  background: var(--color-secundario);
  color: #fff;
  border: none;
  padding: 0.5rem 1.2rem;
  border-radius: 6px;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 8px var(--color-shadow);
}

.logout-btn:hover {
  background: var(--color-principal);
  box-shadow: 0 4px 16px var(--color-shadow);
}

.receptionist-nav {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.receptionist-nav button {
  padding: 0.75rem 1.5rem;
  background-color: #f5f5f5;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  color: var(--color-texto);
  transition: background 0.2s, color 0.2s;
}

.receptionist-nav button.active,
.receptionist-nav button:hover {
  background-color: var(--color-secundario);
  color: white;
}

.receptionist-nav button:focus {
  outline: none;
  box-shadow: 0 0 0 2px var(--color-secundario);
}

.receptionist-content {
  background-color: var(--color-fondo);
  border-radius: 0 0 12px 12px;
  padding: 2rem;
  box-shadow: 0 0 10px var(--color-shadow);
}

.receptionist-content p {
  color: var(--color-texto);
  font-size: 1rem;
}

.search-bar {
  width: 100%;
  max-width: 350px;
  padding: 8px 12px;
  margin-bottom: 16px;
  border: 1px solid var(--color-borde);
  border-radius: 4px;
  font-size: 1rem;
  background: var(--color-fondo);
  color: var(--color-texto);
  transition: border 0.2s;
}
.search-bar:focus {
  outline: none;
  border-color: var(--color-secundario);
}
</style>