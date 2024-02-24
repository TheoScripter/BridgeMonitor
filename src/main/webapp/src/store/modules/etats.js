import EtatService from "@/services/EtatService";

const state = {
    allEtats:[],
    historique:[],

}

const getters = {
    getEtats: state => {
        return state.allEtats;
    },

    getHistoriquePont: state => {
        return state.historique
    }

}

const actions = {
    getallEtats({ commit }){
        EtatService.getEtats()
        .then(response => {
            commit('setEtats', response.data);
      })
      .catch(error => console.error("Erreur lors de la recuperation des etats",error));
    },

    getHistoriquePontById({ commit },id_pont){
        EtatService.getHistoriqueEtat(id_pont)
        .then(response => {
            commit('setHistorique', response.data);
      })
      .catch(error => console.error("Erreur lors de la recuperation del'historique",error));
    }
}


const mutations = {
    setEtats(state, etats){
        state.allEtats = etats;
    },

    setHistorique(state,historique){
        state.historique = historique;
    }
}

export default{
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}