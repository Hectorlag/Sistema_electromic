<template>
  <div class="home">
    <div class="container d-flex justify-content-center align-items-center">
      <div class="wrapper fadeInDown">
        <div id="formContent">

           <!-- Logo de la empresa -->
           <div class="logo-container">
            <img src="@/assets/imagenes/paralogin.png" alt="Logo Empresa" class="logo">
          </div>

          <!-- Login  -->
          <form v-on:submit.prevent="login">
            <div class="form-group password-input">
              <input v-model="nombre" type="text" id="nombre" placeholder="Usuario" class="finput-field fadeIn second" />
            </div>

            <div class="form-group password-input">
              <input v-model="contrasenia" type="password" id="password" placeholder="Contraseña" class="form-control" />
              <img src="@/assets/imagenes/ojo.png" alt="Visor Contraseña" class="password-toggle"
                @click="togglePasswordVisibility" />
            </div>

            <input type="submit" class="fadeIn fourth" value="Log In">
          </form>

          <!-- Error en la contraseña o usuario -->
          <div class="alert alert-danger" role="alert" v-if="error">
            {{ error }}
          </div>

          <!-- Link para registrarse -->
          <div class="register-link">
            ¿No tienes una cuenta? <router-link to="/usuarioregistrar">Regístrate aquí</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


  
<script>
export default {
  data() {
    return {
      nombre: '',
      contrasenia: '',
      error: ''
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
    
    login() {
      const credentials = {
        nombre: this.nombre,
        contrasenia: this.contrasenia,
      };

      this.$axios.post('http://localhost:8080/usuarios/login', credentials)
        .then(response => {
          // Verificación exitosa, redirigir a la página principal
          // Almacena el nombre de usuario en localStorage
          localStorage.setItem('usuario', this.nombre); // Almacena el nombre de usuario

          // Redirige a la página principal
          this.$router.push('/inicio');
        })
        .catch(error => {
          // Verificación fallida, mostrar mensaje de error
          this.error = 'Usuario y/o contraseña incorrecta';
        });
    },
  }
};
</script>
  
<style scoped>
html,
body {
  background-color: #56baed;
  font-family: "Poppins", sans-serif;
  height: 80vh;
  margin: 0;
  padding: 0;
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
  margin-left: 250px;
  margin-top: -100px;
}



.wrapper {
  text-align: center;
  max-width: 450px;
  width: 100%;
  margin: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  padding: 20px;
  height: auto; /* Ajusta la altura del formulario automáticamente */
}


#formFooter {
  background-color: #f6f6f6;
  border-top: 1px solid #dce8f1;
  padding: 25px;
  text-align: center;
  -webkit-border-radius: 0 0 10px 10px;
  border-radius: 0 0 10px 10px;
}



/* TABS */

h2.inactive {
  color: #cccccc;
}

h2.active {
  color: #0d0d0d;
  border-bottom: 2px solid #5fbae9;
}



/* FORM TYPOGRAPHY*/

input[type=button],
input[type=submit],
input[type=reset] {
  background-color: #56baed;
  border: none;
  color: white;
  padding: 15px 80px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  text-transform: uppercase;
  font-size: 13px;
  -webkit-box-shadow: 0 10px 30px 0 rgba(95, 186, 233, 0.4);
  box-shadow: 0 10px 30px 0 rgba(95, 186, 233, 0.4);
  -webkit-border-radius: 5px 5px 5px 5px;
  border-radius: 5px 5px 5px 5px;
  margin: 5px 20px 40px 20px;
  -webkit-transition: all 0.3s ease-in-out;
  -moz-transition: all 0.3s ease-in-out;
  -ms-transition: all 0.3s ease-in-out;
  -o-transition: all 0.3s ease-in-out;
  transition: all 0.3s ease-in-out;
}

input[type=button]:hover,
input[type=submit]:hover,
input[type=reset]:hover {
  background-color: #39ace7;
}

input[type=button]:active,
input[type=submit]:active,
input[type=reset]:active {
  -moz-transform: scale(0.95);
  -webkit-transform: scale(0.95);
  -o-transform: scale(0.95);
  -ms-transform: scale(0.95);
  transform: scale(0.95);
}

input[type=text] {
  background-color: #f6f6f6;
  border: none;
  color: #0d0d0d;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 5px;
  width: 85%;
  border: 2px solid #f6f6f6;
  -webkit-transition: all 0.5s ease-in-out;
  -moz-transition: all 0.5s ease-in-out;
  -ms-transition: all 0.5s ease-in-out;
  -o-transition: all 0.5s ease-in-out;
  transition: all 0.5s ease-in-out;
  -webkit-border-radius: 5px 5px 5px 5px;
  border-radius: 5px 5px 5px 5px;
}

input[type=text]:focus {
  background-color: #fff;
  border-bottom: 2px solid #5fbae9;
}

input[type=text]:placeholder {
  color: #cccccc;
}



/* ANIMATIONS */

/* Simple CSS3 Fade-in-down Animation */
.fadeInDown {
  -webkit-animation-name: fadeInDown;
  animation-name: fadeInDown;
  -webkit-animation-duration: 1s;
  animation-duration: 1s;
  -webkit-animation-fill-mode: both;
  animation-fill-mode: both;
}

@-webkit-keyframes fadeInDown {
  0% {
    opacity: 0;
    -webkit-transform: translate3d(0, -100%, 0);
    transform: translate3d(0, -100%, 0);
  }

  100% {
    opacity: 1;
    -webkit-transform: none;
    transform: none;
  }
}

@keyframes fadeInDown {
  0% {
    opacity: 0;
    -webkit-transform: translate3d(0, -100%, 0);
    transform: translate3d(0, -100%, 0);
  }

  100% {
    opacity: 1;
    -webkit-transform: none;
    transform: none;
  }
}

/* Simple CSS3 Fade-in Animation */
@-webkit-keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

@-moz-keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

.fadeIn {
  opacity: 0;
  -webkit-animation: fadeIn ease-in 1;
  -moz-animation: fadeIn ease-in 1;
  animation: fadeIn ease-in 1;

  -webkit-animation-fill-mode: forwards;
  -moz-animation-fill-mode: forwards;
  animation-fill-mode: forwards;

  -webkit-animation-duration: 1s;
  -moz-animation-duration: 1s;
  animation-duration: 1s;
}

.fadeIn.first {
  -webkit-animation-delay: 0.4s;
  -moz-animation-delay: 0.4s;
  animation-delay: 0.4s;
}

.fadeIn.second {
  -webkit-animation-delay: 0.6s;
  -moz-animation-delay: 0.6s;
  animation-delay: 0.6s;
}

.fadeIn.third {
  -webkit-animation-delay: 0.8s;
  -moz-animation-delay: 0.8s;
  animation-delay: 0.8s;
}

.fadeIn.fourth {
  -webkit-animation-delay: 1s;
  -moz-animation-delay: 1s;
  animation-delay: 1s;
}

/* Simple CSS3 Fade-in Animation */
.underlineHover:after {
  display: block;
  left: 0;
  bottom: -10px;
  width: 0;
  height: 2px;
  background-color: #56baed;
  content: "";
  transition: width 0.2s;
}

.underlineHover:hover {
  color: #0d0d0d;
}

.underlineHover:hover:after {
  width: 100%;
}



/* OTHERS */

*:focus {
  outline: none;
}

#icon {
  width: 60%;
}

.label-input {
  display: inline-block;
  width: 100px;
  /* ajusta el ancho según sea necesario */
  text-align: right;
  /* alinea el texto a la derecha */
  margin-right: 10px;
  /* agrega un espacio entre el label y el input */
}

.input-field {
  width: calc(100% - 110px);
  /* calcula el ancho del input restando el ancho del label */
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
  right: 50px;
  transform: translateY(-50%);
  cursor: pointer;
}
.form-group {
  margin-bottom: 30px; /* Ajusta la distancia entre los grupos de entrada */
}

.form-control {
  margin-left: 35px;
  width: 340px;
  height: 50px;

}

#password {
  text-align: center;
}

.input-field {
  width: calc(100% - 160px); /* Ajustamos el ancho del campo de contraseña */
}

.form-control {
  width: calc(100% - 75px); /* Ajustamos el ancho del campo de contraseña visual */
}

.logo-container {
  text-align: center; /* Centra la imagen horizontalmente */
  margin-bottom: 8px; /* Ajusta el espacio entre la imagen y el formulario */
}

.logo {
  width: 130px; /* Ajusta el ancho de la imagen según sea necesario */
  height: auto; /* Ajusta la altura automáticamente para mantener la proporción */
}



</style>



