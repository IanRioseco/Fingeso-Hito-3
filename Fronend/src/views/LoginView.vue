<template>
  <div class="login-container">
    <div class="login-box">
      <img src="@/assets/img/logoUH.png" alt="RedSalud Logo" class="logo">
      <h2>Iniciar Sesión</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="rut">RUT</label>
          <input 
            id="rut" 
            v-model="credentials.rut" 
            type="text" 
            placeholder="12345678-9" 
            required
            @input="formatRut"
          >
        </div>
        <div class="form-group">
          <label for="password">Contraseña</label>
          <input 
            id="password" 
            v-model="credentials.password" 
            type="password" 
            required
          >
        </div>
        <div class="form-group">
          <label for="role">Rol</label>
          <select id="role" v-model="credentials.role" required>
            <option value="">Seleccione un rol</option>
            <option value="admin">Administrador</option>
            <option value="doctor">Médico</option>
            <option value="patient">Paciente</option>
            <option value="technician">Técnico</option>
            <option value="receptionist">Recepcionista</option>
            <option value="pharmacy">Farmacia</option>
          </select>
        </div>
        <button 
          type="submit" 
          class="btn-login" 
          :disabled="isLoading"
        >
          {{ isLoading ? 'Iniciando sesión...' : 'Ingresar' }}
        </button>
        <p v-if="error" class="error-message">{{ error }}</p>
      </form>
      <p class="register-link">
        ¿Eres paciente y no tienes cuenta? 
        <router-link to="/register">Regístrate aquí</router-link>
      </p>
    </div>
  </div>
</template>

<script>
import { authService } from '@/services/auth.service';

/* Formato RUT chileno */
function formatRut(rut) {
  rut = rut.replace(/[^0-9kK]/g, '');
  if (rut.length > 1) {
    const dv = rut.slice(-1);
    const rutBody = rut.slice(0, -1);

    rut = rutBody.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '-' + dv;
  }
  return rut;
}

export default {
  data() {
    return {
      credentials: {
        rut: '',
        password: '',
        role: ''
      },
      error: '',
      isLoading: false
    }
  },
  methods: {
    formatRut() {
      // Eliminar cualquier carácter que no sea número o 'k'
      let value = this.credentials.rut.replace(/[^0-9kK]/g, '');
      
      if (value.length > 1) {
        // Separar el dígito verificador
        const dv = value.slice(-1);
        const rutBody = value.slice(0, -1);
        
        // Formatear con puntos y guión
        this.credentials.rut = rutBody.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '-' + dv;
      } else {
        this.credentials.rut = value;
      }
    },
    async handleLogin() {
      try {
        this.isLoading = true;
        this.error = '';

        const rutLimpio = this.credentials.rut.replace(/[.-]/g, '');

        console.log('Intentando login con:', {
          rut: rutLimpio,
          password: this.credentials.password,
          role: this.credentials.role
        });
        
        const response = await authService.login(
          rutLimpio,
          this.credentials.password,
          this.credentials.role
        );
        
        console.log('Respuesta del servidor:', response);

        // Si llegamos aquí, el login fue exitoso
        if (response) {
          console.log('Login exitoso, rol:', this.credentials.role);
          
          try {
            switch(this.credentials.role) {
              case 'admin':
                console.log('Redirigiendo a Admin');
                await this.$router.push({ name: 'Admin' });
                break;
              case 'doctor':
                await this.$router.push({ name: 'Doctor' });
                break;
              case 'patient':
                await this.$router.push({ name: 'Patient' });
                break;
              case 'technician':
                await this.$router.push({ name: 'Technician' });
                break;
              case 'pharmacy':
                await this.$router.push({ name: 'Pharmacy' });
                break;
              case 'receptionist':
                await this.$router.push({ name: 'Receptionist' });
                break;
              default:
                console.log('Rol no válido:', this.credentials.role);
                this.error = 'Rol no válido';
            }
          } catch (routerError) {
            console.error('Error en la redirección:', routerError);
            this.error = 'Error al redireccionar: ' + routerError.message;
          }
        }
      } catch (error) {
        console.error('Error completo:', error);
        if (error.response) {
          console.log('Error de respuesta:', error.response.data);
          switch (error.response.status) {
            case 401:
              this.error = 'Contraseña incorrecta';
              break;
            case 403:
              this.error = 'Rol incorrecto';
              break;
            case 404:
              this.error = 'Usuario no encontrado';
              break;
            case 500:
              this.error = 'Error en el servidor';
              break;
            default:
              this.error = error.response.data || 'Error durante el inicio de sesión';
          }
        } else if (error.request) {
          this.error = 'No se pudo conectar con el servidor';
        } else {
          this.error = 'Error durante el inicio de sesión';
        }
      } finally {
        this.isLoading = false;
      }
    }
  }
}

</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.login-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.logo {
  width: 180px;
  margin-bottom: 1.5rem;
  margin-top: 1.5rem;
  margin-left: 1.5rem;
  margin-right: 1.5rem;
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

input, select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.btn-login {
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

.btn-login:hover {
  background-color: #099;
  color: #fff;
}

.error-message {
  color: #B92E25;
  margin-top: 1rem;
  text-align: center;
}

.register-link {
  text-align: center;
  margin-top: 1.5rem;
  color: #747473;
}

.register-link a {
  color: #009999;
  text-decoration: none;
}
</style>