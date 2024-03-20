// Importa Vuex y crea tu store
import { createStore } from 'vuex';

export default createStore({
  state: {
    nombreUsuario: null,
  },
  mutations: {
    setNombreUsuario(state, nombreUsuario) {
      state.nombreUsuario = nombreUsuario;
    },
  },
  actions: {
    actualizarNombreUsuario({ commit }, nombreUsuario) {
      commit('setNombreUsuario', nombreUsuario);
    },
  },
  getters: {
    nombreUsuario(state) {
      return state.nombreUsuario;
    },
  },
});
