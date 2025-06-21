<template>
  <div class="user-management">
    <div class="header-actions">
      <h2>Gestión de Empleados</h2>
      <button class="btn-add" @click="addEmployee">
        <i class="fas fa-plus"></i> Nuevo Empleado
      </button>
    </div>

    <!-- Tabla que muestra la lista de todos los empleados -->
    <div class="table-container">
      <table>
        <!-- Encabezados de la tabla -->
        <thead>
          <tr>
            <th>RUT</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Rol</th>
            <th>Especialidad</th> <!-- Solo se mostrará para médicos -->
            <th>Correo</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <!-- Cuerpo de la tabla con la lista de empleados -->
        <tbody>
          <!-- Iteración sobre cada empleado en la lista -->
          <tr v-for="employee in employees" :key="employee.rut">
            <!-- RUT formateado con puntos y guión -->
            <td>{{ formatRut(employee.rut) }}</td>
            <!-- Nombre del empleado -->
            <td>{{ employee.nombre }}</td>
            <!-- Apellido del empleado -->
            <td>{{ employee.apellido }}</td>
            <!-- Rol del empleado -->
            <td><span>{{ employee.rol.nombre }}</span></td>
            <!-- Especialidad (solo visible para médicos) -->
            <td>{{ employee.rol.nombre === 'Medico' ? (employee.especialidad ? employee.especialidad.nombre : '-') : '-' }}</td>
            <!-- Correo electrónico -->
            <td>{{employee.correo }}</td>
            <!-- Botones de acción (editar y eliminar) -->
            <td class="actions">
              <!-- Botón para editar empleado -->
              <button class="btn-edit" @click="editEmployee(employee)">
                <i class="fas fa-edit"></i>
              </button>
              <!-- Botón para eliminar empleado -->
              <button class="btn-delete" @click="deleteEmployee(employee.rut)">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal de registro/edición -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <UserFormModal
          :employee="editingEmployee"
          @submit="handleSubmit"
          @cancel="closeModal"
          @error="handleError"
        />
      </div>
    </div>

    <!-- Mensaje de error -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
//IMPORTACIONES
import { ref, onMounted } from 'vue';
import UserFormModal from './UserFormModal.vue';
import employeeService from '@/services/employee.service';


// DEFINICIÓN DEL COMPONENTE
export default {
  name: 'UserManagement',
  components: {
    UserFormModal
  },
  //DATOS REACTIVOS PARA EL COMPONENTE
  setup() {
    const employees = ref([]);
    const showModal = ref(false);
    const errorMessage = ref('');
    const editingEmployee = ref(null);

    // funcion para cargar todos los empleados del sistema
    const loadEmployees = async () => {
      try {
        // obtener los empleados del backend a través del servicio
        const response = await employeeService.getAllEmployees();
        // Log para verificar la estructura de datos recibida
        console.log('Respuesta del servidor:', response);
        
        // inicializar array que contendra todos los empleados procesados
        const employeeArray = [];
        
        // procesar medicos si existen en la respuesta
        if (response.medicos) {
          // mapear cada medico agregando su rol y manteniendo su especialidad
          employeeArray.push(...response.medicos.map(medico => ({
            ...medico, // Mantener todos los datos originales del médico
            rol: { nombre: 'Medico' }, // Agregar el rol específico
            especialidad: medico.especialidad // Mantener la especialidad existente
          })));
        }
        
        // procesar tecnicos si existen en la respuesta
        if (response.tecnicos) {
          // mapear cada tecnico agregando su rol
          employeeArray.push(...response.tecnicos.map(tecnico => ({
            ...tecnico, // Mantener todos los datos originales del técnico
            rol: { nombre: 'Técnico' } // Agregar el rol específico
          })));
        }
        
        // procesar recepcionistas si existen en la respuesta
        if (response.recepcionistas) {
          // mapear cada recepcionista agregando su rol
          employeeArray.push(...response.recepcionistas.map(recepcionista => ({
            ...recepcionista, // Mantener todos los datos originales del recepcionista
            rol: { nombre: 'Recepcionista' } // Agregar el rol específico
          })));
        }
        
        // procesar farmacéuticos si existen en la respuesta
        if (response.farmaceuticos) {
          // mapear cada farmacéutico agregando su rol
          employeeArray.push(...response.farmaceuticos.map(farmaceutico => ({
            ...farmaceutico, // Mantener todos los datos originales del farmacéutico
            rol: { nombre: 'Farmacéutico' } // Agregar el rol específico
          })));
        }
        
        // log para verificar el array final procesado
        console.log('Array de empleados procesado:', employeeArray);
        // actualizar el estado reactivo con los empleados procesados
        employees.value = employeeArray;
      } catch (error) {
        // manejar cualquier error que ocurra durante la carga
        console.error('Error al cargar empleados:', error);
        // mostrar mensaje de error al usuario
        errorMessage.value = 'Error al cargar la lista de empleados';
      }
    };

    


    const handleSubmit = async (response) => {
      try {
        // response es la respuesta del backend emitida por el modal de registro/edición
        if (response.success) {
          showModal.value = false;
          editingEmployee.value = null;
          await loadEmployees();
          errorMessage.value = '';
        } else {
          errorMessage.value = response.message || 'Error al registrar/actualizar el empleado';
        } 
      } catch (error) {
        console.error('Error al registrar empleado:', error);
        errorMessage.value = error.response?.data?.message || 'Error al registrar/actualizar el empleado';
      }
    };
    //funcio para manejar el error del formulario
    const handleError = (message) => {
      errorMessage.value = message;
    };
    // Función para editar un empleado
    const deleteEmployee = async (rut) => {
      // Confirmación antes de eliminar
      if (confirm('¿Está seguro de eliminar este empleado?')) {
        //try catch para capturar errores
        try {
          await employeeService.deleteEmployee(rut);// Llamada al servicio para eliminar el empleado
          await loadEmployees();// Recargar empleados
          errorMessage.value = '';
          // atrapado de errores
        } catch (error) {
          console.error('Error al eliminar empleado:', error);//debbug
          errorMessage.value = 'Error al eliminar el empleado';
        }
      }
    };


    // Función para formatear el RUT
    const formatRut = (rut) => {
      if (!rut) return '';
      const rutLimpio = rut.replace(/[^0-9kK]/g, '');
      if (rutLimpio.length > 1) {
        const dv = rutLimpio.slice(-1);
        const rutBody = rutLimpio.slice(0, -1);
        return rutBody.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '-' + dv;
      }
      return rut;
    };

    const editEmployee = (employee) => {
      editingEmployee.value = { ...employee };
      showModal.value = true;
    };

    const closeModal = () => {
      showModal.value = false;
      editingEmployee.value = null;
    };

    const addEmployee = () => {
      editingEmployee.value = null;
      showModal.value = true;
    };



    onMounted(() => {
      loadEmployees();
    });
    // Retornar los datos y funciones reactivas
    return {
      employees,
      showModal,
      errorMessage,
      handleSubmit,
      handleError,
      deleteEmployee,
      formatRut,
      editEmployee,
      closeModal,
      addEmployee,
      editingEmployee
    };
  }
};
</script>

<style scoped>
.user-management {
  padding: 1rem;
}

.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  background-color: rgb(241, 241, 241);
  padding: 10px;
  border-radius: 8px;
}


.btn-add {
  background-color: #0875C1;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-add:hover {
  background-color: #C51A6F;
}

.table-container {
  overflow-x: auto;
  border-radius: 12px;
  background-color: #ededed;
}
.tr {
  border-color: black;
  border-top: black
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  
}

th, td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f5f5f5;
  font-weight: bold;
  border-color: black;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.btn-edit, .btn-delete {
  padding: 0.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-edit {
  background-color: #0875C1;
  color: white;
}

.btn-delete {
  background-color: #dc3545;
  color: white;
}

.btn-edit:hover {
  background-color: #065a94;
}

.btn-delete:hover {
  background-color: #c82333;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  max-width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  width: 70%;
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 1rem;
  border-radius: 4px;
  margin-top: 1rem;
}
</style>