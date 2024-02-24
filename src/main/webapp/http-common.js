import axios from 'axios'

export default axios.create({
    baseURL: process.env.QUARKUS_API_BASE || "http://localhost:8887",
    headers:{
        "Content-Type": "application/json",
        "Authorization": "Bearer " + sessionStorage.getItem('vue-token')
    }
});