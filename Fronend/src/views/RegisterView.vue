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
            @input="userData.nombrePa = userData.nombrePa.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]/g,'')"
          >
        </div>
        <div class="form-group">
          <label for="apellidoPa">Apellido</label>
          <input 
            id="apellidoPa" 
            v-model="userData.apellidoPa" 
            type="text" 
            required
            @input="userData.apellidoPa = userData.apellidoPa.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]/g,'')"
          >
        </div>
        <div class="form-group">
          <label for="fechanacimiento">Fecha de nacimiento</label>
          <input 
            id="fechanacimiento"
            v-model="userData.fechanacimiento"
            type="date"
            required
          >
        </div>
        <div class="form-group">
          <label for="sexo">Sexo</label>
          <select id="sexo" v-model="userData.sexo" required>
            <option value="" disabled>Seleccione sexo</option>
            <option value="Femenino">Femenino</option>
            <option value="Masculino">Masculino</option>
            <option value="Otro">Otro</option>
          </select>
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
            placeholder="+56 9 XXXX XXXX"
            @input="userData.telefono = userData.telefono.replace(/[^0-9+]/g, '').replace(/^(?!\+569)/, '+569')"
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
//IMPORTACIONES
import { authService } from '@/services/auth.service';

//EXPORTACIONES
export default {
  //DATOS REACTIVOS PARA EL FORMULARIO
  data() {
    return {
      userData: {
        rut: '',
        nombrePa: '',
        apellidoPa: '',
        fechanacimiento: '',
        sexo: '',
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
    //FORMATEO DEL RUT
    // Esta funcion formatea el RUT ingresado por el usuario
    formatRut() {
      let value = this.userData.rut.replace(/[^0-9kK]/g, '');//elimina caracteres no numericos y la 'k' o 'K'
      //si el rut tiene más de 1 caracter, se divide en dos partes
      if (value.length > 1) {
        const dv = value.slice(-1);
        const rutBody = value.slice(0, -1);
        
        this.userData.rut = rutBody.replace(/\B(?=(\d{3})+(?!\d))/g, '.') + '-' + dv;//formatea el rut con puntos y guion
      } else {
        this.userData.rut = value;
      }
    },
    //MeTODO PARA MANEJAR EL REGISTRO
    async handleRegister() {
      try {
        this.isLoading = true;
        this.error = '';

        const rutLimpio = this.userData.rut.replace(/[.-]/g, '');//elimina puntos y guiones del RUT para enviarlo limpio al servidor
        //crea un objeto con los datos del usuario
        const userData = {
          ...this.userData,
          rut: rutLimpio
        };

        console.log('Intentando registro con:', userData);//para debbuging
        //llamada al servicio de autenticación para registrar al usuario
        const response = await authService.register(userData);
        
        console.log('Respuesta del servidor:', response);//para debbuging

        //verifica si la respuesta es exitosa
        if (response && response.success) {
          // Mostrar mensaje de éxito
          alert('Registro exitoso. Por favor, inicia sesión.');
          // Redirigir al login después del registro exitoso
          this.$router.push({ name: 'Login' });
        } else {
          this.error = 'Error durante el registro: No se recibió una respuesta válida del servidor';
        }
        //manejo de errores en el registro
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
          //MANEJO DE ERRORES DE LA CONEXIÓN
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

.login-link a:hover {
  text-decoration: underline;
}

.register-box form {
  display: flex;
  flex-direction: column;
}

.register-box form input,
.register-box form select {
  margin-bottom: 1rem;
}

.register-box form input[type="date"] {
  padding: 0.75rem;
}


.register-box form select {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}
</style>