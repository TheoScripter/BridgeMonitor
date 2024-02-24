<template>
  <Bar :options="chartOptions" :data="chartData"/>
</template>

<script>
  import { Bar } from 'vue-chartjs'
  import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
  import {isToday, isThisWeek, isThisMonth, isThisYear} from 'date-fns'

  ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

  export default {
    components: { Bar },
    props: {
      capteurId: {
        type: Number,
        required: true
      },
      filter: String
    },
    data() {
      return {
        capteur:0,
        nomCapteur:"",
        
        labels: [],
        datasets: [{
          backgroundColor: '#3e50b5',
          data: []
        }],
        
        chartOptions : {}
      }
    },


    async beforeMount(){
      await this.$store.dispatch('capteurs/getCapteurs',this.$route.params.id_pont);
      //await this.$store.dispatch('capteurs/getCapteurs',this.$route.params.id_pont);

      await this.$store.dispatch('releves/getRelevesOfCapteur',this.capteurId);

      // new Promise(resolve => setTimeout(resolve, 1000)); //C'est sal mais pour l'instant ca marche
      this.fetchDataN();
    },


    mounted(){

    //var capteur_temp = this.$store.getters['capteurs/getCapteurbyIdOfPont'](this.capteurId);
   // capteur_temp = this.$store.getters['capteurs/getCapteurbyIdOfPont'](this.capteurId);
  
      //this.fetchDataN();
      //this.startInterval();
    },

    computed:{
      capteur1(){
        return this.$store.getters['capteurs/getCapteurbyIdOfPont'](this.capteurId);
      },

      chartData1() {
        return this.$store.getters["releves/getReleveByCapteurId"](this.capteurId);
      },
      chartData(){
        return {labels:this.labels, datasets: this.datasets}
      },
    },

    methods:{
      async fetchDataN(){
        await this.$store.dispatch('releves/getRelevesOfCapteur', this.capteurId);
      },

      startInterval(){
        this.intervalId = setInterval(async () => {
          await this.fetchDataN();
        }, 5000); 
      },

      filterData(data, filter){

        switch (filter) {
          case "today":
            return data.filter((releve) => {return isToday(releve.dateTimeReleve)})

          case "thisWeek":
            return data.filter((releve) => {return isThisWeek(releve.dateTimeReleve)})

          case "thisMonth":
            return data.filter((releve) => {return isThisMonth(releve.dateTimeReleve)})

          case "thisYear":
            return data.filter((releve) => {return isThisYear(releve.dateTimeReleve)})

          default:
            return []
        }
      },

      gestion(){
        if (this.chartData1 && this.chartData1.length > 0) {
            var xaxis = []
            var yaxis = []
            const data = this.chartData1;

            const filteredData = this.filterData(data, this.filter);
            console.log('fetch data releves numeriques :',data);
            console.log('fetch releves numeriques :',filteredData);

            const sortedData = filteredData.sort((a, b) => {
              const dateA = new Date(a.dateTimeReleve);
              const dateB = new Date(b.dateTimeReleve);
              return dateA - dateB; 
            });


            for (let i = 0; i < sortedData.length; i++) {
              var time = new Date(sortedData[i].dateTimeReleve)
              xaxis.push(time.toLocaleString('fr-FR') + "." + time.getMilliseconds());
              yaxis.push(sortedData[i].valeur ? 1 : 0);
            }

            this.labels = xaxis
            this.datasets = [{
              backgroundColor: '#3e50b5',
              data: yaxis
            }]
        }
      },
    },


    watch : {
      chartData1: function(){
        this.gestion();
      },
      filter: function(){
        this.gestion();
      },


      capteur1: function(capteur){
           this.chartOptions = {
              plugins: {
                  title: {
                    display: true,
                    text : `${capteur.nom}`
                },
                legend: {
                  display: false
                }
              },
              responsive: true,
              maintainAspectRatio: false,
              scales : {
                // x: {
                //   ticks : function(dateString){
                //     return new Date(dateString)
                //   }
                // },
                y: {
                  ticks: {
                    callback : function(value){
                      if(value == 1){
                        return capteur.uniteNumerique.etatHaut
                      }
                      if(value == 0){
                        return capteur.uniteNumerique.etatBas
                      }
                    }
                  }
                }
              }
            
          
            }
          }
      }
  }
  


</script>

<style>

</style>
