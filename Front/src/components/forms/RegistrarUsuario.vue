<template>
  <div class="formulario-container">
    <h1>Registrar Usuario</h1>
    <form>
      <div class="form-group password-input">
        <label for="nombre">Nombre: </label>
        <input v-model="nuevoUsuario.nombre" type="text" id="nombre" class="form-control" />
      </div>

      <div class="form-group password-input">
        <label for="password">Contraseña: </label>
        <input v-model="nuevoUsuario.contrasenia" type="password" id="password" class="form-control" />
        <img src="@/assets/imagenes/ojo.png" alt="Visor Contraseña" class="password-toggle"
          @click="togglePasswordVisibility" />
      </div>

      <div class="d-grid gap-3">
        <button type="button" @click="registrarUsuario" class="btn btn-success btn-sm gradient-custom-4 text-body">
          Registrar
        </button>
      </div>

      <div v-if="mensajeError" class="error">{{ mensajeError }}</div>

      <!-- Mensaje de confirmación -->
      <div v-if="registroExitoso" class="confirmacion">
        ¡Registro exitoso! Usuario creado.
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      nuevoUsuario: {
        nombre: '',
        contrasenia: ''
      },
      registroExitoso: false,
      mensajeError: ''
    };
  },
  methods: {
    togglePasswordVisibility() {
      const passwordInput = document.getElementById('password');
      if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
      } else {
        passwordInput.type = 'password';
      }
    },
    registrarUsuario() {
      // Valida que se haya ingresado un nombre y una contraseña
      if (!this.nuevoUsuario.nombre || !this.nuevoUsuario.contrasenia) {
        this.mensajeError = 'Nombre y contraseña son campos obligatorios.';
        setTimeout(() => {
          this.mensajeError = ''; 
        }, 3000); 
        return;
      }

      // Lógica para registrar el nuevo usuario
      this.$axios.post('http://localhost:8080/usuarios/crear', this.nuevoUsuario)
        .then(() => {
          // Limpia los campos y mostrar mensaje de éxito
          this.nuevoUsuario.nombre = '';
          this.nuevoUsuario.contrasenia = '';
          this.registroExitoso = true;
          this.mensajeError = '';
          // Oculta el mensaje después de unos segundos
          setTimeout(() => {
            this.registroExitoso = false;
            // Redirige a la página de inicio de sesión después de un registro exitoso
            this.$router.push('/login');
          }, 3000);
        })
        .catch(error => {
          console.error('Error al registrar usuario:', error);
          // Captura y muestra el mensaje de error enviado por el servidor
          if (error.response && error.response.data) {
            this.mensajeError = error.response.data;
          } else {
            this.mensajeError = 'Error al registrar usuario. Por favor, inténtalo de nuevo más tarde.';
          }
        });
    }
  }
};
</script>

<style scoped>
/* Estilos para los gradientes de fondo */
.gradient-custom-3 {
  background: #84fab0;
  background: -webkit-linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
  background: linear-gradient(to right, rgba(132, 250, 176, 0.5), rgba(143, 211, 244, 0.5));
}

.gradient-custom-4 {
  background: linear-gradient(to right, rgba(132, 250, 176, 1), rgba(143, 211, 244, 1));
}

/* Estilos para espaciar y centrar el formulario */
.formulario-container {
  border: 3px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 10px;
  max-width: 1200;
  margin: -250px;
  margin-bottom: -30px;
}

h1 {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  margin-top: 15px;
}

.error {
  color: red;
  font-size: 0.8rem;
  text-align: center;
  margin-top: 10px;
}

/* Estilos específicos para el formulario de registro */
.formulario-container form {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
  margin: 20px;
}

.formulario-container .form-group {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 10px;
}

.formulario-container label {
  width: 80px;
  text-align: right;
  margin-right: 10px;
}

.formulario-container .form-control {
  flex: 1;
  margin-left: 10px;
}

.confirmacion {
  background-color: #d4edda;
  color: #155724;
  padding: 10px;
  border: 1px solid #c3e6cb;
  border-radius: 5px;
  margin-top: 10px;
  text-align: center;
}

.d-grid {
  padding: 30px 70px 10px 120px;
}

/* Estilos para el icono del visor de contraseña */
.password-input {
  position: relative;
}

.password-toggle {
  position: absolute;
  height: 25px;
  width: 25px;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  cursor: pointer;
}
</style>
