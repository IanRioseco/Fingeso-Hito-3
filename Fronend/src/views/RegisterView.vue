<template>
  <div class="register-container">
    <div class="register-box">
      <img src="@/assets/img/logoUH.png" alt="RedSalud Logo" class="logo">
      <h2>Registro de Paciente</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="rut">RUT</label>
          <input 
            id="rut" 
            v-model="userData.rut" 
            type="text" 
            placeholder="12345678-9" 
            required
            @input="formatRut"
          >
        </div>
        <div class="form-group">
          <label for="nombrePa">Nombre</label>
          <input 
            id="nombrePa" 
            v-model="userData.nombrePa" 
            type="text" 
            required
          >
        </div>
        <div class="form-group">
          <label for="apellidoPa">Apellido</label>
          <input 
            id="apellidoPa" 
            v-model="userData.apellidoPa" 
            type="text" 
            required
          >
        </div>
        <div class="form-group">
          <label for="correo">Correo Electrónico</label>
          <input 
            id="correo" 
            v-model="userData.correo" 
            type="email" 
            required
          >
        </div>
        <div class="form-group">
          <label for="telefono">Teléfono</label>
          <input 
            id="telefono" 
            v-model="userData.telefono" 
            type="tel" 
            required
          >
        </div>
        <div class="form-group">
          <label for="direccion">Dirección</label>
          <input 
            id="direccion" 
            v-model="userData.direccion" 
            type="text" 
            required
          >
        </div>
        <div class="form-group">
          <label for="password">Contraseña</label>
          <input 
            id="password" 
            v-model="userData.password" 
            type="password" 
            required
          >
        </div>
        <button 
          type="submit" 
          class="btn-register" 
          :disabled="isLoading"
        >
          {{ isLoading ? 'Registrando...' : 'Registrarse' }}
        </button>
        <p v-if="error" class="error-message">{{ error }}</p>
      </form>
      <p class="login-link">
        ¿Ya tienes cuenta? 
        <router-link to="/login">Inicia sesión aquí</router-link>
      </p>
    </div>
  </div>
</template>

<script>
import { authService } from '@/services/auth.service';

export default {
  data() {
    return {
      userData: {
        rut: '',
        nombrePa: '',
        apellidoPa: '',
        correo: '',
        telefono: '',
        direccion: '',
        password: ''
      },
      error: '',
      isLoading: false
    }
  },
  methods: {
    formatRut() {
      let value = this.userData.rut.replace(/[^0-9kK]/g, '');
      
      if (value.length > 1) {
        const dv = value.slice(-1);
        const rutBody = value.slice(0, -1);
        
        this.userData.rut = rutBody.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '-' + dv;
      } else {
        this.userData.rut = value;
      }
    },
    async handleRegister() {
      try {
        this.isLoading = true;
        this.error = '';

        const rutLimpio = this.userData.rut.replace(/[.-]/g, '');
        const userData = {
          ...this.userData,
          rut: rutLimpio
        };

        console.log('Intentando registro con:', userData);
        
        const response = await authService.register(userData);
        
        console.log('Respuesta del servidor:', response);

        if (response && response.success) {
          // Mostrar mensaje de éxito
          alert('Registro exitoso. Por favor, inicia sesión.');
          // Redirigir al login después del registro exitoso
          this.$router.push({ name: 'Login' });
        } else {
          this.error = 'Error durante el registro: No se recibió una respuesta válida del servidor';
        }
      } catch (error) {
        console.error('Error completo:', error);
        if (error.response) {
          console.log('Error de respuesta:', error.response.data);
          switch (error.response.status) {
            case 400:
              this.error = 'Datos de registro inválidos. Por favor, verifica todos los campos.';
              break;
            case 409:
              this.error = 'El RUT o correo ya está registrado en el sistema.';
              break;
            case 500:
              this.error = 'Error en el servidor. Por favor, intenta más tarde.';
              break;
            default:
              this.error = error.response.data?.message || 'Error durante el registro';
          }
        } else if (error.request) {
          this.error = 'No se pudo conectar con el servidor. Por favor, verifica tu conexión.';
        } else {
          this.error = 'Error durante el registro: ' + error.message;
        }
      } finally {
        this.isLoading = false;
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.register-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

.logo {
  width: 180px;
  margin-bottom: 1.5rem;
  margin-top: 1.5rem;
  display: block;
  margin-left: auto;
  margin-right: auto;
}

h2 {
  color: #009999;
  margin-bottom: 1.5rem;
  text-align: center;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #747473;
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.btn-register {
  width: 100%;
  padding: 0.75rem;
  background-color: #C51A6F;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  margin-top: 1rem;
}

.btn-register:hover {
  background-color: #099;
  color: #fff;
}

.error-message {
  color: #B92E25;
  margin-top: 1rem;
  text-align: center;
}

.login-link {
  text-align: center;
  margin-top: 1.5rem;
  color: #747473;
}

.login-link a {
  color: #009999;
  text-decoration: none;
}
</style>