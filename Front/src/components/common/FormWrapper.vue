<!-- ============================================ -->
<!-- 📁 components/common/FormWrapper.vue -->
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
      
      <!-- Área de contenido - Centrada para formularios -->
      <main class="content-column">
        <div class="formulario-container" :class="formContainerClass">
          <!-- El slot permite insertar cualquier contenido del formulario -->
          <slot></slot>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/common/NavBar.vue';
import Sidebar from '@/components/common/Sidebar.vue';

export default {
  name: 'FormWrapper',
  components: {
    NavBar,
    Sidebar
  },
  props: {
    // Permite ajustar el ancho del formulario según necesidad
    formWidth: {
      type: String,
      default: '400px'
    },
    // Permite formularios más anchos como presupuestos
    isWide: {
      type: Boolean,
      default: false
    },
    // Permite tamaño medio (como clientes) para simetría
    isMedium: {
      type: Boolean,
      default: false
    },
    // Permite más separación del navbar
    extraTopSpace: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    formContainerClass() {
      return {
        'formulario-wide': this.isWide,
        'formulario-medium': this.isMedium,
        'extra-top-space': this.extraTopSpace
      };
    }
  }
}
</script>

<style scoped>
/* ============================================ */
/* LAYOUT CON CSS GRID - MISMO QUE TABLEWRAPPER */
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
/* COLUMNA DEL CONTENIDO - CENTRADA PARA FORMULARIOS */
/* ============================================ */
.content-column {
  /* Segunda columna del grid - toma todo el espacio restante */
  grid-column: 2 !important;
  
  /* CENTRADO PARA FORMULARIOS */
  display: flex !important;
  justify-content: center !important;
  align-items: flex-start !important;
  
  /* Ancho máximo disponible */
  width: 100% !important;
  min-width: 0 !important;
  
  /* Padding para el contenido */
  padding: 40px 30px 25px 30px !important;
  
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
/* CONTENEDOR DEL FORMULARIO - CENTRADO */
/* ============================================ */
.formulario-container {
  border: 3px solid #ddd !important;
  border-radius: 10px !important;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1) !important;
  padding: 20px !important;
  background: white !important;
  
  /* Ancho controlado - centrado perfecto */
  width: 100% !important;
  max-width: 400px !important;
  margin: 0 !important;
  
  /* Sin flex para que no afecte el contenido interno */
  flex-shrink: 0 !important;
}

/* Formularios tamaño medio (como clientes/órdenes) para simetría */
.formulario-container.formulario-medium {
  max-width: 450px !important;
}

/* Formularios más anchos (como presupuestos) */
.formulario-container.formulario-wide {
  max-width: 550px !important;
}

/* Formularios que necesitan más espacio del navbar */
.formulario-container.extra-top-space {
  margin-top: 10px !important;
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
    padding: 30px 20px !important;
  }
}

@media (max-width: 768px) {
  .content-column {
    padding: 20px 15px !important;
  }
  
  .formulario-container {
    max-width: 100% !important;
    padding: 15px !important;
  }
  
  .formulario-container.formulario-wide,
  .formulario-container.formulario-medium {
    max-width: 100% !important;
  }
}

@media (max-width: 480px) {
  .content-column {
    padding: 15px 10px !important;
  }
  
  .formulario-container {
    padding: 10px !important;
  }
}
</style>