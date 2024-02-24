import Vue from 'vue'
import Vuex from 'vuex'
import regions from "@/store/modules/regions";
import releves from "@/store/modules/releves";
import ponts from "@/store/modules/ponts";
import capteurs from "@/store/modules/capteurs"
import unites from "@/store/modules/unites"
import etats from "@/store/modules/etats"


Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: {
      username: "",
      isAuthenticated: false,
      givenName: "",
      familyName: "",
      email: "",
      adminRole: false,
      idToken: "",
      accessToken: ""
    }
  },
  getters: {
    isAdmin: state => state.user.adminRole,
  },
  mutations: {
    login ( state, payload ) {
      state.user.isAuthenticated = true;
      state.user.idToken = payload.idToken;
      state.user.accessToken = payload.accessToken;
    },
    logout ( state ) {
      state.user.username  = "";
      state.user.isAuthenticated = false;
      state.user.givenName = "";
      state.user.familyName = "";
      state.user.email = "";
      state.user.adminRole = false;
      state.user.idToken = "";
      state.user.accessToken = "";
    },
    setName ( state, payload ) {
      state.user.username = payload.username;
      state.user.givenName = payload.givenName;
      state.user.familyName = payload.familyName;
      state.user.email = payload.email;
      state.user.adminRole = payload.adminRole;
    }
  },
  actions: {
  },
  modules: {
    regions,
    releves,
    ponts,
    capteurs,
    unites,
    etats,
    
  }
})
