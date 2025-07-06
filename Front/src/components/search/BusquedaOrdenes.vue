<template>
  <ModernFormWrapper :is-small="true" class="busqueda-simple">
    
    <!-- Resultados de búsqueda -->
    <div v-if="mostrarResultados" class="resultados-container">
      <div class="header-resultados">
        <h2 class="titulo-resultados">
          <i class="bi bi-search"></i>
          {{ ordenesEncontradas.length }} orden{{ ordenesEncontradas.length !== 1 ? 'es' : '' }} encontrada{{ ordenesEncontradas.length !== 1 ? 's' : '' }}
        </h2>
        <button @click="volverABusqueda" class="btn-volver">
          <i class="bi bi-arrow-left"></i>
          Nueva Búsqueda
        </button>
      </div>

      <!-- Tabla de resultados -->
      <div class="tabla-container">
        <table class="tabla-resultados">
          <thead>
            <tr>
              <th>N° Orden</th>
              <th>Estado</th>
              <th>N° Presupuesto</th>
              <th>Cliente</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="orden in ordenesEncontradas" :key="orden.id_orden">
              <td><span class="numero-orden">{{ orden.id_orden }}</span></td>
              <td>
                <span :class="getEstadoClass(orden.estado)">
                  <i :class="getEstadoIcon(orden.estado)"></i>
                  {{ orden.estado }}
                </span>
              </td>
              <td>{{ orden.id_presupuesto }}</td>
              <td>{{ orden.cliente_nombre || 'Cargando...' }}</td>
              <td>
                <button @click="editarOrden(orden)" class="btn-editar" title="Editar orden">
                  <i class="bi bi-pencil-fill"></i>
                  Editar
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Formulario de búsqueda -->
    <div v-else class="formulario-busqueda">
      <h1 class="titulo-principal">
        <i class="bi bi-search"></i>
        Buscar Orden de Trabajo
      </h1>
      
      <div class="busqueda-container">
        <!-- Opciones de búsqueda -->
        <div class="opciones-busqueda">
          <label class="opcion-item" :class="{ active: tipoBusqueda === 'estado' }">
            <input type="radio" v-model="tipoBusqueda" value="estado" />
            <div class="opcion-content">
              <i class="bi bi-list-task"></i>
              <span>Por Estado</span>
            </div>
          </label>
          
          <label class="opcion-item" :class="{ active: tipoBusqueda === 'numero' }">
            <input type="radio" v-model="tipoBusqueda" value="numero" />
            <div class="opcion-content">
              <i class="bi bi-hash"></i>
              <span>Por N° Orden</span>
            </div>
          </label>
          
          <label class="opcion-item" :class="{ active: tipoBusqueda === 'cliente' }">
            <input type="radio" v-model="tipoBusqueda" value="cliente" />
            <div class="opcion-content">
              <i class="bi bi-person"></i>
              <span>Por DNI Cliente</span>
            </div>
          </label>
          
          <label class="opcion-item" :class="{ active: tipoBusqueda === 'presupuesto' }">
            <input type="radio" v-model="tipoBusqueda" value="presupuesto" />
            <div class="opcion-content">
              <i class="bi bi-file-earmark-text"></i>
              <span>Por N° Presupuesto</span>
            </div>
          </label>
        </div>

        <!-- Campos de búsqueda -->
        <form @submit.prevent="buscarOrden" class="form-busqueda">
          
          <!-- Por estado -->
          <div v-if="tipoBusqueda === 'estado'" class="campo-busqueda">
            <label for="estadoSelect">Estado de la Orden:</label>
            <select v-model="estadoSeleccionado" id="estadoSelect" class="form-control">
              <option value="">Seleccione un estado</option>
              <option v-for="estado in estadosDisponibles" :key="estado" :value="estado">
                {{ estado }}
              </option>
            </select>
          </div>

          <!-- Por número -->
          <div v-if="tipoBusqueda === 'numero'" class="campo-busqueda">
            <label for="numeroInput">Número de Orden:</label>
            <input 
              v-model="numeroOrden" 
              id="numeroInput"
              type="number"
              min="1"
              placeholder="Ej: 123"
              class="form-control"
            />
          </div>

          <!-- Por cliente -->
          <div v-if="tipoBusqueda === 'cliente'" class="campo-busqueda">
            <label for="dniInput">DNI del Cliente:</label>
            <input 
              v-model="dniCliente" 
              id="dniInput"
              type="text"
              placeholder="Ej: 12345678"
              class="form-control"
            />
          </div>

          <!-- Por presupuesto -->
          <div v-if="tipoBusqueda === 'presupuesto'" class="campo-busqueda">
            <label for="presupuestoInput">Número de Presupuesto:</label>
            <input 
              v-model="numeroPresupuesto" 
              id="presupuestoInput"
              type="number"
              min="1"
              placeholder="Ej: 456"
              class="form-control"
            />
          </div>

          <!-- Botones -->
          <div class="botones-grupo">
            <button type="submit" class="btn-buscar" :disabled="!puedeRealizar">
              <i class="bi bi-search"></i>
              Buscar
            </button>
            <button type="button" @click="limpiarCampos" class="btn-limpiar">
              <i class="bi bi-arrow-clockwise"></i>
              Limpiar
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Mensajes -->
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
import ModernFormWrapper from '@/components/common/ModernFormWrapper.vue';
import EnhancedMessage from '@/components/common/EnhancedMessage.vue';

export default {
  name: 'BusquedaOrdenes',
  components: {
    ModernFormWrapper,
    EnhancedMessage
  },

  data() {
    return {
      // Tipo de búsqueda
      tipoBusqueda: 'estado', // Por defecto buscar por estado
      
      // Campos de búsqueda
      estadoSeleccionado: '',
      numeroOrden: '',
      dniCliente: '',
      numeroPresupuesto: '',
      
      // Estados disponibles
      estadosDisponibles: [
        'En reparación',
        'Para ingresar',
        'Completado',
        'En espera de repuestos',
        'Listo para entrega'
      ],

      // Resultados
      mostrarResultados: false,
      ordenesEncontradas: [],

      // Mensajes
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
    puedeRealizar() {
      switch (this.tipoBusqueda) {
        case 'estado':
          return this.estadoSeleccionado.trim() !== '';
        case 'numero':
          return this.numeroOrden && this.numeroOrden > 0;
        case 'cliente':
          return this.dniCliente.trim() !== '';
        case 'presupuesto':
          return this.numeroPresupuesto && this.numeroPresupuesto > 0;
        default:
          return false;
      }
    }
  },

  methods: {
    // Mensajes
    showSuccessMessage(message, title = '¡Búsqueda Exitosa!') {
      this.messageType = 'success';
      this.messageText = message;
      this.messageTitle = title;
      this.showInlineMessage = true;
    },

    showErrorMessage(message, title = 'Error en la Búsqueda') {
      this.messageType = 'error';
      this.messageText = message;
      this.messageTitle = title;
      this.showInlineMessage = true;
    },

    showInfoMessage(message, title = 'Sin Resultados') {
      this.messageType = 'info';
      this.messageText = message;
      this.messageTitle = title;
      this.showInlineMessage = true;
    },

    closeMessage() {
      this.showModalMessage = false;
      this.showInlineMessage = false;
    },

    handlePrimaryAction() {
      if (this.messageType === 'info') {
        this.volverABusqueda();
      }
    },

    // Búsqueda principal
    async buscarOrden() {
      if (!this.puedeRealizar) return;

      this.closeMessage();

      try {
        let ordenes = [];

        switch (this.tipoBusqueda) {
          case 'estado':
            ordenes = await this.buscarPorEstado();
            break;
          case 'numero':
            ordenes = await this.buscarPorNumero();
            break;
          case 'cliente':
            ordenes = await this.buscarPorDNI();
            break;
          case 'presupuesto':
            ordenes = await this.buscarPorPresupuesto();
            break;
        }

        if (ordenes.length > 0) {
          this.ordenesEncontradas = ordenes;
          await this.cargarNombresClientes();
          this.mostrarResultados = true;
          
          this.showSuccessMessage(
            `Se encontraron ${ordenes.length} orden${ordenes.length !== 1 ? 'es' : ''}.`
          );
        } else {
          this.showInfoMessage('No se encontraron órdenes con los criterios especificados.');
        }
      } catch (error) {
        console.error('Error en búsqueda:', error);
        this.showErrorMessage('Error al realizar la búsqueda. Intenta nuevamente.');
      }
    },

    // ✅ Por estado (endpoint directo)
    async buscarPorEstado() {
      try {
        const response = await this.$axios.get(`http://localhost:8080/ordenes/estado/${this.estadoSeleccionado}`);
        return response.data || [];
      } catch (error) {
        if (error.response && error.response.status === 404) {
          return [];
        }
        throw error;
      }
    },

    // ✅ Por número (endpoint directo)
    async buscarPorNumero() {
      try {
        const response = await this.$axios.get(`http://localhost:8080/ordenes/traer/${this.numeroOrden}`);
        return response.data ? [response.data] : [];
      } catch (error) {
        if (error.response && error.response.status === 404) {
          return [];
        }
        throw error;
      }
    },

    // ✅ Por DNI del cliente (usando endpoints existentes)
    async buscarPorDNI() {
      try {
        // 1. Buscar cliente por DNI
        const clienteResponse = await this.$axios.get(`http://localhost:8080/clientes/traerxcuil/${this.dniCliente}`);
        
        if (!clienteResponse.data) {
          return [];
        }

        const cliente = clienteResponse.data;
        
        // 2. Buscar órdenes del cliente
        const ordenesResponse = await this.$axios.get(`http://localhost:8080/ordenes/cliente/${cliente.id_cliente}`);
        
        return ordenesResponse.data || [];
      } catch (error) {
        if (error.response && error.response.status === 404) {
          return [];
        }
        throw error;
      }
    },

    // ✅ Por número de presupuesto (usando endpoints existentes)
    async buscarPorPresupuesto() {
      try {
        // Obtener todas las órdenes y filtrar por presupuesto
        const response = await this.$axios.get('http://localhost:8080/ordenes/traer');
        const todasLasOrdenes = response.data || [];
        
        return todasLasOrdenes.filter(orden => orden.id_presupuesto == this.numeroPresupuesto);
      } catch (error) {
        throw error;
      }
    },

    // Cargar nombres de clientes para mostrar en la tabla
    async cargarNombresClientes() {
      try {
        const clientesResponse = await this.$axios.get('http://localhost:8080/clientes/traer');
        const clientes = clientesResponse.data || [];

        // Para cada orden, buscar el cliente correspondiente
        for (let orden of this.ordenesEncontradas) {
          // Primero intentar obtener el cliente desde el presupuesto
          try {
            const presupuestoResponse = await this.$axios.get(`http://localhost:8080/presupuestos/traer/${orden.id_presupuesto}`);
            if (presupuestoResponse.data && presupuestoResponse.data.id_cliente) {
              const cliente = clientes.find(c => c.id_cliente == presupuestoResponse.data.id_cliente);
              if (cliente) {
                orden.cliente_nombre = Array.isArray(cliente.nombre) 
                  ? cliente.nombre.join(' ')
                  : cliente.nombre;
              }
            }
          } catch (error) {
            console.log('No se pudo cargar cliente para orden:', orden.id_orden);
          }
        }
      } catch (error) {
        console.error('Error al cargar nombres de clientes:', error);
      }
    },

    // Editar orden
    editarOrden(orden) {
      try {
        const ordenCompleta = {
          ...orden,
          presupuesto: { id_presupuesto: orden.id_presupuesto }
        };
        
        localStorage.setItem('ordenParaEditar', JSON.stringify(ordenCompleta));
        this.$router.push(`/ordenregistrar/${orden.id_orden}`);
        
      } catch (error) {
        console.error('Error al preparar edición:', error);
        this.showErrorMessage('No se pudo abrir el formulario de edición.');
      }
    },

    // Utilidades
    limpiarCampos() {
      this.estadoSeleccionado = '';
      this.numeroOrden = '';
      this.dniCliente = '';
      this.numeroPresupuesto = '';
      this.closeMessage();
    },

    volverABusqueda() {
      this.mostrarResultados = false;
      this.ordenesEncontradas = [];
      this.closeMessage();
    },

    getEstadoClass(estado) {
      const clases = {
        'En reparación': 'estado-reparacion',
        'Para ingresar': 'estado-ingreso',
        'Completado': 'estado-completado',
        'En espera de repuestos': 'estado-espera',
        'Listo para entrega': 'estado-listo'
      };
      return clases[estado] || 'estado-default';
    },

    getEstadoIcon(estado) {
      const iconos = {
        'En reparación': 'bi bi-tools',
        'Para ingresar': 'bi bi-arrow-down-circle',
        'Completado': 'bi bi-check-circle',
        'En espera de repuestos': 'bi bi-clock',
        'Listo para entrega': 'bi bi-truck'
      };
      return iconos[estado] || 'bi bi-circle';
    }
  }
}
</script>

<style scoped>
.busqueda-simple {
  max-width: 700px;
  margin: 20px auto 0; /* ✅ Reducido de 40px a 20px para que esté más arriba */
}

/* ============================================ */
/* FORMULARIO DE BÚSQUEDA - COMPACTO */
/* ============================================ */
.formulario-busqueda {
  padding: 15px; /* ✅ Reducido de 20px a 15px para más compacto */
}

.titulo-principal {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px; /* ✅ Reducido de 12px */
  font-size: 20px; /* ✅ Reducido de 22px para que ocupe menos altura */
  color: #2c3e50;
  margin-bottom: 15px; /* ✅ Reducido de 20px a 15px */
  font-weight: 600;
}

.titulo-principal i {
  color: #3498db;
  font-size: 18px; /* ✅ Reducido proporcionalmente */
}

.busqueda-container {
  background: rgba(52, 152, 219, 0.05);
  border-radius: 12px; /* ✅ Reducido de 16px */
  padding: 15px; /* ✅ Reducido de 18px a 15px */
  border: 1px solid rgba(52, 152, 219, 0.1);
}

.opciones-busqueda {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); /* ✅ Reducido de 150px */
  gap: 8px; /* ✅ Reducido de 10px */
  margin-bottom: 15px; /* ✅ Reducido de 18px */
}

.opcion-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 8px; /* ✅ Reducido de 12px 10px */
  border: 2px solid #e9ecef;
  border-radius: 10px; /* ✅ Reducido de 12px */
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
  text-align: center;
}

.opcion-item:hover {
  border-color: #3498db;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.15);
}

.opcion-item.active {
  border-color: #3498db;
  background: rgba(52, 152, 219, 0.1);
}

.opcion-item input[type="radio"] {
  display: none;
}

.opcion-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px; /* ✅ Reducido de 6px */
}

.opcion-content i {
  font-size: 16px; /* ✅ Reducido de 18px */
  color: #3498db;
}

.opcion-content span {
  font-size: 11px; /* ✅ Reducido de 12px */
  font-weight: 500;
  color: #2c3e50;
}

/* Campos */
.campo-busqueda {
  margin-bottom: 12px; /* ✅ Reducido de 15px */
}

.campo-busqueda label {
  display: block;
  margin-bottom: 5px; /* ✅ Reducido de 6px */
  font-weight: 600;
  color: #2c3e50;
  font-size: 13px; /* ✅ Reducido de 14px */
}

.form-control {
  width: 100%;
  padding: 8px 12px; /* ✅ Reducido de 10px 14px */
  border: 2px solid #e9ecef;
  border-radius: 6px; /* ✅ Reducido de 8px */
  font-size: 14px; /* ✅ Reducido de 15px */
  transition: all 0.3s ease;
  background: white;
}

.form-control:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.botones-grupo {
  display: flex;
  gap: 10px; /* ✅ Reducido de 12px */
  margin-top: 15px; /* ✅ Reducido de 18px */
}

.btn-buscar, .btn-limpiar {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px; /* ✅ Reducido de 8px */
  padding: 8px 14px; /* ✅ Reducido de 10px 16px */
  border: none;
  border-radius: 6px; /* ✅ Reducido de 8px */
  font-size: 13px; /* ✅ Reducido de 14px */
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-buscar {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
}

.btn-buscar:hover:not(:disabled) {
  background: linear-gradient(135deg, #2980b9, #1f5f8b);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(52, 152, 219, 0.3);
}

.btn-buscar:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}

.btn-limpiar {
  background: linear-gradient(135deg, #6c757d, #5a6268);
  color: white;
}

.btn-limpiar:hover {
  background: linear-gradient(135deg, #5a6268, #495057);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(108, 117, 125, 0.3);
}

/* ============================================ */
/* RESULTADOS */
/* ============================================ */
.resultados-container {
  padding: 15px; /* ✅ Reducido de 20px */
  animation: fadeIn 0.4s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.header-resultados {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px; /* ✅ Reducido de 25px */
  padding-bottom: 12px; /* ✅ Reducido de 15px */
  border-bottom: 2px solid #e9ecef;
}

.titulo-resultados {
  font-size: 18px; /* ✅ Reducido de 20px */
  color: #2c3e50;
  font-weight: 600;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px; /* ✅ Reducido de 10px */
}

.titulo-resultados i {
  color: #27ae60;
}

.btn-volver {
  display: flex;
  align-items: center;
  gap: 6px; /* ✅ Reducido de 8px */
  padding: 8px 16px; /* ✅ Reducido de 10px 20px */
  background: linear-gradient(135deg, #95a5a6, #7f8c8d);
  color: white;
  border: none;
  border-radius: 6px; /* ✅ Reducido de 8px */
  font-weight: 600;
  font-size: 13px; /* ✅ Nuevo, más pequeño */
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-volver:hover {
  background: linear-gradient(135deg, #7f8c8d, #6c757d);
  transform: translateY(-2px);
}

/* Tabla */
.tabla-container {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.tabla-resultados {
  width: 100%;
  border-collapse: collapse;
}

.tabla-resultados th {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  padding: 12px 10px; /* ✅ Reducido de 15px 12px */
  text-align: left;
  font-weight: 600;
  font-size: 13px; /* ✅ Reducido de 14px */
}

.tabla-resultados td {
  padding: 12px 10px; /* ✅ Reducido de 15px 12px */
  border-bottom: 1px solid #e9ecef;
  font-size: 13px; /* ✅ Reducido de 14px */
}

.tabla-resultados tr:hover {
  background: rgba(52, 152, 219, 0.05);
}

.numero-orden {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  padding: 3px 8px; /* ✅ Reducido de 4px 10px */
  border-radius: 10px; /* ✅ Reducido de 12px */
  font-weight: 600;
  font-size: 11px; /* ✅ Reducido de 12px */
}

.btn-editar {
  display: flex;
  align-items: center;
  gap: 5px; /* ✅ Reducido de 6px */
  padding: 6px 12px; /* ✅ Reducido de 8px 16px */
  background: linear-gradient(135deg, #f39c12, #e67e22);
  color: white;
  border: none;
  border-radius: 5px; /* ✅ Reducido de 6px */
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 11px; /* ✅ Reducido de 12px */
  font-weight: 600;
}

.btn-editar:hover {
  background: linear-gradient(135deg, #e67e22, #d35400);
  transform: translateY(-1px);
  box-shadow: 0 4px 15px rgba(243, 156, 18, 0.3);
}

/* Estados */
.estado-reparacion { color: #f39c12; font-weight: 600; }
.estado-ingreso { color: #3498db; font-weight: 600; }
.estado-completado { color: #27ae60; font-weight: 600; }
.estado-espera { color: #e74c3c; font-weight: 600; }
.estado-listo { color: #9b59b6; font-weight: 600; }

/* ============================================ */
/* RESPONSIVE - COMPACTO */
/* ============================================ */
@media (max-width: 768px) {
  .busqueda-simple {
    margin: 10px auto 0; /* ✅ Reducido para móviles */
  }

  .formulario-busqueda {
    padding: 12px; /* ✅ Reducido de 15px */
  }

  .titulo-principal {
    font-size: 18px; /* ✅ Reducido */
    margin-bottom: 12px; /* ✅ Reducido */
  }

  .busqueda-container {
    padding: 12px; /* ✅ Reducido */
  }

  .opciones-busqueda {
    grid-template-columns: repeat(2, 1fr);
    gap: 6px; /* ✅ Reducido de 8px */
    margin-bottom: 12px; /* ✅ Reducido */
  }

  .opcion-item {
    padding: 8px 6px; /* ✅ Reducido */
  }

  .opcion-content i {
    font-size: 14px; /* ✅ Reducido */
  }

  .opcion-content span {
    font-size: 10px; /* ✅ Reducido */
  }

  .campo-busqueda {
    margin-bottom: 10px; /* ✅ Reducido */
  }

  .campo-busqueda label {
    margin-bottom: 4px; /* ✅ Reducido */
    font-size: 12px; /* ✅ Reducido */
  }

  .form-control {
    padding: 6px 10px; /* ✅ Reducido */
    font-size: 13px; /* ✅ Reducido */
  }

  .botones-grupo {
    flex-direction: column;
    gap: 6px; /* ✅ Reducido */
    margin-top: 12px; /* ✅ Reducido */
  }

  .btn-buscar, .btn-limpiar {
    padding: 6px 12px; /* ✅ Reducido */
    font-size: 12px; /* ✅ Reducido */
  }

  .header-resultados {
    flex-direction: column;
    gap: 10px; /* ✅ Reducido */
    text-align: center;
    margin-bottom: 15px; /* ✅ Reducido */
    padding-bottom: 10px; /* ✅ Reducido */
  }

  .tabla-resultados {
    font-size: 11px; /* ✅ Reducido */
  }

  .tabla-resultados th,
  .tabla-resultados td {
    padding: 6px 4px; /* ✅ Reducido */
  }
}

@media (max-width: 480px) {
  .busqueda-simple {
    margin: 5px auto 0; /* ✅ Margen mínimo para móviles pequeños */
  }

  .formulario-busqueda {
    padding: 10px; /* ✅ Más compacto */
  }

  .titulo-principal {
    font-size: 16px; /* ✅ Más pequeño */
    margin-bottom: 10px; /* ✅ Más compacto */
    flex-direction: column;
    gap: 4px; /* ✅ Reducido */
  }

  .busqueda-container {
    padding: 10px; /* ✅ Más compacto */
  }

  .opciones-busqueda {
    grid-template-columns: 1fr;
    gap: 4px; /* ✅ Más compacto */
    margin-bottom: 10px; /* ✅ Reducido */
  }

  .opcion-item {
    padding: 6px 4px; /* ✅ Más compacto */
  }

  .campo-busqueda {
    margin-bottom: 8px; /* ✅ Más compacto */
  }

  .botones-grupo {
    margin-top: 10px; /* ✅ Más compacto */
  }

  .tabla-container {
    overflow-x: auto;
  }
}
</style>