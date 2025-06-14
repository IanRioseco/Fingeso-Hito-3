<template>
  <div class="admin-dashboard">
    <header class="admin-header">
      <h1>Panel de Administración de {{ adminName }}</h1>
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
import UserManagement from '@/components/admin/UserManagement.vue';
import ReportsGenerator from '@/components/admin/ReportsGenerator.vue';
import SettingsPanel from '@/components/admin/SettingsPanel.vue';
import { authService } from '@/services/auth.service';

export default {
  name: 'AdminView',
  components: {
    UserManagement,
    ReportsGenerator,
    SettingsPanel
  },
  data() {
    return {
      activeTab: 'users',
      adminName: ''
    }
  },
  created() {
    const userData = authService.getCurrentUser();
    if (userData && userData.usuario) {
      this.adminName = `${userData.usuario.nombre} ${userData.usuario.apellido}`;
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
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
}
</style>