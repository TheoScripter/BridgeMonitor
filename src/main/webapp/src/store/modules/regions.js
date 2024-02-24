import RegionService from "@/services/RegionService";

//initial state
const state= {
    allRegions: [],
    pontsInRegion: [],
    regionUnique: []
}

//Getters
const getters = {
    getAllRegions: state => {
        return state.allRegions;
    },
    getPontsInRegion: state1 => {
        return state1.pontsInRegion;
    },
    getRegionUnique: state2 => {
        return state2.regionUnique;
    }

}

//Actions
const actions = {
    getRegions({ commit }){
        RegionService.getAll().then(response => {
            console.log(response.data);
            commit('setAllRegions',response.data);
        });
    },
    getRegion({ commit },regionId){
        RegionService.get(regionId).then(response => {
            console.log(response.data);
            commit('setRegion',response.data);
        });
    },
    getPonts({ commit },regionId){
        RegionService.getPonts(regionId)
            .then(response => {
                commit('setPontsInRegion', response.data);
            })
            .catch(error => {
                console.error("Erreur lors de la récupération des ponts dans la région :", error);
            });
    },

    createRegion ({ commit },region){
        RegionService.create(region).then(response => {
            commit('newRegion',response.data);
        }).catch(error => console.log(error));
    },
    updateRegion({ commit },region){
        RegionService.update(region).then( response =>{
            commit('updateRegion',response.data);
        });
    },
    deleteRegion({ commit },id){
        RegionService.delete(id).then( () =>{
            commit('deleteRegion',id);
        });
    }
}

//Mutations
const mutations = {
    setAllRegions (state,regions){
        state.allRegions = regions;
    },

    setRegion (state2,region){
        state2.regionUnique = region;
    },

    setPontsInRegion(state1,ponts) {
        state1.pontsInRegion = ponts;
    },

    newRegion(state,region){
        state.allRegions.push(region);
    },
    updateRegion (state,region){
        state.allRegions.splice(state.allRegions.findIndex(m => m.id == region.id),1,region);
    },
    deleteRegion(state,id){
        state.allRegions.splice(state.allRegions.findIndex(m => m.id == id),1);
    }
}


export default{
    namespaced:true,
    state,
    getters,
    actions,
    mutations
}