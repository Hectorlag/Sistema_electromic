<template>
  <!-- ✅ CAMBIO PRINCIPAL: Usar ModernTableWrapper -->
  <ModernTableWrapper 
    titulo="Listado de Órdenes de Trabajo"
    create-route="/ordenregistrar"
    create-button-text="Nueva Orden"
  >
    <!-- Tabla con clases estándar -->
    <table class="table-standard">
      <thead>
        <tr>
          <th class="celda-numero">N°</th>
          <th>Estado</th>
          <th class="celda-numero">N° Presupuesto</th>
          <th>Fecha Presupuesto</th>
          <th>Cliente</th>
          <th>Producto</th>
          <th class="acciones-col">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="orden in ordenesActivas" :key="orden.id_orden">
          <td class="celda-numero">{{ orden.id_orden }}</td>
          <td>
            <span :class="getEstadoClass(orden.estado)">
              <i :class="getEstadoIcon(orden.estado)"></i>
              {{ orden.estado }}
            </span>
          </td>
          <td class="celda-numero">{{ orden.id_presupuesto }}</td>
          <td>{{ formatearFecha(orden.fecha) }}</td>
          <td>
            <button 
              class="btn-link cliente-link"
              @click="mostrarDetalleCliente(orden.id_cliente)"
              title="Ver detalles del cliente"
            >
              <i class="bi bi-person-fill"></i>
              Ver cliente
            </button>
          </td>
          <td>
            <button 
              class="btn-link producto-link"
              @click="mostrarDetalleProducto(orden.id_producto)"
              title="Ver detalles del producto"
            >
              <i class="bi bi-box-fill"></i>
              Ver producto
            </button>
          </td>
          <td class="acciones-col">
            <div class="acciones-group">
              <!-- ✅ ACTUALIZADO: Botón de edición mejorado -->
              <button 
                class="btn-action btn-edit"
                @click="editarOrden(orden)"
                title="Editar orden de trabajo"
              >
                <i class="bi bi-pencil-fill"></i> Editar
              </button>
              <button 
                class="btn-action btn-delete"
                @click="eliminarOrdenDeTrabajo(orden.id_orden)"
                title="Eliminar orden de trabajo"
              >
                <i class="bi bi-trash-fill"></i> Eliminar
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Mensaje sin datos -->
    <div v-if="ordenesActivas.length === 0" class="tabla-sin-datos">
      <i class="bi bi-clipboard-data"></i>
      <h3>No hay órdenes de trabajo registradas</h3>
      <p>Comienza creando tu primera orden de trabajo usando el botón "Nueva Orden" arriba</p>
    </div>

    <!-- ✅ NUEVO: Mensajes mejorados -->
    <EnhancedMessage
      :type="messageType"
      :message="messageText"
      :title="messageTitle"
      :show-modal="showModalMessage"
      :show-inline="showInlineMessage"
      :primary-button-text="primaryButtonText"
      :show-secondary-button="showSecondaryButton"
      @close="closeMessage"
      @primary-action="handlePrimaryAction"
    />
  </ModernTableWrapper>
</template>

<script>
// ✅ IMPORTS ACTUALIZADOS
import ModernTableWrapper from '@/components/common/ModernTableWrapper.vue';
import EnhancedMessage from '@/components/common/EnhancedMessage.vue'; // ✅ NUEVO
import Swal from 'sweetalert2';

export default {
  name: 'OrdenesTrabajoView',
  components: {
    ModernTableWrapper,
    EnhancedMessage  // ✅ NUEVO
  },
  
  data() {
    return {
      ordenesTrabajo: [],
      cliente: {},
      producto: {},

      // ✅ NUEVAS PROPIEDADES PARA MENSAJES MEJORADOS
      messageType: 'info',
      messageText: '',
      messageTitle: '',
      showModalMessage: false,
      showInlineMessage: false,
      primaryButtonText: 'Continuar',
      showSecondaryButton: true,
    };
  },

  computed: {
    ordenesActivas() {
      return this.ordenesTrabajo.filter((orden) => !orden.deleted);
    },
  },

  methods: {
    // ✅ MÉTODOS MEJORADOS PARA MENSAJES
    showSuccessMessage(message, title = '¡Operación Exitosa!', useModal = true) {
      this.messageType = 'success';
      this.messageText = message;
      this.messageTitle = title;
      this.primaryButtonText = 'Continuar';
      this.showSecondaryButton = false;
      
      if (useModal) {
        this.showModalMessage = true;
      } else {
        this.showInlineMessage = true;
      }
    },

    showErrorMessage(message, title = 'Error en la Operación', useModal = false) {
      this.messageType = 'error';
      this.messageText = message;
      this.messageTitle = title;
      this.primaryButtonText = 'Intentar Nuevamente';
      this.showSecondaryButton = true;
      
      if (useModal) {
        this.showModalMessage = true;
      } else {
        this.showInlineMessage = true;
      }
    },

    closeMessage() {
      this.showModalMessage = false;
      this.showInlineMessage = false;
    },

    handlePrimaryAction() {
      // Acción específica si es necesaria
    },

    // ✅ NUEVO: Método para editar orden (navegación mejorada)
    editarOrden(orden) {
      try {
        console.log('Editando orden:', orden);
        
        // Guardar los datos de la orden en localStorage para que el formulario los pueda cargar
        const ordenCompleta = {
          ...orden,
          // Asegurar que tenga la estructura correcta
          presupuesto: {
            id_presupuesto: orden.id_presupuesto
          }
        };
        
        // Guardar temporalmente la orden para edición
        localStorage.setItem('ordenParaEditar', JSON.stringify(ordenCompleta));
        
        // Navegar al formulario de edición
        this.$router.push(`/ordenregistrar/${orden.id_orden}`);
        
      } catch (error) {
        console.error('Error al preparar edición:', error);
        this.showErrorMessage(
          'No se pudo abrir el formulario de edición. Por favor, intenta nuevamente.',
          'Error de Navegación'
        );
      }
    },

    // ✅ ACTUALIZADO: Obtener órdenes con mejor manejo de errores
    obtenerOrdenesTrabajo() {
      console.log("Iniciando obtenerOrdenesTrabajo");
      this.$axios.get("http://localhost:8080/ordenes/traer")
        .then((response) => {
          console.log("Respuesta de la API:", response.data);
          
          // Verificar que la respuesta sea válida
          if (!response.data || !Array.isArray(response.data)) {
            throw new Error('Respuesta inválida del servidor');
          }
          
          // Ordenar órdenes por ID
          response.data.sort((a, b) => a.id_orden - b.id_orden);
          
          this.ordenesTrabajo = response.data.map(orden => ({ ...orden, deleted: false }));
          localStorage.setItem("ordenesTrabajo", JSON.stringify(this.ordenesTrabajo));
        })
        .catch((error) => {
          console.error("Error al obtener órdenes de trabajo:", error);
          this.showErrorMessage(
            'No se pudieron cargar las órdenes de trabajo. Verifica tu conexión e intenta nuevamente.',
            'Error de Conexión',
            true
          );
        });
    },

    formatearFecha(fecha) {
      if (!fecha) return '';
      try {
        const date = new Date(fecha);
        return date.toLocaleDateString('es-ES', {
          day: '2-digit',
          month: '2-digit',
          year: 'numeric'
        });
      } catch (error) {
        console.error('Error al formatear fecha:', error);
        return 'Fecha inválida';
      }
    },

    getEstadoClass(estado) {
      const clases = {
        'En reparación': 'badge-estado badge-reparacion',
        'Para ingresar': 'badge-estado badge-ingreso',
        'Completado': 'badge-estado badge-completado',
        'En espera de repuestos': 'badge-estado badge-espera',
        'Listo para entrega': 'badge-estado badge-listo'
      };
      return clases[estado] || 'badge-estado badge-default';
    },

    getEstadoIcon(estado) {
      const iconos = {
        'En reparación': 'bi bi-tools',
        'Para ingresar': 'bi bi-arrow-down-circle-fill',
        'Completado': 'bi bi-check-circle-fill',
        'En espera de repuestos': 'bi bi-clock-fill',
        'Listo para entrega': 'bi bi-truck'
      };
      return iconos[estado] || 'bi bi-circle-fill';
    },

    // ✅ ACTUALIZADO: Eliminación con mensajes mejorados
    eliminarOrdenDeTrabajo(id) {
      const orden = this.ordenesTrabajo.find((orden) => orden.id_orden === id);

      if (!orden) {
        this.showErrorMessage(
          'No se encontró la orden de trabajo especificada.',
          'Orden No Encontrada'
        );
        return;
      }

      // Usar SweetAlert2 para confirmación
      Swal.fire({
        title: '¿Confirmar eliminación?',
        text: `¿Estás seguro de eliminar la orden de trabajo N°${orden.id_orden}?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '✓ Sí, eliminar',
        cancelButtonText: '✗ Cancelar',
        customClass: {
          confirmButton: 'btn btn-danger me-3',
          cancelButton: 'btn btn-secondary',
        },
        buttonsStyling: false,
        reverseButtons: true
      }).then(result => {
        if (result.isConfirmed) {
          this.ejecutarEliminacion(orden);
        }
      });
    },

    // ✅ NUEVO: Método separado para ejecutar la eliminación
    ejecutarEliminacion(orden) {
      // Actualiza el estado en el frontend inmediatamente
      orden.deleted = true;
      localStorage.setItem("ordenesTrabajo", JSON.stringify(this.ordenesTrabajo));

      // Envía la solicitud para eliminar lógicamente en el backend
      this.$axios.delete(`http://localhost:8080/ordenes/borrar/${orden.id_orden}`)
        .then(() => {
          console.log("Orden de trabajo eliminada:", orden.id_orden);
          
          // Mostrar mensaje de éxito
          this.showSuccessMessage(
            `La orden de trabajo N°${orden.id_orden} ha sido eliminada correctamente.`,
            '¡Orden Eliminada!',
            false // Usar mensaje inline
          );
        })
        .catch((error) => {
          console.error("Error al eliminar la orden de trabajo:", error);
          
          // Revierte el cambio en caso de error
          orden.deleted = false;
          localStorage.setItem("ordenesTrabajo", JSON.stringify(this.ordenesTrabajo));
          
          this.showErrorMessage(
            'No se pudo eliminar la orden de trabajo. Por favor, intenta nuevamente.',
            'Error al Eliminar',
            true
          );
        });
    },

    // Método para buscar los detalles del cliente por su ID
    async buscarClientePorId(idCliente) {
      try {
        const response = await this.$axios.get(`http://localhost:8080/clientes/traer/${idCliente}`);
        console.log("Detalles del cliente:", response.data);
        this.cliente = response.data;
        return response.data;
      } catch (error) {
        console.error("Error al obtener los detalles del cliente:", error);
        throw error;
      }
    },

    // ✅ ACTUALIZADO: Método para mostrar detalles del cliente con mejor formato
    async mostrarDetalleCliente(idCliente) {
      try {
        await this.buscarClientePorId(idCliente);
        
        const nombreCompleto = Array.isArray(this.cliente.nombre) 
          ? `${this.cliente.nombre.slice(-1)[0]} ${this.cliente.nombre.slice(0, -1).join(' ')}`
          : this.cliente.nombre || 'Sin nombre';
        
        Swal.fire({
          title: 'Detalles del Cliente',
          html: `
            <div class="detalle-modal">
              <div class="detalle-item">
                <strong>N°:</strong> ${this.cliente.id_cliente}
              </div>
              <div class="detalle-item">
                <strong>Nombre:</strong> ${nombreCompleto}
              </div>
              <div class="detalle-item">
                <strong>Email:</strong> ${this.cliente.email || 'Sin email'}
              </div>
              <div class="detalle-item">
                <strong>Teléfono:</strong> ${
                  Array.isArray(this.cliente.telefono) && this.cliente.telefono[0] 
                    ? this.cliente.telefono[0] 
                    : this.cliente.telefono || 'Sin teléfono'
                }
              </div>
              <div class="detalle-item">
                <strong>Dirección:</strong> ${this.cliente.direccion || 'Sin dirección'}
              </div>
              <div class="detalle-item">
                <strong>DNI:</strong> ${this.cliente.cuil || 'Sin DNI'}
              </div>
            </div>
          `,
          confirmButtonText: 'Cerrar',
          customClass: {
            confirmButton: 'btn btn-primary'
          },
          width: '450px'
        });
      } catch (error) {
        this.showErrorMessage(
          'No se pudieron obtener los detalles del cliente.',
          'Error al Cargar Cliente'
        );
      }
    },

    // ✅ ACTUALIZADO: Método para mostrar detalles del producto con información relevante
    async mostrarDetalleProducto(idProducto) {
      try {
        await this.buscarProductoPorId(idProducto);
        
        // Formatear accesorios si existen
        let accesoriosHtml = '';
        if (this.producto.accesorios && Array.isArray(this.producto.accesorios)) {
          const accesoriosValidos = this.producto.accesorios.filter(acc => acc && acc.trim() !== '');
          if (accesoriosValidos.length > 0) {
            accesoriosHtml = `
              <div class="detalle-item">
                <strong>Accesorios:</strong> ${accesoriosValidos.join(', ')}
              </div>
            `;
          }
        }
        
        Swal.fire({
          title: 'Detalles del Producto',
          html: `
            <div class="detalle-modal">
              <div class="detalle-item">
                <strong>N°:</strong> ${this.producto.id_producto}
              </div>
              <div class="detalle-item">
                <strong>Marca:</strong> ${this.producto.marca || 'Sin especificar'}
              </div>
              <div class="detalle-item">
                <strong>Modelo:</strong> ${this.producto.modelo || 'Sin especificar'}
              </div>
              <div class="detalle-item">
                <strong>Tipo:</strong> ${this.producto.tipo || 'Sin especificar'}
              </div>
              <div class="detalle-item">
                <strong>N° Serie:</strong> ${this.producto.num_serie || 'Sin especificar'}
              </div>
              ${accesoriosHtml}
            </div>
          `,
          confirmButtonText: 'Cerrar',
          customClass: {
            confirmButton: 'btn btn-primary'
          },
          width: '450px'
        });
      } catch (error) {
        this.showErrorMessage(
          'No se pudieron obtener los detalles del producto.',
          'Error al Cargar Producto'
        );
      }
    },

    async buscarProductoPorId(idProducto) {
      try {
        const response = await this.$axios.get(`http://localhost:8080/productos/traer/${idProducto}`);
        console.log("Detalles del producto:", response.data);
        this.producto = response.data;
        return response.data;
      } catch (error) {
        console.error("Error al obtener los detalles del producto:", error);
        throw error;
      }
    },

    // ✅ NUEVO: Limpiar datos temporales al salir
    limpiarDatosTemporales() {
      localStorage.removeItem('ordenParaEditar');
    }
  },

  mounted() {
    // Limpiar datos temporales de ediciones anteriores
    this.limpiarDatosTemporales();
    
    // Intenta cargar órdenes de trabajo desde el almacenamiento local
    const storedOrdenes = localStorage.getItem("ordenesTrabajo");
    if (storedOrdenes) {
      try {
        const parsedOrdenes = JSON.parse(storedOrdenes);
        if (Array.isArray(parsedOrdenes)) {
          this.ordenesTrabajo = parsedOrdenes.map(orden => ({ 
            ...orden, 
            deleted: orden.deleted || false 
          }));
        } else {
          throw new Error('Datos inválidos en localStorage');
        }
      } catch (error) {
        console.error('Error al parsear órdenes del localStorage:', error);
        localStorage.removeItem("ordenesTrabajo"); // Limpiar datos corruptos
        this.obtenerOrdenesTrabajo();
      }
    } else {
      this.obtenerOrdenesTrabajo();
    }
  },

  // ✅ NUEVO: Limpiar al salir del componente
  beforeUnmount() {
    this.limpiarDatosTemporales();
  }
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

/* Badges para estados de órdenes */
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

.badge-reparacion {
  background: linear-gradient(135deg, rgba(255, 152, 0, 0.15), rgba(255, 152, 0, 0.1));
  color: #ff9800;
  border: 1px solid rgba(255, 152, 0, 0.2);
}

.badge-ingreso {
  background: linear-gradient(135deg, rgba(33, 150, 243, 0.15), rgba(33, 150, 243, 0.1));
  color: #2196f3;
  border: 1px solid rgba(33, 150, 243, 0.2);
}

.badge-completado {
  background: linear-gradient(135deg, rgba(76, 175, 80, 0.15), rgba(76, 175, 80, 0.1));
  color: #4caf50;
  border: 1px solid rgba(76, 175, 80, 0.2);
}

.badge-espera {
  background: linear-gradient(135deg, rgba(255, 193, 7, 0.15), rgba(255, 193, 7, 0.1));
  color: #ffc107;
  border: 1px solid rgba(255, 193, 7, 0.2);
}

.badge-listo {
  background: linear-gradient(135deg, rgba(156, 39, 176, 0.15), rgba(156, 39, 176, 0.1));
  color: #9c27b0;
  border: 1px solid rgba(156, 39, 176, 0.2);
}

.badge-default {
  background: linear-gradient(135deg, rgba(158, 158, 158, 0.15), rgba(158, 158, 158, 0.1));
  color: #9e9e9e;
  border: 1px solid rgba(158, 158, 158, 0.2);
}

.badge-estado i {
  font-size: 10px;
}

/* Botones de enlace para cliente y producto */
.btn-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: none;
  border: none;
  color: #3498db;
  cursor: pointer;
  font-size: 13px;
  font-weight: 500;
  text-decoration: none;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.btn-link:hover {
  background: rgba(52, 152, 219, 0.1);
  color: #2980b9;
  text-decoration: none;
  transform: translateY(-1px);
}

.btn-link i {
  font-size: 12px;
}

.cliente-link {
  color: #e74c3c;
}

.cliente-link:hover {
  background: rgba(231, 76, 60, 0.1);
  color: #c0392b;
}

.producto-link {
  color: #f39c12;
}

.producto-link:hover {
  background: rgba(243, 156, 18, 0.1);
  color: #e67e22;
}

/* ============================================ */
/* MEJORAS PARA COMPATIBILIDAD CON MENSAJES MEJORADOS */
/* ============================================ */

/* Asegurar que los mensajes overlay no interfieran con el layout */
.modern-table-wrapper {
  position: relative !important;
}

/* Los estilos de shared-table-styles.css se aplican automáticamente */
</style>

<style>
/* Estilos globales para los modales de SweetAlert2 */
.detalle-modal {
  text-align: left;
}

.detalle-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  font-size: 14px;
  line-height: 1.4;
}

.detalle-item:last-child {
  border-bottom: none;
}

.detalle-item strong {
  color: #2c3e50;
  margin-right: 8px;
  display: inline-block;
  min-width: 80px;
}
</style>