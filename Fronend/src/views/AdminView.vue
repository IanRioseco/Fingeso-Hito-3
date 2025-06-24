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
  background: var(--color-fondo);
  border-radius: 16px;
  box-shadow: 0 2px 16px var(--color-shadow);
}

.admin-header {
  margin-bottom: 2rem;
  border-bottom: 1px solid var(--color-borde);
  padding-bottom: 1rem;
  background: var(--color-fondo);
  border-radius: 12px 12px 0 0;
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
  color: var(--color-principal);
  margin: 0;
  font-size: 1.7rem;
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
  color: var(--color-texto);
  transition: background 0.2s, color 0.2s;
}

.admin-nav button.active,
.admin-nav button:hover {
  background-color: var(--color-secundario);
  color: white;
}

.admin-nav button:focus {
  outline: none;
  box-shadow: 0 0 0 2px var(--color-secundario);
}

.admin-nav button:active {
  background-color: var(--color-principal);
  color: white;
}

.admin-nav button:disabled,
.admin-nav button:disabled:hover {
  background-color: #ddd;
  color: #aaa;
  cursor: not-allowed;
}

.logout-btn {
  padding: 0.75rem 1.5rem;
  background-color: var(--color-secundario);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  color: white;
  font-weight: bold;
  transition: background 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 8px var(--color-shadow);
}

.logout-btn:hover {
  background-color: var(--color-principal);
  box-shadow: 0 4px 16px var(--color-shadow);
}

.admin-content {
  background-color: var(--color-fondo);
  border-radius: 0 0 12px 12px;
  padding: 2rem;
  box-shadow: 0 0 10px var(--color-shadow);
}
</style>