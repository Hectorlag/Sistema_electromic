<!-- ============================================ -->
<!-- 📁 components/common/ModernTableWrapper.vue -->
<!-- ============================================ -->
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
      <!-- Top Navigation -->
      <TopNavigation @open-command-palette="openCommandPalette" />
      
      <!-- Área de contenido - ocupar todo el ancho -->
      <main class="modern-content-area-table">
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
import NavigationRail from '@/components/common/NavigationRail.vue';
import CommandPalette from '@/components/common/CommandPalette.vue';
import TopNavigation from '@/components/common/TopNavigation.vue';

export default {
  name: 'ModernTableWrapper',
  components: {
    NavBar,
    NavigationRail,
    CommandPalette,
    TopNavigation
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
  },

  data() {
    return {
      commandPaletteOpen: false
    }
  },

  methods: {
    openCommandPalette() {
      this.commandPaletteOpen = true;
    }
  }
}
</script>

<style scoped>
/* ============================================ */
/* MODERN LAYOUT - TABLA OCUPA TODO EL ANCHO */
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
  margin-top: 80px; /* Espacio para navbar */
  margin-left: 65px; /* Espacio para navigation rail */
  min-height: calc(100vh - 80px);
  width: calc(100vw - 65px);
  display: flex;
  flex-direction: column;
  background-color: #f8f9fa;
}

/* ============================================ */
/* ÁREA DE CONTENIDO - TODO EL ANCHO */
/* ============================================ */
.modern-content-area-table {
  flex: 1;
  display: block; /* No centra, ocupa todo */
  padding: 20px 30px 30px 30px;
  overflow-y: auto;
  box-sizing: border-box;
}

/* ============================================ */
/* CONTENEDOR DEL LISTADO */
/* ============================================ */
.listado-container {
  width: 100%;
  margin: 0;
}

.listado-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  background: rgba(255, 255, 255, 0.95);
  padding: 20px 25px;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.listado-header:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
}

.listado-titulo {
  background: linear-gradient(135deg, #7399c2, #5d7fa3);
  color: white;
  padding: 15px 25px;
  margin: 0;
  border-radius: 10px;
  font-size: 20px;
  font-weight: 600;
  box-shadow: 0 4px 15px rgba(115, 153, 194, 0.3);
}

.btn-crear {
  background: linear-gradient(135deg, #28a745, #20c997);
  color: white;
  padding: 12px 24px;
  border-radius: 10px;
  text-decoration: none;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
  border: none;
  cursor: pointer;
}

.btn-crear:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(40, 167, 69, 0.4);
  color: white;
  text-decoration: none;
  background: linear-gradient(135deg, #20c997, #17a085);
}

.listado-tabla {
  width: 100%;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

/* ============================================ */
/* RESPONSIVE */
/* ============================================ */
@media (max-width: 1024px) {
  .modern-content-area-table {
    padding: 20px 25px;
  }
}

@media (max-width: 768px) {
  .modern-layout-container {
    margin-left: 0;
    width: 100vw;
    margin-top: 140px; /* Espacio extra para navigation rail horizontal */
  }
  
  .modern-content-area-table {
    padding: 15px 15px;
  }
  
  .listado-header {
    flex-direction: column;
    gap: 15px;
    text-align: center;
    padding: 15px 20px;
  }
  
  .listado-titulo {
    font-size: 18px;
    width: 100%;
    padding: 12px 20px;
  }
  
  .btn-crear {
    width: 100%;
    justify-content: center;
    padding: 10px 20px;
  }
}

@media (max-width: 480px) {
  .modern-content-area-table {
    padding: 10px 10px;
  }
  
  .listado-header {
    padding: 12px 15px;
  }
  
  .listado-titulo {
    font-size: 16px;
    padding: 10px 15px;
  }
  
  .btn-crear {
    padding: 8px 16px;
    font-size: 14px;
  }
}
</style>