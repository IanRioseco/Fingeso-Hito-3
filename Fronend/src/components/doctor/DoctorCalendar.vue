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
            <button @click.stop="eliminarDisponibilidad(day.date, timeSlot)" style="margin-left:8px; color:#C51A6F; background:none; border:none; cursor:pointer;">🗑️</button>
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
  //DATOS REACTIVOS PARA EL COMPONENTE
  data() {
    return {
      currentDate: new Date(),
      showAvailabilityModal: false,
      selectedAppointment: null,
      timeSlots: this.generateTimeSlots(),// Generar bloques de tiempo de 30 minutos
      availability: [],
      appointments: [], 
      blockDuration: 30 // Duración de cada bloque en minutos
    }
  },
  // Computed properties para calcular días de la semana y rango de fechas
  computed: {
    // Función para obtener los días de la semana
    weekDays() {
      // Obtener el inicio de la semana (lunes)
      const startDate = startOfWeek(this.currentDate, { weekStartsOn: 1 });
      // Crear un array de 5 días de la semana
      return Array.from({ length: 5 }, (_, i) => { // Solo días de semana (L-V)
        // Sumar i días al inicio de la semana
        const date = addDays(startDate, i);
        // Formatear la fecha y el nombre del día
        return {
          date: format(date, 'yyyy-MM-dd'),
          name: format(date, 'EEEE', { locale: es }),
          isToday: isToday(date)
        };
      });
    },
    // Función para obtener el rango de fechas de la semana actual
    weekRange() {
      // Obtener el inicio de la semana (lunes) y sumar 4 días para obtener el rango
      const start = startOfWeek(this.currentDate, { weekStartsOn: 1 });
      // Formatear el rango de fechas
      const end = addDays(start, 4); // Solo días de semana
      // Formatear el rango de fechas
      return `${format(start, 'd MMM', { locale: es })} - ${format(end, 'd MMM yyyy', { locale: es })}`;
    }
  },
  // Métodos para manejar la lógica del calendario
  methods: {
    // Generar bloques de tiempo de 30 minutos
    generateTimeSlots() {
      const slots = [];
      const startHour = 8; // 8 AM
      const endHour = 20; // 8 PM
      // Iterar entre los horarios de inicio y fin
      for (let hour = startHour; hour < endHour; hour++) {
        // Iterar entre los minutos de cada hora
        for (let minutes = 0; minutes < 60; minutes += this.blockDuration) {
          // Formatear la hora y los minutos
          const time = `${hour.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
          // Agregar el bloque de tiempo al array
          slots.push(time);
        }
      }
      // Devolver el array de bloques de tiempo
      return slots;
    },
    // Navegar a la semana anterior o siguiente
    prevWeek() {
      this.currentDate = addDays(this.currentDate, -7);
      this.fetchData();
    },
    // Navegar a la semana siguiente
    nextWeek() {
      this.currentDate = addDays(this.currentDate, 7);
      this.fetchData();
    },
    // Verificar si un bloque de tiempo está disponible o reservado
    isSlotAvailable(date, time) {
      // Verificar si el bloque de tiempo está en la disponibilidad
      return this.availability.some(slot => 
        slot.date === date && slot.time === time && slot.available
      );
    },
    // Verificar si un bloque de tiempo está reservado
    isSlotBooked(date, time) {
      return this.appointments.some(appt => 
        appt.date === date && appt.time === time
      );
    },
    // Obtener la cita correspondiente a un bloque de tiempo
    getAppointment(date, time) {
      return this.appointments.find(appt => 
        appt.date === date && appt.time === time
      ) || {};
    },
    // Manejar el clic en un bloque de tiempo
    handleSlotClick(date, time) {
      if (this.isSlotBooked(date, time)) {
        this.selectedAppointment = this.getAppointment(date, time);
      } else if (this.isSlotAvailable(date, time)) {
        // Podría abrir un modal para crear una cita manualmente
      }
    },
    // Funciones para manejar la disponibilidad y citas
    async fetchData() {
      try {
        // Obtener año y mes visibles
        const year = this.currentDate.getFullYear();
        const month = this.currentDate.getMonth(); // 0-indexed
        // Obtener disponibilidad y citas para el mes visible
        await this.$store.dispatch('doctor/fetchAvailability', { year, month });

        // Calcular el inicio y fin de la semana actual
        const weekStart = format(startOfWeek(this.currentDate, { weekStartsOn: 1 }), 'yyyy-MM-dd');
        // Sumar 4 días para obtener el fin de semana (viernes)
        const weekEnd = format(addDays(startOfWeek(this.currentDate, { weekStartsOn: 1 }), 4), 'yyyy-MM-dd');

        // Llamada al store para obtener citas de la semana
        await this.$store.dispatch('doctor/fetchAppointments', { start: weekStart, end: weekEnd });
        this.availability = this.$store.state.doctor.availability;// Obtener la disponibilidad
        this.appointments = this.$store.state.doctor.appointments;// Obtener las citas
      } catch (error) {
        console.error('Error al cargar datos del calendario:', error);
      }
    },
    // Funciones para manejar la disponibilidad y citas
    async saveAvailability(newAvailability) {
      try {
        // Llamada al store para guardar la nueva disponibilidad
        await this.$store.dispatch('doctor/saveAvailability', newAvailability);
        this.fetchData();
      } catch (error) {
        console.error('Error al guardar disponibilidad:', error);
      }
    },
    // Funciones para manejar citas
    async updateAppointment(updatedAppointment) {
      try {
        // Llamada al store para actualizar la cita
        await this.$store.dispatch('doctor/updateAppointment', updatedAppointment);
        // Refrescar los datos del calendario
        this.fetchData();
        this.selectedAppointment = null;
      } catch (error) {
        console.error('Error al actualizar cita:', error);
      }
    },
    // Función para cancelar una cita
    async cancelAppointment(appointmentId) {
      try {
        // Llamada al store para cancelar la cita
        await this.$store.dispatch('doctor/cancelAppointment', appointmentId);
        // Refrescar los datos del calendario
        this.fetchData();
        this.selectedAppointment = null;
        // atrapado de errores
      } catch (error) {
        console.error('Error al cancelar cita:', error);///debbug
      }
    },
    // Función para eliminar una disponibilidad
    async eliminarDisponibilidad(date, time) {
      // Buscar el ID del horario correspondiente
      const slot = this.availability.find(a => a.date === date && a.time === time && a.available);
      if (!slot || !slot.id) {
        alert('No se encontró el ID del horario para eliminar.');
        return;
      }
      await this.$store.dispatch('doctor/eliminarDisponibilidad', slot.id);// Llamada al store para eliminar la disponibilidad
      this.fetchData();
    }
  },
  // Ciclo de vida del componente para cargar datos al montarse
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
  overflow: hidden;
}

.time-column {
  width: 80px;
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
  min-width: 0;
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