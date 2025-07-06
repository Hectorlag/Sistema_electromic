<template>
    <div>
        <NavBar />
    </div>
    <div class="listado-tabla">
        <h1 class="titulo">Detalle del usuario</h1>
        <table class="table">
            <thead>
                <tr>
                    <th>N°</th>
                    <th>Nombre</th>
                    <th>Contraseña</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody v-if="usuario">
                <tr>
                    <td>{{ usuario.id_usuario }}</td>
                    <td>{{ usuario.nombre }}</td>
                    <td class="contrasenia-container">
                        <span v-if="mostrarContrasenia">{{ usuario.contrasenia }}</span>
                        <span v-else>●●●●●●●</span>
                    </td>

                    <td class="acciones">
                        <button class="mostrar-contrasenia-btn" @click="toggleMostrarContrasenia">{{ mostrarContrasenia
                ?
                'Ocultar' : 'Contraseña' }} </button>
                        <button class="editar" @click="editarUsuario(usuario)">Editar</button>
                    </td>

                </tr>
            </tbody>
        </table>
        <!-- Formulario para editar un usuario (se muestra solo cuando se selecciona "Editar") -->
        <div v-if="usuarioAEditar" class="editar-usuario">
            <h3>Editar Usuario</h3>
            <div class="form-group">
                <label>ID:</label>
                <input v-model="usuarioAEditar.id_usuario" disabled />
            </div>
            <div class="form-group">
                <label>Nombre</label>
                <input v-model="usuarioAEditar.nombre" type="text" />
            </div>
            <div class="form-group">
                <div class="password-input">
                    <label for="password">Contraseña:</label>
                    <input v-model="usuarioAEditar.contrasenia" type="password" id="password" />
                    <img src="@/assets/imagenes/ojo.png" alt="Visor Contraseña" class="password-toggle"
                        @click="togglePasswordVisibility" />
                </div>

            </div>
            <div class="acciones">
                <button type="submit" @click="actualizarUsuario">Actualizar</button>
                <button type="button" @click="cancelarEdicion" class="cancelar">Cancelar</button>
            </div>
        </div>
        <!-- Msj de actualización exitosa -->
        <div v-if="mensajeExito" class="mensaje-exito">Modificación exitosa!</div>
    </div>
    <p v-if="!usuario" class="mensaje">No se pudo cargar la información del usuario.</p>
</template>

<script>
import { mapGetters } from 'vuex';
import NavBar from '@/components/common/NavBar.vue';


export default {
    components: {
        NavBar,
    },
    computed: {
        ...mapGetters(['nombreUsuario']),
    },
    data() {
        return {
            usuario: null,
            nombre: '',
            usuarioAEditar: null,
            mensajeExito: false,
            mostrarContrasenia: false,
        };
    },
    methods: {

        toggleMostrarContrasenia() {
            this.mostrarContrasenia = !this.mostrarContrasenia;
        },
        togglePasswordVisibility() {
            const passwordInput = document.getElementById('password');
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
            } else {
                passwordInput.type = 'password';
            }
        },

        editarUsuario(usuario) {
            this.usuarioAEditar = { ...usuario }; // Clona el usuario para evitar la edición directa del usuario original
        },
        actualizarUsuario() {
            if (!this.usuarioAEditar) return;

            this.$axios.put(`http://localhost:8080/usuarios/editar/${this.usuarioAEditar.id_usuario}`, {
                nombre: this.usuarioAEditar.nombre,
                contrasenia: this.usuarioAEditar.contrasenia
            })
                .then(() => {
                    // Actualiza solo el nombre de usuario en el localStorage
                    localStorage.setItem('usuario', this.usuarioAEditar.nombre);
                    // Actualiza el nombre de usuario en la variable 'nombre'
                    this.nombre = this.usuarioAEditar.nombre;
                    this.obtenerUsuario(); // Obtiene nuevamente los datos del usuario actualizado
                    this.mensajeExito = true;
                    setTimeout(() => {
                        this.mensajeExito = false;
                        this.usuarioAEditar = null;
                    }, 3000);
                })
                .catch(error => {
                    console.error('Error al actualizar el usuario:', error);
                });
        },

        cancelarEdicion() {
            this.usuarioAEditar = null;
        },
        obtenerUsuario() {
            if (!this.nombre) return;
            this.$axios.get(`http://localhost:8080/usuarios/traerXnombre/${this.nombre}`)
                .then(response => {
                    if (response.data) {
                        this.usuario = response.data;
                    } else {
                        console.log('No se encontró ningún usuario con el nombre proporcionado.');
                    }
                })
                .catch(error => {
                    console.error('Error al obtener usuario:', error);
                });
        }

    },
    mounted() {
        const nombreUsuario = localStorage.getItem('usuario');
        this.nombre = nombreUsuario;
        this.obtenerUsuario();
    },
};
</script>

<style scoped>
.listado-tabla {
    width: 1200px;
    margin-top: -40px;
}

.editar-usuario {
    width: 100%;
}

.titulo {
    text-align: center;
    margin-top: 10px;
}

/* Estilos para la tabla */
.table {
    width: 80%;
    border-collapse: collapse;
    margin-top: 20px;
}

.table th,
.table td {
    border: 1px solid #ddd;
    padding: 18px;
    text-align: left;
}

.table th {
    background-color: #f2f2f2;
}

/* Estilos para los botones editar y contraseña */

.acciones button {
    width: 46%;
    padding: 6px;
    margin-right: 10px;
}

/* Estilos para los botones editar */
.editar {
    padding: 2px 8px;
    font-size: 0.8em;
    width: 55px;
}

.editar {
    background-color: #007bff;
    color: white;
}

/* Estilos para el formulario de edición */
.editar-usuario {
    background-color: #f2e9e9;
    padding: 15px;
    border-radius: 5px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    margin-top: 7px;
    width: 70%;
    max-width: 400px;
    border: 1px solid #ddd;
}

.form-group label {
    display: block;
    margin-bottom: 3px;
}

.form-group input {
    width: 70%;
    padding: 3px;
    box-sizing: border-box;
    margin-bottom: 8px;
    border: 1px solid #ddd;
}

.editar-usuario button {
    background-color: #4caf50;
    color: white;
    padding: 6px 12px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    margin-right: 10px;
    border: 1px solid #4caf50;
    margin-bottom: 30px;
}

.editar-usuario button:hover {
    background-color: #45a049;
}

.mensaje-exito {
    display: inline-block;
    margin-left: 40%;
    background-color: #d4edda;
    color: #155724;
    padding: 10px 30px;
    border: 1px solid #c3e6cb;
    border-radius: 5px;
    margin-top: 10px;
    text-align: center;
}

.acciones button:last-child {
    margin-right: 0;
}

.acciones button.cancelar {
    background-color: #e53935;
    color: white;
}

.password-input {
    position: relative;
}

.password-toggle {
    position: absolute;
    height: 25px;
    width: 25px;
    top: 50%;
    right: 120px;
    cursor: pointer;
}

.contrasenia-container {
    width: 300px;
}

.acciones {
    text-align: center;
    margin-top: 20px;
    width: 280px;
}

.mostrar-contrasenia-btn,
.editar {
    width: 100px;
    padding: 6px 12px;
    font-size: 0.9em;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.mostrar-contrasenia-btn {
    background-color: #007bff;
    color: white;
}

.editar {
    background-color: #007bff;
    color: white;
}

.mostrar-contrasenia-btn:hover,
.editar:hover {
    background-color: #0056b3;
}
</style>