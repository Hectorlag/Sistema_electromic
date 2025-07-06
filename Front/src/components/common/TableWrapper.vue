<!-- ============================================ -->
<!-- 📁 components/common/TableWrapper.vue -->
<!-- ============================================ -->
<template>
  <div class="app-layout">
    <!-- Navbar fijo arriba -->
    <NavBar />
    
    <!-- Container principal con CSS Grid -->
    <div class="layout-container">
      <!-- Sidebar - Columna fija -->
      <aside class="sidebar-column">
        <Sidebar />
      </aside>
      
      <!-- Área de contenido - Toma todo el espacio restante -->
      <main class="content-column">
        <div class="listado-container">
          <!-- Header con título y botón crear -->
          <div class="listado-header">
            <h2 class="listado-titulo">{{ titulo }}</h2>
            <router-link v-if="createRoute" :to="createRoute" class="btn-crear">
              <i class="bi bi-plus-circle-fill"></i>
              {{ createButtonText }}
            </router-link>
          </div>
          
          <!-- Contenido de la tabla (slot) -->
          <div class="listado-tabla">
            <slot></slot>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/common/NavBar.vue';
import Sidebar from '@/components/common/Sidebar.vue';

export default {
  name: 'TableWrapper',
  components: {
    NavBar,
    Sidebar
  },
  props: {
    // Título del listado
    titulo: {
      type: String,
      required: true
    },
    // Ruta para el botón crear
    createRoute: {
      type: String,
      default: null
    },
    // Texto del botón crear
    createButtonText: {
      type: String,
      default: 'Crear Nuevo'
    }
  }
}
</script>

<style scoped>
/* ============================================ */
/* LAYOUT CON CSS GRID - CONTROL TOTAL */
/* ============================================ */
.app-layout {
  min-height: 100vh !important;
  background-color: #f8f9fa !important;
  display: flex !important;
  flex-direction: column !important;
  width: 100% !important;
  margin: 0 !important;
  padding: 0 !important;
}

.layout-container {
  /* CSS Grid para control exacto del layout */
  display: grid !important;
  grid-template-columns: 250px 1fr !important; /* Sidebar fijo 250px, contenido ocupa el resto */
  grid-template-rows: 1fr !important;
  
  /* Posicionamiento y dimensiones */
  margin-top: 80px !important;
  min-height: calc(100vh - 80px) !important;
  width: 100vw !important;
  
  /* Reset de márgenes */
  margin-left: 0 !important;
  margin-right: 0 !important;
  padding: 0 !important;
  
  /* Sin gaps entre columnas */
  gap: 0 !important;
}

/* ============================================ */
/* COLUMNA DEL SIDEBAR - ANCHO FIJO */
/* ============================================ */
.sidebar-column {
  /* Fijo en la primera columna del grid */
  grid-column: 1 !important;
  
  /* Ancho exacto */
  width: 250px !important;
  min-width: 250px !important;
  max-width: 250px !important;
  
  /* Sin márgenes ni padding */
  margin: 0 !important;
  padding: 0 !important;
  
  /* Background del sidebar */
  background-color: #2c3e50 !important;
  
  /* Scroll interno si es necesario */
  overflow-y: auto !important;
  overflow-x: hidden !important;
}

/* ============================================ */
/* COLUMNA DEL CONTENIDO - OCUPA TODO EL RESTO */
/* ============================================ */
.content-column {
  /* Segunda columna del grid - toma todo el espacio restante */
  grid-column: 2 !important;
  
  /* Ancho máximo disponible */
  width: 100% !important;
  min-width: 0 !important; /* Permite que se contraiga si es necesario */
  
  /* Padding para el contenido */
  padding: 25px 30px 25px 25px !important;
  
  /* Background y scroll */
  background-color: #f8f9fa !important;
  overflow-y: auto !important;
  overflow-x: hidden !important;
  
  /* Altura completa */
  min-height: calc(100vh - 80px) !important;
  
  /* Box sizing */
  box-sizing: border-box !important;
}

/* ============================================ */
/* CONTENEDOR DEL LISTADO - ANCHO COMPLETO */
/* ============================================ */
.listado-container {
  width: 100% !important;
  height: 100% !important;
  margin: 0 !important;
  padding: 0 !important;
}

.listado-header {
  display: flex !important;
  justify-content: space-between !important;
  align-items: center !important;
  margin-bottom: 20px !important;
  background: white !important;
  padding: 15px 20px !important;
  border-radius: 8px !important;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1) !important;
  width: 100% !important;
  box-sizing: border-box !important;
}

.listado-titulo {
  background-color: #7399c2 !important;
  color: white !important;
  padding: 12px 20px !important;
  margin: 0 !important;
  border-radius: 6px !important;
  font-size: 20px !important;
  font-weight: 600 !important;
}

.btn-crear {
  background: linear-gradient(135deg, #28a745, #20c997) !important;
  color: white !important;
  padding: 10px 20px !important;
  border-radius: 6px !important;
  text-decoration: none !important;
  font-weight: 500 !important;
  display: flex !important;
  align-items: center !important;
  gap: 8px !important;
  transition: all 0.3s ease !important;
  box-shadow: 0 2px 8px rgba(40, 167, 69, 0.3) !important;
  flex-shrink: 0 !important;
  white-space: nowrap !important;
}

.btn-crear:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 4px 12px rgba(40, 167, 69, 0.4) !important;
  color: white !important;
}

.listado-tabla {
  width: 100% !important;
  background: white !important;
  border-radius: 8px !important;
  overflow: hidden !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1) !important;
  box-sizing: border-box !important;
}

/* ============================================ */
/* RESPONSIVE - STACK EN MÓVILES */
/* ============================================ */
@media (max-width: 992px) {
  .layout-container {
    /* Cambiar a layout vertical en móviles */
    grid-template-columns: 1fr !important;
    grid-template-rows: auto 1fr !important;
  }
  
  .sidebar-column {
    grid-column: 1 !important;
    grid-row: 1 !important;
    width: 100% !important;
    min-width: 100% !important;
    max-width: 100% !important;
    height: auto !important;
  }
  
  .content-column {
    grid-column: 1 !important;
    grid-row: 2 !important;
    padding: 20px 15px !important;
  }
}

@media (max-width: 768px) {
  .content-column {
    padding: 15px 10px !important;
  }
  
  .listado-header {
    flex-direction: column !important;
    gap: 15px !important;
    text-align: center !important;
  }
  
  .listado-titulo {
    font-size: 18px !important;
    width: 100% !important;
  }
  
  .btn-crear {
    width: 100% !important;
    justify-content: center !important;
  }
}

@media (max-width: 480px) {
  .content-column {
    padding: 10px 8px !important;
  }
  
  .listado-header {
    padding: 10px !important;
  }
  
  .listado-titulo {
    font-size: 16px !important;
    padding: 10px 15px !important;
  }
  
  .btn-crear {
    padding: 8px 16px !important;
    font-size: 14px !important;
  }
}
</style>