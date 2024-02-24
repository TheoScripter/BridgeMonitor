import http from '../../http-common'

class PontService{

    getAll(){
        return http.get("/ponts");
    }

    getPont(id){
        return http.get(`/ponts/${id}`)
    }

    getCapteur(id){
        return http.get(`/ponts/${id}/capteurs`)
     }

    getCapteurNumerique(id){
        return http.get(`/ponts/${id}/capteurs/numeriques`)
    }

    getCapteurAnalogique(id){
        return http.get(`/ponts/${id}/capteurs/analogiques`)
    }

    getHistorique(id){
        return http.get(`/ponts/${id}/historique`)
    }

    createPont(data){
        return http.post("/ponts",data)

    }
    update(pont){
        return http.put("/ponts",pont)
    }

    delete(id){
        return http.delete(`/ponts/${id}`)
    }
}

export default new PontService();