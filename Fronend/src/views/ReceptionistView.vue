<template>
  <div class="receptionist-dashboard">
    <header class="receptionist-header">
      <h1>Panel Recepcionista</h1>
      <div class="receptionist-info" v-if="nombre && rut">
        <span>{{ nombre }}</span>
        <span>{{ rut }}</span>
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
.logout-btn {
  background: #C51A6F;
  color: #fff;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  margin-left: 1rem;
}

.receptionist-dashboard {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.receptionist-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #ddd;
}  

.receptionist-header h1 {

  color: #009999;
  margin: 0 0 0.5rem 0;
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
  color: #747473;
}

.receptionist-nav button.active {
  background-color: #C51A6F;
  color: white;
}

.receptionist-nav button:hover {
  background-color: #C51A6F;
  color: white;
}

.receptionist-nav button:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(197, 26, 111, 0.5);
}

.receptionist-content {
  background-color: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
}

.receptionist-content h2 {
  color: #009999;
  margin-bottom: 1.5rem;
}

.receptionist-content p {
  color: #747473;
  font-size: 1rem;
}

.receptionist-info {
  display: flex;
  flex-direction: field;
  align-items: flex-end;
  font-size: 1.1rem;
  color: #747473;
}

.receptionist-info span:first-child {
  font-weight: 600;
  color: #009999;
}

.receptionist-info span {
  margin-bottom: 0.2rem;
}

.receptionist-info span:last-child {
  margin-left: 1rem;
  font-weight: 600;
  color: #009999;
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