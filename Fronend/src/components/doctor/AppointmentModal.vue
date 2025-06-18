<template>
  <div class="modal">
    <div class="modal-content">
      <h3>Detalles de la Cita</h3>
      <!-- Aquí puedes mostrar los detalles de la cita -->
      <button @click="$emit('close')">Cerrar</button>
      <button @click="$emit('save', appointment)">Guardar</button>
      <button @click="$emit('cancel', appointment?.id)">Cancelar</button>
    </div>
  </div>
</template>

<script setup>
defineProps(['appointment'])// Define las propiedades que recibirá el componente :)
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
</style>