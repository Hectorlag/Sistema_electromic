import './assets/main.css'

import App from './App.vue'
import '@/assets/Css/bootstrap.min.css'
import '@/assets/Js/bootstrap.bundle.min.js'
import '@fortawesome/fontawesome-free/css/all.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import Swal from 'sweetalert2';



import { createApp } from 'vue'
import store from './components/store.js'; // Importa tu store Vuex aquí
import axios from 'axios';
import { createRouter, createWebHistory } from 'vue-router';
import Inicio from './components/Inicio.vue';
import Sidebar from './components/Sidebar.vue';
import NavBar from './components/NavBar.vue';
import Login from './components/Login.vue';
import RegistrarCliente from './components/RegistrarCliente.vue'
import ListarClientes from './components/ListarClientes.vue';
import BuscarCliente from './components/BuscarCliente.vue';
import RegistrarProducto from './components/RegistrarProducto.vue';
import ListarProductos from './components/ListarProductos.vue';
import BuscarProducto from './components/BuscarProducto.vue';
import BuscarProductoMarca from './components/BuscarProductoMarca.vue';
import RegistrarPresupuesto from './components/RegistrarPresupuesto.vue';
import ListarPresupuestos from './components/ListarPresupuestos.vue';
import BuscarPresupuesto from './components/BuscarPresupuesto.vue';
import RegistrarOrden from './components/RegistrarOrden.vue';
import ListarOrdenes from './components/ListarOrdenes.vue';
import BuscarOrden from './components/BuscarOrden.vue';
import RegistrarUsuario from './components/RegistrarUsuario.vue';
import ListarUsuarios from './components/ListarUsuarios.vue';
import MiCuentaPerfil from './components/MiCuentaPerfil.vue';

// Configura Axios para usar la URL base de tu API Spring Boot
axios.defaults.baseURL = 'http://localhost:8080'; 
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

const app = createApp(App);

// Agrega Axios a las propiedades globales de la aplicación Vue
app.config.globalProperties.$axios = axios;

// Configura las rutas de Vue Router
const routes = [
  { path: '/inicio', component: Inicio },
  { path: '/login', component: Login },
  { path: '/clienteregistrar', component: RegistrarCliente },
  { path: '/clienteslistar', component: ListarClientes },
  { path: '/clientebuscar', component: BuscarCliente },
  { path: '/productoregistrar', component: RegistrarProducto },
  { path: '/productoslistar', component: ListarProductos },
  { path: '/productobuscar', component: BuscarProducto },
  { path: '/buscarmarca', component: BuscarProductoMarca },
  { path: '/presupuestoregistrar', component: RegistrarPresupuesto },
  { path: '/presupuestoslistar', component: ListarPresupuestos },
  { path: '/presupuestobuscar', component: BuscarPresupuesto },
  { path: '/ordenregistrar', component: RegistrarOrden },
  { path: '/ordeneslistar', component: ListarOrdenes },
  { path: '/ordenbuscar', component: BuscarOrden },
  { path: '/usuarioregistrar', component: RegistrarUsuario},
  { path: '/usuarioslistar', component: ListarUsuarios },
  { path: '/perfilcuenta', component: MiCuentaPerfil },
  { path: '/sidebar', component: Sidebar },
  { path: '/navbar', component: NavBar },

 
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

app.use(router);
app.use(store); // Usa el store Vuex en tu aplicación
app.mount('#app');
