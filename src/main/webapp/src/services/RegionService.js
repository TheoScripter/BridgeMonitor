import http from '../../http-common'

class RegionService{
    getAll(){
        return http.get("/regions");
    }

    get(id){
        return http.get(`/regions/${id}`);
    }

    getPonts(id){
        return http.get(`/regions/${id}/ponts`);
    }

    create(data){
        return http.post("/regions",data);
    }

    update(data){
        return http.put("/regions",data);
    }

    delete(id){
        return http.delete(`/regions/${id}`);
    }
}

export default new RegionService();