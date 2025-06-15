<template>
  <div class="user-management">
    <div class="header-actions">
      <h2>Gestión de Empleados</h2>
      <button class="btn-add" @click="showModal = true">
        <i class="fas fa-plus"></i> Nuevo Empleado
      </button>
    </div>

    <!-- Tabla de empleados -->
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>RUT</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Rol</th>
            <th>Correo</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="employee in employees" :key="employee.rut">
            <td>{{ formatRut(employee.rut) }}</td>
            <td>{{ employee.nombre }}</td>
            <td>{{ employee.apellido }}</td>
            <td>{{ employee.rol?.nombre }}</td>
            <td>{{ employee.correo }}</td>
            <td class="actions">
              <button class="btn-edit" @click="editEmployee(employee)">
                <i class="fas fa-edit"></i>
              </button>
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
          @submit="handleSubmit"
          @cancel="showModal = false"
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
import { ref, onMounted } from 'vue';
import UserFormModal from './UserFormModal.vue';
import employeeService from '@/services/employee.service';

export default {
  name: 'UserManagement',
  components: {
    UserFormModal
  },
  setup() {
    const employees = ref([]);
    const showModal = ref(false);
    const errorMessage = ref('');

    const loadEmployees = async () => {
      try {
        const response = await employeeService.getAllEmployees();
        // Convertir el objeto de empleados en un array
        const employeeArray = [];
        if (response.medicos) employeeArray.push(...response.medicos);
        if (response.tecnicos) employeeArray.push(...response.tecnicos);
        if (response.recepcionistas) employeeArray.push(...response.recepcionistas);
        if (response.farmaceuticos) employeeArray.push(...response.farmaceuticos);
        employees.value = employeeArray;
      } catch (error) {
        console.error('Error al cargar empleados:', error);
        errorMessage.value = 'Error al cargar la lista de empleados';
      }
    };    const handleSubmit = async (employeeData) => {
      try {
        console.log('Datos enviados desde UserManagement:', JSON.stringify(employeeData, null, 2));
        const response = await employeeService.registerEmployee(employeeData);
        if (response.success) {
          showModal.value = false;
          await loadEmployees();
          errorMessage.value = '';
        } else {
          errorMessage.value = response.message || 'Error al registrar el empleado';
        }
      } catch (error) {
        console.error('Error al registrar empleado:', error);
        errorMessage.value = error.response?.data?.message || 'Error al registrar el empleado';
      }
    };

    const handleError = (message) => {
      errorMessage.value = message;
    };

    const deleteEmployee = async (rut) => {
      if (confirm('¿Está seguro de eliminar este empleado?')) {
        try {
          await employeeService.deleteEmployee(rut);
          await loadEmployees();
          errorMessage.value = '';
        } catch (error) {
          console.error('Error al eliminar empleado:', error);
          errorMessage.value = 'Error al eliminar el empleado';
        }
      }
    };

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

    onMounted(() => {
      loadEmployees();
    });

    return {
      employees,
      showModal,
      errorMessage,
      handleSubmit,
      handleError,
      deleteEmployee,
      formatRut
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
  background-color: #065a94;
}

.table-container {
  overflow-x: auto;
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
  border-radius: 8px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 1rem;
  border-radius: 4px;
  margin-top: 1rem;
}
</style>