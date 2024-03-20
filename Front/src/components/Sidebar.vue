<template>
    <div>
        <!-- asideBar -->
        <aside class="sidebar">
            <ul>
                <div class="usuario">
                    <!-- Contenedor del icono de usuario -->
                    <div class="icono-usuario">
                        <i class="fas fa-user"></i>
                    </div>
                    <!-- Contenedor para el nombre de usuario -->
                    <div class="nombre-usuario">
                        {{ nombre }}
                    </div>
                </div>
                <li>
                    <a href="#" @click="toggleSection('Clientes')">Clientes</a>
                    <ul v-show="secciones.Clientes">
                        <li><router-link to="clienteregistrar">Registrar</router-link></li>
                        <li><router-link to="clienteslistar">Listar</router-link></li>
                        <li><router-link to="clientebuscar">Buscar</router-link></li>
                    </ul>
                </li>
                <li>
                    <a href="#" @click="toggleSection('Productos')">Productos</a>
                    <ul v-show="secciones.Productos">
                        <li><router-link to="productoregistrar">Registrar</router-link></li>
                        <li><router-link to="productoslistar">Listar</router-link></li>
                        <li><router-link to="productobuscar">Buscar</router-link></li>
                    </ul>
                </li>
                <li>
                    <a href="#" @click="toggleSection('Presupuestos')">Presupuestos</a>
                    <ul v-show="secciones.Presupuestos">
                        <li><router-link to="presupuestoregistrar">Registrar</router-link></li>
                        <li><router-link to="presupuestoslistar">Listar</router-link></li>
                        <li><router-link to="presupuestobuscar">Buscar</router-link></li>
                    </ul>
                </li>
                <li>
                    <a href="#" @click="toggleSection('OrdenDeTrabajo')">Ordenes de Trabajo</a>
                    <ul v-show="secciones.OrdenDeTrabajo">
                        <li><router-link to="ordenregistrar">Registrar</router-link></li>
                        <li><router-link to="ordeneslistar">Listar</router-link></li>
                        <li><router-link to="ordenbuscar">Buscar</router-link></li>
                    </ul>
                </li>
                <li>
                    <a href="#" @click="toggleSection('micuenta')">Mi cuenta</a>
                    <ul v-show="secciones.micuenta">
                        <li><router-link to="/perfilcuenta">Perfil</router-link></li>
                        <li><router-link to="/login">Cerrar sesión</router-link></li>
                    </ul>
                </li>
            </ul>
        </aside>

        <router-view></router-view>

        <div class="principal" v-if="mostrarImagenDeFondo && $route.path === '/dashboard'">
            <!-- La imagen de fondo -->
            <img src="@/assets/imagenes/bg.jpg" alt="Background Image">
        </div>
    </div>
</template>

<script>
export default {
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
            nombre: '' // Inicializa la variable nombre
        };
    },
    methods: {
        toggleSection(section) {
            this.secciones[section] = !this.secciones[section];
        },
    },
    watch: {
        '$route'() {
            // Oculta la imagen de fondo en todas las rutas excepto en la página principal
            this.mostrarImagenDeFondo = this.$route.path === '/dashboard';
        }
    },
    created() {
        // Verifica si hay un usuario en el localStorage y establecer el nombre de usuario
        const usuarioString = localStorage.getItem('usuario');
        if (usuarioString) {
            try {
                const usuario = JSON.parse(usuarioString);
                if (usuario && usuario.nombre) {
                    this.nombreUsuario = usuario.nombre;
                }
            } catch (error) {
                console.error('Error al analizar el usuario almacenado en localStorage:', error);
            }
        }
    },

    mounted() {
        // Obtiene el nombre de usuario del localStorage
        const nombreUsuario = localStorage.getItem('usuario');
        // Asigna el nombre de usuario a la variable 'nombre'
        this.nombre = nombreUsuario;
        // Emite un evento con el nombre de usuario
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

.sidebar {
    position: fixed;
    top: 80px;
    left: 0;
    height: 100%;
    width: 200px;
    background-color: #343a40;
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.9);
}

.sidebar ul {
    list-style-type: none;
    padding: 0;
    margin: 8px;
}

.sidebar ul li a {
    display: block;
    color: white;
    padding: 14px;
    text-decoration: none;
}

.sidebar ul li ul {
    display: none;
    background-color: #23272b;
}

.sidebar ul li:hover ul {
    display: block;
}

.usuario {
    display: flex;
    flex-direction: column;
    margin-top: 25px;
    margin-bottom: 20px;
}

.icono-usuario {
    width: 35px;
    height: 35px;
    border-radius: 50%;
    background-color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 10px;
    margin-left: 70px;
}

.icono-usuario i {
    color: black;
    font-size: 22px;
}

.nombre-usuario {
    background-color: white;
    color: black;
    text-align: center;
    font-size: 14px;
    padding: 3px;
    border-radius: 5px;
    font-family: Arial, sans-serif;
    font-weight: bold;
}

</style>
