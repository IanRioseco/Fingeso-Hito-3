<template>
  <div class="login-container">
    <div class="login-box">
      <img src="@/assets/img/logo-ultrahealt.png" alt="RedSalud Logo" class="logo">
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
        <button type="submit" class="btn-login">Ingresar</button>
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
export default {
  name: 'LoginView',
  data() {
    return {
      credentials: {
        rut: '',
        password: '',
        role: ''
      },
      error: ''
    }
  },
  methods: {
    formatRut() {
      // Implementar lógica de formato RUT chileno
    },
    async handleLogin() {
      try {
        console.log('Credenciales enviadas:', this.credentials); // <-- Agrega esto

        // Lógica de autenticación con backend
        await this.$store.dispatch('auth/login', { ...this.credentials });
        // Obtén el rol real del usuario autenticado desde el store
        const userRole = this.$store.getters['auth/userRole'];

        switch(userRole) {
          case 'admin':
            this.$router.push('/admin');
            break;
          case 'doctor':
            this.$router.push('/doctor');
            break;
          case 'patient':
            this.$router.push('/patient');
            break;
          case 'technician':
            this.$router.push('/technician');
            break;
          case 'receptionist':
            this.$router.push('/receptionist');
            break;
          case 'pharmacy':
            this.$router.push('/pharmacy');
            break;
          default:
            this.$router.push('/');
        }
      } catch (error) {
        this.error = 'Credenciales incorrectas';
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