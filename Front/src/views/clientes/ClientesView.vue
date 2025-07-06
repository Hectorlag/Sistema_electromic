<template>
  <!-- ✅ CAMBIO PRINCIPAL: Usar ModernTableWrapper -->
  <ModernTableWrapper 
    titulo="Listado de Clientes"
    create-route="/clienteregistrar"
    create-button-text="Nuevo Cliente"
  >
    <!-- Tabla con clases estándar -->
    <table class="table-standard">
      <thead>
        <tr>
          <th class="celda-numero">N°</th>
          <th>Apellido y Nombre</th>
          <th>Dirección</th>
          <th>DNI</th>
          <th>Email</th>
          <th>Teléfono/s</th>
          <th class="acciones-col">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="cliente in clientesActivos" :key="cliente.id_cliente">
          <td class="celda-numero">{{ cliente.id_cliente }}</td>
          <td class="fw-bold">
            {{ cliente.nombre.slice(-1)[0] }} {{ cliente.nombre.slice(0, -1).join(' ') }}
          </td>
          <td>{{ cliente.direccion }}</td>
          <td class="font-mono">{{ cliente.cuil }}</td>
          <td class="celda-email">{{ cliente.email }}</td>
          <td>
            <ul class="lista-celda">
              <li v-for="tel in cliente.telefono" :key="tel">
                <i class="bi bi-telephone"></i> {{ tel }}
              </li>
            </ul>
          </td>
          <td class="acciones-col">
            <div class="acciones-group">
              <router-link 
                :to="`/clienteregistrar/${cliente.id_cliente}`" 
                class="btn-action btn-edit"
                title="Editar cliente"
              >
                <i class="bi bi-pencil-fill"></i> Editar
              </router-link>
              <button 
                class="btn-action btn-delete"
                @click="eliminarClienteLogicamente(cliente.id_cliente)"
                title="Eliminar cliente"
              >
                <i class="bi bi-trash-fill"></i> Eliminar
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Mensaje sin datos -->
    <div v-if="clientesActivos.length === 0" class="tabla-sin-datos">
      <i class="bi bi-people"></i>
      <h3>No hay clientes registrados</h3>
      <p>Comienza agregando tu primer cliente usando el botón "Nuevo Cliente" arriba</p>
    </div>

    <!-- Mensaje de éxito -->
    <div v-if="mensajeExito" class="mensaje-exito">
      <i class="bi bi-check-circle-fill"></i>
      ¡Modificación exitosa!
    </div>
  </ModernTableWrapper>
</template>

<script>
// ✅ CAMBIO PRINCIPAL: Import del nuevo wrapper moderno
import ModernTableWrapper from '@/components/common/ModernTableWrapper.vue';
import Swal from 'sweetalert2';

export default {
  name: 'ClientesView',
  components: {
    ModernTableWrapper  // ✅ NUEVO: Usar el wrapper moderno
  },
  
  data() {
    return {
      clientes: [],
      mensajeExito: false,
    };
  },

  computed: {
    clientesActivos() {
      return this.clientes.filter(cliente => !cliente.deleted);
    },
  },

  methods: {
    obtenerClientes() {
      this.$axios.get('http://localhost:8080/clientes/traer')
        .then(response => {
          console.log('Respuesta de la API:', response.data);
          
          // Ordena la lista de clientes por el último nombre
          response.data.sort((a, b) => {
            const ultimoNombreA = a.nombre[a.nombre.length - 1];
            const ultimoNombreB = b.nombre[b.nombre.length - 1];
            if (ultimoNombreA < ultimoNombreB) return -1;
            if (ultimoNombreA > ultimoNombreB) return 1;
            return 0;
          });

          this.clientes = response.data;
          localStorage.setItem('clientes', JSON.stringify(response.data));
        })
        .catch(error => {
          console.error('Error al obtener clientes:', error);
          this.mostrarErrorConexion();
        });
    },

    eliminarClienteLogicamente(id) {
      const cliente = this.clientes.find((cliente) => cliente.id_cliente === id);

      if (cliente) {
        this.mostrarVentanaEmergente(
          `¿Estás seguro de eliminar el cliente "${cliente.nombre.join(' ')}"?`, 
          () => {
            // Actualiza el estado en el frontend
            cliente.deleted = true;
            localStorage.setItem("clientes", JSON.stringify(this.clientes));

            // Envía la solicitud para eliminar lógicamente en el backend
            this.$axios.delete(`http://localhost:8080/clientes/borrar/${id}`)
              .then(() => {
                console.log("Cliente eliminado lógicamente:", id);
                this.mensajeExito = true;
                setTimeout(() => {
                  this.mensajeExito = false;
                }, 3000);
              })
              .catch((error) => {
                console.error("Error al eliminar lógicamente el cliente:", error);
                // Revierte el cambio en caso de error
                cliente.deleted = false;
                localStorage.setItem("clientes", JSON.stringify(this.clientes));
                this.mostrarErrorEliminacion();
              });
          }
        );
      }
    },

    mostrarVentanaEmergente(mensaje, callback) {
      const swalWithBootstrapButton = Swal.mixin({
        customClass: {
          confirmButton: 'btn btn-success me-3',
          cancelButton: 'btn btn-danger',
        },
        buttonsStyling: false,
      });

      swalWithBootstrapButton.fire({
        title: '¿Confirmar eliminación?',
        text: mensaje,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '✓ Sí, eliminar',
        cancelButtonText: '✗ Cancelar',
        reverseButtons: true
      }).then(result => {
        if (result.isConfirmed) {
          callback();
          
          // Mostrar confirmación de eliminación
          Swal.fire({
            title: '¡Eliminado!',
            text: 'El cliente ha sido eliminado correctamente.',
            icon: 'success',
            timer: 2000,
            showConfirmButton: false
          });
        }
      });
    },

    mostrarErrorConexion() {
      Swal.fire({
        title: 'Error de conexión',
        text: 'No se pudo conectar con el servidor. Verifica tu conexión.',
        icon: 'error',
        confirmButtonText: 'Entendido'
      });
    },

    mostrarErrorEliminacion() {
      Swal.fire({
        title: 'Error al eliminar',
        text: 'No se pudo eliminar el cliente. Intenta nuevamente.',
        icon: 'error',
        confirmButtonText: 'Entendido'
      });
    }
  },

  mounted() {
    // Intenta cargar clientes desde el almacenamiento local
    const storedClients = localStorage.getItem('clientes');
    if (storedClients) {
      try {
        this.clientes = JSON.parse(storedClients);
      } catch (error) {
        console.error('Error al parsear clientes del localStorage:', error);
        this.obtenerClientes();
      }
    } else {
      this.obtenerClientes();
    }
  },
};
</script>

<style scoped>
/* ============================================ */
/* ESTILOS ADICIONALES PARA MEJORAR UX */
/* ============================================ */

/* Mejorar los tooltips de botones */
.btn-action {
  position: relative;
}

.btn-action:hover::after {
  content: attr(title);
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  z-index: 1000;
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateX(-50%) translateY(5px); }
  to { opacity: 1; transform: translateX(-50%) translateY(0); }
}

/* Mejorar la tabla sin datos */
.tabla-sin-datos {
  padding: 60px 20px !important;
}

.tabla-sin-datos i {
  font-size: 64px !important;
  opacity: 0.3 !important;
  margin-bottom: 20px !important;
}

.tabla-sin-datos h3 {
  font-size: 20px !important;
  margin-bottom: 12px !important;
  color: #2c3e50 !important;
  font-weight: 600 !important;
}

.tabla-sin-datos p {
  font-size: 16px !important;
  margin-bottom: 0 !important;
  color: #7f8c8d !important;
  line-height: 1.5 !important;
}

/* Animación para las filas de la tabla */
.table-standard tbody tr {
  animation: slideInRow 0.3s ease-out;
}

@keyframes slideInRow {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Mejorar el mensaje de éxito */
.mensaje-exito {
  animation: bounceIn 0.5s ease-out;
}

@keyframes bounceIn {
  0% {
    opacity: 0;
    transform: scale(0.3);
  }
  50% {
    opacity: 1;
    transform: scale(1.05);
  }
  70% {
    transform: scale(0.95);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

/* Los estilos de shared-table-styles.css se aplican automáticamente */
</style>