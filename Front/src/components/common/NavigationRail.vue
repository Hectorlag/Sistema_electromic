<template>
  <nav class="navigation-rail">
    <!-- Logo/Home -->
    <div class="rail-item rail-home">
      <router-link to="/inicio" class="rail-link" title="Inicio">
        <i class="bi bi-house-fill"></i>
      </router-link>
    </div>

    <!-- Separador -->
    <div class="rail-separator"></div>

    <!-- Navegación principal -->
    <div class="rail-main">
      <!-- Clientes -->
      <div class="rail-item" :class="{ 'active': isActiveSection('clientes') }">
        <button @click="toggleSection('clientes')" class="rail-link" title="Clientes">
          <i class="bi bi-people-fill"></i>
          <span class="rail-badge" v-if="totalClientes">{{ totalClientes }}</span>
        </button>
        
        <!-- Submenu flotante -->
        <div class="rail-submenu" v-show="openSection === 'clientes'">
          <div class="submenu-header">
            <h4>Clientes</h4>
            <button @click="closeSubmenu" class="close-btn">
              <i class="bi bi-x"></i>
            </button>
          </div>
          <div class="submenu-items">
            <router-link to="/clienteregistrar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-person-plus"></i>
              <span>Registrar Cliente</span>
            </router-link>
            <router-link to="/clienteslistar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-list-ul"></i>
              <span>Listar Clientes</span>
            </router-link>
            <router-link to="/clientebuscar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-search"></i>
              <span>Buscar Cliente</span>
            </router-link>
          </div>
        </div>
      </div>

      <!-- Productos -->
      <div class="rail-item" :class="{ 'active': isActiveSection('productos') }">
        <button @click="toggleSection('productos')" class="rail-link" title="Productos">
          <i class="bi bi-box-fill"></i>
          <span class="rail-badge" v-if="totalProductos">{{ totalProductos }}</span>
        </button>
        
        <div class="rail-submenu" v-show="openSection === 'productos'">
          <div class="submenu-header">
            <h4>Productos</h4>
            <button @click="closeSubmenu" class="close-btn">
              <i class="bi bi-x"></i>
            </button>
          </div>
          <div class="submenu-items">
            <router-link to="/productoregistrar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-plus-circle"></i>
              <span>Registrar Producto</span>
            </router-link>
            <router-link to="/productoslistar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-list-ul"></i>
              <span>Listar Productos</span>
            </router-link>
            <router-link to="/productobuscar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-search"></i>
              <span>Buscar Producto</span>
            </router-link>
          </div>
        </div>
      </div>

      <!-- Presupuestos -->
      <div class="rail-item" :class="{ 'active': isActiveSection('presupuestos') }">
        <button @click="toggleSection('presupuestos')" class="rail-link" title="Presupuestos">
          <i class="bi bi-calculator-fill"></i>
        </button>
        
        <div class="rail-submenu" v-show="openSection === 'presupuestos'">
          <div class="submenu-header">
            <h4>Presupuestos</h4>
            <button @click="closeSubmenu" class="close-btn">
              <i class="bi bi-x"></i>
            </button>
          </div>
          <div class="submenu-items">
            <router-link to="/presupuestoregistrar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-plus-circle"></i>
              <span>Nuevo Presupuesto</span>
            </router-link>
            <router-link to="/presupuestoslistar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-list-ul"></i>
              <span>Listar Presupuestos</span>
            </router-link>
            <router-link to="/presupuestobuscar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-search"></i>
              <span>Buscar Presupuesto</span>
            </router-link>
          </div>
        </div>
      </div>

      <!-- Órdenes de Trabajo -->
      <div class="rail-item" :class="{ 'active': isActiveSection('ordenes') }">
        <button @click="toggleSection('ordenes')" class="rail-link" title="Órdenes de Trabajo">
          <i class="bi bi-clipboard-check-fill"></i>
          <span class="rail-badge alert" v-if="ordenesPendientes">{{ ordenesPendientes }}</span>
        </button>
        
        <div class="rail-submenu" v-show="openSection === 'ordenes'">
          <div class="submenu-header">
            <h4>Órdenes de Trabajo</h4>
            <button @click="closeSubmenu" class="close-btn">
              <i class="bi bi-x"></i>
            </button>
          </div>
          <div class="submenu-items">
            <router-link to="/ordenregistrar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-plus-circle"></i>
              <span>Nueva Orden</span>
            </router-link>
            <router-link to="/ordeneslistar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-list-ul"></i>
              <span>Listar Órdenes</span>
            </router-link>
            <router-link to="/ordenbuscar" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-search"></i>
              <span>Buscar Orden</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Separador -->
    <div class="rail-separator"></div>

    <!-- Acciones especiales -->
    <div class="rail-bottom">
      <!-- Command Palette trigger -->
      <div class="rail-item">
        <button @click="openCommandPalette" class="rail-link" title="Búsqueda rápida (Ctrl+K)">
          <i class="bi bi-search"></i>
        </button>
      </div>

      <!-- Mi Cuenta -->
      <div class="rail-item" :class="{ 'active': isActiveSection('cuenta') }">
        <button @click="toggleSection('cuenta')" class="rail-link" title="Mi Cuenta">
          <i class="bi bi-person-gear"></i>
        </button>
        
        <div class="rail-submenu" v-show="openSection === 'cuenta'">
          <div class="submenu-header">
            <h4>Mi Cuenta</h4>
            <button @click="closeSubmenu" class="close-btn">
              <i class="bi bi-x"></i>
            </button>
          </div>
          <div class="submenu-items">
            <router-link to="/perfilcuenta" class="submenu-item" @click="closeSubmenu">
              <i class="bi bi-person-gear"></i>
              <span>Perfil</span>
            </router-link>
            <router-link to="/login" class="submenu-item logout" @click="closeSubmenu">
              <i class="bi bi-box-arrow-right"></i>
              <span>Cerrar Sesión</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Overlay para cerrar submenus -->
    <div 
      v-if="openSection" 
      class="rail-overlay" 
      @click="closeSubmenu"
    ></div>
  </nav>
</template>

<script>
export default {
  name: 'NavigationRail',
  
  data() {
    return {
      openSection: null,
      totalClientes: 0,
      totalProductos: 156,
      ordenesPendientes: 7
    }
  },

  methods: {
    toggleSection(section) {
      if (this.openSection === section) {
        this.closeSubmenu();
      } else {
        this.openSection = section;
      }
    },

    closeSubmenu() {
      this.openSection = null;
    },

    isActiveSection(section) {
      const path = this.$route.path;
      switch (section) {
        case 'clientes':
          return path.includes('cliente');
        case 'productos':
          return path.includes('producto');
        case 'presupuestos':
          return path.includes('presupuesto');
        case 'ordenes':
          return path.includes('orden');
        case 'cuenta':
          return path.includes('perfil') || path.includes('cuenta');
        default:
          return false;
      }
    },

    openCommandPalette() {
      this.$emit('open-command-palette');
      this.closeSubmenu();
    },

    cargarEstadisticas() {
      try {
        const clientes = JSON.parse(localStorage.getItem('clientes') || '[]');
        this.totalClientes = clientes.filter(c => !c.deleted).length;
      } catch (error) {
        console.error('Error al cargar estadísticas:', error);
      }
    },

    handleKeyboard(event) {
      // Cerrar con Escape
      if (event.key === 'Escape') {
        this.closeSubmenu();
      }
      // Abrir command palette con Ctrl+K
      if (event.ctrlKey && event.key === 'k') {
        event.preventDefault();
        this.openCommandPalette();
      }
    }
  },

  mounted() {
    this.cargarEstadisticas();
    document.addEventListener('keydown', this.handleKeyboard);
  },

  beforeUnmount() {
    document.removeEventListener('keydown', this.handleKeyboard);
  },

  watch: {
    '$route'() {
      this.closeSubmenu();
    }
  }
}
</script>

<style scoped>
/* ============================================ */
/* NAVIGATION RAIL BASE */
/* ============================================ */
.navigation-rail {
  position: fixed;
  left: 0;
  top: 80px;
  width: 65px;
  height: calc(100vh - 80px);
  background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);
  border-right: 1px solid rgba(52, 152, 219, 0.2);
  display: flex;
  flex-direction: column;
  z-index: 1000;
  box-shadow: 2px 0 20px rgba(0, 0, 0, 0.1);
}

/* ============================================ */
/* RAIL ITEMS */
/* ============================================ */
.rail-item {
  position: relative;
  margin: 2px 0;
}

.rail-home {
  margin-bottom: 10px;
}

.rail-main {
  flex: 1;
  padding: 10px 0;
}

.rail-bottom {
  padding: 10px 0 20px 0;
}

.rail-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 65px;
  height: 52px;
  background: none;
  border: none;
  color: #bdc3c7;
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
  position: relative;
  border-radius: 0;
}

.rail-link:hover {
  background: rgba(52, 152, 219, 0.15);
  color: #3498db;
}

.rail-item.active .rail-link {
  background: rgba(52, 152, 219, 0.2);
  color: #3498db;
  border-right: 3px solid #3498db;
}

.rail-link i {
  font-size: 20px;
}

/* ============================================ */
/* BADGES */
/* ============================================ */
.rail-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: #3498db;
  color: white;
  font-size: 10px;
  font-weight: 600;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 16px;
  text-align: center;
  line-height: 1.2;
}

.rail-badge.alert {
  background: #e74c3c;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

/* ============================================ */
/* SEPARADORES */
/* ============================================ */
.rail-separator {
  height: 1px;
  background: rgba(52, 152, 219, 0.2);
  margin: 10px 8px;
}

/* ============================================ */
/* SUBMENUS FLOTANTES */
/* ============================================ */
.rail-submenu {
  position: absolute;
  left: 65px;
  top: 0;
  min-width: 240px;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  z-index: 1001;
  animation: slideIn 0.2s ease-out;
  border: 1px solid rgba(52, 152, 219, 0.1);
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-10px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.submenu-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid rgba(52, 152, 219, 0.1);
  background: rgba(52, 152, 219, 0.05);
  border-radius: 12px 12px 0 0;
}

.submenu-header h4 {
  margin: 0;
  color: #2c3e50;
  font-size: 16px;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  color: #7f8c8d;
  font-size: 18px;
  cursor: pointer;
  padding: 4px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.close-btn:hover {
  background: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
}

/* ============================================ */
/* SUBMENU ITEMS */
/* ============================================ */
.submenu-items {
  padding: 8px 0;
}

.submenu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  color: #2c3e50;
  text-decoration: none;
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
}

.submenu-item:hover {
  background: rgba(52, 152, 219, 0.08);
  color: #3498db;
  text-decoration: none;
}

.submenu-item.router-link-active {
  background: rgba(52, 152, 219, 0.15);
  color: #3498db;
  border-right: 3px solid #3498db;
}

.submenu-item.logout:hover {
  background: rgba(231, 76, 60, 0.08);
  color: #e74c3c;
}

.submenu-item i {
  font-size: 16px;
  width: 18px;
  text-align: center;
}

/* ============================================ */
/* OVERLAY */
/* ============================================ */
.rail-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.1);
  z-index: 999;
}

/* ============================================ */
/* RESPONSIVE */
/* ============================================ */
@media (max-width: 768px) {
  .navigation-rail {
    width: 60px;
  }
  
  .rail-link {
    width: 60px;
    height: 48px;
  }
  
  .rail-link i {
    font-size: 18px;
  }
  
  .rail-submenu {
    left: 60px;
    min-width: 220px;
  }
}

@media (max-width: 480px) {
  .navigation-rail {
    width: 100%;
    height: 60px;
    top: 80px;
    flex-direction: row;
    overflow-x: auto;
    overflow-y: hidden;
  }
  
  .rail-main {
    display: flex;
    flex-direction: row;
    gap: 5px;
    padding: 0 10px;
  }
  
  .rail-bottom {
    display: flex;
    flex-direction: row;
    gap: 5px;
    padding: 0 10px;
  }
  
  .rail-link {
    width: 50px;
    height: 50px;
    border-radius: 8px;
  }
  
  .rail-submenu {
    left: 0;
    top: 60px;
    width: 100vw;
    border-radius: 0;
  }
  
  .rail-separator {
    display: none;
  }
}
</style>