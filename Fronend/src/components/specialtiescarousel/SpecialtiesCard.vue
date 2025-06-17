<template>
  <div class="especialidad-card">
      <span>
        <img :src="especialidad.img" :alt="especialidad.nombre" />
      </span>
      <span>
        <h3>{{ especialidad.nombre }}</h3>
        <p>{{ especialidad.descripcion }}</p>
      </span>
  </div>
</template>

<script setup>
//definicion de propiedades
defineProps({
  especialidad: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.especialidad-card {
  display: inline-block;
  width: 220px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  margin: 0 5px;
  transition: box-shadow 0.2s;
}
.especialidad-card a {
  display: flex;
  align-items: center;
  text-decoration: none;
  padding: 16px;
  gap: 10px;
}
.especialidad-card img {
  width: 64px;
  height: 64px;
  object-fit: contain;
}
.especialidad-card h3 {
  color: #0875C1;
  font-size: 16px;
  line-height: 18px;
  font-weight: 600;
  margin: 0 0 5px 0;
}
.especialidad-card p {
  color: #747473;
  font-size: 14px;
  line-height: 16px;
  margin-bottom: 0;
}
.especialidad-card:hover {
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.25);
}
</style>
