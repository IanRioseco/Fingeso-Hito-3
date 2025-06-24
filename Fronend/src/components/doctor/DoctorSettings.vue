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
          <label>Correo electrónico</label>
          <input type="email" v-model="user.email" />
        </div>
        <div class="form-group">
          <label>Teléfono</label>
          <input type="text" v-model="user.telefono" @input="user.telefono = user.telefono.replace(/[^0-9]/g, '')" placeholder="+56 9 XXXX XXXX" />
        </div>
        <div class="form-group">
          <label>Nueva contraseña</label>
          <input type="password" v-model="user.password" placeholder="Dejar en blanco para no cambiar" />
        </div>
        <div class="form-actions">
          <button class="settings-btn" type="submit">Guardar cambios</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, toRaw } from 'vue'
import medicoService from '@/services/medicoService';

// Obtener usuario desde localStorage y crear objeto reactivo
const userRaw = JSON.parse(localStorage.getItem('user')) || {};
console.log("userRaw desde localStorage:", userRaw);

const usuario = userRaw.usuario || {};
console.log("usuario extraído de userRaw:", usuario);

const user = reactive({
  nombre: usuario.nombre || '',
  apellido: usuario.apellido || '',
  correo: usuario.correo || '',
  password: '',
  telefono: usuario.telefono || '',
  idmedico: usuario.idmedico || '',
  rut: usuario.rut || '',
  rol: usuario.rol,
  especialidad: usuario.especialidad
});
console.log("user reactivo inicial:", toRaw(user));

const medico = ref(null);

onMounted(async () => {
  console.log("ID de médico en user:", user.idmedico);

  if (user.idmedico) {
    try {
      const respuesta = await medicoService.obtenerPorId(user.idmedico);
      medico.value = respuesta.data
      console.log("Respuesta de medicoService.obtenerPorId:", toRaw(medico.value));

      if (medico.value) {
        // Antes de actualizar user
        console.log("user antes de completar con datos de médico:", toRaw(user));

        user.nombre = medico.value.nombre || user.nombre;
        user.apellido = medico.value.apellido || user.apellido;
        user.rut = medico.value.rut || user.rut;
        user.rol = medico.value.rol
        user.telefono = medico.value.telefono || user.telefono;
        user.password = medico.value.password || user.password;
        user.especialidad = medico.value.especialidad || user.especialidad;

        // Después de actualizar user
        console.log("user después de completar con datos de médico:", toRaw(user));
      } else {
        console.warn("No se encontró médico con ese ID.");
      }
    } catch (error) {
      console.error("Error al obtener médico por ID:", error);
    }
  } else {
    console.warn("No hay idmedico en user.");
  }
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
    const reader = new FileReader();
    reader.onload = (ev) => {
      profilePhotoUrl.value = ev.target.result;
    };
    reader.readAsDataURL(file);
  }
}

function saveSettings() {
  const updatedUser = {
    nombre: user.nombre,
    apellido: user.apellido,
    correo: user.correo,
    telefono: user.telefono,
    password: user.password || '',
    rut: user.rut,
    rol: user.rol,
    especialidad: user.especialidad
    // agrega otros campos si tu backend los requiere
  };

  console.log('user antes de guardar:', toRaw(user));
  console.log('updatedUser que se enviará:', updatedUser);

  medicoService.actualizar(user.idmedico, updatedUser)
    .then(response => {
      console.log('Configuración actualizada:', response);
      localStorage.setItem('user', JSON.stringify({ ...userRaw, usuario: { ...user } }));
      alert('Cambios guardados');
    })
    .catch(error => {
      console.error('Error al guardar cambios:', error);
      alert('Error al guardar cambios');
    });
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