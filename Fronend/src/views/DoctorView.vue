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
}

.doctor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #ddd;
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
  color: #0875C1;
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
  border: 2px solid #C51A6F;
  background: #f5f5f5;
}

.doctor-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  font-size: 1.1rem;
  color: #747473;
}

.doctor-meta span:first-child {
  font-weight: 600;
  color: #0875C1;
}

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

.logout-btn:hover {
  background: #0875C1;
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
  color: #747473;
}

.doctor-nav button.active {
  background-color: #C51A6F;
  color: white;
}

.doctor-content {
  background-color: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
  min-height: 500px;
}

.doctor-nav button:hover {
  background-color: #C51A6F;
  color: white;
}

.doctor-nav button:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(13, 110, 253, 0.5);
}

.doctor-nav button:active {
  background-color: #0875C1;
  color: white;
}

.doctor-nav button:disabled {
  background-color: #ddd;
  color: #aaa;
  cursor: not-allowed;
}

.doctor-nav button:disabled:hover {
  background-color: #ddd;
  color: #aaa;
}

.doctor-nav button:disabled:focus {
  box-shadow: none;
}
.doctor-nav button:disabled:active {
  background-color: #ddd;
  color: #aaa;
}
</style>