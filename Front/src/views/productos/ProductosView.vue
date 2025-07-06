<template>
  <!-- ✅ CAMBIO PRINCIPAL: Usar ModernTableWrapper -->
  <ModernTableWrapper 
    titulo="Listado de Productos"
    create-route="/productoregistrar"
    create-button-text="Nuevo Producto"
  >
    <!-- Tabla con clases estándar -->
    <table class="table-standard">
      <thead>
        <tr>
          <th class="celda-numero">N°</th>
          <th>N° de Serie</th>
          <th>Tipo</th>
          <th>Marca</th>
          <th>Modelo</th>
          <th>Accesorios</th>
          <th class="celda-numero">N° Cliente</th>
          <th>Nombre Cliente</th>
          <th class="acciones-col">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="producto in productosActivos" :key="producto.id_producto">
          <td class="celda-numero">{{ producto.id_producto }}</td>
          <td class="font-mono">{{ producto.num_serie }}</td>
          <td>
            <span class="badge-estado badge-activo">{{ producto.tipo }}</span>
          </td>
          <td class="fw-bold">{{ producto.marca }}</td>
          <td>{{ producto.modelo }}</td>
          <td>
            <span v-if="!tieneAccesorios(producto)" class="text-muted">
              <i class="bi bi-dash-circle"></i> Sin accesorios
            </span>
            <ul v-else class="lista-celda">
              <li v-for="accesorio in obtenerAccesoriosLimpios(producto.accesorios)" :key="accesorio">
                <i class="bi bi-tools"></i> {{ accesorio }}
              </li>
            </ul>
          </td>
          <td class="celda-numero">{{ producto.id_cliente }}</td>
          <td class="fw-bold">{{ obtenerNombreCliente(producto) }}</td>
          <td class="acciones-col">
            <div class="acciones-group">
              <router-link 
                :to="`/productoregistrar/${producto.id_producto}`" 
                class="btn-action btn-edit"
                title="Editar producto"
              >
                <i class="bi bi-pencil-fill"></i> Editar
              </router-link>
              <button 
                class="btn-action btn-delete"
                @click="eliminarProductoLogicamente(producto.id_producto)"
                title="Eliminar producto"
              >
                <i class="bi bi-trash-fill"></i> Eliminar
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Mensaje sin datos -->
    <div v-if="productosActivos.length === 0" class="tabla-sin-datos">
      <i class="bi bi-box"></i>
      <h3>No hay productos registrados</h3>
      <p>Comienza agregando tu primer producto usando el botón "Nuevo Producto" arriba</p>
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
  name: 'ProductosView',
  components: {
    ModernTableWrapper  // ✅ NUEVO: Usar el wrapper moderno
  },
  
  data() {
    return {
      productos: [],
      mensajeExito: false,
      clientes: [],
    };
  },

  computed: {
    productosActivos() {
      return this.productos.filter((producto) => !producto.deleted);
    },
  },

  methods: {
    obtenerNombreCliente(producto) {
      if (producto.nombre && Array.isArray(producto.nombre)) {
        return producto.nombre.join(' ');
      }
      // Buscar el cliente en la lista de clientes
      const cliente = this.clientes.find(c => c.id_cliente === producto.id_cliente);
      if (cliente) {
        if (Array.isArray(cliente.nombre)) {
          // Mostrar apellido y nombre (último elemento primero)
          return cliente.nombre.slice(-1)[0] + ' ' + cliente.nombre.slice(0, -1).join(' ');
        }
        return cliente.nombre || 'Sin asignar';
      }
      return 'Sin asignar';
    },

    tieneAccesorios(producto) {
      if (!producto.accesorios) return false;
      if (Array.isArray(producto.accesorios)) {
        return producto.accesorios.some(acc => acc && acc.trim());
      }
      return producto.accesorios.trim();
    },

    obtenerAccesoriosLimpios(accesorios) {
      if (!accesorios) return [];
      if (Array.isArray(accesorios)) {
        return accesorios.filter(acc => acc && acc.trim());
      }
      return [accesorios];
    },

    obtenerProductos() {
      this.$axios.get("http://localhost:8080/productos/traerconcliente")
        .then((response) => {
          console.log("Respuesta de la API:", response.data);
          
          // Ordenar productos por ID
          response.data.sort((a, b) => a.id_producto - b.id_producto);
          
          this.productos = response.data;
          localStorage.setItem("productos", JSON.stringify(response.data));
        })
        .catch((error) => {
          console.error("Error al obtener productos:", error);
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

    eliminarProductoLogicamente(id) {
      const producto = this.productos.find((producto) => producto.id_producto === id);

      if (producto) {
        this.mostrarVentanaEmergente(
          `¿Estás seguro de eliminar el producto "${producto.marca} ${producto.modelo}"?`, 
          () => {
            // Actualiza el estado en el frontend
            producto.deleted = true;
            localStorage.setItem("productos", JSON.stringify(this.productos));

            // Envía la solicitud para eliminar lógicamente en el backend
            this.$axios.delete(`http://localhost:8080/productos/borrar/${id}`)
              .then(() => {
                console.log("Producto eliminado lógicamente:", id);
                this.mensajeExito = true;
                setTimeout(() => {
                  this.mensajeExito = false;
                }, 3000);
              })
              .catch((error) => {
                console.error("Error al eliminar lógicamente el producto:", error);
                // Revierte el cambio en caso de error
                producto.deleted = false;
                localStorage.setItem("productos", JSON.stringify(this.productos));
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
            text: 'El producto ha sido eliminado correctamente.',
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
        text: 'No se pudo eliminar el producto. Intenta nuevamente.',
        icon: 'error',
        confirmButtonText: 'Entendido'
      });
    }
  },

  mounted() {
    // Intenta cargar productos desde el almacenamiento local
    const storedProducts = localStorage.getItem('productos');
    if (storedProducts) {
      try {
        this.productos = JSON.parse(storedProducts);
      } catch (error) {
        console.error('Error al parsear productos del localStorage:', error);
        this.obtenerProductos();
      }
    } else {
      this.obtenerProductos();
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

/* Estilos específicos para productos */
.lista-accesorios {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
}

.lista-accesorios i {
  color: #6c757d;
  font-size: 12px;
}

/* Badge para tipos de productos */
.badge-estado {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.badge-activo {
  background: linear-gradient(135deg, rgba(39, 174, 96, 0.15), rgba(39, 174, 96, 0.1));
  color: #27ae60;
  border: 1px solid rgba(39, 174, 96, 0.2);
}

/* Mejorar la lista de celdas para accesorios */
.lista-celda {
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 13px;
}

.lista-celda li {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 2px 0;
  color: #495057;
}

.lista-celda li i {
  color: #6c757d;
  font-size: 11px;
}

/* Los estilos de shared-table-styles.css se aplican automáticamente */
</style>