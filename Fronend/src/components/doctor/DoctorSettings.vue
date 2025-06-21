<template>
  <div class="doctor-settings-profile">
    <div class="profile-left">
      <div class="profile-photo">
        <img :src="profilePhotoUrl" alt="Foto de perfil" />
        <input type="file" @change="onPhotoChange" id="photoInput" style="display:none" />
        <button class="settings-btn" @click="triggerPhotoInput">Cambiar foto</button>
      </div>
    </div>
    <div class="profile-right">
      <h2>Configuración del Doctor</h2>
      <form class="profile-form" @submit.prevent="saveSettings">
        <div class="form-group">
          <label>Nombre</label>
          <input type="text"
              v-model="user.nombre"
              @input="user.nombre = user.nombre.replace(/[0-9]/g, '')" />
        </div>
        <div class="form-group">
          <label>Apellido</label>
          <input type="text"
            v-model="user.apellido"
            @input="user.apellido = user.apellido.replace(/[0-9]/g, '')" />     
        </div>
        <div class="form-group">
          <label>Correo electrónico</label>
          <input type="email" v-model="user.email" />
        </div>
        <div class="form-group">
          <label>Nueva contraseña</label>
          <input type="password" v-model="user.nuevaPassword" placeholder="Dejar en blanco para no cambiar" />
        </div>
        <div class="form-actions">
          <button class="settings-btn" type="submit">Guardar cambios</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

// Obtener usuario desde localStorage y crear objeto reactivo
const userRaw = JSON.parse(localStorage.getItem('user')) || {};
const user = reactive({
  nombre: userRaw.nombre || userRaw.usuario?.nombre || '',
  apellido: userRaw.apellido || userRaw.usuario?.apellido || '',
  email: userRaw.email || userRaw.usuario?.email || '',
  nuevaPassword: ''
});

const profilePhotoUrl = ref(
  userRaw.fotoPerfil ||
  `https://ui-avatars.com/api/?name=${encodeURIComponent(user.nombre + ' ' + user.apellido)}&background=0875C1&color=fff`
);

function triggerPhotoInput() {
  document.getElementById('photoInput').click();
}

function onPhotoChange(e) {
  const file = e.target.files[0];
  if (file) {
    // Solo para previsualización local, no guarda en backend
    const reader = new FileReader();
    reader.onload = (ev) => {
      profilePhotoUrl.value = ev.target.result;
    };
    reader.readAsDataURL(file);
  }
}

function saveSettings() {
  // Aquí iría la lógica para guardar los cambios en el backend
  // Puedes enviar user.nombre, user.apellido, user.email, user.nuevaPassword y profilePhotoUrl.value
  alert('Cambios guardados (simulado)');
}
</script>

<style scoped>
.doctor-settings-profile {
  display: flex;
  max-width: 800px;
  margin: 2rem auto;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.10);
  padding: 2rem;
  gap: 2.5rem;
}
.profile-left {
  flex: 0 0 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-right: 1px solid #eee;
  padding-right: 2rem;
}
.profile-photo img {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 1rem;
  border: 3px solid #C51A6F;
}
.profile-right {
  flex: 1;
  padding-left: 2rem;
}
.profile-form {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}
.form-group {
  display: flex;
  flex-direction: column;
}
.form-group label {
  font-weight: bold;
  margin-bottom: 0.3rem;
  color: #0875C1;
}
.form-group input {
  padding: 0.5rem;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-size: 1rem;
}
.form-actions {
  margin-top: 1.5rem;
  display: flex;
  justify-content: flex-end;
}
.settings-btn {
  background-color: #C51A6F;
  color: white;
  border: none;
  padding: 0.5rem 1.2rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.2s;
}
.settings-btn:hover {
  background-color: #0875C1;
}
@media (max-width: 800px) {
  .doctor-settings-profile {
    flex-direction: column;
    padding: 1rem;
    gap: 1.5rem;
  }
  .profile-left {
    border-right: none;
    border-bottom: 1px solid #eee;
    padding-right: 0;
    padding-bottom: 1.5rem;
  }
  .profile-right {
    padding-left: 0;
  }
}
</style>