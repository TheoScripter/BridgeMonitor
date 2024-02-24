<template>
  <LineChartGenerator :options="chartOptions" :data="chartData"/>
</template>

<script>
import { Line as LineChartGenerator } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement } from 'chart.js'
import {isToday, isThisWeek, isThisMonth, isThisYear} from 'date-fns'

ChartJS.register(Title, Tooltip, Legend, LineElement, LinearScale, CategoryScale, PointElement)

export default {
  components: { LineChartGenerator },
  props: {
    capteurId: {
      type: Number,
      required: true
    },
    filter: String,
  },
  data() {
    return {

      labels: [],
        datasets: [{
          backgroundColor: '#3e50b5',
          data: []
        }],
        
        chartOptions : {          
          maintainAspectRatio: false,
        }
     
    }
  },

  async beforeMount(){
    await this.$store.dispatch('capteurs/getCapteurs',this.$route.params.id_pont);
    //await this.$store.dispatch('capteurs/getCapteurs',this.$route.params.id_pont);
    //new Promise(resolve => setTimeout(resolve, 1000)); //C'est sal mais pour l'instant ca marche
    this.fetchDataA();
  },


  mounted(){
    
    var capteur_temp = this.$store.getters['capteurs/getCapteurbyIdOfPont'](this.capteurId);
    capteur_temp = this.$store.getters['capteurs/getCapteurbyIdOfPont'](this.capteurId);
    console.log("CAPTEUR ANALOG",capteur_temp);
     
    //this.fetchDataA();
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
      async fetchDataA(){
          await this.$store.dispatch('releves/getRelevesOfCapteur', this.capteurId);
      },

      startInterval(){
        this.intervalId = setInterval(async () => {
          await this.fetchDataA();
        }, 5000); 
      },

      filterData(releves, filter){
        console.log(releves);
        console.log("type de releves",typeof(releves));
        switch (filter) {
          case "today":
            return releves.filter((releve) => {return isToday(releve.dateTimeReleve)})

          case "thisWeek":
            return releves.filter((releve) => {return isThisWeek(releve.dateTimeReleve)})

          case "thisMonth":
            return releves.filter((releve) => {return isThisMonth(releve.dateTimeReleve)})

          case "thisYear":
            return releves.filter((releve) => {return isThisYear(releve.dateTimeReleve)})

          default:
            return []
        }
      },

      gestion(){
        if (this.chartData1 && this.chartData1.length > 0) {
          
          var xaxis = []
          var yaxis = []
          const releves = this.chartData1;
        
          const filteredData = this.filterData(releves, this.filter)
          console.log('fetch data releves analogiques :',releves);
          console.log('fetch releves analogiques :',filteredData);

          const sortedData = filteredData.sort((a, b) => {
            const dateA = new Date(a.dateTimeReleve);
            const dateB = new Date(b.dateTimeReleve);
            return dateA - dateB; 
          });


          for (let i = 0; i < sortedData.length; i++) {
            var time = new Date(sortedData[i].dateTimeReleve)
            xaxis.push(time.toLocaleString('fr-FR') + "." + time.getMilliseconds());
            yaxis.push(sortedData[i].valeur);
          }

          this.labels = xaxis
          this.datasets = [{
            label:'',
            backgroundColor: '#3e50b5',
            data: yaxis
          }]

        }
      },

    },

    watch: {
      chartData1: function(){  
        this.gestion();
      },

      filter: function(){
        this.gestion();
      },


      capteur1: function(capteur){
        this.chartOptions= {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            title: {
              display: true,
              text: `${capteur.nom} (${capteur.uniteAnalogique.symbole})`,
            },
            legend: {
              display: false
            }
          },
          
        };
      }
    },
  
};
</script>
