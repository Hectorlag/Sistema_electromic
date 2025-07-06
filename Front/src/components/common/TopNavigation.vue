<template>
  <div class="top-navigation">
    <!-- Breadcrumbs Section -->
    <div class="breadcrumbs-section">
      <nav class="breadcrumbs">
        <router-link 
          to="/inicio" 
          class="breadcrumb-item home"
          :class="{ 'active': isCurrentRoute('/inicio') }"
        >
          <i class="bi bi-house-fill"></i>
          <span>Dashboard</span>
        </router-link>
        
        <template v-for="(crumb, index) in breadcrumbs" :key="index">
          <i class="bi bi-chevron-right breadcrumb-separator"></i>
          <router-link 
            v-if="crumb.route && index < breadcrumbs.length - 1"
            :to="crumb.route" 
            class="breadcrumb-item"
            :class="{ 'active': isCurrentRoute(crumb.route) }"
          >
            <i v-if="crumb.icon" :class="crumb.icon"></i>
            <span>{{ crumb.label }}</span>
          </router-link>
          <span 
            v-else
            class="breadcrumb-item current"
          >
            <i v-if="crumb.icon" :class="crumb.icon"></i>
            <span>{{ crumb.label }}</span>
          </span>
        </template>
      </nav>

      <!-- Page Title & Description -->
      <div v-if="pageInfo.title" class="page-header">
        <h1 class="page-title">
          <i v-if="pageInfo.icon" :class="pageInfo.icon"></i>
          {{ pageInfo.title }}
        </h1>
        <p v-if="pageInfo.description" class="page-description">
          {{ pageInfo.description }}
        </p>
      </div>
    </div>

    <!-- Actions Section -->
    <div class="actions-section">
      <!-- Quick Search -->
      <div class="quick-search">
        <button 
          @click="openCommandPalette" 
          class="search-trigger"
          title="Búsqueda rápida (Ctrl+K)"
        >
          <i class="bi bi-search"></i>
          <span>Buscar...</span>
          <div class="search-shortcut">
            <kbd>Ctrl</kbd>
            <kbd>K</kbd>
          </div>
        </button>
      </div>

      <!-- Context Actions -->
      <div v-if="contextActions.length > 0" class="context-actions">
        <button
          v-for="action in contextActions"
          :key="action.id"
          @click="executeAction(action)"
          :class="['action-btn', action.variant || 'primary']"
          :title="action.tooltip"
        >
          <i v-if="action.icon" :class="action.icon"></i>
          <span>{{ action.label }}</span>
        </button>
      </div>

      <!-- User Menu -->
      <div class="user-menu" ref="userMenuRef">
        <button 
          @click="toggleUserMenu" 
          class="user-trigger"
          :class="{ 'active': userMenuOpen }"
        >
          <div class="user-avatar">
            <i class="bi bi-person-fill"></i>
          </div>
          <div class="user-info">
            <span class="user-name">{{ userName }}</span>
            <span class="user-role">Administrador</span>
          </div>
          <i class="bi bi-chevron-down user-chevron"></i>
        </button>

        <!-- User Dropdown -->
        <div v-if="userMenuOpen" class="user-dropdown">
          <div class="dropdown-header">
            <div class="user-avatar large">
              <i class="bi bi-person-fill"></i>
            </div>
            <div class="user-details">
              <h4>{{ userName }}</h4>
              <p>admin@sistema.com</p>
            </div>
          </div>
          
          <div class="dropdown-section">
            <router-link to="/perfilcuenta" class="dropdown-item" @click="closeUserMenu">
              <i class="bi bi-person-gear"></i>
              <span>Mi Perfil</span>
            </router-link>
            <button class="dropdown-item" @click="toggleTheme">
              <i class="bi bi-palette"></i>
              <span>Cambiar Tema</span>
            </button>
          </div>
          
          <div class="dropdown-separator"></div>
          
          <div class="dropdown-section">
            <router-link to="/login" class="dropdown-item logout" @click="logout">
              <i class="bi bi-box-arrow-right"></i>
              <span>Cerrar Sesión</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Overlay para cerrar menus -->
    <div 
      v-if="userMenuOpen" 
      class="menu-overlay" 
      @click="closeUserMenu"
    ></div>
  </div>
</template>

<script>
export default {
  name: 'TopNavigation',
  
  emits: ['open-command-palette'],

  data() {
    return {
      userMenuOpen: false,
      userName: 'Hectorla'
    }
  },

  computed: {
    breadcrumbs() {
      const path = this.$route.path;
      const crumbs = [];

      // Mapeo de rutas a breadcrumbs
      const routeMap = {
        '/clienteregistrar': [
          { label: 'Clientes', icon: 'bi bi-people-fill', route: '/clienteslistar' },
          { label: 'Registrar Cliente', icon: 'bi bi-person-plus-fill' }
        ],
        '/clienteslistar': [
          { label: 'Clientes', icon: 'bi bi-people-fill' },
          { label: 'Lista de Clientes', icon: 'bi bi-list-ul' }
        ],
        '/clientebuscar': [
          { label: 'Clientes', icon: 'bi bi-people-fill', route: '/clienteslistar' },
          { label: 'Buscar Cliente', icon: 'bi bi-search' }
        ],
        '/productoregistrar': [
          { label: 'Productos', icon: 'bi bi-box-fill', route: '/productoslistar' },
          { label: 'Registrar Producto', icon: 'bi bi-plus-circle-fill' }
        ],
        '/productoslistar': [
          { label: 'Productos', icon: 'bi bi-box-fill' },
          { label: 'Lista de Productos', icon: 'bi bi-list-ul' }
        ],
        '/productobuscar': [
          { label: 'Productos', icon: 'bi bi-box-fill', route: '/productoslistar' },
          { label: 'Buscar Producto', icon: 'bi bi-search' }
        ],
        '/presupuestoregistrar': [
          { label: 'Presupuestos', icon: 'bi bi-calculator-fill', route: '/presupuestoslistar' },
          { label: 'Nuevo Presupuesto', icon: 'bi bi-plus-circle-fill' }
        ],
        '/presupuestoslistar': [
          { label: 'Presupuestos', icon: 'bi bi-calculator-fill' },
          { label: 'Lista de Presupuestos', icon: 'bi bi-list-ul' }
        ],
        '/ordenregistrar': [
          { label: 'Órdenes', icon: 'bi bi-clipboard-check-fill', route: '/ordeneslistar' },
          { label: 'Nueva Orden', icon: 'bi bi-plus-circle-fill' }
        ],
        '/ordeneslistar': [
          { label: 'Órdenes de Trabajo', icon: 'bi bi-clipboard-check-fill' },
          { label: 'Lista de Órdenes', icon: 'bi bi-list-ul' }
        ],
        '/perfilcuenta': [
          { label: 'Mi Cuenta', icon: 'bi bi-person-gear' },
          { label: 'Perfil', icon: 'bi bi-person-gear' }
        ]
      };

      return routeMap[path] || [];
    },

    pageInfo() {
      const path = this.$route.path;
      
      const pageMap = {
        '/inicio': {
          title: 'Dashboard Principal',
          description: 'Resumen general de tu sistema de gestión',
          icon: 'bi bi-house-fill'
        },
        '/clienteregistrar': {
          title: 'Registrar Cliente',
          description: 'Agregar un nuevo cliente al sistema',
          icon: 'bi bi-person-plus-fill'
        },
        '/clienteslistar': {
          title: 'Lista de Clientes',
          description: 'Gestionar todos los clientes registrados',
          icon: 'bi bi-people-fill'
        },
        '/clientebuscar': {
          title: 'Buscar Cliente',
          description: 'Encontrar cliente específico',
          icon: 'bi bi-search'
        },
        '/productoregistrar': {
          title: 'Registrar Producto',
          description: 'Agregar producto al inventario',
          icon: 'bi bi-plus-circle-fill'
        },
        '/productoslistar': {
          title: 'Inventario de Productos',
          description: 'Gestionar stock y productos disponibles',
          icon: 'bi bi-box-fill'
        },
        '/presupuestoregistrar': {
          title: 'Nuevo Presupuesto',
          description: 'Crear presupuesto para cliente',
          icon: 'bi bi-calculator-fill'
        },
        '/presupuestoslistar': {
          title: 'Lista de Presupuestos',
          description: 'Gestionar todos los presupuestos',
          icon: 'bi bi-calculator-fill'
        },
        '/ordenregistrar': {
          title: 'Nueva Orden de Trabajo',
          description: 'Crear orden de reparación',
          icon: 'bi bi-clipboard-plus-fill'
        },
        '/ordeneslistar': {
          title: 'Órdenes de Trabajo',
          description: 'Gestionar órdenes activas y completadas',
          icon: 'bi bi-clipboard-check-fill'
        }
      };

      return pageMap[path] || {};
    },

    contextActions() {
      const path = this.$route.path;
      const actions = [];

      // Solo acciones secundarias - los botones principales van en los wrappers
      if (path.includes('listar')) {
        actions.push({
          id: 'export',
          label: 'Exportar',
          icon: 'bi bi-download',
          variant: 'secondary',
          tooltip: 'Exportar datos a Excel',
          action: () => this.exportData()
        });

        actions.push({
          id: 'filter',
          label: 'Filtros',
          icon: 'bi bi-funnel',
          variant: 'secondary',
          tooltip: 'Aplicar filtros avanzados',
          action: () => this.openFilters()
        });
      }

      // ✅ ELIMINÉ: Botones principales duplicados (Nuevo Cliente, Nuevo Producto, etc.)
      // Estos van en los wrappers específicos para evitar duplicación

      return actions;
    }
  },

  methods: {
    isCurrentRoute(route) {
      return this.$route.path === route;
    },

    openCommandPalette() {
      this.$emit('open-command-palette');
    },

    toggleUserMenu() {
      this.userMenuOpen = !this.userMenuOpen;
    },

    closeUserMenu() {
      this.userMenuOpen = false;
    },

    executeAction(action) {
      if (action.action) {
        action.action();
      }
    },

    exportData() {
      // Implementar exportación
      console.log('Exportando datos...');
    },

    openFilters() {
      // Implementar filtros
      console.log('Abriendo filtros...');
    },

    toggleTheme() {
      // Implementar cambio de tema
      console.log('Cambiando tema...');
      this.closeUserMenu();
    },

    logout() {
      // Implementar logout
      localStorage.removeItem('usuario');
      this.$router.push('/login');
    },

    obtenerUsuario() {
      try {
        const usuarioString = localStorage.getItem('usuario');
        if (usuarioString) {
          const usuario = JSON.parse(usuarioString);
          this.userName = usuario?.nombre || usuarioString;
        }
      } catch (error) {
        this.userName = localStorage.getItem('usuario') || 'Usuario';
      }
    },

    handleClickOutside(event) {
      if (this.userMenuOpen && this.$refs.userMenuRef && !this.$refs.userMenuRef.contains(event.target)) {
        this.closeUserMenu();
      }
    }
  },

  mounted() {
    this.obtenerUsuario();
    document.addEventListener('click', this.handleClickOutside);
  },

  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },

  watch: {
    '$route'() {
      this.closeUserMenu();
    }
  }
}
</script>

<style scoped>
/* ============================================ */
/* TOP NAVIGATION BASE */
/* ============================================ */
.top-navigation {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 12px 30px; /* ✅ REDUCIDO: Menos padding para formularios */
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  position: relative;
  z-index: 100;
}

/* ============================================ */
/* BREADCRUMBS SECTION */
/* ============================================ */
.breadcrumbs-section {
  flex: 1;
  min-width: 0;
}

.breadcrumbs {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px; /* ✅ REDUCIDO */
  flex-wrap: wrap;
}

.breadcrumb-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 8px;
  text-decoration: none;
  color: #7f8c8d;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.breadcrumb-item:hover {
  background: rgba(52, 152, 219, 0.08);
  color: #3498db;
  text-decoration: none;
}

.breadcrumb-item.current {
  color: #2c3e50;
  font-weight: 600;
  background: rgba(52, 152, 219, 0.1);
}

.breadcrumb-item.home {
  background: rgba(52, 152, 219, 0.05);
}

.breadcrumb-separator {
  color: #bdc3c7;
  font-size: 12px;
}

/* ============================================ */
/* PAGE HEADER */
/* ============================================ */
.page-header {
  margin-top: 4px; /* ✅ REDUCIDO */
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 0 0 6px 0;
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
}

.page-title i {
  color: #3498db;
  font-size: 22px;
}

.page-description {
  margin: 0;
  color: #7f8c8d;
  font-size: 15px;
  line-height: 1.4;
}

/* ============================================ */
/* ACTIONS SECTION */
/* ============================================ */
.actions-section {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  flex-shrink: 0;
}

/* ============================================ */
/* QUICK SEARCH */
/* ============================================ */
.quick-search {
  position: relative;
}

.search-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  background: rgba(248, 249, 250, 0.8);
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #7f8c8d;
  font-size: 14px;
  min-width: 220px;
}

.search-trigger:hover {
  background: rgba(52, 152, 219, 0.05);
  border-color: rgba(52, 152, 219, 0.2);
  color: #3498db;
}

.search-trigger i {
  font-size: 16px;
}

.search-trigger span {
  flex: 1;
  text-align: left;
}

.search-shortcut {
  display: flex;
  gap: 4px;
}

/* ============================================ */
/* CONTEXT ACTIONS */
/* ============================================ */
.context-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.action-btn.primary {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
}

.action-btn.primary:hover {
  background: linear-gradient(135deg, #2980b9, #1f4e79);
  transform: translateY(-1px);
}

.action-btn.secondary {
  background: rgba(108, 117, 125, 0.1);
  color: #6c757d;
  border: 1px solid rgba(108, 117, 125, 0.2);
}

.action-btn.secondary:hover {
  background: rgba(108, 117, 125, 0.15);
  color: #495057;
}

/* ============================================ */
/* USER MENU */
/* ============================================ */
.user-menu {
  position: relative;
}

.user-trigger {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  background: rgba(248, 249, 250, 0.5);
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.user-trigger:hover,
.user-trigger.active {
  background: rgba(52, 152, 219, 0.05);
  border-color: rgba(52, 152, 219, 0.2);
}

.user-avatar {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 16px;
}

.user-avatar.large {
  width: 48px;
  height: 48px;
  font-size: 20px;
}

.user-info {
  display: flex;
  flex-direction: column;
  text-align: left;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
  line-height: 1.2;
}

.user-role {
  font-size: 12px;
  color: #7f8c8d;
  line-height: 1.2;
}

.user-chevron {
  color: #bdc3c7;
  font-size: 12px;
  transition: transform 0.2s ease;
}

.user-trigger.active .user-chevron {
  transform: rotate(180deg);
}

/* ============================================ */
/* USER DROPDOWN */
/* ============================================ */
.user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 8px;
  min-width: 260px;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(0, 0, 0, 0.06);
  z-index: 1001;
  animation: slideDown 0.2s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.dropdown-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.user-details h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
}

.user-details p {
  margin: 0;
  font-size: 14px;
  color: #7f8c8d;
}

.dropdown-section {
  padding: 8px 0;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  color: #2c3e50;
  text-decoration: none;
  transition: all 0.2s ease;
  cursor: pointer;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
  font-size: 14px;
}

.dropdown-item:hover {
  background: rgba(52, 152, 219, 0.08);
  color: #3498db;
  text-decoration: none;
}

.dropdown-item.logout:hover {
  background: rgba(231, 76, 60, 0.08);
  color: #e74c3c;
}

.dropdown-item i {
  font-size: 16px;
  width: 18px;
  text-align: center;
}

.dropdown-separator {
  height: 1px;
  background: rgba(0, 0, 0, 0.06);
  margin: 8px 0;
}

/* ============================================ */
/* MENU OVERLAY */
/* ============================================ */
.menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: transparent;
  z-index: 1000;
}

/* ============================================ */
/* KBD ELEMENTS */
/* ============================================ */
kbd {
  background: rgba(0, 0, 0, 0.1);
  color: #7f8c8d;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
  font-family: monospace;
  border: 1px solid rgba(0, 0, 0, 0.08);
}

/* ============================================ */
/* RESPONSIVE */
/* ============================================ */
@media (max-width: 1024px) {
  .search-trigger {
    min-width: 180px;
  }
  
  .user-info {
    display: none;
  }
}

@media (max-width: 768px) {
  .top-navigation {
    padding: 16px 20px;
    flex-direction: column;
    gap: 16px;
  }
  
  .actions-section {
    width: 100%;
    justify-content: space-between;
  }
  
  .search-trigger {
    min-width: 160px;
  }
  
  .context-actions {
    gap: 6px;
  }
  
  .action-btn {
    padding: 8px 12px;
    font-size: 13px;
  }
  
  .breadcrumbs {
    gap: 6px;
  }
  
  .breadcrumb-item {
    padding: 4px 8px;
    font-size: 13px;
  }
  
  .page-title {
    font-size: 20px;
  }
}

@media (max-width: 480px) {
  .top-navigation {
    padding: 12px 16px;
  }
  
  .search-trigger span {
    display: none;
  }
  
  .search-trigger {
    min-width: auto;
    width: 44px;
    justify-content: center;
  }
  
  .search-shortcut {
    display: none;
  }
  
  .action-btn span {
    display: none;
  }
  
  .action-btn {
    width: 40px;
    padding: 8px;
    justify-content: center;
  }
  
  .breadcrumbs {
    overflow-x: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;
  }
  
  .breadcrumbs::-webkit-scrollbar {
    display: none;
  }
}
</style>