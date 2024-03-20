<template>
  <div id="app">
    <router-view />
  </div>
</template>


<script>
import Inicio from './components/Inicio.vue';
import NavBar from './components/NavBar.vue';
import Sidebar from './components/Sidebar.vue';
import Login from './components/Login.vue';
import RegistrarCliente from './components/RegistrarCliente.vue';
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

export default {
  name: 'App',
  components: {
    Inicio,
    NavBar,
    Login,
    RegistrarCliente,
    Sidebar,
    ListarClientes,
    BuscarCliente,
    RegistrarProducto,
    ListarProductos,
    BuscarProducto,
    BuscarProductoMarca,
    RegistrarPresupuesto,
    ListarPresupuestos,
    BuscarPresupuesto,
    RegistrarOrden,
    ListarOrdenes,
    BuscarOrden,
    RegistrarUsuario,
    ListarUsuarios,
    MiCuentaPerfil

  }
};
</script>