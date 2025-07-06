<template>
    <div>
        <NavBar />
    </div>
    <div class="container">
        <div class="productos-container">
            <div v-if="productoEncontrado">
                <!-- Renderizar datos del producto en una tabla con estilo -->
                <h3 class="mb-4">Datos del Producto Encontrado</h3>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>N° Producto</th>
                            <th>Marca</th>
                            <th>Modelo</th>
                            <th>Número de Serie</th>
                            <th>Tipo</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>{{ productoEncontrado.id_producto }}</td>
                            <td>{{ productoEncontrado.marca }}</td>
                            <td>{{ productoEncontrado.modelo }}</td>
                            <td>{{ productoEncontrado.num_serie }}</td>
                            <td>{{ productoEncontrado.tipo }}</td>
                        </tr>
                    </tbody>
                </table>
                <!-- Botón para volver al formulario de búsqueda -->
                <button @click="volverAlFormulario" class="btn btn-secondary volver-btn">Volver al formulario de búsqueda</button>
            </div>
            <div v-else>
                <div class="container-externo">
                    <div class="formulario-busqueda">
                        <h2 class="mb-3">Buscar Producto</h2>
                        <form @submit.prevent="buscarProducto" class="mb-4">
                            <div class="search-container">
                                <div class="mb-3">
                                    <label for="marca" class="form-label">Marca:</label>
                                    <input v-model="marca" id="marca" class="form-control input-lg" placeholder="Ingrese la marca" />
                                </div>
                                <div class="mb-3">
                                    <label for="tipo">Tipo:</label>
                                    <select v-model="tipo" id="tipo" class="form-control">
                                        <option value="" disabled selected>Seleccione un tipo</option>
                                        <option v-for="tipo in tiposOrdenados" :key="tipo" :value="tipo">{{ tipo }}</option>
                                        <option value="Otro">Otro</option>
                                    </select>
                                    <input v-if="tipo === 'Otro'" v-model="otroTipo" type="text" class="form-control mt-2" placeholder="Ingrese otro tipo">
                                </div>
                                <div class="button-container">
                                    <button type="submit" class="btn btn-primary btn-lg">Buscar</button>
                                    <button type="button" @click="limpiarCampos" class="btn btn-secondary btn-lg spaced-button">Limpiar</button>
                                </div>
                            </div>
                        </form>
                        <div v-if="campoVacio" class="alert alert-danger mt-2">
                            <p>{{ campoVacio }}</p>
                        </div>
                        <!-- Mensaje cuando no se encuentra el producto -->
                        <div v-if="busquedaRealizada && !productoEncontrado" class="alert alert-danger mt-4">
                            <p>{{ campoVacio || 'No se encontró ningún producto con esa marca y tipo.' }}</p>
                        </div>
                    </div>
                </div>
            </div>
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
            marca: '',
            tipo: '',
            otroTipo: '',
            productoEncontrado: [],
            campoVacio: null,
            error: null,
            busquedaRealizada: false,
            tiposDisponibles: ["TV", "Celular", "Microondas", "Notebook", "Impresora", "Escáner", "Tablet", "Parlante", "Plancha", "Reproductor de música", "Auriculares", "Monitor de PC"],
        };
    },
    computed: {
        tiposOrdenados() {
            return [...this.tiposDisponibles].sort();
        },
    },
    methods: {
        limpiarCampos() {
            this.marca = '';
            this.tipo = '';
            this.otroTipo = '';
            this.productoEncontrado = null;
            this.campoVacio = null;
            this.error = null;
            this.busquedaRealizada = false;
        },
        volverAlFormulario() {
            // Limpiar los campos y restablecer las variables relacionadas con la búsqueda
            this.limpiarCampos();
        },
        buscarProducto() {
            if (this.marca.trim() === '' || this.tipo.trim() === '') {
                this.campoVacio = 'Por favor, ingrese la marca y el tipo del producto.';
                setTimeout(() => {
                    this.campoVacio = null;
                }, 3000);
                return;
            }

            let url = 'http://localhost:8080/productos/traerxmarcaytipo/';
            url += `${this.marca}/${this.tipo}`;

            this.$axios.get(url)
                .then(response => {
                    if (response.data) {
                        // Si hay datos en la respuesta, significa que se encontró un producto
                        this.productoEncontrado = response.data;
                        this.error = null;
                        this.busquedaRealizada = true;
                    } else {
                        // Si no hay datos en la respuesta, significa que no se encontró el producto
                        this.productoEncontrado = null;
                        this.error = null;
                        this.busquedaRealizada = true;
                        this.campoVacio = 'No se encontró ningún producto con esa marca y tipo.';
                        setTimeout(() => {
                            this.campoVacio = null;
                        }, 3000);
                    }
                })
                .catch(error => {
                    console.error('Error al buscar el producto:', error);
                    this.productoEncontrado = null;
                    this.error = 'Error al buscar el producto. Inténtelo de nuevo.';
                    this.busquedaRealizada = true;
                });
        },
    },
};
</script>
<style scoped>

.formulario-busqueda,
h2 {
    border-radius: 10px;
    padding: 20px 50px;
    max-width: 600px;
    width: 130%;
}

/* Estilos del contenedor de entrada y botones */
.search-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px 40px;
}

/* Estilos del contenedor interno */
.productos-container {
    max-width: 1200px;
    width: 100%;
    margin-left: 50%;
}

/* Estilos del formulario y botones */
.input-lg {
    padding: 14px;
    font-size: 18px;
    width: 100%;
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
    justify-content: center;
}

.volver-btn {
    margin-top: 20px;
    width: auto;
    display: block;
    margin-left: auto;
    margin-right: auto;
}

.alert {
    font-size: 0.8rem;
    padding: 0.25rem 0.5rem;
    max-width: 280px;
    margin-left: 30px;
}
</style>