<template>
  <div class="modal-form">
    <h2>Registro de Empleado</h2>
    <form @submit.prevent="handleSubmit" class="worker-form">
      <!-- Información Personal -->
      <div class="form-section">
        <h3>Información Personal</h3>
        <div class="form-group">
          <label for="nombre">Nombre:</label>
          <input 
            type="text" 
            id="nombre" 
            v-model="formData.nombre" 
            required
          />
        </div>

        <div class="form-group">
          <label for="apellido">Apellido:</label>
          <input 
            type="text" 
            id="apellido" 
            v-model="formData.apellido" 
            required
          />
        </div>

        <div class="form-group">
          <label for="rut">RUT:</label>
          <input 
            type="text" 
            id="rut" 
            v-model="formData.rut" 
            required
            placeholder="12345678-9"
            @input="formatRut"
          />
        </div>

        <div class="form-group">
          <label for="correo">Correo Electrónico:</label>
          <input 
            type="email" 
            id="correo" 
            v-model="formData.correo" 
            required
          />
        </div>

        <div class="form-group">
          <label for="telefono">Teléfono:</label>
          <input 
            type="tel" 
            id="telefono" 
            v-model="formData.telefono"
            placeholder="+56 9 XXXX XXXX"
          />
        </div>

        <div class="form-group">
          <label for="password">Contraseña:</label>
          <input 
            type="password" 
            id="password" 
            v-model="formData.password" 
            required
          />
        </div>
      </div>

      <!-- Información Laboral -->
      <div class="form-section">
        <h3>Información Laboral</h3>
        <div class="form-group">
          <label for="rol">Rol:</label>
          <select id="rol" v-model="selectedRol" required @change="handleRolChange">
            <option value="">Seleccione un rol</option>
            <option value="MEDICO">Médico</option>
            <option value="TECNICO">Técnico</option>
            <option value="RECEPCIONISTA">Recepcionista</option>
            <option value="FARMACEUTICO">Farmacéutico</option>
          </select>
        </div>

        <div class="form-group">
          <label for="especialidad">Especialidad:</label>
          <input 
            type="text" 
            id="especialidad" 
            v-model="formData.especialidad"
            :disabled="!isMedico"
            :required="isMedico"
          />
        </div>
      </div>

      <div class="form-actions">
        <button type="button" class="btn-cancelar" @click="cancelar">Cancelar</button>
        <button type="submit" class="btn-guardar">Guardar</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import employeeService from '@/services/employee.service';

const rolMap = {
  'MEDICO': { id_rol: 2, nombre: 'Médico' },
  'TECNICO': { id_rol: 3, nombre: 'Técnico' },
  'RECEPCIONISTA': { id_rol: 4, nombre: 'Recepcionista' },
  'FARMACEUTICO': { id_rol: 5, nombre: 'Farmacéutico' }
};

const selectedRol = ref('');
const formData = ref({
  nombre: '',
  apellido: '',
  rut: '',
  correo: '',
  telefono: '',
  password: '',
  rol: null,
  especialidad: ''
});

const emit = defineEmits(['submit', 'cancel', 'error']);

const isMedico = computed(() => selectedRol.value === 'MEDICO');

const handleRolChange = () => {
  if (selectedRol.value) {
    formData.value.rol = rolMap[selectedRol.value];
  } else {
    formData.value.rol = null;
  }
};

const formatRut = () => {
  let value = formData.value.rut.replace(/[^0-9kK]/g, '');
  
  if (value.length > 1) {
    const dv = value.slice(-1);
    const rutBody = value.slice(0, -1);
    formData.value.rut = rutBody.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '-' + dv;
  } else {
    formData.value.rut = value;
  }
};

const handleSubmit = async () => {
  try {
    // Validar que el rol esté seleccionado
    if (!formData.value.rol) {
      emit('error', 'Debe seleccionar un rol');
      return;
    }

    // Crear el objeto base del empleado
    const employeeData = {
      rut: formData.value.rut,
      nombre: formData.value.nombre,
      apellido: formData.value.apellido,
      correo: formData.value.correo,
      telefono: formData.value.telefono,
      rol: formData.value.rol  // Ya es un objeto completo con id_rol y nombre
    };

    if (formData.value.especialidad && isMedico.value) {
      employeeData.especialidad = formData.value.especialidad;
    }
    
    console.log('Objeto empleado a enviar:', JSON.stringify(employeeData, null, 2));
    
    const response = await employeeService.registerEmployee(employeeData);
    emit('submit', response);
  } catch (error) {
    console.error('Error al registrar empleado:', error);
    emit('error', error.message || 'Error al registrar el empleado');
  }
};

const cancelar = () => {
  emit('cancel');
};
</script>

<style scoped>
.modal-form {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.worker-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-section {
  border: 1px solid #e0e0e0;
  padding: 15px;
  border-radius: 6px;
}

.form-section h3 {
  margin-bottom: 15px;
  color: #0875C1;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #333;
}

input, select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

input:focus, select:focus {
  border-color: #0875C1;
  outline: none;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.btn-cancelar, .btn-guardar {
  padding: 8px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.btn-cancelar {
  background: #fff;
  border: 1px solid #C51A6F;
  color: #C51A6F;
}

.btn-guardar {
  background: #0875C1;
  border: 1px solid #0875C1;
  color: white;
}

.btn-cancelar:hover {
  background: #C51A6F;
  color: white;
}

.btn-guardar:hover {
  background: #065a94;
}

input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}
</style>