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
        <div v-for="timeSlot in timeSlots" :key="timeSlot" class="time-slot time-slot-hour">
          <span v-if="timeSlot.endsWith(':00')" class="main-hour">{{ timeSlot }}</span>
          <span v-else class="half-hour">{{ timeSlot }}</span>
        </div>
      </div>

      <div
        v-for="day in weekDays"
        :key="day.date"
        class="day-column"
        :class="{ today: day.isToday }"
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
            available: isSlotAvailable(day.date, timeSlot),
            booked: isSlotBooked(day.date, timeSlot),
            unavailable: !isSlotAvailable(day.date, timeSlot) && !isSlotBooked(day.date, timeSlot)
          }"
        >
          <div v-if="isSlotBooked(day.date, timeSlot)" class="appointment-info">
            <span class="patient-name">{{ getAppointment(day.date, timeSlot).patientName }}</span>
            <span class="appointment-type">{{ getAppointment(day.date, timeSlot).type }}</span>
          </div>
          <div v-else-if="isSlotAvailable(day.date, timeSlot)" class="slot-status">
            Disponible
            <button
              @click.stop="eliminarDisponibilidad(day.date, timeSlot)"
              style="margin-left:8px; color:#C51A6F; background:none; border:none; cursor:pointer;"
            >🗑️</button>
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
// Importaciones necesarias
import AvailabilityModal from './AvailabilityModal.vue';
import AppointmentModal from './AppointmentModal.vue';
import { format, addDays, startOfWeek, isToday, parseISO } from 'date-fns';
import { es } from 'date-fns/locale';
//IMPORTACIONES
//definicion de variables y funciones
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
      availability: [],
      appointments: [],
      blockDuration: 30
    }
  },
  computed: {
      weekDays() {
      const startDate = startOfWeek(this.currentDate, { weekStartsOn: 1 });
      return Array.from({ length: 7 }, (_, i) => {
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
      const end = addDays(start, 6);
      return `${format(start, 'd MMM', { locale: es })} - ${format(end, 'd MMM yyyy', { locale: es })}`;
    },
    timeSlots() {
      const slots = [];
      const startHour = 8;
      const endHour = 20;
      for (let hour = startHour; hour < endHour; hour++) {
        for (let minutes = 0; minutes < 60; minutes += this.blockDuration) {
          const time = `${hour.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
          slots.push(time);
        }
      }
      return slots;
    }
  },
  methods: {
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
        const year = this.currentDate.getFullYear();
        const month = this.currentDate.getMonth() + 1;
        await this.$store.dispatch('doctor/fetchAvailability', { year, month });
        this.availability = this.$store.state.doctor.availability;
        this.appointments = this.$store.state.doctor.appointments;
      } catch (error) {
        console.error('Error al cargar datos del calendario:', error);
      }
    },
    async saveAvailability(newAvailability) {
      try {
        const user = JSON.parse(localStorage.getItem('user'));
        const idMedico = user?.idmedico || user?.usuario?.idmedico;
        if (!idMedico) {
          alert('No se encontró el ID del médico autenticado');
          return;
        }
        const bloques = [];
        newAvailability.forEach(rango => {
          const { date, startTime, endTime } = rango;
          let [startHour, startMin] = startTime.split(':').map(Number);
          let [endHour, endMin] = endTime.split(':').map(Number);
          let current = startHour * 60 + startMin;
          const end = endHour * 60 + endMin;
          while (current < end) {
            const blockStartHour = Math.floor(current / 60).toString().padStart(2, '0');
            const blockStartMin = (current % 60).toString().padStart(2, '0');
            const blockEnd = current + this.blockDuration;
            const blockEndHour = Math.floor(blockEnd / 60).toString().padStart(2, '0');
            const blockEndMin = (blockEnd % 60).toString().padStart(2, '0');
            bloques.push({
              fecha: date,
              horainicio: `${blockStartHour}:${blockStartMin}`,
              horafin: `${blockEndHour}:${blockEndMin}`,
              idMedico: idMedico
            });
            current += this.blockDuration;
          }
        });
        await this.$store.dispatch('doctor/saveAvailability', bloques);
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
    },
    async eliminarDisponibilidad(date, time) {
      const slot = this.availability.find(a => a.date === date && a.time === time && a.available);
      if (!slot || !slot.id) {
        alert('No se encontró el ID del horario para eliminar.');
        return;
      }
      await this.$store.dispatch('doctor/eliminarDisponibilidad', slot.id);
      this.fetchData();
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
  max-width: 1600px;
  margin: 0 auto;           /* Centra horizontalmente */
  display: flex;
  flex-direction: column;
  align-items: center;      /* Centra el contenido */
}
.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.day-column.today .day-header {
  background-color: #C51A6F;
  color: #fff;
}

.calendar-header h2 {
  color: #0875C1;
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
  background-color: #0875C1;
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
  overflow: auto;
  margin: 0 auto;
  background: #fff;
  width: 100%;           /* Ocupa todo el ancho disponible */
  min-width: 1200px;     /* Asegura que el grid no se vea apretado */
}

.time-column {
  width: 100px;          /* Más ancho para la columna de horas */
  flex-shrink: 0;
  text-align: center;
}

.time-header {
  background-color: #0875C1;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 72px;
 /* Ajusta según lo que veas en pantalla */
 color: #fff;
}

.day-header {
  background-color: #0875C1;
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
  min-width: 160px;      /* Más ancho para cada día */
}

.day-column.today .day-header {
  background-color: #0875C1;
}

.time-slot {
  height: 60px;
  border-bottom: 1px solid #eee;
  border-right: 1px solid #eee;
  padding: 0.25rem;
  position: relative;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8em;
  color: #999;
}

.time-label span {
  font-size: 0.8em;
  color: #999;
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