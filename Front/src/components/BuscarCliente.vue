<template>
  <div>
    <NavBar />
  </div>
  <div class="container">
    <div class="clientes-container">
      <div v-if="clienteEncontrado">
        <!-- Contenedor para los datos del cliente y la lista de productos -->
        <div class="cliente-productos-container">
          <!-- Contenedor para los datos del cliente -->
          <div class="cliente-container">

            <table class="table table-bordered horizontal-table">
              <h3 class="mb-3">Cliente</h3>
              <tbody>
                <tr>
                  <th>N° Cliente</th>
                  <td>{{ clienteEncontrado.id_cliente }}</td>
                </tr>
                <tr>
                  <th>Nombre Completo</th>
                  <td>{{ clienteEncontrado.nombreCompleto.join(' ') }}</td>
                </tr>
                <tr>
                  <th>Dirección</th>
                  <td>{{ clienteEncontrado.direccion }}</td>
                </tr>
                <tr>
                  <th>Teléfonos</th>
                  <td>{{ clienteEncontrado.telefono.join(' ') }}</td>
                </tr>
                <!-- Agrega más filas según la estructura de tu objeto cliente -->
              </tbody>
            </table>

          </div>

          <!-- Contenedor para la lista de productos del cliente -->
          <div class="productos-container">
            <h3 class="mb-3">Lista de Productos</h3>
            <table class="table table-bordered horizontal-table">
              <thead>
                <tr>
                  <th>N°</th>
                  <th>Marca</th>
                  <th>Modelo</th>
                  <th>Número de Serie</th>
                  <th>Tipo</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="producto in clienteEncontrado.listaproductos" :key="producto.id">
                  <td>{{ producto.id }}</td>
                  <td>{{ producto.marca }}</td>
                  <td>{{ producto.modelo }}</td>
                  <td>{{ producto.num_serie }}</td>
                  <td>{{ producto.tipo }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <!-- Botón para volver al formulario de búsqueda -->
        <button @click="volverAlFormulario" class="btn btn-secondary volver-btn">Volver al formulario de
          búsqueda</button>
      </div>
      <div v-else>
        <div class="container-externo">
          <div class="formulario-busqueda">
            <h3 class="mb-4">Buscar Cliente</h3>
            <form @submit.prevent="buscarCliente" class="mb-4">
              <div class="search-container">
                <div class="input-group mb-3">
                  <span class="input-group-text"><i class="bi bi-search"></i></span>
                  <input v-model="cuil" id="cuil" class="form-control input-lg" placeholder="Ingrese el DNI" />
                </div>

                <div class="button-container">
                  <button type="submit" class="btn btn-primary btn-lg">Buscar</button>
                  <button type="button" @click="limpiarCampos"
                    class="btn btn-secondary btn-lg spaced-button">Limpiar</button>
                </div>
              </div>
            </form>


            <div v-if="campoVacio" class="alert alert-danger mt-2" style="font-size: 0.8rem; padding: 0.5rem 1rem;">
              <p>{{ campoVacio }}</p>
            </div>

            <!-- Mensaje cuando no se encuentra el cliente -->
            <div v-if="busquedaRealizada && !clienteEncontrado" class="alert alert-danger mt-2"
              style="font-size: 0.8rem; padding: 0.5rem 1rem;">
              <p>{{ campoVacio || 'No se encontró ningún cliente con ese DNI.' }}</p>
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
      cuil: '',
      clienteEncontrado: null,
      campoVacio: null,
      error: null,
      busquedaRealizada: false,
    };
  },

  methods: {
    limpiarCampos() {
      this.cuil = '';
      this.clienteEncontrado = null;
      this.campoVacio = null;
      this.error = null;
      this.busquedaRealizada = false;
    },

    volverAlFormulario() {
      // Limpiar los campos y restablecer las variables relacionadas con la búsqueda
      this.cuil = '',
        this.clienteEncontrado = null;
      this.campoVacio = null;
      this.error = null;
      this.busquedaRealizada = false;
    },

    buscarCliente() {
      if (this.cuil.trim() === '') {
        this.campoVacio = 'Por favor, ingrese el DNI del cliente.';
        setTimeout(() => {
          this.campoVacio = null;
        }, 3000);
        return;
      }

      this.$axios.get(`http://localhost:8080/clientes/traerxcuil/${this.cuil}`)
        .then(response => {
          if (response.data) {
            this.clienteEncontrado = response.data;
            this.error = null;
            this.busquedaRealizada = true;
          } else {
            this.clienteEncontrado = null;
            this.error = null;
            this.busquedaRealizada = true;
            this.campoVacio = 'No se encontró ningún cliente con ese DNI.';
            setTimeout(() => {
              this.campoVacio = null;
            }, 3000);
          }
        })
        .catch(error => {
          console.error('Error al buscar el cliente:', error);
          this.clienteEncontrado = null;
          this.error = 'Error al buscar el cliente. Inténtelo de nuevo.';
          this.busquedaRealizada = true;
        });
    },

    volver() {
      this.limpiarCampos();
    },
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
.clientes-container {
  max-width: 1200px;
  width: 100%;
  margin-left: 50%;
  display: flex;
  flex-direction: column;
  margin-top: 40px;

}

/* Estilos del contenedor del cliente */
.cliente-container {
  display: flex;
  flex-direction: row;

}

/* Estilos del contenedor de productos */
.productos-container {
  margin-top: 18px;
}

.cliente-productos-container {
  display: flex;
  justify-content: space-between;
  margin-left: -80px;
}

.cliente-container,
.productos-container {
  flex-basis: 48%;
  padding: 20px
}

/* Estilos del formulario y botones */
.input-lg {
  padding: 16px;
  font-size: 18px;
  width: 100%;
  box-sizing: border-box;
}

.btn-lg {
  padding: 10px 30px;
  font-size: 16px;
  margin-top: 10px;
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
  /* Bordes redondeados */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* Sombra suave */
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
  /* Centra los botones */
}

.volver-btn {
  width: auto;
  display: block;
  margin-left: 25%;
  margin-right: auto;
}
</style>
