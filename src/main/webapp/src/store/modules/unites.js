import UniteService from "@/services/UniteService";

const state = {
    unitesA : [],
    unitesN : [],
}

const getters = {
    getUnitesA: state => {
        return state.unitesA;
    },
    getUnitesN: state => {
        return state.unitesN;
    }
    
}

const actions = {
    
    getUnitesAnalogiques({commit}){
        UniteService.getAllAnalogiques().then(response => {
            console.log(response.data);
            commit('setUnitesA',response.data);
        });
    },
    getUnitesNumeriques({commit}){
        UniteService.getAllNumeriques().then(response => {
            console.log(response.data);
            commit('setUnitesN',response.data);
        });
    },

}

const mutations = {
    setUnitesA (state,unites){
        state.unitesA = unites;
    },
    setUnitesN (state,unites){
        state.unitesN = unites;
    }
}

export default{
    namespaced:true,
    state,
    getters,
    actions,
    mutations
}