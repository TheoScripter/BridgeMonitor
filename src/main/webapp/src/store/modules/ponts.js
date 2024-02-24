import PontService from "@/services/PontService";


const state = { 
    allPonts: [],
    Pont: {},
    PontWithCapteurs: [],
    PontWithCapteursNumeriques: [],
    PontWithCapteursAnalogiques: []
}

const getters = {
    getAllPonts: state => {
        return state.allPonts;
    },
    getFullPont: state => {
        return state.Pont;
    },

    getPontWithCapteurs: state => {
        return state.PontWithCapteurs;
    },
    getPontWithCapteursNumeriques: state => {
        return state.PontWithCapteursNumeriques;
    },
    getPontWithCapteursAnalogiques: state => {
        return state.PontWithCapteursAnalogiques;
    }
}

const actions = {
    getPonts({ commit }){
        PontService.getAll().then(response => {
            console.log(response.data);
            commit('setAllPonts',response.data);
        });
    },


    getPontById({commit},id_pont){
        PontService.getPont(id_pont)
            .then(response => {
                commit('setPont', response.data);
            })
            .catch(error => {
                console.error("Erreur lors de la récupération du pont :", error);
            });
    },


    getPontCapteurs({ commit },pontId){
        PontService.getCapteur(pontId)
            .then(response => {
                commit('setCapteursInPont', response.data);
            })
            .catch(error => {
                console.error("Erreur lors de la récupération des capteurs dans le pont :", error);
            });
    },

    getPontCapteursNumeriques({ commit },pontId){
        PontService.getCapteurNumerique(pontId)
            .then(response => {
                commit('setCapteursNumeriquesInPont', response.data);
            })
            .catch(error => {
                console.error("Erreur lors de la récupération des capteurs dans le pont :", error);
            });
    },
    getPontCapteursAnalogiques({ commit },pontId){
        PontService.getCapteurAnalogique(pontId)
            .then(response => {
                commit('setCapteursAnalogiquesInPont', response.data);
            })
            .catch(error => {
                console.error("Erreur lors de la récupération des capteurs dans le pont :", error);
            });
    },

    createPont({ commit },pont){
        PontService.createPont(pont)
        .then(response => {
            commit('newPont',response.data);
        }).catch(error => console.log("erreur creation pont",error));
    },
    updatePont({ commit },pont){
        console.log("updated pont :", pont);
            PontService.update(pont).then( response =>{
                commit('updatePont',response.pont);
            });
    },
    deletePont({ commit },id){
            PontService.delete(id).then( ()=> {
                commit('deletePont',id);
            });
    }

}

const mutations = {
    setAllPonts (state, ponts){
        state.allPonts = ponts;
    },

    setPont(state, pont){
        //console.log("pont reçu",pont);
        state.Pont = pont;
        
    },

    setCapteursInPont(state, pont) {
        //console.log('Données reçues pour les capteurs : ', pont);
        state.PontWithCapteurs = pont;
    },

    setCapteursNumeriquesInPont(state, pont) {
        //console.log('Données reçues pour les capteurs numeriques : ', pont);
        state.PontWithCapteursNumeriques = pont;
    },

    setCapteursAnalogiquesInPont(state, pont) {
        //onsole.log('Données reçues pour les capteurs analogiques : ', pont);
        state.PontWithCapteursAnalogiques = pont;
    },

    newPont(state,pont){
        state.allPonts.push(pont);
    },

    updatePont (state,pont){
        if (pont) {
            const index = state.allPonts.findIndex(p => p.id == pont.id);
            if (index !== -1) {
              state.allPonts.splice(index, 1, pont);
            }
          }
    },

    deletePont(state,id){
        state.allPonts.splice(state.allPonts.findIndex(p => p.id == id),1);
    }
}

export default{
    namespaced:true,
    state,
    getters,
    actions,
    mutations
}