<template>
    <div>
        <NavBar />
    </div>
    <div class="container">
        <div class="ordenes-container">
            <div v-if="ordenesEncontradas.length > 0">
                <!-- Renderiza datos de las órdenes en una tabla -->
                <h3 class="mb-4">Datos de las Órdenes Encontradas</h3>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>N° Orden</th>
                            <th>Descripción</th>
                            <th>N° Presupuesto</th>
                            <th>Fecha</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="orden in ordenesEncontradas" :key="orden.id_orden">
                            <td>{{ orden.id_orden }}</td>
                            <td>{{ orden.estado }}</td>
                            <td>{{ orden.id_presupuesto }}</td>
                            <td>{{ orden.fecha }}</td>
                        </tr>
                    </tbody>
                </table>
                <!-- Botón para volver al formulario de búsqueda -->
                <button @click="volverAlFormulario" class="btn btn-secondary volver-btn">Volver al formulario de
                    búsqueda</button>
            </div>
            <div v-else>
                <div class="container-externo">
                    <div class="formulario-busqueda">
                        <h3 class="mb-4">Buscar Orden de Trabajo</h3>
                        <form @submit.prevent="buscarOrden" class="mb-4">
                            <div class="search-container">
                                <div class="input-group mb-3">
                                    <span class="input-group-text"><i class="bi bi-search"></i></span>
                                    <input v-model="estado" id="descripcion" class="form-control input-lg"
                                        placeholder="Ingrese el estado" />
                                </div>

                                <div class="button-container">
                                    <button type="submit" class="btn btn-primary btn-lg">Buscar</button>
                                    <button type="button" @click="limpiarCampos"
                                        class="btn btn-secondary btn-lg spaced-button">Limpiar</button>
                                </div>
                            </div>
                        </form>
                        <div v-if="campoVacio" class="alert alert-danger alert-sm mt-2">
                            <p> {{ campoVacio }}</p>
                        </div>
                        <!-- Mensaje cuando no se encuentra la orden -->
                        <div v-if="busquedaRealizada && !ordenesEncontradas.length" class="alert alert-danger mt-1"
                            style="font-size: 0.8rem; padding: 0.5rem 1rem;">
                            <p>{{ campoVacio || 'No se encontraron órdenes con ese estado.' }}</p>
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
            ordenesEncontradas: [],
            campoVacio: null,
            error: null,
            busquedaRealizada: false,
        };
    },

    methods: {
        limpiarCampos() {
            this.estado = '';
            this.ordenesEncontradas = [];
            this.campoVacio = null;
            this.error = null;
            this.busquedaRealizada = false;
        },

        volverAlFormulario() {
            // Limpiar los campos y restablecer las variables relacionadas con la búsqueda
            this.estado = '';
            this.ordenesEncontradas = [];
            this.campoVacio = null;
            this.error = null;
            this.busquedaRealizada = false;
        },

        buscarOrden() {
            if (this.estado.trim() === '') {
                this.campoVacio = 'Por favor, ingrese la descripción de la orden.';
                setTimeout(() => {
                    this.campoVacio = null;
                }, 3000);
                return;
            }

            this.$axios.get(`http://localhost:8080/ordenes/traerXestado/${this.estado}`)
                .then(response => {
                    if (response.data && response.data.length > 0) {
                        // Si hay datos en la respuesta, significa que se encontraron órdenes
                        this.ordenesEncontradas = response.data;
                        this.error = null;
                        this.busquedaRealizada = true;
                    } else {
                        // Si no hay datos en la respuesta, significa que no se encontraron órdenes
                        this.ordenesEncontradas = [];
                        this.error = null;
                        this.busquedaRealizada = true;
                    }
                })
                .catch(error => {
                    console.error('Error al buscar las órdenes:', error);
                    this.ordenesEncontradas = [];
                    this.error = 'Error al buscar las órdenes. Inténtelo de nuevo.';
                    this.busquedaRealizada = true;
                });
        }
    }
}
</script>

<style scoped>
/* Estilos del contenedor externo */
.container-externo {
    width: 500px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin-top: 40px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
    border-radius: 20px;
}

.formulario-busqueda {
    border-radius: 10px;
    padding: 20px 50px;
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
    padding: 10px 40px;
}

/* Estilos del contenedor interno */
.ordenes-container {
    max-width: 1200px;
    width: 100%;
    margin-left: 40%;
}

.ordenes-container table {
    width: 600px;
}

.ordenes-container .container-externo {
    margin-left: 20%;
}

/* Estilos del formulario y botones */
.input-lg {
    padding: 10px;
    font-size: 18px;
    width: 102%;
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

.alert-sm {
    font-size: 12px;
    padding: 0.25rem 0.5rem;
}
</style>
