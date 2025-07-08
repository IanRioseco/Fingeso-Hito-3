import { createRouter, createWebHistory } from 'vue-router';
import store from '@/store';

// Importar las vistas
//manejo de rutas con autenticación y roles
const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/AdminView.vue'),
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: '/doctor',
    name: 'Doctor',
    component: () => import('@/views/DoctorView.vue'),
    meta: { requiresAuth: true, role: 'doctor' }
  },
  {
    path: '/patient',
    name: 'Patient',
    component: () => import('@/views/PatientView.vue'),
    meta: { requiresAuth: true, role: 'patient' }
  },
  {
    path: '/technician',
    name: 'Technician',
    component: () => import('@/views/TechnicianView.vue'),
    meta: { requiresAuth: true, role: 'technician' }
  },
  {
    path: '/receptionist',
    name: 'Receptionist',
    component: () => import('@/views/ReceptionistView.vue'),
    meta: { requiresAuth: true, role: 'receptionist' }
  },
  {
    path: '/pharmacy',
    name: 'Pharmacy',
    component: () => import('@/views/PharmacyView.vue'),
    meta: { requiresAuth: true, role: 'pharmacy' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
];
// Crear el router
const router = createRouter({
  history: createWebHistory(),
  routes
});

// Navegación guard
router.beforeEach(async (to, from, next) => {
  // para debugging
  console.log('ROUTER - Navegando a:', to.path);
  console.log('ROUTER - Meta requiresAuth:', to.meta.requiresAuth);
  console.log('ROUTER - Meta role:', to.meta.role);
  
  // Temporalmente deshabilitar el guard para debugging
  console.log('ROUTER - Guard temporalmente deshabilitado para debugging');
  next();
  
  /* 
  // Inicializar autenticación si no está inicializada
  await store.dispatch('auth/initializeAuth');
  
  const isAuthenticated = store.getters['auth/isAuthenticated'];
  const userRole = store.getters['auth/userRole'];
  const currentUser = store.getters['auth/currentUser'];
  
  console.log('ROUTER - Estado de auth:', { isAuthenticated, userRole, currentUser });
  
  if (to.meta.requiresAuth) {
    console.log('ROUTER - Ruta requiere autenticación');
    
    if (!isAuthenticated) {
      console.log('ROUTER - Usuario no autenticado, redirigiendo a login');
      next('/login');
      return;
    }
    
    if (to.meta.role && userRole !== to.meta.role) {
      console.log('ROUTER - Rol no autorizado:', { required: to.meta.role, actual: userRole });
      next('/login');
      return;
    }
    
    console.log('ROUTER - Autenticación exitosa, permitiendo navegación');
    next();
  } else {
    console.log('ROUTER - Ruta no requiere autenticación');
    next();
  }
  */
});

export default router;