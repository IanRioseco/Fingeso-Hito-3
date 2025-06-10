<template>
  <div class="doctor-calendar">
    <div class="calendar-header">
      <h2>Mi Calendario</h2>
      <div class="calendar-controls">
        <button @click="prevWeek" class="btn-nav">
          <i class="fas fa-chevron-left"></i>
        </button>
        <span class="current-week">{{ weekRange }}</span>
        <button @click="nextWeek" class="btn-nav">
          <i class="fas fa-chevron-right"></i>
        </button>
        <button @click="showAvailabilityModal = true" class="btn-set-availability">
          <i class="fas fa-calendar-plus"></i> Configurar Disponibilidad
        </button>
      </div>
    </div>

    <div class="calendar-grid">
      <div class="time-column">
        <div class="time-header">Hora</div>
        <div v-for="timeSlot in timeSlots" :key="timeSlot" class="time-slot">
          {{ timeSlot }}
        </div>
      </div>
      
      <div 
        v-for="day in weekDays" 
        :key="day.date" 
        class="day-column"
        :class="{ 'today': day.isToday }"
      >
        <div class="day-header">
          <div class="day-name">{{ day.name }}</div>
          <div class="day-date">{{ day.date }}</div>
        </div>
        
        <div 
          v-for="timeSlot in timeSlots" 
          :key="`${day.date}-${timeSlot}`"
          class="time-slot"
          @click="handleSlotClick(day.date, timeSlot)"
          :class="{
            'available': isSlotAvailable(day.date, timeSlot),
            'booked': isSlotBooked(day.date, timeSlot),
            'unavailable': !isSlotAvailable(day.date, timeSlot) && !isSlotBooked(day.date, timeSlot)
          }"
        >
          <div v-if="isSlotBooked(day.date, timeSlot)" class="appointment-info">
            <span class="patient-name">{{ getAppointment(day.date, timeSlot).patientName }}</span>
            <span class="appointment-type">{{ getAppointment(day.date, timeSlot).type }}</span>
          </div>
          <div v-else-if="isSlotAvailable(day.date, timeSlot)" class="slot-status">
            Disponible
          </div>
          <div v-else class="slot-status">
            No disponible
          </div>
        </div>
      </div>
    </div>

    <!-- Modal para configurar disponibilidad -->
    <AvailabilityModal 
      v-if="showAvailabilityModal" 
      @close="showAvailabilityModal = false"
      @save="saveAvailability"
    />

    <!-- Modal para ver detalles de cita -->
    <AppointmentModal 
      v-if="selectedAppointment"
      :appointment="selectedAppointment"
      @close="selectedAppointment = null"
      @save="updateAppointment"
      @cancel="cancelAppointment"
    />
  </div>
</template>

<script>
import AvailabilityModal from './AvailabilityModal.vue';
import AppointmentModal from './AppointmentModal.vue';
import { format, addDays, startOfWeek, isToday, parseISO } from 'date-fns';
import { es } from 'date-fns/locale';

export default {
  name: 'DoctorCalendar',
  components: {
    AvailabilityModal,
    AppointmentModal
  },
  data() {
    return {
      currentDate: new Date(),
      showAvailabilityModal: false,
      selectedAppointment: null,
      timeSlots: this.generateTimeSlots(),
      availability: [], // Esto vendría del backend
      appointments: [], // Esto vendría del backend
      blockDuration: 30 // Duración de cada bloque en minutos
    }
  },
  computed: {
    weekDays() {
      const startDate = startOfWeek(this.currentDate, { weekStartsOn: 1 });
      return Array.from({ length: 5 }, (_, i) => { // Solo días de semana (L-V)
        const date = addDays(startDate, i);
        return {
          date: format(date, 'yyyy-MM-dd'),
          name: format(date, 'EEEE', { locale: es }),
          isToday: isToday(date)
        };
      });
    },
    weekRange() {
      const start = startOfWeek(this.currentDate, { weekStartsOn: 1 });
      const end = addDays(start, 4); // Solo días de semana
      return `${format(start, 'd MMM', { locale: es })} - ${format(end, 'd MMM yyyy', { locale: es })}`;
    }
  },
  methods: {
    generateTimeSlots() {
      const slots = [];
      const startHour = 8; // 8 AM
      const endHour = 20; // 8 PM
      
      for (let hour = startHour; hour < endHour; hour++) {
        for (let minutes = 0; minutes < 60; minutes += this.blockDuration) {
          const time = `${hour.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
          slots.push(time);
        }
      }
      
      return slots;
    },
    prevWeek() {
      this.currentDate = addDays(this.currentDate, -7);
      this.fetchData();
    },
    nextWeek() {
      this.currentDate = addDays(this.currentDate, 7);
      this.fetchData();
    },
    isSlotAvailable(date, time) {
      return this.availability.some(slot => 
        slot.date === date && slot.time === time && slot.available
      );
    },
    isSlotBooked(date, time) {
      return this.appointments.some(appt => 
        appt.date === date && appt.time === time
      );
    },
    getAppointment(date, time) {
      return this.appointments.find(appt => 
        appt.date === date && appt.time === time
      ) || {};
    },
    handleSlotClick(date, time) {
      if (this.isSlotBooked(date, time)) {
        this.selectedAppointment = this.getAppointment(date, time);
      } else if (this.isSlotAvailable(date, time)) {
        // Podría abrir un modal para crear una cita manualmente
      }
    },
    async fetchData() {
      try {
        // Obtener disponibilidad y citas para la semana actual
        const weekStart = format(startOfWeek(this.currentDate, { weekStartsOn: 1 }), 'yyyy-MM-dd');
        const weekEnd = format(addDays(startOfWeek(this.currentDate, { weekStartsOn: 1 }), 4), 'yyyy-MM-dd');
        
        await this.$store.dispatch('doctor/fetchAvailability', { start: weekStart, end: weekEnd });
        await this.$store.dispatch('doctor/fetchAppointments', { start: weekStart, end: weekEnd });
        
        this.availability = this.$store.state.doctor.availability;
        this.appointments = this.$store.state.doctor.appointments;
      } catch (error) {
        console.error('Error al cargar datos del calendario:', error);
      }
    },
    async saveAvailability(newAvailability) {
      try {
        await this.$store.dispatch('doctor/saveAvailability', newAvailability);
        this.fetchData();
      } catch (error) {
        console.error('Error al guardar disponibilidad:', error);
      }
    },
    async updateAppointment(updatedAppointment) {
      try {
        await this.$store.dispatch('doctor/updateAppointment', updatedAppointment);
        this.fetchData();
        this.selectedAppointment = null;
      } catch (error) {
        console.error('Error al actualizar cita:', error);
      }
    },
    async cancelAppointment(appointmentId) {
      try {
        await this.$store.dispatch('doctor/cancelAppointment', appointmentId);
        this.fetchData();
        this.selectedAppointment = null;
      } catch (error) {
        console.error('Error al cancelar cita:', error);
      }
    }
  },
  created() {
    this.fetchData();
  }
}
</script>

<style scoped>
.doctor-calendar {
  padding: 1rem;
  width: 100%;
  max-width: 1200px;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.calendar-header h2 {
  color: #009999;
  margin: 0;
}

.calendar-controls {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.btn-nav {
  background-color: #f5f5f5;
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-nav:hover {
  background-color: #e0e0e0;
}

.current-week {
  font-weight: 600;
  min-width: 200px;
  text-align: center;
}

.btn-set-availability {
  background-color: #99cc00;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.calendar-grid {
  display: flex;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.time-column {
  width: 80px;
  flex-shrink: 0;
  text-align: center;
}

.time-header {
  background-color: #009999;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 72px;
 /* Ajusta según lo que veas en pantalla */
 color: #fff;
}

.day-header {
  background-color: #009999;
  height: 72px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.day-name {
  font-size: 1.1rem;
  text-transform: capitalize;
}

.day-date {
  font-size: 0.9rem;
  opacity: 0.9;
}

.day-column {
  flex: 1;
  min-width: 0;
}

.day-column.today .day-header {
  background-color: #007777;
}

.time-slot {
  height: 60px;
  border-bottom: 1px solid #eee;
  border-right: 1px solid #eee;
  padding: 0.25rem;
  position: relative;
  cursor: pointer;
}

.time-slot:hover {
  background-color: #f9f9f9;
}

.time-slot.available {
  background-color: #e8f5e9;
}

.time-slot.booked {
  background-color: #ffebee;
}

.time-slot.unavailable {
  background-color: #f5f5f5;
}

.time-slot .time-label {
  position: absolute;
  top: 0.25rem;
  left: 0.25rem;
  font-size: 0.75rem;
  color: #757575;
}

.appointment-info {
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: center;
  padding: 0.25rem;
}

.patient-name {
  font-weight: 600;
  margin-bottom: 0.25rem;
}

.appointment-type {
  font-size: 0.8rem;
  color: #616161;
}

.slot-status {
  display: flex;
  height: 100%;
  align-items: center;
  justify-content: center;
  color: #616161;
  font-size: 0.9rem;
}
</style>