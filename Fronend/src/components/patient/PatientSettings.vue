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
      <h2>Configuración del Paciente</h2>
      <form class="profile-form" @submit.prevent="saveSettings">
        <div class="form-group">
          <label>Numero de telefono</label>
          <input type="text"
              v-model="user.telefono"
              @input="user.telefono = user.telefono.replace(/[^0-9]/g, '')"
              placeholder="+56 9 XXXX XXXX" />
        </div>
        <div class="form-group">
          <label>Correo electrónico</label>
          <input type="email" v-model="user.correo" />
        </div>
        <div class="form-group">
          <label>Direccion</label>
          <input type="text" v-model="user.direccion" />
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
import patientService from '@/services/patientService';


// Obtener usuario desde localStorage y crear objeto reactivo
const userRaw = JSON.parse(localStorage.getItem('user')) || {};
const usuario = userRaw.usuario || {};

console.log('userRaw:', userRaw);

const user = reactive({
  nombrePa: usuario.nombrePa || '',
  apellidoPa: usuario.apellidoPa || '',
  correo: usuario.correo || '',
  password: '',
  direccion: usuario.direccion || '',
  telefono: usuario.telefono || '',
  id_paciente: usuario.id_paciente || '',
  rut: usuario.rut || '',
  fechanacimiento: usuario.fechanacimiento || '',
  sexo: usuario.sexo || '',
  rol: usuario.rol || 'paciente'
});

const paciente = ref(null);


onMounted(async () => {
  if (user.id_paciente) {
    paciente.value = await patientService.getPatientByRut(user.id_paciente);
    // Si hay datos faltantes, los completas en user
    if (paciente.value) {
      user.nombrePa = paciente.value.nombrePa || user.nombrePa;
      user.apellidoPa = paciente.value.apellidoPa || user.apellidoPa;
      user.rut = paciente.value.rut || user.rut;
      user.fechanacimiento = paciente.value.fechanacimiento || user.fechanacimiento;
      user.sexo = paciente.value.sexo || user.sexo;
      user.rol = paciente.value.rol || user.rol;
      user.telefono = paciente.value.telefono || user.telefono;
      user.password = paciente.value.password || user.password;
    }
  }
});

console.log('userRaw:', userRaw);
console.log('userRaw.nombre:', userRaw.usuario?.nombrePa);
console.log('userRaw.usuario?.apellido:', userRaw.usuario?.apellidoPa);
console.log('userRaw.usuario?.correo:', userRaw.usuario?.correo);
console.log('userRaw.direccion:', userRaw.usuario?.direccion);
console.log('userRaw.telefono:', userRaw.usuario?.telefono);
console.log('userRaw.id_paciente:', userRaw.usuario?.id_paciente);
console.log('userRaw.rut:', userRaw.usuario?.rut || userRaw.rut);
console.log('userRaw.usuario?.fechaNacimiento:', userRaw.usuario?.fechanacimiento);
console.log('userRaw.usuario?.sexo:', userRaw.usuario?.sexo);
console.log('userRaw.usuario?.rol:', userRaw.usuario?.rol);

// Mostrar cómo queda el objeto user
console.log('user:', toRaw(user));

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

  console.log('paciente:', paciente);

  const updatedUser = {
    nombrePa: user.nombrePa,
    apellidoPa: user.apellidoPa,
    correo: user.correo,
    password: user.password,
    direccion: user.direccion,
    telefono: user.telefono,
    rut: user.rut,
    rol: user.rol,
    fechanacimiento: user.fechanacimiento,
    sexo: user.sexo
  };

  // DEPURACIÓN: Mostrar lo que se enviará
  console.log('user:', user);
  console.log('updatedUser:', updatedUser);

  patientService.updatePatient(user.id_paciente, updatedUser)
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