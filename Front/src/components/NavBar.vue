<template>
  <div>
    <!-- Barra de navegación superior con redes sociales -->
    <nav class="navbar">

      <!-- Botón de menú con router-link -->
      <router-link to="/inicio" class="navbar-toggler" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon custom-toggler"></span>
      </router-link>

      <div class="container-fluid">
        <div class="redes-sociales ml-auto"> <!-- Mover redes sociales a la derecha -->
          <a href="#" class="nav-link"><i class="fab fa-facebook-f"></i></a>
          <a href="#" class="nav-link"><i class="fab fa-twitter"></i></a>
          <a href="#" class="nav-link"><i class="fab fa-instagram"></i></a>
        </div>

        <!-- Contenedor cierre de sesión -->
        <div class="icono-usuario-nav">
          <div>
            <router-link to="/login">
              <img src="@/assets/imagenes/apagar.png" class="cerrar-sesion tamano-logo-cerrar-sesion-nav">
            </router-link>
          </div>
        </div>
      </div>

    </nav>

    <router-view></router-view>

    <div class="principal" v-if="mostrarImagenDeFondo && $route.path === '/inicio'">
      <!-- La imagen de fondo -->
      <img src="@/assets/imagenes/bg.jpg" alt="Background Image">
    </div>
  </div>
</template>

<script>

export default {
  components: {
  },
  data() {
    return {
      secciones: {
        Clientes: false,
        Productos: false,
        Presupuestos: false,
        OrdenDeTrabajo: false,
        micuenta: false,
      },
      mostrarImagenDeFondo: true,
      nombre: ''
    };
  },
  methods: {
    toggleSection(section) {
      this.secciones[section] = !this.secciones[section];
    },
  },
  watch: {
    '$route'() {
      // Ocultar la imagen de fondo en todas las rutas excepto en la página principal
      this.mostrarImagenDeFondo = this.$route.path === '/inicio';
    }
  },
  created() {
    // Verificar si hay un usuario en el localStorage y establecer el nombre de usuario
    const usuarioString = localStorage.getItem('usuario');
    if (usuarioString) {
      try {
        const usuario = JSON.parse(usuarioString);
        if (usuario && usuario.nombre) {
          this.nombreUsuario = usuario.nombre;
        }
      }
      catch (error) {
        console.error('Error al analizar el usuario almacenado en localStorage:', error);
      }
    }
  },
  mounted() {
    // Obtener el nombre de usuario del localStorage
    const nombreUsuario = localStorage.getItem('usuario');
    // Asignar el nombre de usuario a la variable 'nombre'
    this.nombre = nombreUsuario;
    // Emitir un evento con el nombre de usuario
    this.$emit('buscar-usuario', nombreUsuario);
  },
}
</script>

<style scoped>
.principal {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

.principal img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.navbar {
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  background-color: #343a40;
  height: 80px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.9);
}


.redes-sociales {
  display: flex;
  align-items: center;
  margin-left: 1000px;
  margin-top: -50px;
}

.redes-sociales a {
  color: white;
  margin-left: 20px;
}

.redes-sociales a:hover {
  color: #e4a906;
}

/*Estilos para el boton de la barra de navegación*/

.icono-usuario-nav {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: white;
  display: flex;
  justify-content: center;
  margin-left: 70px;
  margin-top: -50px;
}

.tamano-logo-cerrar-sesion-nav {
  width: 18px;
  height: 18px;
  margin-top: 4px;
}

.navbar-toggler {
  margin-left: 20px;
  margin-top: 15px;
}

.empresa{
  width: 400px;
  height: 70px;
  margin-right: 695px;
}

</style>