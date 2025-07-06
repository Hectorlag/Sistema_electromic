<template>
  <!-- ✅ CAMBIO PRINCIPAL: Usar ModernTableWrapper -->
  <ModernTableWrapper 
    titulo="Listado de Presupuestos"
    create-route="/presupuestoregistrar"
    create-button-text="Nuevo Presupuesto"
  >
    <!-- Tabla con clases estándar -->
    <table class="table-standard">
      <thead>
        <tr>
          <th class="celda-numero">N°</th>
          <th>Descripción</th>
          <th>Fecha</th>
          <th>Aprobado</th>
          <th class="celda-numero">N° Cliente</th>
          <th>Nombre Cliente</th>
          <th class="celda-numero">Importe</th>
          <th>Marca Producto</th>
          <th>N° Serie</th>
          <th class="acciones-col">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="presupuesto in presupuestosActivos" :key="presupuesto.id_presupuesto">
          <td class="celda-numero">{{ presupuesto.id_presupuesto }}</td>
          <td>{{ presupuesto.descripcion }}</td>
          <td>{{ formatearFecha(presupuesto.fecha) }}</td>
          <td>
            <span :class="getEstadoAprobadoClass(presupuesto.aprobado)">
              <i :class="getEstadoAprobadoIcon(presupuesto.aprobado)"></i>
              {{ presupuesto.aprobado ? 'Aprobado' : 'Pendiente' }}
            </span>
          </td>
          <td class="celda-numero">{{ presupuesto.id_cliente }}</td>
          <td class="fw-bold">{{ obtenerNombreCliente(presupuesto) }}</td>
          <td class="celda-numero">{{ formatearImporte(presupuesto.importe) }}</td>
          <td class="fw-bold">{{ presupuesto.marca || 'Sin especificar' }}</td>
          <td class="font-mono">{{ presupuesto.num_serie || 'Sin especificar' }}</td>
          <td class="acciones-col">
            <div class="acciones-group">
              <router-link 
                :to="`/presupuestoregistrar/${presupuesto.id_presupuesto}`" 
                class="btn-action btn-edit"
                title="Editar presupuesto"
              >
                <i class="bi bi-pencil-fill"></i> Editar
              </router-link>
              <button 
                class="btn-action btn-delete"
                @click="eliminarPresupuestoLogicamente(presupuesto.id_presupuesto)"
                title="Eliminar presupuesto"
              >
                <i class="bi bi-trash-fill"></i> Eliminar
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Mensaje sin datos -->
    <div v-if="presupuestosActivos.length === 0" class="tabla-sin-datos">
      <i class="bi bi-file-earmark-text"></i>
      <h3>No hay presupuestos registrados</h3>
      <p>Comienza creando tu primer presupuesto usando el botón "Nuevo Presupuesto" arriba</p>
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
  name: 'PresupuestosView',
  components: {
    ModernTableWrapper  // ✅ NUEVO: Usar el wrapper moderno
  },
  
  data() {
    return {
      presupuestos: [],
      mensajeExito: false,
      clientes: [],
    };
  },

  computed: {
    presupuestosActivos() {
      return this.presupuestos.filter((presupuesto) => !presupuesto.deleted);
    },
  },

  methods: {
    obtenerNombreCliente(presupuesto) {
      if (presupuesto.nombre && Array.isArray(presupuesto.nombre)) {
        // Mostrar apellido y nombre (último elemento primero)
        return presupuesto.nombre.slice(-1)[0] + ' ' + presupuesto.nombre.slice(0, -1).join(' ');
      }
      // Buscar el cliente en la lista de clientes
      const cliente = this.clientes.find(c => c.id_cliente === presupuesto.id_cliente);
      if (cliente) {
        if (Array.isArray(cliente.nombre)) {
          return cliente.nombre.slice(-1)[0] + ' ' + cliente.nombre.slice(0, -1).join(' ');
        }
        return cliente.nombre || 'Sin asignar';
      }
      return 'Sin asignar';
    },

    formatearFecha(fecha) {
      if (!fecha) return '';
      const date = new Date(fecha);
      return date.toLocaleDateString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      });
    },

    formatearImporte(importe) {
      if (!importe) return '$0.00';
      return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS'
      }).format(importe);
    },

    getEstadoAprobadoClass(aprobado) {
      return aprobado ? 'badge-estado badge-aprobado' : 'badge-estado badge-pendiente';
    },

    getEstadoAprobadoIcon(aprobado) {
      return aprobado ? 'bi bi-check-circle-fill' : 'bi bi-clock-fill';
    },

    obtenerPresupuestos() {
      this.$axios.get("http://localhost:8080/presupuestos/traerconclientes")
        .then((response) => {
          console.log("Respuesta de la API:", response.data);
          
          // Ordenar presupuestos por ID
          response.data.sort((a, b) => a.id_presupuesto - b.id_presupuesto);
          
          this.presupuestos = response.data.map(presupuesto => ({ 
            ...presupuesto, 
            deleted: false 
          }));
          localStorage.setItem("presupuestos", JSON.stringify(this.presupuestos));
        })
        .catch((error) => {
          console.error("Error al obtener presupuestos:", error);
          this.mostrarErrorConexion();
        });
    },

    obtenerClientes() {
      this.$axios.get('http://localhost:8080/clientes/traer')
        .then(response => {
          console.log('Respuesta de la API clientes:', response.data);
          this.clientes = response.data;
          localStorage.setItem('clientes', JSON.stringify(response.data));
        })
        .catch(error => {
          console.error('Error al obtener clientes:', error);
        });
    },

    eliminarPresupuestoLogicamente(id) {
      const presupuesto = this.presupuestos.find((presupuesto) => presupuesto.id_presupuesto === id);

      if (presupuesto) {
        this.mostrarVentanaEmergente(
          `¿Estás seguro de eliminar el presupuesto N°${presupuesto.id_presupuesto}?`, 
          () => {
            // Actualiza el estado en el frontend
            presupuesto.deleted = true;
            localStorage.setItem("presupuestos", JSON.stringify(this.presupuestos));

            // Envía la solicitud para eliminar lógicamente en el backend
            this.$axios.delete(`http://localhost:8080/presupuestos/borrar/${id}`)
              .then(() => {
                console.log("Presupuesto eliminado lógicamente:", id);
                this.mensajeExito = true;
                setTimeout(() => {
                  this.mensajeExito = false;
                }, 3000);
              })
              .catch((error) => {
                console.error("Error al eliminar lógicamente el presupuesto:", error);
                // Revierte el cambio en caso de error
                presupuesto.deleted = false;
                localStorage.setItem("presupuestos", JSON.stringify(this.presupuestos));
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
            text: 'El presupuesto ha sido eliminado correctamente.',
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
        text: 'No se pudo eliminar el presupuesto. Intenta nuevamente.',
        icon: 'error',
        confirmButtonText: 'Entendido'
      });
    }
  },

  mounted() {
    // Intenta cargar presupuestos desde el almacenamiento local
    const storedPresupuestos = localStorage.getItem('presupuestos');
    if (storedPresupuestos) {
      try {
        this.presupuestos = JSON.parse(storedPresupuestos).map(presupuesto => ({ 
          ...presupuesto, 
          deleted: false 
        }));
      } catch (error) {
        console.error('Error al parsear presupuestos del localStorage:', error);
        this.obtenerPresupuestos();
      }
    } else {
      this.obtenerPresupuestos();
    }

    // Cargar clientes
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

/* Badges para estados de presupuestos */
.badge-estado {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.badge-pendiente {
  background: linear-gradient(135deg, rgba(255, 193, 7, 0.15), rgba(255, 193, 7, 0.1));
  color: #f39c12;
  border: 1px solid rgba(255, 193, 7, 0.2);
}

.badge-aprobado {
  background: linear-gradient(135deg, rgba(39, 174, 96, 0.15), rgba(39, 174, 96, 0.1));
  color: #27ae60;
  border: 1px solid rgba(39, 174, 96, 0.2);
}

.badge-estado i {
  font-size: 10px;
}

/* Los estilos de shared-table-styles.css se aplican automáticamente */
</style>