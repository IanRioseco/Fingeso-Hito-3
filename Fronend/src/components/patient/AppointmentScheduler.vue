<template>
  <div class="appointment-scheduler">
    <h2>Agendar Nueva Cita</h2>
    <!--pasos para el agendamiento de cita-->
    <div class="scheduler-steps">
      <div class="step" :class="{ 'active': currentStep === 1, 'completed': currentStep > 1 }">
        <div class="step-number">1</div>
        <div class="step-label">Especialidad</div>
      </div>
      <div class="step" :class="{ 'active': currentStep === 2, 'completed': currentStep > 2 }">
        <div class="step-number">2</div>
        <div class="step-label">Médico</div>
      </div>
      <div class="step" :class="{ 'active': currentStep === 3, 'completed': currentStep > 3 }">
        <div class="step-number">3</div>
        <div class="step-label">Fecha y Hora</div>
      </div>
      <div class="step" :class="{ 'active': currentStep === 4 }">
        <div class="step-number">4</div>
        <div class="step-label">Confirmación</div>
      </div>
    </div>
    
    <div class="step-content">
      <!-- Paso 1: Selección de especialidad -->
      <div v-if="currentStep === 1" class="specialty-selection">
        <h3>Selecciona una especialidad</h3>
        <div class="specialty-grid">
          <div 
            v-for="specialty in specialties" 
            :key="specialty.id"
            class="specialty-card"
            @click="selectSpecialty(specialty)"
            :class="{ 'selected': selectedSpecialty?.id === specialty.id }"
          >
            <div class="specialty-icon">
              <i :class="specialty.icon"></i>
            </div>
            <div class="specialty-name">{{ specialty.name }}</div>
          </div>
        </div>
      </div>
      
      <!-- Paso 2: Selección de médico -->
      <div v-if="currentStep === 2" class="doctor-selection">
        <h3>Selecciona un médico</h3>
        <div class="search-filter">
          <input 
            v-model="doctorSearch" 
            type="text" 
            placeholder="Buscar médico..." 
            class="search-input"
          >
        </div>
        <div class="doctor-list">
          <div 
            v-for="doctor in filteredDoctors" 
            :key="doctor.id"
            class="doctor-card"
            @click="selectDoctor(doctor)"
            :class="{ 'selected': selectedDoctor?.id === doctor.id }"
          >
            <div class="doctor-avatar">
              <img :src="doctor.avatar" :alt="`Dr. ${doctor.lastName}`">
            </div>
            <div class="doctor-info">
              <div class="doctor-name">Dr. {{ doctor.firstName }} {{ doctor.lastName }}</div>
              <div class="doctor-specialty">{{ doctor.specialty }}</div>
              <div class="doctor-rating">
                <i class="fas fa-star"></i>
                <span>{{ doctor.rating }}</span>
                <span class="reviews">({{ doctor.reviews }} evaluaciones)</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Paso 3: Selección de fecha y hora -->
      <div v-if="currentStep === 3" class="datetime-selection">
        <h3>Selecciona fecha y hora</h3>
        <div class="calendar-container">
          <div class="calendar-header">
            <button @click="prevMonth" class="nav-button">
              <i class="fas fa-chevron-left"></i>
            </button>
            <div class="month-year">
              {{ currentMonthName }} {{ currentYear }}
            </div>
            <button @click="nextMonth" class="nav-button">
              <i class="fas fa-chevron-right"></i>
            </button>
          </div>
          <div class="calendar-grid">
            <div class="day-header" v-for="day in dayNames" :key="day">
              {{ day }}
            </div>
            <div 
              v-for="day in calendarDays" 
              :key="day.date"
              class="calendar-day"
              :class="{
                'current-month': day.isCurrentMonth,
                'available': day.isAvailable,
                'selected': day.isSelected,
                'today': day.isToday
              }"
              @click="selectDate(day)"
            >
              <div class="day-number">{{ day.day }}</div>
              <div v-if="day.isAvailable" class="day-slots">
                {{ day.availableSlots }} horarios
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="selectedDate" class="time-selection">
          <h4>
            <template v-if="availableTimeSlots.length > 0">
              Horarios disponibles para el {{ formatDateForDisplay(selectedDate) }}
            </template>
            <template v-else>
              No hay horarios disponibles para el {{ formatDateForDisplay(selectedDate) }}
            </template>
          </h4>
          <div class="time-slots">
            <button 
              v-for="slot in availableTimeSlots" 
              :key="slot.time"
              class="time-slot"
              @click="selectTimeSlot(slot)"
              :class="{ 'selected': selectedTimeSlot?.time === slot.time }"
            >
              {{ slot.time }}
            </button>
          </div>
        </div>
      </div>
      
      <!-- Paso 4: Confirmación -->
      <div v-if="currentStep === 4" class="confirmation-step">
        <h3>Confirma tu cita</h3>
        <div class="appointment-summary">
          <div class="summary-item">
            <span class="summary-label">Especialidad:</span>
            <span class="summary-value">{{ selectedSpecialty.name }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">Médico:</span>
            <span class="summary-value">Dr. {{ selectedDoctor.firstName }} {{ selectedDoctor.lastName }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">Fecha:</span>
            <span class="summary-value">{{ formatDateForDisplay(selectedDate) }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">Hora:</span>
            <span class="summary-value">{{ selectedTimeSlot.time }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">Ubicación:</span>
            <span class="summary-value">{{ selectedDoctor.location }}</span>
          </div>
        </div>
        
        <div class="confirmation-actions">
          <button @click="currentStep = 3" class="btn-back">
            <i class="fas fa-arrow-left"></i> Volver
          </button>
          <button @click="confirmAppointment" class="btn-confirm">
            Confirmar Cita <i class="fas fa-check"></i>
          </button>
        </div>
        
        <!-- Mensajes de éxito y error -->
        <div v-if="appointmentSuccess" class="alert alert-success">
          {{ appointmentSuccess }}
        </div>
        <div v-if="appointmentError" class="alert alert-error">
          {{ appointmentError }}
        </div>
      </div>
    </div>
    
    <div class="step-actions">
      <button 
        v-if="currentStep > 1" 
        @click="prevStep" 
        class="btn-prev"
      >
        <i class="fas fa-arrow-left"></i> Anterior
      </button>
      <button 
        v-if="currentStep < 4" 
        @click="nextStep" 
        class="btn-next"
        :disabled="!canProceed"
      >
        Siguiente <i class="fas fa-arrow-right"></i>
      </button>
    </div>
  </div>
</template>

<script>
import { format, startOfMonth, endOfMonth, eachDayOfInterval, isSameMonth, isToday, isSameDay, addMonths, subMonths, parseISO } from 'date-fns';
import { es } from 'date-fns/locale';


export default {
  name: 'AppointmentScheduler',
  filters: {
    formatDate(date) {
      if (!date) return '';
      // Forzar a string yyyy-MM-dd
      if (typeof date === 'string' && date.length === 10 && date[4] === '-' && date[7] === '-') {
        return format(parseISO(date), "EEEE d 'de' MMMM 'de' yyyy", { locale: es });
      }
      return '';
    }
  },
  data() {
    return {
      currentStep: 1,
      selectedSpecialty: null,
      selectedDoctor: null,
      selectedDate: null,
      selectedTimeSlot: null,
      doctorSearch: '',
      currentDate: new Date(),
      specialties: [
        { id: 1, name: 'Cardiología', icon: 'fas fa-heart' },
        { id: 2, name: 'Dermatología', icon: 'fas fa-allergies' },
        { id: 3, name: 'Pediatría', icon: 'fas fa-baby' },
        { id: 4, name: 'Neurología', icon: 'fas fa-brain' },
        { id: 5, name: 'Oftalmología', icon: 'fas fa-eye' },
        { id: 6, name: 'Ortopedia', icon: 'fas fa-bone' },
        { id: 7, name: 'Ginecología', icon: 'fas fa-female' },
        { id: 8, name: 'Urología', icon: 'fas fa-male' }
      ],
      doctors: [], // Esto se cargaría según la especialidad seleccionada
      availableDates: [], // Esto vendría del backend
      availableTimeSlots: [], // Esto vendría del backend
      appointmentError: null,
      appointmentSuccess: null
    }
  },
  computed: {
    canProceed() {
      switch(this.currentStep) {
        case 1: return this.selectedSpecialty !== null;
        case 2: return this.selectedDoctor !== null;
        case 3: return this.selectedDate !== null && this.selectedTimeSlot !== null;
        default: return true;
      }
    },
    filteredDoctors() {
      if (!this.doctorSearch) return this.doctors;
      const search = this.doctorSearch.toLowerCase();
      return this.doctors.filter(doctor => 
        doctor.firstName.toLowerCase().includes(search) || 
        doctor.lastName.toLowerCase().includes(search)
      );
    },
    currentMonthName() {
      return format(this.currentDate, 'MMMM', { locale: es });
    },
    currentYear() {
      return format(this.currentDate, 'yyyy');
    },
    dayNames() {
      return ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];
    },
    calendarDays() {
      // Genera la grilla del calendario mensual, alineando correctamente los días de la semana
      const start = startOfMonth(this.currentDate);
      const end = endOfMonth(this.currentDate);
      const days = [];
      // Día de la semana del primer día del mes (0=domingo, 1=lunes...)
      let firstDay = start.getDay();
      firstDay = firstDay === 0 ? 7 : firstDay; // Ajuste: 1=lunes, 7=domingo
      // Rellenar días vacíos al inicio para alinear con lunes
      for (let i = 1; i < firstDay; i++) {
        days.push({ empty: true });
      }
      // Días reales del mes
      for (let d = 0; d < end.getDate(); d++) {
        const dateObj = new Date(start.getFullYear(), start.getMonth(), d + 1);
        const dateStr = format(dateObj, 'yyyy-MM-dd');
        const availableObj = this.availableDates.find(ad => ad.date === dateStr);
        days.push({
          date: dateStr,
          day: (d + 1).toString(),
          isCurrentMonth: true,
          isAvailable: !!availableObj,
          isSelected: this.selectedDate === dateStr,
          isToday: format(new Date(), 'yyyy-MM-dd') === dateStr,
          availableSlots: availableObj ? availableObj.slots : 0
        });
      }
      // Rellenar días vacíos al final para completar la última semana
      while (days.length % 7 !== 0) {
        days.push({ empty: true });
      }
      return days;
    }
  },
  methods: {
    formatDateForDisplay(date) {
      if (!date) return '';
      try {
        // Si ya es string yyyy-MM-dd
        if (typeof date === 'string' && date.length === 10 && date[4] === '-' && date[7] === '-') {
          return format(parseISO(date), "EEEE d 'de' MMMM 'de' yyyy", { locale: es });
        }
        // Si es Date
        if (date instanceof Date) {
          return format(date, "EEEE d 'de' MMMM 'de' yyyy", { locale: es });
        }
        // Fallback
        return String(date);
      } catch (e) {
        return String(date);
      }
    },
    nextStep() {
      if (this.canProceed) {
        if (this.currentStep === 1) {
          this.fetchDoctors();
        } else if (this.currentStep === 2) {
          this.fetchAvailableDates();
        }
        this.currentStep++;
      }
    },
    prevStep() {
      this.currentStep--;
    },
    selectSpecialty(specialty) {
      this.selectedSpecialty = specialty;
    },
    selectDoctor(doctor) {
      this.selectedDoctor = doctor;
    },
    prevMonth() {
      this.currentDate = subMonths(this.currentDate, 1);
      this.fetchAvailableDates();
    },
    nextMonth() {
      this.currentDate = addMonths(this.currentDate, 1);
      this.fetchAvailableDates();
    },
    selectDate(day) {
      if (day.isAvailable) {
        this.selectedDate = day.date;
        const found = this.availableDates.find(d => d.date === this.selectedDate);
        // slotTimes ya tiene { time, idHorario }
        this.availableTimeSlots = found ? found.slotTimes : [];
        this.selectedTimeSlot = null;
      }
    },
    selectTimeSlot(slot) {
      this.selectedTimeSlot = slot;
    },
    async fetchDoctors() {
      try {
        this.doctors = await this.$store.dispatch('appointments/fetchDoctors', this.selectedSpecialty.id);
      } catch (error) {
        console.error('Error al cargar médicos:', error);
      }
    },
    async fetchAvailableDates() {
      try {
        if (!this.selectedDoctor) return;
        // Obtener horarios reales del backend (por médico)
        const horarios = await this.$store.dispatch('appointments/fetchAvailability', {
          doctorId: this.selectedDoctor.id
        });
        // Agrupa por fecha real (el backend entrega fecha y hora exacta)
        const grouped = {};
        horarios.forEach(horario => {
          const fecha = horario.fecha;
          if (!grouped[fecha]) grouped[fecha] = [];
          // Genera slots de 30 minutos entre horainicio y horafin, cada slot lleva idHorario
          let [startHour, startMin] = horario.horainicio.split(':').map(Number);
          let [endHour, endMin] = horario.horafin.split(':').map(Number);
          let current = startHour * 60 + startMin;
          const endTime = endHour * 60 + endMin;
          while (current < endTime) {
            const hour = Math.floor(current / 60).toString().padStart(2, '0');
            const min = (current % 60).toString().padStart(2, '0');
            grouped[fecha].push({
              time: `${hour}:${min}`,
              idHorario: horario.id // Asocia el idHorario a cada slot
            });
            current += 30;
          }
        });
        this.availableDates = Object.keys(grouped).map(date => ({
          date,
          slots: grouped[date].length,
          available: grouped[date].length > 0,
          slotTimes: grouped[date]
        }));
      } catch (error) {
        console.error('Error al cargar fechas disponibles:', error);
      }
    },
    async confirmAppointment() {
      this.appointmentError = null;
      this.appointmentSuccess = null;
      try {
        // Construir el objeto cita para el backend
        const appointmentData = {
          idMedico: this.selectedDoctor.id,
          idHorario: this.selectedTimeSlot.idHorario, // Ahora sí se envía el idHorario correcto
          estado: 'CitaAgendada'
        };
        console.log('appointmentData', appointmentData, 'selectedTimeSlot', this.selectedTimeSlot, 'selectedDoctor', this.selectedDoctor);
        // Llama a la acción del store para crear la cita
        const result = await this.$store.dispatch('appointments/createAppointment', appointmentData);
        if (result.success) {
          this.appointmentSuccess = '¡Cita agendada exitosamente!';
          setTimeout(() => {
            this.$router.push('/patient/appointments');
          }, 1200);
        } else {
          this.appointmentError = result.error || 'No se pudo agendar la cita.';
        }
      } catch (error) {
        this.appointmentError = error.message || 'Error inesperado al agendar la cita.';
      }
    },
    generateAvailableDatesForMonth(horarios, currentDate) {
      const start = startOfMonth(currentDate);
      const end = endOfMonth(currentDate);
      const days = eachDayOfInterval({ start, end });
      const availableDates = [];
      days.forEach(date => {
        const jsDay = date.getDay() === 0 ? 7 : date.getDay();
        const horario = horarios.find(h => h.dayOfWeek === jsDay);
        if (
          horario &&
          typeof horario.start === 'string' && horario.start &&
          typeof horario.end === 'string' && horario.end
        ) {
          const slots = [];
          const [startHour, startMin] = horario.start.split(':').map(Number);
          const [endHour, endMin] = horario.end.split(':').map(Number);
          if (!isNaN(startHour) && !isNaN(startMin) && !isNaN(endHour) && !isNaN(endMin)) {
            let current = new Date(date);
            current.setHours(startHour, startMin, 0, 0);
            const endTime = new Date(date);
            endTime.setHours(endHour, endMin, 0, 0);
            while (current < endTime) {
              slots.push(format(new Date(current), 'HH:mm'));
              current.setMinutes(current.getMinutes() + 30);
            }
          }
          availableDates.push({
            date: format(date, 'yyyy-MM-dd'),
            slots: slots.length,
            available: slots.length > 0,
            slotTimes: slots
          });
        }
      });
      return availableDates;
    }
  }
}
</script>

<style scoped>
.appointment-scheduler {
  padding: 1rem;
}

.scheduler-steps {
  display: flex;
  justify-content: space-between;
  margin: 2rem 0;
  position: relative;
}

.scheduler-steps::before {
  content: '';
  position: absolute;
  top: 20px;
  left: 0;
  right: 0;
  height: 2px;
  background-color: #ddd;
  z-index: 1;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 2;
}

.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #ddd;
  color: #747473;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.step-label {
  color: #747473;
  font-size: 0.9rem;
}

.step.active .step-number {
  background-color: #009999;
  color: white;
}

.step.completed .step-number {
  background-color: #99cc00;
  color: white;
}

.step-content {
  margin: 2rem 0;
}

.specialty-selection h3, 
.doctor-selection h3, 
.datetime-selection h3,
.confirmation-step h3 {
  color: #009999;
  margin-bottom: 1.5rem;
}

.specialty-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
}

.specialty-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1.5rem 1rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.specialty-card:hover {
  border-color: #009999;
  box-shadow: 0 0 10px rgba(0, 153, 153, 0.1);
}

.specialty-card.selected {
  border-color: #009999;
  background-color: #f0f9f9;
}

.specialty-icon {
  font-size: 2rem;
  color: #009999;
  margin-bottom: 0.5rem;
}

.specialty-name {
  font-weight: 500;
}

.search-filter {
  margin-bottom: 1.5rem;
}

.search-input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.doctor-list {
  display: grid;
  gap: 1rem;
}

.doctor-card {
  display: flex;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.doctor-card:hover {
  border-color: #009999;
  box-shadow: 0 0 10px rgba(0, 153, 153, 0.1);
}

.doctor-card.selected {
  border-color: #009999;
  background-color: #f0f9f9;
}

.doctor-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 1rem;
  flex-shrink: 0;
}

.doctor-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.doctor-info {
  flex-grow: 1;
}

.doctor-name {
  font-weight: 600;
  margin-bottom: 0.25rem;
}

.doctor-specialty {
  color: #747473;
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
}

.doctor-rating {
  color: #ffc107;
  font-size: 0.9rem;
}

.doctor-rating .reviews {
  color: #747473;
  margin-left: 0.5rem;
}

.calendar-container {
  max-width: 600px;
  margin: 0 auto 2rem;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.nav-button {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #009999;
  padding: 0.5rem;
}

.month-year {
  font-weight: 600;
  font-size: 1.2rem;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0.5rem;
}

.day-header {
  text-align: center;
  font-weight: 600;
  padding: 0.5rem;
  color: #009999;
}

.calendar-day {
  aspect-ratio: 1;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 0.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
}

.calendar-day.current-month {
  background-color: white;
}

.calendar-day:not(.current-month) {
  background-color: #f9f9f9;
  color: #aaa;
}

.calendar-day.today {
  border-color: #009999;
}

.calendar-day.available {
  cursor: pointer;
  border-color: #99cc00;
}

.calendar-day.available:hover {
  background-color: #f8fff8;
}

.calendar-day.selected {
  background-color: #99cc00;
  color: white;
}

.day-number {
  font-weight: 600;
}

.day-slots {
  font-size: 0.7rem;
  background-color: #e8f5e9;
  padding: 0.1rem 0.3rem;
  border-radius: 4px;
  color: #2e7d32;
}

.calendar-day.selected .day-slots {
  background-color: #2e7d32;
  color: white;
}

.time-selection h4 {
  color: #009999;
  margin-bottom: 1rem;
  text-align: center;
}

.time-slots {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 0.5rem;
}

.time-slot {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  text-align: center;
  cursor: pointer;
  background-color: white;
}

.time-slot:hover {
  border-color: #009999;
}

.time-slot.selected {
  background-color: #009999;
  color: white;
  border-color: #009999;
}

.appointment-summary {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.summary-item {
  display: flex;
  margin-bottom: 1rem;
}

.summary-label {
  font-weight: 600;
  width: 120px;
  color: #009999;
}

.summary-value {
  flex-grow: 1;
}

.confirmation-actions {
  display: flex;
  justify-content: space-between;
}

.btn-back, .btn-confirm {
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-back {
  background-color: #f5f5f5;
  color: #747473;
  border: none;
}

.btn-confirm {
  background-color: #99cc00;
  color: white;
  border: none;
}

.step-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
}

.btn-prev, .btn-next {
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-prev {
  background-color: #f5f5f5;
  color: #747473;
  border: none;
}

.btn-next {
  background-color: #009999;
  color: white;
  border: none;
}

.btn-next:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}

.alert {
  padding: 0.75rem 1.25rem;
  border-radius: 4px;
  margin-top: 1rem;
  font-size: 0.9rem;
}

.alert-success {
  background-color: #e8f5e9;
  color: #2e7d32;
  border: 1px solid #c8e6c9;
}

.alert-error {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}
</style>