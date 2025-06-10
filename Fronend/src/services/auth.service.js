export default {
  async login(credentials) {
    if (
      credentials.rut === '12345678-9' &&
      credentials.password === 'paciente' &&
      credentials.role === 'admin'
    ) {
      return {
        user: { rut: '12345678-9', role: 'admin' },
        token: 'fake-jwt-token'
      }
    }
    if (
      credentials.rut === '98765432-1' &&
      credentials.password === 'doctor' &&
      credentials.role === 'doctor'
    ) {
      return {
        user: { rut: '98765432-1', role: 'doctor' },
        token: 'fake-jwt-token'
      }
    }
    // Si no coincide ninguna condición, lanza el error:
    throw new Error('Credenciales incorrectas');
  },

  async register(userData) {
    return { success: true }
  }
}