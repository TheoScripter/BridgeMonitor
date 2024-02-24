

<template>
  <v-card class="custom-card elevate">

    <v-data-table :key="ponts.id" :headers="headers" :items="ponts" item-key="id" @click:row="redirectToDashboard"></v-data-table>
    
   <!-- 
    <v-card-title class="text-center indigo text--lighten-3">
      <h3>Ponts</h3>
    </v-card-title>

    <v-card-text>
      <v-simple-table>
        <template v-slot:default>
          <thead>
            <tr>
              <th>Nom</th>
              <th>Longueur</th>
              <th>Largeur</th>
              <th>Latitude</th>
              <th>Longitude</th>
              <th>Date de création</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="pont in ponts" :key="pont.id" @click="redirectToDashboard(pont.id)">
              <td>{{ pont.nom }}</td>
              <td>{{ pont.longueur }}</td>
              <td>{{ pont.largeur }}</td>
              <td>{{ pont.latitude }}</td>
              <td>{{ pont.longitude }}</td>
              <td>{{ pont.dateCreation }}</td>
            </tr>
          </tbody>
        </template>
      </v-simple-table>
    </v-card-text>
    -->

  </v-card>
</template>



<style scoped>

tbody tr {
  cursor: pointer;
}

.custom-card {
  margin: 20px;
}

.elevate {
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
}

.indigo {
  background-color: #3e50b5 !important;
  color: #ffffff !important;
}
</style>



<script>
export default {
  name: "ListePonts",
  data() {
    return {
      ponts: [],

      headers: [
        { text: "Nom", sortable: true, value: "nom" },
        { text: "Longueur", sortable: true, value: "longueur" },
        { text: "Largeur", sortable: true, value: "largeur" },
        { text: "Latitude", sortable: true, value: "latitude" },
        { text: "Longitude", sortable: true, value: "longitude" },
        { text: "Date de création", sortable: true, value: "dateCreation" },
      ],
    };
  },
  computed: {
    ponts1() {
      return this.$store.getters["regions/getPontsInRegion"];
    },
  },
  watch: {
    ponts1(ponts) {
      this.ponts = ponts;
      console.log(ponts);
    },

  },
  mounted() {
    const routeID = this.$route.params.id_region;
    this.$store.dispatch("regions/getPonts", routeID);
    this.region_ID = routeID;
  },

  methods: {
    redirectToDashboard(event) {
      console.log(event)
      this.$router.push({ name : 'DashBoard', params:{id_pont: event.id} });
    },
  }
};
</script>
