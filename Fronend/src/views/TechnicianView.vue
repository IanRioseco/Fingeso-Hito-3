<template>
  <div class="technician-dashboard">
    <header class="technician-header">
      <h1>Panel Técnico</h1>
      <button class="logout-btn" @click="logout">Cerrar sesión</button>
    </header>
    <!-- ...contenido de técnico... -->
  </div>
</template>

<script>
import { authService } from '@/services/auth.service';
export default {
  name: 'TechnicianView',
  methods: {
    logout() {
      authService.logout();
      this.$router.push('/');
    }
  }
}
</script>

<style scoped>
.logout-btn {
  background: #C51A6F;
  color: #fff;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  margin-left: 1rem;
}
</style>