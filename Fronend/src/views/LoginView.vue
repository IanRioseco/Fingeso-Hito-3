<template>
  <div class="login-container">
    <div class="login-box">
      <img src="@/assets/img/logoUH.png" alt="RedSalud Logo" class="logo" />
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
          />
        </div>
        <div class="form-group">
          <label for="password">Contraseña</label>
          <div class="password-wrapper">
            <input 
              id="password" 
              :type="showPassword ? 'text' : 'password'"
              v-model="credentials.password" 
              required
            />
            <button 
                type="button" 
                class="toggle-password" 
                @click="showPassword = !showPassword"
                :aria-label="showPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'"
              >
                <span v-if="showPassword">
                  <!-- Ojo abierto SVG -->
                  <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="none" viewBox="0 0 24 24"><path stroke="#C51A6F" stroke-width="2" d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12Z"/><circle cx="12" cy="12" r="3" stroke="#C51A6F" stroke-width="2"/></svg>
                </span>
                <span v-else>
                  <!-- Ojo cerrado SVG -->
                  <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="none" viewBox="0 0 24 24"><path stroke="#C51A6F" stroke-width="2" d="M3 3l18 18M1 12s4-7 11-7c2.21 0 4.21.57 6 1.53M23 12s-4 7-11 7c-2.21 0-4.21-.57-6-1.53"/><path stroke="#C51A6F" stroke-width="2" d="M9.53 9.53A3 3 0 0012 15a3 3 0 002.47-5.47"/></svg>
                </span>
              </button>
          </div>
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
        <div class="form-actions">
          <button 
            type="submit" 
            class="btn-login" 
            :disabled="isLoading"
          >
            {{ isLoading ? 'Iniciando sesión...' : 'Ingresar' }}
          </button>
        </div>
        <p v-if="error" class="error-message">{{ error }}</p>
      </form>
      <div class="form-actions">
        <button class="btn-home" @click="$router.push('/')">
          Volver al inicio
        </button>
      </div>
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
  rut = rut.replace(/[^0-9kK]/g, '');//eliminar caracteres no numerico
  if (rut.length > 1) {
    const dv = rut.slice(-1);//obtener el digito verificador
    const rutBody = rut.slice(0, -1);//obtener el número de RUT

    rut = rutBody.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '-' + dv;//formatear con puntos y guión
  }
  return rut;
}

//componente
export default {
  data() {
    return {
      credentials: {
        rut: '',
        password: '',
        role: ''
      },
      error: '',
      isLoading: false,
      showPassword: false
    }
  },
  methods: {
    //método para formatear el rut
    formatRut() {
      let value = this.credentials.rut.replace(/[^0-9kK]/g, '');
      if (value.length > 1) {
        const dv = value.slice(-1);
        const rutBody = value.slice(0, -1);
        this.credentials.rut = rutBody.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '-' + dv;
      } else {
        this.credentials.rut = value;
      }
    },
    //método para manejar el inicio de sesión
    async handleLogin() {
      try {
        this.isLoading = true;
        this.error = '';

        const rutLimpio = this.credentials.rut.replace(/[.-]/g, '');//eliminar caracteres no numerico

        console.log('Intentando login con:', {//para debugging
          rut: rutLimpio,
          password: this.credentials.password,
          role: this.credentials.role
        });
        //llamar al servicio de login
        const response = await authService.login(
          rutLimpio,
          this.credentials.password,
          this.credentials.role
        );
        
        console.log('Respuesta del servidor:', response);//para debugging

        // Si llegamos aquí, el login fue exitoso
        if (response) {
          console.log('Login exitoso, rol:', this.credentials.role);
          
          try {
            //redireccionar según el rol
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
                console.log('Rol no válido:', this.credentials.role);//para debugging
                // Si el rol no es válido, mostrar un mensaje de error
                this.error = 'Rol no válido';
            }
            //MANEJO DE ERRORES EN LA REDIRECCIÓN
          } catch (routerError) {
            console.error('Error en la redirección:', routerError);
            this.error = 'Error al redireccionar: ' + routerError.message;
          }
        }
        //MANEJO DE ERRORES DE LA LOGICA DE LOGIN
      } catch (error) {
        console.error('Error completo:', error);//para debugging
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
              this.error = error.response.data.message || 'Error durante el inicio de sesión';
          }
          //MANEJO DE ERRORES DE LA CONEXIÓN
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

.btn-home {
  width: 100%;
  padding: 0.75rem;
  background-color: #0875C1;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
  transition: background 0.2s;
}
.btn-home:hover {
  background-color: #C51A6F;
}

.toggle-password:focus {
  outline: none;
}

.toggle-password {
  background: none;
  border: none;
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  font-size: 1.2rem;
  color: #C51A6F;
  padding: 0 0.5rem;
}

.password-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.password-wrapper input {
  flex: 1;
}

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

.btn-home:hover {
  background-color: #C51A6F;
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