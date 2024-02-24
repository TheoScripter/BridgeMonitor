<template>
  <v-card class="elevate">
    <v-card-title class="indigo grey--text text--lighten-3">
      <h3>Regions France</h3>
    </v-card-title>

    <v-card-text>
      <v-list>
        <v-list-item-group>
          <v-list-item v-for="region in regions" :key="region.id" :to="{ name: 'OverViewPont', params: { id_region: region.id } }">
            <v-list-item-content class="d-flex align-center">
              <v-list-item-title class="region-link" @mouseover="onMouseOver(region.id)" @mouseleave="onMouseLeave">
                {{region.nom + ' (' + region.ponts.length + (region.ponts.length === 1 ? ' pont' : ' ponts') + ')'}}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: "ListeRegions",

  data() {
    return {
      regions: [],
      highlightedRegion: null,
    };
  },

  computed: {
    regions1() {
      return this.$store.getters["regions/getAllRegions"];
    },
  },

  methods: {
    /*redirectToRegionDetail(regionId) {
      this.$router.push({ name: "ListeRegions", params: { id: regionId } });
    },
  */
    onMouseOver(regionId) {
      this.highlightedRegion = regionId;
    },

    onMouseLeave() {
      this.highlightedRegion = null;
    },
  },

  watch: {
    regions1(regions1) {
      this.regions = regions1;
    },
  },

  mounted() {
    this.$store.dispatch("regions/getRegions");
  },
};
</script>

<style scoped>
.elevate {
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
}

.blue.darken-2 {
  background-color: #3e50b5 !important;
}

.region-link {
  color: #2196F3;
  transition: color 0.3s; /* Ajout d'une transition pour l'effet de survol */
}

.region-link:hover {
  color: #64B5F6; /* Changement de couleur au survol */
}

.align-center {
  align-items: center;
}
</style>
