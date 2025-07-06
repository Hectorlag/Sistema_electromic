<template>
  <div class="app-layout">
    <!-- Navbar fijo arriba -->
    <NavBar />

    <!-- Container principal -->
    <div class="layout-container">
      <!-- Sidebar -->
      <aside class="sidebar-column">
        <Sidebar />
      </aside>

      <!-- Área de contenido -->
      <main class="content-column">
        <!-- Imagen de fondo solo para inicio -->
        <div v-if="$route.path === '/inicio'" class="background-image">
          <img 
            src="@/assets/imagenes/reparacion_electronica.jpg" 
            alt="Reparación Electrónica"
            @error="handleImageError"
            @load="handleImageLoad"
          >
          <div class="image-overlay"></div>
        </div>

        <!-- Dashboard Content -->
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

        <!-- Router view con fondo transparente (para otras rutas) -->
        <div v-if="$route.path !== '/inicio'" class="router-content">
          <router-view />
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/common/Sidebar.vue'
import NavBar from '@/components/common/NavBar.vue'

export default {
  name: 'HomeView',
  components: {
    Sidebar,
    NavBar
  },

  data() {
    return {
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
      console.error('❌ Error al cargar la imagen:', event)
    },

    handleImageLoad() {
      console.log('✅ Imagen de fondo cargada correctamente')
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
      // Simular carga de estadísticas - aquí irían las llamadas a la API
      try {
        const clientes = JSON.parse(localStorage.getItem('clientes') || '[]');
        this.totalClientes = clientes.filter(c => !c.deleted).length;
        this.clientesNuevos = Math.floor(this.totalClientes * 0.1); // 10% aproximado
        
        // Simular otras estadísticas
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
    
    // Actualizar la hora cada minuto
    setInterval(this.actualizarFecha, 60000);
  }
}
</script>

<style scoped>
/* ============================================ */
/* LAYOUT BASE CON CSS GRID */
/* ============================================ */
.app-layout {
  min-height: 100vh;
  background-color: #f8f9fa;
  display: flex;
  flex-direction: column;
  width: 100%;
  margin: 0;
  padding: 0;
}

.layout-container {
  display: grid;
  grid-template-columns: 250px 1fr;
  grid-template-rows: 1fr;
  margin-top: 80px;
  min-height: calc(100vh - 80px);
  width: 100vw;
  margin-left: 0;
  margin-right: 0;
  padding: 0;
  gap: 0;
}

.sidebar-column {
  grid-column: 1;
  width: 250px;
  min-width: 250px;
  max-width: 250px;
  margin: 0;
  padding: 0;
  background-color: #2c3e50;
  overflow-y: auto;
  overflow-x: hidden;
}

.content-column {
  grid-column: 2;
  width: 100%;
  min-width: 0;
  position: relative;
  overflow-y: auto;
  overflow-x: hidden;
  min-height: calc(100vh - 80px);
  background-color: #f8f9fa;
  box-sizing: border-box;
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
/* CONTENIDO DEL DASHBOARD */
/* ============================================ */
.dashboard-content {
  position: relative;
  z-index: 2;
  padding: 30px 40px;
  min-height: 100%;
  width: 100%;
}

/* ============================================ */
/* SECCIÓN DE BIENVENIDA */
/* ============================================ */
.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  background: rgba(255, 255, 255, 0.95);
  padding: 25px 30px;
  border-radius: 15px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.welcome-content h1 {
  color: #2c3e50;
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 8px 0;
}

.welcome-content p {
  color: #7f8c8d;
  font-size: 16px;
  margin: 0;
}

.current-time {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #3498db;
  font-weight: 500;
  font-size: 14px;
}

/* ============================================ */
/* GRID DE MÉTRICAS */
/* ============================================ */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.metric-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  padding: 25px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.metric-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
}

.metric-icon {
  width: 60px;
  height: 60px;
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  flex-shrink: 0;
}

.metric-primary .metric-icon { background: linear-gradient(135deg, #3498db, #2980b9); }
.metric-success .metric-icon { background: linear-gradient(135deg, #2ecc71, #27ae60); }
.metric-warning .metric-icon { background: linear-gradient(135deg, #f39c12, #e67e22); }
.metric-info .metric-icon { background: linear-gradient(135deg, #9b59b6, #8e44ad); }

.metric-content h3 {
  font-size: 32px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 5px 0;
}

.metric-content p {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0 0 8px 0;
  font-weight: 500;
}

.metric-change {
  font-size: 12px;
  font-weight: 600;
  padding: 3px 8px;
  border-radius: 12px;
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
  margin-bottom: 40px;
}

.section-title {
  color: white;
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* ============================================ */
/* ACCIONES RÁPIDAS */
/* ============================================ */
.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
}

.quick-action-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  padding: 25px;
  text-decoration: none;
  color: inherit;
  transition: all 0.3s ease;
  text-align: center;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.quick-action-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  text-decoration: none;
  color: inherit;
}

.action-icon {
  width: 70px;
  height: 70px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 15px;
  font-size: 28px;
  color: white;
}

.action-icon.client { background: linear-gradient(135deg, #3498db, #2980b9); }
.action-icon.product { background: linear-gradient(135deg, #2ecc71, #27ae60); }
.action-icon.order { background: linear-gradient(135deg, #f39c12, #e67e22); }
.action-icon.budget { background: linear-gradient(135deg, #9b59b6, #8e44ad); }

.quick-action-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 8px 0;
}

.quick-action-card p {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0;
  line-height: 1.4;
}

/* ============================================ */
/* ACTIVIDAD RECIENTE */
/* ============================================ */
.activity-cards {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.activity-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  transition: transform 0.3s ease;
}

.activity-card:hover {
  transform: translateX(5px);
}

.activity-card .activity-icon {
  width: 45px;
  height: 45px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
  flex-shrink: 0;
}

.activity-content h4 {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 4px 0;
}

.activity-content p {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0;
}

/* ============================================ */
/* ROUTER CONTENT (OTRAS RUTAS) */
/* ============================================ */
.router-content {
  position: relative;
  z-index: 2;
  background: transparent;
  min-height: 100%;
  width: 100%;
  padding: 0;
  margin: 0;
}

/* ============================================ */
/* RESPONSIVE */
/* ============================================ */
@media (max-width: 992px) {
  .layout-container {
    grid-template-columns: 1fr;
    grid-template-rows: auto 1fr;
  }
  
  .sidebar-column {
    grid-column: 1;
    grid-row: 1;
    width: 100%;
    min-width: 100%;
    max-width: 100%;
    height: auto;
  }
  
  .content-column {
    grid-column: 1;
    grid-row: 2;
  }
  
  .dashboard-content {
    padding: 20px 15px;
  }
}

@media (max-width: 768px) {
  .welcome-section {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }
  
  .metrics-grid {
    grid-template-columns: 1fr;
  }
  
  .quick-actions-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .dashboard-content {
    padding: 15px 10px;
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
    font-size: 20px;
  }
}
</style>