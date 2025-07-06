<template>
  <!-- Command Palette Modal -->
  <teleport to="body">
    <div 
      v-if="isOpen" 
      class="command-palette-overlay"
      @click="close"
      @keydown.esc="close"
    >
      <div 
        class="command-palette"
        @click.stop
        ref="paletteRef"
      >
        <!-- Search Input -->
        <div class="search-container">
          <i class="bi bi-search search-icon"></i>
          <input
            ref="searchInput"
            v-model="searchQuery"
            type="text"
            placeholder="Buscar acciones, páginas, clientes..."
            class="search-input"
            @keydown.down.prevent="highlightNext"
            @keydown.up.prevent="highlightPrevious"
            @keydown.enter.prevent="executeCommand"
            @keydown.tab.prevent="executeCommand"
          />
          <div class="search-shortcut">
            <kbd>ESC</kbd>
          </div>
        </div>

        <!-- Results -->
        <div class="results-container" v-if="filteredCommands.length > 0">
          <!-- Quick Actions -->
          <div v-if="quickActions.length > 0" class="results-section">
            <div class="section-header">
              <i class="bi bi-lightning-fill"></i>
              <span>Acciones Rápidas</span>
            </div>
            <div
              v-for="(command, index) in quickActions"
              :key="`quick-${index}`"
              :class="['command-item', { 'highlighted': highlightedIndex === getCommandIndex(command) }]"
              @click="executeCommand(command)"
              @mouseenter="highlightedIndex = getCommandIndex(command)"
            >
              <div class="command-icon" :class="command.category">
                <i :class="command.icon"></i>
              </div>
              <div class="command-content">
                <div class="command-title">{{ command.title }}</div>
                <div class="command-description">{{ command.description }}</div>
              </div>
              <div class="command-shortcut" v-if="command.shortcut">
                <kbd v-for="key in command.shortcut" :key="key">{{ key }}</kbd>
              </div>
            </div>
          </div>

          <!-- Navigation -->
          <div v-if="navigationCommands.length > 0" class="results-section">
            <div class="section-header">
              <i class="bi bi-compass-fill"></i>
              <span>Navegación</span>
            </div>
            <div
              v-for="(command, index) in navigationCommands"
              :key="`nav-${index}`"
              :class="['command-item', { 'highlighted': highlightedIndex === getCommandIndex(command) }]"
              @click="executeCommand(command)"
              @mouseenter="highlightedIndex = getCommandIndex(command)"
            >
              <div class="command-icon" :class="command.category">
                <i :class="command.icon"></i>
              </div>
              <div class="command-content">
                <div class="command-title">{{ command.title }}</div>
                <div class="command-description">{{ command.description }}</div>
              </div>
              <div class="command-path">{{ command.path }}</div>
            </div>
          </div>

          <!-- Data Search -->
          <div v-if="dataCommands.length > 0" class="results-section">
            <div class="section-header">
              <i class="bi bi-database-fill"></i>
              <span>Datos</span>
            </div>
            <div
              v-for="(command, index) in dataCommands"
              :key="`data-${index}`"
              :class="['command-item', { 'highlighted': highlightedIndex === getCommandIndex(command) }]"
              @click="executeCommand(command)"
              @mouseenter="highlightedIndex = getCommandIndex(command)"
            >
              <div class="command-icon" :class="command.category">
                <i :class="command.icon"></i>
              </div>
              <div class="command-content">
                <div class="command-title">{{ command.title }}</div>
                <div class="command-description">{{ command.description }}</div>
              </div>
              <div class="command-meta">{{ command.meta }}</div>
            </div>
          </div>
        </div>

        <!-- No Results -->
        <div v-else-if="searchQuery.length > 0" class="no-results">
          <i class="bi bi-search"></i>
          <h3>Sin resultados</h3>
          <p>No se encontraron comandos para "<strong>{{ searchQuery }}</strong>"</p>
        </div>

        <!-- Initial State -->
        <div v-else class="initial-state">
          <div class="welcome-message">
            <i class="bi bi-lightning-charge"></i>
            <h3>Command Palette</h3>
            <p>Busca acciones, navega rápidamente o encuentra datos</p>
          </div>
          
          <div class="quick-tips">
            <h4>Tips rápidos:</h4>
            <div class="tip-item">
              <kbd>Ctrl</kbd> + <kbd>K</kbd> <span>Abrir/Cerrar</span>
            </div>
            <div class="tip-item">
              <kbd>↑</kbd> <kbd>↓</kbd> <span>Navegar</span>
            </div>
            <div class="tip-item">
              <kbd>Enter</kbd> <span>Ejecutar</span>
            </div>
          </div>
        </div>

        <!-- Footer -->
        <div class="palette-footer">
          <div class="footer-shortcuts">
            <span><kbd>↑</kbd><kbd>↓</kbd> navegar</span>
            <span><kbd>Enter</kbd> seleccionar</span>
            <span><kbd>ESC</kbd> cerrar</span>
          </div>
        </div>
      </div>
    </div>
  </teleport>
</template>

<script>
export default {
  name: 'CommandPalette',
  
  props: {
    modelValue: {
      type: Boolean,
      default: false
    }
  },

  emits: ['update:modelValue'],

  data() {
    return {
      searchQuery: '',
      highlightedIndex: 0,
      commands: [
        // Quick Actions
        {
          id: 'new-client',
          title: 'Nuevo Cliente',
          description: 'Registrar un nuevo cliente',
          icon: 'bi bi-person-plus-fill',
          category: 'action',
          type: 'quick',
          action: () => this.$router.push('/clienteregistrar'),
          keywords: ['nuevo', 'cliente', 'registrar', 'agregar', 'crear']
        },
        {
          id: 'new-product',
          title: 'Nuevo Producto',
          description: 'Agregar producto al inventario',
          icon: 'bi bi-plus-circle-fill',
          category: 'action',
          type: 'quick',
          action: () => this.$router.push('/productoregistrar'),
          keywords: ['nuevo', 'producto', 'agregar', 'inventario', 'stock']
        },
        {
          id: 'new-order',
          title: 'Nueva Orden',
          description: 'Crear orden de trabajo',
          icon: 'bi bi-clipboard-plus-fill',
          category: 'action',
          type: 'quick',
          action: () => this.$router.push('/ordenregistrar'),
          keywords: ['nueva', 'orden', 'trabajo', 'crear', 'reparacion']
        },
        {
          id: 'new-budget',
          title: 'Nuevo Presupuesto',
          description: 'Generar presupuesto para cliente',
          icon: 'bi bi-calculator-fill',
          category: 'action',
          type: 'quick',
          action: () => this.$router.push('/presupuestoregistrar'),
          keywords: ['nuevo', 'presupuesto', 'cotizar', 'precio']
        },

        // Navigation
        {
          id: 'dashboard',
          title: 'Dashboard',
          description: 'Página principal con métricas',
          icon: 'bi bi-house-fill',
          category: 'navigation',
          type: 'navigation',
          path: '/inicio',
          action: () => this.$router.push('/inicio'),
          keywords: ['inicio', 'dashboard', 'principal', 'home', 'resumen']
        },
        {
          id: 'clients-list',
          title: 'Lista de Clientes',
          description: 'Ver todos los clientes registrados',
          icon: 'bi bi-people-fill',
          category: 'navigation',
          type: 'navigation',
          path: '/clienteslistar',
          action: () => this.$router.push('/clienteslistar'),
          keywords: ['clientes', 'lista', 'listar', 'ver', 'todos']
        },
        {
          id: 'products-list',
          title: 'Lista de Productos',
          description: 'Ver inventario de productos',
          icon: 'bi bi-box-fill',
          category: 'navigation',
          type: 'navigation',
          path: '/productoslistar',
          action: () => this.$router.push('/productoslistar'),
          keywords: ['productos', 'inventario', 'stock', 'lista', 'ver']
        },
        {
          id: 'orders-list',
          title: 'Órdenes de Trabajo',
          description: 'Ver todas las órdenes activas',
          icon: 'bi bi-clipboard-check-fill',
          category: 'navigation',
          type: 'navigation',
          path: '/ordeneslistar',
          action: () => this.$router.push('/ordeneslistar'),
          keywords: ['ordenes', 'trabajo', 'reparaciones', 'activas']
        },
        {
          id: 'budgets-list',
          title: 'Lista de Presupuestos',
          description: 'Ver todos los presupuestos',
          icon: 'bi bi-calculator-fill',
          category: 'navigation',
          type: 'navigation',
          path: '/presupuestoslistar',
          action: () => this.$router.push('/presupuestoslistar'),
          keywords: ['presupuestos', 'cotizaciones', 'lista', 'ver']
        },
        {
          id: 'search-client',
          title: 'Buscar Cliente',
          description: 'Buscar cliente específico',
          icon: 'bi bi-search',
          category: 'navigation',
          type: 'navigation',
          path: '/clientebuscar',
          action: () => this.$router.push('/clientebuscar'),
          keywords: ['buscar', 'cliente', 'encontrar', 'localizar']
        },

        // Data commands (estos se pueden cargar dinámicamente)
        {
          id: 'profile',
          title: 'Mi Perfil',
          description: 'Configuración de cuenta personal',
          icon: 'bi bi-person-gear',
          category: 'account',
          type: 'navigation',
          path: '/perfilcuenta',
          action: () => this.$router.push('/perfilcuenta'),
          keywords: ['perfil', 'cuenta', 'configuracion', 'usuario']
        }
      ]
    }
  },

  computed: {
    isOpen: {
      get() {
        return this.modelValue;
      },
      set(value) {
        this.$emit('update:modelValue', value);
      }
    },

    filteredCommands() {
      if (!this.searchQuery.trim()) {
        return this.commands.slice(0, 8); // Mostrar algunos comandos por defecto
      }

      const query = this.searchQuery.toLowerCase();
      return this.commands.filter(command => {
        return (
          command.title.toLowerCase().includes(query) ||
          command.description.toLowerCase().includes(query) ||
          command.keywords.some(keyword => keyword.includes(query))
        );
      });
    },

    quickActions() {
      return this.filteredCommands.filter(cmd => cmd.type === 'quick');
    },

    navigationCommands() {
      return this.filteredCommands.filter(cmd => cmd.type === 'navigation');
    },

    dataCommands() {
      return this.filteredCommands.filter(cmd => cmd.type === 'data');
    }
  },

  methods: {
    open() {
      this.isOpen = true;
      this.searchQuery = '';
      this.highlightedIndex = 0;
      this.$nextTick(() => {
        this.$refs.searchInput?.focus();
      });
    },

    close() {
      this.isOpen = false;
      this.searchQuery = '';
      this.highlightedIndex = 0;
    },

    getCommandIndex(command) {
      return this.filteredCommands.indexOf(command);
    },

    highlightNext() {
      if (this.highlightedIndex < this.filteredCommands.length - 1) {
        this.highlightedIndex++;
      }
    },

    highlightPrevious() {
      if (this.highlightedIndex > 0) {
        this.highlightedIndex--;
      }
    },

    executeCommand(command = null) {
      const commandToExecute = command || this.filteredCommands[this.highlightedIndex];
      
      if (commandToExecute && commandToExecute.action) {
        commandToExecute.action();
        this.close();
      }
    },

    handleGlobalKeydown(event) {
      // Abrir con Ctrl+K o Cmd+K
      if ((event.ctrlKey || event.metaKey) && event.key === 'k') {
        event.preventDefault();
        if (this.isOpen) {
          this.close();
        } else {
          this.open();
        }
      }
    }
  },

  mounted() {
    document.addEventListener('keydown', this.handleGlobalKeydown);
  },

  beforeUnmount() {
    document.removeEventListener('keydown', this.handleGlobalKeydown);
  },

  watch: {
    isOpen(newValue) {
      if (newValue) {
        document.body.style.overflow = 'hidden';
      } else {
        document.body.style.overflow = '';
      }
    },

    searchQuery() {
      this.highlightedIndex = 0;
    }
  }
}
</script>

<style scoped>
/* ============================================ */
/* COMMAND PALETTE OVERLAY */
/* ============================================ */
.command-palette-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  z-index: 9999;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding-top: 15vh;
  animation: fadeIn 0.15s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* ============================================ */
/* COMMAND PALETTE CONTAINER */
/* ============================================ */
.command-palette {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  width: 90%;
  max-width: 640px;
  max-height: 70vh;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.2);
  animation: slideIn 0.2s ease-out;
  display: flex;
  flex-direction: column;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* ============================================ */
/* SEARCH CONTAINER */
/* ============================================ */
.search-container {
  display: flex;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  gap: 12px;
}

.search-icon {
  color: #7f8c8d;
  font-size: 20px;
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 18px;
  color: #2c3e50;
  background: transparent;
  font-weight: 500;
}

.search-input::placeholder {
  color: #bdc3c7;
  font-weight: 400;
}

.search-shortcut {
  display: flex;
  gap: 4px;
}

/* ============================================ */
/* RESULTS CONTAINER */
/* ============================================ */
.results-container {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
}

.results-section {
  margin-bottom: 8px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px 8px 24px;
  color: #7f8c8d;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* ============================================ */
/* COMMAND ITEMS */
/* ============================================ */
.command-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 24px;
  cursor: pointer;
  transition: all 0.15s ease;
  border-left: 3px solid transparent;
}

.command-item:hover,
.command-item.highlighted {
  background: rgba(52, 152, 219, 0.08);
  border-left-color: #3498db;
}

.command-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
  flex-shrink: 0;
}

.command-icon.action { background: linear-gradient(135deg, #3498db, #2980b9); }
.command-icon.navigation { background: linear-gradient(135deg, #2ecc71, #27ae60); }
.command-icon.account { background: linear-gradient(135deg, #9b59b6, #8e44ad); }
.command-icon.data { background: linear-gradient(135deg, #f39c12, #e67e22); }

.command-content {
  flex: 1;
  min-width: 0;
}

.command-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 2px;
}

.command-description {
  font-size: 14px;
  color: #7f8c8d;
  line-height: 1.3;
}

.command-shortcut {
  display: flex;
  gap: 4px;
  flex-shrink: 0;
}

.command-path {
  font-size: 12px;
  color: #95a5a6;
  font-family: monospace;
  background: rgba(0, 0, 0, 0.04);
  padding: 4px 8px;
  border-radius: 6px;
  flex-shrink: 0;
}

.command-meta {
  font-size: 12px;
  color: #95a5a6;
  flex-shrink: 0;
}

/* ============================================ */
/* STATES */
/* ============================================ */
.no-results,
.initial-state {
  padding: 40px 24px;
  text-align: center;
  color: #7f8c8d;
}

.no-results i,
.initial-state i {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.no-results h3,
.initial-state h3 {
  font-size: 20px;
  margin: 0 0 8px 0;
  color: #2c3e50;
}

.no-results p,
.initial-state p {
  margin: 0;
  font-size: 16px;
}

.welcome-message {
  margin-bottom: 32px;
}

.quick-tips h4 {
  color: #2c3e50;
  font-size: 14px;
  margin: 0 0 16px 0;
  font-weight: 600;
}

.tip-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 8px;
  font-size: 14px;
}

/* ============================================ */
/* FOOTER */
/* ============================================ */
.palette-footer {
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  padding: 12px 24px;
  background: rgba(248, 249, 250, 0.5);
  border-radius: 0 0 16px 16px;
}

.footer-shortcuts {
  display: flex;
  gap: 16px;
  justify-content: center;
  font-size: 12px;
  color: #7f8c8d;
}

/* ============================================ */
/* KBD ELEMENTS */
/* ============================================ */
kbd {
  background: rgba(0, 0, 0, 0.1);
  color: #2c3e50;
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
  font-family: monospace;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

/* ============================================ */
/* SCROLLBAR */
/* ============================================ */
.results-container::-webkit-scrollbar {
  width: 6px;
}

.results-container::-webkit-scrollbar-track {
  background: transparent;
}

.results-container::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 3px;
}

.results-container::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.2);
}

/* ============================================ */
/* RESPONSIVE */
/* ============================================ */
@media (max-width: 768px) {
  .command-palette-overlay {
    padding: 10vh 16px 16px 16px;
  }
  
  .command-palette {
    width: 100%;
    max-height: 80vh;
  }
  
  .search-container {
    padding: 16px 20px;
  }
  
  .search-input {
    font-size: 16px;
  }
  
  .command-item {
    padding: 12px 20px;
    gap: 12px;
  }
  
  .command-icon {
    width: 40px;
    height: 40px;
    font-size: 16px;
  }
  
  .command-title {
    font-size: 15px;
  }
  
  .command-description {
    font-size: 13px;
  }
  
  .command-shortcut {
    display: none;
  }
}
</style>