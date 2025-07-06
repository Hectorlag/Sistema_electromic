<template>
  <ModernFormWrapper :is-small="true" class="busqueda-cliente">
    
    <!-- Resultados de búsqueda -->
    <div v-if="clienteEncontrado" class="resultados-container">
      <div class="header-resultados">
        <h2 class="titulo-resultados">
          <i class="bi bi-person-check"></i>
          Cliente Encontrado
        </h2>
        <button @click="volverABusqueda" class="btn-volver">
          <i class="bi bi-arrow-left"></i>
          Nueva Búsqueda
        </button>
      </div>

      <!-- TABLA DE INFORMACIÓN DEL CLIENTE (Formato Lista) -->
      <div class="tabla-cliente-container">
        <div class="tabla-header">
          <h3 class="tabla-titulo">
            <i class="bi bi-person-fill"></i>
            Información del Cliente
          </h3>
        </div>
        
        <div class="tabla-wrapper">
          <table class="tabla-listado">
            <thead>
              <tr>
                <th class="col-numero">N°</th>
                <th class="col-nombre">APELLIDO Y NOMBRE</th>
                <th class="col-direccion">DIRECCIÓN</th>
                <th class="col-dni">DNI</th>
                <th class="col-email">EMAIL</th>
                <th class="col-telefono">TELÉFONO/S</th>
                <th class="col-acciones">ESTADO</th>
              </tr>
            </thead>
            <tbody>
              <tr class="fila-cliente-encontrado">
                <td class="col-numero">
                  <span class="numero-cliente">{{ clienteEncontrado.id_cliente }}</span>
                </td>
                <td class="col-nombre">
                  <strong>{{ getNombreCompleto() }}</strong>
                </td>
                <td class="col-direccion">
                  {{ clienteEncontrado.direccion || 'Sin dirección' }}
                </td>
                <td class="col-dni">
                  <span class="dni-badge">{{ clienteEncontrado.cuil }}</span>
                </td>
                <td class="col-email">
                  <a v-if="getEmailCliente()" 
                     :href="`mailto:${getEmailCliente()}`" 
                     class="email-cliente">
                    {{ getEmailCliente() }}
                  </a>
                  <span v-else class="sin-email">Sin email</span>
                  <!-- Debug info oculta por defecto -->
                  <small v-if="showDebug" class="debug-email">{{ emailDebugInfo }}</small>
                </td>
                <td class="col-telefono">
                  <div v-if="clienteEncontrado.telefono && clienteEncontrado.telefono.length > 0" class="telefonos-container">
                    <a v-for="(telefono, index) in clienteEncontrado.telefono.slice(0, 2)" 
                       :key="index"
                       :href="`tel:${telefono}`" 
                       class="telefono-link">
                      <i class="bi bi-telephone"></i>
                      {{ telefono }}
                    </a>
                    <span v-if="clienteEncontrado.telefono.length > 2" class="mas-telefonos">
                      +{{ clienteEncontrado.telefono.length - 2 }} más
                    </span>
                  </div>
                  <span v-else class="sin-telefono">Sin teléfono</span>
                </td>
                <td class="col-acciones">
                  <span class="estado-activo">
                    <i class="bi bi-check-circle-fill"></i>
                    Activo
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- TABLA DE PRODUCTOS ASOCIADOS -->
      <div class="tabla-productos-container">
        <div class="tabla-header">
          <h3 class="tabla-titulo">
            <i class="bi bi-box-seam"></i>
            Productos Asociados
            <span class="contador-productos">({{ clienteEncontrado.listaproductos?.length || 0 }})</span>
          </h3>
          <button v-if="emailDebugInfo" @click="toggleDebug" class="btn-debug">
            <i class="bi bi-bug"></i>
            {{ showDebug ? 'Ocultar' : 'Debug' }}
          </button>
        </div>
        
        <div v-if="clienteEncontrado.listaproductos && clienteEncontrado.listaproductos.length > 0" class="tabla-wrapper">
          <table class="tabla-listado tabla-productos">
            <thead>
              <tr>
                <th class="col-prod-numero">N°</th>
                <th class="col-prod-marca">MARCA</th>
                <th class="col-prod-modelo">MODELO</th>
                <th class="col-prod-tipo">TIPO</th>
                <th class="col-prod-serie">N° SERIE</th>
                <th class="col-prod-estado">ESTADO</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="producto in clienteEncontrado.listaproductos" 
                  :key="producto.id" 
                  class="fila-producto">
                <td class="col-prod-numero">
                  <span class="numero-producto">{{ producto.id }}</span>
                </td>
                <td class="col-prod-marca">
                  <strong>{{ producto.marca }}</strong>
                </td>
                <td class="col-prod-modelo">
                  {{ producto.modelo }}
                </td>
                <td class="col-prod-tipo">
                  <span class="tipo-producto">{{ producto.tipo }}</span>
                </td>
                <td class="col-prod-serie">
                  <code class="numero-serie">{{ producto.num_serie }}</code>
                </td>
                <td class="col-prod-estado">
                  <span class="producto-activo">
                    <i class="bi bi-check-circle-fill"></i>
                    Activo
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <div v-else class="sin-productos">
          <div class="mensaje-sin-productos">
            <i class="bi bi-inbox"></i>
            <h4>Sin productos registrados</h4>
            <p>Este cliente no tiene productos asociados en el sistema.</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Formulario de búsqueda -->
    <div v-else class="formulario-busqueda">
      <h1 class="titulo-principal">
        <i class="bi bi-search"></i>
        Buscar Cliente
      </h1>
      
      <div class="busqueda-container">
        <form @submit.prevent="buscarCliente" class="form-busqueda">
          
          <div class="campo-busqueda">
            <label for="dniInput">DNI del Cliente:</label>
            <div class="input-with-icon">
              <i class="bi bi-person-vcard"></i>
              <input 
                v-model="cuil" 
                id="dniInput"
                type="text"
                placeholder="Ej: 12345678"
                class="form-control"
                autofocus
              />
            </div>
          </div>

          <div class="botones-grupo">
            <button type="submit" class="btn-buscar" :disabled="!puedeRealizar">
              <i class="bi bi-search"></i>
              Buscar Cliente
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
  name: 'BusquedaCliente',
  components: {
    ModernFormWrapper,
    EnhancedMessage
  },

  data() {
    return {
      // Campo de búsqueda
      cuil: '',
      
      // Resultados
      clienteEncontrado: null,
      busquedaRealizada: false,

      // Debug
      emailDebugInfo: '',
      showDebug: false,

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
      return this.cuil && this.cuil.trim() !== '';
    }
  },

  methods: {
    // Sistema avanzado de detección de email
    getEmailCliente() {
      if (!this.clienteEncontrado) {
        this.emailDebugInfo = '';
        return '';
      }
      
      console.log('🔍 BÚSQUEDA DE EMAIL INICIADA');
      console.log('📦 Datos del cliente:', this.clienteEncontrado);
      
      // Lista completa de posibles campos de email
      const emailFields = [
        'email', 'Email', 'EMAIL',
        'mail', 'Mail', 'MAIL',
        'correo', 'Correo', 'CORREO',
        'e_mail', 'e-mail', 'eMail',
        'emailAddress', 'email_address',
        'correoElectronico', 'correo_electronico'
      ];
      
      // 1. Buscar en propiedades principales
      for (const field of emailFields) {
        const value = this.clienteEncontrado[field];
        console.log(`   Revisando campo '${field}':`, value);
        
        if (this.isValidEmail(value)) {
          console.log(`✅ EMAIL ENCONTRADO en '${field}':`, value);
          this.emailDebugInfo = `Encontrado en: ${field}`;
          return value;
        }
      }
      
      // 2. Buscar en objetos anidados
      const nestedObjects = ['contacto', 'contact', 'datos', 'info', 'personal'];
      for (const objKey of nestedObjects) {
        if (this.clienteEncontrado[objKey] && typeof this.clienteEncontrado[objKey] === 'object') {
          console.log(`   Revisando objeto anidado '${objKey}':`, this.clienteEncontrado[objKey]);
          
          for (const field of emailFields) {
            const value = this.clienteEncontrado[objKey][field];
            if (this.isValidEmail(value)) {
              console.log(`✅ EMAIL ENCONTRADO en '${objKey}.${field}':`, value);
              this.emailDebugInfo = `Encontrado en: ${objKey}.${field}`;
              return value;
            }
          }
        }
      }
      
      // 3. Buscar por patrón en todas las claves
      const allKeys = Object.keys(this.clienteEncontrado);
      const potentialEmailKeys = allKeys.filter(key => 
        key.toLowerCase().includes('email') || 
        key.toLowerCase().includes('mail') ||
        key.toLowerCase().includes('correo')
      );
      
      console.log('   Claves potenciales encontradas:', potentialEmailKeys);
      
      for (const key of potentialEmailKeys) {
        const value = this.clienteEncontrado[key];
        console.log(`   Valor en '${key}':`, value);
        
        if (this.isValidEmail(value)) {
          console.log(`✅ EMAIL ENCONTRADO por patrón en '${key}':`, value);
          this.emailDebugInfo = `Encontrado por patrón en: ${key}`;
          return value;
        }
      }
      
      // 4. Buscar cualquier string que contenga @
      for (const [key, value] of Object.entries(this.clienteEncontrado)) {
        if (typeof value === 'string' && value.includes('@') && this.isValidEmail(value)) {
          console.log(`✅ EMAIL ENCONTRADO por contenido @ en '${key}':`, value);
          this.emailDebugInfo = `Encontrado por @ en: ${key}`;
          return value;
        }
      }
      
      console.log('❌ NO SE ENCONTRÓ EMAIL VÁLIDO');
      this.emailDebugInfo = 'No encontrado en ningún campo disponible';
      return '';
    },

    // Validador de email
    isValidEmail(value) {
      if (!value || typeof value !== 'string') return false;
      if (value === 'disp' || value === 'disponible' || value === 'n/a' || value === '') return false;
      
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailRegex.test(value.trim());
    },

    // Toggle debug info
    toggleDebug() {
      this.showDebug = !this.showDebug;
    },

    // Mensajes
    showSuccessMessage(message, title = '¡Cliente Encontrado!') {
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

    showInfoMessage(message, title = 'Cliente No Encontrado') {
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
    async buscarCliente() {
      if (!this.puedeRealizar) {
        this.showErrorMessage('Por favor, ingrese el DNI del cliente.');
        return;
      }

      this.closeMessage();
      this.busquedaRealizada = true;

      try {
        const response = await this.$axios.get(`http://localhost:8080/clientes/traerxcuil/${this.cuil}`);
        
        if (response.data) {
          this.clienteEncontrado = response.data;
          
          // Debug completo
          console.log('🚀 RESPUESTA DEL SERVIDOR:');
          console.log('   Status:', response.status);
          console.log('   Data:', response.data);
          
          // Buscar email inmediatamente
          const emailEncontrado = this.getEmailCliente();
          console.log('📧 EMAIL FINAL:', emailEncontrado);
          
          this.showSuccessMessage(
            `Cliente encontrado: ${this.getNombreCompleto()}`
          );
        } else {
          this.showInfoMessage(
            `No se encontró ningún cliente con DNI ${this.cuil}.`
          );
        }
      } catch (error) {
        console.error('❌ Error al buscar cliente:', error);
        
        if (error.response && error.response.status === 404) {
          this.showInfoMessage(
            `No existe un cliente registrado con DNI ${this.cuil}.`
          );
        } else {
          this.showErrorMessage(
            'Error al conectar con el servidor. Intenta nuevamente.'
          );
        }
      }
    },

    // Utilidades
    getNombreCompleto() {
      if (!this.clienteEncontrado) return '';
      
      if (this.clienteEncontrado.nombreCompleto && Array.isArray(this.clienteEncontrado.nombreCompleto)) {
        return this.clienteEncontrado.nombreCompleto.join(' ');
      }
      
      if (this.clienteEncontrado.nombre && Array.isArray(this.clienteEncontrado.nombre)) {
        return this.clienteEncontrado.nombre.join(' ');
      }
      
      return this.clienteEncontrado.nombre || 'Sin nombre';
    },

    limpiarCampos() {
      this.cuil = '';
      this.clienteEncontrado = null;
      this.busquedaRealizada = false;
      this.emailDebugInfo = '';
      this.showDebug = false;
      this.closeMessage();
    },

    volverABusqueda() {
      this.limpiarCampos();
    }
  }
}
</script>

<style scoped>
.busqueda-cliente {
  max-width: 1400px;
  margin: 20px auto 0;
}

/* ============================================ */
/* FORMULARIO DE BÚSQUEDA */
/* ============================================ */
.formulario-busqueda {
  padding: 15px;
}

.titulo-principal {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 20px;
  color: #2c3e50;
  margin-bottom: 15px;
  font-weight: 600;
}

.titulo-principal i {
  color: #3498db;
  font-size: 18px;
}

.busqueda-container {
  background: rgba(52, 152, 219, 0.05);
  border-radius: 12px;
  padding: 15px;
  border: 1px solid rgba(52, 152, 219, 0.1);
}

.campo-busqueda {
  margin-bottom: 15px;
}

.campo-busqueda label {
  display: block;
  margin-bottom: 6px;
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.input-with-icon {
  position: relative;
}

.input-with-icon i {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #7f8c8d;
  font-size: 16px;
  z-index: 2;
}

.form-control {
  width: 100%;
  padding: 10px 14px 10px 45px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 15px;
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
  gap: 12px;
  margin-top: 18px;
}

.btn-buscar, .btn-limpiar {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
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
  padding: 15px;
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
  margin-bottom: 25px;
  padding-bottom: 12px;
  border-bottom: 2px solid #e9ecef;
}

.titulo-resultados {
  font-size: 18px;
  color: #2c3e50;
  font-weight: 600;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.titulo-resultados i {
  color: #27ae60;
}

.btn-volver {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: linear-gradient(135deg, #95a5a6, #7f8c8d);
  color: white;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-volver:hover {
  background: linear-gradient(135deg, #7f8c8d, #6c757d);
  transform: translateY(-2px);
}

/* ============================================ */
/* CONTENEDORES DE TABLA */
/* ============================================ */
.tabla-cliente-container,
.tabla-productos-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #e9ecef;
  margin-bottom: 20px;
  overflow: hidden;
}

.tabla-header {
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
  padding: 15px 20px;
  border-bottom: 1px solid #dee2e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tabla-titulo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.tabla-titulo i {
  color: #3498db;
  font-size: 14px;
}

.contador-productos {
  background: #f39c12;
  color: white;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 600;
  margin-left: 8px;
}

.btn-debug {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 10px;
  background: #e67e22;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-debug:hover {
  background: #d35400;
}

/* ============================================ */
/* TABLA ESTILO LISTADO */
/* ============================================ */
.tabla-wrapper {
  overflow-x: auto;
}

.tabla-listado {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.tabla-listado thead th {
  background: #f8f9fa;
  color: #495057;
  padding: 12px 8px;
  text-align: left;
  font-weight: 600;
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border-bottom: 2px solid #dee2e6;
  white-space: nowrap;
}

.tabla-listado tbody td {
  padding: 12px 8px;
  border-bottom: 1px solid #f1f3f4;
  vertical-align: middle;
}

.tabla-listado tbody tr:hover {
  background: rgba(52, 152, 219, 0.03);
}

/* ============================================ */
/* COLUMNAS CLIENTE */
/* ============================================ */
.col-numero { width: 60px; text-align: center; }
.col-nombre { width: 200px; }
.col-direccion { width: 180px; }
.col-dni { width: 120px; }
.col-email { width: 200px; }
.col-telefono { width: 150px; }
.col-acciones { width: 100px; text-align: center; }

.numero-cliente {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  padding: 4px 8px;
  border-radius: 10px;
  font-weight: 600;
  font-size: 11px;
}

.fila-cliente-encontrado {
  background: rgba(39, 174, 96, 0.05);
  border-left: 4px solid #27ae60;
}

.dni-badge {
  background: rgba(52, 152, 219, 0.1);
  color: #2980b9;
  padding: 3px 8px;
  border-radius: 8px;
  font-weight: 600;
  font-size: 12px;
}

.email-cliente {
  color: #3498db;
  text-decoration: none;
  font-weight: 500;
}

.email-cliente:hover {
  text-decoration: underline;
}

.sin-email {
  color: #95a5a6;
  font-style: italic;
  font-size: 12px;
}

.debug-email {
  display: block;
  color: #e67e22;
  font-size: 9px;
  margin-top: 2px;
  font-style: italic;
}

.telefonos-container {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.telefono-link {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #3498db;
  text-decoration: none;
  font-size: 12px;
  padding: 2px 4px;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.telefono-link:hover {
  background: rgba(52, 152, 219, 0.1);
  text-decoration: none;
}

.telefono-link i {
  font-size: 10px;
}

.mas-telefonos {
  font-size: 10px;
  color: #7f8c8d;
  font-style: italic;
  margin-top: 2px;
}

.sin-telefono {
  color: #95a5a6;
  font-style: italic;
  font-size: 12px;
}

.estado-activo {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #27ae60;
  font-weight: 600;
  font-size: 11px;
}

.estado-activo i {
  font-size: 12px;
}

/* ============================================ */
/* COLUMNAS PRODUCTOS */
/* ============================================ */
.col-prod-numero { width: 60px; text-align: center; }
.col-prod-marca { width: 120px; }
.col-prod-modelo { width: 150px; }
.col-prod-tipo { width: 100px; }
.col-prod-serie { width: 130px; }
.col-prod-estado { width: 80px; text-align: center; }

.tabla-productos tbody tr {
  background: white;
}

.tabla-productos tbody tr:nth-child(even) {
  background: #f8f9fa;
}

.tabla-productos tbody tr:hover {
  background: rgba(243, 156, 18, 0.05) !important;
}

.fila-producto {
  border-left: 3px solid #f39c12;
}

.numero-producto {
  background: linear-gradient(135deg, #f39c12, #e67e22);
  color: white;
  padding: 4px 8px;
  border-radius: 10px;
  font-weight: 600;
  font-size: 11px;
}

.col-prod-marca strong {
  color: #2c3e50;
  font-weight: 700;
}

.tipo-producto {
  background: rgba(52, 152, 219, 0.1);
  color: #2980b9;
  padding: 3px 8px;
  border-radius: 8px;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.3px;
}

.numero-serie {
  background: #f8f9fa;
  color: #6c757d;
  padding: 3px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 11px;
  border: 1px solid #e9ecef;
}

.producto-activo {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #27ae60;
  font-weight: 600;
  font-size: 11px;
}

.producto-activo i {
  font-size: 12px;
}

/* ============================================ */
/* SIN PRODUCTOS */
/* ============================================ */
.sin-productos {
  padding: 50px 30px;
  text-align: center;
  background: #fafafa;
}

.mensaje-sin-productos {
  color: #6c757d;
}

.mensaje-sin-productos i {
  font-size: 48px;
  opacity: 0.4;
  margin-bottom: 15px;
}

.mensaje-sin-productos h4 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #495057;
}

.mensaje-sin-productos p {
  font-size: 14px;
  margin: 0;
  opacity: 0.8;
}

/* ============================================ */
/* RESPONSIVE */
/* ============================================ */
@media (max-width: 768px) {
  .busqueda-cliente {
    margin: 10px auto 0;
  }

  .formulario-busqueda {
    padding: 12px;
  }

  .titulo-principal {
    font-size: 18px;
    margin-bottom: 12px;
  }

  .busqueda-container {
    padding: 12px;
  }

  .botones-grupo {
    flex-direction: column;
    gap: 8px;
  }

  .header-resultados {
    flex-direction: column;
    gap: 12px;
    text-align: center;
  }

  .tabla-header {
    padding: 12px 15px;
    flex-direction: column;
    gap: 10px;
    text-align: center;
  }

  .tabla-wrapper {
    overflow-x: auto;
  }

  .tabla-listado {
    min-width: 800px;
    font-size: 11px;
  }

  .tabla-listado th,
  .tabla-listado td {
    padding: 8px 6px;
  }
}

@media (max-width: 480px) {
  .formulario-busqueda {
    padding: 10px;
  }

  .titulo-principal {
    font-size: 16px;
    margin-bottom: 10px;
  }

  .busqueda-container {
    padding: 10px;
  }

  .tabla-header {
    padding: 10px 12px;
  }

  .tabla-titulo {
    font-size: 14px;
  }

  .tabla-listado {
    min-width: 700px;
    font-size: 10px;
  }

  .tabla-listado th,
  .tabla-listado td {
    padding: 6px 4px;
  }

  .sin-productos {
    padding: 30px 15px;
  }

  .mensaje-sin-productos i {
    font-size: 36px;
  }
}
</style>