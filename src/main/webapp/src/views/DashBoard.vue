<template>
  <v-container fluid>
    <v-row>
      <v-col cols="3">
          <FilterDialog @apply-filter="applyFilter" />
          <PontCard/>
      </v-col>
      <v-col cols="8">

        <v-carousel hide-delimiters class="custom-carousel">
          <v-carousel-item v-for="capteur in capteursAnalogiquesData" :key="capteur.id">
              <v-btn @click="downloadRelevesOfCapteur(capteur.id)"><i class="mdi mdi-export"></i></v-btn>
              <ChartLine :capteurId="capteur.id" :filter="filter"></ChartLine>
          </v-carousel-item>
        </v-carousel>

        <v-carousel hide-delimiters class="custom-carousel">
          <v-carousel-item v-for="capteur in capteursNumeriquesData" :key="capteur.id">
            <v-btn @click="downloadRelevesOfCapteur(capteur.id)"><i class="mdi mdi-export"></i></v-btn>
            <ChartBar :capteurId="capteur.id" :filter="filter"></ChartBar>
          </v-carousel-item>
        </v-carousel>

      </v-col>
      
    </v-row>
  </v-container>
</template>


<style scoped>
.custom-carousel {
  border: 1px solid #221f1f;
}
</style>


<script>
import ChartLine from "@/components/Chart/ChartLine.vue";
import ChartBar from "@/components/Chart/ChartBar.vue";
import FilterDialog from "@/components/Chart/FilterDialog.vue";
import PontCard from "@/components/Chart/PontCard.vue";

export default {
  components: {
    ChartLine,
    ChartBar,
    FilterDialog,
    PontCard
  },
  data() {
    return {
      pont_ID: null,
      capteursAnalogiquesData: [],
      capteursNumeriquesData: [],
      filter : "today",
      etats: [],
    };
  },
  computed: {
    capteurAnalogique() {
      return this.$store.getters["ponts/getPontWithCapteursAnalogiques"];
    },
    capteurNumerique() {
      return this.$store.getters["ponts/getPontWithCapteursNumeriques"];
    },
    csvFileToDownload(){
      return this.$store.getters["releves/relevesCSV"];
    }
  },
  watch: {
    capteurAnalogique(capteur) {
      this.capteursAnalogiquesData = capteur;
    },
    capteurNumerique(capteur) {
      this.capteursNumeriquesData = capteur;
    },
  },
  mounted() {
    this.$store.dispatch('capteurs/getCapteurs',this.$route.params.id_pont); //necessaire pour Chartline et CharBar
    this.$store.dispatch("ponts/getPontCapteursAnalogiques", this.$route.params.id_pont);
    this.$store.dispatch("ponts/getPontCapteursNumeriques", this.$route.params.id_pont);
  },
  methods: {
    applyFilter(filter) {
      console.log('Filtre appliqué :', filter);
      this.filter = filter
    },
    async downloadRelevesOfCapteur(capteurId){
      console.log(capteurId)
      await this.$store.dispatch("releves/downloadRelevesOfCapteur", capteurId)
    }
  },
};
</script>

