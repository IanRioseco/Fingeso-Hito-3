<template>
  <div class="patient-dashboard">
    <header class="patient-header">
      <div class="patient-info">
        <h1>Bienvenido, {{ patientName }}</h1>
        <div class="patient-meta">
          <span>RUT: {{ patientRut }}</span>
        </div>
      </div>
      <div class="patient-actions">
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
    }
  },
  //metodo para obtener datos del usuario
  created() {
    const userData = authService.getCurrentUser();//obtener datos del usuario
    if (userData && userData.usuario) {
      this.patientName = `${userData.usuario.nombrePa} ${userData.usuario.apellidoPa}`;
      this.patientRut = userData.usuario.rut;
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
.patient-dashboard {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.patient-content {
  background-color: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.25);
  min-height: 500px;
}

.patient-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #ddd;
}

.patient-info h1 {
  color: black;
  margin: 0 0 0.5rem 0;
}

.patient-meta {
  display: flex;
  gap: 1rem;
  color: #747473;
  font-size: 0.9rem;
}

.patient-actions {
  display: flex;
  gap: 1rem;
}

.logout-btn {
  background-color: #0875C1;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.logout-btn:hover {
  background-color: #C51A6F;
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
  color: #747473;
  white-space: nowrap;
}

.patient-nav button.active {
  background-color: #C51A6F;
  color: white;
}

</style>