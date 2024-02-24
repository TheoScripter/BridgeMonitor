import http from '../../http-common'

class CapteurService{


    getCapteursPont(id){
        return http.get(`/ponts/${id}/capteurs`);
    }

    getCapteur(id){
        return http.get(`/capteurs/${id}`);
    }

    getReleve(id){
        return http.get(`/capteurs/${id}/releves`);
    }

    createAnalogique(data){
        return http.post("/capteurs/analogiques",data);
    }
    createNumerique(data){
        return http.post("/capteurs/numeriques",data);
    }

    updateAnalogique(data){
        return http.put("/capteurs/analogiques",data);
    }
    updateNumerique(data){
        return http.put("/capteurs/numeriques",data);
    }

    delete(id){
        return http.delete(`/capteurs/${id}`);
    }
}

export default new CapteurService();