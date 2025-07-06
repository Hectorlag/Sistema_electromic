<template>
  <div class="login-container">
    <!-- Fondo animado -->
    <div class="background-animation">
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
        <div class="shape shape-4"></div>
        <div class="shape shape-5"></div>
      </div>
    </div>

    <div class="login-wrapper">
      <div class="login-card" :class="{ 'shake': hasError }">
        <!-- Header con logo -->
        <div class="login-header">
          <div class="logo-container">
            <div class="logo-circle">
              <i class="bi bi-gear-fill logo-icon"></i>
            </div>
          </div>
          <h1 class="login-title">Bienvenido</h1>
          <p class="login-subtitle">Accede a tu sistema de gestión</p>
        </div>

        <!-- Formulario -->
        <form @submit.prevent="login" class="login-form">
          <!-- Campo Usuario -->
          <div class="input-group" :class="{ 'focus': focusStates.usuario, 'error': fieldErrors.usuario }">
            <div class="input-wrapper">
              <i class="bi bi-person-fill input-icon"></i>
              <input 
                v-model="nombre" 
                type="text" 
                placeholder="Nombre de usuario" 
                class="form-input"
                @focus="setFocus('usuario', true)"
                @blur="setFocus('usuario', false)"
                @input="clearFieldError('usuario')"
                required
              />
              <div class="input-border"></div>
            </div>
          </div>

          <!-- Campo Contraseña -->
          <div class="input-group" :class="{ 'focus': focusStates.password, 'error': fieldErrors.password }">
            <div class="input-wrapper">
              <i class="bi bi-lock-fill input-icon"></i>
              <input 
                v-model="contrasenia" 
                :type="showPassword ? 'text' : 'password'" 
                placeholder="Contraseña" 
                class="form-input"
                @focus="setFocus('password', true)"
                @blur="setFocus('password', false)"
                @input="clearFieldError('password')"
                required
              />
              <button 
                type="button"
                class="password-toggle"
                @click="togglePasswordVisibility"
                tabindex="-1"
              >
                <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
              </button>
              <div class="input-border"></div>
            </div>
          </div>

          <!-- Opción recordar sesión -->
          <div class="form-options">
            <label class="checkbox-container">
              <input type="checkbox" v-model="rememberMe">
              <span class="checkmark"></span>
              <span class="checkbox-text">Recordar sesión</span>
            </label>
            <a href="#" class="forgot-password">¿Olvidaste tu contraseña?</a>
          </div>

          <!-- Botón de login -->
          <button 
            type="submit" 
            class="login-button"
            :class="{ 'loading': isLoading }"
            :disabled="isLoading"
          >
            <span v-if="!isLoading" class="button-content">
              <i class="bi bi-arrow-right-circle"></i>
              Iniciar Sesión
            </span>
            <div v-else class="loading-spinner">
              <div class="spinner"></div>
              <span>Verificando...</span>
            </div>
          </button>
        </form>

        <!-- Mensaje de error -->
        <transition name="error-slide">
          <div v-if="error" class="error-message">
            <i class="bi bi-exclamation-triangle-fill"></i>
            <span>{{ error }}</span>
          </div>
        </transition>

        <!-- Footer -->
        <div class="login-footer">
          <p class="register-text">
            ¿No tienes una cuenta? 
            <router-link to="/registrarusuario" class="register-link">
              Regístrate aquí
            </router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      nombre: '',
      contrasenia: '',
      error: '',
      showPassword: false,
      isLoading: false,
      hasError: false,
      rememberMe: false,
      focusStates: {
        usuario: false,
        password: false
      },
      fieldErrors: {
        usuario: false,
        password: false
      }
    };
  },
  
  methods: {
    setFocus(field, state) {
      this.focusStates[field] = state;
    },

    clearFieldError(field) {
      this.fieldErrors[field] = false;
      this.error = '';
      this.hasError = false;
    },

    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
    
    async login() {
      // Limpiar errores anteriores
      this.error = '';
      this.hasError = false;
      this.fieldErrors = { usuario: false, password: false };
      
      // Validación básica
      if (!this.nombre.trim()) {
        this.fieldErrors.usuario = true;
        this.error = 'Por favor ingresa tu nombre de usuario';
        this.triggerErrorAnimation();
        return;
      }
      
      if (!this.contrasenia.trim()) {
        this.fieldErrors.password = true;
        this.error = 'Por favor ingresa tu contraseña';
        this.triggerErrorAnimation();
        return;
      }

      this.isLoading = true;
      
      const credentials = {
        nombre: this.nombre.trim(),
        contrasenia: this.contrasenia,
      };

      try {
        const response = await this.$axios.post('http://localhost:8080/usuarios/login', credentials);
        
        // Éxito en el login
        if (this.rememberMe) {
          localStorage.setItem('rememberUser', this.nombre);
        }
        localStorage.setItem('usuario', this.nombre);
        
        // Pequeña pausa para mostrar el estado de éxito
        await new Promise(resolve => setTimeout(resolve, 500));
        
        this.$router.push('/inicio');
        
      } catch (error) {
        // Error en el login
        this.error = 'Usuario y/o contraseña incorrectos';
        this.fieldErrors.usuario = true;
        this.fieldErrors.password = true;
        this.triggerErrorAnimation();
      } finally {
        this.isLoading = false;
      }
    },

    triggerErrorAnimation() {
      this.hasError = true;
      setTimeout(() => {
        this.hasError = false;
      }, 600);
    }
  },

  mounted() {
    // Cargar usuario recordado si existe
    const rememberedUser = localStorage.getItem('rememberUser');
    if (rememberedUser) {
      this.nombre = rememberedUser;
      this.rememberMe = true;
    }
  }
};
</script>

<style scoped>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.login-container {
  min-height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  position: fixed;
  top: 0;
  left: 0;
  overflow: hidden;
  z-index: 9999;
}

/* Fondo animado */
.background-animation {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.shape {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
}

.shape-1 {
  width: 80px;
  height: 80px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.shape-2 {
  width: 60px;
  height: 60px;
  top: 20%;
  right: 10%;
  animation-delay: -2s;
}

.shape-3 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  left: 20%;
  animation-delay: -4s;
}

.shape-4 {
  width: 120px;
  height: 120px;
  top: 60%;
  right: 20%;
  animation-delay: -1s;
}

.shape-5 {
  width: 40px;
  height: 40px;
  bottom: 10%;
  right: 30%;
  animation-delay: -3s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
    opacity: 0.5;
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
    opacity: 0.8;
  }
}

/* Contenedor principal del login */
.login-wrapper {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 420px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 32px;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  width: 100%;
  max-width: 420px;
  min-width: 320px;
}

.login-card.shake {
  animation: shake 0.6s ease-in-out;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  10%, 30%, 50%, 70%, 90% { transform: translateX(-5px); }
  20%, 40%, 60%, 80% { transform: translateX(5px); }
}

/* Header */
.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.logo-container {
  margin-bottom: 16px;
}

.logo-circle {
  width: 70px;
  height: 70px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.3);
}

.logo-icon {
  font-size: 28px;
  color: white;
  animation: rotate 8s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.login-title {
  font-size: 28px;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 6px 0;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.login-subtitle {
  font-size: 15px;
  color: #718096;
  margin: 0;
  font-weight: 400;
}

/* Formulario */
.login-form {
  margin-bottom: 20px;
  width: 100%;
}

.input-group {
  margin-bottom: 20px;
  position: relative;
  width: 100%;
}

.input-wrapper {
  position: relative;
  width: 100%;
  display: block;
}

.form-input {
  width: 100%;
  padding: 14px 14px 14px 48px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 16px;
  background: #f7fafc;
  color: #2d3748;
  transition: all 0.3s ease;
  outline: none;
  box-sizing: border-box;
}

.form-input:focus {
  background: white;
  border-color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.15);
}

.input-group.focus .form-input {
  background: white;
  border-color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.15);
}

.input-group.error .form-input {
  border-color: #e53e3e;
  background: #fed7d7;
}

.input-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #a0aec0;
  font-size: 18px;
  z-index: 2;
  transition: color 0.3s ease;
}

.input-group.focus .input-icon {
  color: #667eea;
}

.input-group.error .input-icon {
  color: #e53e3e;
}

.password-toggle {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #a0aec0;
  cursor: pointer;
  font-size: 18px;
  transition: color 0.3s ease;
  z-index: 2;
}

.password-toggle:hover {
  color: #667eea;
}

.input-border {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.input-group.focus .input-border {
  width: 100%;
}

/* Opciones del formulario */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  font-size: 14px;
  width: 100%;
}

.checkbox-container {
  display: flex;
  align-items: center;
  cursor: pointer;
  user-select: none;
}

.checkbox-container input {
  display: none;
}

.checkmark {
  width: 18px;
  height: 18px;
  border: 2px solid #e2e8f0;
  border-radius: 4px;
  margin-right: 8px;
  position: relative;
  transition: all 0.3s ease;
}

.checkbox-container input:checked + .checkmark {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-color: #667eea;
}

.checkbox-container input:checked + .checkmark::after {
  content: '';
  position: absolute;
  left: 5px;
  top: 2px;
  width: 4px;
  height: 8px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.checkbox-text {
  color: #4a5568;
}

.forgot-password {
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  color: #5a6fd8;
  text-decoration: underline;
}

/* Botón de login */
.login-button {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  box-sizing: border-box;
  display: block;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.login-button:active {
  transform: translateY(0);
}

.login-button:disabled {
  cursor: not-allowed;
  opacity: 0.8;
}

.button-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.loading-spinner {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Mensaje de error */
.error-message {
  background: rgba(229, 62, 62, 0.1);
  border: 1px solid rgba(229, 62, 62, 0.3);
  color: #e53e3e;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 500;
}

.error-slide-enter-active,
.error-slide-leave-active {
  transition: all 0.3s ease;
}

.error-slide-enter-from,
.error-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Footer */
.login-footer {
  text-align: center;
  padding-top: 12px;
  border-top: 1px solid #e2e8f0;
  margin-top: 8px;
}

.register-text {
  color: #718096;
  font-size: 13px;
  margin: 0;
}

.register-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s ease;
}

.register-link:hover {
  color: #5a6fd8;
  text-decoration: underline;
}

/* Responsive */
@media (max-width: 768px) {
  .login-container {
    padding: 16px;
  }
  
  .login-card {
    padding: 28px 20px;
  }
  
  .login-title {
    font-size: 28px;
  }
  
  .logo-circle {
    width: 60px;
    height: 60px;
  }
  
  .logo-icon {
    font-size: 24px;
  }
  
  .form-options {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
}

@media (max-width: 480px) {
  .login-card {
    padding: 24px 16px;
  }
  
  .login-title {
    font-size: 24px;
  }
  
  .form-input {
    padding: 12px 12px 12px 44px;
    font-size: 16px; /* Evita zoom en iOS */
  }
  
  .login-button {
    padding: 12px;
  }
}
</style>