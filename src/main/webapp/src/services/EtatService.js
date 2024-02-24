import http from '../../http-common'

class EtatService{

    getEtats(){
        return http.get(`/etats`);
    }

    getHistoriqueEtat(id){
        return http.get(`/ponts/${id}/historique`);
    }

}

export default new EtatService();