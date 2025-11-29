import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '../stores/authStore';

// Lazy loading route components
const Home = () => import('../pages/Home.vue');
const Category = () => import('../pages/Category.vue');
const NewsDetail = () => import('../pages/NewsDetail.vue');
const LoginPage = () => import('../pages/LoginPage.vue');
const RegisterPage = () => import('../pages/RegisterPage.vue');
const AdminPage = () => import('../pages/AdminPage.vue');
const NewsUploadPage = () => import('../pages/NewsUploadPage.vue');

const routes = [
  {
    path: '/',
    name: 'Login',
    component: LoginPage,
    meta: {
      title: 'Login Portal - Paparazzi News',
      requiresAuth: false
    }
  },
  {
    path: '/category/:categoryName',
    name: 'Category',
    component: Category,
    props: true,
    meta: {
      title: 'Category News'
    }
  },
  {
    path: '/news/:id',
    name: 'NewsDetail',
    component: NewsDetail,
    props: true,
    meta: {
      title: 'News Detail'
    }
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: {
      title: 'Paparazzi News - Home'
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage,
    meta: {
      title: '注册 - Paparazzi News',
      requiresAuth: false
    }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminPage,
    meta: {
      title: 'Admin Control Panel - Paparazzi News',
      requiresAuth: true,
      requiresAdmin: true
    }
  },
  {
    path: '/upload',
    name: 'NewsUpload',
    component: NewsUploadPage,
    meta: {
      title: 'Upload News - Paparazzi News',
      requiresAuth: true,
      requiresMember: true
    }
  },
  // 404 route, redirect to home
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }];

const router = createRouter({
  history: createWebHistory(),
  routes,
  // Configure scroll behavior
  scrollBehavior(to, from, savedPosition) {
    // If there is a saved position, restore to that position
    if (savedPosition) {
      return savedPosition;
    }
    // Otherwise scroll to top
    return { top: 0 };
  }
});

// Global before guard for updating page title and permission checking
router.beforeEach((to, from, next) => {
  // Update page title
  document.title = to.meta.title || 'Paparazzi News';
  
  // Permission checking
  const authStore = useAuthStore();
  
  // If route requires authentication
  if (to.meta.requiresAuth === true) {
    if (!authStore.isAuthenticated) {
      // Not logged in, redirect to login page
      return next({ name: 'Login', query: { redirect: to.fullPath } });
    }
    
    // If route requires admin permission
    if (to.meta.requiresAdmin === true && !authStore.isAdmin) {
      // Not an admin, redirect to home page
      return next({ name: 'Home' });
    }
    
    // If route requires member permission
    if (to.meta.requiresMember === true && !authStore.isMember) {
      // Not a member, redirect to home page
      return next({ name: 'Home' });
    }
  }
  
  // If a logged-in user tries to access login or register page, redirect to home page for all roles
  if ((to.name === 'Login' || to.name === 'Register') && authStore.isAuthenticated) {
    return next({ name: 'Home' });
  }
  
  next();
});

export default router;