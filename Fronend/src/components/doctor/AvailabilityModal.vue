<template>
  <div class="modal">
    <div class="modal-content">
      <h3>Configurar Disponibilidad</h3>
      <!-- Aquí puedes agregar el formulario de disponibilidad -->
      <button class="btn-close" @click="$emit('close')">Cerrar</button>
      <button class="btn-save" @click="$emit('save', {})">Guardar</button>
    </div>
  </div>
</template>

<script setup>
// Puedes agregar props y lógica aquí si lo necesitas
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}
.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
}
.btn-close {
  background-color: #099;
  color: #fff;
  font-weight: bold;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
}
.btn-save {
  background-color: #C51A6F;
  color: #fff;
  font-weight: bold;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
}
</style>