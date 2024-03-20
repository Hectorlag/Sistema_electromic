<template>
    <div>
        <NavBar />
    </div>
    <div class="listado-ordenes">
    </div>
    <div v-if="mostrarTabla" class="listado-tabla">
        <h2 class="titulo">Listado de Ordenes de Trabajo</h2>
        <table class="table table-hover">
            <thead>
                <tr>
                    <th>N°</th>
                    <th>Estado</th>
                    <th>N° Presupuesto</th>
                    <th>Fecha</th>
                    <th>Cliente</th>
                    <th>Producto</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="orden in ordenesActivas" :key="orden.id">
                    <td>{{ orden.id_orden }}</td>
                    <td>{{ orden.estado }}</td>
                    <td>{{ orden.id_presupuesto }}</td>
                    <td>{{ orden.fecha }}</td>
                    <td>
                        <a href="#" @click="mostrarDetalleCliente(orden.id_cliente)">Ver detalle cliente</a>
                    </td>
                    <td>
                        <a href="#" @click="mostrarDetalleProducto(orden.id_producto)">Ver detalle producto</a>
                    </td>
                    <td class="acciones">
                        <button class="btn btn-primary editar" @click="editarOrdenDeTrabajo(orden)">
                            <i class="bi bi-pencil-fill"></i> Editar
                        </button>
                        <button class="btn btn-danger eliminar" @click="eliminarOrdenDeTrabajo(orden.id_orden)">
                            <i class="bi bi-trash-fill"></i> Eliminar
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <!-- Formulario para editar una orden de trabajo (se muestra solo cuando se selecciona "Editar") -->
    <div v-if="mostrarFormularioEdicion">
        <div v-if="ordenTrabajoAEditar" class="editar-orden-trabajo">
            <h3>Editar Orden de Trabajo</h3>
            <div class="formulario-edicion">
                <div class="form-group">
                    <label>ID:</label>
                    <input v-model="ordenTrabajoAEditar.id_orden" disabled />
                </div>
                <div class="form-group">
                    <label>Estado:</label>
                    <select v-model="ordenTrabajoAEditar.estado" id="estado" class="form-control">
                        <option value="En reparación">En reparación</option>
                        <option value="Para ingresar">Para ingresar</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Presupuesto:</label>
                    <input v-model="ordenTrabajoAEditar.presupuesto.id_presupuesto" disabled />
                </div>
                <div class="acciones">
                    <button type="submit" @click="actualizarOrdenTrabajo">Actualizar</button>
                    <button type="button" @click="cancelarEdicionOrdenTrabajo" class="cancelar">Cancelar</button>
                </div>
            </div>
        </div>
        <!-- Mensaje de actualización exitosa -->
        <div v-if="mensajeExito" class="mensaje-exito">
            Modificación exitosa!!
        </div>
        <!-- Mensaje de eliminación exitosa -->
        <div v-if="mensajeExitoEliminacion" class="mensaje-exito-eliminacion">
            Eliminación exitosa!!
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
            ordenTrabajoAEditar: { id_orden: '', estado: '', presupuesto: { id_presupuesto: "", fecha: "" } },
            mensajeExito: false,
            ordenesTrabajo: [],
            mostrarFormularioEdicion: false,
            mensajeExitoEliminacion: false,
            mostrarTabla: true,
            cliente: {},
            producto: {},
        };
    },
    methods: {

        obtenerOrdenesTrabajo() {
            console.log("Iniciando obtenerOrdenesTrabajo");
            this.$axios.get("http://localhost:8080/ordenes/traer")
                .then((response) => {
                    console.log("Listado de ordenes:", response.data);
                    this.ordenesTrabajo = response.data.map(orden => ({ ...orden, deleted: false }));
                    localStorage.setItem("ordenesTrabajo", JSON.stringify(response.data));
                })
                .catch((error) => {
                    console.error("Error al obtener órdenes de trabajo:", error);
                });
        },

        editarOrdenDeTrabajo(ordenTrabajo) {

            this.mostrarFormularioEdicion = true;
            this.ordenTrabajoAEditar.id_orden = ordenTrabajo.id_orden;
            this.ordenTrabajoAEditar.estado = ordenTrabajo.estado;
            this.ordenTrabajoAEditar.presupuesto = { id_presupuesto: ordenTrabajo.id_presupuesto };
            this.mostrarTabla = false;
        },
        actualizarOrdenTrabajo() {
            console.log('Orden de trabajo a actualizar:', this.ordenTrabajoAEditar);
            // Lógica para actualizar la orden de trabajo
            this.$axios
                .put(
                    `http://localhost:8080/ordenes/editar/${this.ordenTrabajoAEditar.id_orden}`,
                    this.ordenTrabajoAEditar
                )
                .then(() => {
                    this.obtenerOrdenesTrabajo();
                    this.ordenTrabajoAEditar = null; // Se limpia la orden de trabajo a editar después de la actualización
                    this.mensajeExito = true;
                    setTimeout(() => {
                        this.mensajeExito = false;
                        this.mostrarTabla = true;
                    }, 3000);

                })
                .catch((error) => {
                    console.error("Error al actualizar la orden de trabajo:", error);
                });
        },

        cancelarEdicionOrdenTrabajo() {
            // Cancela la edición y reinicia la variable de ordenTrabajoAEditar
            this.ordenTrabajoAEditar = null;
            this.mostrarFormularioEdicion = false;
            this.mostrarTabla = true;
        },

        eliminarOrdenDeTrabajo(id) {
            // Pregunta al usuario si está seguro antes de eliminar la orden de trabajo
            this.mostrarVentanaEmergente(`¿Estás seguro de eliminar la orden de trabajo ${id}?`, () => {
                this.$axios
                    .delete(`http://localhost:8080/ordenes/borrar/${id}`)
                    .then(() => {
                        console.log("Orden de trabajo eliminada:", id);
                        this.obtenerOrdenesTrabajo();
                        this.mensajeExitoEliminacion = true; // Muestra el mensaje de eliminación exitosa
                        setTimeout(() => {
                            this.mensajeExitoEliminacion = false; // Oculta el mensaje después de 3 segundos
                        }, 3000);

                        // Muestra un mensaje de éxito utilizando SweetAlert2
                        this.mostrarAlerta("¡Éxito!", "La orden de trabajo se ha eliminado correctamente.", "success");
                    })
                    .catch((error) => {
                        console.error("Error al eliminar la orden de trabajo:", error);
                        // Muestra un mensaje de error utilizando SweetAlert2
                        this.mostrarAlerta("Error", "Hubo un error al eliminar la orden de trabajo.", "error");
                    });
            });
        },

        // Método para mostrar una ventana emergente con SweetAlert2
        mostrarVentanaEmergente(mensaje, callback) {
            Swal.fire({
                title: mensaje,
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    callback();
                }
            });
        },

        // Método para mostrar una alerta utilizando SweetAlert2
        mostrarAlerta(titulo, mensaje, icono) {
            Swal.fire({
                title: titulo,
                text: mensaje,
                icon: icono,
                confirmButtonText: 'Aceptar'
            });
        },
        // Método para buscar los detalles del cliente por su ID
        buscarClientePorId(idCliente) {
            this.$axios.get(`http://localhost:8080/clientes/traer/${idCliente}`)
                .then((response) => {
                    // Aquí puedes manejar la respuesta y mostrar los detalles del cliente, por ejemplo, en un modal
                    console.log("Detalles del cliente:", response.data);
                    this.cliente = response.data;
                })
                .catch((error) => {
                    console.error("Error al obtener los detalles del cliente:", error);
                });
        },
     // Método para mostrar los detalles del cliente por su ID
        mostrarDetalleCliente(idCliente) {
            this.buscarClientePorId(idCliente);
            Swal.fire({
                title: 'Detalles del Cliente',
                html: `
          <p><strong>N°:</strong> ${this.cliente.id_cliente}</p>
          <p><strong>Nombre:</strong> ${this.cliente.nombre.slice(-1)[0]} ${this.cliente.nombre.slice(0, -1).join(' ')}</p>
          <p><strong>Email:</strong> ${this.cliente.email}</p>
          <p><strong>Teléfono:</strong> ${this.cliente.telefono[0]}</p>
        `,
                confirmButtonText: 'Aceptar',
                customClass: {
                    confirmButton: 'btn btn-primary'
                }
            });
        },
        mostrarDetalleProducto(idProducto) {
    // Primero, busca los detalles del producto por su ID
    this.buscarProductoPorId(idProducto)
        .then(() => {
            // Una vez que se completó la solicitud HTTP, muestra los detalles del producto
            Swal.fire({
                title: 'Detalles del Producto',
                html: `
                    <p><strong>N°: </strong>${this.producto.id}</p>
                    <p><strong>Tipo: </strong>${this.producto.tipo}</p>
                    <p><strong>Marca: </strong>${this.producto.marca}</p>
                    <p><strong>Modelo: </strong>${this.producto.modelo}</p>
                `,
                confirmButtonText: 'Aceptar',
                customClass: {
                    confirmButton: 'btn btn-primary'
                }
            });
        })
        .catch((error) => {
            console.error("Error al obtener los detalles del producto:", error);
            // En caso de error, muestra un mensaje de error
            Swal.fire({
                title: 'Error',
                text: 'Hubo un error al obtener los detalles del producto.',
                icon: 'error',
                confirmButtonText: 'Aceptar'
            });
        });
},

buscarProductoPorId(idProducto) {
    // Retorna una promesa para manejar la solicitud HTTP de manera asincrónica
    return new Promise((resolve, reject) => {
        this.$axios.get(`http://localhost:8080/productos/traer/${idProducto}`)
            .then((response) => {
                // Aquí puedes manejar la respuesta y mostrar los detalles del producto, por ejemplo, en un modal
                console.log("Detalles del producto:", response.data);
                this.producto = response.data;
                resolve(); // Resuelve la promesa una vez que se obtienen los detalles del producto
            })
            .catch((error) => {
                console.error("Error al obtener los detalles del producto:", error);
                reject(error); // Rechaza la promesa en caso de error
            });
    });
},


        

    },
    computed: {
        ordenesActivas() {
            return this.ordenesTrabajo.filter((orden) => !orden.deleted);
        },
    },

    mounted() {
        // Intenta cargar órdenes de trabajo desde el almacenamiento local
        const storedOrdenes = localStorage.getItem("ordenes");

        if (storedOrdenes) {
            // Si hay órdenes de trabajo almacenadas, úsalas en lugar de hacer una solicitud al servidor
            this.ordenesTrabajo = JSON.parse(storedOrdenes);
        } else {
            // Si no hay órdenes de trabajo almacenadas, obtén la lista del servidor
            this.obtenerOrdenesTrabajo();
        }
    },
}
</script>

<style scoped>
.listado-orden-trabajo {
    margin-top: 60px;
}

.listado-tabla {
    width: 1200px;
    margin-bottom: 40px;
}

.editar-orden-trabajo {
    width: 100%;
}

.titulo {
    background-color: #7399c2;
    color: white;
    padding: 5px;
    margin-top: 100px;
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
    padding: 17px;
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
    margin-top: 25px;
}

.acciones button {
    width: 45%;
    padding: 5px;
    margin-right: 8px;
    margin-top: 5%;
}

.editar {
    padding: 1px 4px;
    font-size: 0.6em;
    width: auto;
}

.eliminar {
    padding: 1px 4px;
    font-size: 0.6em;
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

/*-- Estilos para el formulario editar orden de trabajo*/
.editar-orden-trabajo {
    border: 3px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    padding: 20px;
    max-width: 400px;
    margin: 120px 210px;
    margin-left: 60%;
}

h3 {
    text-align: center;
    font-size: 20px;
    margin-bottom: 30px;
    margin-top: 5px;
}

.form-group {
    display: flex;
    align-items: center;
    margin-bottom: 25px;
}

.form-group label {
    width: 390px;
    margin-right: 5px;
}


.acciones button:last-child {
    margin-right: 0;
}

.form-group select {
    width: 190%;
    padding: 3px;
    box-sizing: border-box;
    margin-bottom: 10px;
    border: 1px solid #ddd;
}

.editar-orden-trabajo button {
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

.editar-orden-trabajo button:hover {
    background-color: #45a049;
}

.mensaje-exito {
    display: inline-block;
    margin-left: 150%;
    background-color: #d4edda;
    color: #155724;
    padding: 10px 30px;
    border: 1px solid #c3e6cb;
    border-radius: 5px;
    margin-top: 10px;
    text-align: center;
}

.acciones button.cancelar {
    background-color: #e53935;
    color: white;
}

.detalle {
    width: 20%;
    margin-top: 8%;
    /* Espacio superior */
    padding: 5px 10px;
    /* Espacio interno */
    border: 1px solid #ccc;
    /* Borde */
    border-radius: 5px;
    /* Bordes redondeados */
}

.cerrar-detalle {
    position: absolute;
    /* Posiciona el icono de manera absoluta */
    margin-left: 210px;
    cursor: pointer;
    /* Cambia el cursor al pasar sobre el icono */
    margin-top: -5px;
}


/* Estilos para el contenido de los detalles */
.detalle-content {
    /* Espacio entre elementos dentro del detalle */
    margin-bottom: 3px;
}

h4 {
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    margin-top: 15px;
}

.detalle-content p {
    font-size: 14px;
}

/* Estilos para los botones de ver detalles */
.botones-ver-detalle {
    margin-top: 10px;
    /* Espacio superior */
}
</style>
