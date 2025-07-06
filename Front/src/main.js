/*import './assets/main.css'
import App from './App.vue'

// CSS - Importar en orden específico
import '@/assets/Css/bootstrap.min.css'
import '@fortawesome/fontawesome-free/css/all.css' // FontAwesome primero
import 'bootstrap-icons/font/bootstrap-icons.css'   // Bootstrap Icons después
import './assets/css/shared-form-styles.css'
import './assets/css/shared-table-styles.css'

// JS
import '@/assets/Js/bootstrap.bundle.min.js'
import Swal from 'sweetalert2'

// Vue
import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import store from './store/store.js'
import axios from 'axios'

// ============================================
// IMPORTS CORREGIDOS PARA NUEVA ESTRUCTURA
// ============================================

// Componentes comunes
import Sidebar from './components/common/Sidebar.vue'
import NavBar from './components/common/NavBar.vue'

// Vistas principales (en views/)
import HomeView from './views/HomeView.vue'                     // ← CORREGIDO: HomeView en lugar de Inicio
import LoginView from './views/auth/LoginView.vue'
import ProfileView from './views/auth/ProfileView.vue'

// Vistas de listados (ya en views/)
import ClientesView from './views/clientes/ClientesView.vue'
import ProductosView from './views/productos/ProductosView.vue'
import PresupuestosView from './views/presupuestos/PresupuestosView.vue'
import OrdenesView from './views/ordenes/OrdenesView.vue'
import UsuariosView from './views/usuarios/UsuariosView.vue'

// Formularios (ya en components/forms/)
import RegistrarCliente from './components/forms/RegistrarCliente.vue'
import RegistrarProducto from './components/forms/RegistrarProducto.vue'
import RegistrarPresupuesto from './components/forms/RegistrarPresupuesto.vue'
import RegistrarOrden from './components/forms/RegistrarOrden.vue'
import RegistrarUsuario from './components/forms/RegistrarUsuario.vue'

// Componentes de búsqueda (ya en components/search/)
import BuscarCliente from './components/search/BuscarCliente.vue'
import BuscarProducto from './components/search/BuscarProducto.vue'
import BuscarProductoMarca from './components/search/BuscarProductoMarca.vue'
import BuscarPresupuesto from './components/search/BuscarPresupuesto.vue'
import BuscarOrden from './components/search/BuscarOrden.vue'

// Configuración de Axios
axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*'

// ============================================
// RUTAS ACTUALIZADAS
// ============================================
const routes = [
  { path: '/', redirect: '/inicio' },
  { path: '/inicio', component: HomeView },
  { path: '/login', component: LoginView },

  // Clientes
  { path: '/clienteregistrar/:id?', component: RegistrarCliente },
  { path: '/clienteslistar', component: ClientesView },
  { path: '/clientebuscar', component: BuscarCliente },

  // Productos - ✅ ACTUALIZADO
  { path: '/productoregistrar/:id?', component: RegistrarProducto },
  { path: '/productoslistar', component: ProductosView },
  { path: '/productobuscar', component: BuscarProducto },
  { path: '/buscarmarca', component: BuscarProductoMarca },

  // Presupuestos - ✅ ACTUALIZADO  
  { path: '/presupuestoregistrar/:id?', component: RegistrarPresupuesto },
  { path: '/presupuestoslistar', component: PresupuestosView },
  { path: '/presupuestobuscar', component: BuscarPresupuesto },

  // Órdenes - ✅ ACTUALIZADO
  { path: '/ordenregistrar/:id?', component: RegistrarOrden },
  { path: '/ordeneslistar', component: OrdenesView },
  { path: '/ordenbuscar', component: BuscarOrden },

  // Usuarios - ✅ ACTUALIZADO: Cambié la ruta para que coincida con el login
  { path: '/registrarusuario', component: RegistrarUsuario },
  { path: '/usuarioslistar', component: UsuariosView },

  // Perfil
  { path: '/perfilcuenta', component: ProfileView },

  // Componentes de navegación (opcionales)
  { path: '/sidebar', component: Sidebar },
  { path: '/navbar', component: NavBar },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// Crear y configurar la app
const app = createApp(App)

// Configurar propiedades globales
app.config.globalProperties.$axios = axios
app.config.globalProperties.$swal = Swal

// Usar plugins
app.use(router)
app.use(store)

// Montar la aplicación
app.mount('#app')*/
import './assets/main.css'
import App from './App.vue'

// CSS - Importar en orden específico
import '@/assets/Css/bootstrap.min.css'
import '@fortawesome/fontawesome-free/css/all.css' // FontAwesome primero
import 'bootstrap-icons/font/bootstrap-icons.css'   // Bootstrap Icons después
import './assets/css/shared-form-styles.css'
import './assets/css/shared-table-styles.css'

// JS
import '@/assets/Js/bootstrap.bundle.min.js'
import Swal from 'sweetalert2'

// Vue
import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import store from './store/store.js'
import axios from 'axios'

// ============================================
// IMPORTS MODERNOS - SIMPLIFICADOS
// ============================================

// ✅ CAMBIO PRINCIPAL: Usar ModernHomeView en lugar de HomeView
import HomeView from './views/ModernHomeView.vue'  // ← ACTUALIZADO
import LoginView from './views/auth/LoginView.vue'
import ProfileView from './views/auth/ProfileView.vue'

// Vistas de listados
import ClientesView from './views/clientes/ClientesView.vue'
import ProductosView from './views/productos/ProductosView.vue'
import PresupuestosView from './views/presupuestos/PresupuestosView.vue'
import OrdenesView from './views/ordenes/OrdenesView.vue'
import UsuariosView from './views/usuarios/UsuariosView.vue'

// Formularios
import RegistrarCliente from './components/forms/RegistrarCliente.vue'
import RegistrarProducto from './components/forms/RegistrarProducto.vue'
import RegistrarPresupuesto from './components/forms/RegistrarPresupuesto.vue'
import RegistrarOrden from './components/forms/RegistrarOrden.vue'
import RegistrarUsuario from './components/forms/RegistrarUsuario.vue'

// ============================================
// ✅ COMPONENTES DE BÚSQUEDA ACTUALIZADOS
// ============================================
import BuscarCliente from './components/search/BuscarCliente.vue'
import BuscarProducto from './components/search/BuscarProducto.vue'
import BuscarProductoMarca from './components/search/BuscarProductoMarca.vue'
import BuscarPresupuesto from './components/search/BuscarPresupuesto.vue'
// ✅ NUEVO: Usar el componente de búsqueda de órdenes mejorado
import BuscarOrden from './components/search/BusquedaOrdenes.vue'  // ← ACTUALIZADO

// Configuración de Axios
axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*'

// ============================================
// ✅ RUTAS ACTUALIZADAS CON BÚSQUEDAS MEJORADAS
// ============================================
const routes = [
  // Rutas principales
  { path: '/', redirect: '/inicio' },
  { path: '/inicio', component: HomeView },  // ← Ahora apunta a ModernHomeView.vue
  { path: '/login', component: LoginView },
  { path: '/perfilcuenta', component: ProfileView },

  // ============================================
  // CLIENTES - Rutas completas
  // ============================================
  { 
    path: '/clienteregistrar/:id?', 
    component: RegistrarCliente,
    name: 'ClienteRegistrar',
    meta: { title: 'Registrar Cliente' }
  },
  { 
    path: '/clienteslistar', 
    component: ClientesView,
    name: 'ClientesListar',
    meta: { title: 'Listado de Clientes' }
  },
  { 
    path: '/clientebuscar', 
    component: BuscarCliente,
    name: 'ClienteBuscar',
    meta: { title: 'Buscar Cliente' }
  },

  // ============================================
  // PRODUCTOS - Rutas completas
  // ============================================
  { 
    path: '/productoregistrar/:id?', 
    component: RegistrarProducto,
    name: 'ProductoRegistrar',
    meta: { title: 'Registrar Producto' }
  },
  { 
    path: '/productoslistar', 
    component: ProductosView,
    name: 'ProductosListar',
    meta: { title: 'Listado de Productos' }
  },
  { 
    path: '/productobuscar', 
    component: BuscarProducto,
    name: 'ProductoBuscar',
    meta: { title: 'Buscar Producto' }
  },
  { 
    path: '/buscarmarca', 
    component: BuscarProductoMarca,
    name: 'ProductoBuscarMarca',
    meta: { title: 'Buscar Producto por Marca' }
  },

  // ============================================
  // PRESUPUESTOS - Rutas completas
  // ============================================
  { 
    path: '/presupuestoregistrar/:id?', 
    component: RegistrarPresupuesto,
    name: 'PresupuestoRegistrar',
    meta: { title: 'Registrar Presupuesto' }
  },
  { 
    path: '/presupuestoslistar', 
    component: PresupuestosView,
    name: 'PresupuestosListar',
    meta: { title: 'Listado de Presupuestos' }
  },
  { 
    path: '/presupuestobuscar', 
    component: BuscarPresupuesto,
    name: 'PresupuestoBuscar',
    meta: { title: 'Buscar Presupuesto' }
  },

  // ============================================
  // ✅ ÓRDENES DE TRABAJO - Rutas completas con búsqueda mejorada
  // ============================================
  { 
    path: '/ordenregistrar/:id?', 
    component: RegistrarOrden,
    name: 'OrdenRegistrar',
    meta: { title: 'Registrar Orden de Trabajo' }
  },
  { 
    path: '/ordeneslistar', 
    component: OrdenesView,
    name: 'OrdenesListar',
    meta: { title: 'Listado de Órdenes de Trabajo' }
  },
  { 
    path: '/ordenbuscar', 
    component: BuscarOrden,  // ← Ahora apunta al componente mejorado BusquedaOrdenes.vue
    name: 'OrdenBuscar',
    meta: { title: 'Buscar Orden de Trabajo' }
  },

  // ============================================
  // USUARIOS - Rutas completas
  // ============================================
  { 
    path: '/registrarusuario', 
    component: RegistrarUsuario,
    name: 'UsuarioRegistrar',
    meta: { title: 'Registrar Usuario' }
  },
  { 
    path: '/usuarioslistar', 
    component: UsuariosView,
    name: 'UsuariosListar',
    meta: { title: 'Listado de Usuarios' }
  },

  // ============================================
  // ✅ RUTAS ADICIONALES PARA BÚSQUEDAS ESPECÍFICAS
  // ============================================
  
  // Alias y rutas alternativas para búsquedas
  { 
    path: '/buscar/cliente', 
    redirect: '/clientebuscar',
    meta: { title: 'Buscar Cliente' }
  },
  { 
    path: '/buscar/producto', 
    redirect: '/productobuscar',
    meta: { title: 'Buscar Producto' }
  },
  { 
    path: '/buscar/presupuesto', 
    redirect: '/presupuestobuscar',
    meta: { title: 'Buscar Presupuesto' }
  },
  { 
    path: '/buscar/orden', 
    redirect: '/ordenbuscar',
    meta: { title: 'Buscar Orden de Trabajo' }
  },

  // ============================================
  // ✅ MANEJO DE RUTAS NO ENCONTRADAS
  // ============================================
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/inicio'
  }
]

// ============================================
// ✅ CONFIGURACIÓN DEL ROUTER MEJORADA
// ============================================
const router = createRouter({
  history: createWebHistory(),
  routes,
  // Configuración adicional para mejor UX
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0, behavior: 'smooth' }
    }
  }
})

// ============================================
// ✅ GUARDS DE NAVEGACIÓN (OPCIONAL)
// ============================================
router.beforeEach((to, from, next) => {
  // Actualizar el título de la página
  if (to.meta.title) {
    document.title = `${to.meta.title} - Sistema de Gestión`
  } else {
    document.title = 'Sistema de Gestión'
  }
  
  // Continuar con la navegación
  next()
})

// Crear y configurar la app
const app = createApp(App)

// Configurar propiedades globales
app.config.globalProperties.$axios = axios
app.config.globalProperties.$swal = Swal

// ============================================
// ✅ CONFIGURACIÓN GLOBAL MEJORADA
// ============================================

// Manejo global de errores de Axios
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // Redirigir al login si no está autenticado
      router.push('/login')
    } else if (error.response?.status === 404) {
      console.warn('Recurso no encontrado:', error.config?.url)
    } else if (error.response?.status >= 500) {
      // Error del servidor
      console.error('Error del servidor:', error.message)
      Swal.fire({
        title: 'Error del Servidor',
        text: 'Ha ocurrido un error interno. Por favor, intenta nuevamente.',
        icon: 'error',
        confirmButtonText: 'Entendido'
      })
    }
    return Promise.reject(error)
  }
)

// Usar plugins
app.use(router)
app.use(store)

// Montar la aplicación
app.mount('#app')

// ============================================
// ✅ EXPORTAR CONFIGURACIONES PARA DEBUGGING
// ============================================
if (process.env.NODE_ENV === 'development') {
  window.router = router
  window.axios = axios
  console.log('🚀 App iniciada en modo desarrollo')
  console.log('📍 Rutas disponibles:', routes.map(r => r.path))
}