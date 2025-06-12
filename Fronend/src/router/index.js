import { createRouter, createWebHistory } from 'vue-router';
import store from '@/store';

// Ejemplo de rutas, ajusta según tus vistas/componentes
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

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Navegación guard
router.beforeEach((to, from, next) => {
  console.log('Navegando a:', to.path);
  console.log('Meta requiresAuth:', to.meta.requiresAuth);
  console.log('Meta role:', to.meta.role);
  
  if (to.meta.requiresAuth) {
    console.log('Ruta requiere autenticación');
    next();
  } else {
    console.log('Ruta no requiere autenticación');
    next();
  }
});

export default router;