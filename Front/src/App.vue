<!--
<template>
  <div id="app">
    <router-view />
  </div>
</template>


<script>
/*
// Vistas
import HomeView from './views/HomeView.vue';
import NavBar from './components/common/NavBar.vue';
import Sidebar from './components/common/Sidebar.vue';
import LoginView from './views/auth/LoginView.vue';
import RegistrarCliente from './components/forms/RegistrarCliente.vue';
import ClientesView from './views/clientes/ClientesView.vue';
import BuscarCliente from './components/search/BuscarCliente.vue';
import RegistrarProducto from './components/forms/RegistrarProducto.vue';
import ProductosView from './views/productos/ProductosView.vue';
import BuscarProducto from './components/search/BuscarProducto.vue';
import BuscarProductoMarca from './components/search/BuscarProductoMarca.vue';
import RegistrarPresupuesto from './components/forms/RegistrarPresupuesto.vue';
import PresupuestosView from './views/presupuestos/PresupuestosView.vue';
import BuscarPresupuesto from './components/search/BuscarPresupuesto.vue';
import RegistrarOrden from './components/forms/RegistrarOrden.vue';
import OrdenesView from './views/ordenes/OrdenesView.vue';
import BuscarOrden from './components/search/BuscarOrden.vue';
import RegistrarUsuario from './components/forms/RegistrarUsuario.vue';
import UsuariosView from './views/usuarios/UsuariosView.vue';
import ProfileView from './views/auth/ProfileView.vue';

export default {
  name: 'App',
  components: {
    HomeView,
    NavBar,
    LoginView,
    RegistrarCliente,
    Sidebar,
    ClientesView,
    BuscarCliente,
    RegistrarProducto,
    ProductosView,
    BuscarProducto,
    BuscarProductoMarca,
    RegistrarPresupuesto,
    PresupuestosView,
    BuscarPresupuesto,
    RegistrarOrden,
    OrdenesView,
    BuscarOrden,
    RegistrarUsuario,
    UsuariosView,
    ProfileView
  }
};

</script>

<style>
/*
* {
  box-sizing: border-box;
}

body {
  margin: 0 !important;
  padding: 0 !important;
  overflow-x: hidden;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
  background-color: #f8f9fa;
}

#app {
  width: 100%;
  min-height: 100vh;
  margin: 0;
  padding: 0;
  background-color: #f8f9fa;
}

:root {
  --navbar-height: 80px;
  --sidebar-width: 250px;
  --primary-color: #3498db;
  --secondary-color: #2c3e50;
  --background-color: #f8f9fa;
}

::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.text-center {
  text-align: center !important;
}

.w-100 {
  width: 100% !important;
}

.h-100 {
  height: 100% !important;
}

@media (max-width: 992px) {
  :root {
    --sidebar-width: 0px;
  }
}

@media (max-width: 768px) {
  :root {
    --navbar-height: 70px;
  }
}

.router-view {
  width: 100%;
  min-height: 100vh;
}

</style>-->

<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  
  // ✅ YA NO NECESITAMOS IMPORTAR TODOS LOS COMPONENTES
  // La nueva arquitectura los maneja internamente en cada vista
  
  mounted() {
    // Configuración global si es necesaria
    console.log('🚀 App iniciada con navegación moderna');
  }
};
</script>

<style>
/* ============================================ */
/* RESET GLOBAL MODERNO Y LIMPIO */
/* ============================================ */

/* Reset básico sin interferir */
* {
  box-sizing: border-box;
}

/* Body completamente limpio */
body {
  margin: 0 !important;
  padding: 0 !important;
  overflow-x: hidden;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
  background-color: #f8f9fa;
}

/* App completamente limpio */
#app {
  width: 100%;
  min-height: 100vh;
  margin: 0;
  padding: 0;
  background-color: #f8f9fa;
}

/* ============================================ */
/* VARIABLES CSS GLOBALES PARA NAVEGACIÓN MODERNA */
/* ============================================ */
:root {
  --navbar-height: 80px;
  --rail-width: 65px; /* ✅ NUEVO: Ancho del Navigation Rail */
  --primary-color: #3498db;
  --secondary-color: #2c3e50;
  --background-color: #f8f9fa;
  --white: #ffffff;
  --border-radius: 8px;
  --box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  --transition: all 0.3s ease;
}

/* ============================================ */
/* SCROLLBAR GLOBAL PERSONALIZADA */
/* ============================================ */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
  transition: var(--transition);
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* ============================================ */
/* UTILIDADES GLOBALES */
/* ============================================ */
.text-center {
  text-align: center !important;
}

.w-100 {
  width: 100% !important;
}

.h-100 {
  height: 100% !important;
}

.d-flex {
  display: flex !important;
}

.justify-content-center {
  justify-content: center !important;
}

.align-items-center {
  align-items: center !important;
}

/* ============================================ */
/* CLASES UTILITARIAS PARA SPACING */
/* ============================================ */
.m-0 { margin: 0 !important; }
.p-0 { padding: 0 !important; }
.mt-1 { margin-top: 0.25rem !important; }
.mt-2 { margin-top: 0.5rem !important; }
.mt-3 { margin-top: 1rem !important; }
.mb-1 { margin-bottom: 0.25rem !important; }
.mb-2 { margin-bottom: 0.5rem !important; }
.mb-3 { margin-bottom: 1rem !important; }

/* ============================================ */
/* RESPONSIVE BREAKPOINTS GLOBALES */
/* ============================================ */
@media (max-width: 992px) {
  :root {
    --rail-width: 0px; /* En móviles el rail se oculta */
  }
}

@media (max-width: 768px) {
  :root {
    --navbar-height: 70px;
  }
}

/* ============================================ */
/* OVERRIDE PARA ROUTER-VIEW */
/* ============================================ */
.router-view {
  width: 100%;
  min-height: 100vh;
}

/* ============================================ */
/* ESTILOS GLOBALES PARA ANIMACIONES */
/* ============================================ */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from {
  transform: translateX(-20px);
}

.slide-leave-to {
  transform: translateX(20px);
}

/* ============================================ */
/* MEJORAS DE ACCESIBILIDAD */
/* ============================================ */
*:focus {
  outline: 2px solid var(--primary-color);
  outline-offset: 2px;
  border-radius: 4px;
}

button:focus,
a:focus,
input:focus,
select:focus,
textarea:focus {
  outline: 2px solid var(--primary-color);
  outline-offset: 2px;
}

/* ============================================ */
/* PRELOAD DE FUENTES PARA MEJOR PERFORMANCE */
/* ============================================ */
@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}

/* ============================================ */
/* MODO OSCURO - PREPARADO PARA FUTURO */
/* ============================================ */
@media (prefers-color-scheme: dark) {
  /* Variables para modo oscuro - se pueden activar más tarde */
  /*
  :root {
    --background-color: #1a1a1a;
    --primary-color: #4a9eff;
    --secondary-color: #2d3748;
  }
  */
}
</style>