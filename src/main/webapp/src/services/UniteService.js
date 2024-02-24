import http from '../../http-common'

class UniteService{
    getAllAnalogiques(){
        return http.get("/unitesanalogiques");
    }

    getAllNumeriques(){
        return http.get("/unitesnumeriques");
    }
}

export default new UniteService();