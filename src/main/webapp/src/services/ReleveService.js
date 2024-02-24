import http from '../../http-common'

class ReleveService{
    getAll(){
        return http.get("/releves");
    }

    get(id){
        return http.get(`/releves/${id}`);
    }

    getRelevesOfCapteur(capteurId){
        return http.get(`/capteurs/${capteurId}/releves`)
    }                                                                                                                                                                                                                                                                                                                                          
}

export default new ReleveService();