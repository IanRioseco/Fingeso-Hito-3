<template>
  <div class="modal-form">
    <h2>Registro de Trabajador</h2>
    <form @submit.prevent="handleSubmit" class="worker-form">
      <!-- Información Personal -->
      <div class="form-section">
        <h3>Información Personal</h3>
        <div class="form-group">
          <label for="nombre">Nombre Completo:</label>
          <input 
            type="text" 
            id="nombre" 
            v-model="formData.nombre" 
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
          />
        </div>

        <div class="form-group">
          <label for="email">Correo Electrónico:</label>
          <input 
            type="email" 
            id="email" 
            v-model="formData.email" 
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
      </div>

      <!-- Información Laboral -->
      <div class="form-section">
        <h3>Información Laboral</h3>
        <div class="form-group">
          <label for="cargo">Cargo:</label>
          <select id="cargo" v-model="formData.cargo" required>
            <option value="">Seleccione un cargo</option>
            <option value="medico">Médico</option>
            <option value="enfermero">Enfermero/a</option>
            <option value="administrativo">Administrativo</option>
            <option value="tecnico">Técnico</option>
          </select>
        </div>

        <div class="form-group">
          <label for="especialidad">Especialidad:</label>
          <input 
            type="text" 
            id="especialidad" 
            v-model="formData.especialidad"
            :disabled="formData.cargo !== 'medico'"
          />
        </div>

        <div class="form-group">
          <label for="fechaIngreso">Fecha de Ingreso:</label>
          <input 
            type="date" 
            id="fechaIngreso" 
            v-model="formData.fechaIngreso" 
            required
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
import { ref } from 'vue';

const formData = ref({
  nombre: '',
  rut: '',
  email: '',
  telefono: '',
  cargo: '',
  especialidad: '',
  fechaIngreso: ''
});

const emit = defineEmits(['submit', 'cancel']);

const handleSubmit = () => {
  emit('submit', formData.value);
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