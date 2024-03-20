<template>
    <div>
        <NavBar />
    </div>
    <div class="container">
        <div class="presupuestos-container">
            <div v-if="presupuestoEncontrado" class="tabla-exterior">
                <!-- Renderizar datos del presupuesto en una tabla con estilo -->
                <h3 class="mb-4">Datos del Presupuesto Encontrado</h3>
                <table class="table table-bordered">
                    <!-- Encabezados de la tabla -->
                    <thead>
                        <tr>
                            <th>N° Presupuesto</th>
                            <th>Descripción</th>
                            <th>Fecha</th>
                            <th>Aprobado</th>
                            <th>N° Cliente</th>
                            <th>Nombre Cliente</th>
                            <th>Importe</th>
                        </tr>
                    </thead>
                    <!-- Contenido de la tabla -->
                    <tbody>
                        <tr>
                            <!-- Celdas con los datos del presupuesto -->
                            <td>{{ presupuestoEncontrado.id_presupuesto }}</td>
                            <td>{{ presupuestoEncontrado.descripcion }}</td>
                            <td>{{ presupuestoEncontrado.fecha }}</td>
                            <td>{{ presupuestoEncontrado.aprobado ? 'Sí' : 'No' }}</td>
                            <td>{{ presupuestoEncontrado.id_cliente }}</td>
                            <td>{{ presupuestoEncontrado.nombre.join(' ') }}</td>
                            <td>{{ presupuestoEncontrado.importe }}</td>
                        </tr>
                    </tbody>
                </table>
                <!-- Botón para volver al formulario de búsqueda -->
                <button @click="volverAlFormulario" class="btn btn-secondary volver-btn">Volver al formulario de
                    búsqueda</button>

            </div>
            <div v-else>
                <!-- Formulario de búsqueda -->
                <div class="container-externo">
                    <div class="formulario-busqueda">
                        <h3 class="mb-3">Buscar Presupuesto</h3>
                        <form @submit.prevent="buscarPresupuesto" class="mb-4">
                            <div class="search-container">
                                <!-- Campo de selección para el cliente -->
                                <div class="mb-3">
                                    <label for="id_cliente" class="form-label">Cliente</label>
                                    <select v-model="id_cliente" id="id_cliente" class="form-control input-lg">
                                        <option v-for="cliente in clientes" :key="cliente.id_cliente"
                                            :value="cliente.id_cliente">
                                            {{ cliente.nombre.slice(-1)[0] }} {{ cliente.nombre.slice(0, -1).join(' ') }}
                                        </option>
                                    </select>
                                </div>
                                <!-- Campo de fecha -->
                                <div class="mb-3">
                                    <label for="fecha" class="form-label">Fecha</label>
                                    <input v-model="fecha" id="fecha" class="form-control input-lg" type="date" />
                                </div>
                                <!-- Botones de búsqueda y limpieza -->
                                <div class="button-container">
                                    <button type="submit" class="btn btn-primary btn-lg">Buscar</button>
                                    <button type="button" @click="limpiarCampos"
                                        class="btn btn-secondary btn-lg spaced-button">Limpiar</button>
                                </div>
                            </div>
                        </form>
                        <!-- Manejo de mensajes de error o éxito -->
                        <div v-if="campoVacio" class="alert alert-danger mt-2"
                            style="font-size: 0.8rem; padding: 0.5rem 1rem;">
                            <p>{{ campoVacio }}</p>
                        </div>
                        <div v-if="busquedaRealizada && !presupuestoEncontrado" class="alert alert-danger mt-1"
                            style="font-size: 0.8rem; padding: 0.5rem 1rem;">
                            <p>{{ campoVacio || 'No se encontró ningún presupuesto con esos criterios.' }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';

export default {
    components: {
        NavBar,
    },
    data() {
        return {
            id_cliente: '',
            fecha: '',
            presupuestoEncontrado: null,
            campoVacio: null,
            error: null,
            busquedaRealizada: false,
            clientes: [],
        };
    },

    methods: {
        limpiarCampos() {
            this.id_cliente = '';
            this.fecha = '';
            this.presupuestoEncontrado = null;
            this.campoVacio = null;
            this.error = null;
            this.busquedaRealizada = false;
        },

        volverAlFormulario() {
            // Limpiar los campos y restablecer las variables relacionadas con la búsqueda
            this.id_cliente = '';
            this.fecha = '';
            this.presupuestoEncontrado = null;
            this.campoVacio = null;
            this.error = null;
            this.busquedaRealizada = false;
        },
        obtenerClientes() {
            this.$axios.get("http://localhost:8080/clientes/traerXidYnombre")
                .then((response) => {
                    this.clientes = response.data;
                })
                .catch((error) => {
                    console.error("Error al obtener clientes:", error);
                });
        },


        buscarPresupuesto() {
            if (this.id_cliente === '' || this.fecha === '') {
                this.campoVacio = 'Por favor, ingrese el cliente y la fecha.';
                setTimeout(() => {
                    this.campoVacio = null;
                }, 3000);
                return;
            }

            this.$axios.get(`http://localhost:8080/presupuestos/traerxfecha/${this.id_cliente}/${this.fecha}`)
                .then(response => {
                    if (response.data) {
                        // Si hay datos en la respuesta, significa que se encontró un presupuesto
                        this.presupuestoEncontrado = response.data;
                        this.error = null;
                        this.busquedaRealizada = true;
                    } else {
                        // Si no hay datos en la respuesta, significa que no se encontró el presupuesto
                        this.presupuestoEncontrado = null;
                        this.error = null;
                        this.busquedaRealizada = true;
                    }
                })
                .catch(error => {
                    console.error('Error al buscar el presupuesto:', error);
                    this.presupuestoEncontrado = null;
                    this.error = 'Error al buscar el presupuesto. Inténtelo de nuevo.';
                    this.busquedaRealizada = true;
                });
        },
        volver() {
            this.limpiarCampos();
        }
    },

    mounted() {
        this.obtenerClientes(); // Llama a la función para cargar la lista de clientes al montar el componente
    },

}
</script>


<style scoped>
/* Estilos del contenedor externo */
.container-externo {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin-top: 70px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
    border-radius: 20px;
    margin-right: -250px;

}

.formulario-busqueda {
    border-radius: 10px;
    padding: 10px 30px;
    max-width: 600px;
    width: 100%;
}

h3 {
    font-size: 26px;
    text-align: center;
}

/* Estilos del contenedor de entrada y botones */
.search-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 8px 40px;
}

/* Estilos del campo de fecha */
#fecha {
    padding: 10px;
    font-size: 18px;
    width: calc(145% - 80px);
    /* Se ajusta el ancho para dejar espacio para el padding del contenedor */
    box-sizing: border-box;
    margin-bottom: 15px;
    /* Espacio inferior entre campos de entrada */
    margin-right: 40px;
    /* Margen derecho para alinear con los otros inputs */
}

/* Estilos del formulario y botones */
.input-lg {
    padding: 10px;
    font-size: 14px;
    width: 100%;
    /* Modificado para ocupar el ancho completo */
    box-sizing: border-box;
}

.btn-lg {
    padding: 6px 30px;
    font-size: 14px;
    margin-top: 20px;
}

.btn-primary,
.btn-secondary {
    margin-top: 10px;
    width: 100%;
}

.tabla-exterior{
    margin-left: 30%;
}

/* Estilos de la tabla */
.table {
    width: 100%;
    margin-top: 20px;
    border-collapse: collapse;
}

.table th,
.table td {
    border: 1px solid #ddd;
    padding: 18px;
    text-align: left;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.table th {
    background-color: #f2f2f2;
}

.table tbody tr:nth-child(even) {
    background-color: #f9f9f9;
}

/* Estilos de los botones */
.button-container {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 20px;
}

.volver-btn {
  transform: translateX(250px) scale(0.9);
  width: 300px; 
}


</style>