// src/views/Home.vue

<template>
  <div>
    <div class="home-main">
      <!-- Banner principal -->
      <section class="banner">
        <img src="@/assets/img/logoUH.png" alt="RedSalud" class="banner-img" />
        <div class="banner-content">
          <h1>Bienvenido a UltraHealth</h1>
          <p>Tu salud, nuestra prioridad. Agenda tus citas, consulta tus exámenes y accede a tu historial médico.</p>
          <div class="home-actions">
            <router-link to="/login" class="btn-home">Iniciar Sesión</router-link>
            <router-link to="/register" class="btn-home btn-secondary">Registrarse</router-link>
          </div>
        </div>
      </section>

      <!-- Especialidades -->
      <section class="especialidades-container">
        <h2>Especialidades Médicas</h2>
        <section class="especialidades-container-info">
          <div class="especialidades-carrusel-wrapper">
            <CarouselEspecialties/>
          </div>
          <section class="aside-container">
            <aside class="aside-container-info">
              <h2>Especialidades Médicas</h2>
              <p>Explora nuestras especialidades médicas y reserva tu hora en línea.</p>
              <p>Con presencia de Arica a Punta Arenas.</p>
              <div class="btn-citas-container">
                <router-link to="/login" class="btn-citas">Agendar Cita</router-link>
              </div>
            </aside>
          </section>
        </section>
      </section>


      <!-- Servicios -->
      <section class="servicios">
        <div class="servicio-card">
          <img src="@/assets/img/logo-ultrahealt.png" alt="Agenda en línea" />
          <h3>Agenda en línea</h3>
          <p>Reserva tu hora con especialistas fácilmente desde casa.</p>
        </div>
        <div class="servicio-card">
          <img src="@/assets/img/logo-ultrahealt.png" alt="Resultados de exámenes" />
          <h3>Resultados de exámenes</h3>
          <p>Accede a tus exámenes clínicos en línea de forma segura.</p>
        </div>
        <div class="servicio-card">
          <img src="@/assets/img/logo-ultrahealt.png" alt="Recetas electrónicas" />
          <h3>Recetas electrónicas</h3>
          <p>Consulta y descarga tus recetas médicas.</p>
        </div>
      </section>

      <!-- Noticias y otros -->
      <section class="noticias">
        <h2>Noticias y Salud</h2>
        <div class="noticia">
          <h4>Mamógrafo con Tomosíntesis: Conoce sus ventajas</h4>
          <p>RedSalud incorpora un nuevo mamógrafo con tomosíntesis para una toma de mamografías más clara, precisa y eficiente.</p>
        </div>
        <div class="noticia">
          <h4>RedSalud es reconocida como la marca más valorada en Clínicas y Centros Médicos</h4>
          <p>Por segundo año consecutivo RedSalud fue reconocida como la marca más valorada en Clínicas y Centros Médicos según el estudio Chile 3D 2025 de NIQ.</p>
        </div>
      </section>
      <section class = "footer">
        <p>&copy; 2025 RedSalud. Todos los derechos reservados.</p>
      </section>
    </div>
  </div>
</template>

<script setup >
import CarouselEspecialties from '@/components/specialtiescarousel/CarouselSpecialties.vue';
</script>

<style scoped>
.home-main {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #f5f5f5;
  min-height: 100vh;
}
.banner {
  width: 100%;
  background: linear-gradient(90deg, #0875C1 0%, #C51A6F 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem 0 1rem 0;
  flex-wrap: wrap;
}
.banner-img {
  max-width: 220px;
  margin: 0 2rem 1rem 0;
  background: #fff;
  border-radius: 50%;
  box-shadow: 0 2px 8px #0001;
}
.banner-content {
  max-width: 500px;
}
.home-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-start;
  margin: 1.5rem 0;
}
.btn-home {
  padding: 1rem 2rem;
  background: #fff;
  color: #0875C1;
  border-radius: 6px;
  text-decoration: none;
  font-weight: bold;
  border: 2px solid #0875C1;
}
.btn-home:hover {
  background: #C51A6F;
  color: #fff;
  border: 2px solid #0875C1;
}
.btn-secondary {
  font-weight: bold;
  background: #0875C1;
  color: #fff;
  border: 2px solid #C51A6F;
}
.btn-secondary:hover {
  background: #fff;
  color: #0875C1;
}
.especialidades-container {
  margin: 2rem auto;
  text-align: center;
  background: #fff;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 2px 8px #0001;
  min-height: 500px;
  max-width: 1200px;
  width: 100%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.25);
}
.especialidades h2 {
  margin-bottom: 1rem;
  color: #0875C1;
}
.especialidades-container-info {
  display: grid;
  grid-template-columns: 1fr 350px;
  gap: 2rem;
  align-items: center;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
}

.especialidades-carrusel-wrapper {
  max-width: 100%;
  overflow-x: auto;
}

.carrusel-container {
  max-width: 100%;
  width: 100%;
  margin: 0;
}

.aside-container {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px #0001;
  padding: 1.5rem;
  text-align: center;
  color: #0875C1;
}
.aside-container-info {
  margin: 0 auto;
}
.btn-citas-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}
.btn-citas {
  background: #0875C1;
  color: #fff;
  border: 2px solid #C51A6F;
  border-radius: 6px;
  padding: 5px 10px;
  text-decoration: none;
  font-weight: bold;
}
.btn-citas:hover {
  background: #C51A6F;
  color: #fff;
  border: 2px solid #0875C1;
}
.servicios {
  display: flex;
  gap: 2rem;
  justify-content: space-around;
  margin: 2rem 0;
  flex-wrap: wrap;
  background: #fff;
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.25);
  border-radius: 10px;
  padding: 2rem;
  max-width: 1200px;
  width: 100%;
}
.servicio-card {
  background: #fff;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.20);
  min-width: 220px;
  text-align: center;
  max-width: 300px;
}
.servicio-card img {
  max-width: 100px;
  margin-bottom: 1rem;
}
.noticias {
  margin: 2rem 0;
  width: 100%;
  max-width: 1200px;
  
}
.noticia {
  background: #e8f5e9;
  margin-bottom: 1rem;
  padding: 1rem;
  border-radius: 8px;
}
@media (max-width: 900px) {
  .servicios {
    flex-direction: column;
    align-items: center;
  }
  .banner {
    flex-direction: column;
    text-align: center;
  }
  .banner-img {
    margin: 0 auto 1rem auto;
  }
  .banner-content {
    max-width: 100%;
  }
  .especialidades-container-info {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
}
.footer {
  width: 100%;
  background: linear-gradient(90deg, #0875C1 0%, #C51A6F 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem 0 1rem 0;
  flex-wrap: wrap;
  bottom: 0;
}
</style>
