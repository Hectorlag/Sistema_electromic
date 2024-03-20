<template>
  <div>
    <NavBar />
  </div>

  <div class="formulario-container">
    <h1>Registrar producto</h1>
    <form>
      <div class="form-group">
        <label for="marca">Marca: </label>
        <input v-model="nuevoProducto.marca" type="text" id="marca" class="form-control" />
      </div>

      <div class="form-group">
        <label for="modelo">Modelo: </label>
        <input v-model="nuevoProducto.modelo" type="text" id="modelo" class="form-control" />
      </div>

      <div class="form-group">
        <label for="num_serie">Número de Serie: </label>
        <input v-model="nuevoProducto.num_serie" type="text" id="num_serie" class="form-control" />
      </div>

      <div class="form-group">
        <label for="tipo">Tipo: </label>
        <select v-model="tipoSeleccionado" @change="actualizarTipoSeleccionado" id="tipo" class="form-control">
          <option value="" disabled selected>Seleccione un tipo</option>
          <option v-for="tipo in tiposOrdenados" :key="tipo" :value="tipo">{{ tipo }}</option>
          <option value="Otro">Otro</option>
        </select>

        <input v-if="tipoSeleccionado === 'Otro'" v-model="otroTipo" type="text" class="form-control"
          placeholder="Ingrese el tipo">
      </div>

      <div class="form-group">
        <label for="accesorio">Accesorio: </label>
        <!-- Divido el campo accesorio en dos inputs -->
        <div class="input-group">
          <input v-model="nuevoProducto.accesorios[0]" type="text" id="accesorio1" class="form-control"
            placeholder="Accesorio 1" />
          <input v-model="nuevoProducto.accesorios[1]" type="text" id="accesorio2" class="form-control"
            placeholder="Accesorio 2" />
        </div>
      </div>

      <div class="form-group">
        <label for="cliente">Cliente: </label>
        <select v-model="nuevoProducto.cliente" id="cliente" class="form-control">
          <option value="" disabled selected v-if="!nuevoProducto.cliente">Seleccione un cliente</option>
          <option v-for="cliente in clientes" :key="cliente.id_cliente" :value="cliente">
            {{ cliente.nombre.slice(-1)[0] }} {{ cliente.nombre.slice(0, -1).join(' ') }}
          </option>
        </select>
      </div>

      <div class="d-grid gap-2">
        <button type="button" @click="registrarProducto" class="btn btn-success btn-sm gradient-custom-4 text-body">
          Registrar
        </button>
      </div>

      <div v-if="mensajeError" class="error">{{ mensajeError }}</div>

      <!-- Mensaje de confirmación -->
      <div v-if="registroExitoso" class="confirmacion">
        ¡Registro exitoso!
      </div>
    </form>
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
      nuevoProducto: { marca: '', modelo: '', num_serie: '', tipo: '', accesorios: ['', ''], cliente: { id_cliente: "" } },
      otroTipo: '',
      tipoSeleccionado: '', // Nuevo dato para manejar el tipo seleccionado
      tiposDisponibles: ["TV", "Celular", "Microondas", "Notebook", "Impresora",
        "Escáner", "Tablet", "Parlante", "Plancha", "Reproductor de música",
        "Auriculares", "Monitor de PC"],

      registroExitoso: false,
      productos: [],
      clientes: [],
      mensajeError: '',
    };
  },
  computed: {
    tiposOrdenados() {
      return [...this.tiposDisponibles].sort();
    }
  },

  methods: {
    obtenerProductos() {
      console.log("Iniciando obtenerProductos");
      this.$axios.get("http://localhost:8080/productos/traerconcliente")
        .then((response) => {
          console.log("Respuesta de la API:", response.data);
          this.productos = response.data;

          // Almacena la lista de productos en el almacenamiento local
          localStorage.setItem("productos", JSON.stringify(response.data));
        })
        .catch((error) => {
          console.error("Error al obtener productos:", error);
        });
    },
    actualizarTipoSeleccionado() {
    this.nuevoProducto.tipo = this.tipoSeleccionado;
  },

    actualizarCliente() {
      // Imprimo el cliente seleccionado para asegurarte de que sea correcto
      console.log("Cliente seleccionado:", this.nuevoProducto.cliente);
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

    registrarProducto() {
      // Si el tipo seleccionado es 'Otro', se asigna el valor del input otroTipo a nuevoProducto.tipo
      if (this.tipoSeleccionado === 'Otro') {
        this.nuevoProducto.tipo = this.otroTipo;
      }

      // Valida del lado del cliente
      if (!this.nuevoProducto.marca || !this.nuevoProducto.modelo || !this.nuevoProducto.num_serie || !this.nuevoProducto.tipo) {
        console.error('Todos los campos son obligatorios.');
        this.mensajeError = 'Los campos marca, modelo, num serie, tipo y cliente son obligatorios .';
        setTimeout(() => {
          this.mensajeError = '';
        }, 3000);
        return;
      }

      // Asegúra de que el campo nombre sea siempre un array
      if (!Array.isArray(this.nuevoProducto.accesorios)) {
        this.nuevoProducto.accesorios = this.nuevoProducto.accesorios ? [this.nuevoProducto.accesorios] : [];
      }

      // Convierte el objeto proxy a un objeto plano
      const productoPlano = JSON.parse(JSON.stringify(this.nuevoProducto));

      // Lógica para registrar un nuevo producto
      this.$axios.post('http://localhost:8080/productos/crear', productoPlano)
        .then(() => {
          console.log("EL producto a registrar es:", productoPlano)
          this.obtenerProductos();
          this.nuevoProducto = { marca: '', modelo: '', num_serie: '', tipo: '', accesorios: ['', ''], cliente: { id_cliente: "" } };
          this.otroTipo = ''; // Limpiar el campo otroTipo después de registrar el producto
          this.tipoSeleccionado = ''; // Limpiar el tipo seleccionado
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
          console.error('Error al agregar el producto:', error);
          console.log('Respuesta del servidor:', error.response);
          console.log('Data del error:', error.data);
          console.log('Datos enviados desde Vue.js:', productoPlano);
          console.log('Configuración de la solicitud:', error.config);
        });
    }
  },

  created() {
    this.obtenerProductos();
    this.obtenerClientes();
  },

}
</script>

<style scoped>
/* Estilos para los gradientes de fondo */
.gradient-custom-3 {
  background: #84fab0;
  background: -webkit-linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
  background: linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
}

.gradient-custom-4 {
  background: linear-gradient(to right, rgba(132, 250, 176, 1), rgba(143, 211, 244, 1));
}

/* Estilos para espaciar y centrar el formulario */
.formulario-container {
  border: 3px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  max-width: 400px;
  margin: 100px -220px;
  padding-bottom: 3px;
}

h1 {
  text-align: center;
  font-size: 22px;
  margin-bottom: 10px;
  margin-top: 5px;
}

.error {
  color: red;
  font-size: 0.8rem;
  text-align: center;
  margin-top: -15px;
}

/* Estilos específicos para el formulario de registro */
.formulario-container form {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
}

.formulario-container .form-group {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 20px;
}

.formulario-container label {
  width: 70px;
  text-align: right;
  margin-right: 50px;
}

.formulario-container .form-control {
  flex: 1;
  margin-left: 10px;
}

.confirmacion {
  background-color: #d4edda;
  color: #155724;
  padding: 8px;
  border: 1px solid #c3e6cb;
  border-radius: 5px;
  margin-top: -5px;
  text-align: center;
}

.d-grid {
  padding: 8px 60px 15px 60px;
  margin-bottom: 10px;
}
.form-control::placeholder {
    font-size: 12px; 
}

</style>