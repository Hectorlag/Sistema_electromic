<template>
  <div>
    <NavBar />
  </div>
  <div class="listado-productos">
    <div v-if="mostrarTabla" class="listado-tabla">
      <h2 class="titulo">Listado de Productos</h2>
      <table class="table table-hover">
        <thead>
          <tr>
            <th>N°</th>
            <th>N° de Serie</th>
            <th>Tipo</th>
            <th>Marca</th>
            <th>Modelo</th>
            <th>Accesorios</th>
            <th>N° Cliente</th>
            <th>Nombre</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="producto in productosActivos" :key="producto.id">
            <td>{{ producto.id_producto }}</td>
            <td>{{ producto.num_serie }}</td>
            <td>{{ producto.tipo }}</td>
            <td>{{ producto.marca }}</td>
            <td>{{ producto.modelo }}</td>
            <td>
              <span v-if="producto.accesorios.length === 0">No hay accesorios</span>
              <span v-else>
                <i class="bi bi-tools"></i>
                <span>{{ producto.accesorios.join(' ') }}</span>
              </span>
            </td>

            <td>{{ producto.id_cliente }}</td>
            <td>{{ producto.nombre ? producto.nombre.join(' ') : '' }}</td>
            <td class="acciones">
              <button class="btn btn-primary editar" @click="editarProducto(producto)">
                <i class="bi bi-pencil-fill"></i> Editar
              </button>
              <button class="btn btn-danger eliminar" @click="eliminarProductoLogicamente(producto.id_producto)">
                <i class="bi bi-trash-fill"></i> Eliminar
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Formulario para editar un producto (se muestra solo cuando se selecciona "Editar") -->
    <div v-if="mostrarFormularioEdicion">
      <div v-if="productoAEditar" class="editar-producto">
        <h3>Editar Producto</h3>
        <div class="formulario-editar">
          <div class="form-group">
            <label>ID:</label>
            <input v-model="productoAEditar.id_producto" disabled />
          </div>
          <div class="form-group">
            <label>Número de Serie:</label>
            <input v-model="productoAEditar.num_serie" type="text" />
          </div>

          <div class="form-group">
            <label>Tipo:</label>
            <select v-model="productoAEditar.tipo" class="form-control" v-if="tiposOrdenados.length > 0">
              <option value="" disabled>Seleccione un tipo</option>
              <option v-for="tipo in tiposOrdenados" :key="tipo" :value="tipo">{{ tipo }}</option>
              <option value="Otro">Otro</option>
            </select>
            <input v-if="productoAEditar.tipo === 'Otro'" v-model="otroTipo" type="text"
              class="form-control otro-tipo-input" placeholder="Ingrese el tipo">
          </div>

          <div class="form-group">
            <label>Marca:</label>
            <input v-model="productoAEditar.marca" type="text" />
          </div>

          <div class="form-group">
            <label>Modelo:</label>
            <input v-model="productoAEditar.modelo" type="text" />
          </div>

          <!-- Accesorios -->
          <div class="form-group">
            <label>Accesorios:</label>
            <div class="accesorios-container">
              <!-- Primer accesorio -->
              <div class="accesorio-item">
                <input type="text" v-model="productoAEditar.accesorios[0]" class="accesorio-input">
              </div>
              <!-- Primer accesorio -->
              <div class="accesorio-item">
                <input type="text" v-model="productoAEditar.accesorios[1]" class="accesorio-input">
              </div>
              <!-- Tercer accesorio -->
              <div class="accesorio-item">
                <input type="text" v-model="productoAEditar.accesorios[2]" class="accesorio-input">
              </div>
            </div>
          </div>

          <!-- Permite al usuario seleccionar un cliente diferente -->
          <div class="form-group">
            <label for="cliente" class="form-label">Cliente</label>
            <div class="input-group">
              <select v-model="productoAEditar.cliente" id="cliente" class="form-control">
                <option v-for="cliente in clientes" :key="cliente.id_cliente" :value="cliente">
                  {{ cliente.nombre.join(' ') }}
                </option>
              </select>
            </div>
          </div>

          <div class="acciones">
            <button type="submit" @click="actualizarProducto" class="actualizar boton-verde">Actualizar</button>
            <button type="button" @click="cancelarEdicion" class="cancelar boton-rojo">Cancelar</button>
          </div>
          <div v-if="mensajeError" class="error">{{ mensajeError }}</div>
        </div>
      </div>
    </div>

    <!-- Msj de actualización exitosa -->
    <div v-if="mensajeExito" class="mensaje-exito">
      Modificación exitosa!!
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
      productos: [],
      productoAEditar: {
        id_producto: '',
        marca: '',
        modelo: '',
        num_serie: '',
        tipo: '',
        accesorios: [],
        cliente: {
          id_cliente: "",
          nombre: [],
        },
      },
      otroTipo: '', // Nuevo dato para manejar el tipo "Otro"
      tiposDisponibles: ["TV", "Celular", "Microondas", "Notebook", "Impresora",
        "Escáner", "Tablet", "Parlante", "Plancha", "Reproductor de música",
        "Auriculares", "Monitor de PC"],

      mensajeExito: false,
      clientes: [],
      mostrarFormularioEdicion: false,
      mostrarTabla: true,
      mensajeError: '',
    };
  },

  methods: {

    agregarAccesorio() {
      this.productoAEditar.accesorios.push('');
    },
    eliminarAccesorio(index) {
      this.productoAEditar.accesorios.splice(index, 1);
    },

    obtenerClientes() {
      this.$axios.get('http://localhost:8080/clientes/traer')
        .then(response => {
          this.clientes = response.data;
          // Almacena la lista de clientes en el almacenamiento local
          localStorage.setItem('clientes', JSON.stringify(response.data));
        })
        .catch(error => {
          console.error('Error al obtener clientes:', error);
        });
    },

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

    editarProducto(producto) {
      // Copia profunda del producto para evitar cambios no deseados
      this.productoAEditar = JSON.parse(JSON.stringify(producto));
      // Ajusta la propiedad a "nombre"
      this.productoAEditar.nombre = [];
      this.productoAEditar.accesorios = [...producto.accesorios]; // Inicializar los accesorios correctamente
      this.mostrarFormularioEdicion = true;
      this.mostrarTabla = false;

      // Selecciona el cliente actual en el campo de selección de clientes
      this.productoAEditar.cliente = this.clientes.find(cliente => cliente.id_cliente === producto.id_cliente);

      // Si el tipo actual no está incluido en las opciones disponibles, agrégalo
      if (!this.tiposDisponibles.includes(producto.tipo)) {
        this.tiposDisponibles.push(producto.tipo);
      }

      // Selecciona automáticamente el tipo actual y limpia el campo de otroTipo
      this.tipoSeleccionado = producto.tipo;
      this.otroTipo = '';
    },


    actualizarProducto() {
      // Si el tipo seleccionado es 'Otro', se asigna el valor del input otroTipo a productoAEditar.tipo
      if (this.productoAEditar.tipo === 'Otro') {
        this.productoAEditar.tipo = this.otroTipo;
      }
      // Validación de campos obligatorios
      if (!this.productoAEditar.marca || !this.productoAEditar.modelo || !this.productoAEditar.num_serie || !this.productoAEditar.tipo || !this.productoAEditar.cliente) {
        console.error('Todos los campos son obligatorios.');
        this.mensajeError = 'Los campos marca, modelo, num serie, tipo y cliente son obligatorios.';
        setTimeout(() => {
          this.mensajeError = '';
        }, 3000);
        return;
      }

      // Limpia los accesorios vacíos
      this.productoAEditar.accesorios = this.productoAEditar.accesorios.filter(accesorio => accesorio.trim() !== '');

      // Lógica para actualizar el producto
      this.$axios
        .put(
          `http://localhost:8080/productos/editar/${this.productoAEditar.id_producto}`,
          this.productoAEditar
        )
        .then(() => {
          // Actualización exitosa
          this.obtenerProductos();
          this.productoAEditar = null; // Limpiar el producto a editar después de la actualización
          this.otroTipo = ''; // Limpiar el campo otroTipo después de registrar el producto
          this.tipoSeleccionado = ''; // Limpiar el tipo seleccionado
          this.mensajeExito = true;
          setTimeout(() => {
            this.mensajeExito = false;
            this.mostrarTabla = true;
          }, 3000);
        })
        .catch((error) => {
          console.error("Error al actualizar el producto:", error);
          // Manejo de errores
        });

      // Obtener el nombre del cliente asociado
      const idClienteAsociado = this.productoAEditar.id_cliente;
      const clienteAsociado = this.clientes.find(cliente => cliente.id_cliente === idClienteAsociado);
      if (clienteAsociado) {
        console.log("Nombre del cliente actualizado:", clienteAsociado.nombre);
      }
    },

    cancelarEdicion() {
      // Cancela la edición y reinicia la variable de clienteAEditar
      this.productoAEditar = null;
      this.mostrarTabla = true;
    },

    eliminarProductoLogicamente(id) {
      const producto = this.productos.find((producto) => producto.id_producto === id);

      if (producto) {
        // Muestra la ventana emergente
        this.mostrarVentanaEmergente("¿Estás seguro de eliminar este producto?", () => {
          // Lógica para eliminar el producto si el usuario confirma
          // Actualiza el estado en el frontend
          producto.deleted = true;

          // Actualiza la lista de productos en el almacenamiento local
          localStorage.setItem("productos", JSON.stringify(this.productos));

          // Envía la solicitud para eliminar lógicamente en el backend
          this.$axios
            .delete(`http://localhost:8080/productos/borrar/${id}`)
            .then(() => {
              console.log("Producto eliminado lógicamente:", id);
              // Éxito, puedes mostrar un mensaje o hacer más acciones si es necesario
            })
            .catch((error) => {
              console.error(
                "Error al eliminar lógicamente el producto:",
                error
              );
              // Puede revertir el cambio en el frontend si es necesario
              producto.deleted = false;
              // También puede revertir el cambio en el almacenamiento local
              localStorage.setItem("productos", JSON.stringify(this.productos));
            });
        });
      }
    },

    eliminarProducto(id) {
      // Lógica para eliminar el producto
      var url = `http://localhost:8080/productos/borrar/${id}`;
      this.enviarSolicitud('DELETE', null, url, 'Producto eliminado')
        .then(() => {
          // Llama a obtenerProductos() para actualizar la lista después de eliminar un producto
          this.obtenerProductos();
        });
    },

    // Otros métodos como enviarSolicitud, show_alerta y mostrarVentanaEmergente
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
  productosActivos() {
    return this.productos.filter((producto) => !producto.deleted);
  },
  tiposOrdenados() {
    return [...this.tiposDisponibles].sort();
  }
},


mounted() {
  // Intenta cargar productos desde el almacenamiento local
  const storedProducts = localStorage.getItem('productos');

  if (storedProducts) {
    // Si hay productos almacenados, úsalos en lugar de hacer una solicitud al servidor
    this.productos = JSON.parse(storedProducts);
  } else {
    // Si no hay productos almacenados, obtén la lista del servidor
    this.obtenerProductos();
  }
  this.obtenerClientes();
},

};
</script>

<style scoped>
.listado-productos {
  margin-top: 60px;
}

.listado-tabla {
  width: 1200px;
  margin-bottom: 40px;
}

.editar-producto {
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
  padding: 10px;
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


/* Estilos para la celda de acciones */
.acciones {
  text-align: center;
  margin-top: 25px;
}

/* Estilos para los botones editar y eliminar */
.acciones button {
  display: inline-block;
  width: 80px;
  padding: 3px;
  margin: 3px;
}

/* Estilos para los botones editar y eliminar */
.editar {
  padding: 1px 5px;
  font-size: 0.7em;
  width: auto;

}

.eliminar {
  padding: 1px 5px;
  font-size: 0.7em;
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
.editar-producto {
  border: 3px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  max-width: 500px;
  margin: 40px auto;
  margin-top: 8%;
  margin-left: 50%;
}

h3 {
  text-align: center;
  font-size: 20px;
  margin-bottom: 15px;
  margin-top: 2px;
}

.formulario-editar {
  width: 450px;
  margin: 0 auto;
}

h3 {
  text-align: center;
  font-size: 22px;
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.form-group label {
  display: inline-block;
  width: 30%;
  margin-bottom: 2px;
}

.form-group input {
  width: 70%;
  padding: 2px;
  box-sizing: border-box;
  margin-bottom: 8px;
  border: 1px solid #ddd;
}

.editar-producto button {
  background-color: #4caf50;
  color: white;
  padding: 4px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  margin-right: 35px;
  border: 1px solid #4caf50;
  margin-bottom: 22px;
}

.editar-producto button:hover {
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
  text-align: center;
}

.acciones button:last-child {
  margin-right: 0;
}

.acciones button.cancelar {
  background-color: #e53935;
  color: white;
  color: white;
  padding: 5px 12px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  border: 1px solid #4caf50;
  margin-bottom: 22px;
}

.error {
  color: red;
  font-size: 0.8rem;
  text-align: center;
  margin-top: -15px;
}

.accesorio-item {
  margin-bottom: -20px;
}

/* Estilos para los inputs de accesorios */
.accesorio-input {
  padding: 14px;
  font-size: 0.8em;
  /* Ajusta el tamaño de la fuente del input de accesorio */
  margin-right: 22px;
}

.acciones2 {
  text-align: center;
  margin-top: 20px;
}

.acciones2 button {
  width: 26%;
  padding: 6px;
  margin-right: 10px;
}

/* Estilos para los accesorios */
.accesorios-container {
  display: flex;
  justify-content: flex-end;
  /* Alinea los accesorios a la derecha */
}

.accesorio-item {
  margin-left: 1px;
  margin-bottom: 1px;
  width: 115px;
}

/* Ajusta el margen izquierdo del primer accesorio */
.accesorio-item:first-child {
  margin-left: 55px;
}

.form-group input.form-control,
.form-group select.form-control {
  width: 70%;
  /* Ajusta el ancho del input/select según sea necesario */
  padding: 6px 12px;
  /* Ajusta el relleno del input/select */
  font-size: 14px;
  /* Ajusta el tamaño de la fuente */
  border: 1px solid #ced4da;
  /* Ajusta el estilo del borde */
  border-radius: 4px;
  /* Ajusta el radio de borde */
  margin-right: 5px;
}

/* Estilo para el input "Otro" */
.form-group input.form-control[type="text"][placeholder="Ingrese el tipo"] {
  width: 70%;
  /* Ajusta el ancho del input según sea necesario */
  padding: 4px 8px;
  /* Ajusta el relleno del input */
  font-size: 14px;
  /* Ajusta el tamaño de la fuente */
  border: 1px solid #ced4da;
  /* Ajusta el estilo del borde */
  border-radius: 4px;
  margin-left: 3px;
}
</style>