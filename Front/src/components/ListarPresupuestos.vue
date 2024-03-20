<template>
  <div>
    <NavBar />
  </div>
  <div class="listado-presupuestos">
    <div v-if="mostrarTabla" class="listado-tabla">
      <h2 class="titulo">Listado de Presupuestos</h2>
      <table class="table table-hover">
        <thead>
          <tr>
            <th>N°</th>
            <th>Descripción</th>
            <th>Fecha</th>
            <th>Aprobado</th>
            <th>N°cliente</th>
            <th>Nombre</th>
            <th>Importe</th>
            <th>Marca producto</th>
            <th>N° serie</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="presupuesto in presupuestosActivos" :key="presupuesto.id">
            <td>{{ presupuesto.id_presupuesto }}</td>
            <td>{{ presupuesto.descripcion }}</td>
            <td>{{ presupuesto.fecha }}</td>
            <td>{{ presupuesto.aprobado ? 'Sí' : 'No' }}</td>
            <td>{{ presupuesto.id_cliente }}</td>
            <td>{{ presupuesto.nombre ? presupuesto.nombre.join(' ') : '' }}</td>
            <td>{{ presupuesto.importe }}</td>
            <td>{{ presupuesto.marca }}</td>
            <td>{{ presupuesto.num_serie }}</td>
            <td class="acciones">
              <button class="btn btn-primary editar" @click="editarPresupuesto(presupuesto)">
                <i class="bi bi-pencil-fill"></i> Editar
              </button>
              <button class="btn btn-danger eliminar"
                @click="eliminarPresupuestoLogicamente(presupuesto.id_presupuesto)">
                <i class="bi bi-trash-fill"></i> Eliminar
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Formulario para editar un presupuesto (se muestra solo cuando se selecciona "Editar") -->
    <div v-if="mostrarFormularioEdicion">
      <div v-if="presupuestoAEditar" class="editar-presupuesto">
        <h3>Editar Presupuesto</h3>
        <div class="form-group">
          <label>N°:</label>
          <input v-model="presupuestoAEditar.id_presupuesto" disabled />
        </div>
        <div class="form-group">
          <label>Descripción:</label>
          <textarea v-model="presupuestoAEditar.descripcion" id="descripcion" class="form-control"></textarea>
        </div>
        <div class="form-group">
          <label>Fecha:</label>
          <input v-model="presupuestoAEditar.fecha" type="date" />
        </div>
        <div class="form-group">
          <label>Aprobado:</label>
          <div class="form-check">
            <input v-model="presupuestoAEditar.aprobado" class="form-check-input" type="radio" id="aprobadoSi"
              value="true" />
            <label class="form-check-label" for="aprobadoSi">Sí</label>
          </div>
          <div class="form-check">
            <input v-model="presupuestoAEditar.aprobado" class="form-check-input" type="radio" id="aprobadoNo"
              value="false" />
            <label class="form-check-label" for="aprobadoNo">No</label>
          </div>
        </div>

        <div class="form-group">
          <label for="cliente" class="form-label">Cliente:</label>
          <select v-model="presupuestoAEditar.cli" id="cliente" class="form-control">
            <option v-for="cliente in clientes" :key="cliente.id_cliente" :value="cliente">
              {{ cliente.id_cliente }} {{ cliente.nombre.slice(-1)[0] }} {{ cliente.nombre.slice(0, -1).join(' ') }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label>Importe:</label>
          <input v-model="presupuestoAEditar.importe" type="number" />
        </div>
        <div class="acciones">
          <button type="submit" @click="actualizarPresupuesto">Actualizar</button>
          <button type="button" @click="cancelarEdicion" class="cancelar">Cancelar</button>
        </div>
        <div v-if="mensajeError" class="error">{{ mensajeError }}</div>
      </div>

      <!-- Msj de actualización exitosa -->
      <div v-if="mensajeExito" class="mensaje-exito">
        Modificación exitosa!!
      </div>
      <!-- Msj de eliminación exitosa -->
      <div v-if="mensajeExitoEliminacion" class="mensaje-exito">
        Eliminación exitosa!!
      </div>
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
      presupuestos: [],
      presupuestoAEditar: {
        id_presupuesto: "",
        descripcion: "",
        fecha: "",
        aprobado: false,
        cli: {
          id_cliente: "",
          nombre: [],
        },
        producto: {
          id_producto: "",
          marca: "",
          num_serie: "",
        },
        importe: "",
      },
      mensajeExito: false,
      clientes: [],
      mostrarFormularioEdicion: false,
      mostrarTabla: true,
      mensajeError: '',
    };
  },
  methods: {
    obtenerClientes() {
      this.$axios.get("http://localhost:8080/clientes/traer")
        .then((response) => {
          this.clientes = response.data;
          localStorage.setItem("clientes", JSON.stringify(response.data));
        })
        .catch((error) => {
          console.error("Error al obtener clientes:", error);
        });
    },

    obtenerPresupuestos() {
  console.log("Iniciando obtenerPresupuestos");
  this.$axios.get("http://localhost:8080/presupuestos/traerconclientes")
    .then((response) => {
      console.log("Datos de presupuestos:", response.data);
      this.presupuestos = response.data.map(presupuesto => ({ ...presupuesto, deleted: false }));
      localStorage.setItem("presupuestos", JSON.stringify(response.data));
    })
    .catch((error) => {
      console.error("Error al obtener presupuestos:", error);
    });
},


    editarPresupuesto(presupuesto) {
      // Copia profunda del presupuesto para evitar cambios no deseados
      this.presupuestoAEditar = JSON.parse(JSON.stringify(presupuesto));
      this.presupuestoAEditar.nombre = [];
      this.mostrarFormularioEdicion = true;
      this.mostrarTabla = false;

      // Seleccionar el cliente actual en el campo de selección de clientes
      this.presupuestoAEditar.cli = this.clientes.find(cliente => cliente.id_cliente === presupuesto.id_cliente);
    },

    actualizarPresupuesto() {
      console.log('Presupuesto a actualizar:', this.presupuestoAEditar);
      console.log('Cliente asociado:', this.presupuestoAEditar.cli);

      // Valida del lado del cliente
      if (!this.presupuestoAEditar.descripcion || !this.presupuestoAEditar.fecha || !this.presupuestoAEditar.cli) {
        console.error('Los campos descripcion, fecha y cliente son obligatorios.');
        this.mensajeError = 'Los campos descripcion, fecha y cliente son obligatorios.';
        setTimeout(() => {
          this.mensajeError = '';
        }, 3000);
        return;
      }

      // Verifica si la fecha ingresada es anterior a la fecha actual
      const fechaIngresada = new Date(this.presupuestoAEditar.fecha);
      const fechaActual = new Date();

      if (fechaIngresada.setDate(fechaIngresada.getDate() + 1) <= fechaActual) {
        console.error('La fecha ingresada no puede ser anterior a la fecha actual.');
        this.mensajeError = 'La fecha ingresada no puede ser anterior a la fecha actual.';
        setTimeout(() => {
          this.mensajeError = '';
        }, 3000);
        return;
      }


      // Lógica para actualizar el presupuesto
      this.$axios
        .put(
          `http://localhost:8080/presupuestos/editar/${this.presupuestoAEditar.id_presupuesto}`,
          this.presupuestoAEditar
        )
        .then(() => {
          this.obtenerPresupuestos();
          this.presupuestoAEditar = null; // Se limpia el presupuesto a editar después de la actualización
          this.mensajeExito = true;
          setTimeout(() => {
            this.mensajeExito = false;
            this.mostrarTabla = true;
          }, 3000);
        })
        .catch((error) => {
          console.error("Error al actualizar el presupuesto:", error);
        });

      // Obtiene el nombre del cliente asociado
      const idClienteAsociado = this.presupuestoAEditar.id_cliente;
      const clienteAsociado = this.clientes.find(cliente => cliente.id_cliente === idClienteAsociado);
      if (clienteAsociado) {
        console.log("Nombre del cliente actualizado:", clienteAsociado.nombre);
      }
    },

    cancelarEdicion() {
      // Cancela la edición y reinicia la variable de presupuestoAEditar
      this.presupuestoAEditar = null;
      this.mostrarFormularioEdicion = false;
      this.mostrarTabla = true;
    },

    eliminarPresupuestoLogicamente(id) {
    const presupuesto = this.presupuestos.find(presupuesto => presupuesto.id_presupuesto === id);

    if (presupuesto) {
        // Muestra la ventana emergente
        this.mostrarVentanaEmergente(`¿Estás seguro de eliminar al presupuesto ${id}?`, () => {
            // Lógica para eliminar el presupuesto si el usuario confirma
            // Actualiza el estado en el frontend
            presupuesto.deleted = true;

            // Actualiza la lista de presupuestos en el almacenamiento local
            localStorage.setItem("presupuestos", JSON.stringify(this.presupuestos));

            // Envía la solicitud para eliminar lógicamente en el backend
            this.$axios
                .delete(`http://localhost:8080/presupuestos/borrar/${id}`)
                .then(() => {
                    console.log("Presupuesto eliminado lógicamente:", id);
                    // Éxito, puedes mostrar un mensaje o hacer más acciones si es necesario
                })
                .catch((error) => {
                    console.error("Error al eliminar lógicamente el presupuesto:", error);
                    // Puede revertir el cambio en el frontend si es necesario
                    presupuesto.deleted = false;
                    // También puede revertir el cambio en el almacenamiento local
                    localStorage.setItem("presupuestos", JSON.stringify(this.presupuestos));
                });
        });
    }
},




        eliminarPresupuesto(id) {
            // Lógica para eliminar el cliente
            var url = `http://localhost:8080/presupuestos/borrar/${id}`;
            this.enviarSolicitud('DELETE', null, url, 'Presupuesto eliminado')
                .then(() => {
                    // Llama a obtenerClientes() para actualizar la lista después de eliminar un cliente
                    this.obtenerPresupuestos();
                });
        },

        enviarSolicitud(metodo, parametro, url, mensaje) {
            this.$axios({ method: metodo, url: url, data: parametro })
                .then(function (respuesta) {
                    var status = respuesta.data[0]['status'];
                    if (status === 'success') {
                        this.show_alerta(mensaje, status);
                        window.setTimeout(function () {
                            window.location.href = '/presupuestoslistar';
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
    presupuestosActivos() {
      return this.presupuestos.filter((presupuesto) => !presupuesto.deleted);
    },
  },

  mounted() {
    // Intenta cargar presupuestos desde el almacenamiento local
    const storedPresupuestos = localStorage.getItem("presupuestos");

    if (storedPresupuestos) {
      // Si hay presupuestos almacenados, úsalos en lugar de hacer una solicitud al servidor
      this.presupuestos = JSON.parse(storedPresupuestos);
    } else {
      // Si no hay presupuestos almacenados, obtén la lista del servidor
      this.obtenerPresupuestos();
    }
    this.obtenerClientes();
  },
};
</script>

<style scoped>
.listado-presupuestos {
  margin-top: 60px;
}

.listado-tabla {
  width: 1200px;
  margin-bottom: 40px;
}

.editar-presupuesto {
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
  padding: 16px;
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

.acciones {
  text-align: center;
  margin-top: 25px;
}

/* Estilos para los botones editar y eliminar */

.acciones button {
  width: 45%;
  padding: 5px;
  margin-right: 3px;
}

.editar {
  padding: 1px 6cm;
  font-size: 0.6em;
  width: 55px;
}

.eliminar {
  padding: 1px 6px;
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

/* Estilos para el formulario editar presupuesto */
.editar-presupuesto {
  border: 3px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  max-width: 500px;
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

.form-group textarea,
.form-group input,
.form-group select {
  flex: 1;
  padding: 6px;
  box-sizing: border-box;
}

.acciones button:last-child {
  margin-right: 0;
}

.form-group select {
  width: 70%;
  padding: 3px;
  box-sizing: border-box;
  margin-bottom: 10px;
  border: 1px solid #ddd;
}

.editar-presupuesto button {
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

.editar-presupuesto button:hover {
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

.acciones button.cancelar {
  background-color: #e53935;
  color: white;
}

.form-check {
  margin-right: -70px;
}

.error {
  color: red;
  font-size: 0.8rem;
  text-align: center;
  margin-top: 15px;
}
</style>
