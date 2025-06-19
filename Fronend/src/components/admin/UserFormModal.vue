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
            @input="formData.nombre = formData.nombre.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]/g,'')"
          />
        </div>

        <div class="form-group">
          <label for="apellido">Apellido:</label>
          <input 
            type="text" 
            id="apellido" 
            v-model="formData.apellido" 
            required
            @input="formData.apellido = formData.apellido.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]/g,'')"
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
            @input="formData.telefono = formData.telefono.replace(/[^0-9+]/g, '').replace(/^(?!\+569)/, '+569')"
            required
          />
          <small>Debe comenzar con +569 y tener 8 dígitos después (ej: +56912345678)</small>
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
          <div class = "form-group-medic" v-if="selectedRol === 'MEDICO'">
            <label for="especial8idad">Especialidad:</label>
                <select id ="especialidad" v-model="formData.especialidad" required>
                  <option value="">Seleccione una especialidad</option>
                  <option v-for ="especialidad in especialidades" :key="especialidad.id_especialidad" :value="especialidad.nombre">
                    {{ especialidad.nombre }}
                  </option>
                </select>
          </div>
        </div>

        <div class="form-group" v-if="selectedRol === 'FARMACEUTICO'">
          <label for="farmacia">Farmacia:</label>
          <select id="farmacia" v-model="formData.farmaciaId" required>
            <option value="">Seleccione una farmacia</option>
            <option v-for="farmacia in farmacias" :key="farmacia.idFarmacia" :value="farmacia.idFarmacia">
              {{ farmacia.nombre }}
            </option>
          </select>
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
import { ref, computed, watch, onMounted } from 'vue';
import employeeService from '@/services/employee.service';
import PharmacyService from '@/services/PharmacyService';
import specialtyService from '@/services/specialtyService';

const props = defineProps({
  employee: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['submit', 'cancel', 'error']);

// DEFINICIÓN DE VARIABLES Y FUNCIONES
// mapa de roles con sus respectivos IDs y nombres
const rolMap = {
  'MEDICO': { id_rol: 2, nombre: 'Médico' },
  'TECNICO': { id_rol: 3, nombre: 'Técnico' },
  'RECEPCIONISTA': { id_rol: 4, nombre: 'Recepcionista' },
  'FARMACEUTICO': { id_rol: 5, nombre: 'Farmacéutico' }
};

const selectedRol = ref('');// Rol seleccionado por el usuario
//datos del formulario
const formData = ref({
  nombre: '',
  apellido: '',
  rut: '',
  correo: '',
  telefono: '',
  password: '',
  rol: null,
  especialidad: '',
  farmaciaId: ''

});

const farmacias = ref([]);
const especialidades = ref([])

// Computed para verificar si el rol seleccionado es Médico
const isMedico = computed(() => selectedRol.value === 'MEDICO');

watch(
  () => props.employee,
  (newVal) => {
    if (newVal) {
      formData.value = {
        nombre: newVal.nombre || '',
        apellido: newVal.apellido || '',
        rut: newVal.rut || '',
        correo: newVal.correo || '',
        telefono: newVal.telefono || '',
        password: '',
        rol: newVal.rol || null,
        especialidad: newVal.especialidad || '',
        farmaciaId: newVal.farmacia?.idFarmacia || ''
      };
      selectedRol.value = Object.keys(rolMap).find(key => rolMap[key].nombre === (newVal.rol?.nombre || newVal.rol)) || '';
    } else {
      formData.value = {
        nombre: '', apellido: '', rut: '', correo: '', telefono: '', password: '', rol: null, especialidad: '', farmaciaId: ''
      };
      selectedRol.value = '';
    }
  },
  { immediate: true }
);

const handleRolChange = () => {
  // Si el rol seleccionado es Médico, habilitar el campo de especialidad
  if (selectedRol.value) {
    formData.value.rol = rolMap[selectedRol.value];
  } else {
    formData.value.rol = null;
  }
};

// Formatear el RUT al ingresar
const formatRut = () => {
  let value = formData.value.rut.replace(/[^0-9kK]/g, '');
  if (value.length > 1) {
    const dv = value.slice(-1);
    const rutBody = value.slice(0, -1);
    formData.value.rut = rutBody.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '-' + dv;// Formatear con puntos y guiones
  } else {
    formData.value.rut = value;
  }
};
// Manejo del envío del formulario
const handleSubmit = async () => {
  try {
    if (!formData.value.rol) {
      emit('error', 'Debe seleccionar un rol');
      return;
    }
    // Limpiar RUT para el backend (sin puntos ni guion)
    const cleanRut = formData.value.rut.replace(/\.|-/g, '');
    const employeeData = {
      rut: formData.value.rut,
      nombre: formData.value.nombre,
      apellido: formData.value.apellido,
      correo: formData.value.correo,
      telefono: formData.value.telefono,
      rol: formData.value.rol
    };
    if (formData.value.especialidad && isMedico.value) {
      employeeData.especialidad = formData.value.especialidad;
    }
    if (selectedRol.value === 'FARMACEUTICO' && formData.value.farmaciaId) {
      employeeData.farmaciaId = formData.value.farmaciaId;
    }
    // Solo agregar password si es registro o si el usuario la cambió
    if (!props.employee || formData.value.password) {
      employeeData.password = formData.value.password;
    }
    let response;
    if (props.employee && props.employee.rut) {
      // Actualizar (usar rut limpio)
      response = await employeeService.updateEmployee(cleanRut, employeeData);
    } else {
      // Registrar
      response = await employeeService.registerEmployee(employeeData);
    }
    emit('submit', response);
    //atrapado de errores
  } catch (error) {
    emit('error', error.response?.data?.message || 'Error al registrar/actualizar empleado');
  }
};

const cancelar = () => emit('cancel');

onMounted(async () => {
  try {
    farmacias.value = await PharmacyService.getAllFarmacias();
  } catch (e) {
    farmacias.value = [];
  }

  try {
    // Corrección: usar el import correcto y el método correcto
    const res = await specialtyService.getAllEspecialidad();
    // Si la respuesta es un objeto con .data, usarlo
    especialidades.value = res.data ? res.data : res;
  } catch (e) {
    especialidades.value = [];
  }

});

const validarTelefono = () => {
  const regex = /^\+569\d{8}$/;
  if (!regex.test(formData.value.telefono)) {
    alert('El teléfono debe tener el formato +569XXXXXXXX');
    formData.value.telefono = '';
  }
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