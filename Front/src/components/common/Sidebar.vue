<template>
  <div class="sidebar">
    <!-- Usuario -->
    <div class="usuario-section">
      <div class="icono-usuario">
        <i class="bi bi-person-fill"></i>
      </div>
      <div class="nombre-usuario">
        {{ nombreUsuario || 'hectorla' }}
      </div>
    </div>

    <!-- Navegación -->
    <nav class="sidebar-nav">
      <!-- Clientes -->
      <div class="nav-group">
        <a href="#" @click="toggleSection('Clientes')" class="nav-link">
          <i class="bi bi-people-fill"></i>
          <span>Clientes</span>
          <i class="bi bi-chevron-down chevron" :class="{ 'rotated': secciones.Clientes }"></i>
        </a>
        <div class="sub-menu" v-show="secciones.Clientes">
          <router-link to="/clienteregistrar" class="sub-link">
            <i class="bi bi-person-plus-fill"></i>
            <span>Registrar</span>
          </router-link>
          <router-link to="/clienteslistar" class="sub-link">
            <i class="bi bi-list-ul"></i>
            <span>Listar</span>
          </router-link>
          <router-link to="/clientebuscar" class="sub-link">
            <i class="bi bi-search"></i>
            <span>Buscar</span>
          </router-link>
        </div>
      </div>

      <!-- Productos -->
      <div class="nav-group">
        <a href="#" @click="toggleSection('Productos')" class="nav-link">
          <i class="bi bi-box-fill"></i>
          <span>Productos</span>
          <i class="bi bi-chevron-down chevron" :class="{ 'rotated': secciones.Productos }"></i>
        </a>
        <div class="sub-menu" v-show="secciones.Productos">
          <router-link to="/productoregistrar" class="sub-link">
            <i class="bi bi-plus-circle-fill"></i>
            <span>Registrar</span>
          </router-link>
          <router-link to="/productoslistar" class="sub-link">
            <i class="bi bi-list-ul"></i>
            <span>Listar</span>
          </router-link>
          <router-link to="/productobuscar" class="sub-link">
            <i class="bi bi-search"></i>
            <span>Buscar</span>
          </router-link>
        </div>
      </div>

      <!-- Presupuestos -->
      <div class="nav-group">
        <a href="#" @click="toggleSection('Presupuestos')" class="nav-link">
          <i class="bi bi-calculator-fill"></i>
          <span>Presupuestos</span>
          <i class="bi bi-chevron-down chevron" :class="{ 'rotated': secciones.Presupuestos }"></i>
        </a>
        <div class="sub-menu" v-show="secciones.Presupuestos">
          <router-link to="/presupuestoregistrar" class="sub-link">
            <i class="bi bi-plus-circle-fill"></i>
            <span>Registrar</span>
          </router-link>
          <router-link to="/presupuestoslistar" class="sub-link">
            <i class="bi bi-list-ul"></i>
            <span>Listar</span>
          </router-link>
          <router-link to="/presupuestobuscar" class="sub-link">
            <i class="bi bi-search"></i>
            <span>Buscar</span>
          </router-link>
        </div>
      </div>

      <!-- Órdenes de Trabajo -->
      <div class="nav-group">
        <a href="#" @click="toggleSection('OrdenDeTrabajo')" class="nav-link">
          <i class="bi bi-clipboard-check-fill"></i>
          <span>Órdenes de Trabajo</span>
          <i class="bi bi-chevron-down chevron" :class="{ 'rotated': secciones.OrdenDeTrabajo }"></i>
        </a>
        <div class="sub-menu" v-show="secciones.OrdenDeTrabajo">
          <router-link to="/ordenregistrar" class="sub-link">
            <i class="bi bi-plus-circle-fill"></i>
            <span>Registrar</span>
          </router-link>
          <router-link to="/ordeneslistar" class="sub-link">
            <i class="bi bi-list-ul"></i>
            <span>Listar</span>
          </router-link>
          <router-link to="/ordenbuscar" class="sub-link">
            <i class="bi bi-search"></i>
            <span>Buscar</span>
          </router-link>
        </div>
      </div>

      <!-- Mi Cuenta -->
      <div class="nav-group">
        <a href="#" @click="toggleSection('micuenta')" class="nav-link">
          <i class="bi bi-gear-fill"></i>
          <span>Mi Cuenta</span>
          <i class="bi bi-chevron-down chevron" :class="{ 'rotated': secciones.micuenta }"></i>
        </a>
        <div class="sub-menu" v-show="secciones.micuenta">
          <router-link to="/perfilcuenta" class="sub-link">
            <i class="bi bi-person-gear"></i>
            <span>Perfil</span>
          </router-link>
          <router-link to="/login" class="sub-link">
            <i class="bi bi-box-arrow-right"></i>
            <span>Cerrar Sesión</span>
          </router-link>
        </div>
      </div>
    </nav>
  </div>
</template>

<script>
export default {
  name: 'Sidebar',
  data() {
    return {
      secciones: {
        Clientes: false,
        Productos: false,
        Presupuestos: false,
        OrdenDeTrabajo: false,
        micuenta: false,
      },
      nombreUsuario: ''
    };
  },
  created() {
    this.obtenerUsuario();
  },
  methods: {
    toggleSection(section) {
      Object.keys(this.secciones).forEach(key => {
        if (key !== section) {
          this.secciones[key] = false;
        }
      });
      this.secciones[section] = !this.secciones[section];
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
    }
  }
}
</script>

<style scoped>
.sidebar {
  width: 250px;
  min-height: calc(100vh - 80px);
  background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);
  box-shadow: 2px 0 15px rgba(52, 152, 219, 0.15);
  overflow-y: auto;
  margin: 0;
  padding: 0;
  flex-shrink: 0;
}

/* Usuario */
.usuario-section {
  padding: 2rem 1rem;
  text-align: center;
  border-bottom: 1px solid rgba(52, 152, 219, 0.2);
  background: rgba(52, 152, 219, 0.05);
}

.icono-usuario {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #3498db, #2980b9);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem auto;
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
}

.icono-usuario i {
  color: white;
  font-size: 1.5rem;
}

.nombre-usuario {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
  border: 1px solid rgba(52, 152, 219, 0.3);
  backdrop-filter: blur(10px);
}

/* Navegación */
.sidebar-nav {
  padding: 1rem 0;
}

.nav-group {
  margin-bottom: 0.5rem;
}

.nav-link {
  display: flex;
  align-items: center;
  color: #ecf0f1;
  padding: 1rem 1.5rem;
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
}

.nav-link:hover {
  background: rgba(52, 152, 219, 0.15);
  color: #3498db;
  padding-left: 1.8rem;
}

.nav-link i:first-child {
  margin-right: 1rem;
  width: 20px;
  text-align: center;
  font-size: 1.1rem;
}

.nav-link span {
  flex: 1;
  font-weight: 500;
}

.chevron {
  transition: transform 0.3s ease;
  font-size: 0.8rem;
  margin-left: auto;
}

.chevron.rotated {
  transform: rotate(180deg);
}

/* Submenú */
.sub-menu {
  background: rgba(0, 0, 0, 0.1);
  border-left: 3px solid #3498db;
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    max-height: 0;
  }
  to {
    opacity: 1;
    max-height: 300px;
  }
}

.sub-link {
  display: flex;
  align-items: center;
  color: #bdc3c7;
  padding: 0.75rem 1.5rem 0.75rem 2.5rem;
  text-decoration: none;
  transition: all 0.3s ease;
  border-bottom: 1px solid rgba(52, 152, 219, 0.05);
}

.sub-link i {
  margin-right: 0.8rem;
  width: 16px;
  text-align: center;
  font-size: 0.9rem;
  color: #7f8c8d;
}

.sub-link span {
  flex: 1;
}

.sub-link:hover {
  color: #3498db;
  background: rgba(52, 152, 219, 0.1);
  padding-left: 2.7rem;
}

.sub-link:hover i {
  color: #3498db;
}

.sub-link.router-link-active {
  color: #3498db;
  background: rgba(52, 152, 219, 0.2);
  font-weight: 500;
}

.sub-link.router-link-active i {
  color: #3498db;
}

/* Scrollbar */
.sidebar::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-track {
  background: #34495e;
}

.sidebar::-webkit-scrollbar-thumb {
  background: #3498db;
  border-radius: 3px;
}

/* Responsive */
@media (max-width: 768px) {
  .sidebar {
    width: 200px;
  }
}
</style>