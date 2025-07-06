<template>
  <!-- ✅ NUEVO: Usar ModernFormWrapper con mejor posicionamiento -->
  <ModernFormWrapper :is-medium="true" class="form-optimizado">
    <h1 class="form-title">{{ modoEdicion ? 'Editar' : 'Registrar' }} cliente</h1>
    
    <form class="form-content">
      <div class="form-group">
        <label for="nombre">Nombre:</label>
        <input v-model="nuevoCliente.nombre[0]" type="text" id="nombre" class="form-control" />
      </div>

      <div class="form-group">
        <label for="apellido">Apellido:</label>
        <input v-model="nuevoCliente.nombre[1]" type="text" id="apellido" class="form-control" />
      </div>

      <div class="form-group">
        <label for="direccion">Dirección:</label>
        <input v-model="nuevoCliente.direccion" type="text" id="direccion" class="form-control" />
      </div>

      <div class="form-group">
        <label for="cuil">DNI:</label>
        <input v-model="nuevoCliente.cuil" type="text" id="cuil" class="form-control" />
      </div>

      <div class="form-group">
        <label for="email">Email:</label>
        <input v-model="nuevoCliente.email" type="email" id="email" class="form-control" />
      </div>

      <div class="form-group">
        <label for="telefono">Teléfono:</label>
        <input v-model="nuevoCliente.telefono[0]" type="text" id="telefono" class="form-control" />
      </div>

      <div class="form-group">
        <label for="otrotelefono">Otro Teléfono:</label>
        <input v-model="nuevoCliente.telefono[1]" type="text" id="otrotelefono" class="form-control" />
      </div>

      <div class="form-buttons">
        <button v-if="!modoEdicion" type="button" @click="registrarCliente" 
                class="btn gradient-custom-4">
          <i class="bi bi-person-plus-fill"></i>
          Registrar
        </button>
        <button v-if="modoEdicion" type="button" @click="actualizarCliente" 
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
  name: 'RegistrarCliente',
  components: {
    ModernFormWrapper  // ✅ NUEVO: Usar el wrapper moderno
  },

  data() {
    return {
      nuevoCliente: { 
        nombre: ['', ''], 
        direccion: '', 
        cuil: '', 
        email: '', 
        telefono: ['', ''] 
      },
      registroExitoso: false,
      clientes: [],
      mensajeError: '',
      modoEdicion: false,
      clienteOriginal: null,
    };
  },

  methods: {
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

    validarFormatoEmail(email) {
      if (!email) return true;
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return regex.test(email);
    },

    validarCampos() {
      // Limpiar mensajes anteriores
      this.mensajeError = '';
      this.registroExitoso = false;

      // Validar formato del correo electrónico
      if (!this.validarFormatoEmail(this.nuevoCliente.email)) {
        this.mensajeError = 'El formato del correo electrónico no es válido.';
        this.mostrarMensajeTemporalmente('error');
        return false;
      }

      // Asegurar que los campos sean arrays
      if (!Array.isArray(this.nuevoCliente.telefono)) {
        this.nuevoCliente.telefono = this.nuevoCliente.telefono ? [this.nuevoCliente.telefono] : [];
      }

      if (!Array.isArray(this.nuevoCliente.nombre)) {
        this.nuevoCliente.nombre = this.nuevoCliente.nombre ? [this.nuevoCliente.nombre] : [];
      }

      // Verificar campos obligatorios
      if (!this.nuevoCliente.direccion || !this.nuevoCliente.nombre[0] || 
          !this.nuevoCliente.nombre[1] || !this.nuevoCliente.telefono[0] || 
          !this.nuevoCliente.cuil) {
        this.mensajeError = 'Nombre, apellido, dirección, DNI y teléfono son campos obligatorios.';
        this.mostrarMensajeTemporalmente('error');
        return false;
      }

      // Limpiar teléfono secundario si está vacío
      if (!this.nuevoCliente.telefono[1]) {
        delete this.nuevoCliente.telefono[1];
      }

      return true;
    },

    registrarCliente() {
      if (!this.validarCampos()) return;

      // Enviar datos al servidor
      this.$axios.post('http://localhost:8080/clientes/crear', this.nuevoCliente)
        .then(() => {
          this.obtenerClientes();
          this.nuevoCliente = { 
            nombre: ['', ''], 
            direccion: '', 
            cuil: '', 
            email: '', 
            telefono: ['', ''] 
          };
          this.registroExitoso = true;
          this.mensajeError = '';
          this.mostrarMensajeTemporalmente('success');
        })
        .catch(error => {
          console.error('Error al agregar el cliente:', error);
          this.mensajeError = 'Error al registrar el cliente. Intente nuevamente.';
          this.mostrarMensajeTemporalmente('error');
        });
    },

    actualizarCliente() {
      if (!this.validarCampos()) return;

      // Enviar datos actualizados al servidor
      this.$axios.put(`http://localhost:8080/clientes/editar/${this.nuevoCliente.id_cliente}`, this.nuevoCliente)
        .then(() => {
          this.obtenerClientes();
          this.registroExitoso = true;
          this.mensajeError = '';
          
          // Después de 2 segundos, volver al listado
          setTimeout(() => {
            this.$router.push('/clienteslistar');
          }, 2000);
        })
        .catch(error => {
          console.error('Error al actualizar el cliente:', error);
          this.mensajeError = 'Error al actualizar el cliente. Intente nuevamente.';
          this.mostrarMensajeTemporalmente('error');
        });
    },

    cancelarFormulario() {
      // Volver al listado sin guardar cambios
      this.$router.push('/clienteslistar');
    },

    // Mantener el método original para compatibilidad
    cancelarEdicion() {
      this.cancelarFormulario();
    },

    cargarClienteParaEdicion(clienteId) {
      // Buscar el cliente en la lista local o hacer petición al servidor
      const storedClients = localStorage.getItem('clientes');
      if (storedClients) {
        this.clientes = JSON.parse(storedClients);
        const cliente = this.clientes.find(c => c.id_cliente == clienteId);
        if (cliente) {
          this.nuevoCliente = { ...cliente };
          this.clienteOriginal = { ...cliente };
          return;
        }
      }
      
      // Si no encontramos el cliente localmente, hacer petición al servidor
      this.obtenerClientes();
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
      this.cargarClienteParaEdicion(this.$route.params.id);
    } else {
      this.obtenerClientes();
    }
  },

  watch: {
    // Observar cambios en la ruta para manejar navegación dinámica
    '$route'(to, from) {
      if (to.params.id) {
        this.modoEdicion = true;
        this.cargarClienteParaEdicion(to.params.id);
      } else {
        this.modoEdicion = false;
        this.nuevoCliente = { 
          nombre: ['', ''], 
          direccion: '', 
          cuil: '', 
          email: '', 
          telefono: ['', ''] 
        };
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
}
</style>