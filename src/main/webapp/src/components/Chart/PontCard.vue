
<template>
   
    <v-container fluid>
            
                    <v-card v-if="pont">
                    <v-card-title>{{ pont.nom }}</v-card-title>
                    <v-card-subtitle v-if="pont.region">{{ pont.region.nom }}</v-card-subtitle>
                    <v-card-text>
                        Dimensions (m) : {{ pont.longueur }} / {{ pont.largeur }} <br>
                        Position (lat,long) : {{ pont.latitude }}, {{ pont.longitude }}
                    </v-card-text>

                    <v-col cols="10">
                        <v-select
                            v-if="pont.etat"
                            v-model="selectedEtat"
                            :items="etats"
                            label="État du pont"
                            item-text="nom"
                            item-value="id"
                        ></v-select>
                    </v-col>
                    </v-card>
                
                    <v-timeline side="end" align="start" dense>
                        <v-timeline-item v-for="item in historique" :key="item.id" :color="getCircleColor(item)" size="small" >
                                {{item.etat.nom}}<br>
                                {{item.dateTimeChangement}}
                        </v-timeline-item>          
                    </v-timeline>
    </v-container>
</template>

<style scoped>

</style>

<script>
//import VueApexCharts from 'vue-apexcharts';
    export default {
        name: "PontCard",
        
        data() {
            return{
            //pont
                pont_id:0,
                pont: 0,
                
            //Etat et modification de l'etat
                etats:[],
                selectedEtat: null,
                editedPont:{
                    id: 0,
                    nom: ".",
                    longueur: 0,
                    largeur: 0,
                    latitude: "string",
                    longitude: "string",
                    dateCreation: "2022-03-10",
                    etatId: 0,
                    regionId: 0,
                    capteursId: [
                        0
                    ]
                },

            //Chart historique
                historique:[],
            }
            
        },

        mounted(){
            this.pont_id = this.$route.params.id_pont;
            this.$store.dispatch('ponts/getPontById', this.pont_id);
            this.$store.dispatch('etats/getallEtats');
            this.$store.dispatch('etats/getHistoriquePontById',this.pont_id)
        },

        computed: {
            pont1(){
                return this.$store.getters['ponts/getFullPont'];
            },
            etats1(){
                return this.$store.getters['etats/getEtats'];
            },
            historique1(){
                return this.$store.getters['etats/getHistoriquePont'];
            }
           
        },

        watch: {
            pont1:function(pont){
                this.pont = pont;
                this.selectedEtat = this.pont.etat.id;
                this.watchEtat = true;
                
            },
            etats1: function(etats){
                this.etats = etats;
            },
            selectedEtat(newstate, oldstate){
                if (newstate !== oldstate && oldstate !== null) {
                    console.log("old state",oldstate);
                    console.log("new state",newstate);
                    this.changeEtat(newstate);
                }
            },
            historique1:function(historique){
                console.log("HISTORIQUE", historique);
                 const sortedHistorique = historique.slice().sort((a, b) => {
                    const dateA = new Date(a.dateTimeChangement);
                    const dateB = new Date(b.dateTimeChangement);
                    return dateB - dateA; 
                });

                this.historique = sortedHistorique; //historique trié
            },
        },
        

        methods: {
            async changeEtat(etat_id) {
                 this.editedPont = {
                    id: this.pont.id,
                    nom: this.pont.nom,
                    longueur: this.pont.longueur,
                    largeur: this.pont.largeur,
                    latitude: this.pont.latitude,
                    longitude: this.pont.longitude,
                    dateCreation: this.pont.dateCreation,
                    etatId: etat_id, //changement d'etat
                    regionId: this.pont.region.id,
                    capteursId: this.pont.capteurs ? this.pont.capteurs.map(capteur => capteur.id || 0) : []                 
                }
                await this.$store.dispatch("ponts/updatePont",this.editedPont);
                
                await new Promise(resolve => setTimeout(resolve, 800)); //Attendre que l'historique soit changé dans la base

                await this.$store.dispatch('etats/getHistoriquePontById',this.pont_id); //On 'get' l'historique mis à jour
            },


            getCircleColor(item) {
                if (item.etat.id == 1) {
                    return 'orange'; 
                } else if (item.etat.id == 2){
                    return 'green';
                }
                else {
                    return 'blue';
                }
            }
        },

    };
</script>
