<template>
  <div>
    <NavBar />
  </div>
  <div class="formulario-container">
    <h1>Registrar orden de trabajo</h1>
    <form>
      <div class="form-group">
        <label class="form-label" for="presupuesto">Presupuesto:</label>
        <div class="input-container">
          <select v-model="nuevaOrden.presupuesto.id_presupuesto" class="form-control">
            <option v-for="presupuesto in presupuestos" :key="presupuesto.id_presupuesto"
              :value="presupuesto.id_presupuesto">
              N° Pres: {{ presupuesto.id_presupuesto }} | | Fecha: {{ presupuesto.fecha }}
            </option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="form-label" for="estado">Estado:</label>
        <div class="input-container">
          <select v-model="nuevaOrden.estado" id="estado" class="form-control">
            <option value="En reparación">En reparación</option>
            <option value="Para ingresar">Para ingresar</option>
          </select>
        </div>
      </div>


      <div class="d-grid gap-2">
        <button type="button" @click="registrarOrden" class="btn btn-success btn-sm gradient-custom-4 text-body">
          Registrar
        </button>
      </div>
      <!-- Mensaje de error -->
      <div v-if="mensajeError" class="error">{{ mensajeError }}</div>

      <!-- Mensaje de confirmación -->
      <div v-if="registroExitoso" class="confirmacion">
        ¡Registro exitoso!
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
      nuevaOrden: {
        estado: '',
        presupuesto: { id_presupuesto: "" },
      },

      registroExitoso: false,
      presupuestos: [],
      mensajeError: '',
    };
  },

  methods: {

    obtenerPresupuestos() {
      this.$axios.get('http://localhost:8080/presupuestos/traeridyfecha')
        .then(response => {
          console.log('Respuesta de la API:', response.data);
          this.presupuestos = response.data;
        })
        .catch(error => {
          console.error('Error al obtener presupuestos:', error);
        });
    },

    obtenerOrdenesTrabajo() {
      console.log("Iniciando obtenerOrdenesTrabajo");
      this.$axios.get("http://localhost:8080/ordenes/traer")
        .then((response) => {
          console.log("Respuesta de la API:", response.data);
          this.ordenesTrabajo = response.data;
          localStorage.setItem("ordenesTrabajo", JSON.stringify(response.data));
        })
        .catch((error) => {
          console.error("Error al obtener órdenes de trabajo:", error);
        });
    },

    registrarOrden() {
      console.log('Datos del presupuesto a registrar:', this.nuevaOrden);

      // Si hay algún error, detiene el proceso de registro
      if (!this.nuevaOrden.estado || !this.nuevaOrden.presupuesto) {
        console.error('Todos los campos son obligatorios.');
        this.mensajeError = 'Todos los campos son obligatorios.';
        setTimeout(() => {
          this.mensajeError = '';
        }, 3000);
        return;
      }

      // Lógica para registrar una nueva orden de trabajo
      this.$axios.post('http://localhost:8080/ordenes/crear', this.nuevaOrden)
        .then(() => {
          this.obtenerOrdenesTrabajo();
          // Reinicia el formulario
          this.nuevaOrden = { estado: '', presupuesto: { id_presupuesto: "" } };
          // Muestra el mensaje de confirmación
          this.registroExitoso = true;
          // Limpia el mensaje después de unos segundos
          setTimeout(() => {
            this.registroExitoso = false;
          }, 3000);
        })
        .catch(error => {
          console.error('Error al agregar la orden de trabajo:', error);
        });
    }
  },

  created() {
    this.obtenerPresupuestos();
  },
}
</script>

<style scoped>
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
  padding: 20px;
  max-width: 600px;
  margin: 110px -240px;
}

h1 {
  text-align: center;
  font-size: 20px;
  margin-bottom: 50px;
  margin-top: 3px;
}

.input-container {
  position: relative;
  width: 60%;
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
  margin-bottom: 40px;
}

.formulario-container label {
  width: 70px;
  text-align: right;
  margin-right: 20px;
}

.formulario-container .form-control {
  flex: 1;
  margin-left: 15px;
}

.confirmacion {
  background-color: #d4edda;
  color: #155724;
  padding: 10px;
  border: 1px solid #c3e6cb;
  border-radius: 5px;
  margin-top: -12px;
  text-align: center;
}

.d-grid {
  padding: 10px 60px 15px 80px;
  margin-bottom: 10px;
}

.error {
  color: red;
  font-size: 0.8rem;
  text-align: center;
  margin-top: 3px;
}
</style>
