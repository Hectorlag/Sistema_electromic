<template>
  <!-- ✅ NUEVO: Usar ModernFormWrapper con mejor posicionamiento -->
  <ModernFormWrapper :is-medium="true" class="form-optimizado">
    <h1 class="form-title">{{ esEdicion ? 'Editar' : 'Registrar' }} presupuesto</h1>
    
    <!-- Indicador de carga -->
    <div v-if="cargandoDatos" class="loading-indicator">
      <i class="bi bi-hourglass-split"></i>
      <p>Cargando datos del presupuesto...</p>
    </div>

    <form class="form-content" v-show="!cargandoDatos">
      <div class="form-group">
        <label for="descripcion">Descripción:</label>
        <textarea v-model="nuevoPresupuesto.descripcion" id="descripcion" 
                  class="form-control" rows="2" 
                  placeholder="Descripción del trabajo a realizar">
        </textarea>
      </div>

      <div class="form-group">
        <label for="fecha">Fecha:</label>
        <input v-model="nuevoPresupuesto.fecha" type="date" id="fecha" class="form-control" />
      </div>

      <div class="form-group">
        <span class="form-label-text">Aprobado:</span>
        <div class="toggle-switch-container">
          <label class="toggle-switch">
            <input 
              v-model="nuevoPresupuesto.aprobado" 
              type="checkbox" 
              class="toggle-input"
            />
            <span class="toggle-slider">
              <span class="toggle-text toggle-text-no">No</span>
              <span class="toggle-text toggle-text-si">Sí</span>
            </span>
          </label>
        </div>
      </div>

      <div class="form-group">
        <label for="cliente">Cliente:</label>
        <select v-model="nuevoPresupuesto.cli" @change="actualizarCliente" 
                id="cliente" class="form-control">
          <option value="" disabled>Seleccione un cliente</option>
          <option v-for="cliente in clientes" :key="cliente.id_cliente" :value="cliente">
            {{ cliente.nombre.slice(-1)[0] }} {{ cliente.nombre.slice(0, -1).join(' ') }}
          </option>
        </select>
        
        <div v-if="nuevoPresupuesto.cli && nuevoPresupuesto.cli.id_cliente" 
             class="cliente-seleccionado">
          <div class="cliente-info-header">
            <i class="bi bi-person-check-fill"></i>
            <span>Cliente Seleccionado</span>
          </div>
          <div class="cliente-info-body">
            <div class="cliente-info-item">
              <i class="bi bi-person-badge"></i>
              <span>{{ nuevoPresupuesto.cli.nombre.join(' ') }}</span>
            </div>
            <div class="cliente-info-item" v-if="nuevoPresupuesto.cli.email">
              <i class="bi bi-envelope"></i>
              <span>{{ nuevoPresupuesto.cli.email }}</span>
            </div>
            <div class="cliente-info-item" v-if="nuevoPresupuesto.cli.telefono && nuevoPresupuesto.cli.telefono[0]">
              <i class="bi bi-telephone"></i>
              <span>{{ nuevoPresupuesto.cli.telefono[0] }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="form-group">
        <label for="producto">Productos del Cliente:</label>
        <select v-model="productoSeleccionado" id="producto" class="form-control">
          <option value="">Seleccione un producto</option>
          <option v-for="producto in productosDelCliente" :key="producto.id_producto" 
                  :value="producto">
            {{ producto.marca }} {{ producto.modelo }} • {{ producto.tipo }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="importe">Importe:</label>
        <input v-model="nuevoPresupuesto.importe" type="number" min="0" step="0.01"
               id="importe" class="form-control" placeholder="0.00" />
      </div>

      <div class="form-buttons">
        <button v-if="!esEdicion" type="button" @click="guardarPresupuesto" 
                class="btn gradient-custom-4">
          <i class="bi bi-file-earmark-plus-fill"></i>
          Registrar
        </button>
        <button v-if="esEdicion" type="button" @click="guardarPresupuesto" 
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

      <div v-if="mensajeError" class="form-message form-error">
        <i class="bi bi-exclamation-triangle-fill"></i>
        {{ mensajeError }}
      </div>
      <div v-if="registroExitoso" class="form-message form-success">
        <i class="bi bi-check-circle-fill"></i>
        {{ esEdicion ? '¡Actualización exitosa!' : '¡Registro exitoso!' }}
      </div>
    </form>
  </ModernFormWrapper>
</template>

<script>
// ✅ CAMBIO PRINCIPAL: Import del nuevo wrapper moderno
import ModernFormWrapper from '@/components/common/ModernFormWrapper.vue';

export default {
  name: 'PresupuestoForm',
  components: {
    ModernFormWrapper  // ✅ NUEVO: Usar el wrapper moderno
  },

  data() {
    return {
      nuevoPresupuesto: {
        id_presupuesto: null,
        descripcion: '', 
        fecha: '', 
        aprobado: false,
        cli: { id_cliente: "" },
        producto: { id_producto: "" },
        importe: ''
      },
      productoSeleccionado: '',
      registroExitoso: false,
      clientes: [],
      presupuestos: [],
      mensajeError: '',
      productosDelCliente: [],
      cargandoDatos: false,
    };
  },

  computed: {
    esEdicion() {
      return this.$route.params.id !== undefined && this.$route.params.id !== null && this.$route.params.id !== '';
    },

    presupuestoId() {
      return this.$route.params.id;
    }
  },

  watch: {
    // Detectar cambios en la ruta para limpiar el formulario al cambiar de editar a crear
    '$route'(to, from) {
      console.log('Cambio de ruta detectado:', to.path);
      if (!this.esEdicion) {
        console.log('Cambiando a modo registro - limpiando formulario');
        this.limpiarFormulario();
      }
    }
  },

  methods: {
    async obtenerPresupuestos() {
      try {
        console.log("Iniciando obtenerPresupuestos");
        const response = await this.$axios.get("http://localhost:8080/presupuestos/traerconclientes");
        console.log("Respuesta de la API:", response.data);
        this.presupuestos = response.data.map(presupuesto => ({ ...presupuesto, deleted: false }));
        localStorage.setItem("presupuestos", JSON.stringify(response.data));
      } catch (error) {
        console.error("Error al obtener presupuestos:", error);
      }
    },

    async obtenerClientes() {
      try {
        const response = await this.$axios.get('http://localhost:8080/clientes/traer');
        console.log('Respuesta de la API:', response.data);
        this.clientes = response.data;
        localStorage.setItem('clientes', JSON.stringify(response.data));
      } catch (error) {
        console.error('Error al obtener clientes:', error);
      }
    },

    async cargarPresupuestoParaEditar() {
      if (!this.esEdicion) return;
      
      this.cargandoDatos = true;
      try {
        // Intentar obtener desde localStorage primero
        let presupuesto = null;
        const presupuestosGuardados = localStorage.getItem('presupuestos');
        
        if (presupuestosGuardados) {
          const presupuestos = JSON.parse(presupuestosGuardados);
          presupuesto = presupuestos.find(p => p.id_presupuesto == this.presupuestoId);
          console.log('Presupuesto encontrado en localStorage:', presupuesto);
        }

        // Si no está en localStorage, intentar obtener del servidor
        if (!presupuesto) {
          console.log('No encontrado en localStorage, buscando en servidor...');
          try {
            // Primero obtener todos los presupuestos si no los tenemos
            await this.obtenerPresupuestos();
            const presupuestosActualizados = localStorage.getItem('presupuestos');
            if (presupuestosActualizados) {
              const presupuestos = JSON.parse(presupuestosActualizados);
              presupuesto = presupuestos.find(p => p.id_presupuesto == this.presupuestoId);
            }
          } catch (error) {
            console.error('Error al obtener presupuestos del servidor:', error);
          }
        }

        if (presupuesto) {
          console.log('Cargando datos del presupuesto:', presupuesto);
          
          // Cargar datos del presupuesto
          this.nuevoPresupuesto = {
            id_presupuesto: presupuesto.id_presupuesto,
            descripcion: presupuesto.descripcion || '',
            fecha: presupuesto.fecha || '',
            aprobado: presupuesto.aprobado || false,
            cli: { 
              id_cliente: presupuesto.id_cliente,
              nombre: presupuesto.nombre || []
            },
            producto: { 
              id_producto: presupuesto.id_producto || ""
            },
            importe: presupuesto.importe || ''
          };

          // Buscar el cliente completo en la lista
          const clienteCompleto = this.clientes.find(c => c.id_cliente === presupuesto.id_cliente);
          if (clienteCompleto) {
            this.nuevoPresupuesto.cli = clienteCompleto;
            await this.actualizarCliente();
            
            // Seleccionar el producto correspondiente
            if (presupuesto.id_producto) {
              const producto = this.productosDelCliente.find(p => p.id_producto == presupuesto.id_producto);
              if (producto) {
                this.productoSeleccionado = producto;
              }
            }
          }
          
          console.log('Datos cargados correctamente');
        } else {
          console.warn('No se encontró el presupuesto con ID:', this.presupuestoId);
          this.mensajeError = 'No se encontró el presupuesto especificado.';
          this.mostrarMensajeTemporalmente('error');
        }
      } catch (error) {
        console.error('Error al cargar presupuesto para editar:', error);
        this.mensajeError = 'Error al cargar los datos del presupuesto.';
        this.mostrarMensajeTemporalmente('error');
      } finally {
        this.cargandoDatos = false;
      }
    },

    async actualizarCliente() {
      // Verifica si se ha seleccionado un cliente
      if (this.nuevoPresupuesto.cli && this.nuevoPresupuesto.cli.id_cliente) {
        try {
          const response = await this.$axios.get(`http://localhost:8080/productos/uncliente/${this.nuevoPresupuesto.cli.id_cliente}`);
          console.log("Respuesta del servidor:", response);
          
          if (response.status === 200) {
            if (Array.isArray(response.data) && response.data.length > 0) {
              console.log("Productos del cliente:", response.data);
              this.productosDelCliente = response.data;
              if (!this.esEdicion) {
                this.productoSeleccionado = '';
              }
            } else if (response.data && typeof response.data === 'string') {
              console.error("Error del servidor:", response.data);
              this.productosDelCliente = [];
              this.productoSeleccionado = '';
              this.mensajeError = response.data;
              this.mostrarMensajeTemporalmente('error');
            }
          }
        } catch (error) {
          console.error("Error al obtener productos del cliente:", error);
          this.productosDelCliente = [];
          this.productoSeleccionado = '';
          this.mensajeError = "Error al obtener productos del cliente.";
          this.mostrarMensajeTemporalmente('error');
        }
      } else {
        this.productosDelCliente = [];
        this.productoSeleccionado = '';
      }
    },

    async guardarPresupuesto() {
      if (this.esEdicion) {
        await this.actualizarPresupuesto();
      } else {
        await this.registrarPresupuesto();
      }
    },

    async registrarPresupuesto() {
      // Validaciones del lado del cliente
      if (!this.nuevoPresupuesto.descripcion || !this.nuevoPresupuesto.fecha || !this.productoSeleccionado) {
        console.error('Los campos descripcion, fecha y cliente son obligatorios.');
        this.mensajeError = 'Los campos descripción, fecha, cliente y producto son obligatorios.';
        this.mostrarMensajeTemporalmente('error');
        return;
      }

      // Verifica si la fecha ingresada es anterior a la fecha actual
      const fechaIngresada = new Date(this.nuevoPresupuesto.fecha);
      const fechaActual = new Date();

      if (fechaIngresada.setDate(fechaIngresada.getDate() + 1) <= fechaActual) {
        console.error('La fecha ingresada no puede ser anterior a la fecha actual.');
        this.mensajeError = 'La fecha ingresada no puede ser anterior a la fecha actual.';
        this.mostrarMensajeTemporalmente('error');
        return;
      }

      // Asigna el ID del producto seleccionado
      this.nuevoPresupuesto.producto.id_producto = this.productoSeleccionado.id_producto;
      console.log(this.nuevoPresupuesto.producto.id_producto);

      // Convierte el objeto proxy a un objeto plano
      const presupuestoPlano = JSON.parse(JSON.stringify(this.nuevoPresupuesto));

      try {
        await this.$axios.post('http://localhost:8080/presupuestos/crear', presupuestoPlano);
        await this.obtenerPresupuestos();
        this.limpiarFormulario();
        this.registroExitoso = true;
        this.mostrarMensajeTemporalmente('success');
      } catch (error) {
        console.error('Error al agregar el presupuesto:', error);
        this.mensajeError = 'Error al registrar el presupuesto. Intente nuevamente.';
        this.mostrarMensajeTemporalmente('error');
      }
    },

    async actualizarPresupuesto() {
      // Validaciones del lado del cliente
      if (!this.nuevoPresupuesto.descripcion || !this.nuevoPresupuesto.fecha || !this.productoSeleccionado) {
        this.mensajeError = 'Los campos descripción, fecha, cliente y producto son obligatorios.';
        this.mostrarMensajeTemporalmente('error');
        return;
      }

      // Verifica si la fecha ingresada es anterior a la fecha actual
      const fechaIngresada = new Date(this.nuevoPresupuesto.fecha);
      const fechaActual = new Date();

      if (fechaIngresada.setDate(fechaIngresada.getDate() + 1) <= fechaActual) {
        this.mensajeError = 'La fecha ingresada no puede ser anterior a la fecha actual.';
        this.mostrarMensajeTemporalmente('error');
        return;
      }

      // Asigna el ID del producto seleccionado
      this.nuevoPresupuesto.producto.id_producto = this.productoSeleccionado.id_producto;

      // Convierte el objeto proxy a un objeto plano
      const presupuestoPlano = JSON.parse(JSON.stringify(this.nuevoPresupuesto));

      try {
        await this.$axios.put(`http://localhost:8080/presupuestos/editar/${this.presupuestoId}`, presupuestoPlano);
        await this.obtenerPresupuestos();
        this.registroExitoso = true;
        this.mostrarMensajeTemporalmente('success');
        
        // Redirigir a la lista después de un tiempo
        setTimeout(() => {
          this.$router.push('/presupuestoslistar');
        }, 2000);
      } catch (error) {
        console.error('Error al actualizar el presupuesto:', error);
        this.mensajeError = 'Error al actualizar el presupuesto. Intente nuevamente.';
        this.mostrarMensajeTemporalmente('error');
      }
    },

    limpiarFormulario() {
      console.log('Limpiando formulario');
      this.nuevoPresupuesto = {
        id_presupuesto: null,
        descripcion: '', 
        fecha: '', 
        aprobado: false,
        cli: { id_cliente: "" },
        producto: { id_producto: '' },
        importe: ''
      };
      this.productoSeleccionado = '';
      this.productosDelCliente = [];
      this.mensajeError = '';
      this.registroExitoso = false;
      this.cargandoDatos = false;
    },

    mostrarMensajeTemporalmente(tipo) {
      setTimeout(() => {
        if (tipo === 'error') {
          this.mensajeError = '';
        } else {
          this.registroExitoso = false;
        }
      }, 4000);
    },

    cancelarFormulario() {
      // Volver al listado sin guardar cambios
      this.$router.push('/presupuestoslistar');
    }
  },

  async created() {
    try {
      console.log('Inicializando formulario de presupuesto');
      console.log('Ruta actual:', this.$route.path);
      console.log('Parámetros de ruta:', this.$route.params);
      console.log('Es edición?', this.esEdicion);
      
      // Cargar clientes primero
      await this.obtenerClientes();
      
      // Luego cargar presupuestos 
      await this.obtenerPresupuestos();
      
      // Si está en modo edición, cargar los datos del presupuesto
      if (this.esEdicion) {
        console.log('Cargando presupuesto para editar, ID:', this.presupuestoId);
        await this.cargarPresupuestoParaEditar();
      } else {
        console.log('Modo registro - formulario limpio');
        // Asegurar que el formulario esté limpio en modo registro
        this.limpiarFormulario();
      }
    } catch (error) {
      console.error('Error al inicializar el componente:', error);
      this.mensajeError = 'Error al inicializar el formulario.';
      this.mostrarMensajeTemporalmente('error');
    }
  },
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
  width: 110px !important; /* ✅ REDUCIDO DRÁSTICAMENTE para dar espacio a radio buttons */
  text-align: right !important;
  margin-right: 15px !important;
  font-weight: 500 !important;
  color: #555 !important;
  margin-bottom: 0 !important; /* ✅ FORZAR sin margin */
}

/* ✅ NUEVO: Span con EXACTA tipografía de otros labels */
.form-optimizado .formulario-container .form-label-text {
  display: inline-block !important;
  width: 110px !important; /* ✅ MISMO ancho que otros labels */
  text-align: right !important; /* ✅ MISMA alineación */
  margin-right: 15px !important; /* ✅ MISMO margin que otros labels */
  font-weight: 500 !important; /* ✅ MISMO peso de fuente */
  color: #555 !important; /* ✅ MISMO color */
  font-size: 14px !important; /* ✅ MISMO tamaño */
  font-family: inherit !important; /* ✅ MISMA familia de fuente */
  line-height: 1 !important;
  flex-shrink: 0 !important;
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

.form-optimizado .formulario-container .form-group textarea.form-control {
  resize: vertical !important;
  min-height: 60px !important;
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
/* MENSAJES MEJORADOS CON ICONOS - CORREGIDO */
/* ============================================ */
.form-message {
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  gap: 10px !important;
  text-align: center !important;
  margin: 15px auto !important;
  padding: 12px 16px !important;
  border-radius: 8px !important;
  font-size: 14px !important;
  font-weight: 500 !important;
  animation: slideIn 0.3s ease-out !important;
  width: calc(100% - 20px) !important;
  max-width: 400px !important;
  box-sizing: border-box !important;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-error {
  color: #e74c3c !important;
  background: linear-gradient(135deg, rgba(231, 76, 60, 0.1), rgba(231, 76, 60, 0.05)) !important;
  border: 1px solid rgba(231, 76, 60, 0.2) !important;
  box-shadow: 0 4px 15px rgba(231, 76, 60, 0.1) !important;
}

.form-success {
  color: #27ae60 !important;
  background: linear-gradient(135deg, rgba(39, 174, 96, 0.1), rgba(39, 174, 96, 0.05)) !important;
  border: 1px solid rgba(39, 174, 96, 0.2) !important;
  box-shadow: 0 4px 15px rgba(39, 174, 96, 0.1) !important;
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
/* ESTILOS ESPECÍFICOS PARA PRESUPUESTOS */
/* ============================================ */

/* ============================================ */
/* TOGGLE SWITCH MODERNO - SOLUCIÓN USER-FRIENDLY */
/* ============================================ */

.form-optimizado .formulario-container .toggle-switch-container {
  display: flex !important;
  align-items: center !important;
  flex: 1 !important;
  margin-left: 10px !important;
  padding: 8px 0 !important;
}

.form-optimizado .formulario-container .toggle-switch {
  position: relative !important;
  display: inline-block !important;
  width: 70px !important; /* ✅ Más compacto - reducido de 100px */
  height: 28px !important; /* ✅ Más bajo - reducido de 36px */
  cursor: pointer !important;
}

.form-optimizado .formulario-container .toggle-input {
  opacity: 0 !important;
  width: 0 !important;
  height: 0 !important;
  position: absolute !important;
}

.form-optimizado .formulario-container .toggle-slider {
  position: absolute !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  bottom: 0 !important;
  background: linear-gradient(135deg, #dc3545, #c82333) !important; /* ✅ Rojo para "No" */
  border-radius: 14px !important; /* ✅ Más redondeado proporcionalmente */
  transition: all 0.3s ease !important;
  display: flex !important;
  align-items: center !important;
  justify-content: space-between !important;
  padding: 2px !important; /* ✅ Padding más pequeño */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15) !important;
}

.form-optimizado .formulario-container .toggle-slider::before {
  content: '' !important;
  position: absolute !important;
  height: 24px !important; /* ✅ Más pequeño */
  width: 33px !important; /* ✅ Ancho simétrico - exactamente la mitad */
  left: 2px !important;
  bottom: 2px !important;
  background-color: white !important;
  border-radius: 12px !important; /* ✅ Proporcionalmente redondeado */
  transition: all 0.3s ease !important;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2) !important;
  z-index: 2 !important;
}

.form-optimizado .formulario-container .toggle-input:checked + .toggle-slider {
  background: linear-gradient(135deg, #28a745, #20c997) !important; /* ✅ Verde para "Sí" */
}

.form-optimizado .formulario-container .toggle-input:checked + .toggle-slider::before {
  transform: translateX(33px) !important; /* ✅ Exactamente la mitad del ancho */
}

.form-optimizado .formulario-container .toggle-text {
  font-size: 10px !important; /* ✅ Texto más pequeño */
  font-weight: 600 !important;
  color: white !important;
  text-transform: uppercase !important;
  letter-spacing: 0.3px !important; /* ✅ Menos espacio entre letras */
  z-index: 1 !important;
  transition: all 0.3s ease !important;
  pointer-events: none !important;
  width: 33px !important; /* ✅ Ancho simétrico */
  text-align: center !important; /* ✅ Centrado perfecto */
}

.form-optimizado .formulario-container .toggle-text-no {
  margin-left: 2px !important; /* ✅ Margen mínimo */
}

.form-optimizado .formulario-container .toggle-text-si {
  margin-right: 2px !important; /* ✅ Margen mínimo simétrico */
}

/* ✅ Estados hover para mejor UX */
.form-optimizado .formulario-container .toggle-switch:hover .toggle-slider {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.25) !important;
  transform: translateY(-1px) !important;
}

.form-optimizado .formulario-container .toggle-switch:hover .toggle-slider::before {
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.3) !important;
}

/* Cliente seleccionado - DISEÑO MODERNO MEJORADO */
.cliente-seleccionado {
  background: linear-gradient(135deg, rgba(52, 152, 219, 0.1), rgba(52, 152, 219, 0.05)) !important;
  border: 1px solid rgba(52, 152, 219, 0.2) !important;
  border-radius: 12px !important;
  padding: 16px !important;
  margin-top: 8px !important;
  margin-left: 10px !important;
  box-shadow: 0 3px 12px rgba(52, 152, 219, 0.1) !important;
  transition: all 0.3s ease !important;
}

.cliente-seleccionado:hover {
  transform: translateY(-1px) !important;
  box-shadow: 0 5px 20px rgba(52, 152, 219, 0.15) !important;
}

.cliente-info-header {
  display: flex !important;
  align-items: center !important;
  gap: 8px !important;
  margin-bottom: 12px !important;
  padding-bottom: 8px !important;
  border-bottom: 1px solid rgba(52, 152, 219, 0.15) !important;
}

.cliente-info-header i {
  color: #3498db !important;
  font-size: 16px !important;
}

.cliente-info-header span {
  font-weight: 600 !important;
  color: #2c3e50 !important;
  font-size: 14px !important;
}

.cliente-info-body {
  display: flex !important;
  flex-direction: column !important;
  gap: 8px !important;
}

.cliente-info-item {
  display: flex !important;
  align-items: center !important;
  gap: 8px !important;
  padding: 4px 0 !important;
}

.cliente-info-item i {
  color: #7f8c8d !important;
  font-size: 12px !important;
  width: 16px !important;
  text-align: center !important;
  flex-shrink: 0 !important;
}

.cliente-info-item span {
  color: #34495e !important;
  font-size: 13px !important;
  font-weight: 500 !important;
  line-height: 1.3 !important;
}

/* Mejorar la presentación de productos en el select */
.form-optimizado .formulario-container .form-group select.form-control {
  line-height: 1.4 !important;
  padding: 8px 12px !important; /* ✅ Más espacio para texto largo */
}

.form-optimizado .formulario-container .form-group select.form-control option {
  padding: 8px 12px !important;
  line-height: 1.3 !important;
  font-size: 13px !important;
}

/* Indicador de carga moderno */
.loading-indicator {
  text-align: center !important;
  padding: 40px 20px !important;
  color: #6c757d !important;
  background: linear-gradient(135deg, rgba(52, 152, 219, 0.05), rgba(52, 152, 219, 0.02)) !important;
  border-radius: 12px !important;
  border: 1px solid rgba(52, 152, 219, 0.1) !important;
  margin: 20px 0 !important;
}

.loading-indicator i {
  font-size: 32px !important;
  margin-bottom: 12px !important;
  color: #3498db !important;
  animation: spin 2s linear infinite !important;
}

.loading-indicator p {
  font-size: 16px !important;
  margin: 0 !important;
  color: #7f8c8d !important;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
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

  .form-optimizado .formulario-container .form-group label {
    width: 100px !important; /* ✅ Aún más compacto en móvil */
  }

  /* ✅ Span con EXACTA tipografía también en móvil */
  .form-optimizado .formulario-container .form-label-text {
    width: 100px !important; /* ✅ MISMO ancho que labels en móvil */
    font-size: 14px !important; /* ✅ MISMO tamaño que labels en móvil */
    font-weight: 500 !important; /* ✅ MISMO peso */
    font-family: inherit !important; /* ✅ MISMA fuente */
  }

  /* Toggle switch responsive */
  .form-optimizado .formulario-container .toggle-switch {
    width: 65px !important; /* ✅ Aún más compacto en móvil */
    height: 26px !important;
  }

  .form-optimizado .formulario-container .toggle-slider {
    border-radius: 13px !important;
    padding: 2px !important;
  }

  .form-optimizado .formulario-container .toggle-slider::before {
    height: 22px !important;
    width: 30px !important; /* ✅ Simétrico en móvil */
    border-radius: 11px !important;
  }

  .form-optimizado .formulario-container .toggle-input:checked + .toggle-slider::before {
    transform: translateX(29px) !important; /* ✅ Exactamente simétrico */
  }

  .form-optimizado .formulario-container .toggle-text {
    font-size: 9px !important;
    width: 30px !important; /* ✅ Ancho simétrico en móvil */
  }

  /* Cliente seleccionado responsive */
  .cliente-seleccionado {
    padding: 12px !important;
    margin-left: 5px !important;
  }

  .cliente-info-header {
    margin-bottom: 10px !important;
  }

  .cliente-info-header i {
    font-size: 14px !important;
  }

  .cliente-info-header span {
    font-size: 13px !important;
  }

  .cliente-info-body {
    gap: 6px !important;
  }

  .cliente-info-item {
    gap: 6px !important;
  }

  .cliente-info-item i {
    font-size: 11px !important;
    width: 14px !important;
  }

  .cliente-info-item span {
    font-size: 12px !important;
  }
}
</style>