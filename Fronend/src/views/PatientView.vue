<template>
  <div class="patient-dashboard">
    <header class="patient-header">
      <div class="header-logo-title">
        <img src="@/assets/img/logoUH.png" alt="RedSalud Logo" class="logo" />
      </div>
      <div class="patient-actions">
        <img :src="patientPhoto" alt="Foto de perfil" class="profile-header-photo" />
        <div class="patient-info">
          <span class="patient-name">{{ patientName }}</span>
          <span class="patient-detail">RUT: {{ formatRut(patientRut) }}</span>
        </div>
        <button class="logout-btn" @click="logout">
          <i class="fas fa-sign-out"></i> Cerrar Sesión
        </button>
      </div>
    </header>

    <nav class="patient-nav">
      <button 
        v-for="tab in tabs" 
        :key="tab.id" 
        @click="activeTab = tab.id"
        :class="{ active: activeTab === tab.id }"
      >
        {{ tab.label }}
      </button>
    </nav>

    <main class="patient-content">
      <AppointmentScheduler v-if="activeTab === 'schedule'" />
      <PatientAppointments v-if="activeTab === 'appointments'" />
      <MedicalHistory v-if="activeTab === 'history'" />
      <PatientExams v-if="activeTab === 'exams'" />
      <PatientPrescriptions v-if="activeTab === 'prescriptions'" />
      <PatientSettings v-if="activeTab === 'settings'" />
    </main>
  </div>
</template>

<script>
//IMPORTACIONES
import AppointmentScheduler from '@/components/patient/AppointmentScheduler.vue';
import PatientAppointments from '@/components/patient/PatientAppointments.vue';
import MedicalHistory from '@/components/patient/MedicalHistory.vue';
import PatientExams from '@/components/patient/PatientExams.vue';
import PatientPrescriptions from '@/components/patient/PatientPrescriptions.vue';
import PatientSettings from '@/components/patient/PatientSettings.vue';
import { authService } from '@/services/auth.service';

//EXPORTACIONES
export default {
  name: 'PatientView',
  components: {
    AppointmentScheduler,
    PatientAppointments,
    MedicalHistory,
    PatientExams,
    PatientPrescriptions,
    PatientSettings
  },
  data() {
    //definicion de las pestañas y datos del paciente
    return {
      activeTab: 'schedule',
      tabs: [
        { id: 'schedule', label: 'Agendar Cita' },
        { id: 'appointments', label: 'Mis Citas' },
        { id: 'history', label: 'Historia Clínica' },
        { id: 'exams', label: 'Mis Exámenes' },
        { id: 'prescriptions', label: 'Mis Recetas' },
        { id: 'settings', label: 'Configuración' },
      ],
      patientName: '',
      patientRut: '',
      patientPhoto: '', 
    }
  },
  //metodo para obtener datos del usuario
  created() {
    const userData = authService.getCurrentUser();//obtener datos del usuario
    if (userData && userData.usuario) {
      this.patientName = `${userData.usuario.nombrePa} ${userData.usuario.apellidoPa}`;
      this.patientRut = userData.usuario.rut;
      this.patientPhoto =
      userData.usuario.fotoPerfil ||
      `https://ui-avatars.com/api/?name=${encodeURIComponent(this.patientName)}&background=0875C1&color=fff`;
    }
  },
  methods: {
    logout() {
      authService.logout("auth/logout");
      this.$router.push('/login');
    },

    formatRut(rut) {
    if (!rut) return '';
    // Elimina puntos y guion si existen
    rut = rut.replace(/\./g, '').replace('-', '');
    // Extrae dígito verificador
    let dv = rut.slice(-1);
    let cuerpo = rut.slice(0, -1);
    // Agrega puntos cada 3 dígitos
    cuerpo = cuerpo.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    return `${cuerpo}-${dv}`;
  }
  }
}
</script>

<style scoped>
.patient-dashboard {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  background: var(--color-fondo);
  border-radius: 16px;
  box-shadow: 0 2px 16px var(--color-shadow);
}

.patient-header {
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

.profile-header-photo {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--color-secundario);
  margin-right: 1rem;
}

.patient-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  font-size: 1.1rem;
  color: var(--color-texto);
}

.patient-name {
  font-weight: 600;
  color: var(--color-principal);
  font-size: 1.2rem;
}

.patient-detail {
  color: #757575;
  font-size: 1rem;
}

.patient-actions {
  display: flex;
  align-items: center;
  gap: 1.2rem;
}

.logout-btn {
  background-color: var(--color-secundario);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: bold;
  transition: background 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 8px var(--color-shadow);
}

.logout-btn:hover {
  background-color: var(--color-principal);
  box-shadow: 0 4px 16px var(--color-shadow);
}

.patient-nav {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 2rem;
  overflow-x: auto;
  padding-bottom: 0.5rem;
}

.patient-nav button {
  padding: 0.75rem 1.5rem;
  background-color: #f5f5f5;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  color: var(--color-texto);
  white-space: nowrap;
  transition: background 0.2s, color 0.2s;
}

.patient-nav button.active,
.patient-nav button:hover {
  background-color: var(--color-secundario);
  color: white;
}

.patient-nav button:focus {
  outline: none;
  box-shadow: 0 0 0 2px var(--color-secundario);
}

.patient-nav button:disabled,
.patient-nav button:disabled:hover {
  background-color: #ccc;
  color: #aaa;
  cursor: not-allowed;
  box-shadow: none;
}

.patient-content {
  background-color: var(--color-fondo);
  border-radius: 0 0 12px 12px;
  padding: 2rem;
  box-shadow: 0 0 10px var(--color-shadow);
  min-height: 500px;
}
</style>