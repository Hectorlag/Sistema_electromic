<template>
  <div>
    <NavBar />
  </div>

  <div class="formulario-container">
    <h1>Registrar cliente</h1>
    <form>
      <div class="form-group">
        <label for="nombre">Nombre: </label>
        <input v-model="nuevoCliente.nombre[0]" type="text" id="nombre" class="form-control" />
      </div>

      <div class="form-group">
        <label for="apellido">Apellido: </label>
        <input v-model="nuevoCliente.nombre[1]" type="text" id="apellido" class="form-control" />
      </div>

      <div class="form-group">
        <label for="direccion">Dirección: </label>
        <input v-model="nuevoCliente.direccion" type="text" id="direccion" class="form-control" />
      </div>

      <div class="form-group">
        <label for="cuil">Dni: </label>
        <input v-model="nuevoCliente.cuil" type="text" id="cuil" class="form-control" />
      </div>

      <div class="form-group">
        <label for="email">Email: </label>
        <input v-model="nuevoCliente.email" type="email" id="email" class="form-control" />
      </div>

      <div class="form-group">
        <label for="telefono">Teléfono: </label>
        <input v-model="nuevoCliente.telefono[0]" type="text" id="telefono" class="form-control" />
      </div>

      <div class="form-group">
        <label for="otrotelefono">Otro Teléfono: </label>
        <input v-model="nuevoCliente.telefono[1]" type="text" id="otrotelefono" class="form-control" />
      </div>

      <div class="d-grid gap-3">
        <button type="button" @click="registrarCliente" class="btn btn-success btn-sm gradient-custom-4 text-body">
          Registrar
        </button>
      </div>

      <div v-if="mensajeError" class="error">{{ mensajeError }}</div>

      <!-- Mensaje de confirmación -->
      <div v-if="registroExitoso" class="confirmacion">
        ¡Registro exitoso! ha sido registrado.
      </div>
    </form>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue';

export default {
  components: {
    NavBar
  },

  data() {
    return {
      nuevoCliente: { nombre: ['', ''], direccion: '', cuil: '', email: '', telefono: ['', ''] },
      registroExitoso: false,
      clientes: [],
      mensajeError: '',
    };
  },

  methods: {

    obtenerClientes() {
      this.$axios.get('http://localhost:8080/clientes/traer')
        .then(response => {
          console.log('Respuesta de la API:', response.data);
          this.clientes = response.data;

          // Almacena la lista de clientes en el almacenamiento local
          localStorage.setItem('clientes', JSON.stringify(response.data));
        })
        .catch(error => {
          console.error('Error al obtener clientes:', error);
        });
    },

    // Método para validar el formato del correo electrónico
    validarFormatoEmail(email) {
      // Si el campo de correo electrónico está vacío, se considera válido
      if (!email) {
        return true;
      }

      // Expresión regular para validar el formato del correo electrónico
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return regex.test(email);
    },

    registrarCliente() {
      // Valida formato del correo electrónico
      if (!this.validarFormatoEmail(this.nuevoCliente.email)) {
        this.mensajeError = 'El formato del correo electrónico no es válido.';
        setTimeout(() => {
          this.mensajeError = '';
        }, 3000);
        return;
      }

      console.log('Método registrarCliente ejecutado');
      // Aseguro de que el campo de teléfono sea siempre un array
      if (!Array.isArray(this.nuevoCliente.telefono)) {
        this.nuevoCliente.telefono = this.nuevoCliente.telefono ? [this.nuevoCliente.telefono] : [];
      }

      // Aseguro de que el campo nombre sea siempre un array
      if (!Array.isArray(this.nuevoCliente.nombre)) {
        this.nuevoCliente.nombre = this.nuevoCliente.nombre ? [this.nuevoCliente.nombre] : [];
      }

      // Verifica que los campos direccion y cuil no estén vacíos
      if (!this.nuevoCliente.direccion || !this.nuevoCliente.nombre[0] || !this.nuevoCliente.nombre[1] || !this.nuevoCliente.telefono[0] || !this.nuevoCliente.cuil) {
        console.error('Dirección, teléfono y nombre son campos obligatorios.');
        this.mensajeError = 'Nombre, apellido, dirección, dni y teléfono son campos obligatorios.';
        setTimeout(() => {
          this.mensajeError = '';
        }, 3000);
        return;
      }

      // Validación de teléfono[1]
      if (!this.nuevoCliente.telefono[1]) {
        // Si telefono[1] no tiene valor, eliminarlo del array
        delete this.nuevoCliente.telefono[1];
      }

      // Validar formato del correo electrónico
      if (!this.validarFormatoEmail(this.nuevoCliente.email)) {
        this.mensajeError = 'El formato del correo electrónico no es válido.';
        return;
      }

      // Lógica para registrar un nuevo usuario
      this.$axios.post('http://localhost:8080/clientes/crear', this.nuevoCliente)
        .then(() => {
          this.obtenerClientes();
          console.log('Lista de clientes después del registro:', this.clientes);
          this.nuevoCliente = { nombre: ['', ''], direccion: '', cuil: '', email: '', telefono: ['', ''] },
            // Muestra el mensaje de confirmación
            this.registroExitoso = true;
          this.mensajeError = false;
          // Limpia el mensaje después de unos segundos
          setTimeout(() => {
            this.registroExitoso = false;
          }, 3000);
        })
        // Manejar errores aquí
        .catch(error => {
          console.error('Error al agregar el cliente:', error);
          console.log('Respuesta del servidor:', error.response);
          console.log('Data del error:', error.data);
          console.log('Datos enviados desde Vue.js:', this.nuevoCliente);
          console.log('Configuración de la solicitud:', error.config);
        });
    },

  },
  created() {
    this.obtenerClientes();
  },
}
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

/* Estilos para el contenedor del formulario */
.formulario-container {
  border: 3px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  max-width: 400px;
  margin: 100px -200px;
}

h1 {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  margin-top: 8px;
}

.error {
  color: red;
  font-size: 0.8rem;
  text-align: center;
  margin-top: -15px;
}

/* Estilos específicos para el formulario de registro */
.formulario-container form {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
}

.formulario-container .form-group {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 10px;
}

.formulario-container label {
  width: 50px;
  text-align: right;
  margin-right: 30px;
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
  margin-top: -10px;
  text-align: center;
}

.d-grid {
  padding: 8px 60px 15px 60px;
  margin-bottom: 10px;
}
</style>
