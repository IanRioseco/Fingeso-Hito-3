<template>
  <div class="carrusel-container">
    <div class="carrusel-scrollx">
      <div class="carrusel-inner-multifila">
        <div class="slide-fila" v-for="(grupo, idx) in especialidadesAgrupadas" :key="idx">
          <EspecialtiesCard
            v-for="(esp, i) in grupo"
            :key="i"
            :especialidad="esp"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
//IMPORTACIONES
import 'vue3-carousel/dist/carousel.css'
import EspecialtiesCard from './SpecialtiesCard.vue'
import microscopio from '@/assets/img/microscopio.png'
import bancodesangre from '@/assets/img/liquido.png'
import corazon from '@/assets/img/cardiologia.png'
import kinesiologia from '@/assets/img/asistencia-medica-y-medicina.png'
import examenes from '@/assets/img/prescripcion.png'
import dermatologia from '@/assets/img/medicina.png'
import pediatria from '@/assets/img/recien-nacido.png'
import urologia from '@/assets/img/urologia.png'
import ginecologia from '@/assets/img/ginecologia.png'
import ortopedia from '@/assets/img/vendaje.png'
import neurologia from '@/assets/img/cabeza.png'
import oftalmologia from '@/assets/img/optometria.png'
import urgencias from '@/assets/img/cuidado-de-la-salud.png'

//DATOS REACTIVOS PARA EL COMPONENTE
const especialidades = [
  { nombre: 'Laboratorio', descripcion: 'Con presencia de Arica a Punta Arenas', img: microscopio },
  { nombre: 'Banco de Sangre', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: bancodesangre },
  { nombre: 'Cardiología', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: corazon },  
  { nombre: 'Kinesiología', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: kinesiologia },
  { nombre: 'Examenes', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: examenes },
  { nombre: 'Urgencias', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: urgencias },
  { nombre: 'Radiologia', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: bancodesangre },
  { nombre: 'Dermatologia', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: dermatologia },
  { nombre: 'Pediatria', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: pediatria },
  { nombre: 'Neurologia', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: neurologia },
  { nombre: 'Oftalmología', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: oftalmologia },
  { nombre: 'Ortopedia', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: ortopedia },
  { nombre: 'Ginecologia', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: ginecologia },
  { nombre: 'Urologia', descripcion: 'Donar sangre es regalar vida. Reserva tu hora', img: urologia },
]

// Agrupa las especialidades en pares para simular 2 filas
function agruparEnFilas(array, porFila) {
  const filas = []
  for (let i = 0; i < array.length; i += porFila) {
    filas.push(array.slice(i, i + porFila))
  }
  return filas
}
// Agrupa las especialidades en pares para simular 2 filas
const especialidadesAgrupadas = agruparEnFilas(especialidades, 2)
// Configuración del carousel
const config = {
  height: 250,
  itemsToShow: 4, // Cuántas tarjetas mostrar a la vez
  gap: 16,
  wrapAround: false,
}
</script>

<style scoped>
.carrusel-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 0;
  background: #fff;
  border-radius: 12px;
  border: 1px solid #eee;
  box-sizing: border-box;
}
.carrusel-scrollx {
  overflow-x: auto;
  overflow-y: hidden;
  /* Elimina scroll-snap-type */
}

.carrusel-inner-multifila {
  display: flex;
  flex-direction: row;
  gap: 16px;
  /* Elimina scroll-snap-align */
}
.slide-fila {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-bottom: 10px;
}
</style>
