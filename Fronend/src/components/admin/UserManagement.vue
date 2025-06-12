<template>
  <div class="user-management">
    <div class="header-section">
      <h2>Gestión de Usuarios</h2>
      <button @click="showUserForm = true" class="btn-add-user">
        <i class="fas fa-plus"></i> Agregar Usuario
      </button>
    </div>

    <div class="search-section">
      <input 
        v-model="searchQuery" 
        type="text" 
        placeholder="Buscar usuarios..." 
        class="search-input"
      >
      <select v-model="roleFilter" class="role-filter">
        <option value="">Todos los roles</option>
        <option value="doctor">Médicos</option>
        <option value="technician">Técnicos</option>
        <option value="receptionist">Recepcionistas</option>
        <option value="pharmacy">Farmacia</option>
      </select>
    </div>

    <div class="users-table">
      <table>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>RUT</th>
            <th>Rol</th>
            <th>Email</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.id">
            <td>{{ user.fullName }}</td>
            <td>{{ user.rut }}</td>
            <td>{{ formatRole(user.role) }}</td>
            <td>{{ user.email }}</td>
            <td>
              <span :class="['status-badge', user.active ? 'active' : 'inactive']">
                {{ user.active ? 'Activo' : 'Inactivo' }}
              </span>
            </td>
            <td class="actions">
              <button @click="editUser(user)" class="btn-edit">
                <i class="fas fa-edit"></i>
              </button>
              <button 
                @click="toggleUserStatus(user)" 
                :class="['btn-status', user.active ? 'deactivate' : 'activate']"
              >
                <i :class="['fas', user.active ? 'fa-user-slash' : 'fa-user-check']"></i>
              </button>
              <button @click="confirmDelete(user)" class="btn-delete">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal para agregar/editar usuario -->
    <UserFormModal 
      v-if="showUserForm" 
      :user="selectedUser"
      @close="closeUserForm"
      @save="saveUser"
    />
  </div>
</template>

<script>
import UserFormModal from './UserFormModal.vue';

export default {
  name: 'UserManagement',
  components: {
    UserFormModal
  },
  data() {
    return {
      users: [], // Esto vendría del backend
      searchQuery: '',
      roleFilter: '',
      showUserForm: false,
      selectedUser: null
    }
  },
  computed: {
    filteredUsers() {
      let filtered = this.users;
      
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        filtered = filtered.filter(user => 
          user.fullName.toLowerCase().includes(query) || 
          user.rut.includes(query) ||
          user.email.toLowerCase().includes(query)
        );
      }
      
      if (this.roleFilter) {
        filtered = filtered.filter(user => user.role === this.roleFilter);
      }
      
      return filtered;
    }
  },
  methods: {
    formatRole(role) {
      const roles = {
        'doctor': 'Médico',
        'technician': 'Técnico',
        'receptionist': 'Recepcionista',
        'pharmacy': 'Farmacia'
      };
      return roles[role] || role;
    },
    editUser(user) {
      this.selectedUser = { ...user };
      this.showUserForm = true;
    },
    closeUserForm() {
      this.showUserForm = false;
      this.selectedUser = null;
    },
    async saveUser(userData) {
      try {
        if (userData.id) {
          // Actualizar usuario existente
          await this.$store.dispatch('users/updateUser', userData);
        } else {
          // Crear nuevo usuario
          await this.$store.dispatch('users/createUser', userData);
        }
        this.closeUserForm();
        this.fetchUsers();
      } catch (error) {
        console.error('Error al guardar usuario:', error);
      }
    },
    async toggleUserStatus(user) {
      try {
        await this.$store.dispatch('users/toggleUserStatus', {
          id: user.id,
          active: !user.active
        });
        this.fetchUsers();
      } catch (error) {
        console.error('Error al cambiar estado:', error);
      }
    },
    confirmDelete(user) {
      if (confirm(`¿Estás seguro de eliminar a ${user.fullName}?`)) {
        this.deleteUser(user.id);
      }
    },
    async deleteUser(userId) {
      try {
        await this.$store.dispatch('users/deleteUser', userId);
        this.fetchUsers();
      } catch (error) {
        console.error('Error al eliminar usuario:', error);
      }
    },
    async fetchUsers() {
      try {
        await this.$store.dispatch('users/fetchUsers');
        this.users = this.$store.state.users.users;
      } catch (error) {
        console.error('Error al obtener usuarios:', error);
      }
    }
  },
  created() {
    this.fetchUsers();
  }
}
</script>

<style scoped>
.user-management {
  padding: 1rem;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.btn-add-user {
  background-color: #C51A6F;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

.search-section {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.search-input, .role-filter {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.search-input {
  flex-grow: 1;
}

.role-filter {
  width: 200px;
}

.users-table {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f5f5f5;
  font-weight: 600;
    color: #0875C1;
}

.status-badge {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-badge.active {
  background-color: #e6f7e6;
  color: #2e7d32;
}

.status-badge.inactive {
  background-color: #ffebee;
  color: #c62828;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.btn-edit, .btn-status, .btn-delete {
  border: none;
  padding: 0.5rem;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
}

.btn-edit {
  background-color: #e3f2fd;
  color: #1565c0;
}

.btn-status.activate {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.btn-status.deactivate {
  background-color: #fff3e0;
  color: #ef6c00;
}

.btn-delete {
  background-color: #ffebee;
  color: #c62828;
}
</style>