<template>
  <!-- ✅ NUEVO: Usar ModernFormWrapper con mejor posicionamiento -->
  <ModernFormWrapper :is-medium="true" class="form-optimizado">
    <h1 class="form-title">{{ modoEdicion ? 'Editar' : 'Registrar' }} producto</h1>
    
    <form class="form-content">
      <div class="form-group">
        <label for="marca">Marca:</label>
        <input v-model="nuevoProducto.marca" type="text" id="marca" class="form-control" />
      </div>

      <div class="form-group">
        <label for="modelo">Modelo:</label>
        <input v-model="nuevoProducto.modelo" type="text" id="modelo" class="form-control" />
      </div>

      <div class="form-group">
        <label for="num_serie">Número de Serie:</label>
        <input v-model="nuevoProducto.num_serie" type="text" id="num_serie" class="form-control" />
      </div>

      <div class="form-group">
        <label for="tipo">Tipo:</label>
        <select v-model="tipoSeleccionado" @change="actualizarTipoSeleccionado" 
                id="tipo" class="form-control">
          <option value="" disabled>Seleccione un tipo</option>
          <option v-for="tipo in tiposOrdenados" :key="tipo" :value="tipo">{{ tipo }}</option>
          <option value="Otro">Otro</option>
        </select>
      </div>

      <div v-if="tipoSeleccionado === 'Otro'" class="form-group">
        <label for="otroTipo">Otro Tipo:</label>
        <input v-model="otroTipo" type="text" id="otroTipo" class="form-control" 
               placeholder="Ingrese el tipo" />
      </div>

      <div class="form-group">
        <label for="accesorio1">Accesorio 1:</label>
        <input v-model="nuevoProducto.accesorios[0]" type="text" id="accesorio1" 
               class="form-control" placeholder="Accesorio 1" />
      </div>

      <div class="form-group">
        <label for="accesorio2">Accesorio 2:</label>
        <input v-model="nuevoProducto.accesorios[1]" type="text" id="accesorio2" 
               class="form-control" placeholder="Accesorio 2" />
      </div>

      <div class="form-group">
        <label for="cliente">Cliente:</label>
        <select v-model="nuevoProducto.cliente" id="cliente" class="form-control">
          <option value="" disabled v-if="!nuevoProducto.cliente">Seleccione un cliente</option>
          <option v-for="cliente in clientes" :key="cliente.id_cliente" :value="cliente">
            {{ cliente.nombre.slice(-1)[0] }} {{ cliente.nombre.slice(0, -1).join(' ') }}
          </option>
        </select>
      </div>

      <div class="form-buttons">
        <button v-if="!modoEdicion" type="button" @click="registrarProducto" 
                class="btn gradient-custom-4">
          <i class="bi bi-box-seam-fill"></i>
          Registrar
        </button>
        <button v-if="modoEdicion" type="button" @click="actualizarProducto" 
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
        ¡{{ modoEdicion ? 'Actualización' : 'Registro' }} exitoso!
      </div>
    </form>
  </ModernFormWrapper>
</template>

<script>
// ✅ CAMBIO PRINCIPAL: Import del nuevo wrapper moderno
import ModernFormWrapper from '@/components/common/ModernFormWrapper.vue';

export default {
  name: 'RegistrarProducto',
  components: {
    ModernFormWrapper  // ✅ NUEVO: Usar el wrapper moderno
  },

  data() {
    return {
      nuevoProducto: { 
        marca: '', 
        modelo: '', 
        num_serie: '', 
        tipo: '', 
        accesorios: ['', ''], 
        cliente: { id_cliente: "" } 
      },
      otroTipo: '',
      tipoSeleccionado: '',
      tiposDisponibles: [
        "TV", "Celular", "Microondas", "Notebook", "Impresora",
        "Escáner", "Tablet", "Parlante", "Plancha", "Reproductor de música",
        "Auriculares", "Monitor de PC"
      ],
      registroExitoso: false,
      productos: [],
      clientes: [],
      mensajeError: '',
      modoEdicion: false,
      productoOriginal: null,
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
          localStorage.setItem("productos", JSON.stringify(response.data));
        })
        .catch((error) => {
          console.error("Error al obtener productos:", error);
        });
    },

    validarCampos() {
      // Limpiar mensajes anteriores
      this.mensajeError = '';
      this.registroExitoso = false;

      // Verificar campos obligatorios
      if (!this.nuevoProducto.marca || !this.nuevoProducto.modelo || 
          !this.nuevoProducto.num_serie || !this.nuevoProducto.tipo) {
        this.mensajeError = 'Los campos marca, modelo, número de serie y tipo son obligatorios.';
        this.mostrarMensajeTemporalmente('error');
        return false;
      }

      // Asegurar que los accesorios sean un array
      if (!Array.isArray(this.nuevoProducto.accesorios)) {
        this.nuevoProducto.accesorios = this.nuevoProducto.accesorios ? 
          [this.nuevoProducto.accesorios] : ['', ''];
      }

      return true;
    },
    
    actualizarTipoSeleccionado() {
      if (this.tipoSeleccionado === 'Otro') {
        this.nuevoProducto.tipo = this.otroTipo;
      } else {
        this.nuevoProducto.tipo = this.tipoSeleccionado;
      }
    },

    obtenerClientes() {
      this.$axios.get('http://localhost:8080/clientes/traer')
        .then(response => {
          console.log('Respuesta de la API:', response.data);
          this.clientes = response.data;
          localStorage.setItem('clientes', JSON.stringify(response.data));
        })
        .catch(error => {
          console.error('Error al obtener clientes:', error);
        });
    },

    registrarProducto() {
      if (this.tipoSeleccionado === 'Otro') {
        this.nuevoProducto.tipo = this.otroTipo;
      }

      if (!this.validarCampos()) return;

      const productoPlano = JSON.parse(JSON.stringify(this.nuevoProducto));

      this.$axios.post('http://localhost:8080/productos/crear', productoPlano)
        .then(() => {
          console.log("EL producto a registrar es:", productoPlano)
          this.obtenerProductos();
          this.nuevoProducto = { 
            marca: '', 
            modelo: '', 
            num_serie: '', 
            tipo: '', 
            accesorios: ['', ''], 
            cliente: { id_cliente: "" } 
          };
          this.otroTipo = '';
          this.tipoSeleccionado = '';
          this.mensajeError = '';
          this.registroExitoso = true;
          this.mostrarMensajeTemporalmente('success');
        })
        .catch(error => {
          console.error('Error al agregar el producto:', error);
          this.mensajeError = 'Error al registrar el producto. Intente nuevamente.';
          this.mostrarMensajeTemporalmente('error');
        });
    },

    actualizarProducto() {
      if (this.tipoSeleccionado === 'Otro') {
        this.nuevoProducto.tipo = this.otroTipo;
      }

      if (!this.validarCampos()) return;

      const productoPlano = JSON.parse(JSON.stringify(this.nuevoProducto));

      this.$axios.put(`http://localhost:8080/productos/editar/${this.nuevoProducto.id_producto}`, productoPlano)
        .then(() => {
          this.obtenerProductos();
          this.registroExitoso = true;
          this.mensajeError = '';
          
          // Después de 2 segundos, volver al listado
          setTimeout(() => {
            this.$router.push('/productoslistar');
          }, 2000);
        })
        .catch(error => {
          console.error('Error al actualizar el producto:', error);
          this.mensajeError = 'Error al actualizar el producto. Intente nuevamente.';
          this.mostrarMensajeTemporalmente('error');
        });
    },

    cancelarFormulario() {
      // Volver al listado sin guardar cambios
      this.$router.push('/productoslistar');
    },

    // Mantener el método original para compatibilidad
    cancelarEdicion() {
      this.cancelarFormulario();
    },

    cargarProductoParaEdicion(productoId) {
      // Buscar el producto en la lista local o hacer petición al servidor
      const storedProducts = localStorage.getItem('productos');
      if (storedProducts) {
        this.productos = JSON.parse(storedProducts);
        const producto = this.productos.find(p => p.id_producto == productoId);
        if (producto) {
          this.nuevoProducto = { ...producto };
          this.productoOriginal = { ...producto };
          
          // Configurar el tipo seleccionado
          this.tipoSeleccionado = producto.tipo;
          
          // Si el tipo no está en la lista, agregarlo o marcarlo como "Otro"
          if (!this.tiposDisponibles.includes(producto.tipo)) {
            this.tipoSeleccionado = 'Otro';
            this.otroTipo = producto.tipo;
          }
          
          // Asegurar que accesorios sea un array
          if (!Array.isArray(this.nuevoProducto.accesorios)) {
            this.nuevoProducto.accesorios = ['', ''];
          }
          
          // Buscar y asignar el cliente completo
          const cliente = this.clientes.find(c => c.id_cliente === producto.id_cliente);
          this.nuevoProducto.cliente = cliente || { id_cliente: "" };
          
          return;
        }
      }
      
      // Si no encontramos el producto localmente, hacer petición al servidor
      this.obtenerProductos();
    },

    mostrarMensajeTemporalmente(tipo) {
      setTimeout(() => {
        if (tipo === 'error') {
          this.mensajeError = '';
        } else {
          this.registroExitoso = false;
        }
      }, 4000);
    }
  },

  created() {
    // Detectar si estamos en modo edición basado en la ruta
    if (this.$route.params.id) {
      this.modoEdicion = true;
      this.cargarProductoParaEdicion(this.$route.params.id);
    } else {
      this.obtenerProductos();
    }
    this.obtenerClientes();
  },

  watch: {
    // Observar cambios en la ruta para manejar navegación dinámica
    '$route'(to, from) {
      if (to.params.id) {
        this.modoEdicion = true;
        this.cargarProductoParaEdicion(to.params.id);
      } else {
        this.modoEdicion = false;
        this.nuevoProducto = { 
          marca: '', 
          modelo: '', 
          num_serie: '', 
          tipo: '', 
          accesorios: ['', ''], 
          cliente: { id_cliente: "" } 
        };
        this.tipoSeleccionado = '';
        this.otroTipo = '';
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
  width: 140px !important; /* ✅ Más ancho para labels de productos */
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

/* ✅ OVERRIDE completo del TopNavigation para formularios */
.form-optimizado ~ * .top-navigation,
.form-optimizado .top-navigation {
  padding: 10px 30px !important; /* ✅ Top Navigation más compacto */
}

.form-optimizado ~ * .page-title,
.form-optimizado .page-title {
  font-size: 20px !important; /* ✅ Título más pequeño */
  margin-bottom: 4px !important;
}

.form-optimizado ~ * .page-description,
.form-optimizado .page-description {
  font-size: 14px !important; /* ✅ Descripción más pequeña */
  margin-bottom: 0 !important;
}

/* ✅ FORZAR vista sin scroll para formularios medianos */
@media (min-height: 600px) {
  .form-optimizado .modern-content-area {
    padding-top: 5px !important;
    padding-bottom: 5px !important;
  }
  
  .form-optimizado .formulario-container {
    max-height: calc(100vh - 200px) !important; /* ✅ Altura máxima */
    overflow-y: visible !important;
  }
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
/* MENSAJES MEJORADOS CON ICONOS */
/* ============================================ */
.form-message {
  display: flex;
  align-items: center;
  gap: 10px;
  text-align: center;
  margin-top: 15px;
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  animation: slideIn 0.3s ease-out;
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
  color: #e74c3c;
  background: linear-gradient(135deg, rgba(231, 76, 60, 0.1), rgba(231, 76, 60, 0.05));
  border: 1px solid rgba(231, 76, 60, 0.2);
  box-shadow: 0 4px 15px rgba(231, 76, 60, 0.1);
}

.form-success {
  color: #27ae60;
  background: linear-gradient(135deg, rgba(39, 174, 96, 0.1), rgba(39, 174, 96, 0.05));
  border: 1px solid rgba(39, 174, 96, 0.2);
  box-shadow: 0 4px 15px rgba(39, 174, 96, 0.1);
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
    width: 120px !important;
  }
}
</style>