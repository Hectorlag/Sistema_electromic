<template>
    <div>
        <NavBar />
    </div>
    <div class="formulario-container">
        <h1>Registrar Presupuesto</h1>
        <table class="table">
            <tbody>
                <tr>
                    <td class="form-label" style="width: 200px;">Descripción:</td>
                    <td style="width: 800px;">
                        <textarea v-model="nuevoPresupuesto.descripcion" class="form-control form-control-lg"
                            style="width: 100%;"></textarea>
                    </td>
                </tr>
                <tr>
                    <td class="form-label">Fecha:</td>
                    <td>
                        <input v-model="nuevoPresupuesto.fecha" type="date" class="form-control form-control-lg" />
                    </td>
                </tr>
                <tr>
                    <td class="form-label">Aprobado:</td>
                    <td>
                        <div class="form-check form-check-inline">
                            <input v-model="nuevoPresupuesto.aprobado" class="form-check-input" type="radio"
                                id="aprobadoSi" value="true" />
                            <label class="form-check-label" for="aprobadoSi">Sí</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input v-model="nuevoPresupuesto.aprobado" class="form-check-input" type="radio"
                                id="aprobadoNo" value="false" />
                            <label class="form-check-label" for="aprobadoNo">No</label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="form-label">Cliente:</td>
                    <td>
                        <select v-model="nuevoPresupuesto.cli" @change="actualizarCliente" class="form-control">
                            <option v-for="cliente in clientes" :key="cliente.id_cliente" :value="cliente">
                            {{ cliente.nombre.slice(-1)[0] }} {{ cliente.nombre.slice(0, -1).join(' ') }}
                            </option>
                        </select>
                        <div v-if="nuevoPresupuesto.cli.length > 0" class="cliente-seleccionado">
                            <p>Cliente seleccionado:</p>
                            <ul>
                                <li v-for="cliente in nuevoPresupuesto.cli" :key="cliente.id_cliente">
                                    ID: {{ cliente.id_cliente }} -- Nombres: {{ cliente.nombre.join(', ') }}
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>Productos del Cliente</td>
                    <td class="form-label">
                        <select v-model="productoSeleccionado" class="form-control">
                            <option value="">Seleccione un producto</option>
                            <option v-for="producto in productosDelCliente" :key="producto.id_producto"
                                :value="producto">
                                N°:{{ producto.id_producto }} - {{ producto.marca }} - {{ producto.tipo }}
                            </option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="form-label">Importe</td>
                    <td>
                        <input v-model="nuevoPresupuesto.importe" type="number" min="0" class="form-control" />
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="d-grid gap-2">
            <button type="button" @click="registrarPresupuesto"
                class="btn btn-success btn-sm gradient-custom-4 text-body">
                Registrar
            </button>
        </div>
        <!-- Mensaje de error -->
        <div v-if="mensajeError" class="error">{{ mensajeError }}</div>

        <!-- Mensaje de confirmación -->
        <div v-if="registroExitoso" class="confirmacion">
            ¡Registro exitoso!
        </div>
    </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';

export default {
    components: {
        NavBar
    },

    data() {
        return {
            nuevoPresupuesto: {
                descripcion: '', fecha: '', aprobado: false,
                cli: { id_cliente: "" },
                producto: { id_producto: "" },
                importe: ''
            },
            productoSeleccionado: '', // Propiedad para almacenar el ID del producto seleccionado
            registroExitoso: false,
            clientes: [],
            mensajeError: '',
            productosDelCliente: [],
        };
    },
    methods: {

        obtenerPresupuestos() {
            console.log("Iniciando obtenerPresupuestos");
            this.$axios.get("http://localhost:8080/presupuestos/traerconclientes")
                .then((response) => {
                    console.log("Respuesta de la API:", response.data);
                    this.presupuestos = response.data.map(presupuesto => ({ ...presupuesto, deleted: false }));
                    localStorage.setItem("presupuestos", JSON.stringify(response.data));
                })
                .catch((error) => {
                    console.error("Error al obtener presupuestos:", error);
                });
        },

        obtenerClientes() {
            this.$axios.get('http://localhost:8080/clientes/traer')
                .then(response => {
                    console.log('Respuesta de la API:', response.data);
                    this.clientes = response.data;
                    // Almacena la lista de clientes en el almacenamiento local
                    localStorage.setItem('clientes', JSON.stringify(response.data));
                })
                .catch(error => {
                    console.error('Error al obtener clientes:', error);
                });
        },

        actualizarCliente() {
            // Verifica si se ha seleccionado un cliente
            if (this.nuevoPresupuesto.cli && this.nuevoPresupuesto.cli.id_cliente) {
                // Obtiene los productos asociados al cliente seleccionado
                this.$axios.get(`http://localhost:8080/productos/uncliente/${this.nuevoPresupuesto.cli.id_cliente}`)
                    .then((response) => {
                        console.log("Respuesta del servidor:", response);
                        if (response.status === 200) {
                            if (Array.isArray(response.data) && response.data.length > 0) {
                                console.log("Productos del cliente:", response.data);
                                this.productosDelCliente = response.data;
                                this.productoSeleccionado = ''; // Limpia la selección actual
                            } else if (response.data && typeof response.data === 'string') {
                                // Si el servidor devuelve un mensaje de error
                                console.error("Error del servidor:", response.data);
                                // Muestra mensaje de error al usuario
                                this.productosDelCliente = [];
                                this.productoSeleccionado = '';
                                this.mensajeError = response.data;
                                setTimeout(() => {
                                    this.mensajeError = '';
                                }, 3000);
                            }
                        }
                    })
                    .catch((error) => {
                        console.error("Error al obtener productos del cliente:", error);
                        // Muestra mensaje de error al usuario
                        this.productosDelCliente = [];
                        this.productoSeleccionado = '';
                        this.mensajeError = "Error al obtener productos del cliente.";

                    });
            } else {
                this.productosDelCliente = []; // Limpia la lista de productos si no se ha seleccionado un cliente
                this.productoSeleccionado = ''; // Limpia el input si no se ha seleccionado un cliente
            }
        },

        // Método para seleccionar un producto y mostrar su información en el input
        seleccionarProducto(producto) {
            this.productoSeleccionado = `ID: ${producto.id_producto} - Marca: ${producto.marca} - Número de Serie: ${producto.num_serie}`;
        },

        registrarPresupuesto() {
            // Valida del lado del cliente
            if (!this.nuevoPresupuesto.descripcion || !this.nuevoPresupuesto.fecha || !this.productoSeleccionado) {
                console.error('Los campos descripcion, fecha y cliente son obligatorios.');
                this.mensajeError = 'Los campos descripcion, fecha, cliente y producto son obligatorios.';
                setTimeout(() => {
                    this.mensajeError = '';
                }, 3000);
                return;
            }

            // Verifica si la fecha ingresada es anterior a la fecha actual
            const fechaIngresada = new Date(this.nuevoPresupuesto.fecha);
            const fechaActual = new Date();

            if (fechaIngresada.setDate(fechaIngresada.getDate() + 1) <= fechaActual) {
                console.error('La fecha ingresada no puede ser anterior a la fecha actual.');
                this.mensajeError = 'La fecha ingresada no puede ser anterior a la fecha actual.';
                setTimeout(() => {
                    this.mensajeError = '';
                }, 3000);
                return;
            }

            // Asigna el ID del producto seleccionado al campo producto.id_producto en nuevoPresupuesto
            this.nuevoPresupuesto.producto.id_producto = this.productoSeleccionado.id_producto;
            console.log(this.nuevoPresupuesto.producto.id_producto)



            // Convierte el objeto proxy a un objeto plano
            const presupuestoPlano = JSON.parse(JSON.stringify(this.nuevoPresupuesto));

            // Lógica para registrar un nuevo presupuesto
            this.$axios.post('http://localhost:8080/presupuestos/crear', presupuestoPlano)
                .then(() => {
                    this.obtenerPresupuestos();
                    this.nuevoPresupuesto = {
                        descripcion: '', fecha: '', aprobado: false,
                        cli: { id_cliente: "" },
                        producto: { id_producto: '' },
                        importe: null
                    };

                    this.mensajeError = '';
                    // Muestra el mensaje de confirmación
                    this.registroExitoso = true;
                    // Limpia el mensaje después de unos segundos
                    setTimeout(() => {
                        this.registroExitoso = false;
                    }, 3000);
                })
                // Manejar errores aquí
                .catch(error => {
                    console.error('Error al agregar el presupuesto:', error);
                    console.log('Respuesta del servidor:', error.response);
                    console.log('Data del error:', error.data);
                    console.log('Datos enviados desde Vue.js:', presupuestoPlano);
                    console.log('Configuración de la solicitud:', error.config);
                });
        }
    },
    created() {
        this.obtenerClientes();
        this.obtenerPresupuestos();
    },
}
</script>

<style scoped>
/* Estilos del formulario de presupuesto */

.gradient-custom-3 {
    background: #84fab0;

    background: -webkit-linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
    background: linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
}

.gradient-custom-4 {

    /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
    background: linear-gradient(to right, rgba(132, 250, 176, 1), rgba(143, 211, 244, 1));
}

/* Añadido: Estilos para espaciar y centrar el formulario */
.mb-1 {
    margin-bottom: 1.1rem;
}

.text-uppercase {
    text-transform: uppercase;
}

.formulario-container {
    border: 3px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    padding: 20px;
    max-width: 400px;
    margin: 110px -200px;
    margin-top: 90px;
}

.form-control-lg {
    margin-bottom: 7px;
}

.cliente-seleccionado li {
    margin-bottom: 3px;
}

h1 {
    text-align: center;
    font-size: 22px;
    margin-bottom: 18px;
}

/* Añadido: Estilo para la tabla */
.table {
    width: 100%;
}

.form-check-inline {
    margin-right: 60px;
}

.d-grid {
    padding: 2px 60px 30px 60px;
    margin-bottom: -30px;
}

.error {
    color: red;
    font-size: 0.8rem;
    text-align: center;
    margin-top: 1px;
}

.confirmacion {
    background-color: #d4edda;
    color: #155724;
    padding: 10px;
    border: 1px solid #c3e6cb;
    border-radius: 5px;
    margin-top: 2px;
    text-align: center;
}

</style>
