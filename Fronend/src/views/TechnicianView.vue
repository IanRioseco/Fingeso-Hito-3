<template>
  <div class="technician-dashboard">
    <header class="technician-header">
      <div class="header-logo-title">
        <img src="@/assets/img/logoUH.png" alt="RedSalud Logo" class="logo" />
        <div>
          <h1>Panel Tecnólogo Médico</h1>
          <div class="technician-info" v-if="nombre && rut">
            <span>{{ nombre }}</span>
            <span>RUT: {{ formatRut(rut) }}</span>
          </div>
        </div>
      </div>
      <button class="logout-btn" @click="logout">Cerrar sesión</button>
    </header>

    <nav class="technician-nav">
      <button @click="activeTab = 'exams'" :class="{ active: activeTab === 'exams' }">Gestión de Exámenes</button>
      <button @click="activeTab = 'patients'" :class="{ active: activeTab === 'patients' }">Pacientes</button>
      <button @click="activeTab = 'reports'" :class="{ active: activeTab === 'reports' }">Reportes</button>
      <button @click="activeTab = 'settings'" :class="{ active: activeTab === 'settings' }">Configuración</button>
    </nav>

    <div class="technician-content">
      <div v-if="activeTab === 'exams'">
        <p>Aquí puedes gestionar los exámenes médicos.</p>
      </div>
      <div v-if="activeTab === 'patients'">
        <p>Aquí puedes ver y buscar pacientes.</p>
      </div>
      <div v-if="activeTab === 'reports'">
        <p>Aquí puedes generar y visualizar reportes.</p>
      </div>
      <div v-if="activeTab === 'settings'">
        <p>Configuración de tu perfil.</p>
      </div>
    </div>
  </div>
</template>

<script>
import { authService } from '@/services/auth.service';
export default {
  name: 'TechnicianView',
  data() {
    return {
      nombre: '',
      rut: '',
      activeTab: 'exams'
    }
  },
  methods: {
    logout() {
      authService.logout();
      this.$router.push('/');
    },
    formatRut(rutValue) {
      if (!rutValue) return '';
      let rut = rutValue.replace(/[^\dkK]/g, '').toUpperCase();
      if (rut.length < 2) return rut;
      let cuerpo = rut.slice(0, -1).replace(/\B(?=(\d{3})+(?!\d))/g, '.');
      let dv = rut.slice(-1);
      return `${cuerpo}-${dv}`;
    },
    loadUserInfo() {
      const user = authService.getCurrentUser && authService.getCurrentUser();
      if (user && user.usuario) {
        const u = user.usuario;
        this.nombre = `${u.nombre || ''} ${u.apellido || ''}`.trim();
        this.rut = u.rut || '';
      }
    }
  },
  mounted() {
    this.loadUserInfo();
  }
}
</script>

<style scoped>
.technician-dashboard {
  padding: 2rem;
  max-width: 1100px;
  margin: 0 auto;
  background: var(--color-fondo);
  border-radius: 16px;
  box-shadow: 0 2px 16px var(--color-shadow);
}

.technician-header {
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

.technician-header h1 {
  color: var(--color-principal);
  margin: 0 0 0.5rem 0;
  font-size: 1.6rem;
}

.technician-info {
  display: flex;
  gap: 1.5rem;
  font-size: 1.1rem;
  color: var(--color-texto);
  margin-top: 0.2rem;
}

.technician-info span:first-child {
  font-weight: 600;
  color: var(--color-principal);
}

.technician-info span:last-child {
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

.technician-nav {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.technician-nav button {
  padding: 0.75rem 1.5rem;
  background-color: #f5f5f5;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  color: var(--color-texto);
  transition: background 0.2s, color 0.2s;
}

.technician-nav button.active,
.technician-nav button:hover {
  background-color: var(--color-secundario);
  color: white;
}

.technician-nav button:focus {
  outline: none;
  box-shadow: 0 0 0 2px var(--color-secundario);
}

.technician-content {
  background-color: var(--color-fondo);
  border-radius: 0 0 12px 12px;
  padding: 2rem;
  box-shadow: 0 0 10px var(--color-shadow);
}

.technician-content p {
  color: var(--color-texto);
  font-size: 1rem;
}
</style>