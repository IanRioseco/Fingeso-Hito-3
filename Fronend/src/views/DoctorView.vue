<template>
  <div class="doctor-dashboard">
    <header class="doctor-header">
      <h1>Panel Médico</h1>
      <div class="doctor-info">
        <span>Dr. {{ doctorName }}</span>
        <span>{{ specialty }}</span>
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
import DoctorCalendar from '@/components/doctor/DoctorCalendar.vue';
import PatientProfiles from '@/components/doctor/PatientProfiles.vue';
import MedicalReports from '@/components/doctor/MedicalReports.vue';
import DoctorSettings from '@/components/doctor/DoctorSettings.vue';

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
        { id: 'patients', label: 'Pacientes' },
        { id: 'reports', label: 'Reportes' },
        { id: 'settings', label: 'Configuración' }
      ],
      doctorName: 'Juan Pérez', // Esto vendría del backend
      specialty: 'Cardiología' // Esto vendría del backend
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

.doctor-header h1 {
  color: #0875C1;
  margin: 0;
}

.doctor-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  font-size: 1.1rem;
  color: #747473;
}

.doctor-info span:first-child {
  font-weight: 600;
  color: #0875C1;
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
</style>