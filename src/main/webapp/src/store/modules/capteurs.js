import CapteurService from "@/services/CapteurService";

const state={
    capteur:[],
    capteursInPont: [],
    capteurReleves: {},
}

const getters = {
    getCapteur: state => {
        return state.capteur;
    },
    getCapteursInPont: state => {
        return state.capteursInPont;
    },
   /* getReleves: (state) => (capteurId) => {
        const localData = localStorage.getItem(`capteurReleves_${capteurId}`);
        if (localData) {return JSON.parse(localData);}
        return state.capteurReleves[capteurId] || [];
    },*/

    getCapteurbyIdOfPont: (state) =>(capteurId) =>{
        return state.capteursInPont.find(capteur => capteur.id == capteurId)
    }
}

const actions = {
    getCapteurs({ commit },id_pont){
        CapteurService.getCapteursPont(id_pont)
        .then(response => {
            console.log(response.data);
            commit('setCapteursInPont', response.data);
        }) .catch(error => console.error("Erreur lors de la récupération des capteurs dans un pont",error));
    },


    getCapteur({commit},id_capteur){
        CapteurService.getCapteur(id_capteur)
        .then(response => {
            console.log("Capteur :",response.data);
            commit('setCapteur', response.data);
        }) .catch(error => console.error("Erreur lors de la récupération du capteur",error));
    },

    createCapteurAnalog({ commit },capteur){
        CapteurService.createAnalogique(capteur)
        .then(response => {
            commit('newCapteur',response.data);
        }) .catch(error => console.log("erreur creation capteur",error));
    },
    createCapteurNum({commit},capteur){
        CapteurService.createNumerique(capteur)
        .then(response => {
            commit('newCapteur',response.data);
        }) .catch(error => console.log("erreur creation capteur",error));
    },

    updateCapteurAnalog({commit},capteur){
        CapteurService.updateAnalogique(capteur)
        .then(response => {
            commit('updateCapteur',response.data);
        }) .catch(error => console.log("erreur modification capteur",error));
    },
    updateCapteurNum({commit},capteur){
        CapteurService.updateNumerique(capteur)
        .then(response => {
            commit('updateCapteur',response.data);
        }) .catch(error => console.log("erreur modification capteur",error));
    },
    
    deleteCapteur({ commit },id){
        CapteurService.delete(id).then( ()=> {
            commit('deleteCapteur',id);
        });
    }
}

const mutations = {
    setCapteursInPont(state,capteurs){
        state.capteursInPont = capteurs;
    },

    setCapteur(state,capteur){
        state.capteur = capteur;
    },

    setReleves(state, { capteurId, releves }) {
        state.capteurReleves = state.capteurReleves || {};
        state.capteurReleves[capteurId] = releves;
        localStorage.setItem(`capteurReleves_${capteurId}`, JSON.stringify(releves));
    },

    newCapteur(state,capteur){
        state.capteursInPont.push(capteur);
    },
    updateCapteur (state,capteur){
        if (capteur) {
            const index = state.capteursInPont.findIndex(p => p.id == capteur.id);
            if (index !== -1) {
              state.capteursInPont.splice(index, 1, capteur);
            }
          }
    },
    deleteCapteur(state,id){
        state.capteursInPont.splice(state.capteursInPont.findIndex(p => p.id == id),1);
    } 
}

export default{
    namespaced:true,
    state,
    getters,
    actions,
    mutations
}