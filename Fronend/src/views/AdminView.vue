<template>
  <div class="admin-dashboard">
    <header class="admin-header">
      <div class="header-logo-title">
        <img src="@/assets/img/logoUH.png" alt="RedSalud Logo" class="logo" />
        <h1>Panel de Administración de {{ adminName }}</h1>
      </div>
      <nav class="admin-nav">
        <button @click="activeTab = 'users'" :class="{ active: activeTab === 'users' }">
          Gestión de Usuarios
        </button>
        <button @click="activeTab = 'reports'" :class="{ active: activeTab === 'reports' }">
          Reportes
        </button>
        <button @click="activeTab = 'settings'" :class="{ active: activeTab === 'settings' }">
          Configuración
        </button>
        <button class="logout-btn" @click="logout">Cerrar sesión</button>
      </nav>
    </header>

    <main class="admin-content">
      <UserManagement v-if="activeTab === 'users'" />
      <ReportsGenerator v-if="activeTab === 'reports'" />
      <SettingsPanel v-if="activeTab === 'settings'" />
    </main>
  </div>
</template>

<script>
//IMPORTACIONES
import UserManagement from '@/components/admin/UserManagement.vue';
import ReportsGenerator from '@/components/admin/ReportsGenerator.vue';
import SettingsPanel from '@/components/admin/SettingsPanel.vue';
import { authService } from '@/services/auth.service';

//EXPORTACIONES
//componente de vista de administrador
export default {
  name: 'AdminView',
  components: {
    UserManagement,
    ReportsGenerator,
    SettingsPanel
  },
  //definición de datos reactivos
  data() {
    return {
      activeTab: 'users',
      adminName: ''
    }
  },
  //carga de datos al crear el componente
  created() {
    const userData = authService.getCurrentUser();//llamada al servicio de autenticación para obtener el usuario actual
    if (userData && userData.usuario) {
      this.adminName = `${userData.usuario.nombre} ${userData.usuario.apellido}`;//construcción del nombre del administrador
    }
  },
  methods: {
    logout() {
      authService.logout("auth/logout");
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
  padding: 2rem;
  width: 100%;
  max-width: 100%;
  margin: 0 auto;
}

.admin-header {
  margin-bottom: 2rem;
  border-bottom: 1px solid #ddd;
  padding-bottom: 1rem;
}

.admin-header h1 {
  color: #0875C1;
  margin-bottom: 1rem;
}

.admin-nav {
  display: flex;
  gap: 1rem;
}

.admin-nav button {
  padding: 0.75rem 1.5rem;
  background-color: #f5f5f5;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  color: #747473;
}

.admin-nav button.active {
  background-color: #C51A6F;
  color: white;
}

.admin-content {
  background-color: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.25);
}

.logout-btn {
  padding: 0.75rem 1.5rem;
  background-color: #C51A6F;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  color: white;
}

.logout-btn:hover {
  background-color: #0875C1;
}

.admin-nav button:hover {
  background-color: #C51A6F;
  color: white;
}

.admin-nav button:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(13, 110, 253, 0.5);
}

.admin-nav button:active {
  background-color: #0875C1;
  color: white;
}

.admin-nav button:disabled {
  background-color: #ddd;
  color: #aaa;
  cursor: not-allowed;
}

.admin-nav button:disabled:hover {
  background-color: #ddd;
  color: #aaa;
}

.admin-header {
  margin-bottom: 2rem;
  border-bottom: 1px solid #ddd;
  padding-bottom: 1rem;
}
.header-logo-title {
  display: flex;
  align-items: center;
  gap: 1.2rem;
  margin-bottom: 1rem;
}
.logo {
  height: 48px;
  width: auto;
  display: block;
}
.admin-header h1 {
  color: #0875C1;
  margin: 0;
  font-size: 1.7rem;
}
</style>