<template>
  <nav class="navbar">
    <div class="navbar-container">
      <!-- Lado izquierdo: Botón de menú y redes sociales -->
      <div class="navbar-left">
        <!-- Botón de menú -->
        <router-link to="/inicio" class="navbar-menu">
          <span class="navbar-menu-icon"></span>
        </router-link>
        
        <!-- Redes sociales -->
        <div class="redes-sociales">
          <a href="#" class="social-link">
            <i class="fab fa-facebook-f"></i>
          </a>
          <a href="#" class="social-link">
            <i class="fab fa-twitter"></i>
          </a>
          <a href="#" class="social-link">
            <i class="fab fa-instagram"></i>
          </a>
        </div>
      </div>

      <!-- Espacio flexible en el medio -->
      <div class="navbar-center"></div>

      <!-- Lado derecho: Usuario y logout -->
      <div class="navbar-right">
        <div class="user-section">
          <span v-if="nombreUsuario" class="user-name">{{ nombreUsuario }}</span>
          <router-link to="/login" class="logout-btn">
            <img src="@/assets/imagenes/apagar.png" alt="Cerrar sesión" class="logout-icon">
          </router-link>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavBar',
  data() {
    return {
      nombreUsuario: ''
    };
  },
  created() {
    this.obtenerUsuario();
  },
  methods: {
    obtenerUsuario() {
      try {
        const usuarioString = localStorage.getItem('usuario');
        if (usuarioString) {
          const usuario = JSON.parse(usuarioString);
          this.nombreUsuario = usuario?.nombre || usuarioString;
        }
      } catch (error) {
        this.nombreUsuario = localStorage.getItem('usuario') || '';
      }
    }
  }
}
</script>

<style scoped>
/* Navbar */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 80px;
  background: linear-gradient(90deg, #2c3e50 0%, #34495e 100%);
  box-shadow: 0 2px 15px rgba(52, 152, 219, 0.15);
  z-index: 1000;
  backdrop-filter: blur(10px);
}

.navbar-container {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 2rem;
  width: 100%;
}

/* Lado izquierdo */
.navbar-left {
  display: flex;
  align-items: center;
  gap: 2rem;
}

/* Centro flexible */
.navbar-center {
  flex: 1;
}

/* Lado derecho */
.navbar-right {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

/* Botón de menú */
.navbar-menu {
  display: flex;
  align-items: center;
  text-decoration: none;
  padding: 0.5rem;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.navbar-menu:hover {
  background-color: rgba(52, 152, 219, 0.15);
  transform: translateY(-1px);
}

.navbar-menu-icon {
  width: 24px;
  height: 24px;
  background-color: #ecf0f1;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Redes sociales */
.redes-sociales {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.social-link {
  color: #ecf0f1;
  font-size: 1.2rem;
  text-decoration: none;
  padding: 0.5rem;
  border-radius: 50%;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
}

.social-link:hover {
  color: #3498db;
  background-color: rgba(52, 152, 219, 0.15);
  transform: translateY(-2px);
}

/* Sección de usuario */
.user-section {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-name {
  color: white;
  font-weight: 500;
  font-size: 0.95rem;
  padding: 0.5rem 1rem;
  background: rgba(52, 152, 219, 0.15);
  border-radius: 20px;
  border: 1px solid rgba(52, 152, 219, 0.3);
  backdrop-filter: blur(10px);
}

.logout-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  border-radius: 50%;
  text-decoration: none;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(52, 152, 219, 0.3);
}

.logout-btn:hover {
  background: linear-gradient(135deg, #2980b9, #1f4e79);
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.4);
}

.logout-icon {
  width: 18px;
  height: 18px;
  filter: brightness(0) invert(1);
}

/* Responsive */
@media (max-width: 768px) {
  .navbar-container {
    padding: 0 1rem;
  }
  
  .navbar-left {
    gap: 1rem;
  }
  
  .redes-sociales {
    gap: 0.5rem;
  }
  
  .social-link {
    width: 35px;
    height: 35px;
    font-size: 1rem;
  }
  
  .user-name {
    font-size: 0.8rem;
    padding: 0.3rem 0.8rem;
  }
  
  .logout-btn {
    width: 35px;
    height: 35px;
  }
}

@media (max-width: 480px) {
  .redes-sociales {
    display: none;
  }
  
  .navbar-container {
    padding: 0 0.5rem;
  }
  
  .navbar-left {
    gap: 0.5rem;
  }
  
  .user-name {
    display: none;
  }
}
</style>