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
    path: '/:pathMatch(.*)*',
    redirect: '/'
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { requiresAuth: false }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const isAuthenticated = store.getters['auth/isAuthenticated'];
  const userRole = store.getters['auth/userRole'];
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/');
  } else if (to.meta.requiresAuth && to.meta.role && userRole !== to.meta.role) {
    next('/unauthorized');
  } else {
    next();
  }
});

export default router;