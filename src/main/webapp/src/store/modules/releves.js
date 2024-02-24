import ReleveService from "@/services/ReleveService";

//initial state
const state={
    allReleves: [],
    relevesOfCapteur: {},
    relevesCSV : null
}

//Getters
const getters = {
    getAllReleves: state => {
        return state.allReleves;
    },
    getReleveByCapteurId: state => (capteurId) => {
        return state.relevesOfCapteur[capteurId];
    }
//
}

//Actions
const actions = {
    getReleves({ commit }){
        ReleveService.getAll().then(response => {
            commit('setReleves',response.data);
        });
    },
    getRelevesOfCapteur({ commit }, capteurId){
        ReleveService.getRelevesOfCapteur(capteurId).then(response => {
            commit('addRelevesOfCapteur', {capteurId : capteurId , data:response.data});
        })
    },
    async downloadRelevesOfCapteur({commit}, capteurId){

        // axios({
        //     method: 'get',
        //     url: `http://localhost:8887/capteurs/${capteurId}/releves`,
        //     headers: {
        //         "Content-Type": "text/csv"
        //     }
        // }).then(response => {
        //     commit('setRelevesCSV', response.data)
        // })
        // marche pas pour ça on va utiliser fetch

        var fileName

        fetch((process.env.QUARKUS_API_BASE || "http://localhost:8887")+`/capteurs/${capteurId}/releves`, {
            method:'GET',
            headers: {
                "Content-Type": "text/csv",
                "Authorization": "Bearer " + sessionStorage.getItem('vue-token')
            },
        }).then((response) => {
            fileName = response.headers.get('Content-Disposition').split("filename=")[1]
            return response.blob();
        }).then((blob) => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.style.display = "none";
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        }).catch((e) => 
        {
            console.warn(e)
        });

        commit('setRelevesCSV', "hello")
            
        

    }
}

//Mutations
const mutations = {
    setReleves (state,releves){
        state.allReleves = releves;
    },
    addRelevesOfCapteur(state, {capteurId,data}){
        console.log(capteurId,':',data);
        state.relevesOfCapteur = {
            ...state.relevesOfCapteur, // Clonage de l'objet state.relevesOfCapteur
            [capteurId]: data // Réaffectation de la nouvelle valeur pour la clé capteurId
        };
    },
    setRelevesCSV(state, data){
        console.log("csv data", data)
        state.relevesCSV = data
    }
}


export default{                                                                                                                     
    namespaced:true,
    state,
    getters,
    actions,
    mutations
}