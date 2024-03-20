<template>
    <div>
        <NavBar />
    </div>
    <div class="listado-clientes">
        <div v-if="mostrarTabla" class="listado-tabla">
            <h2 class="titulo">Listado de Clientes</h2>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>N°</th>
                        <th>Apellido y nombre</th>
                        <th>Dirección</th>
                        <th>Dni</th>
                        <th>Email</th>
                        <th>Teléfono/s</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="cliente in clientesActivos" :key="cliente.id_cliente">
                        <td>{{ cliente.id_cliente }}</td>
                        <td>{{ cliente.nombre.slice(-1)[0] }} {{ cliente.nombre.slice(0, -1).join(' ') }}</td>
                        <td>{{ cliente.direccion }}</td>
                        <td>{{ cliente.cuil }}</td>
                        <td>{{ cliente.email }}</td>
                        <td>
                            <ul>
                                <li v-for="tel in cliente.telefono" :key="tel">
                                    <i class="bi bi-telephone"></i> {{ tel }}
                                </li>
                            </ul>
                        </td>
                        <td class="acciones">
                            <button class="btn btn-primary editar" @click="editarCliente(cliente)">
                                <i class="bi bi-pencil-fill"></i> Editar
                            </button>
                            <button class="btn btn-danger eliminar"
                                @click="eliminarClienteLogicamente(cliente.id_cliente)">
                                <i class="bi bi-trash-fill"></i> Eliminar
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Formulario para editar un cliente (se muestra solo cuando se selecciona "Editar") -->
        <div v-if="!mostrarTabla && clienteAEditar" class="editar-cliente">
            <h3>Editar Cliente</h3>
            <div class="form-group">
                <label>ID:</label>
                <input v-model="clienteAEditar.id_cliente" disabled />
            </div>
            <div class="form-group">
                <label>Nombre:</label>
                <input v-model="clienteAEditar.nombre[0]" type="text" id="nombre" />
            </div>

            <div class="form-group">
                <label>Apellido:</label>
                <input v-model="clienteAEditar.nombre[1]" type="text" id="apellido" />
            </div>

            <div class="form-group">
                <label>Dirección:</label>
                <input v-model="clienteAEditar.direccion" />
            </div>
            <div class="form-group">
                <label>Dni:</label>
                <input v-model="clienteAEditar.cuil" />
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input v-model="clienteAEditar.email" />
            </div>
            <div class="form-group">
                <label>Télefono:</label>
                <input v-model="clienteAEditar.telefono[0]" />
            </div>
            <div class="form-group">
                <label>Otro télefono:</label>
                <input v-model="clienteAEditar.telefono[1]" />
            </div>

            <div class="acciones">
                <button type="submit" @click="actualizarCliente">Actualizar</button>
                <button type="button" @click="cancelarEdicion" class="cancelar">Cancelar</button>
            </div>
            <div v-if="mensajeError" class="error">{{ mensajeError }}</div>
        </div>
        <!-- Msj de actualización exitosa -->
        <div v-if="mensajeExito" class="mensaje-exito">
            Modificación exitosa!
        </div>
    </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';
import Swal from 'sweetalert2';

export default {
    components: {
        NavBar,
    },
    data() {
        return {
            clientes: [],
            clienteAEditar: null,
            mensajeExito: false,
            mostrarTabla: true,
            mensajeError: '',
        };
    },
    methods: {
        obtenerClientes() {
            this.$axios.get('http://localhost:8080/clientes/traer')
                .then(response => {
                    console.log('Respuesta de la API:', response.data);
                    this.clientes = response.data;
                    // Ordena la lista de clientes por el último nombre
                    response.data.sort((a, b) => {
                        // Obtiene los últimos nombres de cada cliente
                        const ultimoNombreA = a.nombre[a.nombre.length - 1];
                        const ultimoNombreB = b.nombre[b.nombre.length - 1];

                        // Compara los últimos nombres de forma alfabética
                        if (ultimoNombreA < ultimoNombreB) return -1;
                        if (ultimoNombreA > ultimoNombreB) return 1;
                        return 0;
                    });

                    this.clientes = response.data;
                    // Almacena la lista de clientes en el almacenamiento local
                    localStorage.setItem('clientes', JSON.stringify(response.data));
                })
                .catch(error => {
                    console.error('Error al obtener clientes:', error);
                });
        },
        // Método para validar el formato del correo electrónico
        validarFormatoEmail(email) {
            // Si el campo de correo electrónico está vacío, se considera válido
            if (!email) {
                return true;
            }

            // Expresión regular para validar el formato del correo electrónico
            const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return regex.test(email);
        },

        editarCliente(cliente) {
            this.clienteAEditar = Object.assign({}, cliente);
            this.mostrarTabla = false;
        },
        actualizarCliente() {
            // Validar formato del correo electrónico
            if (!this.validarFormatoEmail(this.clienteAEditar.email)) {
                this.mensajeError = 'El formato del correo electrónico no es válido.';
                setTimeout(() => {
                    this.mensajeError = '';
                }, 3000);
                return;
            }
            // Verifica que los campos direccion y cuil no estén vacíos
            if (!this.clienteAEditar.direccion || !this.clienteAEditar.nombre[0] || !this.clienteAEditar.nombre[1] || !this.clienteAEditar.telefono[0] || !this.clienteAEditar.cuil) {
                console.error('Dirección, teléfono y nombre son campos obligatorios.');
                this.mensajeError = 'Nombre, apellido, dirección, cuil y teléfono son campos obligatorios.';
                setTimeout(() => {
                    this.mensajeError = '';
                }, 3000);
                return;
            }
            // Verificar que el ID y telefono estén definidos y telefono sea un array
            if (this.clienteAEditar.id_cliente !== undefined) {
                // Convertir el campo telefono a un array si no lo es
                if (!Array.isArray(this.clienteAEditar.telefono)) {
                    this.clienteAEditar.telefono = [this.clienteAEditar.telefono];
                }
            }

            // Lógica para actualizar el usuario
            this.$axios.put(`http://localhost:8080/clientes/editar/${this.clienteAEditar.id_cliente}`, this.clienteAEditar)
                .then(() => {
                    this.obtenerClientes();
                    this.clienteAEditar = null; // Se limpia el cliente A Editar después de la actualización
                    this.mensajeExito = true;
                    setTimeout(() => {
                        this.mensajeExito = false;
                        this.mostrarTabla = true;
                    }, 3000);
                })
                .catch(error => {
                    console.error('Error al actualizar el cliente:', error);
                });
            console.log('Cliente actualizado:', this.clienteAEditar);
        },

        cancelarEdicion() {
            // Cancela la edición y reinicia la variable de clienteAEditar
            this.clienteAEditar = null;
            this.mostrarTabla = true;
        },

        eliminarClienteLogicamente(id) {
            const cliente = this.clientes.find((cliente) => cliente.id_cliente === id);

            if (cliente) {
                // Muestra la ventana emergente
                this.mostrarVentanaEmergente("¿Estás seguro de eliminar este cliente?", () => {
                    // Lógica para eliminar el cliente si el usuario confirma
                    // Actualiza el estado en el frontend
                    cliente.deleted = true;

                    // Actualiza la lista de clientes en el almacenamiento local
                    localStorage.setItem("clientes", JSON.stringify(this.clientes));

                    // Envía la solicitud para eliminar lógicamente en el backend
                    this.$axios
                        .delete(`http://localhost:8080/clientes/borrar/${id}`)
                        .then(() => {
                            console.log("Cliente eliminado lógicamente:", id);
                            // Éxito, puedes mostrar un mensaje o hacer más acciones si es necesario
                        })
                        .catch((error) => {
                            console.error(
                                "Error al eliminar lógicamente el cliente:",
                                error
                            );
                            // Puede revertir el cambio en el frontend si es necesario
                            cliente.deleted = false;
                            // También puede revertir el cambio en el almacenamiento local
                            localStorage.setItem("clientes", JSON.stringify(this.clientes));
                        });
                });
            }
        },


        eliminarCliente(id) {
            // Lógica para eliminar el cliente
            var url = `http://localhost:8080/clientes/borrar/${id}`;
            this.enviarSolicitud('DELETE', null, url, 'Cliente eliminado')
                .then(() => {
                    // Llama a obtenerClientes() para actualizar la lista después de eliminar un cliente
                    this.obtenerClientes();
                });
        },



        enviarSolicitud(metodo, parametro, url, mensaje) {
            this.$axios({ method: metodo, url: url, data: parametro })
                .then(function (respuesta) {
                    var status = respuesta.data[0]['status'];
                    if (status === 'success') {
                        this.show_alerta(mensaje, status);
                        window.setTimeout(function () {
                            window.location.href = '/clienteslistar';
                        }, 1000);
                    } else {
                        var listado = '';
                        var errores = respuesta.data[1]['errors'];
                        Object.keys(errores).forEach(key => (listado += errores[key][0] + '.'));
                        this.show_alerta(listado, 'error');
                    }
                })
                .catch(function (error) {
                    console.error('Error en la solicitud:', error);
                    this.show_alerta('Error en la solicitud: ' + error.message, 'error');
                });
        },

        show_alerta(mensaje, icono, foco = '') {
            if (foco !== '') {
                document.getElementById(foco).focus();
            }
            Swal.fire({
                title: mensaje,
                icon: icono,
                customClass: {
                    confirmButton: 'btn btn-primary',
                    popup: 'animated zoomIn',
                },
                buttonsStyling: false,
            });
        },

        mostrarVentanaEmergente(mensaje, callback) {
            const swalWithBootstrapButton = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success me-3',
                    cancelButton: 'btn btn-danger',
                },
                buttonsStyling: false,
            });

            swalWithBootstrapButton
                .fire({
                    title: mensaje,
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonText: 'Sí, eliminar',
                    cancelButtonText: 'Cancelar',
                })
                .then(result => {
                    if (result.isConfirmed) {
                        callback();
                    }
                });
        },
    },
    computed: {
        clientesActivos() {
            return this.clientes.filter(cliente => !cliente.deleted);
        },
    },
    mounted() {
        // Intenta cargar clientes desde el almacenamiento local
        const storedClients = localStorage.getItem('clientes');
        if (storedClients) {
            // Si hay clientes almacenados, úsalos en lugar de hacer una solicitud al servidor
            this.clientes = JSON.parse(storedClients);
        } else {
            // Si no hay clientes almacenados, obtén la lista del servidor
            this.obtenerClientes();
        }
    },
};
</script>


<style scoped>
.listado-clientes {
    margin-top: 60px;
}

.listado-tabla {
    width: 1200px;
    margin-bottom: 40px;
}

.editar-cliente {
    width: 100%;
}

.titulo {
    background-color: #7399c2;
    color: white;
    padding: 10px;
    margin-top: 50px;
    width: 100%;
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

.table thead th {
    color: black;
    text-align: left;
    font-weight: bold;
}

/* Estilos para los botones editar y eliminar */
.acciones {
    text-align: center;
    margin-top: 30px;
}

/* Estilos para las celdas de la columna 'acciones' */
.table th.acciones,
.table td.acciones {
    width: 18%;
}

.acciones button {
    display: inline-block;
    width: 80px;
    padding: 3px;
    margin: 3px;
}

.editar {
    padding: 2px 8px;
    font-size: 0.8em;
    width: 50px;

}

.eliminar {
    padding: 2px 8px;
    font-size: 0.8em;
    width: 50px;
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
.editar-cliente {
    border: 3px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    padding: 20px;
    max-width: 600px;
    margin: 40px auto;
    margin-top: 10%;
    margin-left: 50%;
}

h3 {
    text-align: center;
    font-size: 20px;
    margin-bottom: 15px;
    margin-top: 2px;
}

.form-group {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}

.form-group label {
    width: 130px;
    margin-right: 10px;
}

.editar-cliente button {
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

.editar-cliente button:hover {
    background-color: #45a049;
}

.mensaje-exito {
    display: inline-block;
    margin-left: 60%;
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

.error {
    color: red;
    font-size: 0.8rem;
    text-align: center;
    margin-top: -10px;
}
</style>