<!-- ============================================ -->
<!-- 📁 components/common/ModernFormWrapper.vue -->
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
      
      <!-- Área de contenido centrada -->
      <main class="modern-content-area">
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
import NavigationRail from '@/components/common/NavigationRail.vue';
import CommandPalette from '@/components/common/CommandPalette.vue';
import TopNavigation from '@/components/common/TopNavigation.vue';

export default {
  name: 'ModernFormWrapper',
  components: {
    NavBar,
    NavigationRail,
    CommandPalette,
    TopNavigation
  },

  props: {
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
    // Permite más separación del top
    extraTopSpace: {
      type: Boolean,
      default: false
    }
  },

  data() {
    return {
      commandPaletteOpen: false
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
/* MODERN LAYOUT - OPTIMIZADO PARA RAIL */
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
/* ÁREA DE CONTENIDO CENTRADA */
/* ============================================ */
.modern-content-area {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: flex-start; /* ✅ CAMBIADO: Formulario arriba */
  padding: 15px 40px 15px 40px; /* ✅ MÁS COMPACTO: padding vertical reducido */
  overflow-y: auto;
  box-sizing: border-box;
}

/* ============================================ */
/* CONTENEDOR DEL FORMULARIO */
/* ============================================ */
.formulario-container {
  border: 3px solid #ddd;
  border-radius: 12px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  padding: 18px; /* ✅ REDUCIDO: Menos padding interno */
  background: white;
  width: 100%;
  max-width: 420px;
  margin: 0;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.formulario-container:hover {
  box-shadow: 0 12px 35px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

/* Formularios tamaño medio */
.formulario-container.formulario-medium {
  max-width: 480px;
}

/* Formularios más anchos */
.formulario-container.formulario-wide {
  max-width: 600px;
}

/* Espacio extra del top */
.formulario-container.extra-top-space {
  margin-top: 20px;
}

/* ============================================ */
/* RESPONSIVE */
/* ============================================ */
@media (max-width: 1024px) {
  .modern-content-area {
    padding: 12px 30px; /* ✅ MÁS COMPACTO */
  }
}

@media (max-width: 768px) {
  .modern-layout-container {
    margin-left: 0;
    width: 100vw;
    margin-top: 140px; /* Espacio extra para navigation rail horizontal */
  }
  
  .modern-content-area {
    padding: 8px 15px; /* ✅ MÁS COMPACTO */
  }
  
  .formulario-container {
    max-width: 100% !important;
    padding: 15px; /* ✅ REDUCIDO */
  }
}

@media (max-width: 480px) {
  .modern-content-area {
    padding: 5px 10px; /* ✅ MÁS COMPACTO */
  }
  
  .formulario-container {
    padding: 12px; /* ✅ REDUCIDO */
    border-width: 2px;
  }
}
</style>