<template>
  <!-- ✅ NUEVO: Usar ModernFormWrapper con mejor posicionamiento -->
  <ModernFormWrapper :is-medium="true" class="form-optimizado">
    <h1 class="form-title">{{ modoEdicion ? 'Editar' : 'Registrar' }} orden de trabajo</h1>
    
    <form class="form-content">
      <div class="form-group">
        <label for="presupuesto">Presupuesto:</label>
        <select v-model="nuevaOrden.presupuesto.id_presupuesto" id="presupuesto" class="form-control">
          <option value="" disabled>Seleccione un presupuesto aprobado</option>
          <option v-for="presupuesto in presupuestosAprobados" :key="presupuesto.id_presupuesto"
                  :value="presupuesto.id_presupuesto">
            N° {{ presupuesto.id_presupuesto }} | {{ formatearFecha(presupuesto.fecha) }} | {{ presupuesto.cliente_nombre }}
          </option>
        </select>
        
        <!-- Mensaje informativo si no hay presupuestos aprobados -->
        <div v-if="presupuestos.length > 0 && presupuestosAprobados.length === 0" class="info-no-aprobados">
          <i class="bi bi-info-circle"></i>
          <span>No hay presupuestos aprobados disponibles. Solo se pueden crear órdenes de trabajo para presupuestos aprobados.</span>
        </div>
      </div>

      <div class="form-group">
        <label for="estado">Estado:</label>
        <select v-model="nuevaOrden.estado" id="estado" class="form-control">
          <option value="" disabled>Seleccione un estado</option>
          <option v-for="estado in estadosDisponibles" :key="estado" :value="estado">
            {{ estado }}
          </option>
        </select>
      </div>

      <!-- Información detallada del presupuesto seleccionado -->
      <div v-if="presupuestoSeleccionado" class="presupuesto-info">
        <h4><i class="bi bi-info-circle-fill"></i> Información Completa del Presupuesto</h4>
        
        <!-- Información general del presupuesto -->
        <div class="info-section">
          <h5><i class="bi bi-file-earmark-text"></i> Presupuesto</h5>
          <div class="info-grid">
            <div class="info-item">
              <strong>ID:</strong> {{ presupuestoSeleccionado.id_presupuesto }}
            </div>
            <div class="info-item">
              <strong>Fecha:</strong> {{ formatearFecha(presupuestoSeleccionado.fecha) }}
            </div>
            <div class="info-item">
              <strong>Importe:</strong> {{ formatearImporte(presupuestoSeleccionado.importe) }}
            </div>
            <div class="info-item">
              <strong>Estado:</strong> 
              <span class="badge-aprobado">
                <i class="bi bi-check-circle-fill"></i> Aprobado
              </span>
            </div>
          </div>
          <div v-if="presupuestoSeleccionado.descripcion" class="info-description">
            <strong>Descripción:</strong>
            <p>{{ presupuestoSeleccionado.descripcion }}</p>
          </div>
        </div>

        <!-- Información del cliente -->
        <div class="info-section">
          <h5><i class="bi bi-person-fill"></i> Cliente</h5>
          <div class="info-grid">
            <div class="info-item">
              <strong>Nombre:</strong> {{ presupuestoSeleccionado.cliente_nombre || 'Sin especificar' }}
            </div>
            <div class="info-item" v-if="presupuestoSeleccionado.cliente_email">
              <strong>Email:</strong> 
              <a :href="`mailto:${presupuestoSeleccionado.cliente_email}`" class="contact-link">
                {{ presupuestoSeleccionado.cliente_email }}
              </a>
            </div>
            <div class="info-item" v-if="presupuestoSeleccionado.cliente_telefono">
              <strong>Teléfono:</strong> 
              <a :href="`tel:${presupuestoSeleccionado.cliente_telefono}`" class="contact-link">
                {{ presupuestoSeleccionado.cliente_telefono }}
              </a>
            </div>
            <div class="info-item" v-if="presupuestoSeleccionado.cliente_direccion">
              <strong>Dirección:</strong> {{ presupuestoSeleccionado.cliente_direccion }}
            </div>
          </div>
        </div>

        <!-- Información del producto -->
        <div class="info-section">
          <h5><i class="bi bi-box-seam"></i> Producto a Reparar</h5>
          <div class="info-grid-producto">
            <div class="info-item">
              <strong>Marca:</strong> {{ presupuestoSeleccionado.producto_marca || 'Sin especificar' }}
            </div>
            <div class="info-item">
              <strong>N° Serie:</strong> {{ presupuestoSeleccionado.producto_num_serie || 'Sin especificar' }}
            </div>
          </div>
          <div v-if="presupuestoSeleccionado.producto_accesorios && presupuestoSeleccionado.producto_accesorios.length > 0" class="info-accesorios">
            <strong>Accesorios:</strong>
            <div class="accesorios-list">
              <span v-for="(accesorio, index) in presupuestoSeleccionado.producto_accesorios" 
                    :key="index" 
                    v-if="accesorio"
                    class="accesorio-tag">
                {{ accesorio }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="form-buttons">
        <button v-if="!modoEdicion" type="button" @click="registrarOrden" 
                class="btn gradient-custom-4">
          <i class="bi bi-clipboard-plus-fill"></i>
          Registrar
        </button>
        <button v-if="modoEdicion" type="button" @click="actualizarOrden" 
                class="btn gradient-custom-4">
          <i class="bi bi-pencil-fill"></i>
          Actualizar
        </button>
        <button type="button" @click="cancelarFormulario" 
                class="btn btn-secondary">
          <i class="bi bi-x-circle-fill"></i>
          Cancelar
        </button>
      </div>
    </form>

    <!-- ✅ NUEVOS MENSAJES MEJORADOS - Reemplaza mensajes anteriores -->
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
  </ModernFormWrapper>
</template>

<script>
// ✅ IMPORTS ACTUALIZADOS
import ModernFormWrapper from '@/components/common/ModernFormWrapper.vue';
import EnhancedMessage from '@/components/common/EnhancedMessage.vue'; // ✅ NUEVO

export default {
  name: 'OrdenTrabajoForm',
  components: {
    ModernFormWrapper,
    EnhancedMessage  // ✅ NUEVO: Usar el wrapper moderno
  },

  data() {
    return {
      nuevaOrden: {
        estado: '',
        presupuesto: { id_presupuesto: "" },
      },
      estadosDisponibles: [
        'En reparación',
        'Para ingresar',
        'Completado',
        'En espera de repuestos',
        'Listo para entrega'
      ],
      presupuestos: [],
      ordenesTrabajo: [],
      modoEdicion: false,
      ordenOriginal: null,

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
    presupuestoSeleccionado() {
      if (!this.nuevaOrden.presupuesto.id_presupuesto) return null;
      return this.presupuestos.find(p => p.id_presupuesto == this.nuevaOrden.presupuesto.id_presupuesto);
    },

    // ✅ NUEVO: Filtrar solo presupuestos aprobados
    presupuestosAprobados() {
      return this.presupuestos.filter(presupuesto => presupuesto.aprobado === true);
    }
  },

  methods: {
    // ✅ MÉTODOS MEJORADOS PARA MENSAJES
    showSuccessMessage(message, title = '¡Operación Exitosa!', useModal = true) {
      this.messageType = 'success';
      this.messageText = message;
      this.messageTitle = title;
      this.primaryButtonText = this.modoEdicion ? 'Ir al Listado' : 'Continuar';
      this.showSecondaryButton = !this.modoEdicion;
      
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

    showInfoMessage(message, title = 'Información', useModal = false) {
      this.messageType = 'info';
      this.messageText = message;
      this.messageTitle = title;
      this.primaryButtonText = 'Entendido';
      this.showSecondaryButton = false;
      
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
      if (this.messageType === 'success' && this.modoEdicion) {
        // Redirigir al listado después del éxito en edición
        this.$router.push('/ordeneslistar');
      }
    },

    // ✅ ACTUALIZADO: Obtener presupuestos con información completa de cliente y producto
    obtenerPresupuestos() {
      this.$axios.get('http://localhost:8080/presupuestos/traerconclientes')
        .then(response => {
          console.log('Respuesta de la API presupuestos completos:', response.data);
          // Mapear los datos para tener una estructura más clara
          this.presupuestos = response.data.map(presupuesto => ({
            ...presupuesto,
            // Información del cliente (viene del JOIN)
            cliente_nombre: presupuesto.nombre ? presupuesto.nombre.join(' ') : 'Sin especificar',
            cliente_email: presupuesto.email,
            cliente_telefono: presupuesto.telefono && presupuesto.telefono[0] ? presupuesto.telefono[0] : null,
            cliente_direccion: presupuesto.direccion,
            // Información del producto (solo campos necesarios)
            producto_marca: presupuesto.producto_marca || presupuesto.marca,
            producto_num_serie: presupuesto.producto_num_serie || presupuesto.num_serie,
            producto_accesorios: presupuesto.producto_accesorios || presupuesto.accesorios
          }));
          localStorage.setItem('presupuestos', JSON.stringify(this.presupuestos));
          
          // Si no hay información completa del producto, cargarla
          this.cargarInformacionProductos();
        })
        .catch(error => {
          console.error('Error al obtener presupuestos:', error);
          this.showErrorMessage(
            'No se pudieron cargar los presupuestos. Por favor, intenta nuevamente.',
            'Error de Conexión'
          );
        });
    },

    // ✅ NUEVO: Cargar información adicional de productos si es necesaria
    async cargarInformacionProductos() {
      try {
        // Obtener información completa de productos si hace falta
        for (let presupuesto of this.presupuestos) {
          if (presupuesto.id_producto && (!presupuesto.producto_marca || !presupuesto.producto_num_serie)) {
            try {
              const response = await this.$axios.get(`http://localhost:8080/productos/${presupuesto.id_producto}`);
              if (response.data) {
                presupuesto.producto_marca = response.data.marca;
                presupuesto.producto_num_serie = response.data.num_serie;
                presupuesto.producto_accesorios = response.data.accesorios;
              }
            } catch (error) {
              console.log(`No se pudo cargar información del producto ${presupuesto.id_producto}:`, error);
            }
          }
        }
        
        // Actualizar localStorage con la información completa
        localStorage.setItem('presupuestos', JSON.stringify(this.presupuestos));
      } catch (error) {
        console.error('Error al cargar información adicional de productos:', error);
      }
    },

    obtenerOrdenesTrabajo() {
      console.log("Iniciando obtenerOrdenesTrabajo");
      this.$axios.get("http://localhost:8080/ordenes/traer")
        .then((response) => {
          console.log("Respuesta de la API:", response.data);
          this.ordenesTrabajo = response.data;
          localStorage.setItem("ordenesTrabajo", JSON.stringify(response.data));
        })
        .catch((error) => {
          console.error("Error al obtener órdenes de trabajo:", error);
        });
    },

    // ✅ MÉTODO ACTUALIZADO CON VALIDACIONES MEJORADAS
    validarCampos() {
      // Limpiar mensajes anteriores
      this.closeMessage();

      // Verificar si hay presupuestos aprobados disponibles
      if (this.presupuestosAprobados.length === 0) {
        this.showErrorMessage(
          'No hay presupuestos aprobados disponibles. Solo se pueden crear órdenes de trabajo para presupuestos que han sido aprobados.',
          'Sin Presupuestos Aprobados',
          true
        );
        return false;
      }

      // Verificar campos obligatorios
      if (!this.nuevaOrden.estado || !this.nuevaOrden.presupuesto.id_presupuesto) {
        this.showErrorMessage(
          'El estado y el presupuesto son campos obligatorios para crear una orden de trabajo.',
          'Campos Requeridos'
        );
        return false;
      }

      // Verificar que el presupuesto seleccionado esté aprobado
      const presupuestoSeleccionado = this.presupuestos.find(p => p.id_presupuesto == this.nuevaOrden.presupuesto.id_presupuesto);
      if (presupuestoSeleccionado && !presupuestoSeleccionado.aprobado) {
        this.showErrorMessage(
          'El presupuesto seleccionado no está aprobado. Solo se pueden crear órdenes de trabajo para presupuestos aprobados.',
          'Presupuesto No Aprobado'
        );
        return false;
      }

      return true;
    },

    // ✅ MÉTODO ACTUALIZADO CON MENSAJES MEJORADOS
    registrarOrden() {
      console.log('Datos de la orden a registrar:', this.nuevaOrden);

      if (!this.validarCampos()) return;

      // Registrar nueva orden de trabajo
      this.$axios.post('http://localhost:8080/ordenes/crear', this.nuevaOrden)
        .then(() => {
          this.obtenerOrdenesTrabajo();
          // Reinicia el formulario
          this.nuevaOrden = { 
            estado: '', 
            presupuesto: { id_presupuesto: "" } 
          };
          
          // ✅ MOSTRAR MENSAJE DE ÉXITO MEJORADO
          this.showSuccessMessage(
            'La orden de trabajo se ha registrado correctamente en el sistema. Ahora puedes realizar seguimiento del trabajo.',
            '¡Orden de Trabajo Registrada!',
            true
          );
        })
        .catch(error => {
          console.error('Error al agregar la orden de trabajo:', error);
          this.showErrorMessage(
            'No se pudo registrar la orden de trabajo. Por favor, verifica los datos e intenta nuevamente.',
            'Error en el Registro',
            true
          );
        });
    },

    // ✅ MÉTODO ACTUALIZADO CON MENSAJES MEJORADOS
    actualizarOrden() {
      if (!this.validarCampos()) return;

      this.$axios.put(`http://localhost:8080/ordenes/editar/${this.nuevaOrden.id_orden}`, this.nuevaOrden)
        .then(() => {
          this.obtenerOrdenesTrabajo();
          
          // ✅ MOSTRAR MENSAJE DE ÉXITO MEJORADO
          this.showSuccessMessage(
            'Los cambios de la orden de trabajo se han guardado correctamente.',
            '¡Orden de Trabajo Actualizada!',
            true
          );

          // Actualizar también localStorage del listado
          const storedOrdenes = localStorage.getItem('ordenesTrabajo');
          if (storedOrdenes) {
            try {
              const ordenes = JSON.parse(storedOrdenes);
              const index = ordenes.findIndex(o => o.id_orden === this.nuevaOrden.id_orden);
              if (index !== -1) {
                ordenes[index] = { ...ordenes[index], ...this.nuevaOrden };
                localStorage.setItem('ordenesTrabajo', JSON.stringify(ordenes));
              }
            } catch (error) {
              console.error('Error al actualizar localStorage:', error);
            }
          }
        })
        .catch(error => {
          console.error('Error al actualizar la orden de trabajo:', error);
          this.showErrorMessage(
            'No se pudo actualizar la orden de trabajo. Por favor, intenta nuevamente.',
            'Error en la Actualización',
            true
          );
        });
    },

    cancelarFormulario() {
      // Limpiar datos temporales y volver al listado
      localStorage.removeItem('ordenParaEditar');
      this.$router.push('/ordeneslistar');
    },

    // ✅ ACTUALIZADO: Cargar orden para edición con mejor manejo de datos
    cargarOrdenParaEdicion(ordenId) {
      console.log('Cargando orden para edición, ID:', ordenId);
      
      // 1. Primero intentar cargar desde localStorage temporal (viene del listado)
      const ordenTemporal = localStorage.getItem('ordenParaEditar');
      if (ordenTemporal) {
        try {
          const orden = JSON.parse(ordenTemporal);
          console.log('Orden cargada desde datos temporales:', orden);
          
          this.nuevaOrden = {
            id_orden: orden.id_orden,
            estado: orden.estado || '',
            presupuesto: {
              id_presupuesto: orden.id_presupuesto || orden.presupuesto?.id_presupuesto || ""
            }
          };
          
          this.ordenOriginal = { ...this.nuevaOrden };
          
          // Limpiar datos temporales después de usarlos
          localStorage.removeItem('ordenParaEditar');
          return;
        } catch (error) {
          console.error('Error al parsear orden temporal:', error);
          localStorage.removeItem('ordenParaEditar');
        }
      }

      // 2. Si no hay datos temporales, buscar en la lista local
      const storedOrdenes = localStorage.getItem('ordenesTrabajo');
      if (storedOrdenes) {
        try {
          const ordenes = JSON.parse(storedOrdenes);
          const orden = ordenes.find(o => o.id_orden == ordenId);
          
          if (orden) {
            console.log('Orden encontrada en localStorage:', orden);
            
            this.nuevaOrden = {
              id_orden: orden.id_orden,
              estado: orden.estado || '',
              presupuesto: {
                id_presupuesto: orden.id_presupuesto || ""
              }
            };
            
            this.ordenOriginal = { ...this.nuevaOrden };
            return;
          }
        } catch (error) {
          console.error('Error al parsear órdenes del localStorage:', error);
        }
      }
      
      // 3. Como último recurso, hacer petición al servidor
      console.log('Buscando orden en el servidor...');
      this.obtenerOrdenesTrabajo();
      
      // Después de obtener las órdenes, intentar cargar la específica
      this.$nextTick(() => {
        const orden = this.ordenesTrabajo.find(o => o.id_orden == ordenId);
        if (orden) {
          this.nuevaOrden = {
            id_orden: orden.id_orden,
            estado: orden.estado || '',
            presupuesto: {
              id_presupuesto: orden.id_presupuesto || ""
            }
          };
          this.ordenOriginal = { ...this.nuevaOrden };
        } else {
          console.warn('No se encontró la orden con ID:', ordenId);
          this.showErrorMessage(
            'No se encontró la orden de trabajo especificada.',
            'Orden No Encontrada'
          );
        }
      });
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
    }
  },

  created() {
    console.log('Inicializando formulario de orden de trabajo');
    console.log('Ruta actual:', this.$route.path);
    console.log('Parámetros de ruta:', this.$route.params);
    
    // Detectar si estamos en modo edición basado en la ruta
    if (this.$route.params.id) {
      console.log('Modo edición detectado, ID:', this.$route.params.id);
      this.modoEdicion = true;
      
      // Cargar presupuestos primero, luego la orden
      this.obtenerPresupuestos();
      this.cargarOrdenParaEdicion(this.$route.params.id);
    } else {
      console.log('Modo registro - formulario nuevo');
      this.modoEdicion = false;
      this.obtenerOrdenesTrabajo();
      this.obtenerPresupuestos();
    }
  },

  watch: {
    // Observar cambios en la ruta para manejar navegación dinámica
    '$route'(to, from) {
      console.log('Cambio de ruta detectado:', to.path, 'parámetros:', to.params);
      
      if (to.params.id) {
        this.modoEdicion = true;
        this.cargarOrdenParaEdicion(to.params.id);
      } else {
        this.modoEdicion = false;
        this.nuevaOrden = { 
          estado: '', 
          presupuesto: { id_presupuesto: "" } 
        };
        this.ordenOriginal = null;
      }
    }
  }
}
</script>

<style scoped>
/* ============================================ */
/* OVERRIDE ULTRA AGRESIVO DE ESTILOS GLOBALES */
/* ============================================ */

/* ✅ NEUTRALIZAR shared-form-styles.css COMPLETAMENTE */
.form-optimizado .formulario-container .form-content {
  margin-top: 0 !important;
  padding-top: 0 !important;
  display: flex !important;
  flex-direction: column !important;
}

.form-optimizado .formulario-container .form-group {
  margin-bottom: 8px !important; /* ✅ SUPER COMPACTO */
  display: flex !important;
  flex-direction: row !important;
  align-items: center !important;
}

.form-optimizado .formulario-container .form-group label {
  width: 120px !important;
  text-align: right !important;
  margin-right: 15px !important;
  font-weight: 500 !important;
  color: #555 !important;
  margin-bottom: 0 !important; /* ✅ FORZAR sin margin */
}

.form-optimizado .formulario-container .form-group .form-control {
  flex: 1 !important;
  margin-left: 10px !important;
  padding: 6px 10px !important; /* ✅ PADDING MÁS PEQUEÑO */
  border: 1px solid #ddd !important;
  border-radius: 5px !important;
  font-size: 14px !important;
  margin-bottom: 0 !important; /* ✅ FORZAR sin margin */
}

.form-optimizado .formulario-container .form-title {
  text-align: center !important;
  font-size: 18px !important; /* ✅ MÁS PEQUEÑO */
  margin-bottom: 8px !important; /* ✅ SUPER COMPACTO */
  margin-top: 0 !important;
  color: #333 !important;
  font-weight: 600 !important;
}

.form-optimizado .formulario-container .form-buttons {
  padding: 5px 20px !important; /* ✅ PADDING MÍNIMO */
  margin-bottom: 0 !important;
  margin-top: 10px !important; /* ✅ ESPACIO MÍNIMO */
  display: flex !important;
  gap: 10px !important;
  justify-content: center !important;
}

/* ✅ FORZAR altura del contenedor para que sea visible completo */
.form-optimizado .formulario-container {
  margin: 0 !important;
  padding: 12px !important; /* ✅ PADDING SÚPER COMPACTO */
  max-height: none !important;
  height: auto !important;
  box-sizing: border-box !important;
}

/* ✅ OVERRIDE del layout moderno para formularios */
.form-optimizado .modern-content-area {
  padding: 8px 40px !important; /* ✅ PADDING MÍNIMO */
  align-items: flex-start !important;
  justify-content: center !important;
}

/* ============================================ */
/* ESTILOS MODERNOS MEJORADOS */
/* ============================================ */

/* Mejorar spacing para vista completa */
.form-group {
  margin-bottom: 10px !important; /* ✅ MÁS COMPACTO */
}

.form-title {
  margin-bottom: 12px !important; /* ✅ MÁS COMPACTO */
  margin-top: 0 !important; /* ✅ Sin margin superior */
}

.form-buttons {
  margin-top: 15px !important; /* ✅ MÁS COMPACTO */
  padding: 0 20px !important; /* ✅ Override padding global */
}

.form-content {
  margin-top: 0 !important; /* ✅ Override estilos globales */
  padding-top: 0 !important;
}

/* ✅ FORZAR que el wrapper no agregue espacios extra */
.form-optimizado .formulario-container {
  margin: 0 !important;
  padding: 15px !important; /* ✅ Padding más compacto */
}

/* Los gradientes originales mejorados */
.gradient-custom-3 {
  background: linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
}

.gradient-custom-4 {
  background: linear-gradient(135deg, rgba(132, 250, 176, 1), rgba(143, 211, 244, 1));
  transition: all 0.3s ease;
}

.gradient-custom-4:hover {
  background: linear-gradient(135deg, rgba(143, 211, 244, 1), rgba(132, 250, 176, 1));
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(132, 250, 176, 0.3);
}

/* ============================================ */
/* BOTONES MODERNOS CON ICONOS */
/* ============================================ */
.form-buttons {
  display: flex;
  gap: 15px;
  align-items: center;
  justify-content: center;
  margin-top: 25px;
  padding: 0 10px;
}

.form-buttons .btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s ease;
  min-width: 140px;
  justify-content: center;
}

.btn-secondary {
  background: linear-gradient(135deg, #6c757d, #5a6268);
  color: white;
  box-shadow: 0 4px 15px rgba(108, 117, 125, 0.3);
}

.btn-secondary:hover {
  background: linear-gradient(135deg, #5a6268, #495057);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(108, 117, 125, 0.4);
  color: white;
  text-decoration: none;
}

/* ============================================ */
/* MEJORAS ADICIONALES DEL FORMULARIO */
/* ============================================ */
.form-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  text-align: center;
  font-size: 22px;
  margin-bottom: 20px;
  margin-top: 0;
  color: #2c3e50;
  font-weight: 700;
}

.form-title::before {
  content: '';
  width: 4px;
  height: 24px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  border-radius: 2px;
}

/* Mejorar los inputs con efectos modernos */
.form-group .form-control:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
  transform: translateY(-1px);
}

/* Hover effects sutiles */
.form-group:hover .form-control {
  border-color: rgba(52, 152, 219, 0.5);
  transition: all 0.2s ease;
}

/* ============================================ */
/* INFORMACIÓN DETALLADA DEL PRESUPUESTO */
/* ============================================ */
.presupuesto-info {
  background: linear-gradient(135deg, rgba(52, 152, 219, 0.1), rgba(52, 152, 219, 0.05)) !important;
  border: 1px solid rgba(52, 152, 219, 0.2) !important;
  border-radius: 16px !important;
  padding: 20px !important;
  margin: 15px 10px !important;
  box-shadow: 0 6px 20px rgba(52, 152, 219, 0.1) !important;
  transition: all 0.3s ease !important;
}

.presupuesto-info:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px rgba(52, 152, 219, 0.15) !important;
}

.presupuesto-info h4 {
  display: flex !important;
  align-items: center !important;
  gap: 10px !important;
  margin: 0 0 20px 0 !important;
  font-size: 18px !important;
  color: #2c3e50 !important;
  font-weight: 700 !important;
  padding-bottom: 12px !important;
  border-bottom: 2px solid rgba(52, 152, 219, 0.2) !important;
}

.presupuesto-info h4 i {
  color: #3498db !important;
  font-size: 20px !important;
}

/* Secciones de información */
.info-section {
  margin-bottom: 20px !important;
  background: rgba(255, 255, 255, 0.7) !important;
  padding: 16px !important;
  border-radius: 12px !important;
  border: 1px solid rgba(52, 152, 219, 0.1) !important;
}

.info-section:last-child {
  margin-bottom: 0 !important;
}

.info-section h5 {
  display: flex !important;
  align-items: center !important;
  gap: 8px !important;
  margin: 0 0 12px 0 !important;
  font-size: 16px !important;
  font-weight: 600 !important;
  color: #34495e !important;
}

.info-section h5 i {
  color: #3498db !important;
  font-size: 16px !important;
}

.info-grid {
  display: grid !important;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)) !important;
  gap: 12px !important;
}

/* Grid específico para información del producto (2 columnas) */
.info-grid-producto {
  display: grid !important;
  grid-template-columns: 1fr 1fr !important;
  gap: 12px !important;
}

.info-item {
  background: white !important;
  padding: 12px 14px !important;
  border-radius: 8px !important;
  font-size: 14px !important;
  border: 1px solid rgba(52, 152, 219, 0.1) !important;
  transition: all 0.2s ease !important;
}

.info-item:hover {
  border-color: rgba(52, 152, 219, 0.3) !important;
  transform: translateY(-1px) !important;
}

.info-item strong {
  color: #2c3e50 !important;
  display: block !important;
  margin-bottom: 4px !important;
  font-size: 13px !important;
  text-transform: uppercase !important;
  letter-spacing: 0.5px !important;
}

/* Descripción especial */
.info-description {
  margin-top: 12px !important;
  padding: 12px !important;
  background: rgba(52, 152, 219, 0.05) !important;
  border-radius: 8px !important;
  border-left: 4px solid #3498db !important;
}

.info-description strong {
  color: #2c3e50 !important;
  display: block !important;
  margin-bottom: 6px !important;
  font-size: 14px !important;
}

.info-description p {
  margin: 0 !important;
  color: #555 !important;
  line-height: 1.5 !important;
  font-style: italic !important;
}

/* Enlaces de contacto */
.contact-link {
  color: #3498db !important;
  text-decoration: none !important;
  transition: all 0.2s ease !important;
}

.contact-link:hover {
  color: #2980b9 !important;
  text-decoration: underline !important;
}

/* Accesorios */
.info-accesorios {
  margin-top: 12px !important;
  padding: 12px !important;
  background: rgba(39, 174, 96, 0.05) !important;
  border-radius: 8px !important;
  border-left: 4px solid #27ae60 !important;
}

.info-accesorios strong {
  color: #2c3e50 !important;
  display: block !important;
  margin-bottom: 8px !important;
  font-size: 14px !important;
}

.accesorios-list {
  display: flex !important;
  flex-wrap: wrap !important;
  gap: 6px !important;
}

.accesorio-tag {
  background: #27ae60 !important;
  color: white !important;
  padding: 4px 8px !important;
  border-radius: 12px !important;
  font-size: 12px !important;
  font-weight: 500 !important;
}

.badge-aprobado {
  background: linear-gradient(135deg, rgba(39, 174, 96, 0.15), rgba(39, 174, 96, 0.1)) !important;
  color: #27ae60 !important;
  padding: 4px 12px !important;
  border-radius: 16px !important;
  font-size: 12px !important;
  font-weight: 600 !important;
  border: 1px solid rgba(39, 174, 96, 0.2) !important;
  display: inline-flex !important;
  align-items: center !important;
  gap: 4px !important;
}

.badge-aprobado i {
  font-size: 12px !important;
}

/* Mensaje informativo cuando no hay presupuestos aprobados */
.info-no-aprobados {
  display: flex !important;
  align-items: center !important;
  gap: 8px !important;
  margin-top: 8px !important;
  margin-left: 10px !important;
  padding: 12px 16px !important;
  background: linear-gradient(135deg, rgba(255, 193, 7, 0.1), rgba(255, 193, 7, 0.05)) !important;
  border: 1px solid rgba(255, 193, 7, 0.3) !important;
  border-radius: 8px !important;
  color: #856404 !important;
  font-size: 13px !important;
  font-weight: 500 !important;
}

.info-no-aprobados i {
  color: #ffc107 !important;
  font-size: 16px !important;
  flex-shrink: 0 !important;
}

/* ============================================ */
/* MEJORAS PARA COMPATIBILIDAD CON MENSAJES MEJORADOS */
/* ============================================ */

/* Asegurar que los mensajes overlay no interfieran con el layout */
.form-optimizado {
  position: relative !important;
}

/* Eliminar mensajes anteriores para evitar duplicados */
.form-message {
  display: none !important; /* ✅ Ocultar mensajes antiguos */
}

/* Mejorar el z-index del wrapper para que los overlays funcionen bien */
.form-optimizado .formulario-container {
  z-index: 1 !important;
  position: relative !important;
}

/* Asegurar que los botones no interfieran con los overlays */
.form-buttons {
  z-index: 2 !important;
  position: relative !important;
}

/* ============================================ */
/* ANIMACIONES ADICIONALES PARA MEJOR UX */
/* ============================================ */
.form-content {
  transition: opacity 0.3s ease !important;
}

.form-content.loading {
  opacity: 0.7 !important;
  pointer-events: none !important;
}

/* ============================================ */
/* RESPONSIVE MEJORADO */
/* ============================================ */
@media (max-width: 480px) {
  .form-buttons {
    flex-direction: column;
    gap: 10px;
  }
  
  .form-buttons .btn {
    width: 100%;
    min-width: auto;
  }
  
  .form-title {
    font-size: 20px;
  }

  .info-grid {
    grid-template-columns: 1fr !important;
  }

  .info-grid-producto {
    grid-template-columns: 1fr !important;
  }

  /* Información del presupuesto responsive */
  .presupuesto-info {
    margin: 15px 5px !important;
    padding: 16px !important;
  }

  .presupuesto-info h4 {
    font-size: 16px !important;
  }

  .info-section {
    padding: 12px !important;
  }

  .info-section h5 {
    font-size: 14px !important;
  }

  .info-item {
    padding: 10px 12px !important;
  }

  .accesorios-list {
    justify-content: flex-start !important;
  }

  .accesorio-tag {
    font-size: 11px !important;
    padding: 3px 6px !important;
  }

  .info-no-aprobados {
    margin-left: 5px !important;
    padding: 10px 12px !important;
    font-size: 12px !important;
  }

  /* Asegurar que en móvil los mensajes se vean bien */
  .message-overlay {
    padding: 10px !important;
  }
}
</style>