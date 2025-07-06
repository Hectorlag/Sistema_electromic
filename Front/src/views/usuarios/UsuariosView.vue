<template>
    <div>
        <Inicio />
    </div>
    <div class="listado-usuarios">
        <div class="listado-tabla">
            <h1 class="titulo">Listado de Usuarios</h1>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>N°</th>
                        <th>Nombre</th>
                        <th>Contraseña</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="usuario in usuariosActivos" :key="usuario.id_usuario">
                        <td> {{ usuario.id_usuario }}</td>
                        <td> {{ usuario.nombre }} </td>
                        <td> {{ usuario.contrasenia }}</td>
                        <td class="acciones">
                            <button class="editar" @click="editarUsuario(usuario)">Editar</button>
                            <button class="eliminar"
                                @click="eliminarUsuarioLogicamente(usuario.id_usuario)">Eliminar</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Formulario para editar un usuario (se muestra solo cuando se selecciona "Editar") -->
        <div v-if="usuarioAEditar" class="editar-usuario">
            <h3>Editar Usuario</h3>
            <div class="form-group">
                <label>ID:</label>
                <input v-model="usuarioAEditar.id_usuario" disabled />
            </div>
            <div class="form-group">
                <label>Nombre</label>
                <input v-model="usuarioAEditar.nombre" type="text" id="nombre" />
            </div>

            <div class="form-group">
                <div class="form-group password-input">
                    <label for="password">Contraseña: </label>
                    <input v-model="usuarioAEditar.contrasenia" type="password" id="password" class="form-control" />
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
        <div v-if="mensajeExito" class="mensaje-exito">
            Modificación exitosa!
        </div>
    </div>
</template>
<script>

import NavBar from '@/components/common/NavBar.vue';

export default {
    components: {
        NavBar,
    },
    data() {
        return {
            usuarios: [],
            usuarioAEditar: null,
            mensajeExito: false
        };
    },
    methods: {
        togglePasswordVisibility() {
            const passwordInput = document.getElementById('password');
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
            } else {
                passwordInput.type = 'password';
            }
        },

        obtenerUsuarios() {
            this.$axios.get('http://localhost:8080/usuarios/traer')
                .then(response => {
                    console.log('Respuesta de la API:', response.data);
                    this.usuarios = response.data;

                    // Almacena la lista de usuarios en el almacenamiento local
                    localStorage.setItem('usuarios', JSON.stringify(response.data));
                })
                .catch(error => {
                    console.error('Error al obtener usuarios:', error);
                });
        },

        editarUsuario(usuario) {
            this.usuarioAEditar = Object.assign({}, usuario);
        },
        actualizarUsuario() {
            // Verifica que el ID y telefono estén definidos y telefono sea un array
            if (this.usuarioAEditar.id_usuario !== undefined ||
                this.usuarioAEditar.nombre !== undefined ||
                this.usuarioAEditar.contrasenia !== undefined) {
            }

            // Lógica para actualizar el usuario
            this.$axios.put(`http://localhost:8080/usuarios/editar/${this.usuarioAEditar.id_usuario}`, this.usuarioAEditar)
                .then(() => {
                    this.obtenerUsuarios();
                    this.usuarioAEditar = null; // Se limpia el usuario A Editar después de la actualización
                    this.mensajeExito = true;
                    setTimeout(() => {
                        this.mensajeExito = false;
                    }, 3000);
                })
                .catch(error => {
                    console.error('Error al actualizar el usuario:', error);
                });
            console.log('Usuario actualizado:', this.usuarioAEditar);
        },

        cancelarEdicion() {
            // Cancela la edición y reinicia la variable de usuarioAEditar
            this.usuarioAEditar = null;
        },

        eliminarUsuarioLogicamente(id) {
            // Pregunta al si está seguro antes de eliminar al usuario
            if (window.confirm('¿Estás seguro de que deseas eliminar al usuario?')) {
                const usuario = this.usuarios.find(usuario => usuario.id_usuario === id);

                if (usuario) {
                    // Actualiza el estado en el frontend
                    usuario.deleted = true;

                    // Actualiza la lista de clientes en el almacenamiento local
                    localStorage.setItem('usuarios', JSON.stringify(this.usuarios));

                    // Envía la solicitud para eliminar lógicamente en el backend
                    this.$axios.delete(`http://localhost:8080/usuarios/borrar/${id}`)
                        .then(() => {
                            console.log('Usuario eliminado lógicamente:', id);
                            // Éxito, puedes mostrar un mensaje o hacer más acciones si es necesario
                        })
                        .catch(error => {
                            console.error('Error al eliminar lógicamente el usuario:', error);
                            // Puede revertir el cambio en el frontend si es necesario
                            usuario.deleted = false;
                            // También puede revertir el cambio en el almacenamiento local
                            localStorage.setItem('usuarios', JSON.stringify(this.usuarios));
                        });
                }
            }
        },
    },
    computed: {
        usuariosActivos() {
            return this.usuarios.filter(usuario => !usuario.deleted);
        },
    },
    mounted() {
        // Intenta cargar usuarios desde el almacenamiento local
        const storedUsuarios = localStorage.getItem('usuarios');
        if (storedUsuarios) {
            // Si hay usuarios almacenados, úsalos en lugar de hacer una solicitud al servidor
            this.usuarios = JSON.parse(storedUsuarios);
        } else {
            // Si no hay cusuarios almacenados, obtén la lista del servidor
            this.obtenerUsuarios();
        }
    },


};
</script>
  
<style scoped>
.listado-usuarios {
    margin-top: 60px;
}

.listado-tabla {
    width: 1200px;
    margin-bottom: 40px;
}

.editar-usuario {
    width: 100%;
}

.titulo {
    text-align: center;
    margin-top: 50px;
}

/* Estilos para la tabla */
.table {
    width: 100%;
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

/* Estilos para los botones editar y eliminar */
.acciones {
    text-align: center;
    margin-top: 20px;
    width: 200px;
}

.acciones button {
    width: 46%;
    padding: 6px;
    margin-right: 10px;
}

/* Estilos para los botones editar y eliminar */
.editar {
    padding: 2px 8px;
    font-size: 0.8em;
    width: 55px;

}

.eliminar {
    padding: 2px 8px;
    font-size: 0.8em;
    width: auto;
}

.editar {
    background-color: #007bff;
    color: white;
}

.eliminar {
    background-color: #dc3545;
    color: white;
}

.editar:hover {
    background-color: #0056b3;
}

.eliminar:hover {
    background-color: #c82333;
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
    background-color: #4caf50;
    color: white;
    padding: 10px;
    border-radius: 5px;
    text-align: center;
    display: inline-block;
    margin-left: 35%;
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
</style>
  