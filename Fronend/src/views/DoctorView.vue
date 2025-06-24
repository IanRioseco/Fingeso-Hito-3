<template>
  <div class="doctor-dashboard">
    <header class="doctor-header">
      <div class="header-logo-title">
        <img src="@/assets/img/logoUH.png" alt="RedSalud Logo" class="logo" />
        <h1>Panel Médico</h1>
      </div>
      <div class="doctor-info">
        <img
          class="doctor-photo"
          :src="doctorPhotoUrl"
          alt="Foto del doctor"
        />
        <div class="doctor-meta">
          <span>Dr. {{ doctorName }}</span>
          <span>{{ specialty.nombre }}</span>
        </div>
        <button class="logout-btn" @click="logout">Cerrar sesión</button>
      </div>
    </header>

    <nav class="doctor-nav">
      <button 
        v-for="tab in tabs" 
        :key="tab.id" 
        @click="activeTab = tab.id"
        :class="{ active: activeTab === tab.id }"
      >
        {{ tab.label }}
      </button>
    </nav>

    <main class="doctor-content">
      <DoctorCalendar v-if="activeTab === 'calendar'" />
      <PatientProfiles v-if="activeTab === 'patients'" />
      <MedicalReports v-if="activeTab === 'reports'" />
      <DoctorSettings v-if="activeTab === 'settings'" />
    </main>
  </div>
</template>

<script>
//IMPORTACIONES
import DoctorCalendar from '@/components/doctor/DoctorCalendar.vue';
import PatientProfiles from '@/components/doctor/PatientProfiles.vue';
import MedicalReports from '@/components/doctor/MedicalReports.vue';
import DoctorSettings from '@/components/doctor/DoctorSettings.vue';
import { authService } from '@/services/auth.service';

//EXPORTACIONES
export default {
  name: 'DoctorView',
  components: {
    DoctorCalendar,
    PatientProfiles,
    MedicalReports,
    DoctorSettings
  },
  data() {
    return {
      activeTab: 'calendar',
      tabs: [
        { id: 'calendar', label: 'Calendario' },
        { id: 'patients', label: 'Citas' },
        { id: 'reports', label: 'Reportes' },
        { id: 'settings', label: 'Configuración' }
      ],
      doctorName: '',
      specialty: { nombre: '' },
      doctorPhotoUrl: ''
    }
  },
  created() {
    const userData = authService.getCurrentUser();
    if (userData && userData.usuario) {
      this.doctorName = `${userData.usuario.nombre} ${userData.usuario.apellido}`;
      this.specialty = userData.usuario.especialidad || {nombre: 'Especialidad no definida' };
      this.doctorPhotoUrl =
        userData.usuario.fotoPerfil ||
        `https://ui-avatars.com/api/?name=${encodeURIComponent(this.doctorName)}&background=0875C1&color=fff`;
    }
  },
  methods: {
    logout() {
      authService.logout("auth/logout");
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
.doctor-dashboard {
  padding: 2rem;
  max-width: 100%;
  margin: 0 auto;
  width: 100%;
  background: var(--color-fondo);
  border-radius: 16px;
  box-shadow: 0 2px 16px var(--color-shadow);
}

.doctor-header {
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
  gap: 1.2rem;
}

.logo {
  height: 48px;
  width: auto;
  display: block;
}

.doctor-header h1 {
  color: var(--color-principal);
  margin: 0;
}

.doctor-info {
  display: flex;
  align-items: center;
  gap: 1.2rem;
}

.doctor-photo {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--color-secundario);
  background: #f5f5f5;
}

.doctor-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  font-size: 1.1rem;
  color: var(--color-texto);
}

.doctor-meta span:first-child {
  font-weight: 600;
  color: var(--color-principal);
}

.logout-btn {
  background: var(--color-secundario);
  color: #fff;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  margin-left: 1rem;
  transition: background 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 8px var(--color-shadow);
}

.logout-btn:hover {
  background: var(--color-principal);
  box-shadow: 0 4px 16px var(--color-shadow);
}

.doctor-nav {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 2rem;
}

.doctor-nav button {
  padding: 0.75rem 1.5rem;
  background-color: #f5f5f5;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  color: var(--color-texto);
  transition: background 0.2s, color 0.2s;
}

.doctor-nav button.active,
.doctor-nav button:hover {
  background-color: var(--color-secundario);
  color: white;
}

.doctor-nav button:focus {
  outline: none;
  box-shadow: 0 0 0 2px var(--color-secundario);
}

.doctor-nav button:active {
  background-color: var(--color-principal);
  color: white;
}

.doctor-nav button:disabled,
.doctor-nav button:disabled:hover {
  background-color: #ddd;
  color: #aaa;
  cursor: not-allowed;
  box-shadow: none;
}

.doctor-content {
  background-color: var(--color-fondo);
  border-radius: 0 0 12px 12px;
  padding: 2rem;
  box-shadow: 0 0 10px var(--color-shadow);
  min-height: 500px;
}
</style>