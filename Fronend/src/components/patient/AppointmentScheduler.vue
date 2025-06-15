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
          <h4>Horarios disponibles para el {{ selectedDate | formatDate }}</h4>
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
            <span class="summary-value">{{ selectedDate | formatDate }}</span>
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
      return format(date, "EEEE d 'de' MMMM 'de' yyyy", { locale: es });
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
      availableTimeSlots: [] // Esto vendría del backend
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
      const start = startOfMonth(this.currentDate);
      const end = endOfMonth(this.currentDate);
      const days = eachDayOfInterval({ start, end });
      
      // Añadir días del mes anterior para completar la primera semana
      const firstDayOfWeek = start.getDay() === 0 ? 6 : start.getDay() - 1;
      if (firstDayOfWeek > 0) {
        const prevMonthDays = eachDayOfInterval({
          start: subMonths(start, 1),
          end: subMonths(start, 1)
        }).slice(-firstDayOfWeek);
        days.unshift(...prevMonthDays.map(date => ({ date, isCurrentMonth: false })));
      }
      
      // Añadir días del próximo mes para completar la última semana
      const lastDayOfWeek = end.getDay() === 0 ? 0 : 7 - end.getDay();
      if (lastDayOfWeek > 0) {
        const nextMonthDays = eachDayOfInterval({
          start: addMonths(end, 1),
          end: addMonths(end, 1)
        }).slice(0, lastDayOfWeek);
        days.push(...nextMonthDays.map(date => ({ date, isCurrentMonth: false })));
      }
      
      return days.map(day => {
        const date = day.date || day;
        const isCurrentMonth = day.isCurrentMonth !== false;
        const isAvailable = this.availableDates.some(d => 
          isSameDay(parseISO(d.date), date) && d.available
        );
        const availableSlots = this.availableDates.find(d => 
          isSameDay(parseISO(d.date), date)
        )?.slots || 0;
        
        return {
          date,
          day: format(date, 'd'),
          isCurrentMonth,
          isAvailable: isCurrentMonth && isAvailable,
          isSelected: this.selectedDate && isSameDay(date, this.selectedDate),
          isToday: isToday(date),
          availableSlots
        };
      });
    }
  },
  methods: {
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
        // Usar los slots generados en el frontend
        const found = this.availableDates.find(d => d.date === day.date);
        this.availableTimeSlots = found ? found.slotTimes.map(time => ({ time })) : [];
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
        const horarios = await this.$store.dispatch('appointments/fetchAvailability', {
          doctorId: this.selectedDoctor.id
        });
        this.availableDates = this.generateAvailableDatesForMonth(horarios, this.currentDate);
      } catch (error) {
        console.error('Error al cargar fechas disponibles:', error);
      }
    },
    async fetchAvailableTimeSlots() {
      // Ya no es necesario llamar al backend, los slots se generan en el frontend
      // Esta función puede quedar vacía o eliminarse si no se usa en otro lado
    },
    async confirmAppointment() {
      try {
        const appointmentData = {
          specialtyId: this.selectedSpecialty.id,
          doctorId: this.selectedDoctor.id,
          date: format(this.selectedDate, 'yyyy-MM-dd'),
          time: this.selectedTimeSlot.time,
          patientId: this.$store.state.auth.user.id
        };
        await this.$store.dispatch('appointments/createAppointment', appointmentData);
        this.$router.push('/patient/appointments');
      } catch (error) {
        console.error('Error al confirmar cita:', error);
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
</style>