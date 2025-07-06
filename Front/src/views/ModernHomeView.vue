<template>
  <div class="modern-app-layout">
    <!-- Navbar fijo arriba -->
    <NavBar />
    
    <!-- Navigation Rail -->
    <NavigationRail @open-command-palette="openCommandPalette" />
    
    <!-- Command Palette -->
    <CommandPalette v-model="commandPaletteOpen" />

    <!-- Container principal -->
    <div class="modern-layout-container">
      <!-- Imagen de fondo solo para dashboard -->
      <div v-if="$route.path === '/inicio'" class="background-image">
        <img 
          src="@/assets/imagenes/reparacion_electronica.jpg" 
          alt="Reparación Electrónica"
          @error="handleImageError"
          @load="handleImageLoad"
        >
        <div class="image-overlay"></div>
      </div>

      <!-- Top Navigation (solo si no es dashboard) -->
      <TopNavigation 
        v-if="$route.path !== '/inicio'" 
        @open-command-palette="openCommandPalette" 
      />

      <!-- Dashboard Content (solo para /inicio) -->
      <main v-if="$route.path === '/inicio'" class="dashboard-main">
        <div class="dashboard-content">
          <!-- Header de Bienvenida -->
          <div class="welcome-section">
            <div class="welcome-content">
              <h1 class="welcome-title">¡Bienvenido de vuelta, {{ nombreUsuario }}!</h1>
              <p class="welcome-subtitle">Aquí tienes un resumen de tu sistema de gestión</p>
            </div>
            <div class="current-time">
              <i class="bi bi-clock"></i>
              <span>{{ fechaActual }}</span>
            </div>
          </div>

          <!-- Métricas Principales -->
          <div class="metrics-grid">
            <div class="metric-card metric-primary">
              <div class="metric-icon">
                <i class="bi bi-people-fill"></i>
              </div>
              <div class="metric-content">
                <h3 class="metric-number">{{ totalClientes }}</h3>
                <p class="metric-label">Clientes Activos</p>
                <span class="metric-change positive">+{{ clientesNuevos }} este mes</span>
              </div>
            </div>

            <div class="metric-card metric-success">
              <div class="metric-icon">
                <i class="bi bi-box-fill"></i>
              </div>
              <div class="metric-content">
                <h3 class="metric-number">{{ totalProductos }}</h3>
                <p class="metric-label">Productos en Stock</p>
                <span class="metric-change neutral">{{ productosRecientes }} agregados</span>
              </div>
            </div>

            <div class="metric-card metric-warning">
              <div class="metric-icon">
                <i class="bi bi-clipboard-check-fill"></i>
              </div>
              <div class="metric-content">
                <h3 class="metric-number">{{ ordenesActivas }}</h3>
                <p class="metric-label">Órdenes Activas</p>
                <span class="metric-change pending">{{ ordenesPendientes }} pendientes</span>
              </div>
            </div>

            <div class="metric-card metric-info">
              <div class="metric-icon">
                <i class="bi bi-calculator-fill"></i>
              </div>
              <div class="metric-content">
                <h3 class="metric-number">${{ ingresosMes }}</h3>
                <p class="metric-label">Ingresos del Mes</p>
                <span class="metric-change positive">+15% vs anterior</span>
              </div>
            </div>
          </div>

          <!-- Acciones Rápidas -->
          <div class="quick-actions-section">
            <h2 class="section-title">
              <i class="bi bi-lightning-fill"></i>
              Acciones Rápidas
            </h2>
            <div class="quick-actions-grid">
              <router-link to="/clienteregistrar" class="quick-action-card">
                <div class="action-icon client">
                  <i class="bi bi-person-plus-fill"></i>
                </div>
                <h3>Nuevo Cliente</h3>
                <p>Registrar un cliente nuevo en el sistema</p>
              </router-link>

              <router-link to="/productoregistrar" class="quick-action-card">
                <div class="action-icon product">
                  <i class="bi bi-plus-circle-fill"></i>
                </div>
                <h3>Nuevo Producto</h3>
                <p>Agregar producto al inventario</p>
              </router-link>

              <router-link to="/ordenregistrar" class="quick-action-card">
                <div class="action-icon order">
                  <i class="bi bi-clipboard-plus-fill"></i>
                </div>
                <h3>Nueva Orden</h3>
                <p>Crear orden de trabajo</p>
              </router-link>

              <router-link to="/presupuestoregistrar" class="quick-action-card">
                <div class="action-icon budget">
                  <i class="bi bi-calculator-fill"></i>
                </div>
                <h3>Nuevo Presupuesto</h3>
                <p>Generar presupuesto para cliente</p>
              </router-link>
            </div>
          </div>

          <!-- Resumen de Actividad Reciente -->
          <div class="recent-activity-section">
            <h2 class="section-title">
              <i class="bi bi-activity"></i>
              Actividad Reciente
            </h2>
            <div class="activity-cards">
              <div class="activity-card">
                <div class="activity-icon client">
                  <i class="bi bi-person-check"></i>
                </div>
                <div class="activity-content">
                  <h4>Cliente agregado</h4>
                  <p>Juan Pérez - hace 2 horas</p>
                </div>
              </div>

              <div class="activity-card">
                <div class="activity-icon order">
                  <i class="bi bi-clipboard-check"></i>
                </div>
                <div class="activity-content">
                  <h4>Orden completada</h4>
                  <p>Reparación #1234 - hace 4 horas</p>
                </div>
              </div>

              <div class="activity-card">
                <div class="activity-icon product">
                  <i class="bi bi-box-arrow-in-down"></i>
                </div>
                <div class="activity-content">
                  <h4>Stock actualizado</h4>
                  <p>15 productos agregados - ayer</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>

      <!-- Router view para otras páginas -->
      <div v-else class="router-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/common/NavBar.vue';
import NavigationRail from '@/components/common/NavigationRail.vue';
import CommandPalette from '@/components/common/CommandPalette.vue';
import TopNavigation from '@/components/common/TopNavigation.vue';

export default {
  name: 'HomeView',
  components: {
    NavBar,
    NavigationRail,
    CommandPalette,
    TopNavigation
  },

  data() {
    return {
      commandPaletteOpen: false,
      nombreUsuario: 'Hectorla',
      totalClientes: 0,
      clientesNuevos: 0,
      totalProductos: 0,
      productosRecientes: 0,
      ordenesActivas: 0,
      ordenesPendientes: 0,
      ingresosMes: '12,450',
      fechaActual: ''
    }
  },

  methods: {
    handleImageError(event) {
      console.error('❌ Error al cargar la imagen:', event);
    },

    handleImageLoad() {
      console.log('✅ Imagen de fondo cargada correctamente');
    },

    openCommandPalette() {
      this.commandPaletteOpen = true;
    },

    obtenerUsuario() {
      try {
        const usuarioString = localStorage.getItem('usuario');
        if (usuarioString) {
          const usuario = JSON.parse(usuarioString);
          this.nombreUsuario = usuario?.nombre || usuarioString;
        }
      } catch (error) {
        this.nombreUsuario = localStorage.getItem('usuario') || 'Usuario';
      }
    },

    cargarEstadisticas() {
      try {
        const clientes = JSON.parse(localStorage.getItem('clientes') || '[]');
        this.totalClientes = clientes.filter(c => !c.deleted).length;
        this.clientesNuevos = Math.floor(this.totalClientes * 0.1);
        
        this.totalProductos = 156;
        this.productosRecientes = 8;
        this.ordenesActivas = 23;
        this.ordenesPendientes = 7;
      } catch (error) {
        console.error('Error al cargar estadísticas:', error);
      }
    },

    actualizarFecha() {
      const ahora = new Date();
      const opciones = { 
        weekday: 'long', 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      };
      this.fechaActual = ahora.toLocaleDateString('es-ES', opciones);
    }
  },

  mounted() {
    this.obtenerUsuario();
    this.cargarEstadisticas();
    this.actualizarFecha();
    
    setInterval(this.actualizarFecha, 60000);
  }
}
</script>

<style scoped>
/* ============================================ */
/* MODERN LAYOUT BASE */
/* ============================================ */
.modern-app-layout {
  min-height: 100vh;
  background-color: #f8f9fa;
  display: flex;
  flex-direction: column;
  width: 100%;
  margin: 0;
  padding: 0;
}

.modern-layout-container {
  margin-top: 80px;
  margin-left: 65px;
  min-height: calc(100vh - 80px);
  width: calc(100vw - 65px);
  display: flex;
  flex-direction: column;
  background-color: #f8f9fa;
  position: relative;
}

/* ============================================ */
/* IMAGEN DE FONDO */
/* ============================================ */
.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  overflow: hidden;
}

.background-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  filter: brightness(0.3) contrast(1.1) blur(1px);
  transition: opacity 0.3s ease-in-out;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    rgba(44, 62, 80, 0.7) 0%,
    rgba(52, 152, 219, 0.5) 50%,
    rgba(44, 62, 80, 0.8) 100%
  );
  pointer-events: none;
}

/* ============================================ */
/* DASHBOARD MAIN */
/* ============================================ */
.dashboard-main {
  position: relative;
  z-index: 2;
  flex: 1;
  overflow-y: auto;
}

.dashboard-content {
  padding: 40px;
  min-height: 100%;
  width: 100%;
}

/* ============================================ */
/* BIENVENIDA */
/* ============================================ */
.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  background: rgba(255, 255, 255, 0.95);
  padding: 30px 35px;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.welcome-content h1 {
  color: #2c3e50;
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 10px 0;
}

.welcome-content p {
  color: #7f8c8d;
  font-size: 18px;
  margin: 0;
}

.current-time {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #3498db;
  font-weight: 600;
  font-size: 16px;
}

/* ============================================ */
/* MÉTRICAS */
/* ============================================ */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 25px;
  margin-bottom: 50px;
}

.metric-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 30px;
  display: flex;
  align-items: center;
  gap: 25px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.metric-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.metric-icon {
  width: 70px;
  height: 70px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  flex-shrink: 0;
}

.metric-primary .metric-icon { background: linear-gradient(135deg, #3498db, #2980b9); }
.metric-success .metric-icon { background: linear-gradient(135deg, #2ecc71, #27ae60); }
.metric-warning .metric-icon { background: linear-gradient(135deg, #f39c12, #e67e22); }
.metric-info .metric-icon { background: linear-gradient(135deg, #9b59b6, #8e44ad); }

.metric-content h3 {
  font-size: 36px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 8px 0;
}

.metric-content p {
  font-size: 16px;
  color: #7f8c8d;
  margin: 0 0 10px 0;
  font-weight: 500;
}

.metric-change {
  font-size: 13px;
  font-weight: 600;
  padding: 4px 10px;
  border-radius: 15px;
}

.metric-change.positive {
  background: rgba(46, 204, 113, 0.15);
  color: #27ae60;
}

.metric-change.neutral {
  background: rgba(52, 152, 219, 0.15);
  color: #2980b9;
}

.metric-change.pending {
  background: rgba(243, 156, 18, 0.15);
  color: #d68910;
}

/* ============================================ */
/* SECCIONES */
/* ============================================ */
.quick-actions-section,
.recent-activity-section {
  margin-bottom: 50px;
}

.section-title {
  color: white;
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 25px;
  display: flex;
  align-items: center;
  gap: 12px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* ============================================ */
/* ACCIONES RÁPIDAS */
/* ============================================ */
.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 25px;
}

.quick-action-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 30px;
  text-decoration: none;
  color: inherit;
  transition: all 0.3s ease;
  text-align: center;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.quick-action-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
  text-decoration: none;
  color: inherit;
}

.action-icon {
  width: 80px;
  height: 80px;
  border-radius: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  font-size: 32px;
  color: white;
}

.action-icon.client { background: linear-gradient(135deg, #3498db, #2980b9); }
.action-icon.product { background: linear-gradient(135deg, #2ecc71, #27ae60); }
.action-icon.order { background: linear-gradient(135deg, #f39c12, #e67e22); }
.action-icon.budget { background: linear-gradient(135deg, #9b59b6, #8e44ad); }

.quick-action-card h3 {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 10px 0;
}

.quick-action-card p {
  font-size: 16px;
  color: #7f8c8d;
  margin: 0;
  line-height: 1.5;
}

/* ============================================ */
/* ACTIVIDAD RECIENTE */
/* ============================================ */
.activity-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.activity-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 14px;
  padding: 25px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.activity-card:hover {
  transform: translateX(8px);
}

.activity-card .activity-icon {
  width: 50px;
  height: 50px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
}

.activity-content h4 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 6px 0;
}

.activity-content p {
  font-size: 16px;
  color: #7f8c8d;
  margin: 0;
}

/* ============================================ */
/* ROUTER CONTENT */
/* ============================================ */
.router-content {
  position: relative;
  z-index: 2;
  flex: 1;
  background: transparent;
  width: 100%;
}

/* ============================================ */
/* RESPONSIVE */
/* ============================================ */
@media (max-width: 768px) {
  .modern-layout-container {
    margin-left: 0;
    width: 100vw;
    margin-top: 140px;
  }
  
  .dashboard-content {
    padding: 25px 20px;
  }
  
  .welcome-section {
    flex-direction: column;
    text-align: center;
    gap: 20px;
    padding: 25px;
  }
  
  .welcome-content h1 {
    font-size: 28px;
  }
  
  .metrics-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .quick-actions-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
  }
}

@media (max-width: 480px) {
  .quick-actions-grid {
    grid-template-columns: 1fr;
  }
  
  .welcome-content h1 {
    font-size: 24px;
  }
  
  .section-title {
    font-size: 22px;
  }
  
  .dashboard-content {
    padding: 20px 15px;
  }
}
</style>